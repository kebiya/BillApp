package com.yang.bill;

import android.app.Application;
import android.content.Context;

import com.yang.bill.model.bean.remote.User;
import com.yang.bill.utils.SharedPUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class MyApplication extends Application {

    public static MyApplication application;
    private static Context context;
    private static User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = getApplicationContext();
        //初始化Bmob后端云
        Bmob.initialize(this, "941f4add6503358048b02b83fcb605f6");
        currentUser = BmobUser.getCurrentUser(User.class);
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 获取用户id
     * @return
     */
    public static String getCurrentUserId() {
        currentUser = SharedPUtils.getUser(context);
        if (currentUser == null)
            return null;
        return currentUser.getId().toString();
    }
}
