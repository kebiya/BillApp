package com.yang.bill.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.yang.bill.R;
import com.yang.bill.base.BaseActivity;
import com.yang.bill.db.UserDaoUtil;
import com.yang.bill.model.bean.remote.User;
import com.yang.bill.utils.ProgressUtils;
import com.yang.bill.utils.SharedPUtils;
import com.yang.bill.utils.SnackbarUtils;
import com.yang.bill.utils.ToastUtils;
import com.yang.bill.widget.CommonItemLayout;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.widget.Toolbar;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 *
 * 设置activity
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private CommonItemLayout changeCL;
    private CommonItemLayout sortCL;
    private CommonItemLayout payCL;

    private User currentUser;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
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
        changeCL = findViewById(R.id.cil_change);
        sortCL = findViewById(R.id.cil_sort);
        payCL = findViewById(R.id.cil_pay);

        //初始化Toolbar
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

    }

    @Override
    protected void initClick() {
        super.initClick();
        changeCL.setOnClickListener(this);
        sortCL.setOnClickListener(this);
        payCL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cil_change:  //修改密码
                showChangeDialog();
                break;

            case R.id.cil_sort:  //账单分类管理
                startActivity(new Intent(mContext,BillSortActivity.class));
                break;
            case R.id.cil_pay:  //支付方式管理
//                startActivity(new Intent(this,PayEditActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 显示忘记密码对话框
     */
    public void showForgetPwDialog() {
        new MaterialDialog.Builder(this)
                .title("备注")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("请输入注册邮箱", null, (dialog, input) -> {
                    if (input.equals("")) {
                        SnackbarUtils.show(mContext, "内容不能为空！");

                    } else {
                        //找回密码
                        BmobUser.resetPasswordByEmail(input.toString(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastUtils.show(mContext, "重置密码请求成功，请到邮箱进行密码重置操作");
                                } else {
                                    ToastUtils.show(mContext, "失败:" + e.getMessage());
                                }
                            }
                        });
                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .show();
    }

    /**
     * 显示修改密码对话框
     */
    public void showChangeDialog() {

        new MaterialDialog.Builder(mContext)
                .title("修改密码")
                .customView(R.layout.dialog_change_password, false)
                .positiveText("确定")
                .onPositive((dialog, which) -> {
                    View view = dialog.getCustomView();
                    TextInputLayout til = view.findViewById(R.id.change_til_password);
                    TextInputLayout til1 = view.findViewById(R.id.change_til_repassword);
                    String passport = til.getEditText().getText().toString();
                    String repaspsort = til.getEditText().getText().toString();
                    if (passport.equals("") || repaspsort.equals("")) {
                        ToastUtils.show(mContext, "不能为空！");
                    } else if (passport.equals(repaspsort)) {
                        //修改密码
                        changePw(passport);
                    } else {
                        ToastUtils.show(mContext, "两次输入不一致！");
                    }
                })
                .negativeText("取消")
                .show();
    }


    /**
     * 更新用户密码
     */
    public void changePw(String password) {
        if (currentUser == null)
            return;

        ProgressUtils.show(mContext, "正在修改...");
        currentUser.setPassword(password);
        UserDaoUtil userDaoUtil = new UserDaoUtil();
        SharedPUtils.setUser(this, currentUser);
        userDaoUtil.updateUser(currentUser);
        ProgressUtils.dismiss();
        ToastUtils.show(this, "修改成功");
    }
}
