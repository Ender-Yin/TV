package com.example.myapplication_tv2.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication_tv2.R;

import java.util.List;

/**
 * 展示所有视频的列表 4*4， 统一布局
 */
public class videoAdapter extends RecyclerView.Adapter<videoAdapter.ViewHolder>{
    Intent intent;

    //data
    List<videoItem> mData;

    //listener
    View.OnFocusChangeListener mOnFocusChangeListener;
    View.OnClickListener mOnClickListener;

    public videoAdapter(){

    }

    public videoAdapter(List<videoItem> mData){
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( videoAdapter.ViewHolder holder, int position) {
        //绑定一个OnFocusChangeListener
        initListener();

        //绑定数组数据 到对应匹配视图
        holder.mImgPoster.setImageResource(mData.get(position).getPoster());
        holder.mTextViewTitle.setText(mData.get(position).getTitle());
        holder.mTextViewInfo.setText(mData.get(position).getInfotext());

        /*List<videoItem> videoList = new LinkedList<videoItem>();
        videoList.add(new videoItem(R.drawable.iv_post_ry_1, "白蛇传", "更新到100集"));
        videoAdapter videoAdapter = new videoAdapter(videoList);
        holder.recyclerView.setAdapter(videoAdapter);*/

        //设置监听器
        holder.itemView.setTag(position);
        holder.itemView.setOnFocusChangeListener(mOnFocusChangeListener);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //ViewHolder类
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImgPoster;
        TextView mTextViewTitle;
        TextView mTextViewInfo;
        //RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImgPoster = itemView.findViewById(R.id.video_image);
            mTextViewTitle = itemView.findViewById(R.id.video_title);
            mTextViewInfo = itemView.findViewById(R.id.video_info);

        }
    }

    void initListener(){
        //重写一个自己的OnFocusChangeListener
        mOnFocusChangeListener = new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    v.setOutlineSpotShadowColor(R.color.lb_control_button_color);
                    //v.setScaleX(1.2f);
                    //v.setScaleY(1.2f);
                    ViewCompat.animate(v).scaleX(1.10f).scaleY(1.10f).translationZ(2).start();

                }else {
                    v.setOutlineSpotShadowColor(R.color.lb_default_brand_color);
                    //v.setScaleX(1f);
                    //v.setScaleY(1f);
                    ViewCompat.animate(v).scaleX(1f).scaleY(1f).translationZ(1).start();
                }
            }
        };

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnVideoClickListener.onMyClick(v, (Integer) v.getTag());
            }
        };
    }

    //---------------提供回调函数-------------
    private OnVideosClickListener mOnVideoClickListener = null;

    public  interface OnVideosClickListener{
        void onMyClick(View view,int position);
    }

    public void setmOnVideoClickListener(OnVideosClickListener m){
        this.mOnVideoClickListener =  m;
    }

}
