package com.hzdq.bajiesleepchildrenHD.user.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hzdq.bajiesleepchildrenHD.R;
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUIJava;
import com.hzdq.bajiesleepchildrenHD.utils.HideUI;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class CheckPhotoBitmapDialog extends Dialog {
    private PhotoView photoView;
    private Bitmap bitmap;
    public CheckPhotoBitmapDialog(@NonNull Context context) {
        super(context);
    }

    public CheckPhotoBitmapDialog(@NonNull Context context, int themeResId, Bitmap bitmap) {
        super(context, themeResId);
        this.bitmap = bitmap;

    }

    protected CheckPhotoBitmapDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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


        setContentView(R.layout.dialog_check_photo_uri);
        photoView = findViewById(R.id.check_photo_dialog_uri);
        if (bitmap != null){
            Log.d("TAG", "onCreate: ");
        }else {
            Log.d("TAG", "onCreate:null ");
        }

        photoView.setImageBitmap(bitmap);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                dismiss();
            }

            @Override
            public void onOutsidePhotoTap() {
                dismiss();
            }
        });
    }
}
