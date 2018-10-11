package com.example.a13834598889.billiards.Tool.Adapter;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

import static cn.bmob.v3.BmobRealTimeData.TAG;

public class ContactsHolder extends RecyclerView.ViewHolder {

    private TextView text_view_ni_cheng;
    private TextView text_view_ge_xing_qian_ming;
    private ImageView imageView_TouXiang;
    public LinearLayout imLayout;

    public ContactsHolder(View view) {
        super(view);
        text_view_ge_xing_qian_ming = view.findViewById(R.id.ge_xing_qian_ming_text_view);
        text_view_ni_cheng = view.findViewById(R.id.ni_cheng_text_view);
        imageView_TouXiang = view.findViewById(R.id.tou_xiang_image_view);
        imLayout = view.findViewById(R.id.im_Layout);
    }

    public void bindView(final User user) {
        if (user.getNickName() != null) {
            text_view_ni_cheng.setText(user.getNickName());
        } else {
            BmobQuery<User> userBmobQuery = new BmobQuery<>();
            userBmobQuery.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            if (user.getObjectId().equals(list.get(i).getObjectId())) {
                                text_view_ni_cheng.setText(list.get(i).getNickName());
                            }
                        }
                    } else {
                        Log.e(TAG, "done: " + e.getMessage());
                    }
                }
            });
        }
        if (user.getSign() != null) {
            text_view_ge_xing_qian_ming.setText(user.getSign());
        } else {
            text_view_ge_xing_qian_ming.setText(R.string.SnoSign);
        }

        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getObjectId().equals(user.getObjectId())) {
                        if (list.get(i).getPicture_head() != null) {
                            list.get(i).getPicture_head().download(new DownloadFileListener() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        imageView_TouXiang.setImageBitmap(BitmapFactory.decodeFile(s));
                                    } else {
                                        Log.e(TAG, "done: " + e.getMessage());
                                    }
                                }

                                @Override
                                public void onProgress(Integer integer, long l) {

                                }
                            });
                        } else {
                            imageView_TouXiang.setImageResource(R.drawable.touxiang);
                        }
                    }
                }
            }
        });
    }
}
