package com.softdesign.devintensive.data.storage.chrones;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

/**
 * Created by ants on 26.07.2016.
 */
public final class OperationChronesProfilInNetwork extends ChronosOperation<ProfilInNetwork> {
    private String mLogin;
    private String mPassword;

    public OperationChronesProfilInNetwork(String login, String password) {
        this.mLogin = login;
        this.mPassword = password;
    }

    @Nullable
    @Override
    public ProfilInNetwork run() {
        ProfilInNetwork result = new ProfilInNetwork(mLogin, mPassword);
        return result;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<ProfilInNetwork>> getResultClass() {
        return Result.class;

    }

    public final static class Result extends ChronosOperationResult<ProfilInNetwork> {
    }
}
