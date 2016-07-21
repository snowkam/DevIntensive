package com.softdesign.devintensive.ui.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.network.res.UserListRes;

import retrofit2.Response;

/**
 * Created by ant on 20.07.16.
 */
public final class OperationChrones extends ChronosOperation<SaveDB> {
    Response<UserListRes> response;

    public OperationChrones(Response<UserListRes> response) {
        this.response = response;
    }

    @Nullable
    @Override
    public SaveDB run() {
        SaveDB result = new SaveDB(response);
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
