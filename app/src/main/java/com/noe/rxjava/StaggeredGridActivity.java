package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.noe.rxjava.bean.PersonCard;
import com.noe.rxjava.image.ImageLoaderManager;
import com.noe.rxjava.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lijie24 on 2016/12/2.
 */

public class StaggeredGridActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private WaterFallAdapter mAdapter;
    private List<PersonCard> mCardList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggeredgrid);
        init();
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new WaterFallAdapter(this, mCardList);
//        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this,9, Color.parseColor("#cccccc")));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(8, false));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mCardList.addAll(buildData());
        mAdapter.notifyDataSetChanged();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] lastVisibleItem = mLayoutManager.findLastVisibleItemPositions(null);
                int totalItemCount = mLayoutManager.getItemCount();
                if (lastVisibleItem[0] >= totalItemCount - 2) {
                    int count = buildData().size();
                    int start = mCardList.size();
                    mCardList.addAll(buildData());
                    mAdapter.notifyItemRangeInserted(start, count);
//                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    //生成6个明星数据，这些Url地址都来源于网络
    private List<PersonCard> buildData() {

        String[] names = {"迪丽热巴", "范冰冰", "杨幂", "Angelababy", "唐嫣", "柳岩", "迪丽热巴", "范冰冰", "杨幂", "Angelababy", "唐嫣", "柳岩", "金馆长"};
        String[] imgUrs = {
                "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3014402647,3821196097&fm=58",
                "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1477122795&di=f740bd484870f9bcb0cafe454a6465a2&src=http://tpic.home.news.cn/xhCloudNewsPic/xhpic1501/M08/28/06/wKhTlVfs1h2EBoQfAAAAAF479OI749.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=673651839,1464649612&fm=111&gp=0.jpg",
                "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=fd90a83e900a304e4d22a7fae1c9a7c3/d01373f082025aafa480a2f1fcedab64034f1a5d.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg",
                "https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=005560fc8b5494ee982208191df4e0e1/c2fdfc039245d68827b453e7a3c27d1ed21b243b.jpg",
                "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3014402647,3821196097&fm=58",
                "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1477122795&di=f740bd484870f9bcb0cafe454a6465a2&src=http://tpic.home.news.cn/xhCloudNewsPic/xhpic1501/M08/28/06/wKhTlVfs1h2EBoQfAAAAAF479OI749.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=673651839,1464649612&fm=111&gp=0.jpg",
                "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=fd90a83e900a304e4d22a7fae1c9a7c3/d01373f082025aafa480a2f1fcedab64034f1a5d.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg",
                "https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=005560fc8b5494ee982208191df4e0e1/c2fdfc039245d68827b453e7a3c27d1ed21b243b.jpg",
                "https://gss0.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/wh%3D600%2C800/sign=cfbcf1b84510b912bf94fef8f3cdd03b/9a504fc2d5628535e770642292ef76c6a7ef6324.jpg"};

        List<PersonCard> list = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            PersonCard p = new PersonCard();
            p.avatarUrl = imgUrs[i];
            p.name = names[i];
            p.imgHeight = new Random().nextInt(2) * 100 + 400; //偶数和奇数的图片设置不同的高度，以到达错开的目的
            list.add(p);
        }

        return list;
    }

    class WaterFallAdapter extends RecyclerView.Adapter {

        private Context mContext;
        private List<PersonCard> mData; //定义数据源

        //定义构造方法，默认传入上下文和数据源
        public WaterFallAdapter(Context context, List<PersonCard> data) {
            mContext = context;
            mData = data;
        }

        @Override  //将ItemView渲染进来，创建ViewHolder
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.staggeredgrid_item, null);
            return new MyViewHolder(view);
        }

        @Override  //将数据源的数据绑定到相应控件上
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHolder holder2 = (MyViewHolder) holder;
            PersonCard personCard = mData.get(position);
//            if (position % 2 == 0) {
//                Glide.with(mContext).load(personCard.avatarUrl).into(holder2.userAvatar);
//            } else {
//                Glide.with(mContext).load(personCard.avatarUrl).bitmapTransform(new RoundedCornersTransformation(mContext, 10, 0, RoundedCornersTransformation.CornerType.ALL)).into(holder2.userAvatar);
//            }
            ImageLoaderManager.getInstance().showImage(ImageLoaderManager.getDefaultOptions(holder2.userAvatar,personCard.avatarUrl));
//            Glide.with(mContext).load(personCard.avatarUrl).into(holder2.userAvatar);
            holder2.userAvatar.getLayoutParams().height = personCard.imgHeight; //从数据源中获取图片高度，动态设置到控件上
            holder2.userName.setText(personCard.name);
        }

        @Override
        public int getItemCount() {
            if (mData != null) {
                return mData.size();
            }
            return 0;
        }

        //定义自己的ViewHolder，将View的控件引用在成员变量上
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView userAvatar;
            public TextView userName;

            public MyViewHolder(View itemView) {
                super(itemView);
                userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
                userName = (TextView) itemView.findViewById(R.id.user_name);
            }
        }
    }
}
