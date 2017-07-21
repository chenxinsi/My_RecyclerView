# RecyclerView
>### 概述
```
RecyclerView 是Android 5.0版本中新添加的一个用来取代ListView的SDK，
它的灵活性与可替代性比listview更好。
接下来通过一系列的文章讲解如何使用RecyclerView,彻底抛弃ListView.
```
>### 介绍
```
RecyclerView与ListView原理是类似的：都是仅仅维护少量的View并且可以展示大量的数据集。RecyclerView用以下两种方式简化了数据的展示和处理:
__
    1.使用LayoutManager来确定每一个item的排列方式。
__
    2.为增加和删除项目提供默认的动画效果。
__
Adapter：使用RecyclerView之前，你需要一个继承自RecyclerView.Adapter的适配器，作用是将数据与每一个item的界面进行绑定。
__
LayoutManager：用来确定每一个item如何进行排列摆放，何时展示和隐藏。回收或重用一个View的时候，LayoutManager会向适配器请求新的数据来替换旧的数据，这种机制避免了创建过多的View和频繁的调用findViewById方法（与ListView原理类似）。
__
    目前SDK中提供了三种自带的LayoutManager:
__
        LinearLayoutManager
__
        GridLayoutManager
__
        StaggeredGridLayoutManager
```

>### 简单用法
```
1. 添加依赖
在AS的app moudle中build.gradle中添加依赖
dependencies{
...
compile 'com.android.support:recyclerview-v7:25.3.1'
}
2. 编写代码
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">
    <android.support.v7.widget.RecyclerView
        android:id="@+id/my_recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scrollbars="vertical"/>
</RelativeLayout>
```
>>#### 创建Adapter
```
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public String[] datas = null;
    public MyAdapter(String[] datas) {
        this.datas = datas;
    }
//
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
//
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(datas[position]);
    }
//
    @Override
    public int getItemCount() {
        return datas.length;
    }
//
    public static class ViewHolder extends RecyclerView.ViewHolder{
//
        public TextView mTextView;
        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.list_item);
        }
    }
}
```
>>#### 在MainActivity中获取这个RecyclerView,并声明LayoutManager与Adapter
```
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
    MyAdapter mAdapter = new MyAdapter(new String[]{"123","asdas"});
    myRecyclerView.setAdapter(mAdapter);
```
运行！
>>#### 横向布局
```
    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
```
>>#### Grid布局
```
    mLayoutManager = new GridLayoutManager(context,columNum);
    mRecyclerView.setLayoutManager(mLayoutManager);
    注意，在Grid布局中也可以设置列表的Orientation属性，来实现横向和纵向的Grid布局。
```
>>瀑布流布局
```
瀑布流就使用StaggeredGridLayoutManager吧，具体方法与上面类似，就不做介绍啦。
```