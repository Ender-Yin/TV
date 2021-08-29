package com.example.myapplication_tv2.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication_tv2.R;

public class FirstCategoryAdapter extends RecyclerView.Adapter<FirstCategoryAdapter.ViewHolder> implements View.OnFocusChangeListener,View.OnClickListener{
    String[] mDataList;
    Context mContext;       //当前Activity的context

    public FirstCategoryAdapter(Context context, String[] data){
        this.mDataList = data;
        this.mContext = context;
    }

    @Override
    public FirstCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //生成item布局视图
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mainpage_menu_item, parent, false);

        //传入视图 往holder
        FirstCategoryAdapter.ViewHolder holder = new FirstCategoryAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(FirstCategoryAdapter.ViewHolder holder, int position) {
        //重写一个自己的OnFocusChangeListener
        initFocusListener();

        //绑定数组数据 到对应匹配视图， 每个position处的
        holder.button.setText(mDataList[position]);

        holder.button.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.main_menu_btn);

            //绑定焦点变化监听
            button.setOnFocusChangeListener(FirstCategoryAdapter.this);
            button.setOnClickListener(FirstCategoryAdapter.this);
        }

        public Button getmButton(){
            return button;
        }
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //调用自定义接口函数
        mOnItemFocusChangeListener.onMyFocusChange(v, (Integer) v.getTag(), this.mContext);
    }
    @Override
    public void onClick(View v) {
        //调用自定义接口函数
        mOnItemClickListener.onMyClick(v, (Integer) v.getTag(), this.mContext);
    }

    //自定义的初始化方法
    void initFocusListener(){

    }

    //1------------------提供回调函数 focusChange 给activity用----------------------------
    private OnRecyclerViewItemFocusChangeListener mOnItemFocusChangeListener = null;

    public interface OnRecyclerViewItemFocusChangeListener {
        void onMyFocusChange(View view, int position, Context mContext);
    }

    public void setOnItemFocusChangeListener(OnRecyclerViewItemFocusChangeListener listener) {
        this.mOnItemFocusChangeListener = listener;
    }
    //2-------------------Click
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onMyClick(View view, int position, Context mContext);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}