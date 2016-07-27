package com.softdesign.devintensive.data.storage.chrones;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.network.res.UserListRes;

import retrofit2.Response;

/**
 * Created by ant on 20.07.16.
 */
public final class OperationChronesSaveDB extends ChronosOperation<SaveDB> {
    Response<UserListRes> response;

    /*public OperationChronesSaveDB(Response<UserListRes> response) {
        this.response = response;
    }*/

    @Nullable
    @Override
    public SaveDB run() {
        SaveDB result = new SaveDB();
        return result;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<SaveDB>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<SaveDB> {

    }
}
