package com.carshiring.utilities;

import android.app.Application;
import android.content.Context;

import com.mukesh.tinydb.TinyDB;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class AppGlobal extends Application {

    public static final int ERROR_CODE = 102;
    private static AppGlobal appGlobal ;
    public Context context;
    TinyDB sharedpref;

    public String user_id="" ;
    public String user_name="" ;
    public String user_lname="" ;
    public String user_username="" ;
    public String user_email="" ;
    public String user_countrycode="" ;
    public String user_phone="" ;
    public String user_password="" ;
    public String user_zipcode="" ;
    public String user_address="" ;
    public String user_empcode="" ;
    public String user_image ="";
    public String user_attachment="" ;
    public String user_company_id="" ;
    public String user_branch_id="" ;
    public String user_type="" ;
    public String user_department_id="" ;
    public String user_language_id="" ;
    public String user_status ="";
    public String loginData="";

    public String getLoginData() {
        return loginData;
    }

    public void setLoginData(String loginData) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("login_data",loginData);
        this.loginData = loginData;
    }

    public void setUser_id(String user_id) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("userid",user_id);
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_name",user_name);
        this.user_name = user_name;
    }

    public void setUser_lname(String user_lname) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_lname",user_lname);
        this.user_lname = user_lname;
    }

    public void setUser_username(String user_username) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_username",user_username);
        this.user_username = user_username;
    }

    public void setUser_email(String user_email) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_email",user_email);
        this.user_email = user_email;
    }

    public void setUser_countrycode(String user_countrycode) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_countrycode",user_countrycode);
        this.user_countrycode = user_countrycode;
    }

    public void setUser_phone(String user_phone) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_phone",user_phone);
        this.user_phone = user_phone;
    }

    public void setUser_password(String user_password) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_password",user_password);
        this.user_password = user_password;
    }

    public void setUser_zipcode(String user_zipcode) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_zipcode",user_zipcode);
        this.user_zipcode = user_zipcode;
    }

    public void setUser_address(String user_address) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_address",user_address);
        this.user_address = user_address;
    }

    public void setUser_empcode(String user_empcode) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_empcode",user_empcode);
        this.user_empcode = user_empcode;
    }

    public void setUser_image(String user_image) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_image",user_image);
        this.user_image = user_image;
    }

    public void setUser_attachment(String user_attachment) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_attachment",user_attachment);
        this.user_attachment = user_attachment;
    }

    public void setUser_status(String user_status) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_status",user_status);
        this.user_status = user_status;
    }

    public void setUser_company_id(String user_company_id) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_company_id",user_company_id);
        this.user_company_id = user_company_id;
    }

    public void setUser_branch_id(String user_branch_id) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_branch_id",user_branch_id);
        this.user_branch_id = user_branch_id;
    }

    public void setUser_type(String user_type) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_type",user_type);
        this.user_type = user_type;
    }

    public void setUser_department_id(String user_department_id) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_department_id",user_department_id);
        this.user_department_id = user_department_id;
    }

    public void setUser_language_id(String user_language_id) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_language_id",user_language_id);
        this.user_language_id = user_language_id;
    }



    public String getUser_id() {
        return user_id;
    }
    public String getUser_name() {
        return user_name;
    }
    public String getUser_lname() {
        return user_lname;
    }
    public String getUser_username() {
        return user_username;
    }
    public String getUser_email() {
        return user_email;
    }
    public String getUser_countrycode() {
        return user_countrycode;
    }
    public String getUser_phone() {
        return user_phone;
    }
    public String getUser_password() {
        return user_password;
    }
    public String getUser_zipcode() {
        return user_zipcode;
    }
    public String getUser_address() {
        return user_address;
    }
    public String getUser_empcode() {
        return user_empcode;
    }
    public String getUser_image() {
        return user_image;
    }
    public String getUser_attachment() {
        return user_attachment;
    }
    public String getUser_company_id() {
        return user_company_id;
    }
    public String getUser_branch_id() {
        return user_branch_id;
    }
    public String getUser_type() {
        return user_type;
    }
    public String getUser_department_id() {
        return user_department_id;
    }
    public String getUser_language_id() {
        return user_language_id;
    }
    public String getUser_status() {
        return user_status;
    }

    public void resetAllData(){
        setUser_id("");
        setUser_name("");
        setUser_address("");
        setUser_attachment("");
        setUser_branch_id("");
        setUser_company_id("");
        setUser_countrycode("");
        setUser_department_id("");
        setUser_empcode("");
        setUser_email("");
        setUser_image("");
        setUser_lname("");
        setUser_language_id("");
        setUser_phone("");
        setUser_password("");
        setUser_status("");
        setUser_type("");
        setUser_username("");
        setUser_zipcode("");
    }
    public static AppGlobal getInstancess(){
        if(appGlobal==null){ appGlobal=new AppGlobal(); }
        return appGlobal;
    }
}
