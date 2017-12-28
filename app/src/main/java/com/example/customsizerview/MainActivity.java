package com.example.customsizerview;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mFilterTv;
    private LinearLayout mRechargeTypeLayout;
    private LinearLayout mRechargeResultLayout;
    private PopupWindow mPopupWindow; //父类对象指向子类实例，调用子类覆盖的方法
    private TextView mContentTV;
    private LinearLayout mVisiableScreenLayout; //整个页面根布局，用来监听整个页面可见的高度变化
    private int usableHeightPrevious;//视图变化前的可用高度;
    private ViewGroup.LayoutParams layoutParams;
    private View popupView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initValue();
    }
    private void initUI(){
        mFilterTv = (TextView)findViewById(R.id.filterTv);
        mFilterTv.setOnClickListener(this);
        mRechargeTypeLayout = (LinearLayout)findViewById(R.id.rechargeTypeLayout);
        mRechargeTypeLayout.setOnClickListener(this);
        mRechargeResultLayout = (LinearLayout) findViewById(R.id.rechargeResultLayout);
        mRechargeResultLayout.setOnClickListener(this);
        mContentTV = (TextView)findViewById(R.id.contentTV);
        mVisiableScreenLayout = (LinearLayout)findViewById(R.id.visiableScreenLayout);
    }
    private void initValue(){
        //获取当前布局高度
        usableHeightPrevious = computeUsableHeight();
        //完美的监听有底部虚拟按键的手机视图可见高度的变化，就是底部导航栏隐藏或者显示的时候，Activity布局可见高度的变化
        mVisiableScreenLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //底部虚拟按键隐藏或者显示的时候都会回调这个函数
                Log.i("wanlijun","onGlobalLayout");
                int  currentActivityHeight = computeUsableHeight();
                Log.i("wanlijun","currentActivityHeight="+currentActivityHeight);
                if(mPopupWindow != null && mPopupWindow.isShowing()){
                    if(currentActivityHeight > usableHeightPrevious){
//                        layoutParams.height = getComponentHeight(false);
                        //动态修改弹窗高度无效
//                        mPopupWindow.setHeight(getComponentHeight(false));
                        layoutParams = popupView.getLayoutParams();
                        layoutParams.height = getComponentHeight(false);
                        popupView.setLayoutParams(layoutParams);
                        mPopupWindow.setContentView(popupView);
                        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                        mPopupWindow.setOutsideTouchable(true);
                        //不设置背景的话会有很深的黑色边框，很难看
                        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
                        mPopupWindow.showAsDropDown(mRechargeResultLayout,0,10);
                    }else if(currentActivityHeight < usableHeightPrevious){
//                        mPopupWindow.setHeight(getComponentHeight(true));
                        layoutParams = popupView.getLayoutParams();
                        layoutParams.height = getComponentHeight(true);
                        popupView.setLayoutParams(layoutParams);
                        mPopupWindow.setContentView(popupView);
                        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                        mPopupWindow.setOutsideTouchable(true);
                        //不设置背景的话会有很深的黑色边框，很难看
                        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
                        mPopupWindow.showAsDropDown(mRechargeResultLayout,0,10);
                    }
                }
                usableHeightPrevious = currentActivityHeight;
            }
        });
    }
    //获取布局可见高度
    private int computeUsableHeight() {
        Rect r = new Rect();
        mVisiableScreenLayout.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.filterTv:
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                break;
            case R.id.rechargeTypeLayout:
                if(mPopupWindow != null && mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                    mRechargeTypeLayout.setBackgroundResource(R.drawable.gray_corner_back);
                    mRechargeResultLayout.setBackgroundResource(R.drawable.gray_corner_back);
                }else{
                    mRechargeTypeLayout.setBackgroundResource(R.drawable.white_corner_back);
                    showPopupWindow(getResources().getStringArray(R.array.recharge_type));
                }
                break;
            case R.id.rechargeResultLayout:
                if(mPopupWindow != null && mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                    mRechargeResultLayout.setBackgroundResource(R.drawable.gray_corner_back);
                    mRechargeTypeLayout.setBackgroundResource(R.drawable.gray_corner_back);
                }else{
                    mRechargeResultLayout.setBackgroundResource(R.drawable.white_corner_back);
                    showPopupWindow(getResources().getStringArray(R.array.recharge_result));
                }
                break;
        }
    }

    private void showPopupWindow(String[] contents){
        popupView = LayoutInflater.from(MainActivity.this).inflate(R.layout.shaixuan_list,null);
        ListView listView = (ListView)popupView.findViewById(R.id.shaixuanLv);
        TextView shadowTv = (TextView)popupView.findViewById(R.id.shadowTv);
        RechargeAdapter adapter = new RechargeAdapter(MainActivity.this,contents);
        listView.setAdapter(adapter);
        //无法根据底部虚拟按键显示与否动态改变PopupWindow的高度
//        mPopupWindow = new MyPopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT,getComponentHeight(true));
        mPopupWindow = new MyPopupWindow(MainActivity.this);
        mPopupWindow.setContentView(popupView);
        mPopupWindow.setHeight(getComponentHeight(true));
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setOutsideTouchable(true);
        //不设置背景的话会有很深的黑色边框，很难看
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        mPopupWindow.showAsDropDown(mRechargeResultLayout,0,10);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        shadowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPopupWindow != null && mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                    mRechargeResultLayout.setBackgroundResource(R.drawable.gray_corner_back);
                    mRechargeTypeLayout.setBackgroundResource(R.drawable.gray_corner_back);
                }
            }
        });
        layoutParams = getWindow().getAttributes();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("wanlijun","onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    public class RechargeAdapter extends BaseAdapter{
        private Context mContext;
        private String[] mStrAry;
        public RechargeAdapter(Context context,String[] strAry){
            this.mContext = context;
            this.mStrAry = strAry;
        }
        @Override
        public int getCount() {
            return mStrAry.length;
        }

        @Override
        public Object getItem(int i) {
            return mStrAry[i];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.recharge_listitem,null,false);
                holder = new ViewHolder(convertView);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            if(holder != null){
                holder.itemNameTv.setText(mStrAry[position]);
            }
            return convertView;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        class ViewHolder{
            TextView itemNameTv;
            ImageView itemCheckedIv;
            public ViewHolder(View view){
                itemNameTv = (TextView)view.findViewById(R.id.itemNameTv);
                itemCheckedIv = (ImageView)view.findViewById(R.id.itemCheckedIv);
                view.setTag(this);
            }
        }
    }

    //解决按返回键mPopupWindow不消失
    @Override
    public void onBackPressed() {
        if(mPopupWindow != null && mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
            mRechargeResultLayout.setBackgroundResource(R.drawable.gray_corner_back);
            mRechargeTypeLayout.setBackgroundResource(R.drawable.gray_corner_back);
        }else{
            super.onBackPressed();
        }

    }
    private int getComponentHeight(boolean menuVisiable){
//        int statusBarHeight = -1;
//        //获取status_bar_height资源的ID
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            //根据资源ID获取响应的尺寸值
//            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
//        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int[] location = new int[2];
        mContentTV.getLocationOnScreen(location);
        int y = location[1];
        Log.i("wanlijun",getDpi()+"");
        Log.i("wanlijun",dm.heightPixels+"");
        int popHeight = 0;
        if(menuVisiable){
            popHeight = dm.heightPixels - y ;
        }else {
            popHeight = getDpi() - y ;
        }
        return  popHeight;
    }

    //获取屏幕原始尺寸高度，包括底部虚拟按键
    private int getDpi(){
        int dpi = 0;
        Display display= getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Class c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
            method.invoke(display,displayMetrics);
            dpi = displayMetrics.heightPixels;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  dpi;
    }


}
