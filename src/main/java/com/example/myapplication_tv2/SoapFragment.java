package com.example.myapplication_tv2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.app.Fragment;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SoapFragment extends Fragment implements View.OnFocusChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String content="123";

    //当前主Activity的 context
    public Context mContext;

    //去往video list
    Intent intent;
    Bundle bundle;

    //UI
    View fragmentSoapLayout;      //整个Fragment XML界面
    FrameLayout fl1,fl2,fl3,fl4,fl5;
    //RelativeLayout fl1;

    @SuppressLint("ValidFragment")
    public SoapFragment(String content) {
        this.content = content;
    }

    public SoapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity();

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //绑定对应的fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentSoapLayout = inflater.inflate(R.layout.fragment_soap, container, false);

        bindViews();

        return fragmentSoapLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }///////////

    @Override
    public void onStart() {
        super.onStart();

        RelativeLayout relativeLayout = fragmentSoapLayout.findViewById(R.id.container_items);  //1级
        int childCount = relativeLayout.getChildCount();
        Log.i("子视图数量", String.valueOf(childCount));

        FrameLayout child1 = (FrameLayout) relativeLayout.getChildAt(0);      //2级
        TextView child1_2 = (TextView) child1.getChildAt(1);                        //3级
        child1_2.setText("新奥特曼");

    }

    public void bindViews(){
        fl1 = fragmentSoapLayout.findViewById(R.id.item1);
        fl2 = fragmentSoapLayout.findViewById(R.id.item2);
        fl3 = fragmentSoapLayout.findViewById(R.id.item3);
        fl4 = fragmentSoapLayout.findViewById(R.id.item4);
        fl5 = fragmentSoapLayout.findViewById(R.id.item5);

        /*fl1.setOnFocusChangeListener(this);
        fl2.setOnFocusChangeListener(this);
        fl3.setOnFocusChangeListener(this);
        fl4.setOnFocusChangeListener(this);
        fl5.setOnFocusChangeListener(this);*/

        FrameLayout child1st;
        RelativeLayout relativeLayout = fragmentSoapLayout.findViewById(R.id.container_items);  //1级
        int childCount = relativeLayout.getChildCount();
        for(int i = 0; i < childCount; i++){
            child1st = (FrameLayout) relativeLayout.getChildAt(i);
            child1st.setOnFocusChangeListener(this);
        }

        fl1.setOnClickListener(v -> {
            Intent intent1 = new Intent(mContext,MediaPlayerTestActivity.class);    //去 测试系统自带 播放器
            startActivity(intent1);
        });
        fl2.setOnClickListener((v) -> {
            Intent intent1 = new Intent(mContext,IjkMediaPlayerTestActivity.class);     // 去 测试 ijk播放器
            startActivity(intent1);
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            v.setScaleX(1.3f);
            v.setScaleY(1.3f);
        } else {
            v.setScaleY(1f);
            v.setScaleX(1f);
        }
    }
}