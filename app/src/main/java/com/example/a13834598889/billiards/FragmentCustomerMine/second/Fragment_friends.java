package com.example.a13834598889.billiards.FragmentCustomerMine.second;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a13834598889.billiards.JavaBean.Friend;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.Adapter.ContactsAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.BmobRealTimeData.TAG;


/**
 * Created by 13834598889 on 2018/5/3.
 */

public class Fragment_friends extends Fragment {

    private RecyclerView recyclerView_friends;
    private List<User> users = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    private ImageView addImageView;
    private ImageView imageView_back;
    private boolean isLast = false;
    private ContactsAdapter adapter;

    private FragmentManager fragmentManager;

    public static Fragment_friends newInstance() {
        return new Fragment_friends();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();

        initViews(view);
        initClisks();
        initData();
        return view;
    }

    private void initClisks() {
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " + fragmentManager.findFragmentById(R.id.fragment_container).getTag());
                if (fragmentManager.findFragmentByTag("text_button_wodeqiuyou") != null) {
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("text_button_wodeqiuyou"))
                            .remove(fragmentManager.findFragmentByTag("text_button_wodeqiuyou"))
                            .show(fragmentManager.findFragmentByTag("fragment_mine"))
                            .commit();
                }
            }
        });
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(getContext());

                Dialog dialog = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("请输入好友的账号")
                        .setView(editText)
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                final User user=new User();
                                user.setUsername(editText.getText().toString());
                                BmobQuery<User> userBmobQuery=new BmobQuery<>();
                                userBmobQuery.findObjects(new FindListener<User>() {
                                    @Override
                                    public void done(List<User> list, BmobException e) {
                                        if (e==null){
                                            getUser(list, user);
                                        }else {
                                            Log.e(TAG, "done: "+e.getMessage() );
                                        }
                                    }
                                });

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                dialog.setCanceledOnTouchOutside(true);
            }
        });
    }

    private void initViews(View view) {
        addImageView = view.findViewById(R.id.circleImageView_mine88);
        recyclerView_friends = view.findViewById(R.id.contacts_list_recycler_view);
        imageView_back = view.findViewById(R.id.fragment_friends_back);
    }

    private void getUser(List<User> list, User user) {
        boolean have=false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUsername().equals(user.getUsername())){
                have = true;
                if (list.get(i).getObjectId().equals(User.getCurrentUser(User.class).getObjectId())){
                    Toast.makeText(getContext(), "不能添加自己", Toast.LENGTH_SHORT).show();
                }else{
                    have=true;
                    final User testUser=list.get(i);
                    BmobQuery<Friend> query=new BmobQuery<>();
                    query.findObjects(new FindListener<Friend>() {
                        @Override
                        public void done(List<Friend> list, BmobException e) {
                            boolean isFriend=false;
                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getUser().getObjectId().equals(User.getCurrentUser().getObjectId())&&
                                        list.get(j).getFriendUser().getObjectId().equals(testUser.getObjectId())){
                                    isFriend=true;
                                }
                            }
                            if (isFriend){
                                Toast.makeText(getContext(), "你们已经是好友了", Toast.LENGTH_SHORT).show();
                            }else {
                                Friend friend=new Friend();
                                friend.setUser(User.getCurrentUser(User.class));
                                friend.setFriendUser(testUser);
                                friend.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e==null){
                                            Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                            if (fragmentManager.findFragmentByTag("circleImageView_mine88") != null) {
                                                fragmentManager.beginTransaction()
                                                        .hide(fragmentManager.findFragmentByTag("circleImageView_mine88"))
                                                        .remove(fragmentManager.findFragmentByTag("circleImageView_mine88"))
                                                        .add(R.id.fragment_container, Fragment_friends.newInstance(),"text_button_wodeqiuyou")
                                                        .commit();
                                            }
                                        }else {
                                            Log.e(TAG, "done: "+e.getMessage() );
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
        if (!have){
            Toast.makeText(getContext(), "不存在此账号", Toast.LENGTH_SHORT).show();
        }
    }

    private void reConnect() {
        Toast.makeText(getContext(), "正在连接im...", Toast.LENGTH_SHORT).show();
        BmobIM.connect(User.getCurrentUser().getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Log.e(TAG, "done: im连接成功");
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                Log.e(TAG, "onChange: " + BmobIM.getInstance().getCurrentStatus().getMsg());
            }
        });
    }

    private void initData() {
        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            reConnect();
        }
        BmobQuery<Friend> friendBmobQuery = new BmobQuery<>();
        friendBmobQuery.findObjects(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getUser().getObjectId().equals(User.getCurrentUser().getObjectId())) {
                            stringList.add(list.get(i).getFriendUser().getObjectId());
                        }
                    }
                    if (stringList.size() != 0) {
                        for (int i = 0; i < stringList.size(); i++) {
                            if (stringList.size() - 1 == i) {
                                isLast = true;
                            }
                            User user = new User();
                            user.setObjectId(stringList.get(i));
                            users.add(user);
                            if (isLast) {
                                setRecycleViews();
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    private void setRecycleViews() {
        adapter = new ContactsAdapter(users, getContext(), fragmentManager,"liebiao");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_friends.setLayoutManager(layoutManager);
        recyclerView_friends.setAdapter(adapter);
    }
}
