package com.yang.bill.presenter;


import com.yang.bill.base.BaseObserver;
import com.yang.bill.base.RxPresenter;
import com.yang.bill.model.bean.local.BBill;
import com.yang.bill.model.repository.LocalRepository;
import com.yang.bill.presenter.contract.MonthListContract;
import com.yang.bill.utils.BillUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MonthListPresenter extends RxPresenter<MonthListContract.ViewRenderer> implements MonthListContract.Presenter{

    private String TAG="MonthListPresenter";

    @Override
    public void getMonthList(String id, String year, String month) {
        LocalRepository.getInstance().getBBillByUserIdWithYM(id, year, month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BBill>>() {
                    @Override
                    protected void onSuccees(List<BBill> bBills) throws Exception {
                        mView.loadDataSuccess(BillUtils.packageDetailList(bBills));
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        mView.onFailure(e);
                    }
                });
    }

    @Override
    public void deleteBill(Long id) {
        LocalRepository.getInstance().deleteBBillById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Long>() {
                    @Override
                    protected void onSuccees(Long l) throws Exception {
                        mView.onSuccess();
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        mView.onFailure(e);
                    }
                });
    }

    @Override
    public void updateBill(BBill bBill) {
        LocalRepository.getInstance()
                .updateBBill(bBill)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BBill>() {
                    @Override
                    protected void onSuccees(BBill bBill) throws Exception {
                        mView.onSuccess();
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        mView.onFailure(e);
                    }
                });
    }
}
