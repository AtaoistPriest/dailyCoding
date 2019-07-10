/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.passwordmanager.R;

import java.util.ArrayList;

import objects.HidePassword;

public class ApplicationinfosAdapter extends BaseAdapter {

    private ArrayList<HidePassword> list;

    private Context context;

    public ApplicationinfosAdapter(Context context, ArrayList<HidePassword> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View layoutView = View.inflate(context, R.layout.applicationname_account,null);
        TextView name = (TextView)layoutView.findViewById(R.id.applicationNames);
        TextView account = (TextView)layoutView.findViewById(R.id.applicationAccounts);
        HidePassword hidePassword = list.get(position);
        name.setText(hidePassword.getApplicationName());
        account.setText(hidePassword.getApplicationAccount());
        return layoutView;
    }
}
