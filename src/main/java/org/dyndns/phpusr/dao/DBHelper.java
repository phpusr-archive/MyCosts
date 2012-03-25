package org.dyndns.phpusr.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.dyndns.phpusr.R;
import org.dyndns.phpusr.domains.Coast;
import org.dyndns.phpusr.domains.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    final static int DB_VER = 18;
    final static String DB_NAME = "coast.db";
    public static final String LIMIT = "5";
    final String TABLE_NAME_COAST_LIST = "coastList";
    final String TABLE_NAME_COAST_ITEMS = "coastItems";
    final String CREATE_TABLE_COAST_LIST = "CREATE TABLE "+ TABLE_NAME_COAST_LIST +"( " +
            "id INTEGER PRIMARY KEY"+
            ", coastSum DOUBLE " +
            ", coastDate DATE " +
            ")";
    final String CREATE_TABLE_COAST_ITEMS = "CREATE TABLE "+ TABLE_NAME_COAST_ITEMS +"( " +
            "id INTEGER PRIMARY KEY"+
            ", coastName VARCHAR(50) " +
            ", coastPrice DOUBLE " +
            ")";
    final String DROP_TABLE_COAST_LIST = "DROP TABLE IF EXISTS "+ TABLE_NAME_COAST_LIST;
    final String DROP_TABLE_COAST_ITEMS = "DROP TABLE IF EXISTS "+ TABLE_NAME_COAST_ITEMS;
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
        db.execSQL(CREATE_TABLE_COAST_LIST);
        db.execSQL(CREATE_TABLE_COAST_ITEMS);
        fillData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_COAST_LIST);
        db.execSQL(DROP_TABLE_COAST_ITEMS);
        onCreate(db);
    }

    private ArrayList<Coast> getData() {
        ArrayList<Coast> list = new ArrayList<Coast>();
        final Resources r = mContext.getResources();
        final InputStream stream = r.openRawResource(R.raw.import_data);
        InputStreamReader sr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(sr);

        String data = "";
        try {
            while( (data=reader.readLine()) != null ) {                
                list.add(stringToCoastItem(data));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    private Coast stringToCoastItem(String inputString) {
        final String[] split = inputString.split("\t");
        Coast coast = new Coast();
        coast.setName(split[0]);
        coast.setPrice(Double.valueOf(split[1]));
        return coast;
    } 

    private void fillData(SQLiteDatabase db){
        ArrayList<Coast> data = getData();
        for(Coast coast : data) Log.d(Constants.DEBUG_TAG,"item="+coast);

        if( db != null ){
            ContentValues values;

            for(Coast dat : data){
                values = new ContentValues();
                values.put("coastName", dat.getName());
                values.put("coastPrice", dat.getPrice());
                db.insert(TABLE_NAME_COAST_ITEMS, null, values);
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

    public List<Data> getCoastListLast() {
        List<Data> list = new ArrayList<Data>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME_COAST_LIST, new String[] { "id", "coastSum", "coastDate" },
        null, null, null, null, "id DESC", LIMIT);
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

    public List<Coast> getCoastItems() {
        List<Coast> list = new ArrayList<Coast>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME_COAST_ITEMS, new String[] { "id", "coastName", "coastPrice" },
                null, null, null, null, "id", LIMIT);
        if (cursor.moveToFirst()) {
            do {
                final Coast data = new Coast(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2));
                list.add(data);
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
