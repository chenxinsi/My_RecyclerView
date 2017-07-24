package com.liangyu.my_recyclerview;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
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
        MyAdapter mAdapter = new MyAdapter(new String[]{"123","456","789","098"});
        myRecyclerView.setAdapter(mAdapter);

        //这句就是添加我们自定义的分隔线
        myRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "hello"+position, Toast.LENGTH_SHORT).show();
    }
}
