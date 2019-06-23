package com.yang.bill.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yang.bill.R;
import com.yang.bill.base.BaseActivity;
import com.yang.bill.utils.DialogUtil;
import com.yang.bill.utils.SharedPUtils;
import com.yang.bill.utils.ToastUtils;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 启动页
 *
 */

public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, View.OnClickListener {

    private static final int SKIP_AD = 101;
    private TextView skipAdTv;
    private static final String[] PERSSIONS =
            {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
    private static final int RC_PERSSIONS = 121;
    private int count = 3;
    private Handler handler = new MyHandler();


    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        skipAdTv = findViewById(R.id.skip_ad_tv);
        skipAdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeMessages(SKIP_AD);
                fastLogin();
            }
        });
        enterTask();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //no implementation
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        ToastUtils.show(this, "您拒绝了所申请的权限，将无法进入主界面");
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            DialogUtil.showReqPermissionDialog(this);
        }
    }

    public boolean hasPermissions() {
        return EasyPermissions.hasPermissions(this, PERSSIONS);
    }

    @AfterPermissionGranted(RC_PERSSIONS)
    public void enterTask() {
        if (hasPermissions()) {
            // 所有权限已通过要执行的代码
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    handler.sendEmptyMessageDelayed(SKIP_AD,1000);
                }
            }, 1000);
        } else {
            // 申请权限
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.apply_for_camera_and_storage_rights),
                    RC_PERSSIONS,
                    PERSSIONS);
        }
    }

    @Override
    public void onClick(View v) {
        fastLogin();
    }

    private void fastLogin(){
        if(SharedPUtils.isLogined(this)){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(this, LandActivity.class));
            finish();
        }
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SKIP_AD:
                    if(count == 1){
                        fastLogin();
                    }else{
                        count--;
                        if(mActivity != null){
                            skipAdTv.setVisibility(View.VISIBLE);
                            skipAdTv.setText(getText(R.string.skip_ad) + " " + count);
                            handler.sendEmptyMessageDelayed(SKIP_AD,500);
                        }
                    }
                    break;
            }
        }
    }
}
