package com.example.a13834598889.billiards.FragmentShopKepperMine.second;

import android.Manifest;
import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a13834598889.billiards.JavaBean.BilliardStore;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.MainActivity;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.MineDialog;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class FragmentShopThreeAd extends Fragment {

    private CircleImageView backImageVie;
    private ImageView oneCardView;
    private ImageView twoCardView;
    private ImageView threeCardView;

    //头像操作
    private static final int AD_RESULT_CAMERA = 5;
    public static final int AD_RESULT_IMAGE = 4;
    private Uri imageUri;
    private static final int AD_CROP_PICTURE = 6;//裁剪后图片返回码
    //裁剪图片存放地址的Uri
    private Uri cropImageUri;

    private int number;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;

    private Fragment fragmentTest;
    private FragmentManager fragmentManager;

    public static FragmentShopThreeAd newInstance() {
        FragmentShopThreeAd fragmentShopThreeAd = new FragmentShopThreeAd();
        return fragmentShopThreeAd;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_three_ad, container, false);
        initViews(view);
        initClicks();
        initialize();
        return view;
    }

    /**
     * 从bmob下载广告图片并更新UI
     */
    private void initialize() {
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.getObject(User.getCurrentUser().getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    getBitmap(user.getObjectId());
                } else {
                    Log.e(TAG, "done: 广告界面初始化时，查询店家ID出错 " + e.getMessage());
                }
            }
        });
    }

    private void getBitmap(final String objectId) {
        BmobQuery<BilliardStore> billiardStoreBmobQuery = new BmobQuery<>();
        billiardStoreBmobQuery.findObjects(new FindListener<BilliardStore>() {
            @Override
            public void done(List<BilliardStore> list, BmobException e) {
                if (e == null) {
                    boolean have = false;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getStoreID().equals(objectId)) {
                            have = true;
                            downloadBmobFile(list.get(i));
                        }
                    }
                    if (!have) {
                        Log.e(TAG, "done: 店家没有上传过至少一张广告图片 ");
                    }
                } else {
                    Log.e(TAG, "done: 广告界面初始化时，获取BilliardStore实例化时出错 " + e.getMessage());
                }
            }
        });
    }

    private void downloadBmobFile(BilliardStore billiardStore) {
        //广告一
        if (billiardStore.getPicture_1() != null) {
            billiardStore.getPicture_1().download(new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(s);
                        oneCardView.setImageBitmap(bitmap);
                    } else {
                        Log.e(TAG, "done: 下载广告一时，出现错误 " + e.getMessage());
                    }
                }

                @Override
                public void onFinish() {
//                    Log.e(TAG, "onFinish: 广告一下载完成 ");
                }

                @Override
                public void onStart() {
//                    Log.e(TAG, "onStart: 开始下载广告一");
                }

                @Override
                public void onProgress(Integer integer, long l) {
//                    Log.d(TAG, "onProgress: 广告一下载进度：" + integer + "," + l);
                }
            });
        }
        //广告二
        if (billiardStore.getPicture_2() != null) {
            billiardStore.getPicture_2().download(new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(s);
                        twoCardView.setImageBitmap(bitmap);
                    } else {
                        Log.e(TAG, "done: 下载广告二时，出现错误 " + e.getMessage());
                    }
                }

                @Override
                public void onFinish() {
//                    Log.e(TAG, "onFinish: 广告二下载完成 ");
                }

                @Override
                public void onStart() {
//                    Log.e(TAG, "onStart: 开始下载广告二");
                }

                @Override
                public void onProgress(Integer integer, long l) {
//                    Log.d(TAG, "onProgress: 广告二下载进度：" + integer + "," + l);
                }
            });
        }
        //广告三
        if (billiardStore.getPicture_3() != null) {
            billiardStore.getPicture_3().download(new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(s);
                        threeCardView.setImageBitmap(bitmap);
                    } else {
                        Log.e(TAG, "done: 下载广告三时，出现错误 " + e.getMessage());
                    }
                }

                @Override
                public void onFinish() {
//                    Log.e(TAG, "onFinish: 广告三下载完成 ");
                }

                @Override
                public void onStart() {
//                    Log.e(TAG, "onStart: 开始下载广告三");
                }

                @Override
                public void onProgress(Integer integer, long l) {
//                    Log.d(TAG, "onProgress: 广告三下载进度：" + integer + "," + l);
                }
            });
        }
    }

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
                startActivityForResult(intent, AD_RESULT_CAMERA);
            } catch (Exception e) {
                Toast.makeText(getContext(), "没有找到储存目录", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "没有储存卡", Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<分界线

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, AD_RESULT_IMAGE);//打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //碎片的回调在碎片中
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, AD_RESULT_IMAGE);//打开相册
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
            case AD_RESULT_IMAGE:
                if (resultCode == RESULT_OK && data != null) {
                    Log.e(TAG, "onActivityResult: " + AD_RESULT_IMAGE);
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
            case AD_RESULT_CAMERA:
                Log.e(TAG, "onActivityResult: " + AD_RESULT_CAMERA);
                if (resultCode == RESULT_OK) {
                    //进行裁剪
                    startPhotoZoom(imageUri);
                }
                break;
            case AD_CROP_PICTURE: // 取得裁剪后的图片
                Log.e(TAG, "onActivityResult: " + AD_CROP_PICTURE);
                commitADToBmob();
                break;
            default:
                break;
        }
    }

    /**
     * 更换图片并上传到bmob，更新UI
     */
    private void commitADToBmob() {
        //获取店家objectid
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.getObject(User.getCurrentUser().getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    addBilliardStore(user.getObjectId());
                } else {
                    Log.e(TAG, "done: 更改广告界面，查询店铺id出错 " + e.getMessage());
                }
            }
        });
    }

    private void addBilliardStore(final String storeID) {
        //获取BilliardStore的object id
        final BilliardStore billiardStore = new BilliardStore();
        billiardStore.setStoreID(storeID);
        BmobQuery<BilliardStore> billiardStoreBmobQuery = new BmobQuery<>();
        billiardStoreBmobQuery.findObjects(new FindListener<BilliardStore>() {
            @Override
            public void done(List<BilliardStore> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getStoreID().equals(storeID)) {
                            getBilliardStore(list.get(i).getObjectId(), billiardStore);
                        }
                    }
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    private void getBilliardStore(String objectID, BilliardStore billiardStore) {
        //获取图片
        //添加一行BilliardStore
        billiardStore.setObjectId(objectID);
        BmobFile bmobFile = null;
        try {
            bmobFile = new BmobFile(new File(new URI(cropImageUri.toString())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        switch (number) {
            case ONE:
                commitOneAD(bmobFile, billiardStore);
                break;
            case TWO:
                commitTwoAD(bmobFile, billiardStore);
                break;
            case THREE:
                commitThreeAD(bmobFile, billiardStore);
                break;
            default:
                break;
        }

    }

    private void commitThreeAD(BmobFile bmobFile, final BilliardStore billiardStore) {
        billiardStore.setPicture_3(bmobFile);
        billiardStore.getPicture_3().uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    billiardStore.update(billiardStore.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                                try {
                                    File file = new File(new URI(cropImageUri.toString()));
                                    threeCardView.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(file)));
                                } catch (Exception e1) {
                                    Log.e(TAG, "onActivityResult: 修改广告界面 3 获得裁剪后的照片时出错：" + e1.getMessage());
                                }
                            } else {
                                Log.e(TAG, "done: 上传广告三，保存失败 " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "done: 上传广告三时出错" + e.getMessage());
                }
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish: 上传3成功");
            }

            @Override
            public void onStart() {
                Log.e(TAG, "onStart: 开始上传广告二");
            }

            @Override
            public void onProgress(Integer value) {
                Log.d(TAG, "onProgress: 上传 3 进度：" + value);
            }
        });
    }

    private void commitTwoAD(BmobFile bmobFile, final BilliardStore billiardStore) {
        billiardStore.setPicture_2(bmobFile);
        billiardStore.getPicture_2().uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    billiardStore.update(billiardStore.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                                try {
                                    File file = new File(new URI(cropImageUri.toString()));
                                    twoCardView.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(file)));
                                } catch (Exception e1) {
                                    Log.e(TAG, "onActivityResult: 修改广告界面 2 获得裁剪后的照片时出错：" + e1.getMessage());
                                }
                            } else {
                                Log.e(TAG, "done: 上传广告二，保存失败 " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "done: 上传广告二时出错" + e.getMessage());
                }
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish: 上传2成功");
            }

            @Override
            public void onStart() {
                Log.e(TAG, "onStart: 开始上传广告二");
            }

            @Override
            public void onProgress(Integer value) {
                Log.d(TAG, "onProgress: 上传 2 进度：" + value);
            }
        });
    }

    private void commitOneAD(BmobFile bmobFile, final BilliardStore billiardStore) {
        billiardStore.setPicture_1(bmobFile);
        billiardStore.getPicture_1().uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    billiardStore.update(billiardStore.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                                try {
                                    File file = new File(new URI(cropImageUri.toString()));
                                    oneCardView.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(file)));
                                } catch (Exception e1) {
                                    Log.e(TAG, "onActivityResult: 修改广告界面 1 获得裁剪后的照片时出错：" + e1.getMessage());
                                }
                            } else {
                                Log.e(TAG, "done: 上传广告一，保存失败 " + e.getMessage() + " " + billiardStore.getObjectId());
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "done: 上传广告一时出错" + e.getMessage());
                }
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish: 上传 1 成功");
            }

            @Override
            public void onStart() {
                Log.e(TAG, "onStart: 开始上传广告一");
            }

            @Override
            public void onProgress(Integer value) {
                Log.d(TAG, "onProgress: 上传 1 进度：" + value);
            }
        });
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
        startActivityForResult(intent, AD_CROP_PICTURE);
    }

    private void handlerImageBeforeKitKat(Intent data) {
        Uri cropUri = data.getData();
        startPhotoZoom(cropUri);
    }

    private void handlerImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();

        startPhotoZoom(uri);
    }



    private void initViews(View view) {
        backImageVie = view.findViewById(R.id.shop_three_ad_back);
        oneCardView = view.findViewById(R.id.shop_three_ad_one);
        twoCardView = view.findViewById(R.id.shop_three_ad_two);
        threeCardView = view.findViewById(R.id.shop_three_ad_three);
    }

    private void initClicks() {
        backImageVie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTest = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .remove(fragmentTest)
                        .show(fragmentManager.findFragmentByTag("shop_fragment_mine"))
                        .commit();
            }
        });
        oneCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = ONE;
                MineDialog dialog = new MineDialog(getContext());
                setDialogClickListener(dialog);
            }
        });
        twoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = TWO;
                MineDialog dialog = new MineDialog(getContext());
                setDialogClickListener(dialog);
            }
        });
        threeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = THREE;
                MineDialog dialog = new MineDialog(getContext());
                setDialogClickListener(dialog);
            }
        });
    }
}
