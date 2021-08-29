package com.example.myapplication_tv2.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication_tv2.R;

/**
 * 第二级别分类 如 喜剧、恐怖、历史
 */

public class SecondCategoryAdapter extends RecyclerView.Adapter<SecondCategoryAdapter.ViewHolder> implements View.OnFocusChangeListener{
    String[] mDataList;
    Context mContext;       //当前Activity的context

    public SecondCategoryAdapter(Context c, String[] data){
        this.mDataList = data;
        this.mContext = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //生成item布局视图
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.soap_category_item, parent, false);
        
        //传入视图 往holder
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(SecondCategoryAdapter.ViewHolder holder, int position) {
        //重写一个自己的OnFocusChangeListener
        initFocusListener();

        //绑定数组数据 到对应匹配视图， 每个position处的
        holder.mTextView.setText(mDataList[position]);

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.soap_category_title);

            //设置聚焦监听器
            itemView.setOnFocusChangeListener(SecondCategoryAdapter.this);
        }

        public TextView getmTextView(){
            return mTextView;
        }
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.hasFocus()) {
            v.setOutlineSpotShadowColor(R.color.lb_control_button_color);
            //v.setScaleX(1.2f);
            //v.setScaleY(1.2f);
        }else {
            v.setOutlineSpotShadowColor(R.color.lb_default_brand_color);
            //v.setScaleX(1f);
            //v.setScaleY(1f);
        }

        //使用 接口的函数
        mOnItemFocusChangeListener.onMyFocusChange(v, (int)v.getTag(), this.mContext);
    }

    //自定义的初始化方法
    void initFocusListener(){

    }

    //------------------提供回调函数 给activity用----------------------------
    private OnRecyclerViewItemFocusChangeListener mOnItemFocusChangeListener = null;

    public interface OnRecyclerViewItemFocusChangeListener {
        void onMyFocusChange(View view, int position,Context mContext);
    }

    public void setOnItemFocusChangeListener(OnRecyclerViewItemFocusChangeListener listener) {
        this.mOnItemFocusChangeListener = listener;
    }

}
