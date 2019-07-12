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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import datasolve.GetUserApplicationInfo;
import tools.StatusBarUtil;
import tools.UserInfo;

public class MainActivity extends Activity implements View.OnClickListener{

    private long mPressedTime = 0; //用户双击返回

    private TextView changeUserAccountPassword,exitCurAccount,addNewApplication;
    private EditText applicationName;
    private Button search;

    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init(){

        changeUserAccountPassword = (TextView)findViewById(R.id.changeUserAccountPassword);
        exitCurAccount = (TextView)findViewById(R.id.exitCurrentUserAccount);
        addNewApplication = (TextView)findViewById(R.id.addNewApplication);
        applicationName = (EditText)findViewById(R.id.applicationName);
        search = (Button)findViewById(R.id.search);
        listView = (ListView)findViewById(R.id.lists);

        changeUserAccountPassword.setOnClickListener(this);
        exitCurAccount.setOnClickListener(this);
        addNewApplication.setOnClickListener(this);
        search.setOnClickListener(this);

        //产品ID
        Bmob.initialize(this,"338495c72de698ddd259bf683455ebe3");
        // 修改状态栏颜色
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        StatusBarUtil.setTranslucentStatus(this);

        initView();
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

    public void initView(){
        new GetUserApplicationInfo().getUserAllApplicationInfos(this,listView);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.changeUserAccountPassword:
                Intent intent1 = new Intent(this,RegisterPage.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("updateType","reset");
                intent1.putExtras(bundle1);
                startActivity(intent1);
                finish();
                break;

            case R.id.exitCurrentUserAccount:
                UserInfo.removeUserInfo(this);
                Intent intent2 = new Intent(this,WelcomePage.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.addNewApplication:
                Intent intent3 = new Intent(this,AddNewAccount.class);
                Bundle bundle = new Bundle();
                bundle.putString("applicationName",null);
                bundle.putString("applicationAccount",null);
                bundle.putString("option","addNew");
                bundle.putString("objectId",null);
                intent3.putExtras(bundle);
                startActivity(intent3);
                startActivity(intent3);
                break;
            case R.id.search:
                String applicationNameSearching = applicationName.getText().toString();
                Bundle bundle2 = new Bundle();
                bundle2.putString("searchContent",applicationNameSearching);
                Intent intent = new Intent(this,ApplicationDetailInfos.class);
                intent.putExtras(bundle2);
                startActivity(intent);
                finish();
                break;
        }

    }
}
