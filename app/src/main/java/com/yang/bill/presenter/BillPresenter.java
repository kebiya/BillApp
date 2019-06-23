package com.yang.bill.presenter;


import com.yang.bill.base.BaseObserver;
import com.yang.bill.base.RxPresenter;
import com.yang.bill.model.bean.local.BBill;
import com.yang.bill.model.repository.LocalRepository;
import com.yang.bill.presenter.contract.BillContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BillPresenter extends RxPresenter<BillContract.ViewRenderer> implements BillContract.Presenter {

    private String TAG = "BillPresenter";

    @Override
    public void getBillNote() {
        //此处采用同步的方式，防止账单分类出现白块
        mView.loadDataSuccess(LocalRepository.getInstance().getBillNote());
    }

    @Override
    public void addBill(BBill bBill) {
        LocalRepository.getInstance().saveBBill(bBill)
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

    @Override
    public void deleteBillById(Long id) {
        LocalRepository.getInstance()
                .deleteBBillById(id);
    }
}
