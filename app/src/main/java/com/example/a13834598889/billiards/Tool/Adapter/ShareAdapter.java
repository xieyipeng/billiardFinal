package com.example.a13834598889.billiards.Tool.Adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.a13834598889.billiards.FragmentCustomerShare.Fragment_share_message;
import com.example.a13834598889.billiards.JavaBean.Card;
import com.example.a13834598889.billiards.JavaBean.Customer;
import com.example.a13834598889.billiards.JavaBean.Friend;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.bmob.newim.core.BmobIMClient.getContext;
import static cn.volley.VolleyLog.TAG;


public class ShareAdapter extends BaseQuickAdapter<Card, BaseViewHolder> {

    private Context context;

    public ShareAdapter(int layoutResId, @Nullable List<Card> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Card card) {
//        //TODO: 创建时间
//        helper.setText(R.id.text_share_item_time, card.getCreatedAt());
        //TODO: item图片内容
        if (card.getPicture() != null) {
            card.getPicture().download(new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Glide.with(context).load(new File(s)).into((ImageView) helper.getView(R.id.image_share_item_image));
                    } else {
                        Log.e(TAG, "done: " + e.getMessage());
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {
//                        Log.d("bmob", "onProgress: 下载进度：" + integer + "," + l);
                }
            });
        } else {
            ImageView del = helper.getView(R.id.image_share_item_image);
            del.setVisibility(View.GONE);
        }
        //TODO: 内容

//        //TODO: 点赞数
//        helper.setText(R.id.text_dianzanshu, String.valueOf(card.getDianzan().size()));
        //TODO: nickName
        Customer customer = card.getCustomer();
        BmobQuery<Customer> customerBmobQuery = new BmobQuery<>();
        customerBmobQuery.getObject(customer.getObjectId(), new QueryListener<Customer>() {
            @Override
            public void done(Customer customer, BmobException e) {
                if (e == null) {
                    helper.setText(R.id.text_share_item_idName, customer.getNickName());
                    getHeadPhoto(customer);
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }

            //TODO: item头像
            private void getHeadPhoto(Customer customer) {
                if (customer.getPicture_head() != null) {
                    customer.getPicture_head().download(new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Glide.with(context).load(new File(s)).crossFade().into((ImageView) helper.getView(R.id.image_share_item_circleView));
                            } else {
                                Log.e(TAG, "done: " + e.getMessage());
                            }
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {
//                                Log.d("bmob", "onProgress: 下载进度：" + integer + "," + l);
                        }
                    });
                } else {
                    Resources resources = context.getResources();
                    Drawable drawable = resources.getDrawable(R.drawable.touxiang);
                    helper.setImageDrawable(R.id.image_share_item_circleView, drawable);
                }
            }
        });
        initClicks(helper, card, helper.getLayoutPosition());
    }

    private void initClicks(BaseViewHolder helper, final Card card, final int position) {
        //TODO: 点赞点击事件
        final ImageView dianzan = helper.getView(R.id.image_button_dianzan);
        dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionCartoon(dianzan);
                if (card.getDianzan()!=null) {
                    if (card.getDianzan().contains(Customer.getCurrentUser().getObjectId())) {
                        card.getDianzan().remove(Customer.getCurrentUser().getObjectId());
                        card.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    notifyItemChanged(position);
                                } else {
                                    Log.e(TAG, "done: " + e.getMessage());
                                }
                            }
                        });
                    } else {
                        card.getDianzan().add(Customer.getCurrentUser().getObjectId());
                        card.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    notifyItemChanged(position);
                                } else {
                                    Log.e(TAG, "done: " + e.getMessage());
                                }
                            }
                        });
                    }
                }else {
                    card.setDianzan(new ArrayList<String>());
                    card.getDianzan().add(Customer.getCurrentUser().getObjectId());
                    card.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                notifyItemChanged(position);
                            }else {
                                Log.e(TAG, "done: "+e.getMessage() );
                            }
                        }
                    });
                }
            }
        });
        final ImageView shoucang = helper.getView(R.id.image_button_shoucang);
        shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionCartoon(shoucang);
                if (card.getShoucang()==null){
                    card.setShoucang(new ArrayList<String>());
                    card.getShoucang().add(Customer.getCurrentUser().getObjectId());
                    card.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                Toast.makeText(context, "已收藏", Toast.LENGTH_SHORT).show();
                            }else {
                                Log.e(TAG, "done: "+e.getMessage() );
                            }
                        }
                    });
                }else {
                    if (card.getShoucang().contains(Customer.getCurrentUser().getObjectId())) {
                        card.getShoucang().remove(Customer.getCurrentUser().getObjectId());
                        card.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(context, "已取消收藏", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e(TAG, "done: " + e.getMessage());
                                }
                            }
                        });
                    } else {
                        card.getShoucang().add(Customer.getCurrentUser().getObjectId());
                        card.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(context, "已收藏", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e(TAG, "done: " + e.getMessage());
                                }
                            }
                        });
                    }
                }

            }
        });
//        ImageView jiahaoyou=helper.getView(R.id.image_button_jiahaoyou);
//        jiahaoyou.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
//                dialog.setTitleText("是否要添加好友？")
//                        .setConfirmText("Yes")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                final User user = new User();
//                                BmobQuery<User> bmobQuery = new BmobQuery<>();
//                                bmobQuery.getObject(card.getCustomer().getObjectId(), new QueryListener<User>() {
//                                    @Override
//                                    public void done(User user1, BmobException e) {
//                                        if (e == null) {
//                                            user.setUsername(user1.getUsername());
//                                        } else {
//                                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
//                                        }
//                                    }
//                                });
//                                BmobQuery<User> userBmobQuery = new BmobQuery<>();
//                                userBmobQuery.findObjects(new FindListener<User>() {
//                                    @Override
//                                    public void done(List<User> list, BmobException e) {
//                                        if (e == null) {
//                                            getUser(list, user);
//                                            dialog.dismiss();
//                                        } else {
//                                            Log.e(BmobRealTimeData.TAG, "done: " + e.getMessage());
//                                        }
//                                    }
//                                });
//                            }
//                        })
//                        .setCancelText("No")
//                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
//                dialog.setCanceledOnTouchOutside(true);
//            }
//        });
    }

    //TODO: 动画
    private void actionCartoon(View imageView) {
        ObjectAnimator button_dianzan0 = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.7f, 1f);
        ObjectAnimator button_dianzan1 = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.7f, 1f);
        ObjectAnimator button_dianzan2 = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000)
                .play(button_dianzan0)
                .with(button_dianzan1)
                .with(button_dianzan2);
        animatorSet.start();
    }

//    private void getUser(List<User> list, final User user) {
//        boolean have = false;
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getUsername().equals(user.getUsername())) {
//                if (list.get(i).getObjectId().equals(User.getCurrentUser(User.class).getObjectId())) {
//                    have = true;
//                    Toast.makeText(context, "不能添加自己", Toast.LENGTH_SHORT).show();
//                } else {
//                    have = true;
//                    final User testUser = list.get(i);
//                    BmobQuery<Friend> query = new BmobQuery<>();
//                    query.findObjects(new FindListener<Friend>() {
//                        @Override
//                        public void done(List<Friend> list, BmobException e) {
//                            boolean isFriend = false;
//                            for (int j = 0; j < list.size(); j++) {
//                                if (list.get(j).getUser().getObjectId().equals(User.getCurrentUser().getObjectId()) &&
//                                        list.get(j).getFriendUser().getObjectId().equals(testUser.getObjectId())) {
//                                    isFriend = true;
//                                }
//                            }
//                            if (isFriend) {
//                                Toast.makeText(context, "你们已经是好友了", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Friend friend = new Friend();
//                                friend.setUser(User.getCurrentUser(User.class));
//                                friend.setFriendUser(testUser);
//                                friend.save(new SaveListener<String>() {
//                                    @Override
//                                    public void done(String s, BmobException e) {
//                                        if (e == null) {
//                                            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Log.e(BmobRealTimeData.TAG, "done: " + e.getMessage());
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//                }
//            }
//        }
//        if (!have) {
//            Toast.makeText(context, "不存在此账号", Toast.LENGTH_SHORT).show();
//        }
//    }
}


//public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareHolder> {
//
//    private List<Card> cards;
//    private Context mContext;
//
//
//
//
//    public ShareAdapter(List<Card> cards, Context context) {
//        this.cards = cards;
//        this.mContext = context;
//    }
//
//
//    @Override
//    public ShareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_item, parent, false);
//        return new ShareHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final ShareHolder holder, final int position) {
//        final Card card = cards.get(position);
//        holder.bindView(card);
//        holder.dianzan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                actionCartoon(holder.dianzan);
//
//                if (card.getDianzan().contains(Customer.getCurrentUser().getObjectId())) {
//                    card.getDianzan().remove(Customer.getCurrentUser().getObjectId());
//                    card.update(new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if (e==null){
//                                notifyItemChanged(position);
//                            }else {
//                                Log.e(TAG, "done: "+e.getMessage() );
//                            }
//                        }
//                    });
//                } else {
//                    card.getDianzan().add(Customer.getCurrentUser().getObjectId());
//                    card.update(new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if (e==null){
//                                notifyItemChanged(position);
//                            }else {
//                                Log.e(TAG, "done: "+e.getMessage() );
//                            }
//                        }
//                    });
//                }
//            }
//        });
//        holder.shoucang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                actionCartoon(holder.shoucang);
//                if (card.getShoucang().contains(Customer.getCurrentUser().getObjectId())){
//                    card.getShoucang().remove(Customer.getCurrentUser().getObjectId());
//                    card.update(new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if (e==null){
//                                Toast.makeText(mContext, "已取消收藏", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Log.e(TAG, "done: "+e.getMessage() );
//                            }
//                        }
//                    });
//                }else {
//                    card.getShoucang().add(Customer.getCurrentUser().getObjectId());
//                    card.update(new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if (e==null){
//                                Toast.makeText(mContext, "已收藏", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Log.e(TAG, "done: "+e.getMessage() );
//                            }
//                        }
//                    });
//                }
//            }
//        });
//
//        holder.jiahaoyou.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
//                dialog.setTitleText("是否要添加好友？")
//                        .setConfirmText("Yes")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                final User user = new User();
//                                BmobQuery<User> bmobQuery = new BmobQuery<>();
//                                bmobQuery.getObject(card.getCustomer().getObjectId(), new QueryListener<User>() {
//                                    @Override
//                                    public void done(User user1, BmobException e) {
//                                        if (e ==  null){
//                                            user.setUsername(user1.getUsername());
//                                        }else{
//                                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                                        }
//                                    }
//                                });
//                                BmobQuery<User> userBmobQuery=new BmobQuery<>();
//                                userBmobQuery.findObjects(new FindListener<User>() {
//                                    @Override
//                                    public void done(List<User> list, BmobException e) {
//                                        if (e==null){
//                                            getUser(list, user);
//                                            dialog.dismiss();
//                                        }else {
//                                            Log.e(BmobRealTimeData.TAG, "done: "+e.getMessage() );
//                                        }
//                                    }
//                                });
//                            }
//                        })
//                        .setCancelText("No")
//                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
//                dialog.setCanceledOnTouchOutside(true);
//            }
//        });
//
//
//
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = holder.getAdapterPosition();
//                Card card1 = cards.get(position);
//                Intent intent = new Intent(mContext, Fragment_share_message.class);
//                intent.putExtra(Fragment_share_message.image, card1.getPicture().getFileUrl());
//                intent.putExtra(Fragment_share_message.text, card1.getText());
//               getContext().startActivity(intent);
//            }
//        });
//    }
//
//    private void getUser(List<User> list, final User user) {
//        boolean have=false;
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getUsername().equals(user.getUsername())){
//                if (list.get(i).getObjectId().equals(User.getCurrentUser(User.class).getObjectId())){
//                    have = true;
//                    Toast.makeText(getContext(), "不能添加自己", Toast.LENGTH_SHORT).show();
//                }else{
//                    have=true;
//                    final User testUser=list.get(i);
//                    BmobQuery<Friend> query=new BmobQuery<>();
//                    query.findObjects(new FindListener<Friend>() {
//                        @Override
//                        public void done(List<Friend> list, BmobException e) {
//                            boolean isFriend=false;
//                            for (int j = 0; j < list.size(); j++) {
//                                if (list.get(j).getUser().getObjectId().equals(User.getCurrentUser().getObjectId())&&
//                                        list.get(j).getFriendUser().getObjectId().equals(testUser.getObjectId())){
//                                    isFriend=true;
//                                }
//                            }
//                            if (isFriend){
//                                Toast.makeText(getContext(), "你们已经是好友了", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Friend friend=new Friend();
//                                friend.setUser(User.getCurrentUser(User.class));
//                                friend.setFriendUser(testUser);
//                                friend.save(new SaveListener<String>() {
//                                    @Override
//                                    public void done(String s, BmobException e) {
//                                        if (e==null){
//                                            Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
//                                        }else {
//                                            Log.e(BmobRealTimeData.TAG, "done: "+e.getMessage() );
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//                }
//            }
//        }
//        if (!have){
//            Toast.makeText(getContext(), "不存在此账号", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return cards.size();
//    }
//
//
//    private void actionCartoon(View imageView){
//        ObjectAnimator button_dianzan0=ObjectAnimator.ofFloat(imageView,"scaleX",1f,1.7f,1f);
//        ObjectAnimator button_dianzan1=ObjectAnimator.ofFloat(imageView,"scaleY",1f,1.7f,1f);
//        ObjectAnimator button_dianzan2=ObjectAnimator.ofFloat(imageView,"rotation",0f,360f);
//        AnimatorSet animatorSet=new AnimatorSet();
//        animatorSet.setDuration(1000)
//                .play(button_dianzan0)
//                .with(button_dianzan1)
//                .with(button_dianzan2);
//        animatorSet.start();
//    }
//
//    class ShareHolder extends RecyclerView.ViewHolder {
//
//        private CircleImageView circleImageView;
//        private TextView textView_idName;
//        private TextView textView_time;
//        private TextView textView_context;
//        private ImageView imageView;
//        private TextView textView_dianzanshu;
//        private ImageView dianzan;
//        private ImageView shoucang;
//        private ImageView jiahaoyou;
//        private View cardView;
//
//        private ShareHolder(View view) {
//            super(view);
//            circleImageView = view.findViewById(R.id.image_share_item_circleView);
//            textView_idName = view.findViewById(R.id.text_share_item_idName);
//            textView_time = view.findViewById(R.id.text_share_item_time);
//            textView_context = view.findViewById(R.id.text_share_item_context);
//            imageView = view.findViewById(R.id.image_share_item_image);
//            textView_dianzanshu = view.findViewById(R.id.text_dianzanshu);
//
//            dianzan = view.findViewById(R.id.image_button_dianzan);
//            shoucang = view.findViewById(R.id.image_button_shoucang);
//            jiahaoyou = view.findViewById(R.id.image_button_jiahaoyou);
//            cardView = view;
//        }
//
//        private void bindView(Card card) {
//            textView_time.setText(card.getCreatedAt());
//            textView_context.setText(card.getText());
//            textView_dianzanshu.setText(String.valueOf(card.getDianzan().size()));
//            Customer customer = card.getCustomer();
//            BmobQuery<Customer> customerBmobQuery = new BmobQuery<>();
//            customerBmobQuery.getObject(customer.getObjectId(), new QueryListener<Customer>() {
//                @Override
//                public void done(Customer customer, BmobException e) {
//                    if (e == null) {
//                        textView_idName.setText(customer.getNickName());
//                        getHeadPhoto(customer);
//                    } else {
//                        Log.e(TAG, "done: " + e.getMessage());
//                    }
//                }
//
//                private void getHeadPhoto(Customer customer) {
//                    if (customer.getPicture_head() != null) {
//                        customer.getPicture_head().download(new DownloadFileListener() {
//                            @Override
//                            public void done(String s, BmobException e) {
//                                if (e == null) {
//                                    circleImageView.setImageBitmap(BitmapFactory.decodeFile(s));
//                                } else {
//                                    Log.e(TAG, "done: " + e.getMessage());
//                                }
//                            }
//
//                            @Override
//                            public void onProgress(Integer integer, long l) {
//                                Log.d("bmob", "onProgress: 下载进度：" + integer + "," + l);
//                            }
//                        });
//                    } else {
//                        circleImageView.setImageResource(R.drawable.test4);
//                    }
//                }
//            });
//
//            if (card.getPicture() != null) {
//                card.getPicture().download(new DownloadFileListener() {
//                    @Override
//                    public void done(String s, BmobException e) {
//                        if (e == null) {
////
//                            Bitmap bitmap = BitmapFactory.decodeFile(s);
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                imageView.setImageBitmap(bitmap);
//
//                            }
//                        } else {
//                            Log.e(TAG, "done: " + e.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onProgress(Integer integer, long l) {
//                        Log.d("bmob", "onProgress: 下载进度：" + integer + "," + l);
//                    }
//                });
//            }
//        }
//    }
//}
