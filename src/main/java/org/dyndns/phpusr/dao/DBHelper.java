package org.dyndns.phpusr.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.dyndns.phpusr.domains.Data;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author phpusr
 *         Date: 10.03.12
 *         Time: 15:54
 */
public class DBHelper extends SQLiteOpenHelper {

    final static int DB_VER = 1;
    final static String DB_NAME = "coast.db";
    final String TABLE_NAME = "todo";
    final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"( " +
            "_id INTEGER PRIMARY KEY"+
            ", coastSum DOUBLE " +
            ", coastDate DATE " +
            ")";
    final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    final String DATA_FILE_NAME = "data.txt";

    Context mContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
        Log.d(Constants.DEBUG_TAG, "constructor called");
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(Constants.DEBUG_TAG,"onCreate() called");
        db.execSQL(CREATE_TABLE);
        //fillData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    private ArrayList<String> getData() {
        InputStream stream = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            stream = mContext.getAssets().open(DATA_FILE_NAME);
        }
        catch (IOException e) {
            Log.d(Constants.DEBUG_TAG,e.getMessage());
        }

        DataInputStream dataStream = new DataInputStream(stream);
        String data = "";
        try {
            while( (data=dataStream.readLine()) != null ) {
                list.add(data);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private void fillData(SQLiteDatabase db){
        ArrayList<String> data = getData();
        for(String dt:data) Log.d(Constants.DEBUG_TAG,"item="+dt);

        if( db != null ){
            ContentValues values;

            for(String dat:data){
                values = new ContentValues();
                values.put("todo", dat);
                db.insert(TABLE_NAME, null, values);
            }
        }
        else {
            Log.d(Constants.DEBUG_TAG,"db null");
        }
    }

    public void insertData(Data data) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("coastSum", data.getSum());
        //values.put("coastDate", data.getDate()); //TODO Непонятно как вставлять даты
        db.insert(TABLE_NAME, null, values);
    }

    public List<Data> selectAll() {
        List<Data> list = new ArrayList<Data>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { "_id", "coastSum", "coastDate" },
        null, null, null, null, "");
        if (cursor.moveToFirst()) {
            do {
                final Data data = new Data(cursor.getInt(0), cursor.getDouble(1));
                list.add(data);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
}
