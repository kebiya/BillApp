package com.yang.bill.base;


public abstract class BaseMVPFragmentRenderer<T extends BaseContract.BasePresenter> extends BaseFragment
        implements BaseContract.BaseViewRenderer {

    protected T mPresenter;

    protected abstract T bindPresenter();

    @Override
    protected void processLogic(){
        mPresenter = bindPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
