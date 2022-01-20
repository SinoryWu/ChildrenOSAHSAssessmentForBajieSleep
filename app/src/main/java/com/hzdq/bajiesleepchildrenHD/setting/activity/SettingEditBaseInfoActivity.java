package com.hzdq.bajiesleepchildrenHD.setting.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.by_syk.lib.checkimgformat.CheckImgFormat;
import com.google.gson.Gson;
import com.hzdq.bajiesleepchildrenHD.R;
import com.hzdq.bajiesleepchildrenHD.TokenDialog;
import com.hzdq.bajiesleepchildrenHD.databinding.ActivitySettingEditBaseInfoBinding;
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassConfigSave;
import com.hzdq.bajiesleepchildrenHD.dataclass.UpLoadPicResponse;
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity;
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton;
import com.hzdq.bajiesleepchildrenHD.setting.viewmodel.SettingViewModel;
import com.hzdq.bajiesleepchildrenHD.user.adapter.UpDataReportAddPicAdapter;
import com.hzdq.bajiesleepchildrenHD.user.adapter.UpDataReportAddPicAdapter2;
import com.hzdq.bajiesleepchildrenHD.user.dialog.CheckPhotoBitmapDialog;
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2;
import com.hzdq.bajiesleepchildrenHD.utils.FileUtils;
import com.hzdq.bajiesleepchildrenHD.utils.HideUI;
import com.hzdq.bajiesleepchildrenHD.utils.Shp;
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.provider.DocumentsContract.isDocumentUri;

public class SettingEditBaseInfoActivity extends AppCompatActivity {
    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE_HOSPITAL = 0x00000012;
    private static final int PERMISSION_CAMERA_REQUEST_CODE_REPORT = 0x00000014;
    //申请访问相册权限的requestCode
    private static final int PERMISSION_ALBUM_REQUEST_CODE_HOSPITAL = 0x00000013; //成功调用相机后返回的requestCode
    private static final int PERMISSION_ALBUM_REQUEST_CODE_REPORT = 0x00000015; //成功调用相机后返回的requestCode

    private static final int CAMERA_REQUEST_CODE_HOSPITAL = 2;
    private static final int CAMERA_REQUEST_CODE_REPORT = 4;
    //成功打开相册后返回的requestCode
    private static final int CHOOSE_PHOTO_REQUEST_CODE_HOSPITAL = 3;
    private static final int CHOOSE_PHOTO_REQUEST_CODE_REPORT = 5;
    //用于保存拍照图片的uri
    private Uri mCameraUriHospital;
    private Uri mCameraUriReport;
    private PopupWindow popupWindowHospital;
    private PopupWindow popupWindowReport;
    private Animation animation;
    Bitmap bitmapChooseHospital, bitmapCameraHospital;
    Bitmap bitmapChooseReport, bitmapCameraReport;
    Uri picbaseUriHospital, chooseUriHospital;
    Uri picbaseUriReport, chooseUriReport;
    File photoFileHospital;
    File photoFileReport;
    String picidHospital;
    String picidReport;
    List<String> listPicStringHospital = new ArrayList<>();
    List<String> listPicStringReport = new ArrayList<>();
    private List<Bitmap> listpicHospital = new ArrayList<Bitmap>();
    private List<Bitmap> listpicReport = new ArrayList<Bitmap>();
    private RecyclerView mRcAddPicHospital;
    private RecyclerView mRcAddPicReport;
    UpDataReportAddPicAdapter upDataReportAddPicAdapterHospital;
    UpDataReportAddPicAdapter2 upDataReportAddPicAdapterReport;
    private SettingViewModel settingViewModel;
    private TokenDialog tokenDialog = null;
    private PopupWindow popupwindowEvaluate;
    private View customViewEvaluate;

    private PopupWindow popupwindowStandard;
    private View customViewStandard;
    private Shp shp;
  private ActivitySettingEditBaseInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HideUI(this).hideSystemUI();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_setting_edit_base_info);
        mRcAddPicHospital = findViewById(R.id.hospitalPicRecyclerView);
        mRcAddPicReport = findViewById(R.id.report_pic_RecyclerView);
        settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        shp = new Shp(this);
        ActivityCollector2.INSTANCE.addActivity(this);
        initRecyclePic();
        binding.hospitalPicAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPuPopWindow1(v);
            }
        });

        binding.reportPicAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPuPopWindow2(v);
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        Intent intent = getIntent();
        String hospitalName = intent.getStringExtra("hospitalName");
        String hospitalIcon = intent.getStringExtra("hospitalIcon");
        String welcome = intent.getStringExtra("welcome");
        String reportName = intent.getStringExtra("reportName");
        String reportIcon = intent.getStringExtra("reportIcon");
        String standard = intent.getStringExtra("standard");
        String standardContent = intent.getStringExtra("standardContent");
        String evaluate = intent.getStringExtra("evaluate");
        String evaluateContent = intent.getStringExtra("evaluateContent");










        upDataReportAddPicAdapterHospital.setOnItemClickItemListener(new UpDataReportAddPicAdapter.OnItemClickItemListener() {
            @Override
            public void onItemClickItem(int position) {

                upDataReportAddPicAdapterHospital.removeData(position);
                listPicStringHospital.remove(position);

            }
        });

        upDataReportAddPicAdapterReport.setOnItemClickItemListener(new UpDataReportAddPicAdapter2.OnItemClickItemListener() {
            @Override
            public void onItemClickItem(int position) {

                upDataReportAddPicAdapterReport.removeData(position);
                listPicStringReport.remove(position);
            }
        });

        if (hospitalName != null){
            if (!hospitalName.equals("")){
                binding.hospitalName.setText(hospitalName);

            }else {
                binding.hospitalName.setText("");

            }
        }

        if (welcome != null){
            if (!welcome.equals("")){
                binding.welcome.setText(welcome);

            }else  {
                binding.welcome.setText("");

            }
        }

        if (reportName != null){
            if (!reportName.equals("")){
                binding.reportName.setText(reportName);

            }else  {
                binding.reportName.setText("");

            }
        }

        if (standard != null){
//            if (standard.equals("1")){
//                binding.t1.setText("2007版中国指南");
//            }else if (standard.equals("2")){
//                binding.t1.setText("2020版中国指南/2014版美国指南");
//            }else if (standard.equals("")){
//                binding.t1.setText("");
//            }


            if (standard.equals("")){
                settingViewModel.getStandard().setValue("0");
            }else {
                settingViewModel.getStandard().setValue(standard);
            }

        }

        settingViewModel.getStandard().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("0")){
                    binding.t1.setText("");
                    binding.opinionContent.setText("");
                }else if (s.equals("1")){
                    binding.t1.setText("2007版中国指南");
                    binding.opinionContent.setText( "正常：OAHI < 5\n" +
                            "轻度：5 ≤ OAHI < 10\n" +
                            "中度：10 ≤ OAHI < 20\n" +
                            "重度：20 ≤ OAHI");
                }else if (s.equals("2")){
                    binding.t1.setText("2020版中国指南/2014版美国指南");
                    binding.opinionContent.setText( "正常：OAHI < 1\n" +
                            "轻度：1 ≤ OAHI < 5\n" +
                            "中度：5 ≤ OAHI < 10\n" +
                            "重度：10 ≤ OAHI");
                }
            }
        });

//        if (standardContent != null){
//            if (!standardContent.equals("")){
//                binding.opinionContent.setText(standardContent);
//            }else  {
//                binding.opinionContent.setText("");
//            }
//        }


        if (evaluate != null){
//            if (evaluate.equals("1")){
//                binding.t2.setText("智能评估");
//            }else if(evaluate.equals("2")){
//                binding.t2.setText("自由编辑");
//            }else if(evaluate.equals("3")){
//                binding.t2.setText("空白模板");
//            }else if(evaluate.equals("4")){
//                binding.t2.setText("空白模板");
//            }

            if (evaluate.equals("")){
                settingViewModel.getEvaluate().setValue("0");
            }else {
                settingViewModel.getEvaluate().setValue(evaluate);
            }


        }

        settingViewModel.getEvaluate().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("0")){
                    binding.t2.setText("");
                    binding.evaluateContent.setText("");
                }else if (s.equals("1")){
                    binding.t2.setText("智能评估");
                    binding.evaluateContent.setText("睡眠分期分析：已开启\n" +
                            "睡眠呼吸综述：已开启\n" +
                            "建议：已开启");
                }else if (s.equals("2")){
                    binding.t2.setText("自由编辑");
                    binding.evaluateContent.setText("");
                }else if (s.equals("3")){
                    binding.t2.setText("空白模板");
                    binding.evaluateContent.setText("");
                }
            }
        });


//        if (evaluateContent != null){
//            if (!evaluateContent.equals("")){
//                binding.evaluateContent.setText(evaluateContent);
//            }else  {
//                binding.evaluateContent.setText("");
//            }
//        }
        if (!hospitalIcon.equals("")){
            byte[] res = null;
            res =  intent.getByteArrayExtra("hospitalIconBitmap");
                upDataReportAddPicAdapterHospital.addData(0, getPicFromBytes(res,null));
                listPicStringHospital.add(hospitalIcon);
//            binding.imageView23.setImageBitmap(getPicFromBytes(res,null));

        }else  {
            Log.d("TAG", "onCreate: ");
        }

        if (!reportIcon.equals("")){
            byte[] res = null;
            res =  intent.getByteArrayExtra("reportIconBitmap");
                upDataReportAddPicAdapterReport.addData(0, getPicFromBytes(res,null));
                listPicStringReport.add(reportIcon);
//            binding.imageView23.setImageBitmap(getPicFromBytes(res,null));

        }else  {
            Log.d("TAG", "onCreate: ");
        }



        upDataReportAddPicAdapterHospital.setOnItemClickListener(new UpDataReportAddPicAdapter.OnClickItemListener() {
            @Override
            public void onItemClickItem(int position, Bitmap bitmap) {

                CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(SettingEditBaseInfoActivity.this,R.style.CustomDialogPhoto,bitmap);
                checkPhotoBitmapDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                checkPhotoBitmapDialog.show();
                if (checkPhotoBitmapDialog.isShowing()){
                    final Window window=getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(Color.parseColor("#000000"));
                    }
                }
                checkPhotoBitmapDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        final Window window=getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            window.setStatusBarColor(Color.parseColor("#ffffff"));
                        }

                    }
                });


                Window dialogWindow = checkPhotoBitmapDialog.getWindow();
                dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
                dialogWindow.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.y = 0;
                dialogWindow.setAttributes(lp);
            }
        });

        upDataReportAddPicAdapterReport.setOnItemClickListener(new UpDataReportAddPicAdapter2.OnClickItemListener1() {
            @Override
            public void onItemClickItem(int position, Bitmap bitmap) {

                CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(SettingEditBaseInfoActivity.this,R.style.CustomDialogPhoto,bitmap);
                checkPhotoBitmapDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                checkPhotoBitmapDialog.show();
                if (checkPhotoBitmapDialog.isShowing()){
                    final Window window=getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(Color.parseColor("#000000"));
                    }
                }
                checkPhotoBitmapDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        final Window window=getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            window.setStatusBarColor(Color.parseColor("#ffffff"));
                        }

                    }
                });


                Window dialogWindow = checkPhotoBitmapDialog.getWindow();
                dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
                dialogWindow.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.y = 0;
                dialogWindow.setAttributes(lp);
            }
        });


        binding.l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.i2.setImageResource(R.mipmap.pupop_up_icon);
                initPopupWindowViewEvaluate();
                popupwindowEvaluate.showAsDropDown(v, 0, dip2px(SettingEditBaseInfoActivity.this, 3));

            }
        });

        binding.l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.i1.setImageResource(R.mipmap.pupop_up_icon);
                initPopupWindowViewStandard();
                popupwindowStandard.showAsDropDown(v, 0, dip2px(SettingEditBaseInfoActivity.this, 3));
            }
        });


        binding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hospital_logo = "";
                String report_logo = "";

                if (listPicStringHospital.size()>0){
                    hospital_logo = listPicStringHospital.toString().replace("[","").replace("]","");

                }
                if (listPicStringReport.size()>0){
                    report_logo = listPicStringReport.toString().replace("[","").replace("]","");
                }

                if (binding.hospitalName.getText().toString().trim().equals("")){
                    Toast.makeText(SettingEditBaseInfoActivity.this,"机构名称不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (binding.reportName.getText().toString().trim().equals("")){
                    Toast.makeText(SettingEditBaseInfoActivity.this,"睡眠报告抬头不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                shp.saveToSp("welcome",binding.welcome.getText().toString().trim());
                HashMap<String, String> map = new HashMap<>();
                map.put("category", "app_report");
                map.put("hospitalid", String.valueOf(shp.getHospitalId()));
                map.put("hospital_name", binding.hospitalName.getText().toString().trim());
                map.put("hospital_logo", hospital_logo);
                map.put("report_name", binding.reportName.getText().toString().trim());
                map.put("report_logo", report_logo);
                map.put("report_standard", settingViewModel.getStandard().getValue());
                map.put("report_evaluate", settingViewModel.getEvaluate().getValue());



//                String url = OkhttpSingleton.INSTANCE.getBASE_URL()+"/v2/config/save";
                String url = OkhttpSingleton.INSTANCE.getBASE_URL()+"/v2/config/save";


               postConfigSave(url,map);
            }
        });


    }

    @Override
    protected void onDestroy() {
        ActivityCollector2.INSTANCE.removeActivity(this);
        super.onDestroy();
    }

    /**
     * 修改配置
     */
    private void postConfigSave(String url, HashMap<String, String> map) {

        HashMap<String, Object> params = new HashMap<String, Object>();


        Set<String> keys = map.keySet();
        for (String key : keys) {
            params.put(key, map.get(key));

        }

        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody

        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBodyJson)
                .addHeader("token", shp.getToken())
                .addHeader("uid", shp.getUid())
                .addHeader("user_agent",shp.getUserAgent())
                .build();
        //3.将request封装为call
        Call call =  OkhttpSingleton.INSTANCE.ok().newCall(request);

        //4.执行call
//        同步执行
//        Response response = call.execute();

        //异步执行
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this, "修改配置网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String res = response.body().string();



                if (res == null){
                    Toast.makeText(SettingEditBaseInfoActivity.this,"操作失败请稍后尝试",Toast.LENGTH_SHORT).show();
                }else {
                    Gson gson = new Gson();
                    final DataClassConfigSave dataClassConfigSave =  gson.fromJson(res, DataClassConfigSave.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            if (dataClassConfigSave.getCode() == 0) {

//                            mTvProgressListLoadComplete.setVisibility(View.VISIBLE);
                                ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this,"操作成功");
                                setResult(RESULT_OK);
                                finish();

                            }else if (dataClassConfigSave.getCode() == 10010 || dataClassConfigSave.getCode() == 10004) {
                                if (tokenDialog == null) {
                                    tokenDialog = new TokenDialog(SettingEditBaseInfoActivity.this, new TokenDialog.ConfirmAction() {
                                        @Override
                                        public void onRightClick() {
                                            shp.saveToSp("token", "");
                                            shp.saveToSp("uid", "");
                                            startActivity(new Intent(SettingEditBaseInfoActivity.this, LoginActivity.class));
                                            ActivityCollector2.INSTANCE.finishAll();

                                        }
                                    });
                                    tokenDialog.show();
                                    tokenDialog.setCanceledOnTouchOutside(false);
                                } else {
                                    tokenDialog.show();
                                    tokenDialog.setCanceledOnTouchOutside(false);
                                }
                            }
                            else {
                                String msg = dataClassConfigSave.getMsg();
                                ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this, msg);
                            }
                        }
                    });
                }
            }
        });

    }

    public void initPopupWindowViewStandard() {
        // // 获取自定义布局文件pop.xml的视图
        customViewStandard = getLayoutInflater().inflate(R.layout.layout_sleep_standard,
                null, false);

        // 创建PopupWindow实例,280,160分别是宽度和高度
            popupwindowStandard = new PopupWindow(customViewStandard, dip2px(this, 356f), dip2px(this, 82f));
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        uiOptions |= 0x00001000;
        customViewStandard.setSystemUiVisibility(uiOptions);

        TextView t1 = customViewStandard.findViewById(R.id.pop_item_sleep_standard_1);
        TextView t2 = customViewStandard.findViewById(R.id.pop_item_sleep_standard_2);




        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupwindowStandard != null && popupwindowStandard.isShowing()) {
                    popupwindowStandard.dismiss();
                    popupwindowStandard = null;

                }
                settingViewModel.getStandard().setValue("1");
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupwindowStandard != null && popupwindowStandard.isShowing()) {
                    popupwindowStandard.dismiss();
                    popupwindowStandard = null;

                }
                settingViewModel.getStandard().setValue("2");
            }
        });



        popupwindowStandard.setOutsideTouchable(true);
        popupwindowStandard.setFocusable(true);
        customViewStandard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (popupwindowStandard != null && popupwindowStandard.isShowing()) {
                    popupwindowStandard.dismiss();
                    popupwindowStandard = null;


                }


                return true;
            }
        });

        popupwindowStandard.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupwindowStandard = null;
                Log.d("TAG", "onDismiss: ");
                binding.i1.setImageResource(R.mipmap.pupop_down_icon);
            }
        });

    }


    public void initPopupWindowViewEvaluate() {
        // // 获取自定义布局文件pop.xml的视图
        customViewEvaluate = getLayoutInflater().inflate(R.layout.layout_sleep_evaluate,
                null, false);

                // 创建PopupWindow实例,280,160分别是宽度和高度
        popupwindowEvaluate = new PopupWindow(customViewEvaluate, dip2px(this, 356f), dip2px(this, 124f));
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        uiOptions |= 0x00001000;
        customViewEvaluate.setSystemUiVisibility(uiOptions);

        TextView t1 = customViewEvaluate.findViewById(R.id.pop_item_sleep_evaluate_1);
        TextView t2 = customViewEvaluate.findViewById(R.id.pop_item_sleep_evaluate_2);
        TextView t3 = customViewEvaluate.findViewById(R.id.pop_item_sleep_evaluate_3);



        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupwindowEvaluate != null && popupwindowEvaluate.isShowing()) {
                    popupwindowEvaluate.dismiss();
                    popupwindowEvaluate = null;

                }
                settingViewModel.getEvaluate().setValue("1");
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupwindowEvaluate != null && popupwindowEvaluate.isShowing()) {
                    popupwindowEvaluate.dismiss();
                    popupwindowEvaluate = null;

                }
                settingViewModel.getEvaluate().setValue("2");
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupwindowEvaluate != null && popupwindowEvaluate.isShowing()) {
                    popupwindowEvaluate.dismiss();
                    popupwindowEvaluate = null;

                }
                settingViewModel.getEvaluate().setValue("3");
            }
        });


        popupwindowEvaluate.setOutsideTouchable(true);
        popupwindowEvaluate.setFocusable(true);
        customViewEvaluate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (popupwindowEvaluate != null && popupwindowEvaluate.isShowing()) {
                    popupwindowEvaluate.dismiss();
                    popupwindowEvaluate = null;


                }


                return true;
            }
        });

        popupwindowEvaluate.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupwindowEvaluate = null;
                Log.d("TAG", "onDismiss: ");
                binding.i2.setImageResource(R.mipmap.pupop_down_icon);
            }
        });

    }

    private void initRecyclePic() {
        // 纵向滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        mRcAddPicHospital.setLayoutManager(linearLayoutManager);
//   获取数据，向适配器传数据，绑定适配器
//        list = initData();
        upDataReportAddPicAdapterHospital = new UpDataReportAddPicAdapter(SettingEditBaseInfoActivity.this, listpicHospital);
        mRcAddPicHospital.setAdapter(upDataReportAddPicAdapterHospital);
//   添加动画
        mRcAddPicHospital.setItemAnimator(new DefaultItemAnimator());

        mRcAddPicReport.setLayoutManager(linearLayoutManager2);
//   获取数据，向适配器传数据，绑定适配器
//        list = initData();
        upDataReportAddPicAdapterReport = new UpDataReportAddPicAdapter2(SettingEditBaseInfoActivity.this, listpicReport);
        mRcAddPicReport.setAdapter(upDataReportAddPicAdapterReport);
//   添加动画
        mRcAddPicReport.setItemAnimator(new DefaultItemAnimator());
    }

    //下面的这个方法是将byte数组转化为Bitmap对象的一个方法
    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {

        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,  opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;

    }


    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public void hideSoftKeyboard(View view){
        //这里获取view为参数 之前试过用context,LoginActivity.this会造成闪退
        //view不会闪退
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }




    /**
     *
     * @param popupView
     */
    public void showPuPopWindow1(View popupView){
        if (popupWindowHospital == null) {
            popupView = View.inflate(this, R.layout.popview_item, null);
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    //布局位于状态栏下方
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    //全屏
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    //隐藏导航栏
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            uiOptions |= 0x00001000;
            popupView.setSystemUiVisibility(uiOptions);

            // 参数2,3：指明popupwindow的宽度和高度
            popupWindowHospital = new PopupWindow(popupView, dip2px(this,427f),
                    dip2px(this,170f));

            // 设置背景图片， 必须设置，不然动画没作用
//            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindowHospital.setFocusable(true);
            popupWindowHospital.setOutsideTouchable(true);
            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(250);

            // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
            popupWindowHospital.showAtLocation(SettingEditBaseInfoActivity.this.findViewById(R.id.setting_edit_button_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            popupView.startAnimation(animation);

            popupWindowHospital.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindowHospital = null;
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_camera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPermissionAndCameraHospital();
                    popupWindowHospital.dismiss();
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPermissionAndAlbumHospital();
                    popupWindowHospital.dismiss();
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowHospital.dismiss();
                }
            });

        }
    }

    /**
     *
     * @param popupView
     */
    public void showPuPopWindow2(View popupView){
        if (popupWindowReport == null) {
            popupView = View.inflate(this, R.layout.popview_item, null);
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    //布局位于状态栏下方
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    //全屏
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    //隐藏导航栏
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            uiOptions |= 0x00001000;
            popupView.setSystemUiVisibility(uiOptions);

            // 参数2,3：指明popupwindow的宽度和高度
            popupWindowReport = new PopupWindow(popupView, dip2px(this,427f),
                    dip2px(this,170f));

            // 设置背景图片， 必须设置，不然动画没作用
//            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindowReport.setFocusable(true);
            popupWindowReport.setOutsideTouchable(true);
            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(250);

            // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
            popupWindowReport.showAtLocation(SettingEditBaseInfoActivity.this.findViewById(R.id.setting_edit_button_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            popupView.startAnimation(animation);

            popupWindowReport.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindowReport = null;
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_camera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPermissionAndCameraReport();
                    popupWindowReport.dismiss();
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPermissionAndAlbumReport();
                    popupWindowReport.dismiss();
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowReport.dismiss();
                }
            });

        }
    }

    /**
     * post请求上传照片
     * 相机
     * @param url
     * @param file
     */
    public void postUpLoadPhotoCameraHospital1(String url, final File file) {
        //创建OK
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //请求体


        // MediaType.parse() 里面是上传的文件类型。
//        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
//        String filename = file.getName();
//        // 参数分别为， 请求key ，文件名称 ， RequestBody
//        requestBody.addFormDataPart("file", filename, body);
//        //构建请求
//        Request request = new Request.Builder().url(url).post(requestBody.build()).build();


        //请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //post请求Key,value
                .addFormDataPart("file", file.getName(), requestBody)     //post请求Key,value
                .build();
        //构建请求
        Request request = new Request.Builder()
//                .url("http://yun918.cn/study/public/index.php/file_upload.php")
                .url(url)
                .post(body)
                .build();
        //call对象
        Call call = OkhttpSingleton.INSTANCE.ok().newCall(request);
        //call执行请求
        call.enqueue(new Callback() {   //异步
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this, "网络请求失败");
                        Log.d("json", "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String json = response.body().string();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (json.equals("") || json == null){
                            ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this,"上传失败，请重新拍摄");

                        }else {
                            UpLoadPicResponse upLoadPicResponse = new Gson().fromJson(json,UpLoadPicResponse.class);
                            if (upLoadPicResponse.getCode() == 1){
                                upDataReportAddPicAdapterHospital.addData(listpicHospital.size(), bitmapCameraHospital);
                                listPicStringHospital.add("\""+upLoadPicResponse.getData()+"\"");

//                                upDataReportAddPicAdapter.setOnItemClickListener(new UpDataReportAddPicAdapter.OnClickItemListener() {
//                                    @Override
//                                    public void onItemClickItem(int position, Bitmap bitmap) {
//                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(SettingEditBaseInfoActivity.this,R.style.CustomDialogPhoto,bitmap);
//                                        checkPhotoBitmapDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//                                        checkPhotoBitmapDialog.show();
//                                        if (checkPhotoBitmapDialog.isShowing()){
//                                            final Window window=getWindow();
//                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                window.setStatusBarColor(Color.parseColor("#000000"));
//                                            }
//                                        }
//                                        checkPhotoBitmapDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                            @Override
//                                            public void onDismiss(DialogInterface dialogInterface) {
//
//                                                final Window window=getWindow();
//                                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    window.setStatusBarColor(Color.parseColor("#ffffff"));
//                                                }
//
//                                            }
//                                        });
//
//
//                                        Window dialogWindow = checkPhotoBitmapDialog.getWindow();
//                                        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
//                                        dialogWindow.setGravity(Gravity.BOTTOM);
//                                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//                                        lp.y = 0;
//                                        dialogWindow.setAttributes(lp);
//                                    }
//                                });
                            }else if (upLoadPicResponse.getCode() == 10010 || upLoadPicResponse.getCode() == 10004) {
                                if (tokenDialog == null) {
                                    tokenDialog = new TokenDialog(SettingEditBaseInfoActivity.this, new TokenDialog.ConfirmAction() {
                                        @Override
                                        public void onRightClick() {
                                            shp.saveToSp("token", "");
                                            shp.saveToSp("uid", "");
                                            startActivity(new Intent(SettingEditBaseInfoActivity.this, LoginActivity.class));
                                            ActivityCollector2.INSTANCE.finishAll();

                                        }
                                    });
                                    tokenDialog.show();
                                    tokenDialog.setCanceledOnTouchOutside(false);
                                } else {
                                    tokenDialog.show();
                                    tokenDialog.setCanceledOnTouchOutside(false);
                                }
                            }else {
                                ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this, upLoadPicResponse.getMessage());
                            }

                        }






                    }

                });


            }
        });
    }


    /**
     * post请求上传照片
     * 相机
     * @param url
     * @param file
     */
    public void postUpLoadPhotoCameraHospital2(String url, final File file) {
        //创建OK
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //请求体


        // MediaType.parse() 里面是上传的文件类型。
//        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
//        String filename = file.getName();
//        // 参数分别为， 请求key ，文件名称 ， RequestBody
//        requestBody.addFormDataPart("file", filename, body);
//        //构建请求
//        Request request = new Request.Builder().url(url).post(requestBody.build()).build();


        //请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //post请求Key,value
                .addFormDataPart("file", file.getName(), requestBody)     //post请求Key,value
                .build();
        //构建请求
        Request request = new Request.Builder()
//                .url("http://yun918.cn/study/public/index.php/file_upload.php")
                .url(url)
                .post(body)
                .build();
        //call对象
        Call call = OkhttpSingleton.INSTANCE.ok().newCall(request);
        //call执行请求
        call.enqueue(new Callback() {   //异步
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this, "网络请求失败");
                        Log.d("json", "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String json = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (json.equals("") || json == null){
                            ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this,"上传失败，请重新拍摄");

                        }else {
                            UpLoadPicResponse upLoadPicResponse = new Gson().fromJson(json,UpLoadPicResponse.class);
                            if (upLoadPicResponse.getCode() == 1){
                                upDataReportAddPicAdapterHospital.addData(listpicHospital.size(), bitmapChooseHospital);

                                listPicStringHospital.add(upLoadPicResponse.getData());

//                                upDataReportAddPicAdapterHospital.setOnItemClickListener(new UpDataReportAddPicAdapter.OnClickItemListener() {
//                                    @Override
//                                    public void onItemClickItem(int position, Bitmap bitmap) {
//                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(SettingEditBaseInfoActivity.this,R.style.CustomDialogPhoto,bitmap);
//                                        checkPhotoBitmapDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//                                        checkPhotoBitmapDialog.show();
//                                        if (checkPhotoBitmapDialog.isShowing()){
//                                            final Window window=getWindow();
//                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                window.setStatusBarColor(Color.parseColor("#000000"));
//                                            }
//                                        }
//                                        checkPhotoBitmapDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                            @Override
//                                            public void onDismiss(DialogInterface dialogInterface) {
//
//                                                final Window window=getWindow();
//                                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    window.setStatusBarColor(Color.parseColor("#ffffff"));
//                                                }
//
//                                            }
//                                        });
//
//
//                                        Window dialogWindow = checkPhotoBitmapDialog.getWindow();
//                                        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
//                                        dialogWindow.setGravity(Gravity.BOTTOM);
//                                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//                                        lp.y = 0;
//                                        dialogWindow.setAttributes(lp);
//                                    }
//                                });
                            }else if (upLoadPicResponse.getCode() == 10010 || upLoadPicResponse.getCode() == 10004) {
                                if (tokenDialog == null) {
                                    tokenDialog = new TokenDialog(SettingEditBaseInfoActivity.this, new TokenDialog.ConfirmAction() {
                                        @Override
                                        public void onRightClick() {
                                            shp.saveToSp("token", "");
                                            shp.saveToSp("uid", "");
                                            startActivity(new Intent(SettingEditBaseInfoActivity.this, LoginActivity.class));
                                            ActivityCollector2.INSTANCE.finishAll();

                                        }
                                    });
                                    tokenDialog.show();
                                    tokenDialog.setCanceledOnTouchOutside(false);
                                } else {
                                    tokenDialog.show();
                                    tokenDialog.setCanceledOnTouchOutside(false);
                                }
                            }else {
                                ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this, upLoadPicResponse.getMessage());
                            }

                        }






                    }

                });


            }
        });
    }

    /**
     * post请求上传照片
     * 相机
     * @param url
     * @param file
     */
    public void postUpLoadPhotoCameraReport1(String url, final File file) {
        //创建OK
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //请求体


        // MediaType.parse() 里面是上传的文件类型。
//        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
//        String filename = file.getName();
//        // 参数分别为， 请求key ，文件名称 ， RequestBody
//        requestBody.addFormDataPart("file", filename, body);
//        //构建请求
//        Request request = new Request.Builder().url(url).post(requestBody.build()).build();


        //请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //post请求Key,value
                .addFormDataPart("file", file.getName(), requestBody)     //post请求Key,value
                .build();
        //构建请求
        Request request = new Request.Builder()
//                .url("http://yun918.cn/study/public/index.php/file_upload.php")
                .url(url)
                .post(body)
                .build();
        //call对象
        Call call = OkhttpSingleton.INSTANCE.ok().newCall(request);
        //call执行请求
        call.enqueue(new Callback() {   //异步
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this, "网络请求失败");
                        Log.d("json", "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String json = response.body().string();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (json.equals("") || json == null){
                            ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this,"上传失败，请重新拍摄");

                        }else {
                            UpLoadPicResponse upLoadPicResponse = new Gson().fromJson(json,UpLoadPicResponse.class);
                            if (upLoadPicResponse.getCode() == 1){
                                upDataReportAddPicAdapterReport.addData(listpicReport.size(), bitmapCameraReport);
                                listPicStringReport.add("\""+upLoadPicResponse.getData()+"\"");

//                                upDataReportAddPicAdapter.setOnItemClickListener(new UpDataReportAddPicAdapter.OnClickItemListener() {
//                                    @Override
//                                    public void onItemClickItem(int position, Bitmap bitmap) {
//                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(SettingEditBaseInfoActivity.this,R.style.CustomDialogPhoto,bitmap);
//                                        checkPhotoBitmapDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//                                        checkPhotoBitmapDialog.show();
//                                        if (checkPhotoBitmapDialog.isShowing()){
//                                            final Window window=getWindow();
//                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                window.setStatusBarColor(Color.parseColor("#000000"));
//                                            }
//                                        }
//                                        checkPhotoBitmapDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                            @Override
//                                            public void onDismiss(DialogInterface dialogInterface) {
//
//                                                final Window window=getWindow();
//                                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    window.setStatusBarColor(Color.parseColor("#ffffff"));
//                                                }
//
//                                            }
//                                        });
//
//
//                                        Window dialogWindow = checkPhotoBitmapDialog.getWindow();
//                                        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
//                                        dialogWindow.setGravity(Gravity.BOTTOM);
//                                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//                                        lp.y = 0;
//                                        dialogWindow.setAttributes(lp);
//                                    }
//                                });
                            }else if (upLoadPicResponse.getCode() == 10010 || upLoadPicResponse.getCode() == 10004) {
                                if (tokenDialog == null) {
                                    tokenDialog = new TokenDialog(SettingEditBaseInfoActivity.this, new TokenDialog.ConfirmAction() {
                                        @Override
                                        public void onRightClick() {
                                            shp.saveToSp("token", "");
                                            shp.saveToSp("uid", "");
                                            startActivity(new Intent(SettingEditBaseInfoActivity.this, LoginActivity.class));
                                            ActivityCollector2.INSTANCE.finishAll();

                                        }
                                    });
                                    tokenDialog.show();
                                    tokenDialog.setCanceledOnTouchOutside(false);
                                } else {
                                    tokenDialog.show();
                                    tokenDialog.setCanceledOnTouchOutside(false);
                                }
                            }else {
                                ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this, upLoadPicResponse.getMessage());
                            }

                        }






                    }

                });


            }
        });
    }


    /**
     * post请求上传照片
     * 相机
     * @param url
     * @param file
     */
    public void postUpLoadPhotoCameraReport2(String url, final File file) {
        //创建OK
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //请求体


        // MediaType.parse() 里面是上传的文件类型。
//        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
//        String filename = file.getName();
//        // 参数分别为， 请求key ，文件名称 ， RequestBody
//        requestBody.addFormDataPart("file", filename, body);
//        //构建请求
//        Request request = new Request.Builder().url(url).post(requestBody.build()).build();


        //请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //post请求Key,value
                .addFormDataPart("file", file.getName(), requestBody)     //post请求Key,value
                .build();
        //构建请求
        Request request = new Request.Builder()
//                .url("http://yun918.cn/study/public/index.php/file_upload.php")
                .url(url)
                .post(body)
                .build();
        //call对象
        Call call = OkhttpSingleton.INSTANCE.ok().newCall(request);
        //call执行请求
        call.enqueue(new Callback() {   //异步
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this, "网络请求失败");
                        Log.d("json", "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String json = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (json.equals("") || json == null){
                            ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this,"上传失败，请重新拍摄");

                        }else {
                            UpLoadPicResponse upLoadPicResponse = new Gson().fromJson(json,UpLoadPicResponse.class);
                            if (upLoadPicResponse.getCode() == 1){

                                upDataReportAddPicAdapterReport.addData(listpicReport.size(), bitmapChooseReport);

                                listPicStringReport.add(upLoadPicResponse.getData());


                            }else {
                                ToastUtils.INSTANCE.showTextToast(SettingEditBaseInfoActivity.this, upLoadPicResponse.getMessage());
                            }

                        }






                    }

                });


            }
        });
    }
    /**
     * 处理权限申请的回调。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode){

            case PERMISSION_CAMERA_REQUEST_CODE_HOSPITAL:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，调起相机拍照。
                    openCameraHospital();
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this,"拍照权限被拒绝", Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_ALBUM_REQUEST_CODE_HOSPITAL:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，打开相册。
                    openAlbumHospital();
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this,"访问相册权限被拒绝",Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_CAMERA_REQUEST_CODE_REPORT:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，调起相机拍照。
                    openCameraReport();
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this,"拍照权限被拒绝", Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_ALBUM_REQUEST_CODE_REPORT:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，打开相册。
                    openAlbumReport();
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this,"访问相册权限被拒绝",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case CAMERA_REQUEST_CODE_HOSPITAL:
                if (resultCode == RESULT_OK){
//                    upDataProgressAddPicAdapter.addData(listpic.size(), String.valueOf(mCameraUri));
////
//
//                    if (listpic.size() > 3){
//                        mRlAddPic.setVisibility(View.GONE);
//                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        bitmapCameraHospital = compressImage1(getPath(this, mCameraUriHospital));
//                        if (isAndroidQ){
//
//                        }else {
//                            bitmapCamera = compressImage1(mCameraImagePath);;
//                        }

                    }
                    String format = CheckImgFormat.get(SettingEditBaseInfoActivity.this,mCameraUriHospital);
                    if (format.equals(".jpg") || format.equals(".png") || format.equals(".bpm")){
                        File file = createFilePic(SettingEditBaseInfoActivity.this, bitmapCameraHospital);

                        postUpLoadPhotoCameraHospital1("http://copd.daoqihz.com/api/image/ossUpload",file);
                    }else {
                        Toast.makeText(SettingEditBaseInfoActivity.this,"只能上传jpg/png/bpm格式！",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(this,"取消拍摄",Toast.LENGTH_LONG).show();
                }
                break;

            case CHOOSE_PHOTO_REQUEST_CODE_HOSPITAL:
                if (resultCode == RESULT_OK){

                    //判断手机版本号
                    if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT){
                        //Android 4.4及以上手机用这个方法
                        handleImageOnKitKatHospital(data);
                        bitmapChooseHospital = compressImage1(getPath(this, chooseUriHospital));
//                        try {
//                            bitmap = getBitmapFormUri(UpDateProgressActivity.this,Uri.withAppendedPath(picbaseUri, picid));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        String format = CheckImgFormat.get(SettingEditBaseInfoActivity.this,chooseUriHospital);
                        if (format.equals(".jpg") || format.equals(".png") || format.equals(".bpm")){
                            File file = createFilePic(SettingEditBaseInfoActivity.this, bitmapChooseHospital);
                            postUpLoadPhotoCameraHospital2("http://copd.daoqihz.com/api/image/ossUpload",file);
                        }else {
                            Toast.makeText(SettingEditBaseInfoActivity.this,"只能上传jpg/png/bpm格式！",Toast.LENGTH_SHORT).show();
                        }


                    }
                }
                break;
            case CAMERA_REQUEST_CODE_REPORT:
                if (resultCode == RESULT_OK){
//                    upDataProgressAddPicAdapter.addData(listpic.size(), String.valueOf(mCameraUri));
////
//
//                    if (listpic.size() > 3){
//                        mRlAddPic.setVisibility(View.GONE);
//                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        bitmapCameraReport = compressImage1(getPath(this, mCameraUriReport));
//                        if (isAndroidQ){
//
//                        }else {
//                            bitmapCamera = compressImage1(mCameraImagePath);;
//                        }

                    }

                    String format = CheckImgFormat.get(SettingEditBaseInfoActivity.this,mCameraUriReport);
                    if (format.equals(".jpg") || format.equals(".png") || format.equals(".bpm")){
                        File file = createFilePic(SettingEditBaseInfoActivity.this, bitmapCameraReport);
                        postUpLoadPhotoCameraReport1("http://copd.daoqihz.com/api/image/ossUpload",file);
                    }else {
                        Toast.makeText(SettingEditBaseInfoActivity.this,"只能上传jpg/png/bpm格式！",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(this,"取消拍摄",Toast.LENGTH_LONG).show();
                }
                break;

            case CHOOSE_PHOTO_REQUEST_CODE_REPORT:
                if (resultCode == RESULT_OK){

                    //判断手机版本号
                    if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT){
                        //Android 4.4及以上手机用这个方法
                        handleImageOnKitKatReport(data);
                        bitmapChooseReport = compressImage1(getPath(this, chooseUriReport));
//                        try {
//                            bitmap = getBitmapFormUri(UpDateProgressActivity.this,Uri.withAppendedPath(picbaseUri, picid));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        String format = CheckImgFormat.get(SettingEditBaseInfoActivity.this,chooseUriReport);
                        if (format.equals(".jpg") || format.equals(".png") || format.equals(".bpm")){
                            File file = createFilePic(SettingEditBaseInfoActivity.this, bitmapChooseReport);
                            postUpLoadPhotoCameraReport2("http://copd.daoqihz.com/api/image/ossUpload",file);
                        }else {
                            Toast.makeText(SettingEditBaseInfoActivity.this,"只能上传jpg/png/bpm格式！",Toast.LENGTH_SHORT).show();
                        }


                    }
                }
                break;
        }
    }
    /**
     * 检查权限并打开相册。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndAlbumHospital() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //有权限调起相机拍照。
            openAlbumHospital();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(SettingEditBaseInfoActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ALBUM_REQUEST_CODE_HOSPITAL);
        }
    }
    /**
     * 打开相册
     */
    private void openAlbumHospital(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        //把所有照片显示出来
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO_REQUEST_CODE_HOSPITAL);
    }
    /**
     * 检查权限并打开相册。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndAlbumReport() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //有权限调起相机拍照。
            openAlbumReport();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(SettingEditBaseInfoActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ALBUM_REQUEST_CODE_REPORT);
        }
    }
    /**
     * 打开相册
     */
    private void openAlbumReport(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        //把所有照片显示出来
        intent.setType("image/*");
            startActivityForResult(intent, CHOOSE_PHOTO_REQUEST_CODE_REPORT);
    }


    /**
     * 检查权限并拍照。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndCameraHospital() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //有权限调起相机拍照。
            openCameraHospital();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE_HOSPITAL);
        }
    }


    /**
     * 调起相机拍照
     */
    private void openCameraHospital() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            photoFileHospital = null;
            Uri photoUri = null;
            photoUri = createImageUri();
//            if (isAndroidQ) {
//                // 适配android 10
//                photoUri = createImageUri();
//            } else {
//                try {
//                    photoFile = createImageFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if (photoFile != null) {
//                    mCameraImagePath = photoFile.getAbsolutePath();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
//                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
//                    } else {
//                        photoUri = Uri.fromFile(photoFile);
//                    }
//                }
//            }

            mCameraUriHospital = photoUri;
            Log.d("opencamera", String.valueOf(mCameraUriHospital));
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE_HOSPITAL);
            }
        }
    }

    /**
     * 检查权限并拍照。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndCameraReport() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //有权限调起相机拍照。
            openCameraReport();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE_REPORT);
        }
    }


    /**
     * 调起相机拍照
     */
    private void openCameraReport() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            photoFileReport = null;
            Uri photoUri = null;
            photoUri = createImageUri();
//            if (isAndroidQ) {
//                // 适配android 10
//                photoUri = createImageUri();
//            } else {
//                try {
//                    photoFile = createImageFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if (photoFile != null) {
//                    mCameraImagePath = photoFile.getAbsolutePath();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
//                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
//                    } else {
//                        photoUri = Uri.fromFile(photoFile);
//                    }
//                }
//            }

            mCameraUriReport = photoUri;
            Log.d("opencamera", String.valueOf(mCameraUriReport));
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE_REPORT);
            }
        }
    }

    /**
     * 根据图片路径显示图片
     * Api19也就是Android4.4及以上
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKatHospital(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (isDocumentUri(this,uri)){
            //如果document是Uri类型，则通过document id处理
            String docId= DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
//                String id = docId.split(":")[1];//解析数字格式的id
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                picidHospital =docId.split(":")[1];//id="26"
                picbaseUriHospital =Uri.parse("content://media/external/images/media");
//                picture.setImageURI(Uri.withAppendedPath(baseUri, id));
                //直接传入Uri地址，该地址为content://media/external/images/media/26
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://media/external/images/media"),Long.valueOf(docId));
//                imagePath = getImagePath(contentUri,null);
                displayImage(imagePath);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
//            imagePath = getImagePath(uri,null);
            displayImage(imagePath);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
            displayImage(imagePath);
        }
        chooseUriHospital = uri;

    }

    /**
     * 根据图片路径显示图片
     * Api19也就是Android4.4及以上
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKatReport(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (isDocumentUri(this,uri)){
            //如果document是Uri类型，则通过document id处理
            String docId= DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
//                String id = docId.split(":")[1];//解析数字格式的id
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                picidReport =docId.split(":")[1];//id="26"
                picbaseUriReport =Uri.parse("content://media/external/images/media");
//                picture.setImageURI(Uri.withAppendedPath(baseUri, id));
                //直接传入Uri地址，该地址为content://media/external/images/media/26
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://media/external/images/media"),Long.valueOf(docId));
//                imagePath = getImagePath(contentUri,null);
                displayImage(imagePath);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
//            imagePath = getImagePath(uri,null);
            displayImage(imagePath);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
            displayImage(imagePath);
        }
        chooseUriReport = uri;

    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 通过图片路径展示图片
     * @param imagePath
     */
    private void displayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            picture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(SettingEditBaseInfoActivity.this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 创建保存图片的文件
     */
    private File createImageFile() throws IOException {
        String imageName = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            imageName = "output_image.jpg";
        }
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    /**
     * 正面
     * ====根据 bitmap生成新的file
     */
    public static File createFilePic(Context context, Bitmap bitmap) {
        //https://www.jb51.net/article/181745.htm

        //文件夹  这两种文件夹都会随软件卸载而删掉
        File folder = context.getExternalCacheDir();//在Android>data>包名>的cache目录下，一般存放临时缓存数据
        //File folder = this.getExternalFilesDir("image");//在Android>data>包名>的files的image目录下，一般放一些长时间保存的数据
        if (!folder.exists()) {
            folder.mkdir();
        }

        SimpleDateFormat df = null;//设置日期格式在android中，创建文件时，文件名中不能包含“：”冒号
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            df = new SimpleDateFormat("yyyyMMddHHmmss");
        }
        String filename = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            filename = df.format(new Date());
        }
        //file图片
        File file = new File(folder.getAbsolutePath() + File.separator +filename+ ".jpg");

        try {

            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 图片质量压缩
     * @param
     * @param
     * @return
     */
    public static Bitmap compressImage1(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 设置为ture,只读取图片的大小，不把它加载到内存中去
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 1280, 800);// 此处，选取了480x800分辨率的照片

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;// 处理完后，同时需要记得设置为false

        return BitmapFactory.decodeFile(filePath, options);


    }


    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                             int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
//                Log.i(TAG,"isExternalStorageDocument***"+uri.toString());
//                Log.i(TAG,"docId***"+docId);
//                以下是打印示例：
//                isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATset%2FROC2018421103253.wav
//                docId***primary:Test/ROC2018421103253.wav
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
//                Log.i(TAG,"isDownloadsDocument***"+uri.toString());
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
//                Log.i(TAG,"isMediaDocument***"+uri.toString());
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(,"content***"+uri.toString());
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"file***"+uri.toString());
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}