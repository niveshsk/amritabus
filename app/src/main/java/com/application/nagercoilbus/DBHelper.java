package com.application.nagercoilbus;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    Context ctx;

    private String DBNAME;
    private String DBPATH;

    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
        this.ctx = context;
        this.DBNAME = name;

        this.DBPATH = this.ctx.getDatabasePath(DBNAME).getAbsolutePath();
        Log.e("Path 1", DBPATH);

    }

    public void createDataBase() {

        boolean dbExist = checkDataBase();

        SQLiteDatabase db_Read = null;

        if (!dbExist) {
            synchronized (this) {

                db_Read = this.getReadableDatabase();
                Log.e("Path 2", this.getReadableDatabase().getPath());
                db_Read.close();

                copyDataBase();
                Log.v("copyDataBase---", "Successfully");
            }

            // try {

            // } catch (IOException e) {
            // throw new Error("Error copying database");
            // }
        }
    }

    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DBPATH;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            Log.i("SQLite Error", "database does't exist yet.");
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    public void copyDataBase() {

        try {
            final String DATABASE_NAME = "nglbus";
            String DATABASE_PATH = "/data/data/com.application.nagercoilbus/databases/";
            File file = new File(DATABASE_PATH + DATABASE_NAME);
            if(file.exists())
            {
                file.delete();
                System.out.println("delete database file.");
            }
            InputStream myInput = ctx.getAssets().open(DBNAME);
            String outFileName = DBPATH;

            OutputStream myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024 * 3];

            int length = 0;

            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {

        }

    }
public void getdata(){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="select * from busdetails";
    Cursor cursor=db.rawQuery(sql,null);
}
       public ArrayList<HashMap<String,String>> getAllBus(String bus) {
        ArrayList<HashMap<String, String>> contactList = new ArrayList<>();
           String selectQuery = "SELECT  * FROM busdetails where busno = '" + bus + "'";
           String selectTi="select * from time where busno ='"+bus+"'";
           Log.d("fdgfgf",selectQuery);
           Log.d("fdgfgf",selectTi);

           SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor cursor1=db.rawQuery(selectTi,null);
        if (cursor.moveToNext() ){

            do {
                HashMap<String, String> contact = new HashMap<>();
                contact.put("busno", cursor.getString(cursor.getColumnIndex("busno")));
                contact.put("stop1", cursor.getString(cursor.getColumnIndex("stop1")));
                contact.put("stop2", cursor.getString(cursor.getColumnIndex("stop2")));
                contact.put("stop3", cursor.getString(cursor.getColumnIndex("stop3")));
                contact.put("stop4", cursor.getString(cursor.getColumnIndex("stop4")));
                contact.put("stop5", cursor.getString(cursor.getColumnIndex("stop5")));
                contact.put("stop6", cursor.getString(cursor.getColumnIndex("stop6")));
                contact.put("stop7", cursor.getString(cursor.getColumnIndex("stop7")));
                contact.put("stop8", cursor.getString(cursor.getColumnIndex("stop8")));
                contact.put("stop9", cursor.getString(cursor.getColumnIndex("stop9")));
                contact.put("stop10", cursor.getString(cursor.getColumnIndex("stop10")));
                contact.put("stop11", cursor.getString(cursor.getColumnIndex("stop11")));
                contact.put("stop12", cursor.getString(cursor.getColumnIndex("stop12")));
                    contact.put("stop13", cursor.getString(cursor.getColumnIndex("stop13")));
                    contact.put("stop14", cursor.getString(cursor.getColumnIndex("stop14")));
                    contact.put("stop15", cursor.getString(cursor.getColumnIndex("stop15")));
                    contact.put("stop16", cursor.getString(cursor.getColumnIndex("stop16")));
                    contact.put("stop17", cursor.getString(cursor.getColumnIndex("stop17")));
                    contact.put("stop18", cursor.getString(cursor.getColumnIndex("stop18")));
                    contact.put("stop19", cursor.getString(cursor.getColumnIndex("stop19")));
                    contact.put("stop20", cursor.getString(cursor.getColumnIndex("stop20")));
                    contact.put("stop21", cursor.getString(cursor.getColumnIndex("stop21")));
                    contact.put("stop22", cursor.getString(cursor.getColumnIndex("stop22")));
                    contact.put("stop23", cursor.getString(cursor.getColumnIndex("stop23")));
                    contact.put("stop24", cursor.getString(cursor.getColumnIndex("stop24")));
                    contact.put("stop25", cursor.getString(cursor.getColumnIndex("stop25")));
                    contact.put("stop26", cursor.getString(cursor.getColumnIndex("stop26")));
                    contact.put("stop27", cursor.getString(cursor.getColumnIndex("stop27")));
                    contact.put("stop28", cursor.getString(cursor.getColumnIndex("stop28")));
                    contact.put("stop29", cursor.getString(cursor.getColumnIndex("stop29")));
                    contact.put("stop30", cursor.getString(cursor.getColumnIndex("stop30")));
                    if(cursor1.moveToNext()) {
                        do {
                            contact.put("time1", cursor1.getString(cursor1.getColumnIndex("stop1")));
                            contact.put("time2", cursor1.getString(cursor1.getColumnIndex("stop2")));
                            contact.put("time3", cursor1.getString(cursor1.getColumnIndex("stop3")));
                            contact.put("time4", cursor1.getString(cursor1.getColumnIndex("stop4")));
                            contact.put("time5", cursor1.getString(cursor1.getColumnIndex("stop5")));
                            contact.put("time6", cursor1.getString(cursor1.getColumnIndex("stop6")));
                            contact.put("time7", cursor1.getString(cursor1.getColumnIndex("stop7")));
                            contact.put("time8", cursor1.getString(cursor1.getColumnIndex("stop8")));
                            contact.put("time9", cursor1.getString(cursor1.getColumnIndex("stop9")));
                            contact.put("time10", cursor1.getString(cursor1.getColumnIndex("stop10")));
                            contact.put("time11", cursor1.getString(cursor1.getColumnIndex("stop11")));
                            contact.put("time12", cursor1.getString(cursor1.getColumnIndex("stop12")));
                            contact.put("time13", cursor1.getString(cursor1.getColumnIndex("stop13")));
                            contact.put("time14", cursor1.getString(cursor1.getColumnIndex("stop14")));
                            contact.put("time15", cursor1.getString(cursor1.getColumnIndex("stop15")));
                            contact.put("time16", cursor1.getString(cursor1.getColumnIndex("stop16")));
                            contact.put("time17", cursor1.getString(cursor1.getColumnIndex("stop17")));
                            contact.put("time18", cursor1.getString(cursor1.getColumnIndex("stop18")));
                            contact.put("time19", cursor1.getString(cursor1.getColumnIndex("stop19")));
                            contact.put("time20", cursor1.getString(cursor1.getColumnIndex("stop20")));
                            contact.put("time21", cursor1.getString(cursor1.getColumnIndex("stop21")));
                            contact.put("time22", cursor1.getString(cursor1.getColumnIndex("stop22")));
                            contact.put("time23", cursor1.getString(cursor1.getColumnIndex("stop23")));
                            contact.put("time24", cursor1.getString(cursor1.getColumnIndex("stop24")));
                            contact.put("time25", cursor1.getString(cursor1.getColumnIndex("stop25")));
                            contact.put("time26", cursor1.getString(cursor1.getColumnIndex("stop26")));
                            contact.put("time27", cursor1.getString(cursor1.getColumnIndex("stop27")));
                            contact.put("time28", cursor1.getString(cursor1.getColumnIndex("stop28")));
                            contact.put("time29", cursor1.getString(cursor1.getColumnIndex("stop29")));
                            contact.put("time30", cursor1.getString(cursor1.getColumnIndex("stop30")));
                        }while (cursor1.moveToNext());
                    }
                contactList.add(contact);
                } while (cursor.moveToNext() ) ;


        } else {
            cursor.close();
            return null;
        }
        // return contact list
           cursor.close();
        db.close();
        return contactList;
    }

    public ArrayList<String> getBus(String sou,String des) {
        ArrayList<String> contactList = new ArrayList<>();
        // Select All Query
        Log.d("fdgfgf","jdfhjdh");

        String selectQuery = "SELECT  busno FROM busdetails where (stop1 = '" + sou + "' or stop2 = '"+ sou +"' or stop3 = '"+ sou +"' or stop4 = '"+ sou +"' or stop5 = '"+ sou +"' or stop6 = '"+ sou +"' or stop7 = '"+ sou +"'" +
                " or stop8 = '"+ sou +"' or stop9 = '"+sou+"' or stop10 = '"+ sou +"' or stop11 = '"+ sou +"'" +
                " or stop12 = '"+ sou +"' or stop13 = '"+ sou +"' or stop14 = '"+ sou +"' or stop15 = '"+ sou +"' or stop16 = '"+ sou +"' or stop17 = '"+ sou +"' or stop18 = '"+ sou +"' or stop19 = '"+ sou +"' or stop20 = '"+ sou +"' or stop21 = '"+ sou +"' or stop22 = '"+ sou +"' or stop23 = '"+ sou +"' or stop24 = '"+ sou +"' or stop25 = '"+ sou +"'or stop26 = '"+ sou +"'" +
                " or stop27 = '"+ sou +"' or stop28 = '"+ sou +"' or stop29 = '"+ sou +"' or stop30 = '"+ sou +"')" +
                " and (stop1 = '" + des + "' or stop2 = '"+ des +"' or stop3 = '"+ des +"' or stop4 = '"+ des +"' or stop5 = '"+ des +"' or stop6 = '"+ des +"'or stop7 = '"+ des +"' or stop8 = '"
                + des +"' or stop9 = '"+ des +"' or stop10 = '"+ des +"' or stop11 = '"+ des +"'" +
                "or stop12 = '"+ des +"' or stop13 = '"+ des +"' or stop14 = '"+ des +"' or stop15 = '"+ des +"' or stop16 = '"+ des +"'or stop17 = '"+ des +"' or stop18 = '"+ des +"' " +
                "or stop19 = '"+ des +"' or stop20 = '"+ des +"' or stop21 = '"+ des +"' or stop22 = '"+ des +"' or stop23 = '"+ des +"' or stop24 = '"+ des +"' or stop25 = '"+ des +"' or stop26 = '"+ des +"' or stop27 = '"+ des +"' or stop28 = '"+ des +"' or stop29 = '"+ des +"' or stop30 = '"+ des +"')";
        Log.d("fdgfgf",selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToNext()) {
            do {
                contactList.add(cursor.getString(cursor.getColumnIndex("busno")).toUpperCase());
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            db.close();
            return null;
        }
        // return contact list
        return contactList;
    }
    public ArrayList<String> getBusno() {
        ArrayList<String> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  busno FROM busdetails";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToNext()) {

            do {

                // Adding contact to list
                contactList.add(cursor.getString(cursor.getColumnIndex("busno")));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            db.close();
            return null;        }
        // return contact list
        return contactList;
    }
    public ArrayList<String> collegeDepartment() {
        ArrayList<String> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM department";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToNext()) {

            do {

                // Adding contact to list
                contactList.add(cursor.getString(cursor.getColumnIndex("dept")));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            db.close();
            return null;        }
        // return contact list
        return contactList;
    }
    public ArrayList<String> getbusro() {
        ArrayList<String> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM board";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToNext()) {

            do {
                contactList.add(cursor.getString(cursor.getColumnIndex("bo")));

            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return null;
        }
        // return contact list
        cursor.close();
        db.close();
        return contactList;
    }
    public ArrayList<String> getdriver(String bus) {
        ArrayList<String> contactList = new ArrayList<>();
        String selectQuery = "SELECT  drina,drinu FROM " + bus;
        Log.d("fdgfgf",selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToNext()) {
            do {
                contactList.add(cursor.getString(cursor.getColumnIndex("drina")));
                contactList.add(cursor.getString(cursor.getColumnIndex("drinu")));

            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return null;
        }
        // return contact list
        cursor.close();
        db.close();
        return contactList;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DATABASE_NAME = "nglbus";
        String DATABASE_PATH = "/data/data/com.application.nagercoilbus/databases/";
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if(file.exists())
        {
            file.delete();
            System.out.println("delete database file.");
        }
    }



    public ArrayList<String> getBusStop(String bus) {
        ArrayList<String> contactList = new ArrayList<>();
        String selectQuery = "SELECT  board FROM " + bus;
        Log.d("fdgfgf",selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToNext()) {
            do {
                contactList.add(cursor.getString(cursor.getColumnIndex("board")));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return null;
        }
        // return contact list
        cursor.close();
        db.close();
        return contactList;
    }
    public ArrayList<String> getBusTime(String bus) {
        ArrayList<String> contactList = new ArrayList<>();
        String selectQuery = "SELECT  time FROM " + bus;
        Log.d("fdgfgf",selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToNext()) {
            do {
                contactList.add(cursor.getString(cursor.getColumnIndex("time")));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            db.close();
            return null;
        }
        // return contact list
        cursor.close();
        return contactList;
    }

    public String getOrigin(String bus) {
        String query="select origin from busdetails";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToNext();
        return cursor.getString(cursor.getColumnIndex("origin"));

    }

    public Cursor get() {
        String query="select busno from busdetails";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToNext();
        return cursor;
    }
}
