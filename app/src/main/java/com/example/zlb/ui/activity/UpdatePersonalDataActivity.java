package com.example.zlb.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zlb.R;
import com.example.zlb.api.IUser;
import com.example.zlb.bean.CallBackBaseBean;

import com.example.zlb.bean.MessageEvent;
import com.example.zlb.common.MyGlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.wjh.utillibrary.base.ActionBarActivity;
import com.wjh.utillibrary.network.RetrofitHelper;
import com.wjh.utillibrary.network.base.JsonItem;
import com.wjh.utillibrary.network.callback.MsgCallBack;
import retrofit2.Call;
import retrofit2.Response;
import com.wjh.utillibrary.utils.Utils;
import com.wjh.utillibrary.view.CircleImageView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import static com.wjh.utillibrary.common.Config.SERVICE_URL;

public class UpdatePersonalDataActivity extends ActionBarActivity {
    private TextView mNickName;
    private TextView mSex;
    private TextView mBirthday;
    private EditText mAddress;
    private EditText mPhoneNumber;
    private EditText mEmail;
    private Button mSave;
    private CircleImageView mUserImage;
    private RelativeLayout mChangeImg;

    private String id;
    private String nickname;
    private String sex;
    private String birthday;
    private String address;
    private String phoneNumber;
    private String email;
    private String url;
    private List<Uri> uris = new ArrayList<>();
    public final static int REQUEST_PICK_IMG = 10024;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_update_personal_data;
    }

    @Override
    public void initView() {
        super.initView();
        setCenterTitle("修改资料");
        mNickName = (TextView) findViewById(R.id.nickname);
        mSex = (TextView) findViewById(R.id.sex);
        mBirthday = (TextView) findViewById(R.id.birthday);
        mAddress = (EditText) findViewById(R.id.address);
        mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        mEmail = (EditText) findViewById(R.id.email);
        mSave = (Button) findViewById(R.id.save);
        mChangeImg = (RelativeLayout) findViewById(R.id.change_img);
        mUserImage = (CircleImageView) findViewById(R.id.user_image);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        nickname = intent.getStringExtra("nickname");
        sex = intent.getStringExtra("sex");
        birthday = intent.getStringExtra("birthday");
        address = intent.getStringExtra("address");
        phoneNumber = intent.getStringExtra("phoneNumber");
        email = intent.getStringExtra("email");
        url = intent.getStringExtra("url");

        Glide.with(this).load(url).into(mUserImage);

        mNickName.setText(nickname);
        if (sex.equals("") || sex == null){
            mSex.setText("待认证");
        }else {
            if (sex.equals("男")){
                mSex.setText("男");
            }else if (sex.equals("女")){
                mSex.setText("女");
            }

        }

        if (birthday == null || birthday.equals("")){
            mBirthday.setText("待认证");
        }else {
            mBirthday.setText(birthday);
        }
        if (address == null || address.equals("")){
            mAddress.setHint("请填写");
        }else {
            mAddress.setHint(address);
        }
        if (email == null || email.equals("")){
            mEmail.setHint("请填写");
        }else {
            mEmail.setHint(email);
        }
        mPhoneNumber.setHint(phoneNumber);
    }

    @Override
    public void init() {
        super.init();
        mChangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(UpdatePersonalDataActivity.this);
                Matisse.from(UpdatePersonalDataActivity.this)
                        .choose(MimeType.allOf())//图片类型
                        .countable(true)//true:选中后显示数字;false:选中后显示对号
                        .maxSelectable(1)//可选的最大数
                        .capture(true)//选择照片时，是否显示拍照
                        .captureStrategy(new CaptureStrategy(true, "com.example.zlb"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(new MyGlideEngine())//图片加载引擎
                        .forResult(REQUEST_PICK_IMG);//
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a_ddress = mAddress.getText().toString();
                String e_mail = mEmail.getText().toString();
                String phone = mPhoneNumber.getText().toString();
                if (a_ddress !=null && !a_ddress.equals("")){
                    address = a_ddress;
                    if (phone != null && !phone.equals("")){
                        if (Utils.isPhoneNumber(phone)){
                            phoneNumber = phone;
                            if(e_mail != null && !e_mail.equals("")){
                                if (Utils.isEmail(e_mail)){
                                    email = e_mail;
                                    update();
                                }else {
                                    showToast("请填写正确的邮箱格式");
                                }
                            }else {
                                update();
                            }
                        }else {
                            showToast("请填写正确的手机格式");
                        }
                    }else {
                        if(e_mail != null && !e_mail.equals("")){
                            if (Utils.isEmail(e_mail)){
                                email = e_mail;
                                update();
                            }else {
                                showToast("请填写正确的邮箱格式");
                            }
                        }else {
                            update();
                        }
                    }
                }else {
                    if (phone != null && !phone.equals("")){
                        if (Utils.isPhoneNumber(phone)){
                            phoneNumber = phone;
                            if(e_mail != null && !e_mail.equals("")){
                                if (Utils.isEmail(e_mail)){
                                    email = e_mail;
                                    update();
                                }else {
                                    showToast("请填写正确的邮箱格式");
                                }
                            }else {
                                update();
                            }
                        }else {
                            showToast("请填写正确的手机格式");
                        }
                    }else {
                        if(e_mail != null && !e_mail.equals("")){
                            if (Utils.isEmail(e_mail)){
                                email = e_mail;
                                update();
                            }else {
                                showToast("请填写正确的邮箱格式");
                            }
                        }else {
                            update();
                        }
                    }
                }
            }
        });
    }
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_PICK_IMG:
                if (resultCode == RESULT_OK){
                    uris = Matisse.obtainResult(data);
                }
                if (data != null){
                    File file = new File(getRealPathFromUri(this,uris.get(0)));
                    IUser userObj = RetrofitHelper.create(IUser.class);
                    userObj.upload(RetrofitHelper.getBody(file)).enqueue(new MsgCallBack<CallBackBaseBean>(this) {
                        @Override
                        public void onFailed(Call<CallBackBaseBean> call, Throwable t) {

                        }

                        @Override
                        public void onSucceed(Call<CallBackBaseBean> call, Response<CallBackBaseBean> response) {
                            int status = response.body().getStatus();
                            if (status == 0){
                                String url = SERVICE_URL + "/static/image"+response.body().getMsg();
                                Glide.with(UpdatePersonalDataActivity.this).load(url).into(mUserImage);
                                EventBus.getDefault().post(new MessageEvent("refresh"));
                            } else if (status == 1){
                                showToast("上传失败");
                            }
                        }
                    });
                }
                break;
        }
    }

    private void update(){
        IUser userObj = RetrofitHelper.create(IUser.class);
        userObj.updateUserInfo(RetrofitHelper.getBody(new JsonItem("id",id),new JsonItem("nickname",nickname)
                ,new JsonItem("phoneNumber",phoneNumber),new JsonItem("email",email),new JsonItem("sex",sex)
                ,new JsonItem("birthday",birthday),new JsonItem("address",address),new JsonItem("cnId",null)))
                .enqueue(new MsgCallBack<CallBackBaseBean>(UpdatePersonalDataActivity.this,true) {
                    @Override
                    public void onFailed(Call<CallBackBaseBean> call, Throwable t) {

                    }

                    @Override
                    public void onSucceed(Call<CallBackBaseBean> call, Response<CallBackBaseBean> response) {
                        int status = response.body().getStatus();
                        if (status ==0){
                            showToast("更新成功");
                            setResult(RESULT_OK);
                            finish();
                        }else {
                            showToast("更新失败");
                        }
                    }
                });
    }
}
