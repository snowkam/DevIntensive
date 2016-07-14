package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by ant on 14.07.16.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    Context mContext;
    List<UserListRes.UserData> mUser;

    public UsersAdapter(List<UserListRes.UserData> user) {
        mUser = user;
        Log.d("TAG", "UsersAdapter  ========++++++++++++++++++++++++++++++++ "  );
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        Log.d("TAG", "UsersAdapter  ========onCreateViewHolder ++++++++++++++++++++++++++++++++ "  );
        return new UserViewHolder(convertView);

    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserListRes.UserData user = mUser.get(position);

        Picasso.with(mContext)
                .load(user.getPublicInfo().getPhoto())
                .placeholder(mContext.getResources().getDrawable(R.drawable.user_photo))
                .into(holder.userPhoto);

        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getProfileValues().getRating()));
        holder.mCodeLine.setText(String.valueOf(user.getProfileValues().getLinesCode()));
        holder.mProjects.setText(String.valueOf(user.getProfileValues().getProjects()));

        Log.d("TAG", "UsersAdapter onBindViewHolder ======== " + user.getFullName() );

        if (user.getPublicInfo().getBio()==null || user.getPublicInfo().getBio().isEmpty()){
            holder.mbio.setVisibility(View.GONE);
        }else {
            holder.mbio.setVisibility(View.VISIBLE);
            holder.mbio.setText(user.getPublicInfo().getBio());
        }

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        protected AspectRatioImageView userPhoto;
        protected TextView mFullName, mRating, mCodeLine, mProjects, mbio;
        protected Button mMoreInfo;

        public UserViewHolder(View itemView) {
            super(itemView);

            userPhoto = (AspectRatioImageView) itemView.findViewById(R.id.user_photo_item_iv);
            mFullName =(TextView) itemView.findViewById(R.id.user_full_name_txt);
            mRating =(TextView) itemView.findViewById(R.id.reting_item_txt);
            mCodeLine =(TextView) itemView.findViewById(R.id.code_item_txt);
            mProjects =(TextView) itemView.findViewById(R.id.project_item_txt);
            mbio =(TextView) itemView.findViewById(R.id.bio_item_txt);
           /* mMoreInfo = (Button) itemView.findViewById(R.id.more_info_item_btn);*/

        }
    }
}
