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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import datasolve.UserInfoManager;
import objects.User;
import tools.StatusBarUtil;
import tools.UserInfo;

public class WelcomePage extends Activity implements View.OnClickListener {

    private EditText userAccountId;
    private EditText userAccountPassword;
    private Button userLogin;
    private TextView toRegisterPage,toResetPasswordPage;

    private long mPressedTime = 0; //用户双击返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcomepage);

        init();
    }

    /*
    * 初始化控件,加载产品ID,改变状态栏颜色
    * */
    private void init(){
        userAccountId = (EditText)findViewById(R.id.userLAccount);
        userAccountPassword = (EditText)findViewById(R.id.userLPassword);
        userLogin = (Button)findViewById(R.id.userLogin);
        toRegisterPage = (TextView)findViewById(R.id.userLRegister);
        toResetPasswordPage = (TextView)findViewById(R.id.resetPassword);
        //添加监听事件
        userLogin.setOnClickListener(this);
        toRegisterPage.setOnClickListener(this);
        toResetPasswordPage.setOnClickListener(this);
        //产品ID
        Bmob.initialize(this,"338495c72de698ddd259bf683455ebe3");
        // 修改状态栏颜色
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        StatusBarUtil.setTranslucentStatus(this);
        //初始时判断本地是否保有账号
        User user = UserInfo.getUserInfo(this);
        if(user.getUserAccountPassword()!=null&&user.getUserAccountId()!=null){
            userAccountId.setText(user.getUserAccountId());
            userAccountPassword.setText(user.getUserAccountPassword());
            userLogin(user);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.userLogin:
                String userAccountIdStr = userAccountId.getText().toString();
                String userAccountPasswordStr = userAccountPassword.getText().toString();

                User user = new User();
                user.setUserAccountId(userAccountIdStr);
                user.setUserAccountPassword(userAccountPasswordStr);
                userLogin(user);
                break;
            case R.id.userLRegister:
                Intent intent = new Intent(this,RegisterPage.class);
                Bundle bundle = new Bundle();
                bundle.putString("updateType","register");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.resetPassword:
                Intent intent1 = new Intent(this,RegisterPage.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("updateType","reset");
                intent1.putExtras(bundle1);
                startActivity(intent1);
                finish();
                break;


        }

    }

    private void userLogin(User user){
       new UserInfoManager().login(this,user);
    }

    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        }
        else{//退出程序
            this.finish();
            System.exit(0);
        }
    }
}
