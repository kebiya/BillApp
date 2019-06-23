package com.yang.bill.presenter.contract;

import com.yang.bill.base.BaseContract;
import com.yang.bill.model.bean.remote.User;

/**
 * 登录Contract
 */
public interface LandContract extends BaseContract {

    interface ViewRendererRenderer extends BaseViewRenderer {

        void landSuccess(User user);

        void showErrorMsg(String msg);

    }

    interface Presenter extends BaseContract.BasePresenter<ViewRendererRenderer>{
        /**
         * 用户登陆
         */
        void login(String username, String password);

        /**
         * 用户注册
         */
        void signup(String username, String password);
    }
}
