package com.lp.wechat.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMConversation;
import com.lp.wechat.adapter.NewMsgAdpter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LP on 2018/4/3.
 * 消息
 */
public class Fragment_Msg extends Fragment implements OnClickListener, OnItemClickListener {

    private static final String TAG = Fragment_Msg.class.getName();
    private Context ctx;
    private View layout;
    private RelativeLayout rl_errorItem;
    private TextView tv_errorText;
    private ListView lv_contact;
    private NewMsgAdpter mNewMsgAdpter;
    private List<EMConversation> conversationList = new ArrayList<EMConversation>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void refresh() {

    }


}
