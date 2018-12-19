package com.lp.wechat.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by LP on 2018/4/3.
 */

public class NewMsgAdpter extends BaseAdapter{

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0; //TODO
    }
}
