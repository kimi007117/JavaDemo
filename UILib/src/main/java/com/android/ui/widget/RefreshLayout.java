package com.android.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by lijie on 2019-08-17.
 */
public class RefreshLayout extends ViewGroup implements NestedScrollingParent {

    private static final String TAG = "RefreshLayout";

    private Context context;

    private static final float DRAG_RATE = 0.5f;
    private static final int INVALID_POINTER = -1;

    // scroller duration
    private static final int SCROLL_TO_TOP_DURATION = 800;
    private static final int SCROLL_TO_REFRESH_DURATION = 250;
    private static final long SHOW_COMPLETED_TIME = 500;

    private View contentView;
    private int currentContentOffsetY; // contentView/header偏移距离

    private boolean hasMeasureHeader;   // 是否已经计算头部高度
    private int touchSlop;
    private int headerHeight;       // header高度
    private int distanceForRefreshStart;  // 需要下拉这个距离才进入松手刷新状态，默认和header高度一致
    private int distanceForSecondFloorStart;  // 需要下拉这个距离才进入二楼状态，默认和header高度一致
    private int activePointerId;
    private boolean hasSendCancelEvent;
    private float lastMotionX;
    private float lastMotionY;
    private float initDownY;
    private float initDownX;
    private static final int START_POSITION = 0;
    private MotionEvent lastEvent;
    private boolean mIsBeginDragged;
    private AutoScroll autoScroll;
    private State state = State.DONE;
    private OnRefreshListener refreshListener;
    private boolean isAutoRefresh;

    private boolean pullRefreshEnable = true;
    private boolean secondFloorEnable = true;
    private boolean childHorizontalDragging;

    private int autoRefreshDelay = 1500;

    protected IPullDownLayoutHolder mHolder;
    private final NestedScrollingParentHelper mParentHelper;

    // 刷新成功，显示500ms成功状态再滚动回顶部
    private Runnable delayToScrollTopRunnable = new Runnable() {
        @Override
        public void run() {
            Log.v("refreshanima", "refreshComplete -- > CLOSING_ALL");
            changeState(State.CLOSING_ALL);
            autoScroll.scrollTo(START_POSITION, SCROLL_TO_TOP_DURATION);
        }
    };

    private Runnable delayToScrollBottomRunnable = new Runnable() {
        @Override
        public void run() {
            autoScroll.scrollTo(getScreenHeight(), SCROLL_TO_TOP_DURATION);
        }
    };

    private Runnable autoRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            // 标记当前是自动刷新状态，finishScroll调用时需要判断
            // 在actionDown事件中重新标记为false
            isAutoRefresh = true;
            isWaitingRefreshDelay = true;
            Log.v("refreshanima", "autoRefreshRunnable -- > PULL_TO_REFRESH");
            changeState(State.PULL_TO_REFRESH);
            autoScroll.scrollTo(distanceForRefreshStart, SCROLL_TO_REFRESH_DURATION);

            postDelayed(autoRefreshDelayRunnable, autoRefreshDelay);
        }
    };

    private Runnable autoRefreshDelayRunnable = new Runnable() {
        @Override
        public void run() {
            isWaitingRefreshDelay = false;
            if (!isWaitingRefreshComplete) {
                refreshComplete();
            }
        }
    };
    private boolean isWaitingRefreshDelay;
    private boolean isWaitingRefreshComplete;

    public RefreshLayout(Context context) {
        this(context, null);
        this.context = context;
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        autoScroll = new AutoScroll();
        mParentHelper = new NestedScrollingParentHelper(this);
    }

    /**
     * 设置自定义header
     */
    public void setRefreshElastic(IPullDownLayoutHolder holder) {
        mHolder = holder;

        View refreshHeaderView = getRefreshHeaderView();
        // 为header添加默认的layoutParams
        LayoutParams layoutParams = refreshHeaderView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, holder.getRefreshStartHeight());
            refreshHeaderView.setLayoutParams(layoutParams);
        }
        addView(refreshHeaderView);
    }

    public int dipToPixel(float dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public void setRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void refreshComplete() {
        isWaitingRefreshComplete = false;
        if (isWaitingRefreshDelay) {
            return;
        }
        State currentState = state;
        // if refresh completed and the contentView at top, change state to reset.
        if (currentContentOffsetY == START_POSITION) {
            changeState(State.DONE);
        } else {
            postDelayed(delayToScrollTopRunnable, SHOW_COMPLETED_TIME);
        }
        notifyLintenerComplete(currentState);
    }

    public void autoRefresh() {
        autoRefresh(500);
    }

    /**
     * 在onCreate中调用autoRefresh，此时View可能还没有初始化好，需要延长一段时间执行。
     *
     * @param duration 延时执行的毫秒值
     */
    public void autoRefresh(long duration) {
        if (state != State.DONE) {
            return;
        }
        postDelayed(autoRefreshRunnable, duration);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (contentView == null) {
            ensureTarget();
        }

        if (contentView == null) {
            return;
        }

        // ----- measure contentView -----
        // target占满整屏
        contentView.measure(MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));

        // ----- measure refreshView-----
        measureChild(mHolder.getRefreshHeaderView(), widthMeasureSpec, heightMeasureSpec);
        if (!hasMeasureHeader) { // 防止header重复测量
            hasMeasureHeader = true;
            headerHeight = mHolder.getRefreshStartHeight(); // header高度
            Log.v(TAG, "headerHeight:" + headerHeight);
            distanceForRefreshStart = headerHeight;   // 需要pull这个距离才进入松手刷新状态
            distanceForSecondFloorStart = mHolder.getSecondFloorStartHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }

        if (contentView == null) {
            ensureTarget();
        }
        if (contentView == null) {
            return;
        }

        // target铺满屏幕
        final View targetView = this.contentView;
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop() + currentContentOffsetY;
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        targetView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        // header放到target的上方，水平居中
        View refreshHeaderView = getRefreshHeaderView();
        refreshHeaderView.layout(0, 0, width, childTop);
    }

    /**
     * 将第一个Child作为target
     */
    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (contentView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != null && !child.equals(getRefreshHeaderView())) {
                    contentView = child;
                    break;
                }
            }
        }
    }

    /**
     * 下拉松手后，加载过程中设置为False。以防止状态错乱。
     * 直到加载完成，动画执行完毕，设置为True。
     * 其它有必要屏蔽事件的地方谨慎调用。
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        this.pullRefreshEnable = enable;
        if (!pullRefreshEnable) {
            secondFloorEnable = false;
        }
    }

    public void setSecondFloorEnable(boolean enable) {
        secondFloorEnable = enable;
        if (secondFloorEnable) {
            pullRefreshEnable = enable;
        }
    }

    public boolean isSecondFloorEnable() {
        return secondFloorEnable;
    }

    public State getState() {
        return state;
    }

    /**
     * 子View横向滑动时，屏蔽事件截获、分发。
     * 由于重写了dispatchTouchEvent方法，导致子View requestParentDisallowInterceptTouchEvent( false ) 无效。（见ViewGroup源码）
     * 所以需要自定义一个单独的flag，以免影响另外两个flag: pullRefreshEnable、enable
     *
     * @param dragging
     */
    public void setChildrenHorizenDragging(boolean dragging) {
        this.childHorizontalDragging = dragging;
    }

    private int getTargetOffsetY() {
        return contentView == null ? 0 : contentView.getTop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getTargetOffsetY() > 0 && (state != State.REFRESHING || state == State.SECOND_FLOOR_SHOWING)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!pullRefreshEnable) {
            Log.i(TAG, "dispatchTouchEvent.pullRefreshEnable=false");
            return super.dispatchTouchEvent(ev);
        }

        if (childHorizontalDragging) {
            return super.dispatchTouchEvent(ev);
        }

        if (!isEnabled() || contentView == null) {
            return super.dispatchTouchEvent(ev);
        }

        final int actionMasked = ev.getActionMasked(); // support Multi-touch
        try {
            switch (actionMasked) {
                case MotionEvent.ACTION_DOWN: {
                    activePointerId = ev.getPointerId(0);
                    isAutoRefresh = false;
                    hasSendCancelEvent = false;
                    mIsBeginDragged = false;
                    currentContentOffsetY = getTargetOffsetY();
                    initDownX = lastMotionX = ev.getX(0);
                    initDownY = lastMotionY = ev.getY(0);
                    autoScroll.stop();
                    removeCallbacks(delayToScrollTopRunnable);
                    removeCallbacks(delayToScrollBottomRunnable);
                    removeCallbacks(autoRefreshRunnable);
                    boolean touchEvent = super.dispatchTouchEvent(ev);
                    Log.i(TAG, "dispatchTouchEvent.ACTION_DOWN: " + touchEvent);

                    return true;    // return true，否则可能接受不到move和up事件
                }
                case MotionEvent.ACTION_MOVE: {
                    if (activePointerId == INVALID_POINTER) {
                        Log.v(TAG, "dispatchTouchEvent.ACTION_MOVE: Got ACTION_MOVE event but don't have an active pointer id.");
                        return super.dispatchTouchEvent(ev);
                    }
                    lastEvent = ev;
                    float x = ev.getX(MotionEventCompat.findPointerIndex(ev, activePointerId));
                    float y = ev.getY(MotionEventCompat.findPointerIndex(ev, activePointerId));
                    float yDiff = y - lastMotionY;
                    float offsetY = yDiff * DRAG_RATE;
                    lastMotionX = x;
                    lastMotionY = y;

                    if (!mIsBeginDragged && Math.abs(y - initDownY) > touchSlop) {
                        mIsBeginDragged = true;
                    }

                    if (mIsBeginDragged) {
                        boolean moveDown = offsetY > 0; // ↓
                        boolean canMoveDown = canChildScrollUp();
                        boolean moveUp = !moveDown;     // ↑
                        boolean canMoveUp = currentContentOffsetY > START_POSITION;
                        Log.i(TAG, "dispatchTouchEvent.ACTION_MOVE: moveDown:" + moveDown +
                                " canMoveDown:" + canMoveDown + " moveUp:" + moveUp + " canMoveUp:" + canMoveUp);
                        // 判断是否拦截事件
                        if ((moveDown && !canMoveDown) || (moveUp && canMoveUp)) {
                            moveSpinner(offsetY, false);
                            return true;
                        }
                    }
                    boolean touchEvent = super.dispatchTouchEvent(ev);
                    Log.i(TAG, "dispatchTouchEvent.ACTION_MOVE: " + touchEvent);
                    return touchEvent;
                }
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP: {
                    int offsetTop = getTargetOffsetY();
                    updateStateByOffset(offsetTop, false);
                    finishSpinner();
                    activePointerId = INVALID_POINTER;
                    notifyLintenerStart(state);
                    boolean touchEvent = super.dispatchTouchEvent(ev);
                    Log.i(TAG, "dispatchTouchEvent.ACTION_UP: " + touchEvent);
                    return touchEvent;
                }
                case MotionEvent.ACTION_POINTER_DOWN:
                    int pointerIndex = MotionEventCompat.getActionIndex(ev);
                    if (pointerIndex < 0) {
                        Log.v(TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                        return super.dispatchTouchEvent(ev);
                    }
                    lastMotionX = ev.getX(pointerIndex);
                    lastMotionY = ev.getY(pointerIndex);
                    lastEvent = ev;
                    activePointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    onSecondaryPointerUp(ev);
                    lastMotionY = ev.getY(ev.findPointerIndex(activePointerId));
                    lastMotionX = ev.getX(ev.findPointerIndex(activePointerId));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();    //fuck system bug
        }
        return super.dispatchTouchEvent(ev);
    }

    private void notifyLintenerStart(State state) {
        if (refreshListener == null) {
            return;
        }
        switch (state) {
            case SECOND_FLOOR_SHOWING:
                refreshListener.onSecondFloor();
                break;
            case REFRESHING:
                refreshListener.onRefresh();
                break;
            default:
                break;
        }
    }

    private void notifyLintenerComplete(State state) {
        if (refreshListener == null) {
            return;
        }
        switch (state) {
            case SECOND_FLOOR_SHOWING:
                refreshListener.onSecondFloorComplete();
                break;
            case REFRESHING:
                refreshListener.onRefreshComplete();
                break;
            default:
                break;
        }
    }

    private void moveSpinner(float diff, boolean isAutoMove) {
        int offset = Math.round(diff);
        if (offset == 0) {
            return;
        }

        // 发送cancel事件给child
        if (!hasSendCancelEvent && currentContentOffsetY > START_POSITION) {
            sendCancelEvent();
            hasSendCancelEvent = true;
        }

        int targetY = Math.max(0, currentContentOffsetY + offset); // target不能移动到小于0的位置……

        if (offset > 0 && !isAutoMove) { // 下拉的时候才添加阻力, 上滑和自动滚动不要阻力
            // y = x - (x/2)^2
            float extraOS = targetY - distanceForRefreshStart;
            float slingshotDist = distanceForRefreshStart;
            float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, slingshotDist * 2) / slingshotDist);
            float tensionPercent = (float) (tensionSlingshotPercent - Math.pow(tensionSlingshotPercent / 2, 2));

            offset = (int) (offset * (1f - tensionPercent));
            targetY = Math.max(0, currentContentOffsetY + offset);
        }

        Log.i(TAG, "moveSpinner targetY: " + targetY);
        updateStateByOffset(targetY, !isAutoRefresh);
        setupTargetOffsetTopAndBottom(targetY, offset);

        setupRefreshHeader(targetY);

        // 别忘了回调header的位置改变方法。
        /*if (refreshHeader instanceof RefreshHeader) {
            ((RefreshHeader) refreshHeader).onPositionChange(currentContentOffsetY, lastTargetOffsetTop, distanceForRefreshStart, isTouch, state);
        }*/
    }

    private void updateStateByOffset(int offsetY, boolean isTouch) {
        if (state == State.REFRESHING || state == State.SECOND_FLOOR_SHOWING) {
            return;
        }
        if (offsetY <= 0) {
            changeState(State.DONE);
            return;
        }
        if (offsetY < distanceForRefreshStart) {
            changeState(State.PULL_TO_REFRESH);
            return;
        }
        if (offsetY < distanceForSecondFloorStart || !secondFloorEnable) {
            changeState(isTouch ? State.RELEASE_TO_REFRESH : State.REFRESHING);
            return;
        }
        changeState(isTouch ? State.RELEASE_TO_SECOND_FLOOR : State.SECOND_FLOOR_SHOWING);
    }

    private void finishSpinner() {
        int currentOffsetTop = getTargetOffsetY();
        Log.i(TAG, "finishSpinner currentOffsetTop: " + currentOffsetTop + " State: " + state.name());
        if (currentOffsetTop <= START_POSITION) {
            return;
        }

        switch (state) {
            case RELEASE_TO_REFRESH:
            case REFRESHING: {
                if (currentOffsetTop > distanceForRefreshStart) {
                    autoScroll.scrollTo(distanceForRefreshStart, SCROLL_TO_REFRESH_DURATION);
                }
                break;
            }
            case RELEASE_TO_SECOND_FLOOR:
            case SECOND_FLOOR_SHOWING: {
                // 不要向上滑动
                if (currentOffsetTop < getScreenHeight()) {
                    post(delayToScrollBottomRunnable);
                }
                break;
            }
            default: {
                autoScroll.scrollTo(START_POSITION, SCROLL_TO_TOP_DURATION);
                break;
            }
        }
    }

    private void changeState(State state) {
        Log.i(TAG, "changeState: " + String.valueOf(this.state) + " --> " + state.name());
        if (state == this.state) {
            return;
        }
        this.state = state;
        isWaitingRefreshComplete = state == State.REFRESHING || state == State.SECOND_FLOOR_SHOWING;
        pullRefreshEnable = !isWaitingRefreshComplete;

        if (mHolder != null) {
            mHolder.onStateChanged(state);
        }
    }

    private void setupTargetOffsetTopAndBottom(int targetY, int offset) {
        if (offset == 0) {
            return;
        }
        currentContentOffsetY = targetY;
        contentView.offsetTopAndBottom(offset);
        View refreshHeaderView = getRefreshHeaderView();
        refreshHeaderView.getLayoutParams().height = targetY > 0 ? targetY : 0;
        refreshHeaderView.requestLayout();
//        invalidate();
    }


    private void setupRefreshHeader(int targetY) {
        mHolder.setupOffsetY(targetY);
    }


    private View getRefreshHeaderView() {
        return mHolder.getRefreshHeaderView();
    }

    private void sendCancelEvent() {
        if (lastEvent == null) {
            return;
        }
        MotionEvent ev = MotionEvent.obtain(lastEvent);
        ev.setAction(MotionEvent.ACTION_CANCEL);
        super.dispatchTouchEvent(ev);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == activePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            lastMotionY = ev.getY(newPointerIndex);
            lastMotionX = ev.getX(newPointerIndex);
            activePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    public boolean canChildScrollUp() {
        return ViewCompat.canScrollVertically(contentView, -1);
    }

    private class AutoScroll implements Runnable {
        private Scroller scroller = new Scroller(getContext());
        private int lastY;
        private int distance;

        public AutoScroll() {
        }

        @Override
        public void run() {
            boolean finished = !scroller.computeScrollOffset() || scroller.isFinished();
            if (!finished) {
                int currY = scroller.getCurrY();
                int offset = currY - lastY;
                lastY = currY;
                moveSpinner(offset, true);
                post(this);
                if (currY >= distance) {
                    onScrollFinish(false);
                }
            } else {
                stop();
                onScrollFinish(true);
            }
        }

        public void scrollTo(int to, int duration) {
            int from = currentContentOffsetY;
            distance = to - from;
            stop();
            if (distance == 0) {
                return;
            }
            scroller.startScroll(0, 0, 0, distance, duration);
            post(this);
        }

        private void stop() {
            removeCallbacks(this);
            if (!scroller.isFinished()) {
                scroller.forceFinished(true);
            }
            lastY = 0;
        }
    }

    /**
     * 在scroll结束的时候会回调这个方法
     *
     * @param isForceFinish 是否是强制结束的
     */
    private void onScrollFinish(boolean isForceFinish) {
        if (isAutoRefresh && !isForceFinish) {
            isAutoRefresh = false;
            if (refreshListener != null) {
                refreshListener.onRefresh();
            }
            finishSpinner();
            return;
        }

        if (!isAutoRefresh && state == State.CLOSING_ALL && isForceFinish) {
            Log.v("refreshanima", "onScrollFinish CLOSING_ALL -- > DONE currentContentOffsetY=" + currentContentOffsetY + " isForceFinish=" + isForceFinish);
            changeState(State.DONE);
        }
    }

    // NestedScrollingParent

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStopNestedScroll(View target) {
        mParentHelper.onStopNestedScroll(target);
        stopNestedScroll();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed) {
        final int oldScrollY = getScrollY();
        scrollBy(0, dyUnconsumed);
        final int myConsumed = getScrollY() - oldScrollY;
        final int myUnconsumed = dyUnconsumed - myConsumed;
        dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        dispatchNestedPreScroll(dx, dy, consumed, null);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            //flingWithNestedDispatch((int) velocityY);
            return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }


    /**
     * Fling the scroll view
     *
     * @param velocityY The initial velocity in the Y direction. Positive
     *                  numbers mean that the finger/cursor is moving down the screen,
     *                  which means we want to scroll towards the top.
     */
    public void fling(int velocityY) {
        if (getChildCount() > 0) {
            int height = getHeight() - getPaddingBottom() - getPaddingTop();
            int bottom = getChildAt(0).getHeight();

            /*mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0,
                    Math.max(0, bottom - height), 0, height / 2);*/

            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void flingWithNestedDispatch(int velocityY) {
        final int scrollY = getScrollY();
        final boolean canFling = (scrollY > 0 || velocityY > 0)
                && (scrollY < getScrollRange() || velocityY < 0);
        if (!dispatchNestedPreFling(0, velocityY)) {
            dispatchNestedFling(0, velocityY, canFling);
            if (canFling) {
                fling(velocityY);
            }
        }
    }

    int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0,
                    child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }


    public interface OnRefreshListener {
        void onRefresh();

        void onRefreshComplete();

        void onSecondFloor();

        void onSecondFloorComplete();
    }

    public enum State {
        RELEASE_TO_REFRESH, PULL_TO_REFRESH, REFRESHING,
        RELEASE_TO_SECOND_FLOOR, SECOND_FLOOR_SHOWING,
        DONE, CLOSING_ALL;
    }

    public interface OnPullStateChangedListener {
        void onStateChanged(State state);

    }

    public interface IPullDownLayoutHolder extends OnPullStateChangedListener {

        View getRefreshHeaderView();

        int getRefreshStartHeight();

        int getSecondFloorStartHeight();

        void setupOffsetY(int offsetY);
    }

    private int getScreenHeight() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

}
