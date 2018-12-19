package com.lp.wechat.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by LP on 2018/4/8.
 */

public class ViewHodler {

    /**
     * 使用方式:在getView里使用,
     * 例如:TextView textView = ViewHodler.get(convertView, R.id.txt_title_pop);
     * @param view
     * @param id
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View>T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
