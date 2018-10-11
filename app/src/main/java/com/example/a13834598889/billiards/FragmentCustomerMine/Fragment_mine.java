package com.example.a13834598889.billiards.FragmentCustomerMine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a13834598889.billiards.FragmentCustomerMine.second.FragmentHelp;
import com.example.a13834598889.billiards.FragmentCustomerMine.second.FragmentMessageSetting;
import com.example.a13834598889.billiards.FragmentCustomerMine.second.Fragment_card;
import com.example.a13834598889.billiards.FragmentCustomerMine.second.Fragment_friends;
import com.example.a13834598889.billiards.FragmentCustomerMine.second.Fragment_huihua;
import com.example.a13834598889.billiards.FragmentCustomerMine.second.Fragment_yuding;
import com.example.a13834598889.billiards.JavaBean.Customer;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.GetBmobMinePhoto;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13834598889 on 2018/4/29.
 */

public class Fragment_mine extends Fragment {

    private LinearLayout textView_button_tiezi;
    private LinearLayout textView_button_qiuyou;
    private LinearLayout textView_button_yuding;
    private LinearLayout textView_button_qiubi;
    private LinearLayout textView_button_bangzhu;
    private ImageView huihua;
    private LinearLayout message_button;
    public static TextView nickName;
    public static TextView sign;
    public static CircleImageView profilePhoto;
    private FragmentManager fragmentManager;

    private View view2;

    //TODO: fragment_mine
    public static Fragment_mine newInstance() {
        return new Fragment_mine();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         view2 = inflater.inflate(R.layout.fragment_mine, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initViews(view2);
        initView(view2);
        initClicks();
        getMessage();
        return view2;
    }

    private void initClicks() {
        textView_button_qiuyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .hide(fragmentManager.findFragmentByTag("fragment_mine"))
                        .add(R.id.fragment_container, Fragment_friends.newInstance(), "text_button_wodeqiuyou")
                        .commit();
            }
        });

        textView_button_yuding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .hide(fragmentManager.findFragmentByTag("fragment_mine"))
                        .add(R.id.fragment_container, Fragment_yuding.newInstance(), "text_button_wodeyuding")
                        .commit();
            }
        });

        textView_button_tiezi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .hide(fragmentManager.findFragmentByTag("fragment_mine"))
                        .add(R.id.fragment_container, Fragment_card.newInstance(), "card_fragment")
                        .commit();
            }
        });

        huihua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .hide(fragmentManager.findFragmentByTag("fragment_mine"))
                        .add(R.id.fragment_container, Fragment_huihua.newInstance(), "huihua")
                        .commit();
            }
        });

        textView_button_bangzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .hide(fragmentManager.findFragmentByTag("fragment_mine"))
                        .add(R.id.fragment_container, FragmentHelp.newInstance(), "text_button_bangzhu")
                        .commit();
            }
        });
        message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .hide(fragmentManager.findFragmentByTag("fragment_mine"))
                        .add(R.id.fragment_container, FragmentMessageSetting.newInstance(), "customer_message_setting")
                        .commit();
            }
        });


        textView_button_qiubi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                dialog.setTitleText("您的球币数量为10")
                        .show();
                dialog.setCanceledOnTouchOutside(true);
            }
        });
    }

    private void initViews(View view) {
        profilePhoto = view.findViewById(R.id.circleImageView_mine00);
        textView_button_tiezi = view.findViewById(R.id.text_button_wodetiezi);
        huihua=view.findViewById(R.id.chat_huihua);
        textView_button_qiuyou = view.findViewById(R.id.text_button_wodeqiuyou);
        textView_button_yuding = view.findViewById(R.id.text_button_wodeyuding);
        textView_button_qiubi = view.findViewById(R.id.text_button_wodeqiubi);
        textView_button_bangzhu = view.findViewById(R.id.text_button_bangzhu);
        message_button = view.findViewById(R.id.message_button);

        GetBmobMinePhoto.initInterface("编辑资料界面", 1,getContext());
    }

    public static void initView(View view){
        nickName = view.findViewById(R.id.nick_name);
        sign = view.findViewById(R.id.sign);
    }

    public static void setInterfacePhotoMine(Bitmap bitmap) {
        profilePhoto.setImageBitmap(bitmap);
    }

    public static void setShopShowIconMine(Bitmap bitmap) {
        profilePhoto.setImageBitmap(bitmap);
    }

    public static void getMessage(){
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(User.getCurrentUser(Customer.class).getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if (e == null) {
                    nickName.setText(object.getNickName());
                } else {

                }
            }
        });

        bmobQuery.getObject(User.getCurrentUser(Customer.class).getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if (e == null) {
                    sign.setText(object.getSign());
                } else {

                }
            }
        });
    }
}