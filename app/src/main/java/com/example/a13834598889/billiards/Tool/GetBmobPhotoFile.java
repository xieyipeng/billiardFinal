package com.example.a13834598889.billiards.Tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.example.a13834598889.billiards.FragmentShopKepperMine.FragmentShopKeeperMine;
import com.example.a13834598889.billiards.FragmentShopKepperMine.second.FragmentShopMessageSetting;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;

import java.io.InputStream;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;

import static android.content.ContentValues.TAG;
import static com.example.a13834598889.billiards.FragmentShopKepperMine.FragmentShopKeeperMine.setInterfacePhoto;
import static com.example.a13834598889.billiards.FragmentShopKepperMine.second.FragmentShopMessageSetting.setShopChangeIcon;

public class GetBmobPhotoFile {

    public static void initInterface(final String Interface, final int requesCode, final Context context) {
        final String temp = Interface + " requesCode " + String.valueOf(requesCode) + " ";
        BmobQuery<User> bmobQuery = new BmobQuery();
        bmobQuery.getObject(User.getCurrentUser().getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    BmobFile bmobFile = user.getPicture_head();
                    if (bmobFile != null) {
                        bmobFile.download(new DownloadFileListener() {
                            @Override
                            public void onStart() {
//                                Log.e(TAG, "onStart: " + temp + "开始下载文件：");
                            }

                            @Override
                            public void done(String savePath, BmobException e) {
                                if (e == null) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(savePath);
                                    switch (requesCode) {
                                        case 1:
                                            setInterfacePhoto(bitmap);
                                            break;
                                        case 2:
                                            setShopChangeIcon(bitmap);
                                            break;
                                        default:
                                            break;
                                    }
                                } else {
                                    Log.e(TAG, "done: " + temp + "文件下载失败");
                                }
                            }

                            @Override
                            public void onProgress(Integer integer, long l) {
//                                Log.d("bmob", "onProgress: " + temp + "文件时下载进度：" + integer + "," + l);
                            }

                            @Override
                            public void onFinish() {
//                                Log.e(TAG, "onFinish: " + temp + "文件下载完成");
                            }
                        });
                    } else {
                        Log.e(TAG, "done: 服务器上文件为空");
                        Resources resources = context.getResources();
                        @SuppressLint("ResourceType") InputStream inputStream = resources.openRawResource(R.drawable.touxiang);
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(inputStream);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        FragmentShopKeeperMine.profilePhoto.setImageBitmap(bitmap);

                    }
                } else {
                    Log.e(TAG, "done: " + temp + "获取文件时连接服务器失败 \n " + e.getMessage());
                }
            }
        });
    }
}
