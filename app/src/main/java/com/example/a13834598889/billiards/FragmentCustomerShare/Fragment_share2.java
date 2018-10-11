package com.example.a13834598889.billiards.FragmentCustomerShare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.billiards.JavaBean.Expert;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.JavaBean.Web;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/10/7.
 */

public class Fragment_share2 extends Fragment {

    private Banner banner;
    private List<String> urls = new ArrayList<>();
    private ExpertAdapter adapter;
    private RecyclerView recyclerView_share;
    private List<String> titles = new ArrayList<>();
    private List<Web> webs;
    private String image_path = "";
    private List<Expert> expertList = new ArrayList<>();

    public static Fragment_share2 newInstance() {
        return new Fragment_share2();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share2, container, false);
        loadHeadPhoto();
        preView(view);
        return view;
    }

    private void loadHeadPhoto() {
        if (User.getCurrentUser(User.class).getPicture_head() != null) {
            downloadFile_picture(User.getCurrentUser(User.class).getPicture_head());
        }
    }

    private void downloadFile_picture(BmobFile file) {
        file.download(new DownloadFileListener() {
            @Override
            public void done(final String s, BmobException e) {
                if (e == null) {
                    if (s != null) {
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        image_path = s;
                                    } catch (Exception c) {
                                        c.printStackTrace();
                                    }

                                }
                            });
                        } catch (Exception d) {
                            d.printStackTrace();
                        }
                    } else {
                    }
                }

            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
    }

    public class ExpertHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Expert expert0;

        private CardView cardView_layout_1;
        private TextView textView_time_1;
        private TextView textView_title_1;
        private TextView textView_text_1;
        private ImageView imageView_1_1;
        private ImageView imageView_1_2;
        private ImageView imageView_1_3;


        private CardView cardView_layout_2;
        private TextView textView_time_2;
        private TextView textView_title_2;
        private TextView textView_text_2;
        private ImageView imageView_2_1;


        private CardView cardView_layout_3;
        private TextView textView_time_3;
        private TextView textView_title_3;
        private TextView textView_text_3;
        private ImageView imageView_3_1;


        private CardView cardView_layout_4;
        private TextView textView_time_4;
        private TextView textView_title_4;
        private TextView textView_text_4;
        private ImageView imageView_4_1;

        private LinearLayout layout_caozuo;


        public ExpertHolder(View view) {
            super(view);
            cardView_layout_1 = (CardView) view.findViewById(R.id.expert_layout_1);
            textView_time_1 = (TextView) view.findViewById(R.id.expert_1_time);
            textView_title_1 = (TextView) view.findViewById(R.id.expert_1_title);
            textView_text_1 = (TextView) view.findViewById(R.id.expert_1_text);
            imageView_1_1 = (ImageView) view.findViewById(R.id.expert_1_image_1);
            imageView_1_2 = (ImageView) view.findViewById(R.id.expert_1_image_2);
            imageView_1_3 = (ImageView) view.findViewById(R.id.expert_1_image_3);


            cardView_layout_2 = (CardView) view.findViewById(R.id.expert_layout_2);
            textView_time_2 = (TextView) view.findViewById(R.id.expert_2_time);
            textView_title_2 = (TextView) view.findViewById(R.id.expert_2_title);
            textView_text_2 = (TextView) view.findViewById(R.id.expert_2_text);
            imageView_2_1 = (ImageView) view.findViewById(R.id.expert_2_image_1);


            cardView_layout_3 = (CardView) view.findViewById(R.id.expert_layout_3);
            textView_time_3 = (TextView) view.findViewById(R.id.expert_3_time);
            textView_title_3 = (TextView) view.findViewById(R.id.expert_3_title);
            textView_text_3 = (TextView) view.findViewById(R.id.expert_3_text);
            imageView_3_1 = (ImageView) view.findViewById(R.id.expert_3_image_1);


            cardView_layout_4 = (CardView) view.findViewById(R.id.expert_layout_4);
            textView_time_4 = (TextView) view.findViewById(R.id.expert_4_time);
            textView_title_4 = (TextView) view.findViewById(R.id.expert_4_title);
            textView_text_4 = (TextView) view.findViewById(R.id.expert_4_text);
            imageView_4_1 = (ImageView) view.findViewById(R.id.expert_4_image_1);

            layout_caozuo = (LinearLayout) view.findViewById(R.id.layout_dianji_expertRecycler);
            layout_caozuo.setOnClickListener(this);

        }

        public void bindView(Expert expert) {
            this.expert0 = expert;

            if (expert.getDaxiao().equals("大")) {
                //1大
                cardView_layout_4.setVisibility(View.VISIBLE);
                textView_time_4.setText(expert.getCreatedAt().toString());
                textView_title_4.setText(expert.getTitle().toString());
                textView_text_4.setText(expert.getText().toString());
                if (expert.getTu1() != null) {
                    downloadFile_picture(expert.getTu1(), imageView_4_1);
                }

            } else {

                if (expert.getZhangshu().equals("三")) {
                    //3小
                    cardView_layout_1.setVisibility(View.VISIBLE);
                    textView_time_1.setText(expert.getCreatedAt().toString());
                    textView_title_1.setText(expert.getTitle().toString());
                    textView_text_1.setText(expert.getText().toString());
                    if (expert.getTu1() != null) {
                        downloadFile_picture(expert.getTu1(), imageView_1_1);
                        downloadFile_picture(expert.getTu2(), imageView_1_2);
                        downloadFile_picture(expert.getTu3(), imageView_1_3);
                    }

                } else {
                    if (expert.getZuoyou().equals("左")) {
                        //1左
                        cardView_layout_2.setVisibility(View.VISIBLE);
                        textView_time_2.setText(expert.getCreatedAt().toString());
                        textView_title_2.setText(expert.getTitle().toString());
                        textView_text_2.setText(expert.getText().toString());
                        if (expert.getTu1() != null) {
                            downloadFile_picture(expert.getTu1(), imageView_2_1);
                        }

                    } else {
                        //1右
                        cardView_layout_3.setVisibility(View.VISIBLE);
                        textView_time_3.setText(expert.getCreatedAt().toString());
                        textView_title_3.setText(expert.getTitle().toString());
                        textView_text_3.setText(expert.getText().toString());
                        if (expert.getTu1() != null) {
                            downloadFile_picture(expert.getTu1(), imageView_3_1);
                        }
                    }
                }
            }


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_dianji_expertRecycler:
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .add(R.id.fragment_container, Fragment_web.newInstance(expert0, image_path), "web_fragment")
                            .commit();
                    break;
            }
        }

        private void downloadFile_picture(BmobFile file, final ImageView view) {
            file.download(new DownloadFileListener() {
                @Override
                public void done(final String s, BmobException e) {
                    if (e == null) {
                        if (s != null) {
                            try {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Glide.with(getActivity().getApplication()).load(s).into(view);
                                        } catch (Exception c) {
                                            c.printStackTrace();
                                        }

                                    }
                                });
                            } catch (Exception d) {
                                d.printStackTrace();
                            }
                        } else {
                        }
                    }

                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }
    }


    public class ExpertAdapter extends RecyclerView.Adapter<ExpertHolder> {

        private List<Expert> experts;

        public ExpertAdapter(List<Expert> experts) {
            this.experts = experts;
        }

        @Override
        public ExpertHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.expert_item, parent, false);
            return new ExpertHolder(view);
        }

        @Override
        public void onBindViewHolder(ExpertHolder holder, int position) {
            Expert expert = experts.get(position);
            holder.bindView(expert);
        }

        @Override
        public int getItemCount() {
            return experts.size();
        }
    }

    private void addURL() throws MalformedURLException {
        BmobQuery<Web> query = new BmobQuery<>();
        query.findObjects(new FindListener<Web>() {
            @Override
            public void done(List<Web> list, BmobException e) {
                try {
                    webs = list;
                    for (Web web : list) {
                        titles.add(web.getTitle());
                        urls.add(web.getPicture().getFileUrl());
                    }
                    banner.setBannerTitles(titles);
                    banner.setImages(urls);
                    banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
                    //设置图片加载器
                    banner.setImageLoader(new GlideImageLoader());
                    //设置图片集合
                    //设置banner动画效果
                    banner.setBannerAnimation(Transformer.DepthPage);
                    //设置标题集合（当banner样式有显示title时）
                    //设置自动轮播，默认为true
                    banner.isAutoPlay(true);
                    //设置轮播时间
                    banner.setDelayTime(2000);
                    //banner设置方法全部调用完毕时最后调用
                    banner.start();
                } catch (Exception e1) {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
        BmobQuery<Web> webBmobQuery=new BmobQuery<>();
        webBmobQuery.findObjects(new FindListener<Web>() {
            @Override
            public void done(List<Web> list, BmobException e) {
                if (e==null){
                    Log.e(TAG, "done: "+list.get(0).getUri() );
                }else {
                    Log.e(TAG, "done: "+e.getMessage() );
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    public void preView(View view) {
        recyclerView_share = view.findViewById(R.id.recycler_View_share_2);
        banner = view.findViewById(R.id.banner2);
        try {
            addURL();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addData();

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                switch (position) {
                    case 0:
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.fragment_container, Fragment_video.newInstance(webs.get(0), image_path), "video_fragment")
                                .commit();
                        break;
                    case 1:
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.fragment_container, Fragment_video.newInstance(webs.get(1), image_path), "video_fragment")
                                .commit();
                        break;
                    case 2:
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.fragment_container, Fragment_video.newInstance(webs.get(2), image_path), "video_fragment")
                                .commit();
                        break;
                    case 3:
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.fragment_container, Fragment_video.newInstance(webs.get(3), image_path), "video_fragment")
                                .commit();
                        break;
                    case 4:
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.fragment_container, Fragment_video.newInstance(webs.get(4), image_path), "video_fragment")
                                .commit();
                        break;
                }
            }
        });
    }

    private void addData() {
        expertList.clear();
        BmobQuery<Expert> query = new BmobQuery<>();
        query.findObjects(new FindListener<Expert>() {
            @Override
            public void done(List<Expert> list, BmobException e) {
                if (e == null) {
                    Log.e(TAG, "done: list size " + list.size());
                    expertList.addAll(list);
                    if (expertList.size() != 0) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView_share.setLayoutManager(layoutManager);
                        adapter = new ExpertAdapter(list);
                        recyclerView_share.setAdapter(adapter);
                    }
                } else {
                    Log.e(TAG, "done: here " + e.getMessage());
                }
            }
        });
    }
}
