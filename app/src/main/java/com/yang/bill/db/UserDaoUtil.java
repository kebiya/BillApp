package com.yang.bill.db;


import com.yang.bill.model.bean.remote.User;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class UserDaoUtil {

    private static final boolean DUBUG = true;
    private DaoDbHelper helper;
    private UserDao userDao;
    private DaoSession daoSession;

    public UserDaoUtil() {
        helper = DaoDbHelper.getInstance();
        daoSession = helper.getSession();
    }

    /**
     * 添加数据，如果有重复则覆盖
     */
    public void insertUser(User user) {
        daoSession.insertOrReplace(user);
    }

    /**
     * 添加多条数据，需要开辟新的线程
     */
    public void insertMultUser(final List<User> users) {
        daoSession.runInTx(new Runnable() {
            @Override
            public void run() {
                for (User user : users) {
                    daoSession.insertOrReplace(user);
                }
            }
        });
    }


    /**
     * 删除数据
     */
    public void deleteStudent(User user) {
        daoSession.delete(user);
    }

    /**
     * 删除全部数据
     */
    public void deleteAll(Class cls) {
        daoSession.deleteAll(cls);
    }

    /**
     * 更新数据
     */
    public void updateUser(User user) {
        daoSession.update(user);
    }

    /**
     * 按照主键返回单条数据
     */
    public User listOneUser(long key) {
        return daoSession.load(User.class, key);
    }

    /**
     * 根据指定条件查询数据
     */
    public List<User> queryUser(String userName) {
        //查询构建器
        QueryBuilder<User> builder = daoSession.queryBuilder(User.class);
        List<User> list = builder.where(UserDao.Properties.UserName.eq(userName)).list();//.where(UserDao.Properties.UserName.like("王小二")).list();
        return list;
    }

    /**
     * 查询全部数据
     */
    public List<User> queryAll() {
        return daoSession.loadAll(User.class);
    }
}
