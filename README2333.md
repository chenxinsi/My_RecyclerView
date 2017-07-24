# RecyclerView Item点击事件和分割线-----(2)
># Item点击事件
>>### 原理
```
为RecyclerView的每个子item设置setOnClickListener
然后在onClick中再调用一次对外封装的接口
将这个事件传递给外面的调用者
而“为RecyclerView的每个子item设置setOnClickListener”在Adapter中设置
其实直接在onClick中也能完全处理item的点击事件，但是这样会破坏代码的逻辑
```
>>### 步骤
>>>#### 1.在MyAdapter中定义如下接口,模拟ListView的OnItemClickListener：
```
  //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
```
>>>#### 声明一个这个接口的变量
```
private OnItemClickListener mOnItemClickListener = null;
```
>>>#### 在onCreateViewHolder()中为每个item添加点击事件
```
@Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup,  int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }
```
>>>#### 将点击事件转移给外面的调用者：
```
@Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }
```
>>>#### 注意上面调用接口的onItemClick()中的v.getTag()方法，这需要在onBindViewHolder()方法中设置和item的position
```
@Override
    public void onBindViewHolder(ViewHolder viewHolder,  int position) {
        viewHolder.mTextView.setText(datas[position]);
        //将position保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(position);
    }
```
>>>#### 最后暴露给外面的调用者，定义一个设置Listener的方法（）：
```
 public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
```

以上所有步骤都发生在自定义的adapter中，典型的观察者模式，有点绕的地方在于，
这里涉及到两个观察者模式的使用，view的setOnClickListener本来就是观察者模式，我们将这个观察者模式的事件监听传递给了我们自己的观察者模式。
>>>#### 在Activity中使用
```
    mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
    //创建默认的线性LayoutManager
    mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);
    //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
    mRecyclerView.setHasFixedSize(true);
    //创建并设置Adapter
    mAdapter = new MyAdapter(data);
    mRecyclerView.setAdapter(mAdapter);
    mAdapter.setOnItemClickListener(new OnItemClickListener(){
        @Override
        public void onItemClick(View view , int position){
            Toast.makeText(MainActivity.this, data[position], 600).show();
        }
    });
```
># Item分割线
>>>># 我们自定义的MyDecoration.java：（继承RecyclerView.ItemDecoration）
```
package com.study.wnw.recyclerviewdivider;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
//
public class MyDecoration extends RecyclerView.ItemDecoration{
//
    private Context mContext;
    private Drawable mDivider;
    private int mOrientation;
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
//
    //我们通过获取系统属性中的listDivider来添加，在系统中的AppTheme中设置
    public static final int[] ATRRS  = new int[]{
            android.R.attr.listDivider
    };
//
    public MyDecoration(Context context, int orientation) {
        this.mContext = context;
        final TypedArray ta = context.obtainStyledAttributes(ATRRS);
        this.mDivider = ta.getDrawable(0);
        ta.recycle();
        setOrientation(orientation);
    }
//
    //设置屏幕的方向
    public void setOrientation(int orientation){
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw new IllegalArgumentException("invalid orientation");        }        mOrientation = orientation;
    }
//
   @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == HORIZONTAL_LIST){
            drawVerticalLine(c, parent, state);
        }else {
            drawHorizontalLine(c, parent, state);
        }
    }
//
    //画横线, 这里的parent其实是显示在屏幕显示的这部分
    public void drawHorizontalLine(Canvas c, RecyclerView parent, RecyclerView.State state){
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            final View child = parent.getChildAt(i);
//
            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            //Log.d("wnw", left + " " + top + " "+right+"   "+bottom+" "+i);
        }
    }
//
    //画竖线
    public void drawVerticalLine(Canvas c, RecyclerView parent, RecyclerView.State state){
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            final View child = parent.getChildAt(i);
//
           //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
//
    //由于Divider也有长宽高，每一个Item需要向下或者向右偏移
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(mOrientation == HORIZONTAL_LIST){
            //画横线，就是往下偏移一个分割线的高度
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }else {
            //画竖线，就是往右偏移一个分割线的宽度
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}
```
从上面的代码中，我们还通过系统属性来适应屏幕的横屏和竖屏，
然后确定画横的，还是竖的Divider，其实在里面我们做了三件事，
第一件事：获取到系统中的listDivider， 我们就是通过它在主题中去设置的。
第二件事：就是找到我们需要添加Divider的位置，从onDraw方法中去找到，并将Divider添加进去。
第三件事：得到Item的偏移量。

>>>>#### 补充：分割线的设置
```
分隔线Divider的drawable文件：divider..xml
//
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#7b7a7a"/>
    <size android:height="1dp"/>
</shape>
我们在这里面，画了一个：rectangle, 给它填充颜色，还有高度，这样就搞定了，高度小，
显示出来也是一条线：其实线的本质就是长方形。这里可以根据个人需要，画不同类型的divider
//
在styles.xml的AppTheme中，设置listDivider为我们的divider.xml文件：
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="android:listDivider">@drawable/divider</item>
</style>
//
这样，我们将系统的listDivider设置成我们自定义的divider. 还记得我们在MyDecoration中获取系统的listDivider这个属性吗，
这样通过这个属性，我们就可以将我们的divider.xml文件和MyDecoration.java进行关联了。
```
完整代码见
github： https://github.com/chenxinsi/My_RecyclerView