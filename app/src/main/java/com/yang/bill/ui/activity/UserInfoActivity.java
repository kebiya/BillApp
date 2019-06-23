package com.yang.bill.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.yang.bill.R;
import com.yang.bill.base.BaseMVPActivity;
import com.yang.bill.db.UserDaoUtil;
import com.yang.bill.model.bean.remote.User;
import com.yang.bill.presenter.UserInfoPresenter;
import com.yang.bill.presenter.contract.UserInfoContract;
import com.yang.bill.utils.ProgressUtils;
import com.yang.bill.utils.SharedPUtils;
import com.yang.bill.utils.StringUtils;
import com.yang.bill.utils.ToastUtils;
import com.yang.bill.widget.CommonItemLayout;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

/**
 * 用户中心activity
 */
public class UserInfoActivity extends BaseMVPActivity<UserInfoContract.Presenter>
        implements UserInfoContract.ViewRenderer, View.OnClickListener {

    private Toolbar toolbar;
    private RelativeLayout iconRL;
    private ImageView iconIv;
    private CommonItemLayout usernameCL;
    private CommonItemLayout sexCL;
    private CommonItemLayout phoneCL;
    private Button btnSave;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    //图片路径
    protected static Uri tempUri = null;

    private User currentUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        currentUser = SharedPUtils.getUser(this);
    }

    @Override
    protected void initView() {
        super.initView();
        toolbar = findViewById(R.id.toolbar);
        iconRL = findViewById(R.id.rlt_update_icon);
        iconIv = findViewById(R.id.img_icon);
        usernameCL = findViewById(R.id.cil_username);
        sexCL = findViewById(R.id.cil_sex);
        phoneCL = findViewById(R.id.cil_phone);
        btnSave = findViewById(R.id.btn_save);

        //初始化Toolbar
        toolbar.setTitle("账户");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            //返回消息更新上个Activity数据
            setResult(RESULT_OK, new Intent());
            finish();
        });

        //加载当前头像
        if(currentUser.getImage() != null){
            Uri uri = Uri.parse(currentUser.getImage());
            Glide.with(mContext).load(uri).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iconIv);
        }else{
            iconIv.setImageResource(R.drawable.icon);
        }
        //添加用户信息
        usernameCL.setRightText(currentUser.getUserName());
        sexCL.setRightText(currentUser.getGender());
        phoneCL.setRightText(currentUser.getPhone() == null? "": currentUser.getPhone());
    }

    @Override
    protected void initClick() {
        super.initClick();
        iconRL.setOnClickListener(this);
        usernameCL.setOnClickListener(this);
        sexCL.setOnClickListener(this);
        phoneCL.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlt_update_icon:  //头像
                showIconDialog();
//                startActivityForResult(
//                        new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
//                        CHOOSE_PICTURE);
                break;
            case R.id.cil_username:  //用户名
                ToastUtils.show(mContext, "用户名无法修改");
                break;
            case R.id.cil_sex:  //性别
                showGenderDialog();
                break;
            case R.id.cil_phone:  //电话修改
                showPhoneDialog();
                break;
            case R.id.btn_save:  //保存
                doUpdate();
                break;
            default:
                break;
        }
    }

    /**
     * 显示选择头像来源对话框
     */
    public void showIconDialog() {
        new MaterialDialog.Builder(mContext)
                .title("选择图片来源")
                .titleGravity(GravityEnum.CENTER)
                .items(new String[]{"相册", "相机"})
                .positiveText("确定")
                .itemsCallbackSingleChoice(0, (dialog, itemView, which, text) -> {
                    switch (which) {
                        case CHOOSE_PICTURE: // 选择本地照片
                            Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            openAlbumIntent.setType("image/*");
                            startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                            break;
                        case TAKE_PICTURE: // 拍照
                            takePicture();
                            break;
                    }
                    dialog.dismiss();
                    return false;
                }).show();
    }

    /**
     * 显示选择性别对话框
     */
    public void showGenderDialog() {

        new MaterialDialog.Builder(mContext)
                .title("选择性别")
                .titleGravity(GravityEnum.CENTER)
                .items(new String[]{"男", "女"})
                .positiveText("确定")
                .negativeText("取消")
                .itemsCallbackSingleChoice(0, (dialog, itemView, which, text) -> {
                    currentUser.setGender(text.toString());
                    sexCL.setRightText(currentUser.getGender());
                    dialog.dismiss();
                    return false;
                }).show();
    }

    /**
     * 显示更换电话对话框
     */
    public void showPhoneDialog() {
        String phone = currentUser.getPhone() == null ? "" : currentUser.getPhone();
        new MaterialDialog.Builder(mContext)
                .title("电话")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(0, 200, R.color.textRed)
                .input("请输入电话号码", phone, (dialog, input) -> {
                    String inputStr = input.toString();
                    if (inputStr.equals("")) {
                        ToastUtils.show(mContext, "内容不能为空！" + input);
                    } else {
                        if (StringUtils.checkPhoneNumber(inputStr)) {
                            //currentUser.setMobilePhoneNumber(inputStr);
                            phoneCL.setRightText(inputStr);
                        } else {
                            Toast.makeText(UserInfoActivity.this,
                                    "请输入正确的电话号码", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .positiveText("确定")
                .show();
    }

    /**
     * 更新用户数据
     */
    public void doUpdate() {
        if (currentUser == null)
            return;
        ProgressUtils.show(UserInfoActivity.this, "正在修改...");
        mPresenter.updateUser(currentUser);
    }

    /**
     * 拍照
     */
    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(UserInfoActivity.this,
                    "com.yang.bill.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }

        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CHOOSE_PICTURE){
                if(data != null && data.getData() != null){
                    currentUser.setImage(data.getData().toString());
                    Glide.with(mContext).load(data.getData()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iconIv);
                }
            }else if(requestCode == TAKE_PICTURE){
                currentUser.setImage(tempUri.toString());
                Glide.with(mContext).load(tempUri).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iconIv);
            }
        }
    }

    /**
     * 权限请求
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected UserInfoContract.Presenter bindPresenter() {
        return new UserInfoPresenter();
    }

    @Override
    public void onSuccess() {
        ProgressUtils.dismiss();
        UserDaoUtil userDaoUtil = new UserDaoUtil();
        List<User> list = userDaoUtil.queryUser(currentUser.getUserName());
        if(list != null || !list.isEmpty()){
            currentUser = list.get(0);
        }
        SharedPUtils.setUser(this, currentUser);
        ToastUtils.show(this, "保存成功");
        Intent intent = new Intent("com.yang.bill.user");
        sendBroadcast(intent);
    }

    @Override
    public void onFailure(Throwable e) {
        ProgressUtils.dismiss();
        ToastUtils.show(mContext, e.getMessage());
    }
}
