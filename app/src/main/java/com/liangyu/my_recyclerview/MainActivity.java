package com.liangyu.my_recyclerview;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.liangyu.my_recyclerview.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnRecyclerViewItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //android 2.1+
        ActivityMainBinding binding =  DataBindingUtil.setContentView(this,R.layout.activity_main);
        RecyclerView myRecyclerView = binding.myRecyclerView;
       //创建默认的线性LayoutManager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(mLayoutManager);
        //如果确定每个item的高度是固定的,设置这个选项可以提高性能
        myRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        MyAdapter mAdapter = new MyAdapter(new String[]{"1","2","3","4","5","6","7","8","9","10","11"});
        myRecyclerView.setAdapter(mAdapter);
        //为RecyclerView添加HeaderView和FooterView
        View header = LayoutInflater.from(this).inflate(R.layout.header, myRecyclerView, false);
        mAdapter.setHeaderView(header);

        View footer = LayoutInflater.from(this).inflate(R.layout.footer, myRecyclerView, false);
        mAdapter.setFooterView(footer);

        //这句就是添加我们自定义的分隔线
        myRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));
        mAdapter.setOnItemClickListener(this);
    }

    private void setHeaderView(RecyclerView view){


    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "hello"+position, Toast.LENGTH_SHORT).show();
    }
}
