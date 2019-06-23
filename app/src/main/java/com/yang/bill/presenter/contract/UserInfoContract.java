package com.yang.bill.presenter.contract;

import com.yang.bill.base.BaseContract;
import com.yang.bill.model.bean.remote.User;


public interface UserInfoContract extends BaseContract {

    interface ViewRenderer extends BaseViewRenderer {

    }

    interface Presenter extends BasePresenter<ViewRenderer>{
        /**
         * 更新用户信息
         */
        void updateUser(User user);
    }
}
