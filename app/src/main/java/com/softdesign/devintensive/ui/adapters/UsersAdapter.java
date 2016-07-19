package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;

import java.util.List;

/**
 * Created by ant on 14.07.16.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private static final String TAG = ConstantManager.TAG_PREFIX + " UsersAdapter";
    private Context mContext;
    private List<User> mUser;
    private UserViewHolder.CustomClickListener mCustomClickListener;

    public UsersAdapter(List<User> user, UserViewHolder.CustomClickListener customClickListener) {
        mUser = user;
        this.mCustomClickListener = customClickListener;

    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);

        return new UserViewHolder(convertView, mCustomClickListener);

    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {

        final User user = mUser.get(position);
        final String userPhoto;

        if (user.getPhoto().isEmpty()) {
            userPhoto = null;
            Log.e(TAG, "onBindViewHolder : user " + user.getFullName() + " has empty name");
        } else {
            userPhoto = user.getPhoto();
        }

        DataManager.getInstance().getPicasso()
                .load(userPhoto)
                .error(holder.mDummy)
                .placeholder(holder.mDummy)
                .fit()
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.userPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "load from cache");
                    }

                    @Override
                    public void onError() {
                        DataManager.getInstance().getPicasso()
                                .load(userPhoto)
                                .error(holder.mDummy)
                                .placeholder(holder.mDummy)
                                .fit()
                                .centerCrop()
                                .into(holder.userPhoto, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, "Could not fetch image");
                                    }
                                });

                    }
                });

        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getRating()));
        holder.mCodeLine.setText(String.valueOf(user.getCodeLines()));
        holder.mProjects.setText(String.valueOf(user.getProgect()));


        if (user.getBio() == null || user.getBio().isEmpty()) {
            holder.mbio.setVisibility(View.GONE);
        } else {
            holder.mbio.setVisibility(View.VISIBLE);
            holder.mbio.setText(user.getBio());
        }

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected AspectRatioImageView userPhoto;
        protected TextView mFullName, mRating, mCodeLine, mProjects, mbio;
        protected Button mShowMore;
        protected Drawable mDummy;

        private CustomClickListener mListener;


        public UserViewHolder(View itemView, CustomClickListener customClickListener) {
            super(itemView);
            this.mListener = customClickListener;

            userPhoto = (AspectRatioImageView) itemView.findViewById(R.id.user_photo_item_iv);
            mFullName = (TextView) itemView.findViewById(R.id.user_full_name_txt);
            mRating = (TextView) itemView.findViewById(R.id.reting_item_txt);
            mCodeLine = (TextView) itemView.findViewById(R.id.code_item_txt);
            mProjects = (TextView) itemView.findViewById(R.id.project_item_txt);
            mbio = (TextView) itemView.findViewById(R.id.bio_item_txt);
            mShowMore = (Button) itemView.findViewById(R.id.more_info_item_btn);

            mDummy = userPhoto.getContext().getResources().getDrawable(R.drawable.user_bg);
            mShowMore.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onUserClickListener(getAdapterPosition());
            }

        }

        public interface CustomClickListener {
            void onUserClickListener(int position);
        }
    }
}
