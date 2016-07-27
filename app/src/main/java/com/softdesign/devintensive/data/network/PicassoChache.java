package com.softdesign.devintensive.data.network;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

/**
 * Created by ant on 18.07.16.
 */
public class PicassoChache {
    private Context mContext;
    private Picasso mPicassoInstance;

    public PicassoChache(Context context) {
        this.mContext = context;
        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(context, Integer.MAX_VALUE);
        Picasso.Builder  builder = new Picasso.Builder(context);
        builder.downloader(okHttp3Downloader);

        mPicassoInstance = builder.build();
        Picasso.setSingletonInstance(mPicassoInstance);
    }

    public Picasso getPicassoInstance(){
        if (mPicassoInstance == null) {
            new PicassoChache(mContext);
            return  mPicassoInstance;
        }
        return  mPicassoInstance;
    }
}
