package com.softdesign.devintensive.data.storage.chrones;

import android.os.Handler;
import android.util.Log;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.softdesign.devintensive.utils.NtworksStatusChecker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ants on 26.07.2016.
 */
public class ProfilInNetwork {
    private DataManager mDataManager;
    private String mLogin;
    private String mPassword;
    private String strStatus;
    private boolean isStatus = false;

    public ProfilInNetwork(String login, String password) {
        this.mLogin = login;
        this.mPassword = password;
        main();
    }
    private void main() {

        mDataManager = DataManager.getInstance();

        if (NtworksStatusChecker.isNetworkAvailable(DevintensiveApplication.getContext())) {


            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin, mPassword));
            //запускаем нашь запрос в основном потоке тоесть в потоке хронес
            // закоментировано запуск запроса в потоке ретрофита но мы в хроносе
            try {
                Response<UserModelRes> response = call.execute();
                if (response.code() == 200) {
                    isStatus = true;
                    strStatus = "<-- 200 OK";
                    loginSuccess(response.body());
                } else if (response.code() == 404) {
                    strStatus = "Bad login or password";
                    isStatus = false;
                } else {
                    strStatus = "Другая ошибка !!";
                    isStatus = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                strStatus = "Что-то пошло не так смотри  ProfilInNetwork !!";
                isStatus = false;
            }
            /*call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        isStatus = true;
                        strStatus = "<-- 200 OK";
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        strStatus = "Bad login or password";
                        isStatus = false;
                    } else {
                        strStatus = "Другая ошибка !!";
                        isStatus = false;
                    }

                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    isStatus = false;
                }
            });*/

        } else {
            strStatus = "Сеть отсутствует";
            isStatus = false;
        }

    }

    /**
     * Метод сохраняет все данные по профилю пользователя в Профит
     *
     * @param userModel
     */
    private void loginSuccess(UserModelRes userModel) {
        mDataManager.getPreferancesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferancesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);
        saveUserFields(userModel);
        saveUserPhoto(userModel);
        saveUserAvatar(userModel);
        isStatus = true;
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

    private void saveUserPhoto(UserModelRes userModel) {
        String photo = userModel.getData().getUser().getPublicInfo().getPhoto();
        mDataManager.getPreferancesManager().saveUserPhoto(photo);
    }

    private void saveUserAvatar(UserModelRes userModel) {
        String avatar = userModel.getData().getUser().getPublicInfo().getAvatar();
        mDataManager.getPreferancesManager().saveUserAvatar(avatar);
    }


    public String getStrStatus() {
        return strStatus;
    }

    public boolean isStatus() {
        return isStatus;
    }
}
