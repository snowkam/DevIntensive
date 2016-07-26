package com.softdesign.devintensive.data.storage.chrones;

import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by ant on 20.07.16.
 */
public class SaveDB  {

    Response<UserListRes> response;
    List<Repository> allRepositories;
    List<User> allUsers;

    public SaveDB(Response<UserListRes> response) {
        this.response = response;
        main();
    }

    private void main(){
       allRepositories = new ArrayList<Repository>();
       allUsers = new ArrayList<User>();

        for (UserListRes.UserData userRes : response.body().getData()) {

            allRepositories.addAll(getRepoListfromUserRes(userRes));
            allUsers.add(new User(userRes));

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
}
