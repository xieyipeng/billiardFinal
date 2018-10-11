package com.example.a13834598889.billiards.Tool.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a13834598889.billiards.FragmentCustomerMine.thired.FragmentIM;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.b.V;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static org.greenrobot.eventbus.EventBus.TAG;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.SendHolder> {

    private List<BmobIMMessage> messageList;
    private Context mContext;

    public ChatAdapter(List<BmobIMMessage> messages, Context context) {
        this.mContext = context;
        this.messageList = messages;
    }

    public void addMessages(List<BmobIMMessage> messages) {
        messageList.addAll(0, messages);
        notifyDataSetChanged();
        FragmentIM.scrollToBottom();
    }

    public void addMessage(BmobIMMessage message) {
        messageList.addAll(Arrays.asList(message));
        notifyDataSetChanged();
        FragmentIM.scrollToBottom();
    }

    @Override
    public SendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_send_message, parent, false);
        return new ChatAdapter.SendHolder(view);
    }

    @Override
    public void onBindViewHolder(SendHolder holder, int position) {
        BmobIMMessage bmobIMMessage = messageList.get(position);
        holder.bindView(bmobIMMessage);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class SendHolder extends RecyclerView.ViewHolder {

        private TextView context;
        private CircleImageView photo;
        private TextView time;
        private RelativeLayout send;
        private RelativeLayout receive;
        private TextView rc_context;
        private CircleImageView rc_photo;


        public SendHolder(View itemView) {
            super(itemView);
            context = itemView.findViewById(R.id.send_chat_Text);
            photo = itemView.findViewById(R.id.send_chat_photo);
            time = itemView.findViewById(R.id.send_chat_time);
            receive = itemView.findViewById(R.id.receive);
            send = itemView.findViewById(R.id.send);
            rc_context = itemView.findViewById(R.id.receive_chat_Text);
            rc_photo = itemView.findViewById(R.id.receive_chat_photo);
        }

        private void bindView(final BmobIMMessage bmobIMMessage) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Log.e(TAG, "bindView:  getFromId " + bmobIMMessage.getFromId());
//            Log.e(TAG, "bindView:  getContent " + bmobIMMessage.getContent());
//            Log.e(TAG, "bindView:  getExtra " + bmobIMMessage.getExtra());
//            Log.e(TAG, "bindView:  getToId " + bmobIMMessage.getToId());
//            Log.e(TAG, "bindView:  getCreateTime " + df.format(new Date()));
//            Log.e(TAG, "bindView:  getReceiveStatus " + bmobIMMessage.getReceiveStatus());
            if (bmobIMMessage.getFromId().equals(User.getCurrentUser().getObjectId())) {
                receive.setVisibility(View.GONE);
                send.setVisibility(View.VISIBLE);
                context.setText(bmobIMMessage.getContent());
                BmobQuery<User> userBmobQuery = new BmobQuery<>();
                time.setText(df.format(bmobIMMessage.getCreateTime()));
                userBmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getObjectId().equals(bmobIMMessage.getFromId())) {
                                list.get(i).getPicture_head().download(new DownloadFileListener() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(s);
                                            photo.setImageBitmap(bitmap);
                                        } else {
                                            Log.e(TAG, "done: " + e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onProgress(Integer integer, long l) {

                                    }
                                });
                            }
                        }
                    }
                });
            } else {
                receive.setVisibility(View.VISIBLE);
                send.setVisibility(View.GONE);
                rc_context.setText(bmobIMMessage.getContent());
                BmobQuery<User> userBmobQuery = new BmobQuery<>();
                time.setText(String.valueOf(bmobIMMessage.getCreateTime()));
                userBmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getObjectId().equals(bmobIMMessage.getFromId())) {
                                list.get(i).getPicture_head().download(new DownloadFileListener() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(s);
                                            rc_photo.setImageBitmap(bitmap);
                                        } else {
                                            Log.e(TAG, "done: " + e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onProgress(Integer integer, long l) {

                                    }
                                });
                            }
                        }
                    }
                });
            }
        }
    }
}
