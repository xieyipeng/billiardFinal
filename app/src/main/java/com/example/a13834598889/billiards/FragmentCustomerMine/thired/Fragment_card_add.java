package com.example.a13834598889.billiards.FragmentCustomerMine.thired;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a13834598889.billiards.FragmentCustomerMine.second.Fragment_card;
import com.example.a13834598889.billiards.JavaBean.Card;
import com.example.a13834598889.billiards.JavaBean.Customer;
import com.example.a13834598889.billiards.MainActivity;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.MineDialog;
import com.example.a13834598889.billiards.Tool.PictureUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.app.Activity.RESULT_OK;
import static cn.volley.VolleyLog.TAG;

public class Fragment_card_add extends Fragment {


    //图片
    private MineDialog dialog;
    private final int RESULT_CAMERA = 8;
    private final int RESULT_IMAGE = 7;
    private Uri imageUri;
    private final int CROP_PICTURE = 9;//裁剪后图片返回码
    //裁剪图片存放地址的Uri
    private Uri cropImageUri;
    private String imagePath;
    private boolean isChooseOrTake=false;

    private FragmentManager fragmentManager;
    private ImageView backImageView;
    private TextView commitText;
    private EditText inputText;
    private ImageView imageView;

    private String chulihou_image_path;

    public static Fragment_card_add newInstance(){
        return new Fragment_card_add();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_add, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initViews(view);
        initClicks();
        return view;
    }

    private void initClicks() {
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager.findFragmentByTag("fragment_card_add") != null) {
                    fragmentManager.beginTransaction()
                            .remove(fragmentManager.findFragmentByTag("fragment_card_add"))
                            .show(fragmentManager.findFragmentByTag("fragment_share"))
                            .commit();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new MineDialog(getContext());
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
                                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
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
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                        } else {
                            openAlbum();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        commitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputText.getText().toString().equals("")){
                    Toast.makeText(getContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
                }else if (imagePath == null){
                    Toast.makeText(getContext(), "请上传图片", Toast.LENGTH_SHORT).show();
                } else {
                    Customer customer=Customer.getCurrentUser(Customer.class);
                    final Card card=new Card();
                    card.setCustomer(customer);
                    card.setShoucang(new ArrayList<String>());
                    card.setDianzan(new ArrayList<String>());
                    card.setText(inputText.getText().toString());
                    long t = System.currentTimeMillis();//获得当前时间的毫秒数
                    Random rd = new Random(t);//作为种子数传入到Random的构造器中
                    String targetPath = getActivity().getExternalCacheDir() + "compressPic" + rd + ".jpg";
                    chulihou_image_path = PictureUtil.compressImage(imagePath, targetPath, 30);
                    File file = new File(chulihou_image_path);
                    BmobFile bmobFile = new BmobFile(file);
                    if (isChooseOrTake){
                        card.setPicture(bmobFile);
                        card.getPicture().uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    saveCard(card);
                                }else {
                                    Log.e(TAG, "done: "+e.getMessage() );
                                }
                            }
                        });
                    }else {
                        card.setPicture(null);
                        saveCard(card);
                    }
                }
            }
        });
    }

    private void saveCard(Card card) {
        card.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Toast.makeText(getContext(), "发表成功", Toast.LENGTH_SHORT).show();
                    if (fragmentManager.findFragmentByTag("fragment_card_add") != null) {
                        fragmentManager.beginTransaction()
                                .remove(fragmentManager.findFragmentByTag("fragment_card_add"))
                                .show(fragmentManager.findFragmentByTag("fragment_share"))
                                .commit();
                    }
                }else {
                    Log.e(TAG, "done: "+e.getMessage() );
                }
            }
        });
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private void openAlbum() {
        Log.e(TAG, "openAlbum: 非MainActivity，打开相册");
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_IMAGE);//打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //碎片的回调在碎片中
        switch (requestCode) {
            case 101:
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
//                Log.e(TAG, "onActivityResult: " + CROP_PICTURE);
//                if (resultCode == RESULT_OK) {
//                    Bitmap headShot = null;
//                    try {
//                        File file = new File(new URI(cropImageUri.toString()));
//                        headShot = BitmapFactory.decodeFile(String.valueOf(file));
//                    } catch (Exception e) {
//                        Log.e(TAG, "onActivityResult: 编辑资料界面获取剪裁后的照片失败：" + e.getMessage());
//                    }
//                    imageView.setImageBitmap(headShot);
//                }
                break;
            default:
                break;
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
        imagePath = getImagePath(cropUri, null);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);
        isChooseOrTake=true;
    }

    private void handlerImageOnKitKat(Intent data) {
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
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);
        isChooseOrTake=true;
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

    private void initViews(View view) {
        backImageView=view.findViewById(R.id.customer_add_card_back);
        commitText=view.findViewById(R.id.customer_add_card_commit);
        inputText=view.findViewById(R.id.customer_add_card_text);
        imageView=view.findViewById(R.id.customer_add_card_choose);
    }
}
