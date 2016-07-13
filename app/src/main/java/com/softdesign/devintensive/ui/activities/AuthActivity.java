package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NtworksStatusChecker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ant on 11.07.16.
 */
public class AuthActivity extends BaseActivity {
    private CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;

    @BindView(R.id.login_password_et)
    EditText mPassword;
    @BindView(R.id.login_email_et)
    EditText mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mDataManager = DataManager.getInstance();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container_auth);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.remember_txt)
    public void onClickRemember() {
        rememberPassword();
    }

    @OnClick(R.id.login_btn)
    public void onClickLogin() {

        signIn();
    }


    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesing-app.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void loginSuccess(UserModelRes userModel) {
        showSnackbar(userModel.getData().getToken());
        mDataManager.getPreferancesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferancesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);
        saveUserFields(userModel);
        saveUserPhoto(userModel);
        saveUserAvatar(userModel);

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void signIn() {
        if (NtworksStatusChecker.isNetworkAvailable(this)) {

            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackbar("Bad login or password");
                    } else {
                        showSnackbar("Другая ошибка !!");
                    }

                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    //// TODO: 12.07.16 обработать ошибки retrofit

                }
            });
        } else {
            showSnackbar("Сеть отсутствует");
        }

    }

    private void saveUserValues(UserModelRes userModel) {
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRating(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects()
        };
        mDataManager.getPreferancesManager().saveUserProfileValues(userValues);
    }

    private void saveUserFields(UserModelRes userModel) {
        List<String> userFields = new ArrayList<String>();

        userFields.add(userModel.getData().getUser().getContacts().getPhone().replace(" ", ""));
        userFields.add(userModel.getData().getUser().getContacts().getEmail());
        userFields.add(userModel.getData().getUser().getContacts().getVk());
        userFields.add(userModel.getData().getUser().getRepositories().getRepo().get(0).getGit());
        userFields.add(userModel.getData().getUser().getPublicInfo().getBio());

        mDataManager.getPreferancesManager().saveUserProfileData(userFields);
    }

    private void saveUserPhoto(UserModelRes userModel){
        String photo =  userModel.getData().getUser().getPublicInfo().getPhoto();
        mDataManager.getPreferancesManager().saveUserPhoto(photo);
    }

    private void saveUserAvatar(UserModelRes userModel){
        String avatar =  userModel.getData().getUser().getPublicInfo().getAvatar();
        mDataManager.getPreferancesManager().saveUserAvatar(avatar);
    }

}
