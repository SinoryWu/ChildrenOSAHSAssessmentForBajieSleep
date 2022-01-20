package com.hzdq.bajiesleepchildrenHD.utils;

import android.app.Dialog;
import android.view.View;

public class HideDialogUIJava {

    public void hideUI(Dialog dialog){
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }


}
