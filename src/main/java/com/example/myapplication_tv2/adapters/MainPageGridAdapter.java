package com.example.myapplication_tv2.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication_tv2.R;

//import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 首页展示所有视频的列表， 不是统一布局， 而是变化的布局
 */
public class MainPageGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<videoItem> mData;
    private Context mContext;

    private static final int HORIZON_BIG_2 = 1;
    private static final int HORIZON_SMALL_4 = 2;
    private static final int VERTICAL_SMALL_6 = 4;

    private static final int SUBTITLE = 101;

    //listener
    View.OnFocusChangeListener mOnFocusChangeListener;
    View.OnClickListener mOnClickListener;

    public MainPageGridAdapter(Context mContext,List<videoItem> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    public int getItemViewType(int position) {
        if (mData.get(position).getType() == 1) {
            return HORIZON_BIG_2;
        } else if (mData.get(position).getType() == 2) {
            return HORIZON_SMALL_4;
        } else if (mData.get(position).getType() == 4){
            return  VERTICAL_SMALL_6;
        } else if(mData.get(position).getType() == 101){
            return SUBTITLE;
        }

        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if( viewType < 100 ) {          // video items
            View view = null;
            ViewHolderOne viewHolderOne = null;
            if (viewType == HORIZON_BIG_2) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_h_big_2, parent, false);

            } else if (viewType == HORIZON_SMALL_4) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_h_small_4, parent, false);
            } else if (viewType == VERTICAL_SMALL_6) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_v_small_6, parent, false);
            }

            viewHolderOne = new ViewHolderOne(view);

            return viewHolderOne;
        }else if( viewType > 100 ){         // subtitle
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subtitle, parent, false);;
            ViewHolderTwo viewHolderTwo = new ViewHolderTwo(view);

            return viewHolderTwo;
        }

        return  null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if( holder instanceof ViewHolderOne ) { onBindViewHolderOne( (ViewHolderOne) holder, position ); }
        if( holder instanceof ViewHolderTwo ) { onBindViewHolderOne( (ViewHolderTwo) holder, position ); }
    }
    public void onBindViewHolderOne(ViewHolderOne holder, int position) {
        //绑定一个OnFocusChangeListener
        initFocusListener();

        //绑定数组数据 到对应匹配视图
        holder.mImgPoster.setImageResource(mData.get(position).getPoster());
        holder.mTextViewTitle.setText(mData.get(position).getTitle());
        holder.mTextViewInfo.setText(mData.get(position).getInfotext());

        holder.itemView.setTag(position);
        holder.itemView.setOnFocusChangeListener(mOnFocusChangeListener);
    }
    public void onBindViewHolderOne(ViewHolderTwo holder, int position) {
        holder.mTextViewSubtitle.setText(mData.get(position).getInfotext());

        holder.itemView.setTag(position);
        holder.itemView.setOnFocusChangeListener(mOnFocusChangeListener);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolderOne extends RecyclerView.ViewHolder {
        ImageView mImgPoster;
        TextView mTextViewTitle;
        TextView mTextViewInfo;

        public ViewHolderOne(View itemView) {
            super(itemView);
            mImgPoster = itemView.findViewById(R.id.video_image);
            mTextViewTitle = itemView.findViewById(R.id.video_title);
            mTextViewInfo = itemView.findViewById(R.id.video_info);
        }
    }

    public static class ViewHolderTwo extends RecyclerView.ViewHolder {
        TextView mTextViewSubtitle;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            mTextViewSubtitle = itemView.findViewById(R.id.text_subtitle);

        }
    }

    void initFocusListener(){
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
                    ViewCompat.animate(v).scaleX(1.1f).scaleY(1.1f).translationZ(2).start();

                }else {
                    v.setOutlineSpotShadowColor(R.color.lb_default_brand_color);
                    //v.setScaleX(1f);
                    //v.setScaleY(1f);
                    ViewCompat.animate(v).scaleX(1f).scaleY(1f).translationZ(1).start();
                }
            }
        };
    }

    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                //设置实时 该item在该行所占的格子
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type){
                        case HORIZON_BIG_2:
                            return 6;
                        case HORIZON_SMALL_4:
                            return 3;
                        case VERTICAL_SMALL_6:
                            return 2;
                        case SUBTITLE:
                            return 12;
                        default:
                            return 3;
                    }
                }
            });
        }
    }


}
