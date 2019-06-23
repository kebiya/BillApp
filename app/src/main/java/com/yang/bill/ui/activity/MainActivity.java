package com.yang.bill.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.yang.bill.R;
import com.yang.bill.common.Constants;
import com.yang.bill.model.bean.local.BSort;
import com.yang.bill.model.bean.local.NoteBean;
import com.yang.bill.model.bean.remote.User;
import com.yang.bill.model.repository.LocalRepository;
import com.yang.bill.ui.adapter.MainFragmentPagerAdapter;
import com.yang.bill.base.BaseActivity;
import com.yang.bill.ui.fragment.MonthChartFragment;
import com.yang.bill.ui.fragment.MonthListFragment;
import com.yang.bill.utils.DateUtils;
import com.yang.bill.utils.GlideCacheUtil;
import com.yang.bill.utils.SharedPUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.yang.bill.utils.ToastUtils;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

/**
 * 主界面activity
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView tOutcome;
    private TextView tIncome;
    private TextView tTotal;

    private View drawerHeader;
    private ImageView drawerIv;
    private TextView drawerTvAccount;

    private MyBroadcastReceiver broadcastReceiver;

    protected static final int USERINFOACTIVITY_CODE = 0;
    protected static final int LOGINACTIVITY_CODE = 1;

    // Tab
    private FragmentManager mFragmentManager;
    private MainFragmentPagerAdapter mFragmentPagerAdapter;
    private MonthListFragment monthListFragment;
    private MonthChartFragment monthChartFragment;


    private User currentUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //注册广播接收器
        broadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.yang.bill.user");
        registerReceiver(broadcastReceiver, intentFilter);
        //第一次进入将默认账单分类添加到数据库
        if (SharedPUtils.isFirstStart(mContext)) {
            Log.i(TAG, "第一次进入将默认账单分类添加到数据库");
            NoteBean note = new Gson().fromJson(Constants.BILL_NOTE, NoteBean.class);
            List<BSort> sorts = note.getOutSortlis();
            sorts.addAll(note.getInSortlis());
            LocalRepository.getInstance().saveBsorts(sorts);
            LocalRepository.getInstance().saveBPays(note.getPayinfo());
        }

        monthListFragment = new MonthListFragment();
        monthChartFragment = new MonthChartFragment();
    }

    @Override
    protected void initView() {
        super.initView();
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.main_viewpager);
        drawer = findViewById(R.id.main_drawer);
        navigationView = findViewById(R.id.main_nav_view);
        tOutcome = findViewById(R.id.t_outcome);
        tIncome = findViewById(R.id.t_income);
        tTotal = findViewById(R.id.t_total);

        //初始化Toolbar
        toolbar.setTitle("个人记账本 ");
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawerHeader = navigationView.inflateHeaderView(R.layout.drawer_header);
        drawerIv = drawerHeader.findViewById(R.id.drawer_iv);
        drawerTvAccount = drawerHeader.findViewById(R.id.drawer_tv_name);

        //设置头部账户
        setDrawerHeaderAccount();

        //初始化ViewPager
        mFragmentManager = getSupportFragmentManager();
        mFragmentPagerAdapter = new MainFragmentPagerAdapter(mFragmentManager);
        mFragmentPagerAdapter.addFragment(monthListFragment, "明细");
        mFragmentPagerAdapter.addFragment(monthChartFragment, "图表");

        monthListFragment.setMonthListListener((outcome, income, total) -> {
            tOutcome.setText(outcome);
            tIncome.setText(income);
            tTotal.setText(total);
        });

        viewPager.setAdapter(mFragmentPagerAdapter);

        //初始化TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("明细"));
        tabLayout.addTab(tabLayout.newTab().setText("图表"));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initClick() {
        super.initClick();
        //监听侧滑菜单项
        navigationView.setNavigationItemSelectedListener(this);
        //监听侧滑菜单头部点击事件
        drawerHeader.setOnClickListener(v -> startActivityForResult(new Intent(mContext, UserInfoActivity.class), USERINFOACTIVITY_CODE));
    }

    /**
     * 设置toolbar右侧菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_date:
                //时间选择器
                new TimePickerBuilder(mContext, (Date date, View v) -> {
                    monthListFragment.changeDate(DateUtils.date2Str(date, "yyyy"), DateUtils.date2Str(date, "MM"));
                    monthChartFragment.changeDate(DateUtils.date2Str(date, "yyyy"), DateUtils.date2Str(date, "MM"));
                }).setType(new boolean[]{true, true, false, false, false, false})
                        .setRangDate(null, Calendar.getInstance())
                        .isDialog(true)//是否显示为对话框样式
                        .build().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 监听左滑菜单
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_sync:    //同步账单
                ToastUtils.show(mContext, "此功能尚未上线");
                break;
            case R.id.nav_setting:
                startActivity(new Intent(mContext,SettingActivity.class));
                break;
            case R.id.nav_about:
                startActivity(new Intent(mContext,AboutActivity.class));
                break;
            case R.id.nav_exit:
                exitUser();
                break;
            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * 退出登陆 Dialog
     */
    private void exitUser(){
        new MaterialDialog.Builder(mContext)
                .title("确认退出")
                .content("推出登陆将清除所有数据")
                .positiveText("确定")
                .onPositive((dialog, which) -> {
                    GlideCacheUtil.getInstance().clearImageDiskCache(mContext);
                    SharedPUtils.setIsLogined(this,false);
                    //清除本地数据
                    LocalRepository.getInstance().deleteAllBills();
                    startActivity(new Intent(this, LandActivity.class));
                    finish();
                })
                .negativeText("取消")
                .show();
    }

    /**
     * 监听Activity返回值
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case USERINFOACTIVITY_CODE:
                    setDrawerHeaderAccount();
                    break;
                case LOGINACTIVITY_CODE:
                    setDrawerHeaderAccount();
                    break;
            }
        }
    }

    /**
     * 设置DrawerHeader的用户信息
     */
    public void setDrawerHeaderAccount() {
        currentUser = SharedPUtils.getUser(this);
        if (currentUser != null) {
            drawerTvAccount.setText(currentUser.getUserName());
            if(currentUser.getImage() != null){
                Uri uri = Uri.parse(currentUser.getImage());
                Glide.with(mContext).load(uri).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(drawerIv);
            }else{
                drawerIv.setImageResource(R.drawable.icon);
            }
        } else {
            drawerTvAccount.setText("账号");
            drawerIv.setImageResource(R.mipmap.ic_def_icon);
        }
    }

    @Override
    public void onDestroy() {
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            setDrawerHeaderAccount();
        }
    }
}
