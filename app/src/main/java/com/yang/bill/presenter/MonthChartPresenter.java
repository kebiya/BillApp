package com.yang.bill.presenter;

import com.yang.bill.base.BaseObserver;
import com.yang.bill.base.RxPresenter;
import com.yang.bill.model.bean.local.BBill;
import com.yang.bill.model.repository.LocalRepository;
import com.yang.bill.presenter.contract.MonthChartContract;
import com.yang.bill.utils.BillUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MonthChartPresenter extends RxPresenter<MonthChartContract.ViewRenderer> implements MonthChartContract.Presenter{

    private String TAG="MonthChartPresenter";

    @Override
    public void getMonthChart(String id, String year, String month) {
        LocalRepository.getInstance().getBBillByUserIdWithYM(id, year, month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BBill>>() {
                    @Override
                    protected void onSuccees(List<BBill> bBills) throws Exception {
                        mView.loadDataSuccess(BillUtils.packageChartList(bBills));
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        mView.onFailure(e);
                    }
                });
    }
}
