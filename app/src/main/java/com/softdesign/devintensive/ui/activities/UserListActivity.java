package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;


import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {


    private static final String SAVEINSTATE_KEY = "SAVEINSTATE_KEY";
    private static final String TAG = ConstantManager.TAG_PREFIX + "UserListActivity";
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<User> mUsers;
    private ArrayList<UserListRes.UserData> mUsersArray;

    private MenuItem mSearchItem;
    private String mQuery;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mDataManager = DataManager.getInstance();
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mRecyclerView = (RecyclerView) findViewById(R.id.user_list);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mHandler = new Handler();



        setupToolbar();
        setupDrawer();
        loadUsersFromDb();
        /* mUsers = new ArrayList<>();
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
         }*/


    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUsersArray = new ArrayList();
        for (int i = 0; i < mUsers.size(); i++){
            mUsersArray.add(mUsers.get(i));
        }

        Log.d("TAG","onSaveInstanceState ========= do"  );
        outState.putSerializable(SAVEINSTATE_KEY, mUsersArray);
        Log.d("TAG","onSaveInstanceState ========= posle"  );
    }*/

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

    private void loadUsersFromDb() {
        //mUsers = mDataManager.getUserListFromDb();

        if (mDataManager.getUserListFromDb().size()==0) {
            showSnackbar("список пользователей не может быть загружен");
        } else {

            showUsers(mDataManager.getUserListFromDb());
        }
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        mSearchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setQueryHint("Введите имя пользователя");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                showUserByQuery(newText);
                return false;
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    private void showUsers(List<User> users) {
        mUsers = users;
        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserClickListener(int position) {

                UserDTO userDTO = new UserDTO(mUsers.get(position));
                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                startActivity(profileIntent);
            }
        });
        mRecyclerView.swapAdapter(mUsersAdapter, false);
    }

    private void showUserByQuery(String query){
        mQuery = query;

        Runnable searchUsers = new Runnable() {
            @Override
            public void run() {
                showUsers(mDataManager.getUserListByName(mQuery));
            }
        };
        mHandler.removeCallbacks(searchUsers);
        mHandler.postDelayed(searchUsers, ConstantManager.SEARCH_DELAY);


    }
}
