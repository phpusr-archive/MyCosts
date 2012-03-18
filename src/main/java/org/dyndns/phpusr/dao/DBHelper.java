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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author phpusr
 *         Date: 10.03.12
 *         Time: 15:54
 */
public class DBHelper extends SQLiteOpenHelper {

    final static int DB_VER = 3;
    final static String DB_NAME = "coast.db";
    final String TABLE_NAME_COAST_LIST = "coastList";
    final String TABLE_NAME_COAST_ITEMS = "coastItems";
    final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME_COAST_LIST +"( " +
            "id INTEGER PRIMARY KEY"+
            ", coastSum DOUBLE " +
            ", coastDate DATE " +
            ")";
    final String DROP_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME_COAST_LIST;
    final String DATA_FILE_NAME = "data.txt";
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
                db.insert(TABLE_NAME_COAST_LIST, null, values);
            }
        }
        else {
            Log.d(Constants.DEBUG_TAG,"db null");
        }
    }

    public void insetIntoCoastList(Data data) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("coastSum", data.getSum());
        values.put("coastDate", sdf.format(data.getDate()));
        db.insert(TABLE_NAME_COAST_LIST, null, values);
    }

    public List<Data> getCoastList() {
        List<Data> list = new ArrayList<Data>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME_COAST_LIST, new String[] { "id", "coastSum", "coastDate" },
        null, null, null, null, "id DESC", "5");
        if (cursor.moveToFirst()) {
            do {
                final Data data;
                try {
                    data = new Data(cursor.getInt(0), cursor.getDouble(1), sdf.parse(cursor.getString(2)));
                    list.add(data);
                } catch (ParseException e) {
                    Log.d(Constants.DEBUG_TAG, e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
    
    public double getCoastSumLastMonth() {
        SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_NAME_COAST_LIST, new String[]{"SUM(coastSum)"}, "strftime('%Y-%m', coastDate) = strftime('%Y-%m', 'now')", null, null, null, null);

        double sum = 0;
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(0);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return sum;
    }

    //Для отладки
    public List<String> getDates() {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME_COAST_LIST, new String[] { "strftime('%Y-%m', coastDate)" },
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
}
