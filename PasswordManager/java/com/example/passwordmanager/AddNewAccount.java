/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.passwordmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import datasolve.UserInfoManager;
import objects.HidePassword;
import tools.DES;
import tools.StatusBarUtil;
import tools.UserInfo;

public class AddNewAccount extends Activity implements View.OnClickListener {

    private EditText applicationName,applicationAccount,applicationPassword;
    private TextView exitAddNewApplication;
    private Button addNewApplication;

    private String applicationNameString = null;
    private String applicationAccountString = null;
    private String option = "";
    private String objectId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnewaccount);
        init();
    }

    public void init(){
        applicationName = (EditText)findViewById(R.id.applicationAName);
        applicationAccount = (EditText)findViewById(R.id.applicationAAccount);
        applicationPassword = (EditText)findViewById(R.id.applicationAPassword);
        exitAddNewApplication = (TextView)findViewById(R.id.exitAddNewApplication);
        addNewApplication = (Button)findViewById(R.id.addANewApplicationInfo);

        exitAddNewApplication.setOnClickListener(this);
        addNewApplication.setOnClickListener(this);

        //
        Bundle bundle = this.getIntent().getExtras();
        applicationAccountString = bundle.getString("applicationAccount");
        applicationNameString = bundle.getString("applicationName");
        option = bundle.getString("option");
        objectId = bundle.getString("objectId");

        if(applicationNameString!=null&&applicationAccountString!=null){
            applicationName.setText(applicationNameString);
            applicationAccount.setText(applicationAccountString);
        }

        // 修改状态栏颜色
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        StatusBarUtil.setTranslucentStatus(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.exitAddNewApplication:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.addANewApplicationInfo:
                if(option.equals("addNew")){
                    addNewApplication();
                }else{
                    updateApplication();
                }
                break;
        }
    }

    private void updateApplication() {
        final String applicationNameStr = applicationName.getText().toString();
        final String applicationAccountStr = applicationAccount.getText().toString();
        final String applicationPasswordStr = applicationPassword.getText().toString();
        final String objectIdStr = objectId;

        View viewTmp = View.inflate(this, R.layout.edittext_input,null);
        final EditText editText = viewTmp.findViewById(R.id.secretKey);
        final Context context = this;
        new AlertDialog.Builder(this)
                .setTitle("输入您的秘钥")
                .setView(viewTmp)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String secretKey = editText.getText().toString();
                        //加密一次
                        int length = secretKey.length();
                        secretKey = secretKey+"apxjevth".substring(0,(8-length%8));
                        String hideSecrets = DES.encrypt(secretKey.getBytes(),secretKey);
                        //根据密文作为秘钥再加密一次
                        secretKey = hideSecrets;
                        length = secretKey.length();
                        secretKey = secretKey+"bueckows".substring(0,(8-length%8));
                        hideSecrets = DES.encrypt(applicationPasswordStr.getBytes(),secretKey);


                        HidePassword hidePassword = new HidePassword();
                        hidePassword.setUserAccountId(UserInfo.getUserInfo(context).getUserAccountId());
                        hidePassword.setApplicationName(applicationNameStr);
                        hidePassword.setApplicationAccount(applicationAccountStr);
                        hidePassword.setApplicationSecret(hideSecrets);
                        hidePassword.setObjectId(objectIdStr);
                        new UserInfoManager().updateApplicationInfo(context,hidePassword);
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    public void addNewApplication(){


        final String applicationNameStr = applicationName.getText().toString();
        final String applicationAccountStr = applicationAccount.getText().toString();
        final String applicationPasswordStr = applicationPassword.getText().toString();

        View viewTmp = View.inflate(this, R.layout.edittext_input,null);
        final EditText editText = viewTmp.findViewById(R.id.secretKey);
        final EditText editTextConfirm = viewTmp.findViewById(R.id.secretKeyConfirm);
        final Context context = this;
        new AlertDialog.Builder(this)
                .setTitle("输入您的秘钥")
                .setView(viewTmp)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String secretKey = editText.getText().toString();
                        String secretKeyConfirm = editTextConfirm.getText().toString();
                        if(!secretKey.equals(secretKeyConfirm)){
                            Toast.makeText(context, "秘钥不一致，请确认后重新输入！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //秘钥自己对自己加密一次
                        int length = secretKey.length();
                        secretKey = secretKey+"apxjevth".substring(0,(8-length%8));
                        String hideSecrets = DES.encrypt(secretKey.getBytes(),secretKey);
                        //根据密文作为秘钥再加密一次
                        secretKey = hideSecrets;
                        length = secretKey.length();
                        secretKey = secretKey+"bueckows".substring(0,(8-length%8));
                        hideSecrets = DES.encrypt(applicationPasswordStr.getBytes(),secretKey);

                        HidePassword hidePassword = new HidePassword();
                        hidePassword.setUserAccountId(UserInfo.getUserInfo(context).getUserAccountId());
                        hidePassword.setApplicationName(applicationNameStr);
                        hidePassword.setApplicationAccount(applicationAccountStr);
                        hidePassword.setApplicationSecret(hideSecrets);
                        new UserInfoManager().addNewAccountInfo(context,hidePassword);
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }
}
