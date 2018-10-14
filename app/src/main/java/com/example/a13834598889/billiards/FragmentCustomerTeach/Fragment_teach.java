package com.example.a13834598889.billiards.FragmentCustomerTeach;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.Opengles.BallObject;
import com.example.a13834598889.billiards.Opengles.MySurfaceView;
import com.example.a13834598889.billiards.Opengles.OpenglesA;
import com.example.a13834598889.billiards.Opengles.PointA;
import com.example.a13834598889.billiards.Opengles.TouchableObject;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.App;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.db.MigrationHelper;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.volley.VolleyLog.TAG;
import static com.example.a13834598889.billiards.LoginActivity.mGLSurfaceView;

/**
 * Created by 13834598889 on 2018/4/29.
 */

public class Fragment_teach extends Fragment {

    private static CircleImageView circleImageView2;
    public Button button_dianwei;
    public Button button_zishi;
    public static int flagChoose;
    private ImageView tackPhoto;
    private Socket socket;
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

    private void initClicks() {
        tackPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //选择图片获取文件流
                //上传服务器，获取数据

                try {
                    socket=new Socket("74.82.215.154",8000);
                } catch (IOException e) {
                    Log.e(TAG, "onClick: "+e.getMessage() );
                }

                
//                s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
//                host = '178.128.180.92'
//                port = 8000
//                s.connect((host, port))
//
//                BASE_DIR = os.path.dirname(os.path.abspath(__file__))
//
//                while True:
//                inp = input('>>>>>>>>')  # post|11.png
//                        cmd, path = inp.split('|')  # 拿到post，以及文件11.jpg
//                        path = os.path.join(BASE_DIR, path)
//                filename = os.path.basename(path)
//                file_size = os.stat(path).st_size
//                file_info = 'post|%s|%s' % (filename, file_size)  # split获取字符串的信息       以此方式打包，依次为   cmd/name/size
//                s.sendall(bytes(file_info, 'utf8'))  # 第一次发送请求，不是具体内容，而是先发送数据信息
//
//                        f = open(path, 'rb')
//                has_sent = 0
//                while has_sent != file_size:
//                data = f.read(1024)
//                s.sendall(data)  # 发送真实数据
//                has_sent += len(data)
//
//                f.close()
//                print('上传成功')
//                msg = s.recv(1024)
//                print(msg.decode('utf-8'))



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
        button_dianwei =  view.findViewById(R.id.button_teach_dianwei);
        tackPhoto=view.findViewById(R.id.teach_tack_photo);
        button_zishi =  view.findViewById(R.id.button_teach_zishi);
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

}
