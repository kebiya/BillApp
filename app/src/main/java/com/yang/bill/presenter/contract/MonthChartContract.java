package com.yang.bill.presenter.contract;

import com.yang.bill.base.BaseContract;
import com.yang.bill.model.bean.local.MonthChartBean;


public interface MonthChartContract extends BaseContract {

    interface ViewRenderer extends BaseViewRenderer {

        void loadDataSuccess(MonthChartBean bean);

    }

    interface Presenter extends BasePresenter<ViewRenderer> {

        void getMonthChart(String id, String year, String month);
    }
}
