package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RegexInputFilter;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";

    final String REGEX_PHONE = "\\+7\\(\\d{3}\\)\\d{3}\\-\\d{2}\\-\\d{2,11}";
    final String REGEX_EMAIL = "\\w{3,20}\\@\\w{2,}\\.\\w{2}";
    final String REGEX_VK = "\\w{2}\\.\\w{3}\\/\\w{4,}";
    final String REGEX_GITHUB = "\\w{6}\\.\\w{3}\\/\\w{4,}";

    private DataManager mDataManager;
    private int mCurrentEditMode = 0;

    private int mColorMode;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;

    private EditText mUserPhone, mUserEmail, mUserVk, mUserGit, mUserBio;

    private ImageView mCallImg, mSendEmail, mLookVk, mLookGithub;

    private List<EditText> mUserInfoViews;
    private TextView mUserMailText;

    private NavigationView mNavigationView;

    private RelativeLayout mProfilePlaceholder;
    private CollapsingToolbarLayout mCollapsingToolbar;

    private AppBarLayout.LayoutParams mAppBarParams = null;
    private AppBarLayout mAppBarLayout;
    private ImageView mProfileImage;


    private View mHeader;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mHeader = mNavigationView.getHeaderView(0);

        mDataManager = DataManager.getInstance();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mProfilePlaceholder = (RelativeLayout) findViewById(R.id.profile_placeholder);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
        //находим текседит и задаем ему формат Телефона
        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserPhone.setFilters(new InputFilter[]{new RegexInputFilter(REGEX_PHONE)});
        mUserPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 3) {
                    mUserPhone.setText("+7(");
                    mUserPhone.setSelection(3);
                }
                if (editable.length() == 6) {
                    mUserPhone.setText(editable + ")");
                    mUserPhone.setSelection(mUserPhone.getText().length());
                }
                if (editable.length() == 10) {
                    mUserPhone.setText(editable + "-");
                    mUserPhone.setSelection(mUserPhone.getText().length());
                }
                if (editable.length() == 13) {
                    mUserPhone.setText(editable + "-");
                    mUserPhone.setSelection(mUserPhone.getText().length());
                }
            }
        });
        //находим текседит и задаем ему формат email
        mUserEmail = (EditText) findViewById(R.id.email_et);
        mUserEmail.setFilters(new InputFilter[]{new RegexInputFilter(REGEX_EMAIL)});


        //находим текседит и задаем ему формат vk
        mUserVk = (EditText) findViewById(R.id.vkcom_et);
        mUserVk.setFilters(new InputFilter[]{new RegexInputFilter(REGEX_VK)});
        mUserVk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "editable.length() =======" + mUserVk.getText().toString());
                if (mUserVk.getText().length() < 7) {
                    mUserVk.setText("vk.com/");
                    mUserVk.setSelection(7);
                }
            }
        });

        //находим текседит и задаем ему формат github
        mUserGit = (EditText) findViewById(R.id.github_et);
        mUserGit.setFilters(new InputFilter[]{new RegexInputFilter(REGEX_GITHUB)});
        mUserGit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mUserGit.getText().length() < 11) {
                    mUserGit.setText("github.com/");
                    mUserGit.setSelection(11);
                }
            }
        });


        mUserBio = (EditText) findViewById(R.id.bio_et);

        mUserMailText = (TextView) findViewById(R.id.user_email_txt);

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserEmail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);


        mFab.setOnClickListener(this);

        mProfilePlaceholder.setOnClickListener(this);

        //обработка кнопочек
        mCallImg = (ImageView) findViewById(R.id.call_img);
        mCallImg.setOnClickListener(this);
        mSendEmail = (ImageView) findViewById(R.id.send_email_img);
        mSendEmail.setOnClickListener(this);
        mLookVk = (ImageView) findViewById(R.id.vkcom_img);
        mLookVk.setOnClickListener(this);
        mLookGithub =(ImageView) findViewById(R.id.github_img);
        mLookGithub.setOnClickListener(this);


        setupToolbar();
        setupDrawer();
        loadUserInfoValue();
        setupAvatarNavigationDrawer();
        Picasso.with(this)
                .load(mDataManager.getPreferancesManager().loadUserPhoto())
                .placeholder(R.drawable.user_photo)
                .into(mProfileImage);


        if (savedInstanceState == null) {
            // первый запуск активити
            showSnackbar("активити запустилась впервые");
        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        saveUserInfoValue();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                break;
            case R.id.profile_placeholder:
                // сделать выбор откуда брать фото
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.call_img:
                //сделать звонок
                callPhone();
                break;
            case R.id.send_email_img:
                //отправить почту
                sendEmail();
                break;
            case R.id.vkcom_img:
                //смотрим vk.com
                if (mUserVk.getText().toString().matches(REGEX_VK)){
                    lookAdressInternet("https://" + mUserVk.getText().toString());
                } else showSnackbar(getString(R.string.error_nourl_internet));

                break;
            case R.id.github_img:
                //смотрим github.com
                if (mUserGit.getText().toString().matches(REGEX_GITHUB)){
                    lookAdressInternet("https://" + mUserGit.getText().toString());
                } else showSnackbar(getString(R.string.error_nourl_internet));

                break;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);

    }

    /**
     * вспомогательный метод для инициализации(запуска) Snackbar-а
     *
     * @param message - любое стринговое значение которое будет отображатся в Snackbar
     */
    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }


    /**
     * метод построитель, инициализирует(устанавливает) на ActionBar
     * кнопку home
     */

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * метод инициализирует обработчик события  OnNavigationItemSelectedListener на mNavigationView
     * и обрабатывает событие выполняет действие. (в нашем случаи закрывает закрывает mNavigationView)
     */

    private void setupDrawer() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

    }

    /**
     * Переключает режим редактировать
     *
     * @param mode если 1 - режим педактирования , 0 режим просмотра
     */
    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
                mUserPhone.requestFocus(); //устанавливаем фокус на поле телефон
                mUserPhone.setSelection(mUserPhone.getText().length()); // и устанавливаем курсор в конец строки
            }
            showProfilePlaceHolder();
            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                saveUserInfoValue();
            }
            hideProfilePlaceHolder();
            unlockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
        }

    }

    /**
     * метод для загрузки данных о пользователя из Preferances
     */

    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPreferancesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    /**
     * метод для записи данных пользователя в Preferances
     */
    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();

        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferancesManager().saveUserProfileData(userData);

    }

    /**
     * @param keyCode
     * @param event
     * @return
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //если NavigationDrawer открыт закрываем его иначе закрываем активити
            if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
                mNavigationDrawer.closeDrawer(GravityCompat.START);
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * вспомогательный метод для скругления изображения
     */
    private void setupAvatarNavigationDrawer() {
        ImageView mUserAvatarImg = (ImageView) mHeader.findViewById(R.id.user_avatar_img);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.user_avatar);

        mUserAvatarImg.setImageDrawable(new RoundedAvatarDrawable(bitmap));
    }

    /**
     * Получение результата от других активити
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
        }


    }


    /**
     * Загрузка фотографии из галлереи
     */
    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_chose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    /**
     * загрузка фото с камеры
     */
    private void loadPhotoFromCamera() {
        //if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        //       && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED)) {
        Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            mPhotoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
            //// TODO: 01.07.16  обработать ошибку
        }
        if (mPhotoFile != null) {
            //  передать фото в интент
            takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
            startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
        }
        //}

    }

    private void hideProfilePlaceHolder() {
        mProfilePlaceholder.setVisibility(View.GONE);

    }

    private void showProfilePlaceHolder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);

    }

    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItem = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
                                // галлерея
                                loadPhotoFromGallery();
                                //showSnackbar("загрузить из галлереи");
                                break;
                            case 1:
                                //  камера
                                loadPhotoFromCamera();
                                //showSnackbar("сфотографировать");
                                break;
                            case 2:
                                // отмена
                                dialog.cancel();
                                //showSnackbar("отмена");
                                break;
                        }
                    }
                });
                return builder.create();

            default:
                return null;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storegeDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storegeDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    /**
     * метод выводит изображение в ImageView и сохраняет его в Preferances
     * @param selectedImage - путь изображения полученный из галлереи или с камеры
     */
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this).load(selectedImage).into(mProfileImage);
        mDataManager.getPreferancesManager().saveUserPhoto(selectedImage);

    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }


    /**
     * метод вызывает намерения звонка и предает ему интент
     */
    private void callPhone(){
        if (mUserPhone.getText().toString().matches(REGEX_PHONE)) {
            Intent openPhone = new Intent(Intent.ACTION_DIAL);
            openPhone.setData(Uri.parse("tel:"+mUserPhone.getText().toString()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(openPhone);
        } else showSnackbar(getString(R.string.error_call_phone));
    }

    /**
     * метод вызывает намериния почтовых клиентов и отправляет в него нашь интент.
     */
    private void sendEmail(){
        if (mUserEmail.getText().toString().matches(REGEX_EMAIL)) {
            Intent sendEmail = new Intent(Intent.ACTION_SEND);
            sendEmail.setType("plain/text");
            sendEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {mUserEmail.getText().toString()});
            startActivity(sendEmail);
        } else {
            showSnackbar(getString(R.string.error_send_email));
        }
    }

    /**
     * Метод ищет и запускает Намериния для просмотра ссылки в интернет
     * @param url - Строковая ссылка (url страници)
     */
    private void lookAdressInternet(String url){
        Intent webpage = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(webpage);
    }


}
