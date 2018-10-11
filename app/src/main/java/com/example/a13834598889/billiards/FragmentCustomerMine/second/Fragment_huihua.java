package com.example.a13834598889.billiards.FragmentCustomerMine.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.Adapter.ContactsAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;

import static org.greenrobot.eventbus.EventBus.TAG;

public class Fragment_huihua extends Fragment {

    //会话

    private FragmentManager fragmentManager;
    private ImageView back;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private LinearLayout linearLayout;
    private List<User> userList = new ArrayList<>();

    public static Fragment_huihua newInstance() {
        return new Fragment_huihua();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_huihua, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initViews(view);
        initClicks();
        return view;
    }




    private void initClicks() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
//        lin.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                lin.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                refreshLayout.setRefreshing(true);
//                //自动刷新
//                queryMessages(null);
//            }
//        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager.findFragmentByTag("huihua") != null) {
                    fragmentManager.beginTransaction()
                            .remove(fragmentManager.findFragmentByTag("huihua"))
                            .show(fragmentManager.findFragmentByTag("fragment_mine"))
                            .commit();
                }
            }
        });
    }

    /**
     * 获取会话列表的数据：增加新朋友会话
     *
     * @return
     */
    private void getConversations() {
        //添加会话
        userList.clear();
        //TODO 会话：4.2、查询全部会话
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                switch (list.get(i).getConversationType()) {
                    case 1://私聊
                        if (!list.get(i).getConversationId().equals(User.getCurrentUser().getObjectId())) {
                            final User user = new User();
                            user.setObjectId(list.get(i).getConversationId());
                            userList.add(user);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        if (list.size() == 0){
            refreshLayout.setRefreshing(false);
        }

        Log.e(TAG, "getConversations: size"+userList.size() );
        if (userList.size()!=0){
            adapter = new ContactsAdapter(userList, getContext(), fragmentManager, "huihua");
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLayout.setRefreshing(true);
        query();
    }

    private void query() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        getConversations();
    }

    private void initViews(View view) {
        linearLayout = view.findViewById(R.id.huihua_big);
        back = view.findViewById(R.id.message_back);
        refreshLayout = view.findViewById(R.id.message_refresh);
        recyclerView = view.findViewById(R.id.message_view);
    }
}
