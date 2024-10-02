package com.example.damproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EmployeeDataHelper extends SQLiteOpenHelper {
    //declaration de nom de data base avec les noms des columns
    public static final String DB_NAME = "employee.db";
    public static final String TABLE_NAME = "employeeTab";
    public static final String COL_ID = "id";
    public static final String COL_FIRSTNAME = "f_name";
    public static final String COL_LASTNAME = "l_name";
    public static final String COL_PHONE = "phone";
    public static final String COL_MAIL = "mail";
    public static final String COL_IMAGE = "image";


    public EmployeeDataHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        //give permission to write from data base
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    // onCreate pour cree notre data base
    public void onCreate(SQLiteDatabase db) {
        // execution de command de creation de tableau
        db.execSQL("create table " + TABLE_NAME + "(id TEXT PRIMARY KEY ,f_name TEXT,l_name TEXT ,phone TEXT,mail TEXT, image BLOG )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //VERIFICATION SI LE TABLEAUX EMPLOYEE EXIST DEJA ET LE SUPPRIMER
        db.execSQL("drop table if exists " + TABLE_NAME);

    }

    public Boolean insertIntoDataBase(String idd, String fname, String lname, String phone, String mail, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        // we will connect the  columns with variables here through content values
        //put the idd value in COL_ID ...
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, idd);
        contentValues.put(COL_FIRSTNAME, fname);
        contentValues.put(COL_LASTNAME, lname);
        contentValues.put(COL_PHONE, phone);
        contentValues.put(COL_MAIL, mail);
        contentValues.put(COL_IMAGE, image);
        //insert to data base
        //db.insert return -1 if data dosn't inserted so we can use this return to verify
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    //method to show data
    public ArrayList<EmployeeClass> getEmployeeData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<EmployeeClass> arrayList = new ArrayList<EmployeeClass>();
        //select all the data from data base
        //to get all the data in the list view we have to move from the positin 0 to the end
        //convert db.rawqwery to cursor value

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String f_name = cursor.getString(1);
            String l_name = cursor.getString(2);
            String phone = cursor.getString(3);
            String mail = cursor.getString(4);
            byte[] image = cursor.getBlob(5);
            //create new EmployeeClass object and send data
            EmployeeClass employeeClass = new EmployeeClass(id, f_name, l_name, phone, mail, image);
            //add the new objct to the list
            arrayList.add(employeeClass);
        }
        return arrayList;
    }

    public int deleteFromDataBase(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_NAME, "id=?", new String[]{id});
        if (res > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    //update data

    public Boolean updateData(String idd, String fname, String lname, String phone, String mail, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        // we will connect the  columns with variables here through content values
        //put the idd value in COL_ID ...
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, idd);
        contentValues.put(COL_FIRSTNAME, fname);
        contentValues.put(COL_LASTNAME, lname);
        contentValues.put(COL_PHONE, phone);
        contentValues.put(COL_MAIL, mail);
        contentValues.put(COL_IMAGE, image);
        //insert to data base
        //db.insert return -1 if data dosn't inserted so we can use this return to verify
        long result = db.update(TABLE_NAME, contentValues, "id=?", new String[]{idd});
        if (result == -1) return false;
        else return true;
    }

    public int countData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.close();
        final int count = cursor.getCount();
        return count;
    }
}

