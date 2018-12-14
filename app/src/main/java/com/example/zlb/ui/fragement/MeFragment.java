package com.example.zlb.ui.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zlb.R;
import com.example.zlb.adapter.ItemRecyclerAdapter;
import com.example.zlb.api.IUser;
import com.example.zlb.bean.LoginBean;
import com.example.zlb.bean.MeItemBean;
import com.example.zlb.bean.UserInfBean;
import com.example.zlb.ui.activity.AboutUsActivity;
import com.example.zlb.ui.activity.AuthenticationActivity;
import com.example.zlb.ui.activity.ChangePasswordActivity;
import com.example.zlb.ui.activity.FeedBackActivity;
import com.example.zlb.ui.activity.LoginActivity;
import com.example.zlb.ui.activity.MoreActivity;
import com.example.zlb.ui.activity.OrderModeActivity;
import com.example.zlb.ui.activity.PersonalDataActivity;
import com.wjh.utillibrary.base.BaseFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.wjh.utillibrary.common.Config;
import com.wjh.utillibrary.network.RetrofitHelper;
import com.wjh.utillibrary.network.base.JsonItem;
import com.wjh.utillibrary.network.callback.MsgCallBack;
import retrofit2.Call;
import retrofit2.Response;
import com.wjh.utillibrary.utils.UserInfoHelper;
import com.wjh.utillibrary.view.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class MeFragment extends BaseFragment implements View.OnClickListener{
    private final static String TAG = MeFragment.class.getSimpleName();
    private RelativeLayout mUserRl;
    private LinearLayout mUserLogin;
    private LinearLayout mUserNoLogin;
    private LinearLayout mAboutUs;
    private LinearLayout mMore;
    private LinearLayout mFeedback;
    private LinearLayout mChangePassword;
    private LinearLayout mAuthentication;
    private LinearLayout mOrderAll;
    private RecyclerView mOrderRv;
    private TextView mNickname;
    private TextView mState;
    private TextView mRole;
    private CircleImageView mUserImage;
    private int mStaTe;
    private List<MeItemBean> list;
    private static final int LOGIN = 10001;
    private static final int NO_LOGIN = 0;
    private static final int LOGIN_OK = 1;
    private int code = NO_LOGIN;//0表示没登录成功，1表示登录成功
    private static final int TO_PERSONAL_DATA = 10002;
    private static final int TO_CHANGE_PASSWORD = 10005;
    private static final int TO_AUTHENTICATION = 10006;

    class RefreshUserViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOGIN_OK){
                getUserData();
                mUserNoLogin.setVisibility(View.GONE);
                mUserLogin.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mUserRl = view.findViewById(R.id.user_rl);
        mOrderRv = view.findViewById(R.id.order_rv);
        mNickname = view.findViewById(R.id.nickname);
        mState = view.findViewById(R.id.stat);
        mRole = view.findViewById(R.id.role);
        mUserLogin = view.findViewById(R.id.user_login);
        mUserNoLogin = view.findViewById(R.id.user_no_login);
        mAboutUs = view.findViewById(R.id.about_us);
        mMore = view.findViewById(R.id.more);
        mFeedback = view.findViewById(R.id.feedback);
        mChangePassword = view.findViewById(R.id.change_password);
        mAuthentication = view.findViewById(R.id.authentication);
        mUserImage = view.findViewById(R.id.user_image);
        mOrderAll = view.findViewById(R.id.order_all);

        mUserRl.setOnClickListener(this);
        mAboutUs.setOnClickListener(this);
        mMore.setOnClickListener(this);
        mFeedback.setOnClickListener(this);
        mChangePassword.setOnClickListener(this);
        mAuthentication.setOnClickListener(this);
        mOrderAll.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();

        list = new ArrayList<>();
        String DataTitle[] = new String[]{"未支付","未处理","处理中","待评价","已完成"};
        Integer DataImg[] = new Integer[]{R.drawable.order_process,R.drawable.order_process,R.drawable.order_process,R.drawable.order_assess,R.drawable.order_finsh};
        for (int i = 0; i < DataTitle.length; i++){
            MeItemBean bean = new MeItemBean();
            bean.setTitle(DataTitle[i]);
            bean.setId(DataImg[i]);
            list.add(bean);
        }

        ItemRecyclerAdapter adapter = new ItemRecyclerAdapter(R.layout.item_recycler, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (code == LOGIN_OK){
                    Intent intent = new Intent(getContext(),OrderModeActivity.class);
                    switch (position){
                        case 0:
                            intent.putExtra("state",4);
                            break;
                        case 1:
                            intent.putExtra("state",1);
                            break;
                        case 2:
                            intent.putExtra("state",2);
                            break;

                        case 3:
                            intent.putExtra("state",3);
                            break;

                        case 4:
                            intent.putExtra("state",0);
                            break;
                    }
                    startActivity(intent);
                }else {
                    showToast("请先登录");
                }

            }
        });

        mOrderRv.setAdapter(adapter);
        mOrderRv.setLayoutManager(new GridLayoutManager(getContext(),3, GridLayoutManager.VERTICAL,false));
    }

    //懒加载

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.e(TAG, " setUserVisibleHint() --> isVisibleToUser = " + isVisibleToUser);

        if (isVisibleToUser) {
            String token = UserInfoHelper.getInstance().getToken();
            if (token != null && !token.equals("")){
                autoLogin();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_rl:
                if (code == NO_LOGIN){
                    startActivityForResult(LoginActivity.class,LOGIN);
                }else if (code == LOGIN_OK){
                    startActivityForResult(PersonalDataActivity.class,TO_PERSONAL_DATA);
                }
                break;

            case R.id.authentication:
                if (code == LOGIN_OK){
                    switch (mStaTe){
                        case 0:
                            break;

                        case 1:
                            startActivityForResult(AuthenticationActivity.class,TO_AUTHENTICATION);
                            break;

                        case 2:
                            showToast("等待认证中");
                            break;

                        case 3:
                            startActivityForResult(AuthenticationActivity.class,TO_AUTHENTICATION);
                            break;
                    }
                }else {
                    showToast("请先登录");
                }
                break;

            case R.id.feedback:
                if (code == LOGIN_OK){
                    startActivity(FeedBackActivity.class);
                }else {
                    showToast("请先登录");
                }
                break;

            case R.id.about_us:
                startActivity(AboutUsActivity.class);
                mAboutUs.setBackgroundColor(getResources().getColor(R.color.white));
                break;

            case R.id.more:
                startActivity(MoreActivity.class);
                break;

            case R.id.change_password:
                if (code == LOGIN_OK){
                    startActivityForResult(ChangePasswordActivity.class,TO_CHANGE_PASSWORD);
                }else {
                    showToast("请先登录");
                }
                break;

            case R.id.order_all:
                if (code == LOGIN_OK){
                    Intent intent = new Intent(getContext(),OrderModeActivity.class);
                    intent.putExtra("state",9);
                    startActivity(intent);
                }else {
                    showToast("请先登录");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LOGIN:
                if (resultCode ==RESULT_OK){
                    code = LOGIN_OK;
                    mUserNoLogin.setVisibility(View.GONE);
                    mUserLogin.setVisibility(View.VISIBLE);
                    //将数据保存本地
                    String name = data.getExtras().getString("nickname");
                    String password = data.getExtras().getString("password");
                    String token = data.getExtras().getString("token");
                    String phoneNum = data.getExtras().getString("phoneNum");
                    String trueName = data.getExtras().getString("trueName");
                    UserInfoHelper.getInstance().save(name,password,token,trueName,phoneNum);

                    UserInfoHelper.getInstance().setToken(token);
                    UserInfoHelper.getInstance().setNickname(name);
                    UserInfoHelper.getInstance().setPassword(password);
                    UserInfoHelper.getInstance().setTrueName(trueName);
                    UserInfoHelper.getInstance().setPhoneNumber(phoneNum);

                    RefreshUserViewHandler handler = new RefreshUserViewHandler();
                    handler.sendEmptyMessage(LOGIN_OK);
                }
                break;

            case TO_PERSONAL_DATA:
                if (resultCode ==RESULT_OK){
                    //用户退出登陆
                    code = NO_LOGIN;
                    mState.setText("未认证");
                    UserInfoHelper.getInstance().remove();
                    mUserNoLogin.setVisibility(View.VISIBLE);
                    mUserLogin.setVisibility(View.GONE);
                    mUserImage.setImageResource(R.drawable.default_image);

                    UserInfoHelper.getInstance().setToken("");
                    UserInfoHelper.getInstance().setNickname("");
                    UserInfoHelper.getInstance().setPassword("");
                    UserInfoHelper.getInstance().setTrueName("");
                    UserInfoHelper.getInstance().setPhoneNumber("");
                    Log.e(TAG,"---------------->清理用户数据");
                }
                break;

            case TO_CHANGE_PASSWORD:
                if (resultCode ==RESULT_OK){

                    new Handler().postDelayed(new Runnable(){

                        public void run() {

                            code = NO_LOGIN;
                            UserInfoHelper.getInstance().remove();
                            mUserNoLogin.setVisibility(View.VISIBLE);
                            mUserLogin.setVisibility(View.GONE);
                            Log.e(TAG,"---------------->修改密码重新登录");
                            showToast("密码已被修改,请重新登录");
                            startActivityForResult(LoginActivity.class,LOGIN);

                        }

                    }, 1000);

                }

                break;

            case TO_AUTHENTICATION:
                if (resultCode == RESULT_OK){
                    getUserData();
                }
                break;

        }
    }

    public void getUserData(){
        IUser userObj = RetrofitHelper.create(IUser.class);
        userObj.getUserInfo()
                .enqueue(new MsgCallBack<UserInfBean>(getContext()) {
                    @Override
                    public void onFailed(Call<UserInfBean> call, Throwable t) {

                    }

                    @Override
                    public void onSucceed(Call<UserInfBean> call, Response<UserInfBean> response) {
                        int status = response.body().getStatus();
                        Log.e(TAG,"获取个人资料--------------------->"+status);
                        if (status == 0){
                            UserInfBean.Data data = response.body().getData();
                            String nickname = data.getNickname();
                            String imageUrl = Config.SERVICE_URL + "/static/image" + data.getImage();

                            Picasso
                                    .with(getContext())
                                    .load(imageUrl)
                                    .into(mUserImage);

                            int state = data.getState();
                            int role = data.getRole();
                            mStaTe = state;
                            mNickname.setText(nickname);
                            switch (state){
                                case 0:
                                    mState.setText("已认证");
                                    break;
                                case 1:
                                    mState.setText("未认证");
                                    break;
                                case 2:
                                    mState.setText("等待认证");
                                    break;
                                case 3:
                                    mState.setText("认证未通过");
                            }

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
                            //保存认证情况
                            String State = Integer.toString(state);
                            UserInfoHelper.getInstance().saveState(State);
                            UserInfoHelper.getInstance().setState(State);

                        }else if (status == 1){
                            mUserNoLogin.setVisibility(View.VISIBLE);
                            mUserLogin.setVisibility(View.GONE);
                            code = NO_LOGIN;
                            UserInfoHelper.getInstance().remove();
                            showToast("登录失效(有效期为2小时),请重新登录");
                        }
                    }
                });
    }

    private void autoLogin(){
        String nickname = UserInfoHelper.getInstance().getNickname();
        String password = UserInfoHelper.getInstance().getPassword();
        if (nickname != null || !nickname.equals("") || password != null || !password.equals("")){
            IUser userObj = RetrofitHelper.create(IUser.class);
            userObj.login(RetrofitHelper.getBody(new JsonItem("nickname",nickname),new JsonItem("password",password)))
                    .enqueue(new MsgCallBack<LoginBean>(getContext()) {
                        @Override
                        public void onFailed(Call<LoginBean> call, Throwable t) {

                        }

                        @Override
                        public void onSucceed(Call<LoginBean> call, Response<LoginBean> response) {
                            int status = response.body().getStatus();
                            if (status == 0){
                                Log.e(TAG,"----------------------------->自动登录成功");
                                code = LOGIN_OK;

                                //自动登录完成后获得用户数据
                                RefreshUserViewHandler handler = new RefreshUserViewHandler();
                                handler.sendEmptyMessage(LOGIN_OK);

                            } else if (status == 1){
                                mUserNoLogin.setVisibility(View.VISIBLE);
                                mUserLogin.setVisibility(View.GONE);
                                code = NO_LOGIN;
                                showToast("自动登录失败,请手动登录");
                            }
                        }
                    });

        }
    }
}
