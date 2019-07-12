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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import datasolve.UserInfoManager;
import objects.User;
import tools.StatusBarUtil;

public class RegisterPage extends Activity implements View.OnClickListener {

    private EditText userAccountId,userAccountPassword,userAccountPasswordConfirm,registerConfirm;
    private TextView toLoginPage,receiverMsg;
    private Button register;

    private boolean receiver = true;
    private int time = 60;

    private long mPressedTime = 0; //用户双击返回

    private String updateType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerpage);
        init();
    }

    private void init(){
        userAccountId = (EditText)findViewById(R.id.userRAccount);
        userAccountPassword = (EditText)findViewById(R.id.userRPassword);
        userAccountPasswordConfirm = (EditText)findViewById(R.id.userRPasswordConfirm);
        toLoginPage = (TextView)findViewById(R.id.userRLogin);
        register = (Button)findViewById(R.id.userRegister);
        receiverMsg = (TextView)findViewById(R.id.receiverMsg);
        registerConfirm = (EditText) findViewById(R.id.registerConfirm);

        toLoginPage.setOnClickListener(this);
        register.setOnClickListener(this);
        receiverMsg.setOnClickListener(this);

        //获取操作类型
        Bundle bundle = this.getIntent().getExtras();
        updateType = bundle.getString("updateType");
        if(updateType.equals("reset")){
            register.setText("找回密码");
        }
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
            case R.id.userRegister:
                Log.e("-----bmob----","no");
                final User user = new User();
                String userAccountIdStr = userAccountId.getText().toString();
                String userAccountPasswordStr = userAccountPassword.getText().toString();
                String userAccountPasswordConfirmStr = userAccountPasswordConfirm.getText().toString();
                String registerConfirmStr = registerConfirm.getText().toString();
                if(userAccountPasswordStr.equals(userAccountPasswordConfirmStr)){
                    user.setUserAccountId(userAccountIdStr);
                    user.setUserAccountPassword(userAccountPasswordStr);
                }else{
                    userAccountPassword.setText("");
                    userAccountPasswordConfirm.setText("");
                    Toast.makeText(this, "密码不一致，请重新输入您的密码", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!registerConfirmStr.equals("")){
                    BmobSMS.verifySmsCode(userAccountIdStr, registerConfirmStr, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(RegisterPage.this, "验证成功!", Toast.LENGTH_SHORT).show();
                                if("register".equals(updateType)){
                                    register(user);
                                }else{
                                    resetPassword(user);
                                }
                            }else{
                                Toast.makeText(RegisterPage.this, "验证失败，请重新输入!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(this, "验证码不能为空！！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.userRLogin:
                Intent intent = new Intent(this,WelcomePage.class);
                startActivity(intent);
                finish();
                break;
            case R.id.receiverMsg:
                if(receiver){
                    String userAccountIdString = userAccountId.getText().toString();
                    if(!userAccountIdString.equals("")){
                        handler.postDelayed(runnable,time*1000);
                        handler.sendEmptyMessage(001);
                        BmobSMS.requestSMSCode(userAccountIdString, "短信验证", new QueryListener<Integer>() {
                            @Override
                            public void done(Integer smsId, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(RegisterPage.this, "验证码已发送！！！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterPage.this, "请务频繁操作！！！", Toast.LENGTH_SHORT).show();
                                    Log.e("----Bmob--SMS---", e.toString());
                                }
                            }
                        });
                    }else {
                        Toast.makeText(this, "请输入账号！！！", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void register(User user){

        new UserInfoManager().register(this,user);
    }

    private void resetPassword(User user){
        new UserInfoManager().resetPassword(this,user);
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

    //启动页倒计时
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what==001){

                time--;
                receiverMsg.setText("稍等("+time+" s)");
                if(time>0){
                    receiver = false;
                    handler.sendEmptyMessageDelayed(001,1000);
                }else{
                    receiver = true;
                    receiverMsg.setText("获取验证码");
                    time = 60;
                }
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
        }
    };
}
