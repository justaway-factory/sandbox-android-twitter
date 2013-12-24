package info.justaway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

import info.justaway.JustawayApplication;
import twitter4j.UserList;

public class SubscribeUserListAdapter extends ArrayAdapter<UserList> {

    private ArrayList<UserList> userLists = new ArrayList<UserList>();
    private LayoutInflater inflater;
    private int layout;

    public SubscribeUserListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = textViewResourceId;
    }

    @Override
    public void add(UserList userList) {
        super.add(userList);
        userLists.add(userList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // ビューを受け取る
        View view = convertView;
        if (view == null) {
            // 受け取ったビューがnullなら新しくビューを生成
            view = inflater.inflate(this.layout, null);
        }

        UserList userList = userLists.get(position);

        CheckBox checkbox = (CheckBox) view;
        if (checkbox != null) {
            checkbox.setText(userList.getName());
            checkbox.setChecked(JustawayApplication.getApplication().existsTab(userList.getId()));
            checkbox.setTag(userList.getId());
        }

        return view;
    }

}
