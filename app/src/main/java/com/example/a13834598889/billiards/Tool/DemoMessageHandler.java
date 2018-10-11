package com.example.a13834598889.billiards.Tool;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;

import com.example.a13834598889.billiards.FragmentCustomerMine.thired.FragmentIM;
import com.example.a13834598889.billiards.MainActivity;
import com.example.a13834598889.billiards.Tool.Adapter.ChatAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

//TODO 集成：1.6、自定义消息接收器处理在线消息和离线消息
public class DemoMessageHandler extends BmobIMMessageHandler {

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String TAG = "DemoMessageHandler";

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //在线消息
        Log.e(TAG, "bindView:  getFromId "+event.getMessage().getFromId() );
        Log.e(TAG, "bindView:  getContent "+event.getMessage().getContent() );
        Log.e(TAG, "bindView:  getExtra "+event.getMessage().getExtra() );
        Log.e(TAG, "bindView:  getToId "+event.getMessage().getToId() );
        Log.e(TAG, "bindView:  getCreateTime "+df.format(event.getMessage().getCreateTime()));
        Log.e(TAG, "bindView:  getReceiveStatus "+event.getMessage().getReceiveStatus() );

        FragmentIM.chatAdapter.addMessage(event.getMessage());

    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用
        Map<String, List<MessageEvent>> map = event.getEventMap();
        //挨个检测下离线消息所属的用户的信息是否需要更新
        Toast.makeText(MainActivity.context, "有" + map.size() + "个用户发来离线消息", Toast.LENGTH_SHORT).show();
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            int size = list.size();
            Log.e(TAG, "onOfflineReceive: "+"用户" + entry.getKey() + "发来" + size + "条消息" );
            for (int i = 0; i < size; i++) {
                Log.e(TAG, "bindView:  离线消息： "+i+ "  getFromId "+list.get(i).getMessage().getFromId() );
                Log.e(TAG, "bindView:  离线消息： "+i+ "  getContent "+list.get(i).getMessage().getContent() );
                Log.e(TAG, "bindView:  离线消息： "+i+ "  getExtra "+list.get(i).getMessage().getExtra() );
                Log.e(TAG, "bindView:  离线消息： "+i+ "  getToId "+list.get(i).getMessage().getToId() );
                Log.e(TAG, "bindView:  离线消息： "+i+ "  getCreateTime "+df.format(list.get(i).getMessage().getCreateTime()));
                Log.e(TAG, "bindView:  离线消息： "+i+ "  getReceiveStatus "+list.get(i).getMessage().getReceiveStatus() );
            }
        }
    }
}
