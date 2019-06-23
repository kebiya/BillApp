package com.yang.bill.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yang.bill.R;
import com.yang.bill.base.BaseMVPActivity;
import com.yang.bill.model.bean.remote.User;
import com.yang.bill.presenter.LandPresenter;
import com.yang.bill.presenter.contract.LandContract;
import com.yang.bill.utils.ProgressUtils;
import com.yang.bill.utils.SharedPUtils;
import com.yang.bill.utils.ToastUtils;

/**
 * 用户登录、注册activity
 */
public class LandActivity extends BaseMVPActivity<LandContract.Presenter>
        implements LandContract.ViewRendererRenderer, View.OnFocusChangeListener, View.OnClickListener {

    private EditText usernameET;
    private EditText passwordET;
    private EditText rpasswordET;
    private TextView signTV;
    private Button loginBtn;

    //是否是登陆操作
    private boolean isLogin = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_land;
    }

    @Override
    protected void initView() {
        super.initView();
        usernameET=findViewById(R.id.login_et_username);
        passwordET=findViewById(R.id.login_et_password);
        rpasswordET=findViewById(R.id.login_et_rpassword);
        signTV=findViewById(R.id.login_tv_sign);
        loginBtn=findViewById(R.id.login_btn_login);
    }

    @Override
    protected void initClick() {
        super.initClick();
        passwordET.setOnFocusChangeListener(this);
        rpasswordET.setOnFocusChangeListener(this);
        signTV.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:  //button
                if (isLogin) {
                    login();  //登陆
                } else {
                    sign();  //注册
                }
                break;
            case R.id.login_tv_sign:  //sign
                if (isLogin) {
                    //置换注册界面
                    signTV.setText("登录");
                    loginBtn.setText("注册");
                    rpasswordET.setVisibility(View.VISIBLE);
                } else {
                    //置换登陆界面
                    signTV.setText("注册");
                    loginBtn.setText("登录");
                    rpasswordET.setVisibility(View.GONE);
                }
                isLogin = !isLogin;
                break;
            default:
                break;
        }
    }

    /**
     * 执行登陆动作
     */
    public void login() {
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        if (username.length() == 0 || password.length() == 0) {
            ToastUtils.show(mContext, "用户名或密码不能为空");
            return;
        }

        ProgressUtils.show(this, "正在登陆...");

        mPresenter.login(username, password);
    }

    /**
     * 执行注册动作
     */
    public void sign() {
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String rpassword = rpasswordET.getText().toString();
        if (username.length() == 0 || password.length() == 0 || rpassword.length() == 0) {
            ToastUtils.show(mContext, "请填写必要信息");
            return;
        }
        if (!password.equals(rpassword)) {
            ToastUtils.show(mContext, "两次密码不一致");
            return;
        }

        ProgressUtils.show(this, "正在注册...");

        mPresenter.signup(username,password);

    }

    @Override
    protected LandContract.Presenter bindPresenter() {
        return new LandPresenter();
    }

    @Override
    public void landSuccess(User user) {
        ProgressUtils.dismiss();
        if (isLogin) {
            SharedPUtils.setIsLogined(this, true);
            SharedPUtils.setUser(this, user);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else {
            ToastUtils.show(mContext, "注册成功");
        }
        Log.i(TAG,user.toString());
    }

    @Override
    public void showErrorMsg(String msg) {
        ToastUtils.show(this, msg);
        ProgressUtils.dismiss();
    }

    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure(Throwable e) {
        ProgressUtils.dismiss();
        ToastUtils.show(mContext, e.getMessage());
        Log.e(TAG,e.getMessage());
    }
}
