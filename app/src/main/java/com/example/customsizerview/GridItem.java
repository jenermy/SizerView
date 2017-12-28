package com.example.customsizerview;

/**
 * @author wanlijun
 * @description 用于局部刷新，记住要刷新的item的内容和位置，利用getChildAt（）获取当前需要刷新的view，进行局部刷新
 * @time 2017/12/26 9:18
 */

public class GridItem {
    private String  data; //item的内容
    private int position; //item的位置

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
