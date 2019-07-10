/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.passwordmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.bmob.v3.Bmob;
import datasolve.UserInfoManager;
import objects.User;
import tools.StatusBarUtil;
import tools.UserInfo;

public class ChangePasswrd extends Activity implements View.OnClickListener {

    private TextView userAccountId,exitChangePassword;
    private EditText userAccountPassword;
    private Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        init();
    }

    public void init(){
        userAccountId = (TextView)findViewById(R.id.userCAccount);
        userAccountPassword = (EditText)findViewById(R.id.userCPassword);
        changePassword = (Button)findViewById(R.id.changePassword);
        exitChangePassword = (TextView)findViewById(R.id.exitChangePassword);

        User user = UserInfo.getUserInfo(this);
        userAccountId.setText(user.getUserAccountId());
        userAccountPassword.setText(user.getUserAccountPassword());

        changePassword.setOnClickListener(this);
        exitChangePassword.setOnClickListener(this);

        //产品ID
        Bmob.initialize(this,"338495c72de698ddd259bf683455ebe3");
        // 修改状态栏颜色
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        StatusBarUtil.setTranslucentStatus(this);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.exitChangePassword:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.changePassword:
                User user = UserInfo.getUserInfo(this);
                user.setUserAccountPassword(userAccountPassword.getText().toString());
                new UserInfoManager().changePassword(this,user);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
