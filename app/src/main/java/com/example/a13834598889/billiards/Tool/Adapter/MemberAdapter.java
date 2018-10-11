package com.example.a13834598889.billiards.Tool.Adapter;

/**
 * Created by xieyipeng on 2018/10/9.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a13834598889.billiards.JavaBean.Member;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.LeftSlideView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder> implements LeftSlideView.IonSlidingButtonListener {
    private List<User> member;
    private Context mContext;
    private LeftSlideView mMenu = null;

    public MemberAdapter(List<User> users, Context context) {
        this.member = users;
        this.mContext = context;
    }

    @Override
    public MemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //获取自定义View的布局（加载item布局）
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_layout, parent, false);
        return new MemberHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemberHolder holder, int position) {
        final User user = member.get(position);
        holder.bindView(user);
        holder.layout_content.getLayoutParams().width = mContext.getResources().getDisplayMetrics().widthPixels;
        holder.mineLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuIsOpen()) {
                    closeMenu();
                } else {
                    int n = holder.getLayoutPosition();
                    Toast.makeText(mContext, "和 " + n + " 即时通讯，详细信息等", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //左滑设置点击事件
        holder.btn_Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "设置", Toast.LENGTH_SHORT).show();
                int n = holder.getLayoutPosition();
                closeMenu();

            }
        });

        //左滑删除点击事件
        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition();
                //球友名=member.get(n).getUserName();
                Log.e(TAG, "onClick: 删除 " );
                String userName = member.get(n).getUsername();
                //店家名
                getShopNickName(userName, n);
            }

            private void getShopNickName(final String userName, final int position) {
                //店家名
                BmobQuery<User> userBmobQuery = new BmobQuery<>();
                userBmobQuery.getObject(User.getCurrentUser(User.class).getObjectId(), new QueryListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            if (user==null){
                                Log.e(TAG, "done: 删除会员时，user为空");
                            }
                            assert user != null;
                            getMemberID(userName, position, user.getUsername());
                        } else {
                            Log.e(TAG, "done: 删除会员时错误" + e.getMessage());
                        }
                    }
                });
            }

            //拿到店铺名和球友名
            private void getMemberID(final String userName, final int position, final String storeNickName) {
                //拿到该会员的id
                BmobQuery<Member> memberBmobQuery = new BmobQuery<>();
                memberBmobQuery.findObjects(new FindListener<Member>() {
                    @Override
                    public void done(List<Member> list, BmobException e) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getStoreName().equals(storeNickName) && list.get(i).getUserName().equals(userName)) {
                                Member deleteMember=new Member();
                                deleteMember.setObjectId(list.get(i).getObjectId());
                                deleteMember.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e==null){
                                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                            member.remove(position);
                                            notifyItemRemoved(position);
                                        }else {
                                            Log.e(TAG, "done: 获取memberID时，删除失败 "+e.getMessage() );
                                        }
                                        closeMenu();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return member.size();
    }

    class MemberHolder extends RecyclerView.ViewHolder {

        private TextView btn_Set;
        private TextView btn_Delete;
        private LinearLayout mineLinearLayout;
        private ViewGroup layout_content;

        private TextView memberItemNickNameTextView;
        private TextView memberItemSignTextView;
        private CircleImageView memberPhotoCircleView;

        public MemberHolder(View view) {
            super(view);
            memberItemNickNameTextView = view.findViewById(R.id.shop_member_nick_name);
            memberItemSignTextView = view.findViewById(R.id.shop_member_sign);
            memberPhotoCircleView = view.findViewById(R.id.shop_member_photo);

            btn_Set = itemView.findViewById(R.id.left_setting_TextView);
            btn_Delete = itemView.findViewById(R.id.left_delete_TextView);
            mineLinearLayout = itemView.findViewById(R.id.left_text);
            layout_content = itemView.findViewById(R.id.layout_content);

            //和菜单左滑有关
            ((LeftSlideView) itemView).setSlidingButtonListener(MemberAdapter.this);
        }


        public void bindView(User user) {
            memberItemNickNameTextView.setText(user.getNickName());
            if (user.getSign() != null) {
                memberItemSignTextView.setText(user.getSign());
            } else {
                memberItemSignTextView.setText(R.string.SnoSign);
            }
            if (user.getPicture_head() != null) {
                user.getPicture_head().download(new DownloadFileListener() {
                    @Override
                    public void onStart() {
                        Log.e(TAG, "onFinish: 会员界面，头像开始下载");
                    }

                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Bitmap bitmap = BitmapFactory.decodeFile(s);
                            memberPhotoCircleView.setImageBitmap(bitmap);
                        } else {
                            Log.e(TAG, "done: 下载出错" + e.getMessage());
                        }
                    }

                    @Override
                    public void onProgress(Integer integer, long l) {
                        Log.d("bmob", "onProgress: 会员界面，文件时下载进度：" + integer + "," + l);
                    }

                    @Override
                    public void onFinish() {
                        Log.e(TAG, "onFinish: 会员界面，头像下载完成");
                    }
                });
            } else {
                Log.e(TAG, "bindView: 会员界面，user.getPicture_head() == null");
                memberPhotoCircleView.setImageResource(R.drawable.touxiang);
            }
        }
    }

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (LeftSlideView) view;
    }

    /**
     * 滑动或者点击了Item监听
     *
     * @param leftSlideView
     */
    @Override
    public void onDownOrMove(LeftSlideView leftSlideView) {
        if (menuIsOpen()) {
            if (mMenu != leftSlideView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;
    }

    /**
     * 判断菜单是否打开
     *
     * @return
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }
}
