package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.PicassoChache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by ant on 27.06.16.
 */
public class DataManager {
    private static DataManager INSTANCE = null;

    private Picasso mPicasso;


    private Context mContext;
    private PreferancesManager mPreferancesManager;
    private RestService mRestService;

    private DaoSession mDaoSession;


    public DataManager() {
        this.mContext = DevintensiveApplication.getContext();
        this.mPreferancesManager = new PreferancesManager();
        this.mRestService = ServiceGenerator.createService(RestService.class);
        this.mPicasso = new PicassoChache(mContext).getPicassoInstance();
        this.mDaoSession = DevintensiveApplication.getDaoSession();

    }

    public  static DataManager getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    public PreferancesManager getPreferancesManager() {
        return mPreferancesManager;
    }

    //public Context getContext(){
    //    return m
   // }

    //region
    public Call<UserModelRes> loginUser( UserLoginReq userLoginReq) {
        return mRestService.loginUser( userLoginReq);
    }

    public Call<UserListRes> getUserListFromNetwork() {
        return mRestService.getUserList();
    }

    public Call<UserModelRes> loginToken() {
        return mRestService.loginToken();
    }


    public List<User> getUserListFromDb(){
        List<User> userList = new ArrayList<>();

        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();


        } catch (Exception e){
            e.printStackTrace();
        }


        return userList;
    }


    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public List<User> getUserListByName(String query) {
        List<User> userList = new ArrayList<>();
        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.Rating.gt(0), UserDao.Properties.SearchName.like("%" + query.toUpperCase()+"%"))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();

        } catch (Exception e){
            e.printStackTrace();
        }

        return userList;

    }
}
