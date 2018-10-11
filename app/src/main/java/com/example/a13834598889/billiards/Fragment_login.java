package com.example.a13834598889.billiards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xieyipeng on 2018/10/4.
 */

public class Fragment_login extends Fragment {


    private EditText editText_account;
    private EditText editText_passWord;
    private Button button_login;
    private CheckBox checkBox_remberPassWord;
    private SharedPreferences pref;
    private ImageView login;
    private SharedPreferences.Editor editor;

    boolean isRember;

    private ProgressBar progressBar;


    private View view;

    public static Fragment_login newInstance(){
        Fragment_login fragment_login = new Fragment_login();
        return fragment_login;
    }


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initView();
        return view;
    }
    private void login() {
        progressBar.setVisibility(View.VISIBLE);
        button_login.setEnabled(false);
        final String account0 = editText_account.getText().toString();
        final String passWord0 = editText_passWord.getText().toString();
        User user = new User();
        user.setUsername(account0);
        user.setPassword(passWord0);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    editor = pref.edit();
                    if (checkBox_remberPassWord.isChecked()) {
                        editor.putBoolean("rember_passWord", true);
                        editor.putString("account", account0);
                        editor.putString("password", passWord0);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "密码错误", Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.INVISIBLE);
                    button_login.setEnabled(true);
                }
            }
        });
    }



    private void initView() {
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isRember = pref.getBoolean("rember_passWord", false);


        progressBar = (ProgressBar) getActivity().findViewById(R.id.main_progress);
        editText_account = (EditText) view.findViewById(R.id.login_account_Edit_text);
        editText_passWord = (EditText) view.findViewById(R.id.login_passWord_Edit_text);
        button_login = (Button) view.findViewById(R.id.login_button);
        checkBox_remberPassWord = (CheckBox) view.findViewById(R.id.rember_passWord_Check_Box);



        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        if (isRember) {
            String account = pref.getString("account", "");
            String passWord = pref.getString("password", "");
            editText_account.setText(account);
            editText_passWord.setText(passWord);
            checkBox_remberPassWord.setChecked(true);
        }


    }



}
