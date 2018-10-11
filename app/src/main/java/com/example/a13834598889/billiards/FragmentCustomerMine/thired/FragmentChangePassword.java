package com.example.a13834598889.billiards.FragmentCustomerMine.thired;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a13834598889.billiards.FragmentCustomerMine.second.FragmentMessageSetting;
import com.example.a13834598889.billiards.FragmentShopKepperMine.second.FragmentShopMessageSetting;
import com.example.a13834598889.billiards.FragmentShopKepperMine.third.FragmentShopChangePassWord;
import com.example.a13834598889.billiards.JavaBean.ShopKeeper;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.LoginActivity;
import com.example.a13834598889.billiards.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/9/15.
 */

public class FragmentChangePassword extends Fragment  {
    private CircleImageView changePassWordBack;
    private EditText oldWord;
    private EditText newWord;
    private Button okWord;

    private Fragment fragmentTest;
    private FragmentManager fragmentManager;

    public static FragmentChangePassword newInstance(){
        FragmentChangePassword fragmentChangePassword = new FragmentChangePassword();
        return fragmentChangePassword;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_change_pass_word, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initViews(view);
        initClicks();
        return view;
    }

    private void initClicks() {
        changePassWordBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTest = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .remove(fragmentTest)
                        .add(R.id.fragment_container, FragmentMessageSetting.newInstance(), "customer_message_setting")
                        .commit();
            }
        });
        okWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldPassWord=oldWord.getText().toString();
                final String newPassWorld=newWord.getText().toString();
                if (newPassWorld.length()<6){
                    Toast.makeText(getContext(), "新密码长度不符合要求", Toast.LENGTH_SHORT).show();
                }else {
                    BmobQuery<User> userBmobQuery=new BmobQuery<>();
                    userBmobQuery.getObject(User.getCurrentUser().getObjectId(), new QueryListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e==null){
                                changePassword(user, oldPassWord, newPassWorld);
                            }else {
                                Log.e(TAG, "done: 修改密码时，查看原密码失败 "+e.getMessage() );
                            }
                        }
                    });
                }
            }
        });
    }

    private void changePassword(User user, String oldPassWord, final String newPassWorld) {
        final ShopKeeper shopKeeper=new ShopKeeper();
        shopKeeper.setPassword(oldPassWord);
        shopKeeper.setObjectId(user.getObjectId());
        shopKeeper.setUsername(user.getUsername());
        shopKeeper.login(new SaveListener<ShopKeeper>() {
            @Override
            public void done(ShopKeeper user, BmobException e) {
                if (e==null){
                    Log.e(TAG, "done: 修改密码时，验证密码成功" );
                    shopKeeper.setPassword(newPassWorld);
                    shopKeeper.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                changeSuccess();
                            }else {
                                Log.e(TAG, "done: "+e.getMessage() );
                            }
                        }
                    });
                }else {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeSuccess() {
        Toast.makeText(getContext(), "修改成功，请重新登录", Toast.LENGTH_LONG).show();
        getActivity().finish();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    private void initViews(View view) {
        changePassWordBack=view.findViewById(R.id.shop_change_pass_word_back);
        oldWord=view.findViewById(R.id.shop_change_pass_word_old);
        newWord=view.findViewById(R.id.shop_change_pass_word_new);
        okWord=view.findViewById(R.id.shop_change_pass_word_ok);
    }


}
