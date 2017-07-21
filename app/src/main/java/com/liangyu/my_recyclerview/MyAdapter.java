package com.liangyu.my_recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by liangyu on 17-7-21.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    public OnRecyclerViewItemClickListener listener;
    public String[] datas = null;
    public MyAdapter(String[] datas) {
        this.datas = datas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item,parent,false);
        ViewHolder vh = new ViewHolder(view,listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(datas[position]);
        holder.btn.setText(datas[position]);
    }

    @Override
    public int getItemCount() {
        return datas.length;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mTextView;
        public Button btn;
        private OnRecyclerViewItemClickListener mListener;
       /* public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.list_item);
        }*/

        public ViewHolder(View view, OnRecyclerViewItemClickListener listener){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.list_item);
            btn = (Button) view.findViewById(R.id.btn);
            this.mListener = listener;
            btn.setOnClickListener(this);
            //view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getPosition());
            }
        }
    }

    public static interface OnRecyclerViewItemClickListener{
        public void onItemClick(View view, int position);
    }
}
