package com.yang.bill.presenter;


import android.text.TextUtils;

import com.yang.bill.base.RxPresenter;
import com.yang.bill.model.bean.remote.User;
import com.yang.bill.db.UserDaoUtil;
import com.yang.bill.presenter.contract.LandContract;

import java.util.List;


public class LandPresenter extends RxPresenter<LandContract.ViewRendererRenderer> implements LandContract.Presenter{

    private UserDaoUtil userDaoUtil = new UserDaoUtil();

    @Override
    public void login(String username, String password) {
        List<User> list = userDaoUtil.queryUser(username);
        if(list == null || list.isEmpty()){
            mView.showErrorMsg("此账号尚未注册");
        }else{
            User user = list.get(0);
            if(TextUtils.equals(user.getPassword(), password)){
                mView.landSuccess(user);
            }else{
                mView.showErrorMsg("密码错误");
            }
        }
    }

    @Override
    public void signup(String userName, String password) {
        User user =new User();
        user.setUserName(userName);
        user.setPassword(password);
        userDaoUtil.insertUser(user);
        mView.landSuccess(user);
    }
}
