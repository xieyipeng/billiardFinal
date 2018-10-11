package com.example.a13834598889.billiards.FragmentCustomerShare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a13834598889.billiards.JavaBean.Card;
import com.example.a13834598889.billiards.JavaBean.Customer;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by xieyipeng on 2018/10/3.
 */

public class Fragment_share_message extends Fragment {

    private ImageView imageView;
    private TextView textView;
    private static String imageId;
    private static Customer customer;
    private ImageView back;
    private FragmentManager fragmentManager;

    //TODO: fragment_share_message
    public static Fragment_share_message newInstance(Customer customer, String uri, String text) {
        Fragment_share_message.imageId = uri;
        Fragment_share_message.customer = customer;
        return new Fragment_share_message();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shaer_message, container, false);
        initView(view);
        initClicks();
        return view;
    }

    private void initClicks() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentManager.findFragmentByTag("fragment_share_message") != null) {
                    fragmentManager.beginTransaction()
                            .remove(fragmentManager.findFragmentByTag("fragment_share_message"))
                            .show(fragmentManager.findFragmentByTag("fragment_share"))
                            .commit();
                }
            }
        });
    }

    private void initView(View view) {
        fragmentManager = getActivity().getSupportFragmentManager();
        back = view.findViewById(R.id.share_back);
        imageView = view.findViewById(R.id.share_image);
        textView = view.findViewById(R.id.message_share);
        Glide.with(this).load(imageId).into(imageView);

        BmobQuery<Card> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("customer", customer);
        bmobQuery.findObjects(new FindListener<Card>() {
            @Override
            public void done(List<Card> list, BmobException e) {
              for (Card card : list){
                  textView.setText(card.getText());
              }
            }
        });


    }
}