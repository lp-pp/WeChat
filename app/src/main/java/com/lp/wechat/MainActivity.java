package com.lp.wechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.lp.wechat.utils.Utils;
import com.lp.wechat.dialog.WarnTipDialog;
import com.lp.wechat.dialog.titlemenu.ActionItem;
import com.lp.wechat.dialog.titlemenu.TitlePopup;
import com.lp.wechat.dialog.titlemenu.TitlePopup.OnItemOnClickListener;
import com.lp.wechat.view.activity.AddGroupChatActivity;
import com.lp.wechat.view.activity.CetMoneyActivity;
import com.lp.wechat.view.activity.PublicActivity;
import com.lp.wechat.view.fragment.Fragment_Contacts;
import com.lp.wechat.view.fragment.Fragment_Discover;
import com.lp.wechat.view.fragment.Fragment_Msg;
import com.lp.wechat.view.fragment.Fragment_Profile;
import com.lp.wechat.zxing.CaptureActivity;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by LP on 2017/11/30.
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TitlePopup titlePopup;
    private WarnTipDialog tipDialog;
    private NewMessageBroadcastReceiver nMsgReceiver;
    private TextView tv_title;
    private ImageView img_right;
    private TextView tv_unreadMsgLable;  //未读消息TextView
    private TextView tv_unreadAddressLable;  //未读通讯录TextView
    private TextView tv_unreadFindLable;  //发现
    private Fragment[] fragments;
    private Fragment_Msg msgFragment;
    private Fragment_Contacts contactListFragment;
    private Fragment_Discover findFragment;
    private Fragment_Profile profileFragment;
    private ImageView[] imageViews;
    private TextView[] textViews;
    private String connectMsg = "";
    private int index;
    private int currentTabIndex; //当前fragment的index


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WcApp.getInstance().addActivity(this);
        initViews();
        initTabView();
        //initVersion();
        initPopWindow();
        initReceiver();
    }


    private void initViews() {
        tv_title = (TextView) findViewById(R.id.txt_title);
        img_right = (ImageView) findViewById(R.id.img_right);
        //设置消息页面为初始化页面
        img_right.setVisibility(View.VISIBLE);
        img_right.setImageResource(R.drawable.icon_add);
        img_right.setOnClickListener(this);
    }

    private void initTabView() {
        tv_unreadMsgLable = (TextView) findViewById(R.id.txt_unread_msg_number);
        tv_unreadAddressLable = (TextView) findViewById(R.id.txt_unread_address_number);
        tv_unreadFindLable = (TextView) findViewById(R.id.txt_unread_find_number);
        msgFragment = new Fragment_Msg();
        contactListFragment = new Fragment_Contacts();
        findFragment = new Fragment_Discover();
        profileFragment = new Fragment_Profile();
        fragments = new Fragment[]{msgFragment, contactListFragment, findFragment, profileFragment};

        imageViews = new ImageView[4];
        imageViews[0] = (ImageView) findViewById(R.id.img_weixin);
        imageViews[1] = (ImageView) findViewById(R.id.img_contact_list);
        imageViews[2] = (ImageView) findViewById(R.id.img_find);
        imageViews[3] = (ImageView) findViewById(R.id.img_profile);
        imageViews[0].setSelected(true);

        textViews = new TextView[4];
        textViews[0] = (TextView) findViewById(R.id.txt_weixin);
        textViews[1] = (TextView) findViewById(R.id.txt_contact_list);
        textViews[2] = (TextView) findViewById(R.id.txt_find);
        textViews[3] = (TextView) findViewById(R.id.txt_profile);
        textViews[0].setTextColor(0xFF45C01A);

        //添加显示第一个微信栏的fragment，隐藏其它三个
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, msgFragment)
                .add(R.id.fragment_container, contactListFragment)
                .add(R.id.fragment_container, findFragment)
                .add(R.id.fragment_container, profileFragment)
                .hide(contactListFragment).hide(findFragment).hide(profileFragment)
                .show(msgFragment).commit();
        updateUnreadLable();
    }

    private void initVersion() {
        // TODO 检查版本更新
        String versionInfo = Utils.getValue(this, Constants.VersionInfo);
        if (!TextUtils.isEmpty(versionInfo)) {
            tipDialog = new WarnTipDialog(this, "发现新版本：请更新到最新版本!");
            tipDialog.setBtnOkLinstener(onclick);
            tipDialog.show();
        }
    }

    private DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Utils.showLongToast(MainActivity.this, "正在下载...");// TODO
            tipDialog.dismiss();
        }
    };

    private void initPopWindow() {
        //实例化标题栏弹框
        titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titlePopup.setItemOnClickListener(onItemClick);
        // 给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(this, R.string.menu_groupchat, R.drawable.icon_menu_group));
        titlePopup.addAction(new ActionItem(this, R.string.menu_addfriend, R.drawable.icon_menu_addfriend));
        titlePopup.addAction(new ActionItem(this, R.string.menu_qrcode, R.drawable.icon_menu_sao));
        titlePopup.addAction(new ActionItem(this, R.string.menu_money, R.drawable.abv));
    }

    OnItemOnClickListener onItemClick = new OnItemOnClickListener() {

        @Override
        public void onItemClick(ActionItem actionItem, int position) {
            switch (position) {
                case 0:
                    //发起群聊
                    Utils.start_Activity(MainActivity.this, AddGroupChatActivity.class);
                    break;
                case 1:
                    //添加朋友
                    Utils.start_Activity(MainActivity.this, PublicActivity.class,
                            new BasicNameValuePair(Constants.NAME, "添加朋友"));
                    break;
                case 2:
                    //扫一扫
                    Utils.start_Activity(MainActivity.this, CaptureActivity.class);
                    break;
                case 3:
                    //收付款
                    Utils.start_Activity(MainActivity.this, CetMoneyActivity.class);
                    break;
                default:
                    break;
            }
        }
    };

    public void onTabClicked(View v) {
        img_right.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.rl_weixin:
                index = 0;
                if (msgFragment != null) {
                    msgFragment.refresh();
                }
                tv_title.setText(R.string.app_name);
                img_right.setVisibility(View.VISIBLE);
                img_right.setImageResource(R.drawable.icon_add);
            break;
            case R.id.rl_contact_list:
                index = 1;
                tv_title.setText(R.string.contacts);
                img_right.setVisibility(View.VISIBLE);
                img_right.setImageResource(R.drawable.icon_titleaddfriend);
                break;
            case R.id.rl_find:
                index = 2;
                tv_title.setText(R.string.discover);
                break;
            case R.id.rl_profile:
                index = 3;
                tv_title.setText(R.string.me);
                break;
            default:
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded())
                ft.add(R.id.fragment_container, fragments[index]);
            ft.show(fragments[index]).commit();
        }
        imageViews[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        imageViews[index].setSelected(true);
        textViews[currentTabIndex].setTextColor(0xFF999999);
        textViews[index].setTextColor(0xFF45C01A);
        currentTabIndex = index;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_right:
                if (index == 0) {
                    titlePopup.show(findViewById(R.id.layout_bar));
                } else {
                    Utils.start_Activity(MainActivity.this, PublicActivity.class,
                            new BasicNameValuePair(Constants.NAME, "添加朋友"));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void initReceiver() {
        //TODO
    }

    /**
     * 新消息接收者
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }

    }

    private void updateUnreadLable() {
        int count = 0;
        count = EMChatManager.getInstance().getUnreadMsgsCount();
        if (count > 0) {
            tv_unreadMsgLable.setText(String.valueOf(count));
            tv_unreadMsgLable.setVisibility(View.VISIBLE);
        } else {
            tv_unreadMsgLable.setVisibility(View.INVISIBLE);
        }

    }
}
