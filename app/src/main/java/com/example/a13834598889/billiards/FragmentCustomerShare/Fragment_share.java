package com.example.a13834598889.billiards.FragmentCustomerShare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a13834598889.billiards.FragmentCustomerMine.thired.Fragment_card_add;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.Adapter.CardAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.io.IOException;
import java.util.List;

import cn.bmob.newim.db.MigrationHelper;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.volley.VolleyLog.TAG;

/**
 * Created by 13834598889 on 2018/4/29.
 */

public class Fragment_share extends Fragment {


    private static CircleImageView circleImageView3;
    private ImageView add;
    private AssetManager manager;
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private View view;
    private FragmentManager fragmentManager;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SoundPool soundPool;

    //TODO: fragment_share
    public static Fragment_share newInstance() {
        return new Fragment_share();
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_share_final, container, false);
        fragmentManager=getActivity().getSupportFragmentManager();
        initViews(view);
        initClicks();
        return view;
    }

    private void initClicks() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentManager.findFragmentByTag("fragment_share") != null) {
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("fragment_share"))
                            .add(R.id.fragment_container, Fragment_card_add.newInstance(), "fragment_card_add")
                            .commit();
                }
            }
        });
    }



    private void initViews(View view) {
        circleImageView3 = view.findViewById(R.id.circleImageView_mine3);
        slidingTabLayout = view.findViewById(R.id.sliding_tabs_2);
        slidingTabLayout.setTextSelectColor(R.color.title_text);
//        slidingTabLayout.setTextUnselectColor(R.color.colorAccent);
//        slidingTabLayout.setUnderlineColor(R.color.colorAccent);
//        slidingTabLayout.setDividerColor(R.color.colorAccent);
//        slidingTabLayout.setIndicatorColor(R.color.colorAccent);
        add = view.findViewById(R.id.shengyin);

        viewPager = view.findViewById(R.id.viewPager_container5);
        CardAdapter adapter = new CardAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        slidingTabLayout.setViewPager(viewPager);
        loadingPhoto();
    }


    //TODO: 设置界面修改头像之后刷新本界面UI
    public static void changeSharePhoto(Bitmap bitmap) {
        circleImageView3.setImageBitmap(bitmap);
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
                                            circleImageView3.setImageBitmap(bitmap);
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
                                        .into(circleImageView3);
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











