package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import retrofit2.Call;

/**
 * Created by ant on 27.06.16.
 */
public class DataManager {
    private static DataManager INSTANCE = null;


    private Context mContext;
    private PreferancesManager mPreferancesManager;
    private RestService mRestService;

    public DataManager() {
        this.mPreferancesManager = new PreferancesManager();

        this.mRestService = ServiceGenerator.createService(RestService.class);
    }

    public  static DataManager getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
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




    //endregion

}
