package com.android.core.jacoco;

import android.os.Environment;

import com.android.core.util.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lijie on 2020-04-15.
 */
public class JacocoHelper {
    //ec文件的路径
    private static String DEFAULT_COVERAGE_FILE_PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/coverage.ec";

    //项目路径
    private static String PROJECT_PATH;

    /**
     * 初始化
     *
     * @param projectPath '项目路径' + '/app/build/outputs/code-coverage/'
     * @param isDebug     是否打开log
     */
    public static void init(String projectPath, boolean isDebug) {
        PROJECT_PATH = projectPath + "/app/build/outputs/code-coverage/";
    }

    /**
     * 生成ec文件
     *
     * @param isNew 是否重新创建ec文件
     */
    public static void generateEcFile(boolean isNew) {
        OutputStream out = null;
        File mCoverageFilePath = new File(DEFAULT_COVERAGE_FILE_PATH);
        try {
            if (isNew && mCoverageFilePath.exists()) {
                mCoverageFilePath.delete();
            }
            if (!mCoverageFilePath.exists()) {
                mCoverageFilePath.createNewFile();
            }
            out = new FileOutputStream(mCoverageFilePath.getPath(), true);
            Object agent = Class.forName("org.jacoco.agent.rt.RT")
                    .getMethod("getAgent")
                    .invoke(null);
            out.write((byte[]) agent.getClass().getMethod("getExecutionData", boolean.class)
                    .invoke(agent, false));
        } catch (Exception e) {
        } finally {
            if (out == null) {
                return;
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Logger.d("xxxxxxx", getAdbPullCmd());
    }


    /**
     * 导出jacoco生成的ec文件到项目相关目录下
     *
     * @return adb 命令
     */
    public static String getAdbPullCmd() {
        String adb = "adb pull " + DEFAULT_COVERAGE_FILE_PATH + " " + PROJECT_PATH;
        return adb;
    }
}
