/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package tools;

import android.content.Context;
import android.content.SharedPreferences;

import objects.User;

public final class UserInfo {

    public static void saveUserInfo(Context context, User user){

        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("objectId",user.getObjectId());
        editor.putString("userAccountId",user.getUserAccountId());
        editor.putString("userAccountPassword",user.getUserAccountPassword());
        editor.commit();
    }

    public static User getUserInfo(Context context){

        User user = new User();
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String userAccountId = sharedPreferences.getString("userAccountId",null);
        String userAccountPassword = sharedPreferences.getString("userAccountPassword",null);
        String objectId = sharedPreferences.getString("objectId",null);
        user.setUserAccountId(userAccountId);
        user.setUserAccountPassword(userAccountPassword);
        user.setObjectId(objectId);
        return user;
    }

    public static void removeUserInfo(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
