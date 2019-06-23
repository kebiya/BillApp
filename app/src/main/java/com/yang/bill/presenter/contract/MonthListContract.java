package com.yang.bill.presenter.contract;

import com.yang.bill.base.BaseContract;
import com.yang.bill.model.bean.local.BBill;
import com.yang.bill.model.bean.local.MonthListBean;

public interface MonthListContract extends BaseContract {

    interface ViewRenderer extends BaseViewRenderer {

        void loadDataSuccess(MonthListBean list);

    }

    interface Presenter extends BaseContract.BasePresenter<ViewRenderer> {

        void getMonthList(String id, String year, String month);

        void deleteBill(Long id);

        void updateBill(BBill bBill);
    }
}
