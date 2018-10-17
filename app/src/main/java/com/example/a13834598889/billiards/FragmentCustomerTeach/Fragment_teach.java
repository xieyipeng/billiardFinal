package com.example.a13834598889.billiards.FragmentCustomerTeach;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.billiards.JavaBean.SmallBall;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.MainActivity;
import com.example.a13834598889.billiards.Opengles.BallObject;
import com.example.a13834598889.billiards.Opengles.MySurfaceView;
import com.example.a13834598889.billiards.Opengles.OpenglesA;
import com.example.a13834598889.billiards.Opengles.PointA;
import com.example.a13834598889.billiards.Opengles.TouchableObject;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.App;
import com.example.a13834598889.billiards.Tool.MineDialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.db.MigrationHelper;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static cn.volley.VolleyLog.TAG;
import static com.example.a13834598889.billiards.FragmentShopKepperMine.second.FragmentShopThreeAd.AD_RESULT_IMAGE;
import static com.example.a13834598889.billiards.LoginActivity.mGLSurfaceView;

/**
 * Created by 13834598889 on 2018/4/29.
 */

public class Fragment_teach extends Fragment {

    private static CircleImageView circleImageView2;
    public Button button_dianwei;
    public Button button_zishi;
    public static int flagChoose;
    private Uri imageUri;
    private ImageView tackPhoto;
    private Socket socket;
    private List<SmallBall> smallBallList=new ArrayList<>();
    public static ArrayList<BallObject> ballObjects = new ArrayList<>();

    public static Fragment_teach newInstance() {
        return new Fragment_teach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teach, container, false);
        initViews(view);
        initClicks();
        return view;
    }


    //TODO: 向服务器发送请求
    private void sendRequest(File file) {
        try {
            socket = new Socket("178.128.180.92", 8000);
            String file_info = "";
            if (file.getName().equals("1.png") || file.getName().equals("2.png")) {
                //腾讯
                file_info = "post|h|" + file.getName() + "|" + file.length();
            } else {
                //实景
                file_info = "post|r|" + file.getName() + "|" + file.length();
            }
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
            outputStream.write(file_info.getBytes());
            outputStream.flush();
            byte bytes[] = new byte[1024];
            int d = 0;
            while ((d = inputStream.read(bytes, 0, bytes.length)) != -1) {
                outputStream.write(bytes, 0, d);
                //刷新缓冲流（这里必须刷新，否则可能会丢失字节数据）
                outputStream.flush();
            }
            socket.shutdownOutput();
            byte get[] = new byte[1024];
            InputStream socketGet = socket.getInputStream();
            d = socketGet.read(get);
            String result = new String(get, 0, d);
            getBallList(result);

            inputStream.close();
            outputStream.close();
            socketGet.close();
        } catch (IOException e) {
            Log.e(TAG, "sendRequest: " + e.getMessage());
        }
    }

    //TODO: 获得到服务器传来的球的数据，进行的操作
    private void getBallList(String result) {
        result = result.trim();
        String a = "[()]";
        String[] strings = result.split(a);
        for (int i = 0; i < strings.length; i++) {
            if (i % 2 != 0) {
                String temp = strings[i];
                String[] newStrings = temp.split(",");
                for (int j = 0; j < newStrings.length; j++) {
                    newStrings[j] = newStrings[j].trim();
                    SmallBall smallBall=new SmallBall();
                    smallBall.setBall_x(Double.valueOf(newStrings[0]));
                    smallBall.setBall_y(Double.valueOf(newStrings[1]));
                    smallBall.setNum(Integer.valueOf(newStrings[3]));
                    smallBallList.add(smallBall);
                }
            }
        }
        for (int i = 0; i < smallBallList.size(); i++) {
            Log.e(TAG, "getBallList: "+smallBallList.get(i).getBall_x()+" "+smallBallList.get(i).getBall_y()+" "+smallBallList.get(i).getNum());
        }
    }

    private void initClicks() {
        tackPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MineDialog dialog = new MineDialog(getContext());
                setDialogClickListener(dialog);
            }
        });
        button_dianwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mGLSurfaceView = new MySurfaceView(App.getContext(), new ArrayList<BallObject>());

                mGLSurfaceView.cx = -230;
                mGLSurfaceView.cy = 90;
                mGLSurfaceView.cz = 0;

                mGLSurfaceView.currSightDis = 230;
                mGLSurfaceView.angdegElevation = 60;
                mGLSurfaceView.angdegAzimuth = 90;

                mGLSurfaceView.tx = 0;
                mGLSurfaceView.ty = 0;
                mGLSurfaceView.tz = 0;

                mGLSurfaceView.setCameraPostion();


                flagChoose = 1;
                Intent intent = new Intent(getActivity(), OpenglesA.class);
                startActivity(intent);
            }
        });

        button_zishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mGLSurfaceView = new MySurfaceView(App.getContext(), new ArrayList<BallObject>());

                mGLSurfaceView.cx = -100;
                mGLSurfaceView.cy = 50;
                mGLSurfaceView.cz = 0;

                mGLSurfaceView.currSightDis = 50;
                mGLSurfaceView.angdegElevation = 60;
                mGLSurfaceView.angdegAzimuth = 90;

                mGLSurfaceView.tx = 0;
                mGLSurfaceView.ty = 20;
                mGLSurfaceView.tz = 0;

                mGLSurfaceView.setCameraPostion();


                flagChoose = 2;
                Intent intent = new Intent(getActivity(), OpenglesA.class);
                startActivity(intent);
            }
        });
    }

    private void initViews(View view) {
        circleImageView2 = view.findViewById(R.id.circleImageView_mine2);
        button_dianwei = view.findViewById(R.id.button_teach_dianwei);
        tackPhoto = view.findViewById(R.id.teach_tack_photo);
        button_zishi = view.findViewById(R.id.button_teach_zishi);
        loadingPhoto();
    }

    //TODO: 设置界面修改头像之后刷新本界面UI
    public static void changeTeachPhoto(Bitmap bitmap) {
        circleImageView2.setImageBitmap(bitmap);
    }

    //TODO: 头像
    public void loadingPhoto() {
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getObjectId().equals(User.getCurrentUser().getObjectId())) {
                            if (list.get(i).getPicture_head() != null) {
                                list.get(i).getPicture_head().download(new DownloadFileListener() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(s);
                                            circleImageView2.setImageBitmap(bitmap);
                                        } else {
                                            Log.e(MigrationHelper.TAG, "done: " + e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onProgress(Integer integer, long l) {

                                    }
                                });
                            } else {
                                Log.e(TAG, "done: 头像为空");
                                Glide.with(getActivity())
                                        .load(R.drawable.touxiang)
                                        .into(circleImageView2);
                            }
                        }
                    }
                } else {
                    Log.e(MigrationHelper.TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private void setDialogClickListener(final MineDialog dialog) {
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
        dialog.show();
        dialog.setChooseButton(new MineDialog.onChooseClickListener() {
            @Override
            public void onChooseClick() {
                choosePhoto(dialog);
            }
        });
        dialog.setTakeButton(new MineDialog.onTakeOnclickListener() {
            @Override
            public void onTakeClick() {
                takePhoto(dialog);
            }
        });
    }

    private void takePhoto(Dialog dialog) {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            //创建File对象，用于存储拍照后的照片
            File outputImage = new File(MainActivity.path, "out_image.jpg");//SD卡的应用关联缓存目录
            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(getContext(),
                            "com.hanrui.android.fileprovider", outputImage);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 22);
            } catch (Exception e) {
                Toast.makeText(getContext(), "没有找到储存目录", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "没有储存卡", Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }

    private void choosePhoto(Dialog dialog) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        } else {
            openAlbum();
        }
        dialog.dismiss();
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 21);//打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //碎片的回调在碎片中
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, 21);//打开相册
                } else {
                    Toast.makeText(getContext(), "你没有开启权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //碎片的回调在碎片中
        switch (requestCode) {
            case 21:
                if (resultCode == RESULT_OK && data != null) {
                    try {
                        File file = new File(new URI(imageUri.toString()));
                        sendRequest(file);
                    } catch (URISyntaxException e) {
                        Log.e(TAG, "onActivityResult: " + e.getMessage());
                    }
                }
                break;
            case 22:
                if (resultCode == RESULT_OK) {
                    try {
                        File file = new File(new URI(imageUri.toString()));
                        sendRequest(file);
                    } catch (URISyntaxException e) {
                        Log.e(TAG, "onActivityResult: " + e.getMessage());
                    }
                }
                break;
            default:
                break;
        }
    }
}
