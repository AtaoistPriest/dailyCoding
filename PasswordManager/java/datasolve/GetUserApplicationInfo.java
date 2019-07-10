/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package datasolve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.passwordmanager.ApplicationDetailInfos;

import java.util.ArrayList;
import java.util.List;

import adapter.ApplicationinfosAdapter;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import objects.HidePassword;
import objects.User;
import tools.UserInfo;

public class GetUserApplicationInfo {

    public void getUserAllApplicationInfos(final Context context, final ListView listView){

        User user = UserInfo.getUserInfo(context);
        String userAccountId = user.getUserAccountId();
        BmobQuery<HidePassword> bmobQuery = new BmobQuery<HidePassword>();
        bmobQuery.addWhereEqualTo("userAccountId",userAccountId);
        bmobQuery.findObjects(new FindListener<HidePassword>() {
            @Override
            public void done(final List<HidePassword> list, BmobException e) {
                if(e==null){
                    ListAdapter adapter = new ApplicationinfosAdapter(context,(ArrayList<HidePassword>) list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HidePassword hidePassword = list.get(position);
                            String name = hidePassword.getApplicationName();
                            Bundle bundle = new Bundle();
                            bundle.putString("searchContent",name);
                            Intent intent = new Intent(context, ApplicationDetailInfos.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }
                    });
                }else{
                    Log.e("---Bmob---",e.toString());
                }
            }
        });
    }
}
