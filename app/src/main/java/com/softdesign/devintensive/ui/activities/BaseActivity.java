package com.softdesign.devintensive.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.redmadrobot.chronos.gui.activity.ChronosActivity;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

/**
 * Created by ant on 26.06.2016.
 */
public class BaseActivity extends ChronosAppCompatActivity {
    static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";
    protected ProgressDialog mProgressDialog;

    public void showProgress(){
        if (mProgressDialog==null){
            mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        } else {
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        }
    }

    public void hideProgress(){
        if (mProgressDialog != null){
            mProgressDialog.hide();
        }
    }

    public void showError(String message, Exception error){
        showToast(message);
        Log.e(TAG, String.valueOf(error));
    }

    public void  showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG);
    }

}
