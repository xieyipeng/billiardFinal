package com.example.a13834598889.billiards.Tool;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.a13834598889.billiards.R;

/**
 * Created by zy_style
 * 自定义AlertDialog
 */

public class MineDialog extends Dialog {

    private TextView choose;
    private TextView take;
    private onTakeOnclickListener noOnclickListener;
    private onChooseClickListener yesOnclickListener;

    public MineDialog(Context context) {
        super(context, R.style.SelfDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_dialog);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        initView();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onChooseClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onTakeClick();
                }
            }
        });
    }


    /**
     * 初始化界面控件
     */
    private void initView() {
        choose =  findViewById(R.id.mine_dialog_choose);
        take = findViewById(R.id.mine_dialog_take);
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onChooseClickListener {
        void onChooseClick();
    }

    public interface onTakeOnclickListener {
        void onTakeClick();
    }


    public void setTakeButton(onTakeOnclickListener onTakeOnclickListener) {
        this.noOnclickListener = onTakeOnclickListener;
    }

    public void setChooseButton(onChooseClickListener onChooseClickListener) {
        this.yesOnclickListener = onChooseClickListener;
    }
}

