package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.redmadrobot.chronos.ChronosConnector;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import com.softdesign.devintensive.data.storage.chrones.OperationChronesProfilInNetwork;
import com.softdesign.devintensive.data.storage.chrones.OperationChronesSaveDB;
import com.softdesign.devintensive.data.storage.chrones.ProfilInNetwork;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.AppConfig;

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

    private static final int REQVEST_CODE = 10;
    private LinearLayout mLlMain;
    private CoordinatorLayout mCoordinatorLayout;

    private ChronosConnector mConnector;
    private DataManager mDataManager;
    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;
    private boolean mFlagToken = false;


    @BindView(R.id.login_password_et)
    EditText mPassword;
    @BindView(R.id.login_email_et)
    EditText mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mLlMain = (LinearLayout) findViewById(R.id.ll_main_activity_auth);

        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);
        mDataManager = DataManager.getInstance();

        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container_auth);


        if (!mDataManager.getPreferancesManager().getAuthToken().equals("null")) {
            mLlMain.setVisibility(View.INVISIBLE);
            saveUserInDb();
        }


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

    /*private void loginSuccess(UserModelRes userModel) {
        //showSnackbar(userModel.getData().getToken());
        mDataManager.getPreferancesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferancesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);
        saveUserInDb();

        saveUserFields(userModel);
        saveUserPhoto(userModel);
        saveUserAvatar(userModel);

        showMainActivity();


    }*/

    private void showMainActivity() {
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(AuthActivity.this, MainActivity.class);
                //Intent loginIntent = new Intent(AuthActivity.this, UserListActivity.class);
                startActivity(loginIntent);
            }
        }, AppConfig.START_DELAY);*/
        Intent loginIntent = new Intent(AuthActivity.this, MainActivity.class);
        //startActivity(loginIntent);
        startActivityForResult(loginIntent, REQVEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQVEST_CODE){
            finish();
        }
    }

    private void signIn() {
        showProgress();
        Log.d("TAG", "signIn()++++++++++++++++");
        mLlMain.setVisibility(View.INVISIBLE);
        mConnector.runOperation(new OperationChronesProfilInNetwork(mLogin.getText().toString(), mPassword.getText().toString()), true);
       /*
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
                    //обработать ошибки retrofit
                }
            });
        } else {
            showSnackbar("Сеть отсутствует");
        }
        hideProgress();*/

    }

    /*private void saveUserValues(UserModelRes userModel) {
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

    private void saveUserPhoto(UserModelRes userModel) {
        String photo = userModel.getData().getUser().getPublicInfo().getPhoto();
        mDataManager.getPreferancesManager().saveUserPhoto(photo);
    }

    private void saveUserAvatar(UserModelRes userModel) {
        String avatar = userModel.getData().getUser().getPublicInfo().getAvatar();
        mDataManager.getPreferancesManager().saveUserAvatar(avatar);
    }*/

    private void saveUserInDb() {
        showProgress();
        mConnector.runOperation(new OperationChronesSaveDB(), true);

       /* Call<UserListRes> call = mDataManager.getUserListFromNetwork();

        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == 200) {

                        List<Repository> allRepositories = new ArrayList<Repository>();
                        List<User> allUsers = new ArrayList<User>();

                        for (UserListRes.UserData userRes : response.body().getData()) {
                            allRepositories.addAll(getRepoListfromUserRes(userRes));
                            allUsers.add(new User(userRes));
                        }
                        mRepositoryDao.insertOrReplaceInTx(allRepositories);
                        mUserDao.insertOrReplaceInTx(allUsers);
                    } else {
                        showSnackbar("Список пользователей не может быть получен");
                        Log.e(TAG, "onResponse: " + String.valueOf(response.errorBody().source()));
                    }
                } catch (NullPointerException e) {
                    Log.d(TAG, e.toString());
                }
            }
            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                // обработка ошибок
            }
        });*/

    }


    private List<Repository> getRepoListfromUserRes(UserListRes.UserData userData) {
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();
        for (UserModelRes.Repo repositoryRes : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repositoryRes, userId));
        }
        return repositories;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnector.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConnector.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mConnector.onSaveInstanceState(outState);
    }


    public void onOperationFinished(final OperationChronesSaveDB.Result result) {

        if (result.isSuccessful()) {
            //   обрабатываем то что получено от хронеса
            if (result.getOutput().isStatus()){

            } else {
                mLlMain.setVisibility(View.VISIBLE);
                showSnackbar(result.getOutput().getStrStatus());
            }

            mRepositoryDao.insertOrReplaceInTx(result.getOutput().getAllRepositories());
            mUserDao.insertOrReplaceInTx(result.getOutput().getAllUsers());
        }
        showMainActivity();
        hideProgress();
    }

    public void onOperationFinished(final OperationChronesProfilInNetwork.Result result) {

        if (result.isSuccessful()) {
            if (result.getOutput().isStatus()){
                saveUserInDb();
            } else {
                mLlMain.setVisibility(View.VISIBLE);
                showSnackbar(result.getOutput().getStrStatus());
            }
        }
        //hideProgress();
    }

}
