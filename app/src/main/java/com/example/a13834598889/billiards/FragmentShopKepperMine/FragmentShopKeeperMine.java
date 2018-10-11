package com.example.a13834598889.billiards.FragmentShopKepperMine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a13834598889.billiards.FragmentShopKepperMine.second.FragmentShopHelp;
import com.example.a13834598889.billiards.FragmentShopKepperMine.second.FragmentShopLocation;
import com.example.a13834598889.billiards.FragmentShopKepperMine.second.FragmentShopMemberMessage;
import com.example.a13834598889.billiards.FragmentShopKepperMine.second.FragmentShopMessageSetting;
import com.example.a13834598889.billiards.FragmentShopKepperMine.second.FragmentShopThreeAd;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.GetBmobPhotoFile;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class FragmentShopKeeperMine extends Fragment implements View.OnClickListener {

    private FragmentManager fragmentManager;
    public static CircleImageView profilePhoto;
    private LinearLayout mineMessageSetting;
    private LinearLayout membersSetting;
    private LinearLayout threeAd;
    private LinearLayout mineLocation;
    private LinearLayout helpLayout;

    public static TextView nickName;
    public static TextView sign;


    public static FragmentShopKeeperMine newInstance() {
        return new FragmentShopKeeperMine();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_keeper_mine, container, false);
        initViews(view);
        initView(view);
        loadingViews();
        return view;
    }

    public static void loadingViews() {
        BmobQuery<User> userBmobQuery=new BmobQuery<>();
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e==null){
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getObjectId().equals(User.getCurrentUser().getObjectId())){
                            if (list.get(i).getNickName()!=null){
                                nickName.setText(list.get(i).getNickName());
                            }
                            if (list.get(i).getSign()!=null){
                                sign.setText(list.get(i).getSign());
                            }
                        }
                    }
                }else {
                    Log.e(TAG, "done: "+e.getMessage() );
                }
            }
        });
    }

    public static void setInterfacePhoto(Bitmap bitmap) {
        profilePhoto.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .hide(this)
                .commit();
        switch (v.getId()) {
            case R.id.shop_keeper_mine_message_setting:
                //个人信息设置
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.fragment_container, FragmentShopMessageSetting.newInstance(), "shop_keeper_mine_message_setting")
                        .commit();
                break;
            case R.id.shop_keeper_mine_members_message:
                //会员信息
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.fragment_container, FragmentShopMemberMessage.newInstance(), "shop_keeper_mine_members_message")
                        .commit();
                break;
            case R.id.shop_keeper_mine_three_ad:
                //三个广告
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.fragment_container, FragmentShopThreeAd.newInstance(), "shop_keeper_mine_three_ad")
                        .commit();
                break;
            case R.id.shop_keeper_mine_store_location:
                //地理位置
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.fragment_container, FragmentShopLocation.newInstance(), "shop_keeper_mine_store_location")
                        .commit();
                break;
            case R.id.shop_keeper_mine_help:
                //帮助
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.fragment_container, FragmentShopHelp.newInstance(), "shop_keeper_mine_help")
                        .commit();
                break;
            default:
                break;
        }
    }

    public static void setShopShowIcon(Bitmap bitmap) {
        profilePhoto.setImageBitmap(bitmap);
    }

    private void initViews(View view) {
        profilePhoto = view.findViewById(R.id.shop_keeper_mine_profile_photo);
        mineMessageSetting = view.findViewById(R.id.shop_keeper_mine_message_setting);
        membersSetting = view.findViewById(R.id.shop_keeper_mine_members_message);
        threeAd = view.findViewById(R.id.shop_keeper_mine_three_ad);
        mineLocation = view.findViewById(R.id.shop_keeper_mine_store_location);
        helpLayout = view.findViewById(R.id.shop_keeper_mine_help);
        mineMessageSetting.setOnClickListener(this);
        membersSetting.setOnClickListener(this);
        threeAd.setOnClickListener(this);
        mineLocation.setOnClickListener(this);
        helpLayout.setOnClickListener(this);
        GetBmobPhotoFile.initInterface("店铺信息界面", 1,getContext());
    }

    public static void initView(View view){
        nickName=view.findViewById(R.id.shop_mine_nick_name);
        sign=view.findViewById(R.id.shop_mine_sign);
    }
}
