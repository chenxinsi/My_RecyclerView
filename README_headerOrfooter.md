# RecyclerView 添加header footer------(2)
>>#### HeaderView的布局文件： header.xml
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="100dp">
        <TextView
                android:id="@+id/header"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:text="我是Header"
                android:textSize="30sp"
                android:textColor="#fde70b0b"
                android:background="#f9777979"
                android:gravity="center"/>
</LinearLayout>
```
>>#### MainActivity.java中内容：
```
//为RecyclerView添加HeaderView和FooterView
        View header = LayoutInflater.from(this).inflate(R.layout.header, myRecyclerView, false);
        mAdapter.setHeaderView(header);
//
        View footer = LayoutInflater.from(this).inflate(R.layout.footer, myRecyclerView, false);
        mAdapter.setFooterView(footer);
```
>>#### MyAdapter.java的代码
```
package com.liangyu.my_recyclerview;
//
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
//
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
//
    public static final int TYPE_HEADER = 0;//只带Header
    public static final int TYPE_FOOTER = 1;//只带Footer
    public static final int TYPE_NORMAL = 2;//不带header和footer
//
    public OnRecyclerViewItemClickListener listener;
    public String[] datas = null;
    public MyAdapter(String[] datas) {
        this.datas = datas;
    }
//
    public static View mHeaderView;
    public static View mFooterView;
//
    //HeaderView和FooterView的get和set函数
    public View getHeaderView(){
        return mHeaderView;
    }
//
    public void setHeaderView(View headerView){
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
//
    public View getFooterView(){
        return mFooterView;
    }
//
    public void setFooterView(View footerView){
        mFooterView = footerView;
        notifyItemInserted(getItemCount() -1);
    }
//
    /** 重写这个方法很重要,加入Header和Footer的关键,我们通过判断item的类型,从而绑定不同的view **/
    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null && mFooterView == null){
            return TYPE_NORMAL;
        }
//
        if(mHeaderView != null && position == 0){
            return TYPE_HEADER;
        }
//
        if(mFooterView != null && position == (getItemCount() - 1)){
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }
//
    //创建View,如果是HeaderView或者FooterView,直接在Holder中返回
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
        if(mHeaderView != null && viewType == TYPE_HEADER){
            return new ViewHolder(mHeaderView,listener);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new ViewHolder(mFooterView,listener);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item,parent,false);
        ViewHolder vh = new ViewHolder(view,listener);
        return vh;
    }
//
//
    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof ViewHolder){
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                if(mHeaderView != null){
                    holder.mTextView.setText(datas[position - 1]);
                    holder.btn.setText(datas[position - 1]);
                }else{
                    holder.mTextView.setText(datas[position]);
                    holder.btn.setText(datas[position]);
                }
//
                return;
            }
        }else if(getItemViewType(position) == TYPE_HEADER){
            return;
        }else{
            return;
        }
    }
//
    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null){
            return datas.length;
        }else if(mHeaderView != null && mFooterView == null){
            return datas.length + 1;
        }else if(mHeaderView == null && mFooterView != null){
            return datas.length + 1;
        }else{
            return datas.length + 2;
        }
    }
//
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.listener = listener;
    }
//
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//
        public TextView mTextView;
        public Button btn;
        private OnRecyclerViewItemClickListener mListener;
       /* public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.list_item);
        }*/
//
        public ViewHolder(View view, OnRecyclerViewItemClickListener listener){
            super(view);
            if(view == mHeaderView || view == mFooterView){
                return;
            }
            mTextView = (TextView) view.findViewById(R.id.list_item);
            btn = (Button) view.findViewById(R.id.btn);
            this.mListener = listener;
            btn.setOnClickListener(this);
            //view.setOnClickListener(this);
        }
//
        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getPosition());
            }
        }
    }
//
    public static interface OnRecyclerViewItemClickListener{
        public void onItemClick(View view, int position);
    }
}
```