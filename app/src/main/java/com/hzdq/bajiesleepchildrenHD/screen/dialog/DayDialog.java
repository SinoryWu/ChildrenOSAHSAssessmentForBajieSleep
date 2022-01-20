package com.hzdq.bajiesleepchildrenHD.screen.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hzdq.bajiesleepchildrenHD.R;
import com.hzdq.bajiesleepchildrenHD.screen.adapter.MyWheelAdapter;
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUI;
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUIJava;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;

public class DayDialog extends Dialog implements View.OnClickListener {

    private String title,cancel,confirm;

    WheelView myWheelView;
    private TextView mTvTitle,mTvCancel,mTvConfirm;

    private int position1 = 0;
    private IOnCancelListener cancelListener;
    private IOnConfirmListener confirmListener;

    public DayDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public DayDialog setCancel(String cancel,IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener = listener;
        return this;
    }

    public DayDialog setConfirm(String confirm,IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }

    String question;

    public DayDialog(@NonNull Context context) {
        super(context);

    }

    public DayDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DayDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new HideDialogUIJava().hideUI(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_question);
        WindowManager m = getWindow().getWindowManager();
        Display d  = m.getDefaultDisplay();
        WindowManager.LayoutParams p  =getWindow().getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width = (int)(size.x*0.3);

        getWindow().setAttributes(p);


        mTvTitle = findViewById(R.id.day_tv_title);
        mTvCancel = findViewById(R.id.day_tv_cancel);
        mTvConfirm=findViewById(R.id.day_tv_confirm);

        if (!TextUtils.isEmpty(title)){
            mTvTitle.setText(title);
        }

        if (!TextUtils.isEmpty(cancel)){
            mTvCancel.setText(cancel);
        }

        if (!TextUtils.isEmpty(confirm)){
            mTvConfirm.setText(confirm);
        }
        
        myWheelView = findViewById(R.id.day_wheelview);


        myWheelView.setSkin(WheelView.Skin.Holo);
        myWheelView.setWheelData(createArrays());
//        myWheelView.setWheelSize(2);
        myWheelView.setWheelAdapter(new MyWheelAdapter(getContext()));

        myWheelView.setSelection(position1);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.backgroundColor = Color.WHITE;
        style.textColor = Color.GRAY;
        style.selectedTextColor = Color.BLACK;
        myWheelView.setStyle(style);

        myWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                question = String.valueOf(myWheelView.getSelectionItem());
                position1 = position;
                Log.d("myWheelView", question);
            }
        });


        mTvCancel.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
    }


    private ArrayList<String> createArrays() {
        ArrayList<String> list = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            list.add("item" + i);
//        }
        list.add("OSA-18");
        list.add("PSQ");
//        list.add("PSQ");



        return list;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.day_tv_cancel:
                if (cancelListener != null){
                    cancelListener.onCancel(this);
                }
                dismiss();
                break;

            case R.id.day_tv_confirm:
                if (confirmListener != null){
                    confirmListener.onConfirm(this, question);
                }
                dismiss();
                break;
        }
    }

    public interface IOnCancelListener{
        void onCancel(DayDialog dialog);
    }

    public interface IOnConfirmListener{
        void onConfirm(DayDialog dialog, String days);
    }


}
