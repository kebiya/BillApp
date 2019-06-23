package com.yang.bill.presenter.contract;

import com.yang.bill.base.BaseContract;
import com.yang.bill.model.bean.local.BSort;
import com.yang.bill.model.bean.local.NoteBean;

import java.util.List;


public interface BillNoteContract extends BaseContract {

    interface ViewRenderer extends BaseViewRenderer {

        void loadDataSuccess(NoteBean bean);

    }

    interface Presenter extends BasePresenter<ViewRenderer>{
        /**
         * 获取信息
         */
        void getBillNote();

        void updateBBsorts(List<BSort> items);

        void addBSort(BSort bSort);
        void deleteBSortByID(Long id);
    }
}
