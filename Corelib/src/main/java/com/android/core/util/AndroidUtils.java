package com.android.core.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.android.core.context.ContextProvider;

/**
 * Created by lijie on 2020-03-20.
 */
public class AndroidUtils {

    public static boolean isDebugable() {
        try {
            ApplicationInfo info = ContextProvider.get().getContext().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    public static String getProcessName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }

        return null;
    }
}
