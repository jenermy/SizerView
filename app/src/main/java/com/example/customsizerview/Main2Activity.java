package com.example.customsizerview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//两种筛选布局方式：一种是点筛选按钮，上方弹出个PopupWindow,全局刷新，另一种是右滑出来一个抽屉，局部刷新（仿京东筛选方式）

public class Main2Activity extends AppCompatActivity {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private GridView mRechargeTypeGv;
    private GridView mRechargeResultGv;
    private GridView rechargeTypeGv2;
    private GridView rechargeResultGv2;
    private GridAdapter typeAdapter;
    private GridAdapter resultAdapter;
    private TextView mFilterDownTv;
    private String[] typeStrs;
    private String[] resultStrs;
    private ArrayList<GridItem> typeList =  new ArrayList<>();
    private ArrayList<GridItem> resultList = new ArrayList<>();
    private int typePosition = 0;
    private int resultPosition = 0;
    private FragmentManager fragmentManager;
    private PopupWindow popupWindow;
    private int typeCurrentPosition = 0; //PopupWindow当前选择记录
    private int resultCurrentPosition = 0;
    private int mDismissTypePosition = 0;  //PopupWindow消失时的选择记录
    private int mDismissResultPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        initUI();
        initValue();
    }
    private void initUI(){
        drawer = (DrawerLayout)findViewById(R.id.drawer);
//        drawer.setDrawerShadow(R.drawable.dog2, GravityCompat.END);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(Main2Activity.this,
//                drawer,R.string.app_name,R.string.app_name);
        mRechargeTypeGv = (GridView)findViewById(R.id.rechargeTypeGv);
        mRechargeResultGv = (GridView)findViewById(R.id.rechargeResultGv);
        BlankFragment fragment = new BlankFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.contentFrameLayout,fragment).commit();
        mFilterDownTv = (TextView)findViewById(R.id.filterDownTv);
        mFilterDownTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else{
                    showFilterPopupWindow();
                }
            }
        });
    }
    private void initValue(){
        typeStrs = getResources().getStringArray(R.array.recharge_type);
        resultStrs = getResources().getStringArray(R.array.recharge_result);
        for (int i= 0;i<typeStrs.length;i++){
            GridItem item = new GridItem();
            item.setData(typeStrs[i]);
            item.setPosition(i);
            typeList.add(item);
        }
        for(int j=0;j<resultStrs.length;j++){
            GridItem item = new GridItem();
            item.setData(resultStrs[j]);
            item.setPosition(j);
            resultList.add(item);
        }
        typeAdapter = new GridAdapter(Main2Activity.this,typeList);
        resultAdapter = new GridAdapter(Main2Activity.this,resultList);
        typeAdapter.setmGridView(mRechargeTypeGv);
        resultAdapter.setmGridView(mRechargeResultGv);
        mRechargeTypeGv.setAdapter(typeAdapter);
        mRechargeResultGv.setAdapter(resultAdapter);
        //局部刷新
        mRechargeTypeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(typePosition != i) {
//                    GridItem item = (GridItem) adapterView.getItemAtPosition(i);
//                    GridItem beforeItem = (GridItem) adapterView.getItemAtPosition(typePosition);
//                    typeAdapter.updateItemData(item, beforeItem);
//                    typePosition = i;
                    typeAdapter.updateItemBack(i,typePosition);
                    typePosition = i;
                }
            }
        });
        mRechargeResultGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(resultPosition != i) {
//                    GridItem item = (GridItem) adapterView.getItemAtPosition(i);
//                    GridItem beforeItem = (GridItem) adapterView.getItemAtPosition(resultPosition);
//                    resultAdapter.updateItemData(item, beforeItem);
//                    resultPosition = i;
                    resultAdapter.updateItemBack(i,resultPosition);
                    resultPosition = i;
                }
            }
        });
    }

    //全局刷新
    private void showFilterPopupWindow(){
        View view1 = LayoutInflater.from(Main2Activity.this).inflate(R.layout.recharge_popupwindow,null);
        rechargeTypeGv2 = (GridView)view1.findViewById(R.id.rechargeTypeGv2);
        rechargeResultGv2 = (GridView)view1.findViewById(R.id.rechargeResultGv2);
        TextView resetTv = (TextView)view1.findViewById(R.id.resetTv);
        TextView confirmTv = (TextView)view1.findViewById(R.id.confirmTv);
        typeAdapter = new GridAdapter(Main2Activity.this,typeList);
        resultAdapter = new GridAdapter(Main2Activity.this,resultList);
        typeAdapter.setCurrentPosition(typeCurrentPosition);
        resultAdapter.setCurrentPosition(resultCurrentPosition);
        rechargeTypeGv2.setAdapter(typeAdapter);
        rechargeResultGv2.setAdapter(resultAdapter);
        popupWindow = new MyPopupWindow(view1,LinearLayout.LayoutParams.MATCH_PARENT,getComponentHeight());
        //以下三句代码可以解决按返回键PopupWindow不消失或者按返回键直接返回到了上一个页面
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.showAsDropDown(mFilterDownTv,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mDismissTypePosition = typeCurrentPosition;
                mDismissResultPosition = resultCurrentPosition;
            }
        });
        rechargeResultGv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(resultCurrentPosition != i){
                    resultCurrentPosition = i;
                    resultAdapter.setCurrentPosition(i);
                    resultAdapter.notifyDataSetChanged();
                }
            }
        });
        rechargeTypeGv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(typeCurrentPosition != i){
                    typeCurrentPosition = i;
                    typeAdapter.setCurrentPosition(i);
                    typeAdapter.notifyDataSetChanged();
                }
            }
        });
        resetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重置是全都选择全部，如果已经选择的全部，就无需刷新
                if(typeCurrentPosition != 0){
                    typeCurrentPosition = 0;
                    typeAdapter.setCurrentPosition(0);
                    typeAdapter.notifyDataSetChanged();
                }
                if(resultCurrentPosition != 0){
                    resultCurrentPosition = 0;
                    resultAdapter.setCurrentPosition(0);
                    resultAdapter.notifyDataSetChanged();
                }
            }
        });

        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果上次PopupWindow消失的时候选择状态与现在一致，则不必要刷新，减少不必要的网络请求，优化用户体验
                if(mDismissTypePosition != typeCurrentPosition || mDismissResultPosition != resultCurrentPosition){
                    //这里面做数据筛选操作，包括重新请求数据
                }
                popupWindow.dismiss();
            }
        });

    }
    public class GridAdapter extends BaseAdapter{
        private Context mContext;
        private ArrayList<GridItem> gridItems;
        private GridView mGridView;
        private int mCurrentPosition;
        public GridAdapter(Context context,ArrayList<GridItem> gridItems){
            this.mContext = context;
            this.gridItems = gridItems;
        }
        public void setCurrentPosition(int currentPosition) {
            this.mCurrentPosition = currentPosition;
        }

        public void setmGridView(GridView mGridView) {
            this.mGridView = mGridView;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item,null);
                holder = new ViewHolder(convertView);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            if(holder != null){
                if(position == mCurrentPosition){
                    holder.gridItemLayout.setBackgroundResource(R.drawable.white_corner_back);
                }else{
                    holder.gridItemLayout.setBackgroundResource(R.drawable.gray_corner_back);
                }
                holder.gridItemNameTv.setText(gridItems.get(position).getData());
            }
            return convertView;
        }
        //更新Item的数据
        public void updateItemData(GridItem item,GridItem beforeItem){
            int currentPosition = -1;
            int beforePosition = -1;
            boolean current = false;
            boolean before = false;
            for(int i=0;i<gridItems.size();i++){
                if(gridItems.get(i).getPosition() == item.getPosition()){
                    currentPosition = i;
                    current = true;
                }
                if(gridItems.get(i).getPosition() == beforeItem.getPosition()){
                    beforePosition = i;
                    before = true;
                }
                if(current && before){
                    break;
                }
            }
            //可以在这里更新gridItems里面对应位置的数据
            Message message = Message.obtain();
            message.arg1 = currentPosition;
            message.arg2 = beforePosition;
            mHandler.sendMessage(message);
        }
        //更新Item的背景色
        public void updateItemBack(int currentPosition,int beforePosition){
            Message message = Message.obtain();
            message.arg1 = currentPosition;
            message.arg2 = beforePosition;
            mHandler.sendMessage(message);
        }
        private Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //刷新指定item（局部刷新）
                if(mGridView == null) return;
                int visiablePosition = mGridView.getFirstVisiblePosition();
                int currentPosition = msg.arg1;
                int beforePosition = msg.arg2;
                View view1 = mGridView.getChildAt(currentPosition - visiablePosition);
                LinearLayout gridItemLayout = (LinearLayout)view1.findViewById(R.id.gridItemLayout);
                gridItemLayout.setBackgroundResource(R.drawable.white_corner_back);
                View view2 = mGridView.getChildAt(beforePosition - visiablePosition);
                gridItemLayout = (LinearLayout)view2.findViewById(R.id.gridItemLayout);
                gridItemLayout.setBackgroundResource(R.drawable.gray_corner_back);
            }
        };

        @Override
        public Object getItem(int i) {
            return gridItems.get(i);
        }

        @Override
        public int getCount() {
            return gridItems.size();
        }
        class ViewHolder{
            private LinearLayout gridItemLayout;
            private TextView gridItemNameTv;
            public ViewHolder(View view){
                gridItemLayout = (LinearLayout)view.findViewById(R.id.gridItemLayout);
                gridItemNameTv = (TextView)view.findViewById(R.id.gridItemNameTv);
                view.setTag(this);
            }
        }
    }
    private int getComponentHeight(){
        //获取状态栏高度
//        int statusBarHeight = -1;
//        //获取status_bar_height资源的ID
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            //根据资源ID获取响应的尺寸值
//            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
//        }
        //获取屏幕的高度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //获取控件位置
        int[] location = new int[2];
        drawer.getLocationOnScreen(location);
        int y = location[1];
        //popupwindow显示高度为drawer控件的上方到屏幕底部
        int popHeight = dm.heightPixels  - y;
        return  popHeight;
    }

    //获取底部导航栏的高度
    private int getNavigationBarHeight(){
        int rid = getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid!=0){
            int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return getResources().getDimensionPixelSize(resourceId);
        }else {
            return 0;
        }
    }

}
