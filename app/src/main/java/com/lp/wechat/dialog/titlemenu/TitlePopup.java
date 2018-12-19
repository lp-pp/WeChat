package com.lp.wechat.dialog.titlemenu;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lp.wechat.R;
import com.lp.wechat.utils.ViewHodler;

import java.util.ArrayList;

/**
 * Created by LP on 2018/4/4.
 * 功能描述: 标题按钮上的弹窗(继承自PopupWindow)
 */
public class TitlePopup extends PopupWindow{

    private Context mContext;
    //列表弹框间的间隔
    private static final int LIST_PADDING = 10;

    //初始化一个矩形
    private Rect mRect = new Rect();

    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];

    // 屏幕的宽度和高度
    private int mScreenWidth, mScreenHeight;

    //判断是否需要添加或更新列表子类项
    private boolean mIsDirty;

    //位置不在中心
    private int popupGravity = Gravity.NO_GRAVITY;

    //弹窗子类型选中时的监听
    private  OnItemOnClickListener mItemOnClickListener;

    //定义列表对象
    private ListView mListView;

    //定义弹窗子类项列表
    private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();

    public TitlePopup(Context context) {
        this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public TitlePopup(Context context, int width, int height) {
        this.mContext = context;
        //设置可以获取焦点
        setFocusable(true);
        //设置弹窗内可点击
        setTouchable(true);
        //设置弹窗外可点击
        setOutsideTouchable(true);

        //获得屏幕的宽度和高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenHeight = wm.getDefaultDisplay().getHeight();

        //设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);
        //设置弹窗的背景图片
        setBackgroundDrawable(new BitmapDrawable());

        //设置弹窗的布局界面
        setContentView(LayoutInflater.from(mContext).inflate(R.layout.title_popup, null));
        setAnimationStyle(R.style.AnimHead);

        initUI();
    }

    /**
     * 初始化弹框列表
     */
    private void initUI() {
        mListView = (ListView) getContentView().findViewById(R.id.lv_title_list);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击后，弹框消失
                dismiss();

                if (mItemOnClickListener != null)
                    mItemOnClickListener.onItemClick(mActionItems.get(position), position);
            }
        });
    }

    /**
     * 显示弹框列表界面
     * @param view
     */
    public void show(View view) {
        //获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);

        //设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),
                mLocation[1] + view.getHeight());

        ///判断是否需要添加或更新列表子类项
        if (mIsDirty)
            populateActions();

        //显示弹框的位置
        showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING - (getWidth()/2),
                mRect.bottom);

    }

    /**
     * 设置弹框列表子项
     */
    private void populateActions() {
        mIsDirty = false;

        //设置列表的适配器
        mListView.setAdapter(new BaseAdapter() {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext)
                            .inflate(R.layout.layout_item_pop, parent,false);
                }
                TextView textView = ViewHodler.get(convertView, R.id.txt_title_pop);
                textView.setTextColor(mContext.getResources().getColor(R.color.white));
                textView.setTextSize(16);
                //设置文本垂直居中
                textView.setGravity(Gravity.CENTER_VERTICAL);
                //设置文本域的范围
                textView.setPadding(0, 10, 0, 10);
                //设置一行显示文本（不换行）
                textView.setSingleLine(true);

                //设置文字文本
                ActionItem actionItem = mActionItems.get(position);
                if (actionItem != null) {
                    //设置文字文本
                    textView.setText(actionItem.mTitle);
                    if (actionItem.mDrawable != null) {
                        //设置文字与图标的间隔
                        textView.setCompoundDrawablePadding(10);
                        //设置图标在文字的左边
                        textView.setCompoundDrawablesWithIntrinsicBounds(actionItem.mDrawable,
                                null, null, null);

                    }
                }
                return convertView;
            }

            @Override
            public int getCount() {
                return mActionItems.size();
            }

            @Override
            public Object getItem(int position) {
                return mActionItems.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }
        });
    }

    /**
     * 添加子类项
     * @param actionItem
     */
    public void addAction(ActionItem actionItem){
        if (actionItem != null){
            mActionItems.add(actionItem);
            mIsDirty = true;
        }
    }

    /**
     * 清除子类项
     */
    public void cleanAction(){
        if (mActionItems.isEmpty()){
            mActionItems.clear();
            mIsDirty = true;
        }
    }

    /**
     * 根据位置下标得到子类项
     * @param position
     * @return
     */
    public ActionItem getAction(int position){
        if (position < 0 || position > mActionItems.size()){
            return null;
        }
        return mActionItems.get(position);
    }

    /**
     * 设置监听事件
     */
    public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener){
        this.mItemOnClickListener = onItemOnClickListener;
    }

    /**
     * 弹窗子类项按钮监听事件
     */
    public static interface OnItemOnClickListener {
        public void onItemClick(ActionItem actionItem, int position);
    }

}
