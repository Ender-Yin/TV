package com.example.myapplication_tv2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication_tv2.adapters.FirstCategoryAdapter;
import com.example.myapplication_tv2.adapters.MainPageGridAdapter;
import com.example.myapplication_tv2.adapters.videoItem;
import com.example.myapplication_tv2.myWidget.GridRecyclerView;
import com.example.myapplication_tv2.myWidget.MainMenuRecyclerView;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //--------------------------------------------------------------------------
    GridRecyclerView recyclerView;
    //当前主Activity的 context
    public Context mContext;

    String content ;
    View view;

    @SuppressLint("ValidFragment")
    public MovieFragment(String movie) {
        this.content = movie;
    }

    public MovieFragment(){

    }
    public GridRecyclerView getRecyclerView(){return recyclerView;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity();       //获取主活动的 context

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_mainpage_videos);
        GridLayoutManager videoLayoutManager = new GridLayoutManager(mContext, 12);
        videoLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(videoLayoutManager);

        //-----------放入数据
        List<videoItem> videoList = new LinkedList<videoItem>() ;
        videoList.add(new videoItem(R.drawable.h_big_2_jingang, "金刚大战哥斯拉！！！", "更新到100集",1) );
        videoList.add(new videoItem(R.drawable.h_big_2_wife, "言情偶像大剧！！！！", "更新到100集",1) );

        for (int i = 1; i<=8; i++){
            videoList.add(new videoItem(R.drawable.h_small_4_wanmei, "玄幻大片！！！！", "更新到100集",2) );
        }

        videoList.add(new videoItem("首播影院 · 大片尽情畅想", 101) );
        for (int i = 1; i<=6; i++){
            videoList.add(new videoItem(R.drawable.kaijia, "轩辕剑", "火热",4) );
        }

        videoList.add(new videoItem("首播影院 · 大片尽情畅想", 101) );
        for (int i = 1; i<=12; i++){
            videoList.add(new videoItem(R.drawable.kaijia, "轩辕剑", "火热",4) );
        }

        MainPageGridAdapter mainPageGridAdapter = new MainPageGridAdapter(mContext,videoList);
        recyclerView.setAdapter(mainPageGridAdapter);

        //-----------声明滑倒监听 网格视频列表滑动时 ，隐藏顶部栏
        View.OnScrollChangeListener onScrollChangeListener = new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(recyclerView.hasFocus()) {
                    int a = -1;
                    Toast.makeText(mContext, "正在滑动--- 当前位置： " + a, Toast.LENGTH_SHORT).show();
                    View view = getActivity().findViewById(R.id.testview1);
                    view.setVisibility(View.GONE);
                    // 消失后就 清空滑动监听
                    recyclerView.setOnScrollChangeListener(null);
                }
            }
        };
        //----------设置监听-列表获取焦点时，设置滑动监听实例
        recyclerView.setGainFocusListener(new MainMenuRecyclerView.FocusGainListener() {
            @Override
            public void onFocusGain(View child, View focued) {
                recyclerView.setOnScrollChangeListener(onScrollChangeListener);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);
    }



}