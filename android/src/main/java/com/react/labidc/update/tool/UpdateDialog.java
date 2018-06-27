package com.react.labidc.update.tool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.react.labidc.update.R;

/**
 * 更新提醒框
 */
public class UpdateDialog extends Dialog implements View.OnClickListener {

    /**
     * 当前上下文
     */
    private Context context;

    /**
     * 布局id
     */
    private int layoutResID;

    /**
     * 要监听的控件id
     */
    private int[] listenedItems;

    /**
     * 按钮点击事件
     */
    private OnCenterItemClickListener listener;

    public UpdateDialog(Context context, int layoutResID, int[] listenedItems) {
        super(context, R.style.UpdateVsersionDialog);
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        // 此处可以设置dialog显示的位置为居中
        window.setGravity(Gravity.CENTER);
        // 添加动画效果
        window.setWindowAnimations(R.style.bottom_menu_animation);
        setContentView(layoutResID);
        // 宽度全屏
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // 设置dialog宽度为屏幕的4/5
        lp.width = display.getWidth()*4/5;

        getWindow().setAttributes(lp);
        // 点击Dialog外部消失
        setCanceledOnTouchOutside(true);

        /*for (int id : listenedItems) {
            findViewById(id).setOnClickListener(this);
        }*/

        findViewById(R.id.dialog_sure).setOnClickListener(this);
    }

    /**
     * 点击事件
     */
    public interface OnCenterItemClickListener {
        void OnCenterItemClick(UpdateDialog dialog, View view);
    }

    /**
     * 设置监听对象
     * @param listener
     */
    public void setOnCenterItemClickListener(OnCenterItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        dismiss();
        listener.OnCenterItemClick(this, view);
    }
}