package com.lp.wechat;

import android.content.Context;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Created by LP on 2017/11/30.
 *全局异常处理
 */

public class CrashHandler implements UncaughtExceptionHandler{
    private static final String TAG = CrashHandler.class.getSimpleName();

    private static CrashHandler crashHandler = new CrashHandler();
    private Context mCxt;

    @SuppressWarnings("unused")
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler(){}

    public static CrashHandler getInstance(){
        return crashHandler;
    }

    public void init(Context mContext) {
        this.mCxt = mContext;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO

            }
        }).start();
    }


    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    @SuppressWarnings("unused")
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        return true;
    }
}
