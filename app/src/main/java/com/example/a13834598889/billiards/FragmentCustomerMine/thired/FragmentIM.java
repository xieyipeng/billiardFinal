package com.example.a13834598889.billiards.FragmentCustomerMine.thired;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a13834598889.billiards.FragmentCustomerMine.second.Fragment_friends;
import com.example.a13834598889.billiards.MainActivity;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.Adapter.ChatAdapter;
import com.example.a13834598889.billiards.Tool.OrderInfoUtil2_0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.combineMeasuredStates;

public class FragmentIM extends Fragment implements MessageListHandler {

    private String name;
    private String id;
    private Button addButton;
    private ImageView back;
    private TextView title;
    private LinearLayout layout_more;
    private LinearLayout layout_add;
    private LinearLayout layout_emo;
    private EditText editText;
    private Button btn_speak;
    private SwipeRefreshLayout refreshLayout;
    private Button btn_chat_send;
    private Button btn_chat_keyboard;
    private Button btn_chat_voice;
    private BmobIMConversation mConversationManager;
    private BmobIMConversation conversation;
    public static ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private List<BmobIMMessage> messageList = new ArrayList<>();
    private static LinearLayoutManager layoutManager;

    private Button tv_location;
    private FragmentManager fragmentManager;

    public static FragmentIM newInstance(String name, String id, BmobIMConversation conversation) {
        FragmentIM fragmentIM = new FragmentIM();
        fragmentIM.name = name;
        fragmentIM.id = id;
        fragmentIM.conversation = conversation;
        return fragmentIM;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_im, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        mConversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversation);
        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            Toast.makeText(getContext(), "尚未连接IM服务器", Toast.LENGTH_SHORT).show();
        }
        initViews(view);
        queryMessages(null);
        initClicks();
        return view;
    }

    /**
     * 首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     *
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg) {
        //TODO 消息：5.2、查询指定会话的消息记录
        mConversationManager.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                refreshLayout.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        chatAdapter.addMessages(list);
                        chatAdapter.notifyDataSetChanged();
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);

                    }
                } else {
                    Log.e(TAG, "done: "+e.getMessage() );
                }
            }
        });
    }



    private void initClicks() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager.findFragmentByTag("im_Layout") != null) {
                    fragmentManager.beginTransaction()
                            .remove(fragmentManager.findFragmentByTag("im_Layout"))
                            .add(R.id.fragment_container, Fragment_friends.newInstance(), "text_button_wodeqiuyou")
                            .commit();
                    MainActivity.customerNavigation.setVisibility(View.VISIBLE);
                }
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_more.getVisibility() == View.GONE) {
                    layout_more.setVisibility(View.VISIBLE);
                    layout_add.setVisibility(View.VISIBLE);
                    layout_emo.setVisibility(View.GONE);
                    locationClick();
                    hideSoftInputView();
                } else {
                    if (layout_emo.getVisibility() == View.VISIBLE) {
                        layout_emo.setVisibility(View.GONE);
                        layout_add.setVisibility(View.VISIBLE);
                        locationClick();
                    } else {
                        layout_more.setVisibility(View.GONE);
                    }
                }
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                    scrollToBottom();
                }
                return false;
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_more.getVisibility() == View.VISIBLE) {
                    layout_add.setVisibility(View.GONE);
                    layout_emo.setVisibility(View.GONE);
                    layout_more.setVisibility(View.GONE);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollToBottom();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btn_chat_send.setVisibility(View.VISIBLE);
                    btn_chat_keyboard.setVisibility(View.GONE);
                    btn_chat_voice.setVisibility(View.GONE);
                } else {
                    if (btn_chat_voice.getVisibility() != View.VISIBLE) {
                        btn_chat_voice.setVisibility(View.VISIBLE);
                        btn_chat_send.setVisibility(View.GONE);
                        btn_chat_keyboard.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
                    Toast.makeText(getContext(), "尚未连接IM服务器", Toast.LENGTH_SHORT).show();
                } else if (text.trim().equals("")) {
                    Toast.makeText(getContext(), "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    final BmobIMTextMessage message = new BmobIMTextMessage();
                    message.setContent(text);
                    Map<String, Object> map = new HashMap<>();
                    map.put("level", "1");
                    message.setExtraMap(map);
                    message.setExtra("OK");
                    mConversationManager.sendMessage(message, listener);
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
    }


    private void locationClick(){
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.fragment_container, FragmentSendLocation.newInstance(mConversationManager, listener))
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void query() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        queryMessages(null);
    }



    private void initViews(View view) {
        addButton = view.findViewById(R.id.btn_chat_add);
        layout_more = view.findViewById(R.id.layout_more);
        layout_add = view.findViewById(R.id.layout_add);
        layout_emo = view.findViewById(R.id.layout_emo);
        editText = view.findViewById(R.id.edit_msg);
        btn_speak = view.findViewById(R.id.btn_speak);
        btn_chat_send = view.findViewById(R.id.btn_chat_send);
        btn_chat_keyboard = view.findViewById(R.id.btn_chat_keyboard);
        btn_chat_voice = view.findViewById(R.id.btn_chat_voice);
//        layout_record = view.findViewById(R.id.layout_record);
//        iv_record = view.findViewById(R.id.iv_record);
        refreshLayout=view.findViewById(R.id.chat_refresh);
        back = view.findViewById(R.id.tv_left);
        recyclerView = view.findViewById(R.id.rc_view);
        title = view.findViewById(R.id.chat_title);
        tv_location = view.findViewById(R.id.tv_location);
        setView();
    }

    private void setView() {
        title.setText(name);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(messageList, getContext());
        recyclerView.setAdapter(chatAdapter);
    }

    /**
     * 消息发送监听器
     */
    public MessageSendListener listener = new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            Log.e(TAG, "onProgress: " + value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            chatAdapter.addMessage(msg);
            editText.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            Toast.makeText(getContext(), "发送成功", Toast.LENGTH_SHORT).show();
            chatAdapter.notifyDataSetChanged();
            editText.setText("");
            scrollToBottom();
            if (e != null) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "done: " + e.getMessage());
            }
        }
    };

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        for (int i = 0; i < list.size(); i++) {
            Log.e(TAG, "onMessageReceive: 用户" + list.get(i).getMessage().getFromId()+ "发来消息："+list.get(i).getMessage().getContent());
            Toast.makeText(getContext(), "收到消息：" + list.get(i).getMessage().getContent(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(chatAdapter.getItemCount() - 1, 0);
    }

    @Override
    public void onResume() {
        BmobIM.getInstance().addMessageListHandler(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }
}
