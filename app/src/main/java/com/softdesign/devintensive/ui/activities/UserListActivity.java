package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    private static final String SAVEINSTATE_KEY = "SAVEINSTATE_KEY";
    private static final String TAG = ConstantManager.TAG_PREFIX + "UserListActivity";
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<UserListRes.UserData> mUsers;
    private ArrayList<UserListRes.UserData> mUsersArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mDataManager = DataManager.getInstance();
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mRecyclerView =(RecyclerView) findViewById(R.id.user_list);

        LinearLayoutManager gridLayoutManager =  new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        setupToolbar();
        setupDrawer();
         mUsers = new ArrayList<>();
        if (savedInstanceState !=null) {
            mUsersArray = (ArrayList<UserListRes.UserData>) savedInstanceState.getSerializable(SAVEINSTATE_KEY);
             for (int i = 0 ; i< mUsersArray.size(); i++){
                mUsers.add(mUsersArray.get(i));
             }
        }

        if (mUsers.isEmpty()){
        loadUsers();
        } else {
            creatAdapter();
         }
        

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUsersArray = new ArrayList();
        for (int i = 0; i < mUsers.size(); i++){
            mUsersArray.add(mUsers.get(i));
        }

        Log.d("TAG","onSaveInstanceState ========= do"  );
        outState.putSerializable(SAVEINSTATE_KEY, mUsersArray);
        Log.d("TAG","onSaveInstanceState ========= posle"  );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void loadUsers() {
        Call<UserListRes> call =mDataManager.getUserList();

        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {

                //// TODO: 14.07.16  обработка кодов возвращения 200 300 400 .....
                try {
                    mUsers = response.body().getData();
                    creatAdapter();
                } catch (NullPointerException e){
                    Log.d(TAG, e.toString() );
                }


            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                //// TODO: 14.07.16 обработка ошибок

            }
        });

        
    }

    private void creatAdapter(){
        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserClickListener(int position) {

                UserDTO userDTO = new UserDTO(mUsers.get(position));
                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                startActivity(profileIntent);
            }
        });
        mRecyclerView.setAdapter(mUsersAdapter);
    }

    private void setupDrawer() {
        //// TODO: 14.07.16 реализовать переход в другую активити по элементу меню
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
