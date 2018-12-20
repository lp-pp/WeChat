package com.lp.wechat.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.lp.wechat.WcApp;
import com.lp.wechat.dialog.FlippingLoadingDialog;
import com.lp.wechat.net.NetClient;
import com.lp.wechat.utils.Utils;

import org.apache.http.message.BasicNameValuePair;

public class BaseActivity extends Activity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected Activity mContext;
    protected FlippingLoadingDialog mLoadingDialog;
    protected NetClient mNetClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        WcApp.getInstance().addActivity(this);
        mNetClient = new NetClient(this);
        initControl();
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Utils.finish(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 绑定控件id
     */
    protected abstract void initControl();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 打开 Activity
     *
     * @param activity
     * @param cls
     * @param name
     */
    public void start_Activity(Activity activity, Class<?> cls,
                               BasicNameValuePair... name) {
        Utils.start_Activity(activity, cls, name);
    }

    /**
     * 关闭 Activity
     *
     * @param activity
     */
    public void finish(Activity activity) {
        Utils.finish_Activity(activity);
    }

    /**
     * 判断是否有网络连接
     */
    public boolean isNetworkAvailable(Context context) {
        return Utils.isNetworkAvailable(context);
    }

    public FlippingLoadingDialog getLoadingDialog(String msg) {
        if (mLoadingDialog == null)
            mLoadingDialog = new FlippingLoadingDialog(this, msg);
        return mLoadingDialog;
    }

}
