package com.example.myapplication_tv2;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication_tv2.adapters.SecondCategoryAdapter;
import com.example.myapplication_tv2.adapters.videoAdapter;
import com.example.myapplication_tv2.adapters.videoItem;
import com.example.myapplication_tv2.myWidget.TvRecyclerView;
import com.example.myapplication_tv2.myWidget.V7LinearLayoutManager;
import com.example.myapplication_tv2.util.Loger;

import java.util.LinkedList;
import java.util.List;

import static android.view.View.inflate;

//处理 二级分类 和 其分类下的内容显示
public class TwoListActivity extends Activity {
    //电视剧分类目录
    String[] SoapDataList = new String[] {
            "最热","最受好评","TVB","内地","韩剧","美剧","顶级剧场","港台", "偶像",
            "古装","家庭","神话","喜剧","战争"};
    List<videoItem> videoList = new LinkedList<videoItem>() ;

    String curCategory = "";        //当下选择了哪个Second分类？

    //传递数据相关
    Bundle bundle;
    String whichButton;
    Intent intentToDetail;
    Bundle bundleToDetail;

    //UI
    RecyclerView recyclerView_video;
    TvRecyclerView recyclerView_category;
    GridLayoutManager videoLayoutManager;
    V7LinearLayoutManager soapLayoutManager;

    videoAdapter videoAdapter;
    SecondCategoryAdapter secondCategoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_list);

        //来自主页， 读取intent的数据 给bundle对象， 并且获取其中数据
        bundle = this.getIntent().getExtras();
        whichButton = bundle.getString("whichButton");

        //----------------------初始化所有组件/类 -------------------------------------
        initializeAll();

        //判断来自那个按钮 请求 ,加载分类目录
        if(whichButton.equals("soap")){  loadSoapCategoryItems(); }

        //去玩detail
        intentToDetail = new Intent(this,VideoDetailActivity.class);
        bundleToDetail = new Bundle();
    }

    void initializeAll(){
        //获取组件
        recyclerView_category = findViewById(R.id.Soap_category_list);
        soapLayoutManager = new V7LinearLayoutManager(this);
        soapLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_category.setLayoutManager(soapLayoutManager);

        recyclerView_video = findViewById(R.id.Soap_video_list);
        videoLayoutManager = new GridLayoutManager(this, 4);
        videoLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_video.setLayoutManager(videoLayoutManager);

       //View view =  inflate(this,R.layout.soap_category_item,recyclerView_category);
       //recyclerView_category.addView(view);
    }

    @SuppressLint("ResourceAsColor")
    void loadSoapCategoryItems(){
        //绑定Adapter  到Soap_category_list view
        secondCategoryAdapter = new SecondCategoryAdapter(this,SoapDataList);
        recyclerView_category.setAdapter(secondCategoryAdapter);

        //---------------------------MyRecyclerView的父视图失去焦点 和 获得焦点 事件监听------------------
        recyclerView_category.setGainFocusListener(new TvRecyclerView.FocusGainListener() {
            @Override
            public void onFocusGain(View child, View focued) {
                SecondCategoryAdapter.ViewHolder viewHolder = (SecondCategoryAdapter.ViewHolder)
                        recyclerView_category.findViewHolderForAdapterPosition(recyclerView_category.getmLastFocusPosition());
                TextView textView = viewHolder.getmTextView();
                textView.setTextColor(Color.parseColor("#FFFFFFFF"));


                Loger.i("centerRv onFocusGain/父视图得到焦点");
            }
        });
        recyclerView_category.setFocusLostListener(new TvRecyclerView.FocusLostListener() {
            @Override
            public void onFocusLost(View lastFocusChild, int direction) {
                SecondCategoryAdapter.ViewHolder viewHolder = (SecondCategoryAdapter.ViewHolder)
                        recyclerView_category.findViewHolderForAdapterPosition(recyclerView_category.getmLastFocusPosition());
                TextView textView = viewHolder.getmTextView();
                textView.setTextColor(R.color.purple_700);

                Loger.i("centerRv onFocusLost/父视图失去焦点");
            }
        });

        //---------------------------重写SoapAdapter的 自定义焦点回调方法-----------------------------------------
        secondCategoryAdapter.setOnItemFocusChangeListener(new SecondCategoryAdapter.OnRecyclerViewItemFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onMyFocusChange(View view, int position, Context mContext) {
                if (view.hasFocus()){
                    curCategory = SoapDataList[position];
                    Toast.makeText(mContext,"你的焦点 改到了第" + position + "项,也就是 " + SoapDataList[position],Toast.LENGTH_SHORT).show();

                    //加载video们, 当焦点切换时显示
                    initData();
                    loadVideoItems();
                }
            }
        });

        //--------测试--------
    }

    void initData(){
        //判断聚焦了哪个 分类， 以此加载该类别下的video
        switch (curCategory){
            case "最热":
                videoList.clear();          //格式化List
                videoList.add(new videoItem(R.drawable.h_small_4_wanmei,"完美世界","更新到500集"));
                videoList.add(new videoItem(R.drawable.h_big_2_wife,"双世宠妃","更新到56集"));
                for(int i=0; i<10; i++) {
                    videoList.add(new videoItem(R.drawable.iv_post_ry_1, "白蛇传", "更新到100集"));
                }
                break;
            case "最受好评":
                videoList.clear();          //格式化List
                for(int i=0; i<20; i++) {
                    videoList.add(new videoItem(R.drawable.kaijia, "轩辕剑", "更新到10000集"));
                }
                break;
        }

    }

    void loadVideoItems(){
        //给adapter绑定数据
        videoAdapter = new videoAdapter(videoList);
        recyclerView_video.swapAdapter(videoAdapter,true);
        //recyclerView_video.setAdapter(videoAdapter);

        //设置click监听 跳转视频详细网站
        videoAdapter.setmOnVideoClickListener(new videoAdapter.OnVideosClickListener() {
            @Override
            public void onMyClick(View view, int position) {
                bundleToDetail.putString("videoTitle",videoList.get(position).getTitle());
                intentToDetail.putExtras(bundleToDetail);

                startActivity(intentToDetail);
            }
        });
    }

}