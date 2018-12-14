package com.example.zlb.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.example.zlb.R;
import com.example.zlb.api.IUser;
import com.example.zlb.bean.UserInfBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import base.ActionBarActivity;
import common.Config;
import network.RetrofitHelper;
import network.callback.MsgCallBack;
import retrofit2.Call;
import retrofit2.Response;
import utils.UserInfoHelper;

public class PersonalDataActivity extends ActionBarActivity {
    private final static String TAG = PersonalDataActivity.class.getSimpleName();
    private TextView mNickname;
    private TextView mRole;
    private TextView mAddress;
    private TextView mBirthday;
    private TextView mPhoneNumber;
    private TextView mEmail;
    private TextView mExit;
    private TextView mUpdateData;
    private ImageView mSex;
    private ImageView mUserImage;

    private String address ="";
    private String birthday ="";
    private String phoneNumber ="";
    private String email ="";
    private String nickName = "";
    private String sex = "";
    private String id = "";
    private String url = "";


    private static final int TO_UPDATE = 10001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserData();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal_data;
    }

    @Override
    public void initView() {
        super.initView();
        setCenterTitle("个人中心");
        mNickname = (TextView) findViewById(R.id.nickname);
        mRole = (TextView) findViewById(R.id.role);
        mAddress = (TextView) findViewById(R.id.address);
        mBirthday = (TextView) findViewById(R.id.birthday);
        mPhoneNumber = (TextView) findViewById(R.id.phoneNumber);
        mEmail = (TextView) findViewById(R.id.email);
        mExit = (TextView) findViewById(R.id.exit);
        mUpdateData = (TextView) findViewById(R.id.update_data);
        mSex = (ImageView) findViewById(R.id.sex);
        mUserImage = (ImageView) findViewById(R.id.user_image);
    }

    @Override
    public void init() {
        super.init();

        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalDataActivity.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                builder.setTitle("退出当前账号")
                        .setMessage("退出后将返回用户界面")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("PersonalDataActivity","---------------->退出当前用户");
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                builder.show();
            }
        });

        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (url != null && !url.equals("")){
                    ArrayList<String> list = new ArrayList<>();
                    list.add(url);
                    new PhotoPagerConfig.Builder(PersonalDataActivity.this)
                            .setBigImageUrls(list)
                            .setSavaImage(true)
//                        .setPosition(2)
//                        .setSaveImageLocalPath("这里是你想保存的图片地址")
                            .build();
                }else {
                    showToast("未获得图片信息");
                }
            }
        });

        mUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将所需的数据传递修改资料界面
                Intent intent = new Intent(PersonalDataActivity.this,UpdatePersonalDataActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("nickname",nickName);
                intent.putExtra("sex",sex);
                intent.putExtra("address",address);
                intent.putExtra("birthday",birthday);
                intent.putExtra("phoneNumber",phoneNumber);
                intent.putExtra("email",email);
                intent.putExtra("url",url);
                startActivityForResult(intent,TO_UPDATE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TO_UPDATE:
                if (resultCode == RESULT_OK){
                    getUserData();
                    Log.e(TAG,"数据刷新");
                }
                break;
        }
    }

    public void getUserData(){
        nickName = UserInfoHelper.getInstance().getNickname();
        IUser userObj = RetrofitHelper.create(IUser.class);
        userObj.getUserInfo()
                .enqueue(new MsgCallBack<UserInfBean>(PersonalDataActivity.this,true) {
                    @Override
                    public void onFailed(Call<UserInfBean> call, Throwable t) {

                    }

                    @Override
                    public void onSucceed(Call<UserInfBean> call, Response<UserInfBean> response) {
                        int status = response.body().getStatus();
                        Log.e(TAG,"获取个人资料--------------------->"+status);
                        if (status ==0){
                            Log.e(TAG,"获取个人资料--------------------->成功");

                            UserInfBean.Data data = response.body().getData();
                            if (data != null){
                                String nickname = data.getNickname();
                                int role = data.getRole();
                                int state = data.getState();
                                url = Config.SERVICE_URL + "/static/image" + response.body().getData().getImage();
                                Picasso
                                        .with(PersonalDataActivity.this)
                                        .load(url)
                                        .into(mUserImage);
                                //只有认证成功后才会显示性别与生日的信息
                                if (state == 0){
                                    birthday = data.getBirthday();
                                    sex = data.getSex();
                                }
                                id = data.getId();
                                address = data.getAddress();
                                phoneNumber = data.getPhoneNumber();
                                email = data.getEmail();

                                if(sex != null){
                                    switch (sex){
                                        case "男":
                                            mSex.setImageResource(R.drawable.sex_m);
                                            break;
                                        case "女":
                                            mSex.setImageResource(R.drawable.sex_f);
                                            break;
                                    }
                                }

                                mNickname.setText(nickname);
                                mPhoneNumber.setText(phoneNumber);
                                switch (role){
                                    case 0:
                                        mRole.setText("管理员用户");
                                        break;
                                    case 1:
                                        mRole.setText("普通用户");
                                        break;
                                    case 2:
                                        mRole.setText("第三方用户");
                                        break;
                                }



                                if (address != null && !address.equals("")){
                                    mAddress.setText(address);
                                }else {
                                    mAddress.setText("尚未数据");
                                }

                                if (birthday != null && !birthday.equals("")){
                                    mBirthday.setText(birthday);
                                }else {
                                    mBirthday.setText("尚未数据");
                                }

                                if (email != null && !email.equals("")){
                                    mEmail.setText(email);
                                }else {
                                    mEmail.setText("尚未数据");
                                }
                            }
                        }else {
                            UserInfoHelper.getInstance().remove();
                            showToast("登录失效,请重新登录");
                            finish();
                        }
                    }
                });
    }
}
