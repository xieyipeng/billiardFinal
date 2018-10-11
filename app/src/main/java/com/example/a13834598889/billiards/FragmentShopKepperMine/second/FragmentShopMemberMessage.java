package com.example.a13834598889.billiards.FragmentShopKepperMine.second;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a13834598889.billiards.JavaBean.Member;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.Adapter.MemberAdapter;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class FragmentShopMemberMessage extends Fragment {

    private CircleImageView memberBack;
    private CircleImageView memberAdd;
    private Fragment fragmentTest;
    private FragmentManager fragmentManager;
    private LinearLayout no_thing;
    private MemberAdapter memberAdapter;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView memberRecyclerView;
    private List<User> users = new ArrayList<>();

    public static FragmentShopMemberMessage newInstance() {
        return new FragmentShopMemberMessage();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_member_message, container, false);
        initViews(view);
        initClicks();
        loadingMessage();//更新列表
        return view;
    }

    private void initClicks() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingMessage();
                refreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        memberBack.setOnClickListener(new View.OnClickListener() {
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
        memberAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(getContext());
                Dialog dialog = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("请输入好友的账号")
                        .setView(editText)
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                BmobQuery<User> bmobQuery = new BmobQuery<>();
                                bmobQuery.getObject(User.getCurrentUser().getObjectId(), new QueryListener<User>() {
                                    @Override
                                    public void done(User user, BmobException e) {
                                        if (e == null) {
                                            Member member = new Member();
                                            member.setStoreName(user.getUsername());
                                            setMemberUserName(member, editText.getText().toString());
                                        } else {
                                            Log.e(BmobRealTimeData.TAG, "done: " + e.getMessage());
                                        }
                                    }
                                });

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                dialog.setCanceledOnTouchOutside(true);
            }
        });
    }

    private void setMemberUserName(final Member member, final String inputUserName) {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    boolean have = false;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getUsername().equals(inputUserName)) {
                            have = true;
                            final String name = list.get(i).getUsername();
                            if (list.get(i).getPicture_head() != null) {
                                list.get(i).getPicture_head().download(new DownloadFileListener() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                                            sweetAlertDialog.setCustomImage(Drawable.createFromPath(s));
                                            sweetAlertDialog.setTitleText("确认添加？");
                                            sweetAlertDialog.show();
                                            sweetAlertDialog.setConfirmText("添加");
                                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    member.setUserName(name);
                                                    member.save(new SaveListener<String>() {
                                                        @Override
                                                        public void done(String s, BmobException e) {
                                                            if (e == null) {
                                                                Toast.makeText(getContext(), "添加会员成功", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                if (e.getMessage().equals("unique index cannot has duplicate value: newName")) {
                                                                    Toast.makeText(getContext(), "您已经添加了该会员", Toast.LENGTH_SHORT).show();
                                                                }
                                                                Toast.makeText(getContext(), "添加会员失败", Toast.LENGTH_SHORT).show();
                                                                Log.e(BmobRealTimeData.TAG, "done: 保存会员失败 " + e.getMessage());
                                                            }
                                                        }
                                                    });
                                                    sweetAlertDialog.dismiss();
                                                }
                                            });
                                        } else {
                                            Log.e(BmobRealTimeData.TAG, "done: " + e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onProgress(Integer integer, long l) {

                                    }
                                });
                            } else {
                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                                sweetAlertDialog.setCustomImage(R.drawable.touxiang);
                                sweetAlertDialog.setTitleText("确认添加？");
                                sweetAlertDialog.show();
                                sweetAlertDialog.setConfirmText("添加");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        member.setUserName(name);
                                        member.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(getContext(), "添加会员成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (e.getMessage().equals("unique index cannot has duplicate value: newName")) {
                                                        Toast.makeText(getContext(), "您已经添加了该会员", Toast.LENGTH_SHORT).show();
                                                    }
                                                    Toast.makeText(getContext(), "添加会员失败", Toast.LENGTH_SHORT).show();
                                                    Log.e(BmobRealTimeData.TAG, "done: 保存会员失败 " + e.getMessage());
                                                }
                                            }
                                        });
                                        sweetAlertDialog.dismiss();
                                        fragmentTest = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                                        fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                                .remove(fragmentTest)
                                                .add(R.id.fragment_container, FragmentShopMemberMessage.newInstance(), "shop_keeper_mine_members_message")
                                                .commit();
                                    }
                                });
                            }
                            break;
                        }
                    }
                    if (!have) {
                        Toast.makeText(getContext(), "没有该名字", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(BmobRealTimeData.TAG, "done: 添加会员界面，查找姓名时失败 " + e.getMessage());
                }
            }
        });
    }

    private void loadingMessage() {
        final String storeUserName = User.getCurrentUser().getUsername();
        BmobQuery<Member> memberBmobQuery = new BmobQuery<>();
        memberBmobQuery.findObjects(new FindListener<Member>() {
            @Override
            public void done(List<Member> list, BmobException e) {
                //拿到该店所有会员的姓名
                if (e == null) {
                    List<Member> list1 = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getStoreName().equals(storeUserName)) {
                            list1.add(list.get(i));
                        }
                    }
                    users.clear();
                    for (int i = 0; i < list1.size(); i++) {
                        boolean isLast = false;
                        if (i == list1.size() - 1) {
                            isLast = true;
                        }
                        getUsers(list1.get(i).getUserName(), isLast);
                    }
                } else {
                    Log.e(TAG, "done: 加载会员界面时出现错误");
                }
            }
        });
    }

    private void getUsers(final String username, final boolean isLast) {
        final BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                //拿到user
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getUsername().equals(username)) {
                            users.add(list.get(i));
                        }
                    }
                    Log.e(TAG, "done: " + users.size());
                    if (users.size() == 0) {
                        Toast.makeText(getContext(), "店长，您还没有会员呢", Toast.LENGTH_SHORT).show();
                        no_thing.setVisibility(View.VISIBLE);
                        memberRecyclerView.setVisibility(View.INVISIBLE);
                    } else {
                        if (isLast) {
                            setRecycleView();
                            no_thing.setVisibility(View.INVISIBLE);
                            memberRecyclerView.setVisibility(View.VISIBLE);

                        }
                    }
                } else {
                    Log.e(TAG, "done: 获取user时出错: " + e.getMessage());
                }
            }
        });
    }

    private void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        memberAdapter = new MemberAdapter(users, getContext());
        memberRecyclerView.setAdapter(memberAdapter);
        memberRecyclerView.setLayoutManager(layoutManager);
    }

    private void initViews(View view) {
        no_thing = view.findViewById(R.id.shop_member_no_things);
        refreshLayout = view.findViewById(R.id.shop_member_message_refresh);
        memberRecyclerView = view.findViewById(R.id.shop_member_RecycleView);
        memberBack = view.findViewById(R.id.shop_member_back_ImageView);
        memberAdd = view.findViewById(R.id.shop_member_add_ImageView);
    }
}
