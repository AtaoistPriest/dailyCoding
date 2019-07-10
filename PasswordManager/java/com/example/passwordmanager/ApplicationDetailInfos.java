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
import android.widget.ListView;
import android.widget.TextView;

import datasolve.UserInfoManager;
import tools.StatusBarUtil;

public class ApplicationDetailInfos extends Activity implements View.OnClickListener {


    private TextView exitTOMain,applicationName;
    private ListView listView;

    private String searchingContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applicationdetailinfos);
        init();
    }

    public void init(){

        exitTOMain = (TextView)findViewById(R.id.exitApplicationDetailInfos);
        listView = (ListView)findViewById(R.id.applicationArray);
        applicationName = (TextView)findViewById(R.id.applicationsNames);

        exitTOMain.setOnClickListener(this);

        searchingContent = this.getIntent().getExtras().getString("searchContent");
        applicationName.setText(searchingContent);
        searchContent(searchingContent);
        // 修改状态栏颜色
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        StatusBarUtil.setTranslucentStatus(this);
    }

    public void searchContent(String searchingContent){
        new UserInfoManager().searchApplicationInfoByName(this,listView,searchingContent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.exitApplicationDetailInfos){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
