package com.yang.bill.base;

/**
 * Contract基类
 */
public interface BaseContract {

    interface BasePresenter<T> {

        void attachView(T view);

        void detachView();
    }

    interface BaseViewRenderer {

        void onSuccess();

        void onFailure(Throwable e);
    }
}
