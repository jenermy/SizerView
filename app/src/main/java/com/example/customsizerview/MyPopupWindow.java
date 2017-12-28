package com.example.customsizerview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * @author wanlijun
 * @description 解决Android7.0版本上，PopupWindow的showAsDropDown（）设置位置不起作用,同时PopupWindow的高度不能为match_parent，需计算出高度再设置
 * @time 2017/12/27 15:53
 */

public class MyPopupWindow extends PopupWindow {
    public  MyPopupWindow(Context context){
        super(context);
    }
    public MyPopupWindow(View contentView, int width, int height){
        super(contentView,width,height);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT >= 24){
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }
}
