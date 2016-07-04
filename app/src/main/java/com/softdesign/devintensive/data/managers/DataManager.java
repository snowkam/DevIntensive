package com.softdesign.devintensive.data.managers;

/**
 * Created by ant on 27.06.16.
 */
public class DataManager {
    private static DataManager INSTANCE = null;



    private PreferancesManager mPreferancesManager;

    public DataManager() {
        this.mPreferancesManager = new PreferancesManager();
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

}
