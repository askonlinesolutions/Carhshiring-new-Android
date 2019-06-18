package com.carshiring.activities.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.carshiring.R;
import com.carshiring.adapters.CountryAdapter;
import com.carshiring.models.UserDetails;
import com.carshiring.splash.SplashActivity;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.utilities.AppGlobal;
import com.carshiring.utilities.ChoosePhoto;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.carshiring.activities.home.MainActivity.getKeyFromValue;

/**
 * Created by rakhi on 13-03-2018.
 */

public class AccountDetailsActivity extends AppBaseActivity implements CountryAdapter.OnItemClickListener {

    private static final int SELECT_WALL_PICTURE = 101;
    String userId, token, title, ages, Rtitle;
    TinyDB tinyDB;
    Spinner edt_licence_origin;
    UserDetails userDetails = new UserDetails();
    //    UserImage userImage = new UserImage();
    Gson gson = new Gson();
    AppGlobal appGlobal = AppGlobal.getInstancess();
    String s;
    String img;
    long timeInMilliseconds;
    String encodedImage;
    Bitmap bitmap;
    private TinyDB sharedpref;
    private EditText edt_fname, edt_lname, edt_email, edt_phone, edt_zipcode, edt_licence_no,
            edt_city, edt_address;
    private TextView edt_dob, txtCountry;
    private ImageView iv, imgWallEdit, imgUserWall;
    private String str_fname, str_lname, str_dob, str_email, str_phone, str_zipcode, str_licence_no = "", str_licence_origin,
            str_city, str_address, str_image;
    private int SELECT_PROFILE_PICTURE = 100;
    private int mYear, mMonth, mDay;
    private Dialog dialogCountry;
    private boolean isProfilePic;
    private String bitmapString = "a", userChoosenTask;
    private boolean result;
    private ChoosePhoto choosePhoto = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        init();
    }

    private void init() {

        appGlobal.context = getApplicationContext();
        tinyDB = new TinyDB(getApplicationContext());
        String data = tinyDB.getString("login_data");
        userDetails = gson.fromJson(data, UserDetails.class);
        userId = userDetails.getUser_id();

        iv = findViewById(R.id.update_user_image);
        txtCountry = findViewById(R.id.countryList);
        edt_fname = findViewById(R.id.update_user_fname);
        edt_lname = findViewById(R.id.update_user_lname);
        edt_dob = findViewById(R.id.update_user_dob);
        edt_email = findViewById(R.id.update_user_email);
        edt_phone = findViewById(R.id.update_user_phone);
        edt_zipcode = findViewById(R.id.update_user_zip);
        edt_licence_no = findViewById(R.id.update_user_licence);
        edt_licence_origin = findViewById(R.id.update_user_licnce_origin);
        edt_city = findViewById(R.id.update_user_city);
        edt_address = findViewById(R.id.update_user_address);
        imgWallEdit = findViewById(R.id.imgWallEdit);
        imgUserWall = findViewById(R.id.user_wall);
        final ArrayList<String> county = new ArrayList<>(SplashActivity.counrtyList);
        txtCountry.setOnClickListener(view -> countryDialog(county));
        getProfile();

    }

    @Override
    protected void onResume() {
        super.onResume();

        setMyToolBar();
    }

    private void countrySpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_row, SplashActivity.counrtyList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_row);
        edt_licence_origin.setAdapter(arrayAdapter);
        if (str_licence_origin != null) {
            s = SplashActivity.country.get(str_licence_origin);
            int spinnerPosition = arrayAdapter.getPosition(s);
            edt_licence_origin.setSelection(spinnerPosition);
        }

        edt_licence_origin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                str_licence_origin = (String) getKeyFromValue(SplashActivity.country, item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setMyToolBar() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
            actionBar.setTitle(getResources().getString(R.string.update_profile));
        }
    }

    public void getProfile() {
        Utility.showLoading(AccountDetailsActivity.this, getResources().getString(R.string.loading));
        RetroFitApis fitApis = RetrofitApiBuilder.getCargHiresapis();
        final Call<ApiResponse> walList = fitApis.profile(userId);
        walList.enqueue(new Callback<ApiResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response != null && response.body() != null) {
                    if (response.body().status) {
                        UserDetails userDetails;
                        userDetails = response.body().response.user_detail;
                        String logindata = gson.toJson(userDetails);
                        Log.d("TAG", "onResponse: " + response.body().response.user_detail);
                        appGlobal.setLoginData(logindata);
                        String st = appGlobal.getUser_id();
                        edt_email.setText(userDetails.getUser_email());
                        edt_phone.setText(userDetails.getUser_phone());
                        if (userDetails.getUser_license_no() != null)
                            edt_licence_no.setText(userDetails.getUser_license_no());
                        edt_fname.setText(userDetails.getUser_name());
                        if (userDetails.getUser_dob() != null && !userDetails.getUser_dob().equalsIgnoreCase("0000-00-00")) {
                            edt_dob.setText(userDetails.getUser_dob());
                        }
                        str_licence_origin = (String) userDetails.getUser_country();
                        String s = SplashActivity.country.get(str_licence_origin);
                        txtCountry.setText(s);
                        edt_zipcode.setText(userDetails.getUser_zipcode());
                        edt_city.setText((String) userDetails.getUser_city());
                        edt_address.setText(userDetails.getUser_address());
                        if (userDetails.getUser_lname() != null) {
                            edt_lname.setText(userDetails.getUser_lname());
                        }
                        if (userDetails.getUser_image() != null && userDetails.getUser_image().length() > 1) {
                            String url = RetrofitApiBuilder.IMG_BASE_URL + userDetails.getUser_image();
                            Log.d("TAG", "onResponse: imagewall" + url);
                          /*  GetImage task = new GetImage();
                            // Execute the task
                            task.execute(new String[] { url });*/

                            Glide.with(AccountDetailsActivity.this)
                                    .asBitmap()
                                    .load(url)
                                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                    .apply(RequestOptions.skipMemoryCacheOf(true))
                                    .into(new BitmapImageViewTarget(iv) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            iv.setImageBitmap(resource);
                                            super.setResource(resource);
                                        }
                                    });

                        }

                        if (userDetails.getUser_cover() != null && userDetails.getUser_cover().length() > 0) {
                            String url = RetrofitApiBuilder.IMG_BASE_URL + userDetails.getUser_cover();
                          /*  GetImageWall task = new GetImageWall();
                            // Execute the task
                            task.execute(new String[] { url });
*/
                            Glide.with(AccountDetailsActivity.this)
                                    .asBitmap()
                                    .load(url)
                                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                    .apply(RequestOptions.skipMemoryCacheOf(true))
                                    .into(new BitmapImageViewTarget(imgUserWall) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            imgUserWall.setImageBitmap(resource);
                                            super.setResource(resource);
                                        }
                                    });

                        }
                        Utility.hidepopup();
                        countrySpinner();
                    } else {
                        Utility.message(getApplicationContext(), getResources().getString(R.string.something_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(AccountDetailsActivity.this, "" + getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

                //   Utility.message(getApplicationContext(), getResources().getString(R.string.check_internet));
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    public void cancel(View view) {
        finish();
    }

    public void update_profile(View view) {

        str_fname = edt_fname.getText().toString().trim();
        str_lname = edt_lname.getText().toString().trim();
        str_email = edt_email.getText().toString().trim();
        str_phone = edt_phone.getText().toString().trim();
        str_zipcode = edt_zipcode.getText().toString().trim();
        str_licence_no = edt_licence_no.getText().toString().trim();
        //  str_licence_origin = edt_licence_origin.getText().toString().trim();
        str_city = edt_city.getText().toString().trim();
        str_address = edt_address.getText().toString().trim();
        str_dob = edt_dob.getText().toString().trim();

        if (!str_fname.isEmpty() && str_fname.length() > 3) {
            if (!str_lname.isEmpty() && str_lname.length() > 3) {
                if (Utility.checkemail(str_email)) {
                    if (Utility.checkphone(str_phone)) {
                        if (str_dob != null && !str_dob.isEmpty()) {
                            update_profile_1(userId, str_fname);
                        } else {
                            Utility.message(getApplication(), getResources().getString(R.string.please_enter_dob));
                        }
                    } else {
                        Utility.message(getApplication(), getResources().getString(R.string.please_enter_valid_phone_number));
                    }
                } else {
                    Utility.message(getApplication(), getResources().getString(R.string.please_enter_valid_email));
                }
            } else {
                Utility.message(getApplication(), getResources().getString(R.string.please_enter_last_name));
            }
        } else {
            Utility.message(getApplication(), getResources().getString(R.string.please_enter_first_name));
        }
    }

    private void update_profile_1(String userid, String str_fname) {
        if (!Utility.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(AccountDetailsActivity.this, getResources().getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Utility.showloadingPopup(this);
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis();
        Call<ApiResponse> responseCall = retroFitApis.updateprofile(userid, str_fname, str_lname, str_email, str_phone,
                str_zipcode, str_licence_no, str_licence_origin, str_dob, str_city, str_address);
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utility.hidepopup();
                Log.d("TAG", "onResponse: " + response.body().msg);
                assert response.body() != null;
                if (response.body().status) {
                    Utility.message(getApplicationContext(), response.body().msg);
//                    String logindata=gson.toJson(response.body().response.userdetail);
                    userDetails = response.body().response.user_detail;
                    String logindata = gson.toJson(userDetails);
                    appGlobal.setLoginData(logindata);
                    finish();

                } else {
                    Utility.message(getApplicationContext(), response.body().msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.hidepopup();
                Utility.message(getApplicationContext(), getResources().getString(R.string.no_internet_connection));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void dob_pick(View view) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = Calendar.getInstance().get(Calendar.YEAR) - 22;
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        timeInMilliseconds = Utility.getTimeDate(mYear + "-" + mMonth + "-" + mDay);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        str_dob = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                     /*   SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date d=dateFormat.parse(str_dob);
                            System.out.println("DATE"+d);
                            Log.d("TAG", "onDateSet: "+d);
                            str_dob = dateFormat.format(d);
                            System.out.println("Formated"+str_dob);
                        }
                        catch(Exception e) {
                            //java.text.ParseException: Unparseable date: Geting error
                            System.out.println("Excep"+e);
                        }
*/
                        edt_dob.setText(str_dob);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(timeInMilliseconds);
        datePickerDialog.show();

    }

    private void countryDialog(ArrayList<String> county) {
        dialogCountry = new Dialog(AccountDetailsActivity.this);
        dialogCountry.setContentView(R.layout.popup_country_code);
        dialogCountry.setCanceledOnTouchOutside(false);
        dialogCountry.setCancelable(false);
        dialogCountry.show();
        RecyclerView recyclerView = dialogCountry.findViewById(R.id.popupCountryRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        CountryAdapter countryAdapter = new CountryAdapter(county, getApplicationContext(), this);
        recyclerView.setAdapter(countryAdapter);
    }

    public void upload_my_image(View view) {
     /*   if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AccountDetailsActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA);
        } else {
            Log.e("DB", "PERMISSION GRANTED");
            result = true;
        }
        SELECT_PROFILE_PICTURE = 100;*/
        /*selectImage(SELECT_PROFILE_PICTURE);*/
        isProfilePic = true;
        choosePhoto = new ChoosePhoto(this);

    }

    public void upload_wall(View view) {
     /*   if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AccountDetailsActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA);
        } else {
            Log.e("DB", "PERMISSION GRANTED");
            result = true;
        }
        SELECT_PROFILE_PICTURE = 100;*/
        /*selectImage(SELECT_PROFILE_PICTURE);*/
        isProfilePic = false;
        choosePhoto = new ChoosePhoto(this);

    }

    /* Choose an image from Gallery */
    void openImageChooser(int SELECT_PICTURE) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void update_user_wall(String image) {

        if (!Utility.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(AccountDetailsActivity.this, getResources().getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Utility.showLoading(AccountDetailsActivity.this, getResources().getString(R.string.loading));
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis();
        Call<ApiResponse> responseCall = retroFitApis.update_user_wall(userId, image);
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("TAG", "onResponse: data" + gson.toJson(response.body()));
                if (response.body() != null) {
                    if (response.body().status) {
                        Utility.hidepopup();
                        Toast.makeText(getApplicationContext(), response.body().msg, Toast.LENGTH_SHORT).show();
//                    getProfile();
                    } else {
                        if (response.body().msg != null)
                            Utility.message(getApplicationContext(), response.body().msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.hidepopup();
                Log.d("TAG", "onFailure: " + t.getMessage());
                Utility.message(getApplicationContext(), getResources().getString(R.string.no_internet_connection));
            }
        });

    }

    private void update_user_pic(String image) {

        if (!Utility.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(AccountDetailsActivity.this, getResources().getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Utility.showLoading(AccountDetailsActivity.this, getResources().getString(R.string.loading));
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis();
        Call<ApiResponse> responseCall = retroFitApis.update_user_DP(userId, image);
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().status == true) {
                    Log.d("TAG", "onResponse: " + response.body().message);
                    Utility.hidepopup();
                    Toast.makeText(getApplicationContext(), response.body().msg, Toast.LENGTH_SHORT).show();
                    /*getProfile();*/
                } else {
                    Utility.message(getApplicationContext(), response.body().message);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.hidepopup();
                Utility.message(getApplicationContext(), getResources().getString(R.string.no_internet_connection));
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(AccountDetailsActivity.this.getApplicationContext().getContentResolver(), data.getData());
                Uri tempUri = getImageUri(getApplicationContext(), bm);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));

                System.out.println(finalFile + "");

                String filePath = finalFile + "";
                Log.d("MyImagePath", filePath.substring(filePath.lastIndexOf("/") + 1));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        bitmap = bm;
        bitmapString = Utility.BitMapToString(bm);
        if (SELECT_PROFILE_PICTURE == 100) {
            iv.setImageBitmap(bm);
            update_user_pic(bitmapString);
        } else if (SELECT_PROFILE_PICTURE == 200) {

            imgUserWall.setImageBitmap(bm);
            update_user_wall(bitmapString);
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ChoosePhoto.SELECT_PICTURE_CAMERA && requestCode == ChoosePhoto.CHOOSE_PHOTO_INTENT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                choosePhoto.showAlertDialog();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ChoosePhoto.CHOOSE_PHOTO_INTENT) {
                if (data != null && data.getData() != null) {
                    choosePhoto.handleGalleryResult(data);
                } else {
                    choosePhoto.handleCameraResult(choosePhoto.getCameraUri());
                }
            } else if (requestCode == ChoosePhoto.SELECTED_IMG_CROP) {
                if (isProfilePic) {
                    iv.setImageURI(choosePhoto.getCropImageUrl());
                    iv.invalidate();
                    BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
                    bitmap = drawable.getBitmap();
                    bitmapString = Utility.BitMapToString(bitmap);
                    update_user_pic(bitmapString);
                } else {
                    imgUserWall.setImageURI(choosePhoto.getCropImageUrl());
                    imgUserWall.invalidate();
                    BitmapDrawable drawable = (BitmapDrawable) imgUserWall.getDrawable();
                    bitmap = drawable.getBitmap();
                    bitmapString = Utility.BitMapToString(bitmap);
                    update_user_wall(bitmapString);
                }

            }
        }

    }

    @Override
    public void onItemClickCategory(int position) {
        String item = SplashActivity.counrtyList.get(position);
        str_licence_origin = (String) getKeyFromValue(SplashActivity.country, item);
        txtCountry.setText(item);
        dialogCountry.dismiss();
    }
}
