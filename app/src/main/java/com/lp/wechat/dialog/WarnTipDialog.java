package com.lp.wechat.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created by LP on 2018/4/8.
 */
public class WarnTipDialog extends BaseDialog implements View.OnClickListener {

    private OnClickListener onClickListener;
    private String mText;
    private BaseDialog mBaseDialog;

    public WarnTipDialog(Context context, String text) {
        super(context);
        this.mText = text;
        mBaseDialog = new BaseDialog(context);
        init();
    }

    private void init() {

    }

    @Override
    public void onClick(View v) {

    }

    public void setBtnOkLinstener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
