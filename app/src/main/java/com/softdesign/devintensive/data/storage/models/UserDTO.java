package com.softdesign.devintensive.data.storage.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ant on 15.07.16.
 */
public class UserDTO implements Parcelable {
    private String mPhoto;
    private String mFullName;
    private String mRating;
    private String mCodeLine;
    private String mProject;
    private String mBio;
    private List<String> mPepositories;

    public UserDTO(UserListRes.UserData userData) {
        List<String> repoLink = new ArrayList<>();

        mPhoto = userData.getPublicInfo().getPhoto();
        mFullName = userData.getFullName();
        mRating = String.valueOf(userData.getProfileValues().getRating());
        mCodeLine = String.valueOf(userData.getProfileValues().getLinesCode());
        mProject = String.valueOf(userData.getProfileValues().getProjects());
        mBio = userData.getPublicInfo().getBio();

        for (UserModelRes.Repo gitLink : userData.getRepositories().getRepo()) {
            repoLink.add(gitLink.getGit());
        }
        mPepositories = repoLink;
    }

    protected UserDTO(Parcel in) {
        mPhoto = in.readString();
        mFullName = in.readString();
        mRating = in.readString();
        mCodeLine = in.readString();
        mProject = in.readString();
        mBio = in.readString();
        if (in.readByte() == 0x01) {
            mPepositories = new ArrayList<String>();
            in.readList(mPepositories, String.class.getClassLoader());
        } else {
            mPepositories = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPhoto);
        dest.writeString(mFullName);
        dest.writeString(mRating);
        dest.writeString(mCodeLine);
        dest.writeString(mProject);
        dest.writeString(mBio);
        if (mPepositories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mPepositories);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserDTO> CREATOR = new Parcelable.Creator<UserDTO>() {
        @Override
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };

    public String getPhoto() {
        return mPhoto;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getRating() {
        return mRating;
    }

    public String getCodeLine() {
        return mCodeLine;
    }

    public String getProject() {
        return mProject;
    }

    public String getBio() {
        return mBio;
    }

    public List<String> getPepositories() {
        return mPepositories;
    }
}