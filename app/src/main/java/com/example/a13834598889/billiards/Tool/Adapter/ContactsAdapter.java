package com.example.a13834598889.billiards.Tool.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.a13834598889.billiards.FragmentCustomerMine.thired.FragmentIM;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.MainActivity;
import com.example.a13834598889.billiards.R;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static cn.bmob.v3.BmobRealTimeData.TAG;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsHolder> {
    private List<User> users;
    private Context context;
    private FragmentManager fragmentManager;
    private String target;

    public ContactsAdapter(List<User> users, Context context, FragmentManager fragmentManager, String target) {
        Log.e(TAG, "ContactsAdapter: " + users.size() + " " + users.get(0).getObjectId());
        this.users = users;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.target = target;
    }


    @Override
    public ContactsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new ContactsHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsHolder holder, int position) {
        final User user = users.get(position);
        holder.bindView(user);
        holder.imLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
                    reConnect();
                } else {
                    if (target.equals("liebiao")) {
                        ifliebiao(user);
                    }
                    if (target.equals("huihua")) {
                        ifhuihua(user);
                    }
                }
            }
        });
    }

    private void ifliebiao(final User user) {
        Log.e(TAG, "onClick: 聊天自己：" + User.getCurrentUser().getObjectId() + " " + User.getCurrentUser().getUsername());
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getObjectId().equals(user.getObjectId())) {
                            Log.e(TAG, "onClick: 聊天对方：" + list.get(i).getObjectId() + " " + list.get(i).getUsername() + " " + list.get(i).getPicture_head().getFileUrl());
                            BmobIMUserInfo info = new BmobIMUserInfo(list.get(i).getObjectId(), list.get(i).getNickName(), list.get(i).getPicture_head().getFileUrl());
                            BmobIMConversation conversation = BmobIM.getInstance().startPrivateConversation(info, null);
                            MainActivity.customerNavigation.setVisibility(View.GONE);
                            fragmentManager.beginTransaction()
                                    .hide(fragmentManager.findFragmentByTag("text_button_wodeqiuyou"))
                                    .remove(fragmentManager.findFragmentByTag("text_button_wodeqiuyou"))
                                    .add(R.id.fragment_container, FragmentIM.newInstance(list.get(i).getNickName(), list.get(i).getObjectId(), conversation), "im_Layout")
                                    .commit();
                        }
                    }
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    private void ifhuihua(final User user) {
//        Log.e(TAG, "onClick: 聊天自己：" + User.getCurrentUser().getObjectId() + " " + User.getCurrentUser().getUsername());
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getObjectId().equals(user.getObjectId())) {
//                            Log.e(TAG, "onClick: 聊天对方：" + list.get(i).getObjectId() + " " + list.get(i).getUsername() + " " + list.get(i).getPicture_head().getFileUrl());
                            BmobIMUserInfo info = new BmobIMUserInfo(list.get(i).getObjectId(), list.get(i).getNickName(), list.get(i).getPicture_head().getFileUrl());
                            BmobIMConversation conversation = BmobIM.getInstance().startPrivateConversation(info, null);
                            MainActivity.customerNavigation.setVisibility(View.GONE);
                            fragmentManager.beginTransaction()
                                    .remove(fragmentManager.findFragmentByTag("huihua"))
                                    .add(R.id.fragment_container, FragmentIM.newInstance(list.get(i).getNickName(), list.get(i).getObjectId(), conversation), "im_Layout")
                                    .commit();
                        }
                    }
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    private void reConnect() {
        Toast.makeText(context, "正在连接", Toast.LENGTH_SHORT).show();
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
}
