/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package datasolve;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.passwordmanager.AddNewAccount;
import com.example.passwordmanager.ApplicationDetailInfos;
import com.example.passwordmanager.ApplicationInfos;
import com.example.passwordmanager.MainActivity;
import com.example.passwordmanager.R;
import com.example.passwordmanager.WelcomePage;

import java.util.ArrayList;
import java.util.List;

import adapter.ApplicationDetailInfosAdapter;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import objects.HidePassword;
import objects.User;
import tools.Base64;
import tools.DES;
import tools.UserInfo;


/*
* 与Bmob后台进行交互
* */
public class UserInfoManager {

    /*
    * 登录
    * */
    public void login(final Context context, User user){

        String userAccountId = user.getUserAccountId();
        final String userAccountPassword = user.getUserAccountPassword();
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("userAccountId",userAccountId);
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    if(list.size()==1){
                        User user1 = list.get(0);
                        user1.setUserAccountPassword(new String(Base64.decode(user1.getUserAccountPassword())));
                        if(user1.getUserAccountPassword().equals(userAccountPassword)){
                            UserInfo.saveUserInfo(context,user1);
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }else{
                            Toast.makeText(context, "登录失败!密码错误！", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context, "登录失败!账号不存在！", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Log.e("-------BMOB-------",e.toString());
                    Toast.makeText(context, "登录失败!网络状况良好后重试！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /*
    * 查重
    * */
    public void find(final Context context, User user){

        String userAccountId = user.getUserAccountId();
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("userAccountId",userAccountId);
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    if(list.size()==1){
                        Toast.makeText(context, "账户已存在！！！", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Log.e("-------BMOB-------",e.toString());
                }
            }
        });
    }
    /*
    * 注册
    * */
    public void register(final Context context, final User user){

        user.setUserAccountPassword(Base64.encode(user.getUserAccountPassword().getBytes()));
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    user.setUserAccountPassword(new String(Base64.decode(user.getUserAccountPassword())));
                    UserInfo.saveUserInfo(context,user);
                    Intent intent = new Intent(context, ApplicationInfos.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }else{
                    Toast.makeText(context, "注册失败！！！", Toast.LENGTH_LONG).show();
                    find(context,user);
                }
            }
        });
    }
    /*
    * 修改密码
    * */
    public void changePassword(final Context context,final User user){

        user.update(user.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    user.setUserAccountPassword(new String(Base64.decode(user.getUserAccountPassword())));
                    UserInfo.saveUserInfo(context,user);
                    Intent intent = new Intent(context, WelcomePage.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }else{
                    Toast.makeText(context, "修改失败！！！", Toast.LENGTH_LONG).show();
                    Log.e("-------BMOB-------",e.toString());
                }
            }
        });
    }
    /*
    * 找回密码
    * */
    public void resetPassword(final Context context,final User user){

        String userAccountId = user.getUserAccountId();
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("userAccountId",userAccountId);
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    if(list.size()==1){
                        String objectId = list.get(0).getObjectId();
                        user.setObjectId(objectId);
                        user.setUserAccountPassword(Base64.encode(user.getUserAccountPassword().getBytes()));
                        changePassword(context,user);
                    }else{
                        Toast.makeText(context, "账号不存在！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.e("-------BMOB-------",e.toString());
                }
            }
        });
    }
    /*
    * 新增账户
    * */
    public void addNewAccountInfo(final Context context, final HidePassword hidePassword){
        hidePassword.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }else{
                    Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
                    findApplicationAccount(context,hidePassword);
                }
            }
        });
    }
    public void findApplicationAccount(final Context context,final HidePassword hidePassword){
        BmobQuery<HidePassword> eq1 = new BmobQuery<>();
        eq1.addWhereEqualTo("userAccountId",UserInfo.getUserInfo(context).getUserAccountId());
        BmobQuery<HidePassword> eq2 = new BmobQuery<>();
        eq2.addWhereEqualTo("applicationName",hidePassword.getApplicationName());
        List<BmobQuery<HidePassword>> eqs = new ArrayList<>();
        eqs.add(eq1);
        eqs.add(eq2);
        BmobQuery<HidePassword> eq = new BmobQuery<>();
        eq.and(eqs);
        eq.findObjects(new FindListener<HidePassword>() {
            @Override
            public void done(List<HidePassword> list, BmobException e) {
                if(e==null){
                    if(list.size()>0){
                        Toast.makeText(context, "该应用账户已存在，您可以查询后修改", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Log.e("----Bmob----",e.toString());
                }
            }
        });
    }
    /*
    * 根据应用名搜索
    * */
    public void searchApplicationInfoByName(final Context context,final ListView listView,final String applicationNameStr){
        BmobQuery<HidePassword> eq1 = new BmobQuery<>();
        eq1.addWhereEqualTo("userAccountId",UserInfo.getUserInfo(context).getUserAccountId());
        BmobQuery<HidePassword> eq2 = new BmobQuery<>();
        eq2.addWhereEqualTo("applicationName",applicationNameStr);
        List<BmobQuery<HidePassword>> eqs = new ArrayList<>();
        eqs.add(eq1);
        eqs.add(eq2);
        BmobQuery<HidePassword> eq = new BmobQuery<>();
        eq.and(eqs);
        eq.findObjects(new FindListener<HidePassword>() {
            @Override
            public void done(final List<HidePassword> list, BmobException e) {
                if(e==null){
                    if(list.size()>0){
                        ListAdapter adapter = new ApplicationDetailInfosAdapter(context,(ArrayList<HidePassword>)list);
                        listView.setAdapter(adapter);
                        //点击
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                final HidePassword hidePassword = list.get(position);
                                View viewTmp = View.inflate(context, R.layout.edittext_input2,null);
                                final EditText editText = viewTmp.findViewById(R.id.secretKey);
                                new AlertDialog.Builder(context)
                                        .setTitle("输入您的秘钥")
                                        .setView(viewTmp)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                String secretKey = editText.getText().toString();
                                                secretKey = secretKey + "apxjevth".substring(0,(8-secretKey.length()%8));

                                                String hideSecrets = DES.encrypt(secretKey.getBytes(),secretKey);
                                                //根据密文作为秘钥再加密一次
                                                secretKey = hideSecrets;
                                                secretKey = secretKey+"bueckows".substring(0,(8-secretKey.length()%8));

                                                String hidePassword1 = hidePassword.getApplicationSecret();
                                                byte[] resultBytes = DES.decrypt(Base64.decode(hidePassword1), secretKey);
                                                if(resultBytes==null){
                                                    Toast.makeText(context, "秘钥错误", Toast.LENGTH_LONG).show();
                                                }else{
                                                    String result = new String(resultBytes);
                                                    Toast.makeText(context, "已复制到剪切板，快去粘贴吧！", Toast.LENGTH_SHORT).show();
                                                    copy(context,result);
                                                }
                                            }
                                        })
                                        .setNegativeButton("取消",null)
                                        .show();
                            }
                        });
                        //长按
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                                final HidePassword hidePassword = list.get(position);
                                new AlertDialog.Builder(context)
                                        .setTitle("操作")
                                        .setMessage("选取对该条记录进行的操作!")
                                        //删除
                                        .setNeutralButton("删除", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                list.remove(position);
                                                deleteApplicationInfo(context,listView,hidePassword,list);
                                            }
                                        })
                                        //修改
                                        .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent intent = new Intent(context, AddNewAccount.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("applicationName",hidePassword.getApplicationName());
                                                bundle.putString("applicationAccount",hidePassword.getApplicationAccount());
                                                bundle.putString("option","update");
                                                bundle.putString("objectId",hidePassword.getObjectId());
                                                intent.putExtras(bundle);
                                                context.startActivity(intent);

                                            }
                                        })
                                        .setNegativeButton("取消", null).show();

                                return true;
                            }
                        });
                    }
                }else{
                    Log.e("---Bmob---",e.toString());
                }
            }
        });
    }

    private void deleteApplicationInfo(final Context context, final ListView listView, final HidePassword hidePassword, final List<HidePassword> list){

        new AlertDialog.Builder(context)
                .setTitle("删除")
                .setMessage("注意！你正在选择删除该条记录")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hidePassword.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                    ListAdapter adapter = new ApplicationDetailInfosAdapter(context,(ArrayList<HidePassword>)list);
                                    listView.setAdapter(adapter);
                                }else{
                                    Log.e("--delete--","Error:"+e.toString());
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public void updateApplicationInfo(final Context context,final HidePassword hidePassword){

        hidePassword.update(hidePassword.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }else {
                    Log.e("---update---",e.toString());
                }
            }
        });
    }

    /** * 复制内容到剪切板 * *
     * @param copyStr
     * * @return
     * */
    private boolean copy(Context context,String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型
            ClipData  mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }
}
