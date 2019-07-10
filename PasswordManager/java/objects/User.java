/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package objects;

import cn.bmob.v3.BmobObject;

public class User extends BmobObject{

    private String userAccountId;
    private String userAccountPassword;

    public String getUserAccountId() {
        return userAccountId;
    }

    public User setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
        return this;
    }

    public String getUserAccountPassword() {
        return userAccountPassword;
    }

    public User setUserAccountPassword(String userAccountPassword) {
        this.userAccountPassword = userAccountPassword;
        return this;
    }
}
