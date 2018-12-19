package com.lp.wechat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.lp.wechat.R;

/**
 * Created by LP on 2018/4/8.
 */

public class BaseDialog extends Dialog implements View.OnClickListener{

    private Context mContext;// 上下文

    public BaseDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {

    }

}
