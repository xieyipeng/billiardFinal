package com.example.a13834598889.billiards.FragmentShopKeeperNo2;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.a13834598889.billiards.JavaBean.Goods;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.newim.db.MigrationHelper.TAG;
import static com.example.a13834598889.billiards.FragmentShopKeeperNo1.Fragment_qiuzhuo_dianzhu.isInteger;

/**
 * Created by Yangyulin on 2018/8/6.
 */
public class Fragment_store_dianzhu extends Fragment {

    private ImageView addGoods;
    private LinearLayout noGoods;
    private RecyclerView snacksRecycleView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FragmentManager fragmentManager;
    private List<Goods> goodsList = new ArrayList<>();
    private SnacksAdapter1 snacksAdapter;

    //TODO: TAG shop_fragment_snacks
    public static Fragment_store_dianzhu newInstance() {
        return new Fragment_store_dianzhu();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_dianzhu, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initViews(view);
        loadingView();
        initClicks();
        return view;
    }


    //TODO: 点击事件
    private void initClicks() {
        //TODO: 跳转添加商品界面
        addGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .hide(fragmentManager.findFragmentByTag("shop_fragment_snacks"))
                        .add(R.id.fragment_container, Fragment_add_goods.newInstance(), "shop_add_snacks")
                        .commit();
            }
        });
        //TODO: 下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingView();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO: 初始化界面
    private void loadingView() {
        goodsList.clear();
        final BmobQuery<Goods> goodsBmobQuery = new BmobQuery<>();
        goodsBmobQuery.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getStoreID().equals(User.getCurrentUser().getObjectId())) {
                            goodsList.add(list.get(i));
                        }
                    }
                    if (goodsList.size() != 0) {
                        noGoods.setVisibility(View.INVISIBLE);
                        snacksRecycleView.setVisibility(View.VISIBLE);
                        loadingRecycleView();
                    } else {
                        noGoods.setVisibility(View.VISIBLE);
                        snacksRecycleView.setVisibility(View.GONE);
                    }
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    private void loadingRecycleView() {
        snacksAdapter = new SnacksAdapter1(R.layout.item_snacks,goodsList,getContext());
        snacksAdapter.openLoadAnimation();
        snacksAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        snacksAdapter.isFirstOnly(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        snacksRecycleView.setLayoutManager(layoutManager);
        snacksRecycleView.setAdapter(snacksAdapter);
    }

    //TODO: 初始化布局
    public void initViews(View view) {
        addGoods = view.findViewById(R.id.snacks_add_goods);
        noGoods = view.findViewById(R.id.snacks_no_goods);
        snacksRecycleView = view.findViewById(R.id.snacks_recycle_view);
        swipeRefreshLayout = view.findViewById(R.id.snacks_refresh);
    }


    class SnacksAdapter1 extends BaseQuickAdapter<Goods,BaseViewHolder> {

        private List<Goods> goodsList;
        private Context context;

        public SnacksAdapter1(int layoutResId, @Nullable List<Goods> data, Context context) {
            super(layoutResId, data);
            goodsList=data;
            this.context=context;
        }

        @Override
        protected void convert(final BaseViewHolder helper, final Goods good) {
            if (good.getObjectId() != null) {
                BmobQuery<Goods> goodsBmobQuery = new BmobQuery<>();
                goodsBmobQuery.findObjects(new FindListener<Goods>() {
                    @Override
                    public void done(List<Goods> list, BmobException e) {
                        if (e == null) {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getObjectId().equals(good.getObjectId())) {
                                    //TODO: 加载item布局
                                    //TODO: 零食图片
                                    if (list.get(i).getPicture() != null) {
                                        list.get(i).getPicture().download(new DownloadFileListener() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    Glide.with(context).load(new File(s)).crossFade().into((ImageView) helper.getView(R.id.snacks_item_photo));
                                                } else {
                                                    Log.e(TAG, "done: " + e.getMessage());
                                                }
                                            }

                                            @Override
                                            public void onProgress(Integer integer, long l) {

                                            }
                                        });
                                    }
                                    //TODO: 商品名称
                                    helper.setText(R.id.snacks_item_name,list.get(i).getGood_name());
                                    //TODO: 商品价格
                                    helper.setText(R.id.snacks_item_price, String.valueOf(list.get(i).getGood_price() + "元"));
                                    //TODO: 库存量
                                    helper.setText(R.id.snacks_edit_kucunliang,String.valueOf(list.get(i).getShop_num()));
                                }
                            }
                        } else {
                            Log.e(TAG, "done: " + e.getMessage());
                        }
                    }
                });
            } else {
                Log.e(TAG, "bindView: goods.objectID == null");
            }

            final EditText editText=helper.getView(R.id.snacks_edit_kucunliang);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b){
                        String newKucunliang=editText.getText().toString();
                        if (isInteger(newKucunliang)){
                            good.setShop_num(Integer.valueOf(newKucunliang));
                            good.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Log.e(TAG, "done: "+e.getMessage() );
                                    }
                                }
                            });
                        }else {
                            editText.setText(String.valueOf(good.getShop_num()));
                            Toast.makeText(getContext(), "请输入整数", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            final RelativeLayout relativeLayout=helper.getView(R.id.snacks_all);

            //TODO: 点击其他地方去除editText焦点
            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    relativeLayout.requestFocus();
                    return true;
                }
            });

            ImageView imageView=helper.getView(R.id.snacks_item_delete);


            //TODO: 删除按钮点击事件
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Goods deleteGood = new Goods();
                    deleteGood.setObjectId(good.getObjectId());
                    deleteGood.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                goodsList.remove(helper.getLayoutPosition());
                                notifyDataSetChanged();
                                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "done: " + e.getMessage());
                            }
                        }
                    });
                }
            });
        }
    }

//    class SnacksAdapter extends RecyclerView.Adapter<SnacksHolder> {
//
//        private List<Goods> goodsList;
//
//        public SnacksAdapter(List<Goods> goods) {
//            goodsList = goods;
//        }
//
//        @Override
//        public SnacksHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_snacks, parent, false);
//            return new SnacksHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final SnacksHolder holder, final int position) {
//            final Goods goods = goodsList.get(position);
//            holder.bindView(goods);
//
//            holder.editKucunliang.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View view, boolean b) {
//                    if (!b){
//                        String newKucunliang=holder.editKucunliang.getText().toString();
//                        if (isInteger(newKucunliang)){
//                            goods.setShop_num(Integer.valueOf(newKucunliang));
//                            goods.update(new UpdateListener() {
//                                @Override
//                                public void done(BmobException e) {
//                                    if (e==null){
//                                        Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
//                                    }else {
//                                        Log.e(TAG, "done: "+e.getMessage() );
//                                    }
//                                }
//                            });
//                        }else {
//                            holder.editKucunliang.setText(String.valueOf(goods.getShop_num()));
//                            Toast.makeText(getContext(), "请输入整数", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            });
//
//            //TODO: 点击其他地方去除editText焦点
//            holder.snacksAll.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    holder.snacksAll.requestFocus();
//                    return true;
//                }
//            });
//
//            //TODO: 删除按钮点击事件
//            holder.delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Goods deleteGood = new Goods();
//                    deleteGood.setObjectId(goods.getObjectId());
//                    deleteGood.delete(new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if (e == null) {
//                                goodsList.remove(position);
//                                notifyDataSetChanged();
//                                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Log.e(TAG, "done: " + e.getMessage());
//                            }
//                        }
//                    });
//                }
//            });
//            //TODO:
//        }
//
//        @Override
//        public int getItemCount() {
//            return goodsList.size();
//        }
//    }
//
//
//    class SnacksHolder extends RecyclerView.ViewHolder {
//
//        private ImageView delete;
//        private ImageView photo;
//        private TextView good_name;
//        private TextView good_price;
//        private EditText editKucunliang;
//        private RelativeLayout snacksAll;
//
//        public SnacksHolder(View view) {
//            super(view);
//            delete = view.findViewById(R.id.snacks_item_delete);
//            photo = view.findViewById(R.id.snacks_item_photo);
//            good_name = view.findViewById(R.id.snacks_item_name);
//            good_price = view.findViewById(R.id.snacks_item_price);
//            editKucunliang=view.findViewById(R.id.snacks_edit_kucunliang);
//            snacksAll=view.findViewById(R.id.snacks_all);
//        }
//
//        public void bindView(final Goods good) {
//            if (good.getObjectId() != null) {
//                BmobQuery<Goods> goodsBmobQuery = new BmobQuery<>();
//                goodsBmobQuery.findObjects(new FindListener<Goods>() {
//                    @Override
//                    public void done(List<Goods> list, BmobException e) {
//                        if (e == null) {
//                            for (int i = 0; i < list.size(); i++) {
//                                if (list.get(i).getObjectId().equals(good.getObjectId())) {
//                                    //TODO: 加载item布局
//                                    //TODO: 零食图片
//                                    if (list.get(i).getPicture() != null) {
//                                        list.get(i).getPicture().download(new DownloadFileListener() {
//                                            @Override
//                                            public void done(String s, BmobException e) {
//                                                if (e == null) {
//                                                    photo.setImageBitmap(BitmapFactory.decodeFile(s));
//                                                } else {
//                                                    Log.e(TAG, "done: " + e.getMessage());
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onProgress(Integer integer, long l) {
//
//                                            }
//                                        });
//                                    } else {
//                                        photo.setImageBitmap(null);
//                                    }
//                                    //TODO: 商品名称
//                                    good_name.setText("名称：" + list.get(i).getGood_name());
//                                    //TODO: 商品价格
//                                    good_price.setText("价格：" + String.valueOf(list.get(i).getGood_price() + "元"));
//                                    //TODO: 库存量
//                                    editKucunliang.setText(String.valueOf(list.get(i).getShop_num()));
//                                }
//                            }
//                        } else {
//                            Log.e(TAG, "done: " + e.getMessage());
//                        }
//                    }
//                });
//            } else {
//                Log.e(TAG, "bindView: goods.objectID == null");
//            }
//        }
//    }
}



