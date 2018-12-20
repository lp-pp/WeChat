package com.lp.wechat;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import com.baidu.frontia.FrontiaApplication;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.lp.wechat.chat.ChatActivity;
import com.lp.wechat.chat.VoiceCallActivity;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by LP on 2017/11/30.
 */

public class WcApp extends FrontiaApplication{
    private static final String TAG = WcApp.class.getSimpleName();

    private static Context mContext;
    private static WcApp instance;
    // 运用list来保存们每一个activity是关键
    private List<Activity> mActivityLists = new LinkedList<Activity>();

    public static Context getContextInstance(){
        return mContext;
    }

    // 构造方法, 实例化一次
    public synchronized static WcApp getInstance(){
        if (instance == null) {
            instance = new WcApp();
        }
        return instance;
    }

    //add Activity
    public void addActivity(Activity activity){
        mActivityLists.add(activity);
    }

    //关闭每一个list内的Activity
    public void exit(){
        try {
            for (Activity activity : mActivityLists) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initEMChat();
        EMChat.getInstance().init(mContext);
        EMChat.getInstance().setDebugMode(true);
        EMChat.getInstance().setAutoLogin(true);
        EMChatManager.getInstance().getChatOptions().setUseRoster(true);
        FrontiaApplication.initFrontiaApplication(this);
        //CrashHandler crashHandler = CrashHandler.getInstance();// 全局异常捕捉
        //crashHandler.init(mContext);
    }

    /**
     * 初始化EMChat
     */
    private void initEMChat() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || processAppName.equalsIgnoreCase("com.lp.wechat")){
            return;
        }
        // 获取到EMChatOptions对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 设置自定义的文字提示
        options.setNotifyText(new OnMessageNotifyListener() {
            @Override
            public String onNewMessageNotify(EMMessage emMessage) {
                return "你的好友发来了一条消息哦";
            }

            @Override
            public String onLatestMessageNotify(EMMessage emMessage, int fromUsersNum, int messageNum) {
                return fromUsersNum + "个好友，发来了" + messageNum + "条消息";
            }

            @Override
            public String onSetNotificationTitle(EMMessage emMessage) {
                return null;
            }

            @Override
            public int onSetSmallIcon(EMMessage emMessage) {
                return 0;
            }
        });

        options.setOnNotificationClickListener(new OnNotificationClickListener() {
            @Override
            public Intent onNotificationClick(EMMessage emMessage) {
                Intent intent = new Intent(mContext, MainActivity.class);
                ChatType chatType = emMessage.getChatType();
                if (chatType == ChatType.Chat) { //单聊信息
                    intent.putExtra("userId", emMessage.getFrom());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                } else { //群聊信息
                    // message.getTo()为群聊id
                    intent.putExtra("groupId", emMessage.getTo());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                }
                return intent;
            }
        });
//        IntentFilter callFilter = new IntentFilter(
//                EMChatManager.getInstance().getIncomingCallBroadcastAction());
//        registerReceiver(new CallReceiver(), callFilter);
    }

    private String getAppName(int pid) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List list = am.getRunningAppProcesses();
        Iterator iterator = list.iterator();
        PackageManager pm = this.getPackageManager();
        while (iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)
                    iterator.next();
            try {
                if (info.pid == pid) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName,
                            PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
    }

    private class CallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //拨打方username
            String from = intent.getStringExtra("from");
            //call type
            String type = intent.getStringExtra("type");
            Intent i = new Intent(mContext, VoiceCallActivity.class);
            i.putExtra("username", from);
            i.putExtra("isComingCall", true);
            startActivity(i);
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            deleteCacheDirFile(getHJYCacheDir(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();
    }

    public static String getHJYCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return Environment.getExternalStorageDirectory().toString() + "/Health/Cache";
        else
            return "/System/com.lp.Walk/Walk/Cache";
    }

    public static String getHJYDownLoadDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return Environment.getExternalStorageDirectory().toString() + "/Walk/Download";
        else {
            return "/System/com.lp.Walk/Walk/Download";
        }
    }

    public static void deleteCacheDirFile(String filePath, boolean deleteThisPath) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {// 处理目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteCacheDirFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {// 如果是文件，删除
                    file.delete();
                } else {// 目录
                    if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                        file.delete();
                    }
                }
            }
        }
    }
}
