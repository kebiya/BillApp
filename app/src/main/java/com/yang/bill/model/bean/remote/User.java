package com.yang.bill.model.bean.remote;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {

    //不能用int
    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String userName;

    @NotNull
    private String password;

    private String image;

    private String gender;

    private String phone;

    @Generated(hash = 430853351)
    public User(Long id, @NotNull String userName, @NotNull String password,
            String image, String gender, String phone) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.image = image;
        this.gender = gender;
        this.phone = phone;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
