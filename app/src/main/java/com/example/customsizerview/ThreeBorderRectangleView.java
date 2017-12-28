package com.example.customsizerview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author wanlijun
 * @description
 * @time 2017/12/25 17:08
 */

public class ThreeBorderRectangleView extends View {
    public ThreeBorderRectangleView(Context context){
        this(context,null);
    }
    public ThreeBorderRectangleView(Context context, AttributeSet attr){
        this(context,attr,0);
    }
    public ThreeBorderRectangleView(Context context, AttributeSet attr,int defStyle){
        super(context,attr,defStyle);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
