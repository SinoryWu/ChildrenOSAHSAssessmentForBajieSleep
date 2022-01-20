package com.hzdq.bajiesleepchildrenHD.user.activities;

import android.Manifest;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

import com.google.gson.Gson;
import com.hzdq.bajiesleepchildrenHD.R;
import com.hzdq.bajiesleepchildrenHD.TokenDialog;
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityUpdateReportBinding;
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassPostTreatmentRecord;
import com.hzdq.bajiesleepchildrenHD.dataclass.UpLoadPicResponse;
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity;
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton;
import com.hzdq.bajiesleepchildrenHD.user.adapter.UpDataReportAddPicAdapter;
import com.hzdq.bajiesleepchildrenHD.user.dialog.CheckPhotoBitmapDialog;
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel;
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2;
import com.hzdq.bajiesleepchildrenHD.utils.EPSoftKeyBoardListener;
import com.hzdq.bajiesleepchildrenHD.utils.HideUI;
import com.hzdq.bajiesleepchildrenHD.utils.Shp;
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.provider.DocumentsContract.isDocumentUri;

public class UpdateReportActivity2 extends AppCompatActivity {
    private TokenDialog tokenDialog = null;
    private Shp shp;
    private ActivityUpdateReportBinding binding;
    private Button sleep, image, endoscope, biochemistry, other;
    List<Button> map= new ArrayList<>();
    private UserViewModel userViewModel;
    private RelativeLayout mRlAddPic;
    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;
    //申请访问相册权限的requestCode
    private static final int PERMISSION_ALBUM_REQUEST_CODE = 0x00000013;

    //成功调用相机后返回的requestCode
    private static final int CAMERA_REQUEST_CODE = 2;
    //成功打开相册后返回的requestCode
    private static final int CHOOSE_PHOTO_REQUEST_CODE = 3;
    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    private String mCameraImagePath;
    int barposition;
    //用于保存拍照图片的uri
    private Uri mCameraUri;
    private PopupWindow popupWindow;
    private Animation animation;
    Bitmap bitmapChoose,bitmapCamera;
    Uri picbaseUri,chooseUri;
    File photoFile ;
    String picid;
    List<String> listPicString = new ArrayList<>();
    private List<Bitmap> listpic = new ArrayList<Bitmap>();
    private RecyclerView mRcAddPic;
    String name;
    int patient_id;
    UpDataReportAddPicAdapter upDataReportAddPicAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_report);
        EPSoftKeyBoardListener epSoftKeyBoardListener = new EPSoftKeyBoardListener(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        HideUI hideUI = new HideUI(this);
        shp = new Shp(this);
        hideUI.hideSystemUI();
        mRcAddPic = binding.picReclerView;
        mRlAddPic = binding.updateReportAddImage;
        Intent intent = getIntent();
         patient_id = intent.getIntExtra("patient_id",0);

        barposition = intent.getIntExtra("reportbarpostion",0);
        ActivityCollector2.INSTANCE.addActivity(this);

        setBar();

        setLine1();
        epSoftKeyBoardListener.setListener(this, new EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                binding.updateReportMotion.transitionToEnd();
            }

            @Override
            public void keyBoardHide(int height) {
                binding.updateReportMotion.transitionToStart();
            }
        });


        binding.updateReportDoctor.setText("管理医生："+shp.getDoctorName());

       

        binding.updateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        initRecyclePic();
        mRlAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(view);
                showPuPopWindow(view);

            }
        });


        upDataReportAddPicAdapter.setOnItemClickItemListener(new UpDataReportAddPicAdapter.OnItemClickItemListener() {
            @Override
            public void onItemClickItem(int position) {

                if (position < 9){
                    upDataReportAddPicAdapter.removeData(position);
                    listPicString.remove(position);
                    Log.d("updateReportposition", "onItemClickItem: "+position);
//
//                    mRlAddPic.setVisibility(View.VISIBLE);

                }
            }
        });

        binding.updateReportCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCollector2.INSTANCE.removeActivity(UpdateReportActivity2.this);
                finish();

            }
        });

        binding.updateReportConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (userViewModel.getSubtype().getValue().equals("21")){
                    if (binding.oahi.getText().toString().equals("")){
                        ToastUtils.INSTANCE.showTextToast(UpdateReportActivity2.this,"OAHI是必填项");
                    }else {


                        HashMap<String, String> map = new HashMap<>();

                        map.put("hospitalid", String.valueOf(shp.getHospitalId()));
                        map.put("patient_id", String.valueOf(patient_id));
                        map.put("type","2");
                        map.put("oahi",binding.oahi.getText().toString().trim());
                        map.put("osas", String.valueOf(userViewModel.getAddTreatmentDegree().getValue()));
                        map.put("opinion", binding.updateReportEditText.getText().toString().trim());
                        map.put("attachment", listPicString.toString());
                        map.put("subtype",userViewModel.getSubtype().getValue());
                        String url = OkhttpSingleton.INSTANCE.getBASE_URL()+"/v2/treatSave";




                        postTreatmentRecord(url,map);
                    }

                }else{

                    if (binding.updateReportEditText.getText().toString().trim().equals("") && listpic.size()==0){
                        ToastUtils.INSTANCE.showTextToast(UpdateReportActivity2.this,"未填写任何内容");

                    }else {
                        HashMap<String, String> map = new HashMap<>();

                        map.put("hospitalid", String.valueOf(shp.getHospitalId()));
                        map.put("patient_id", String.valueOf(patient_id));
                        map.put("type","2");
                        map.put("osas", String.valueOf(userViewModel.getAddTreatmentDegree().getValue()));
                        map.put("opinion", binding.updateReportEditText.getText().toString().trim());
                        map.put("attachment", listPicString.toString());
                        map.put("subtype",userViewModel.getSubtype().getValue());
                        String url = OkhttpSingleton.INSTANCE.getBASE_URL()+"/v2/treatSave";

                        postTreatmentRecord(url,map);
                    }

                }

            }
        });

    }


    @Override
    protected void onDestroy() {
        ActivityCollector2.INSTANCE.removeActivity(this);
        super.onDestroy();
    }

    /**
     * 提交诊疗报告记录
     */
    protected void postTreatmentRecord(String url, HashMap<String, String> map) {



        //2.构造request
        //2.1构造requestbody

        HashMap<String, Object> params = new HashMap<String, Object>();

        Log.e("params:",String.valueOf(params));
        Set<String> keys = map.keySet();
        for (String key:keys)
        {
            params.put(key,map.get(key));

        }


        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBodyJson)
                .addHeader("token",shp.getToken())
                .addHeader("uid",shp.getUid())
                .addHeader("user-agent", shp.getUserAgent())
                .build();
        //3.将request封装为call
        Call call =   OkhttpSingleton.INSTANCE.ok().newCall(request);

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
                        ToastUtils.INSTANCE.showTextToast(UpdateReportActivity2.this,"上传报告网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Gson gson = new Gson();

                final DataClassPostTreatmentRecord dataClassPostTreatmentRecord = gson.fromJson(res, DataClassPostTreatmentRecord.class);



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dataClassPostTreatmentRecord.getCode() == 0) {

                            ToastUtils.INSTANCE.showTextToast(UpdateReportActivity2.this,"提交成功");
//                    String msg = stopDeviceResponse.getMsg();
                            setResult(RESULT_OK);
                            finish();


                        }else if (dataClassPostTreatmentRecord.getCode() == 10010 || dataClassPostTreatmentRecord.getCode() == 10004) {
                            if (tokenDialog == null) {
                                tokenDialog = new TokenDialog(UpdateReportActivity2.this, new TokenDialog.ConfirmAction() {
                                    @Override
                                    public void onRightClick() {
                                        shp.saveToSp("token", "");
                                        shp.saveToSp("uid", "");
                                        startActivity(new Intent(UpdateReportActivity2.this, LoginActivity.class));
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
                            ToastUtils.INSTANCE.showTextToast(UpdateReportActivity2.this,dataClassPostTreatmentRecord.getMsg());
                        }
                    }
                });


            }
        });
    }


    private void setBar() {
        sleep = findViewById(R.id.update_report_bar_sleep);
        image = findViewById(R.id.update_report_bar_image);
        endoscope = findViewById(R.id.update_report_bar_endoscope);
        biochemistry = findViewById(R.id.update_report_bar_biochemistry);
        other = findViewById(R.id.update_report_bar_other);
        Shp shp = new Shp(this);


        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sleep.setSelected(true);
                sleep.setTextColor(Color.parseColor("#2051BD"));
                image.setSelected(false);
                image.setTextColor(Color.parseColor("#C1C1C1"));
                endoscope.setSelected(false);
                endoscope.setTextColor(Color.parseColor("#C1C1C1"));
                biochemistry.setSelected(false);
                biochemistry.setTextColor(Color.parseColor("#C1C1C1"));
                other.setSelected(false);
                other.setTextColor(Color.parseColor("#C1C1C1"));
                binding.updateReportMotion2.transitionToStart();
                userViewModel.getSubtype().setValue("21");
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleep.setSelected(false);
                sleep.setTextColor(Color.parseColor("#C1C1C1"));
                image.setSelected(true);
                image.setTextColor(Color.parseColor("#2051BD"));
                endoscope.setSelected(false);
                endoscope.setTextColor(Color.parseColor("#C1C1C1"));
                biochemistry.setSelected(false);
                biochemistry.setTextColor(Color.parseColor("#C1C1C1"));
                other.setSelected(false);
                other.setTextColor(Color.parseColor("#C1C1C1"));
                binding.updateReportMotion2.transitionToEnd();
                userViewModel.getAddTreatmentDegree().setValue(0);
                userViewModel.getOahi().setValue("");
                binding.oahi.setText(userViewModel.getOahi().getValue());
                userViewModel.getSubtype().setValue("22");
            }
        });

        endoscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleep.setSelected(false);
                sleep.setTextColor(Color.parseColor("#C1C1C1"));
                image.setSelected(false);
                image.setTextColor(Color.parseColor("#C1C1C1"));
                endoscope.setSelected(true);
                endoscope.setTextColor(Color.parseColor("#2051BD"));
                biochemistry.setSelected(false);
                biochemistry.setTextColor(Color.parseColor("#C1C1C1"));
                other.setSelected(false);
                other.setTextColor(Color.parseColor("#C1C1C1"));
                binding.updateReportMotion2.transitionToEnd();
                userViewModel.getAddTreatmentDegree().setValue(0);
                userViewModel.getOahi().setValue("");
                binding.oahi.setText(userViewModel.getOahi().getValue());
                userViewModel.getSubtype().setValue("23");
            }
        });

        biochemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleep.setSelected(false);
                sleep.setTextColor(Color.parseColor("#C1C1C1"));
                image.setSelected(false);
                image.setTextColor(Color.parseColor("#C1C1C1"));
                endoscope.setSelected(false);
                endoscope.setTextColor(Color.parseColor("#C1C1C1"));
                biochemistry.setSelected(true);
                biochemistry.setTextColor(Color.parseColor("#2051BD"));
                other.setSelected(false);
                other.setTextColor(Color.parseColor("#C1C1C1"));
                binding.updateReportMotion2.transitionToEnd();
                userViewModel.getAddTreatmentDegree().setValue(0);
                userViewModel.getOahi().setValue("");
                binding.oahi.setText(userViewModel.getOahi().getValue());
                userViewModel.getSubtype().setValue("24");
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleep.setSelected(false);
                sleep.setTextColor(Color.parseColor("#C1C1C1"));
                image.setSelected(false);
                image.setTextColor(Color.parseColor("#C1C1C1"));
                endoscope.setSelected(false);
                endoscope.setTextColor(Color.parseColor("#C1C1C1"));
                biochemistry.setSelected(false);
                biochemistry.setTextColor(Color.parseColor("#C1C1C1"));
                other.setSelected(true);
                other.setTextColor(Color.parseColor("#2051BD"));
                binding.updateReportMotion2.transitionToEnd();
                userViewModel.getAddTreatmentDegree().setValue(0);
                userViewModel.getOahi().setValue("");
                binding.oahi.setText(userViewModel.getOahi().getValue());
                userViewModel.getSubtype().setValue("25");
            }
        });

        switch (barposition){
            case 1 :
                sleep.performClick();
                break;
            case 2 :
                image.performClick();
                break;
            case 3 :
                endoscope.performClick();
                break;
            case 4 :
                biochemistry.performClick();
                break;
            case 5 :
                other.performClick();
                break;
        }
    }

    /**
     * post请求上传照片
     * 相机
     * @param url
     * @param file
     */
    public void postUpLoadPhotoCamera1(String url, final File file) {
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
                        ToastUtils.INSTANCE.showTextToast(UpdateReportActivity2.this, "网络请求失败");
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
                            ToastUtils.INSTANCE.showTextToast(UpdateReportActivity2.this,"上传失败，请重新拍摄");

                        }else {
                            UpLoadPicResponse upLoadPicResponse = new Gson().fromJson(json,UpLoadPicResponse.class);
                            if (upLoadPicResponse.getCode() == 1){
                                upDataReportAddPicAdapter.addData(listpic.size(), bitmapCamera);
                                listPicString.add("\""+upLoadPicResponse.getData()+"\"");

                                upDataReportAddPicAdapter.setOnItemClickListener(new UpDataReportAddPicAdapter.OnClickItemListener() {
                                    @Override
                                    public void onItemClickItem(int position, Bitmap bitmap) {

                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(UpdateReportActivity2.this,R.style.CustomDialogPhoto,bitmap);
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
                            }else {
                                ToastUtils.INSTANCE.showTextToast(UpdateReportActivity2.this, upLoadPicResponse.getMessage());
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
    public void postUpLoadPhotoCamera2(String url, final File file) {
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
                        ToastUtils.INSTANCE.showTextToast(UpdateReportActivity2.this, "网络请求失败");
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
                            ToastUtils.INSTANCE.showTextToast(UpdateReportActivity2.this,"上传失败，请重新拍摄");

                        }else {
                            UpLoadPicResponse upLoadPicResponse = new Gson().fromJson(json,UpLoadPicResponse.class);
                            if (upLoadPicResponse.getCode() == 1){
                                upDataReportAddPicAdapter.addData(listpic.size(), bitmapChoose);

                                listPicString.add("\""+upLoadPicResponse.getData()+"\"");

                                upDataReportAddPicAdapter.setOnItemClickListener(new UpDataReportAddPicAdapter.OnClickItemListener() {
                                    @Override
                                    public void onItemClickItem(int position, Bitmap bitmap) {
                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(UpdateReportActivity2.this,R.style.CustomDialogPhoto,bitmap);
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
                            }else {
                                ToastUtils.INSTANCE.showTextToast(UpdateReportActivity2.this, upLoadPicResponse.getMessage());
                            }

                        }






                    }

                });


            }
        });
    }

    private void setLine1(){
        map.add(binding.normal);
        map.add(binding.light);
        map.add(binding.moderate);
        map.add(binding.severe);
        userViewModel.getAddTreatmentDegree().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {


                if (integer != 0){
                    for (int i = 0; i < map.size(); i++) {
                        map.get(i).setSelected(false);
                        map.get(i).setTextColor(Color.parseColor("#C1C1C1"));
                    }
                    map.get(integer-1).setSelected(true);
                    map.get(integer-1).setTextColor(Color.parseColor("#FFFFFF"));
                }else {
                    for (int i = 0; i < map.size(); i++) {
                        map.get(i).setSelected(false);
                        map.get(i).setTextColor(Color.parseColor("#C1C1C1"));
                    }
                }


            }
        });

//        binding.normal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userViewModel.getAddTreatmentDegree().setValue(1);
//            }
//        });
//        binding.light.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userViewModel.getAddTreatmentDegree().setValue(2);
//            }
//        });
//        binding.moderate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userViewModel.getAddTreatmentDegree().setValue(3);
//            }
//        });
//        binding.severe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userViewModel.getAddTreatmentDegree().setValue(4);
//            }
//        });

        binding.oahi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userViewModel.getOahi().postValue(s.toString());
                if (s.toString().equals("") || s.toString().equals(".")){
                    userViewModel.getAddTreatmentDegree().setValue(0);
                }else {
                    Float f = Float.valueOf(s.toString());
                    if (f >= 0f && f <1f){
                        userViewModel.getAddTreatmentDegree().setValue(1);
                    }else  if (f >=1f && f<5f ){
                        userViewModel.getAddTreatmentDegree().setValue(2);
                    }else  if (f >=5f && f<10f ){
                        userViewModel.getAddTreatmentDegree().setValue(3);
                    }else  if (f >=10f ){
                        userViewModel.getAddTreatmentDegree().setValue(4);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        mRcAddPic.setLayoutManager(linearLayoutManager);
//   获取数据，向适配器传数据，绑定适配器
//        list = initData();
        upDataReportAddPicAdapter = new UpDataReportAddPicAdapter(UpdateReportActivity2.this, listpic);
        mRcAddPic.setAdapter(upDataReportAddPicAdapter);
//   添加动画
        mRcAddPic.setItemAnimator(new DefaultItemAnimator());
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
    public void showPuPopWindow(View popupView){
        if (popupWindow == null) {
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
            popupWindow = new PopupWindow(popupView, dip2px(this,427f),
                    dip2px(this,170f));

            // 设置背景图片， 必须设置，不然动画没作用
//            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(250);

            // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
            popupWindow.showAtLocation(UpdateReportActivity2.this.findViewById(R.id.update_report_bottom_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            popupView.startAnimation(animation);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindow = null;
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_camera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPermissionAndCamera();
                    popupWindow.dismiss();
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPermissionAndAlbum();
                    popupWindow.dismiss();
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });

        }
    }



    /**
     * 处理权限申请的回调。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode){

            case PERMISSION_CAMERA_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，调起相机拍照。
                    openCamera();
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this,"拍照权限被拒绝", Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_ALBUM_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，打开相册。
                    openAlbum();
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

            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK){
//                    upDataProgressAddPicAdapter.addData(listpic.size(), String.valueOf(mCameraUri));
////
//
//                    if (listpic.size() > 3){
//                        mRlAddPic.setVisibility(View.GONE);
//                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        bitmapCamera = compressImage1(getPath(this, mCameraUri));
//                        if (isAndroidQ){
//
//                        }else {
//                            bitmapCamera = compressImage1(mCameraImagePath);;
//                        }

                    }
                    File file = createFilePic(UpdateReportActivity2.this, bitmapCamera);
                    postUpLoadPhotoCamera1("http://copd.daoqihz.com/api/image/ossUpload",file);
                }else {
                    Toast.makeText(this,"取消拍摄",Toast.LENGTH_LONG).show();
                }
                break;

            case CHOOSE_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK){

                    //判断手机版本号
                    if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT){
                        //Android 4.4及以上手机用这个方法
                        handleImageOnKitKat(data);
                        bitmapChoose = compressImage1(getPath(this,chooseUri));
//                        try {
//                            bitmap = getBitmapFormUri(UpDateProgressActivity.this,Uri.withAppendedPath(picbaseUri, picid));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        File file = createFilePic(UpdateReportActivity2.this, bitmapChoose);
                      postUpLoadPhotoCamera2("http://copd.daoqihz.com/api/image/ossUpload",file);

                    }
                }
                break;
        }
    }
    /**
     * 检查权限并打开相册。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndAlbum() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //有权限调起相机拍照。
            openAlbum();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(UpdateReportActivity2.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ALBUM_REQUEST_CODE);
        }
    }
    /**
     * 打开相册
     */
    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        //把所有照片显示出来
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO_REQUEST_CODE);
    }



    /**
     * 检查权限并拍照。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndCamera() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //有权限调起相机拍照。
            openCamera();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
        }
    }


    /**
     * 调起相机拍照
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
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

            mCameraUri = photoUri;
            Log.d("opencamera", String.valueOf(mCameraUri));
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    /**
     * 根据图片路径显示图片
     * Api19也就是Android4.4及以上
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (isDocumentUri(this,uri)){
            //如果document是Uri类型，则通过document id处理
            String docId= DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
//                String id = docId.split(":")[1];//解析数字格式的id
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                picid=docId.split(":")[1];//id="26"
                picbaseUri=Uri.parse("content://media/external/images/media");
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
        chooseUri = uri;

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
            Toast.makeText(UpdateReportActivity2.this,"failed to get image",Toast.LENGTH_SHORT).show();
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
