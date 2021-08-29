package com.example.myapplication_tv2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Fragment;
import android.widget.Toast;

import com.example.myapplication_tv2.adapters.FirstCategoryAdapter;
import com.example.myapplication_tv2.myWidget.MainMenuRecyclerView;
import com.example.myapplication_tv2.myWidget.V7LinearLayoutManager;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Objects;

public class MainActivity extends Activity{

    private static final int UPDATE_FOCUS = 1  ;
    //UI
    FrameLayout frag_content;
    MainMenuRecyclerView recyclerView_main;
    V7LinearLayoutManager mainLayoutManager;
    FirstCategoryAdapter mainAdapter;

    //Fragment Object
    private Fragment fg1;
    private MovieFragment fg2;
    private FragmentManager fManager;
    FragmentTransaction fTransaction;

    //intent  去玩下一个activity
    Intent intent;
    Bundle bundle;

    //data
    String[] mainMenuList = {"电视剧","电影","TV","推荐","少儿","少儿","少儿","少儿","少儿","少儿","少儿","少儿","少儿"};
    String currentMenu = "";
    int AppLaunchTime = 0;

    private final String tag = "TV";

    private final Handler mHandlerFocusFirst = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_FOCUS:
                    focusOnFirstItem();
                    //mHandlerUpdateTime.sendEmptyMessageDelayed(UPDATE_TIME, 500);     //不断往mHandler 发送更新时间的信息
                    return true;
            }
            return  false;
        }
    }) ;

    @SuppressLint("CommitTransaction")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bindViews();
        initAll();

        fManager = getFragmentManager();

        //主导航栏 去往 二级分类activity
        intent = new Intent(this, TwoListActivity.class);
        bundle = new Bundle();

        //0.5S后 聚焦第一个选项
        if(AppLaunchTime == 0) {
            mHandlerFocusFirst.sendEmptyMessageDelayed(UPDATE_FOCUS, 1000);
            //AppLaunchTime += 1;     //只第一次打开APP时执行
        }

        //------------------------
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String key = this.getString(R.string.pref_key_player);       // key值名字 保存在String资源中
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("mytest","找到了 放在本地的用户偏好设置");
        editor.apply();

        String value = mSharedPreferences.getString("mytest", "没找到preference");       // 根据key值 在本地用户偏好设置 中查找当前偏好播放器
        Log.i("本地查找: ", value);
    }

    void focusOnFirstItem(){
        Objects.requireNonNull(mainLayoutManager.findViewByPosition(0)).requestFocus();
        //recyclerView_main.getmLastFocusView().requestFocus();
    }
    @Override
    protected void onStart() {
        //mainLayoutManager.findViewByPosition(0).requestFocus();
        super.onStart();
        //recyclerView_main.requestFocus();
    }

    @SuppressLint("CommitTransaction")
    private void initAll() {

        recyclerView_main = findViewById(R.id.menu_recycler_view);
        mainLayoutManager = new V7LinearLayoutManager(this   );
        mainLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_main.setLayoutManager(mainLayoutManager);

        mainAdapter = new FirstCategoryAdapter(this, mainMenuList);
        recyclerView_main.setAdapter(mainAdapter);

        //Objects.requireNonNull(mainLayoutManager.findViewByPosition(0)).requestFocus();

        //---------------------------MyRecyclerView的这视图失去焦点 和 获得焦点 事件监听------------------
       recyclerView_main.setGainFocusListener(new MainMenuRecyclerView.FocusGainListener() {
           @Override
           public void onFocusGain(View child, View focued) {
               FirstCategoryAdapter.ViewHolder viewHolder = (FirstCategoryAdapter.ViewHolder)
                       recyclerView_main.findViewHolderForAdapterPosition(recyclerView_main.getmLastFocusPosition());
               Button button = viewHolder.getmButton();
               button.setBackgroundResource(R.drawable.mainpage_menu_btn);

               //当一级导航栏 聚焦后 显示 个人登录栏
               View view = findViewById(R.id.testview1);    view.setVisibility(View.VISIBLE);
           }
       });
       recyclerView_main.setFocusLostListener(new MainMenuRecyclerView.FocusLostListener() {
           @Override
           public void onFocusLost(View lastFocusChild, int direction) {
               FirstCategoryAdapter.ViewHolder viewHolder = (FirstCategoryAdapter.ViewHolder)
                       recyclerView_main.findViewHolderForAdapterPosition(recyclerView_main.getmLastFocusPosition());
               Button button = viewHolder.getmButton();
               button.setBackgroundResource(R.drawable.mainpage_menu_btn_lostfocus);
           }
       });

        //修改Adapter回调接口函数
        mainAdapter.setOnItemFocusChangeListener((view, position, mContext) -> {
            currentMenu = mainMenuList[position];
            Toast.makeText(mContext,"你选择了第" + position + "项,也就是 " + mainMenuList[position],Toast.LENGTH_SHORT).show();

            loadFragment();
        });
        mainAdapter.setOnItemClickListener((view, position, mContext) -> {
            currentMenu = mainMenuList[position];

            onClick();
        });

    }

    private void bindViews() {
        frag_content = findViewById(R.id.fragment_content);
    }

    public void onClick() {
        //启动下一个activity 传入按下哪个菜单按钮
        switch (currentMenu) {
            case "电视剧":
                bundle.putString("whichButton","soap");     //放入 这是哪个按钮
                intent.putExtras(bundle);                   //放入 bundle包
                startActivity(intent);                      //启程
            case "电影":

        }

    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        // 如果还没有生成fragment 则不用隐藏
        if(fg1 != null)fragmentTransaction.hide(fg1);
        if(fg2 != null)fragmentTransaction.hide(fg2);
    }

    public void loadFragment() {
        fTransaction = fManager.beginTransaction();         //!!!!!!!!!!必须放这里
        hideAllFragment(fTransaction);
        //fTransaction.commitAllowingStateLoss();

        //---------------------------显示各自的fragment界面---------------------
        switch (currentMenu) {
            case "电视剧":
                if (fg1 == null) {
                    fg1 = new SoapFragment();
                    fTransaction.add(R.id.fragment_content, fg1);
                    //fTransaction.commit();
                } else {
                    fTransaction.show(fg1);
                }
                break;

            case "电影":
                if (fg2 == null) {
                    fg2 = new MovieFragment();
                    fTransaction.add(R.id.fragment_content, fg2);
                    //fTransaction.commit();
                } else {
                    fTransaction.show(fg2);
                }
                break;
        }
        fTransaction.commit();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            fg2.getRecyclerView().smoothScrollToPosition(0);
            recyclerView_main.getmLastFocusView().requestFocus();       //回到 上次记忆视图，重新聚焦

            return false;       //消费了
        }

        return super.dispatchKeyEvent(event);
    }
}