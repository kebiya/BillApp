package com.yang.bill.presenter.contract;

import com.yang.bill.base.BaseContract;
import com.yang.bill.model.bean.local.BBill;
import com.yang.bill.model.bean.local.NoteBean;


public interface BillContract extends BaseContract {

    interface ViewRenderer extends BaseViewRenderer {

        void loadDataSuccess(NoteBean bean);

    }

    interface Presenter extends BasePresenter<ViewRenderer>{
        /**
         * 获取信息
         */
        void getBillNote();

        /**
         * 添加账单
         */
        void addBill(BBill bBill);

        /**
         * 修改账单
         */
        void updateBill(BBill bBill);


        /**
         * 删除账单
         */
        void deleteBillById(Long id);
    }
}
