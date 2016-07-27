package com.softdesign.devintensive.data.storage.chrones;

import android.util.Log;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.softdesign.devintensive.utils.NtworksStatusChecker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by ant on 20.07.16.
 */
public class SaveDB  {

    private DataManager mDataManager;
    private String strStatus;
    private boolean isStatus = false;

    //Response<UserListRes> response;
    List<Repository> allRepositories;
    List<User> allUsers;

    public SaveDB() {

        main();
    }

    private void main(){
        allRepositories = new ArrayList<Repository>();
        allUsers = new ArrayList<User>();
        mDataManager = DataManager.getInstance();
        if (NtworksStatusChecker.isNetworkAvailable(DevintensiveApplication.getContext())) {

            Call<UserListRes> call = mDataManager.getUserListFromNetwork();
            try {

                Response<UserListRes> response = call.execute();
                if (response.code() == 200) {
                    isStatus = true;
                    strStatus = "<-- 200 OK";
                    for (UserListRes.UserData userRes : response.body().getData()) {
                        allRepositories.addAll(getRepoListfromUserRes(userRes));
                        allUsers.add(new User(userRes));
                    }
                }  else {
                    strStatus = "Список пользователей не может быть получен";
                    isStatus = false;
                }

            }catch (Exception e) {
                e.printStackTrace();
                strStatus = "Что-то пошло не так смотри  SaveDB !!";
                isStatus = false;
            }
        } else {
            strStatus = "Сеть отсутствует";
            isStatus = false;
        }



    }

    private List<Repository> getRepoListfromUserRes(UserListRes.UserData userData) {
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();
        for (UserModelRes.Repo repositoryRes : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repositoryRes, userId));
        }
        return repositories;
    }

    public List<Repository> getAllRepositories() {
        return allRepositories;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public boolean isStatus() {
        return isStatus;
    }
}
