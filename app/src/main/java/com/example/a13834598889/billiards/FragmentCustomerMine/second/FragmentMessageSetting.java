package com.example.a13834598889.billiards.FragmentCustomerMine.second;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a13834598889.billiards.FragmentCustomerMine.Fragment_mine;
import com.example.a13834598889.billiards.FragmentCustomerMine.thired.FragmentChangePassword;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.MainActivity;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.GetBmobMinePhoto;
import com.example.a13834598889.billiards.Tool.MineDialog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static cn.volley.VolleyLog.TAG;
import static com.example.a13834598889.billiards.FragmentCustomerMine.Fragment_mine.setShopShowIconMine;
import static com.example.a13834598889.billiards.FragmentCustomerOrder.Fragment_map.changeOrderPhoto;
import static com.example.a13834598889.billiards.FragmentCustomerShare.Fragment_share.changeSharePhoto;
import static com.example.a13834598889.billiards.FragmentCustomerTeach.Fragment_teach.changeTeachPhoto;

/**
 * Created by Administrator on 2018/9/15.
 */

public class FragmentMessageSetting extends Fragment {

    private CircleImageView backImageView;
    private LinearLayout nickNameSetting;
    private LinearLayout headPictureSetting;
    private LinearLayout passWordSetting;
    private LinearLayout phoneNumberSetting;
    private LinearLayout emailSetting;
    private LinearLayout signSetting;

    private static CircleImageView shopProfilePhoto;

    //头像操作
    private MineDialog dialog;
    private final int RESULT_CAMERA = 2;
    private final int RESULT_IMAGE = 1;
    private Uri imageUri;
    private final int CROP_PICTURE = 3;//裁剪后图片返回码
    //裁剪图片存放地址的Uri
    private Uri cropImageUri;

    private TextView staticNickNameTextView;
    private TextView phoneNumberTextView;
    private TextView emailTextView;
    private TextView signTextView;

    private Fragment fragmentTest;
    private FragmentManager fragmentManager;

    private TextView ok;

    //TODO: customer_message_setting
    public static FragmentMessageSetting newInstance() {
        return new FragmentMessageSetting();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_setting, container, false);
        initViews(view);
        fragmentManager = getActivity().getSupportFragmentManager();
        bmobCheck();
        initClicks();
        return view;
    }

    private void initViews(View view) {
        backImageView = view.findViewById(R.id.back);
        headPictureSetting = view.findViewById(R.id.message_setting_profile_photo_layout);
        nickNameSetting = view.findViewById(R.id.message_setting_store_name_layout);
        passWordSetting = view.findViewById(R.id.message_setting_change_password_layout);
        phoneNumberSetting = view.findViewById(R.id.message_setting_change_phone_number_layout);
        emailSetting = view.findViewById(R.id.message_setting_change_email_layout);
        signSetting = view.findViewById(R.id.message_setting_change_sign_layout);



        staticNickNameTextView = view.findViewById(R.id.message_setting_store_name_TextView);
        phoneNumberTextView = view.findViewById(R.id.message_setting_change_phone_number_TextView);
        emailTextView = view.findViewById(R.id.message_setting_change_email_TextView);
        signTextView = view.findViewById(R.id.message_setting_change_sign_TextView);
        ok = view.findViewById(R.id.ok);
    }

    private void initPhoto() {
        GetBmobMinePhoto.initInterface("编辑资料界面", 2,getContext());
    }

    //TODO: 加载布局
    private void bmobCheck() {
        initPhoto();
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(User.getCurrentUser().getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if (e == null) {
                    if (object.getPicture_head()!=null){
                        object.getPicture_head().download(new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e==null){
                                }else {
                                    Log.e(TAG, "done: "+e.getMessage() );
                                }
                            }

                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });
                    }else {
                        Resources resources = getContext().getResources();
                        @SuppressLint("ResourceType") InputStream inputStream = resources.openRawResource(R.drawable.touxiang);
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(inputStream);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        shopProfilePhoto.setImageBitmap(bitmap);
                    }
                    staticNickNameTextView.setText(object.getNickName());
                    phoneNumberTextView.setText(object.getMobilePhoneNumber());
                    emailTextView.setText(object.getEmail());
                    if (object.getSign() != null) {
                        signTextView.setText(object.getSign());
                    } else {
                        signTextView.setText(R.string.noSign);
                    }
                } else {
                    Toast.makeText(getActivity(), "个人信息更新失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dialogShow() {
        dialog = new MineDialog(getContext());
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
        dialog.show();

        dialog.setTakeButton(new MineDialog.onTakeOnclickListener() {
            @Override
            public void onTakeClick() {
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
                        intent.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, 0);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, RESULT_CAMERA);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "没有找到储存目录", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "没有储存卡", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });
        dialog.setChooseButton(new MineDialog.onChooseClickListener() {
            @Override
            public void onChooseClick() {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {
                    openAlbum();
                }
                dialog.dismiss();
            }
        });

    }

    private void openAlbum() {
        Log.e(TAG, "openAlbum: 非MainActivity，打开相册");
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_IMAGE);//打开相册
    }

    private void initClicks() {
        //头像
        headPictureSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShow();
            }
        });

        //返回
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager.findFragmentByTag("customer_message_setting") != null) {
                    fragmentManager.beginTransaction()
                            .remove(fragmentManager.findFragmentByTag("customer_message_setting"))
                            .show(fragmentManager.findFragmentByTag("fragment_mine"))
                            .commit();
                }
            }
        });


        //修改密码
        passWordSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTest = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .remove(fragmentTest)
                        .addToBackStack(null)
                        .add(R.id.fragment_container, FragmentChangePassword.newInstance(), "message_setting_change_password_layout")
                        .commit();
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setMobilePhoneNumber(phoneNumberTextView.getText().toString());
                user.setSign(signTextView.getText().toString());
                user.setNickName(staticNickNameTextView.getText().toString());
                user.setEmail(emailTextView.getText().toString());
                user.update(User.getCurrentUser(User.class).getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            Fragment_mine.getMessage();
                        }else {
                            Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //碎片的回调在碎片中
        switch (requestCode) {
            case 100:
                Log.e(TAG, "onRequestPermissionsResult: 回调成功");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
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
            case RESULT_IMAGE:
                if (resultCode == RESULT_OK && data != null) {
                    Log.e(TAG, "onActivityResult: " + imageUri);
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handlerImageOnKitKat(data);
                    } else {
                        //4.4以下系统使用这个方法处理图片
                        handlerImageBeforeKitKat(data);
                    }
                }
                break;
            case RESULT_CAMERA:
                Log.e(TAG, "onActivityResult: " + RESULT_CAMERA);
                if (resultCode == RESULT_OK) {
                    //进行裁剪
                    startPhotoZoom(imageUri);
                }
                break;
            case CROP_PICTURE: // 取得裁剪后的图片
                Log.e(TAG, "onActivityResult: " + CROP_PICTURE);
                if (resultCode == RESULT_OK) {
                    Bitmap headShot;
                    try {
                        File file = new File(new URI(cropImageUri.toString()));
                        headShot = BitmapFactory.decodeFile(String.valueOf(file));
                        //TODO: 更新UI
                        setShopShowIconMine(headShot);
                        changeOrderPhoto(headShot);
                        changeSharePhoto(headShot);
                        changeTeachPhoto(headShot);
                    } catch (Exception e) {
                        Log.e(TAG, "onActivityResult: 编辑资料界面获取剪裁后的照片失败：" + e.getMessage());
                    }
                    commitToBmob();
                }
                break;
            default:
                break;
        }
    }

    private void commitToBmob() {
        BmobFile bmobFile = null;
        try {
            bmobFile = new BmobFile(new File(new URI(cropImageUri.toString())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (bmobFile != null) {
            final User user = new User();
            user.setPicture_head(bmobFile);
//            user.setStore(true);
            user.getPicture_head().uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.e(TAG, "done: 上传服务器成功");
                        user.update(User.getCurrentUser(User.class).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e(TAG, "done: " + e.getMessage());
                                    Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Log.e(TAG, "done: 上传服务器失败 " + e.getMessage());
                    }
                }
            });
        } else {
            Log.e(TAG, "commitToBmob: file is null");
        }
    }

    private void startPhotoZoom(Uri uri) {
        File CropPhoto = new File(MainActivity.path, "crop_image.jpg");
        try {
            if (CropPhoto.exists()) {
                CropPhoto.delete();
            }
            CropPhoto.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cropImageUri = Uri.fromFile(CropPhoto);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, CROP_PICTURE);
    }

    private void handlerImageBeforeKitKat(Intent data) {
        Uri cropUri = data.getData();
        startPhotoZoom(cropUri);
        //获取image的路径
        String imagePath;
        imagePath = getImagePath(cropUri, null);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//        tupian.setImageBitmap(bitmap);
    }

    private void handlerImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(getContext(), uri)) {
                //如果是document类型的Uri,则通过document id处理
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];//解析出数字格式的id
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果是content类型的URI，则使用普通方式处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                //如果是file类型的Uri,直接获取图片路径即可
                imagePath = uri.getPath();
            }
        }
        startPhotoZoom(uri);
    }

    public String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Log.d("test", "getImagePath: " + path);
            }
            cursor.close();
        }
        return path;
    }


}
