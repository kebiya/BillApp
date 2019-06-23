package com.yang.bill.presenter;


import com.yang.bill.base.RxPresenter;
import com.yang.bill.db.UserDaoUtil;
import com.yang.bill.model.bean.remote.User;
import com.yang.bill.presenter.contract.UserInfoContract;


public class UserInfoPresenter extends RxPresenter<UserInfoContract.ViewRenderer>
        implements UserInfoContract.Presenter {

    private String TAG = "UserInfoPresenter";
    private UserDaoUtil userDaoUtil = new UserDaoUtil();

    @Override
    public void updateUser(User user) {
        userDaoUtil.updateUser(user);
        mView.onSuccess();
    }
}
