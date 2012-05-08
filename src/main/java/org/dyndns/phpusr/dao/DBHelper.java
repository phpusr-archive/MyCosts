package org.dyndns.phpusr.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.dyndns.phpusr.R;
import org.dyndns.phpusr.constants.DBConstants;
import org.dyndns.phpusr.domains.Coast;
import org.dyndns.phpusr.domains.Data;
import org.dyndns.phpusr.domains.Drive;
import org.dyndns.phpusr.enums.CoastType;

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

    private final static int DB_VER = 26;
    private final static String DB_NAME = "coast.db";

    /** Для вытаскивания 5 последних покупок */
    private static final String LIMIT = "5";

    /** Список покупок */
    private final String TABLE_COAST_LIST = "coastList";
    /** Наименования продуктов */
    private final String TABLE_COAST_ITEMS = "coastItems";
    /** Список поездок */
    private final String TABLE_DRIVE_LIST = "driveList";

    private final String CREATE_TABLE_COAST_LIST = "CREATE TABLE "+ TABLE_COAST_LIST +"( " +
            "id INTEGER PRIMARY KEY"+
            ", coastSum DOUBLE " +
            ", coastDate DATE " +
            ")";
    private final String CREATE_TABLE_COAST_ITEMS = "CREATE TABLE "+ TABLE_COAST_ITEMS +"( " +
            "id INTEGER PRIMARY KEY" +
            ", coastName VARCHAR(50) " +
            ", coastPrice DOUBLE " +
            ", coastType INTEGER " +
            ")";
    private final String CREATE_TABLE_DRIVE_LIST = "CREATE TABLE "+ TABLE_DRIVE_LIST +"( " +
            "id INTEGER PRIMARY KEY"+
            ", coastId INTEGER " +
            ", driveDate DATE " +
            ")";
    private final String DROP_TABLE_COAST_LIST = "DROP TABLE IF EXISTS "+ TABLE_COAST_LIST;
    private final String DROP_TABLE_COAST_ITEMS = "DROP TABLE IF EXISTS "+ TABLE_COAST_ITEMS;
    private final String DROP_TABLE_DRIVE_LIST = "DROP TABLE IF EXISTS "+ TABLE_DRIVE_LIST;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final Context mContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
        Log.d(DBConstants.DEBUG_TAG, "constructor called");
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(DBConstants.DEBUG_TAG, "onCreate() called");
        db.execSQL(CREATE_TABLE_COAST_LIST);
        db.execSQL(CREATE_TABLE_COAST_ITEMS);
        db.execSQL(CREATE_TABLE_DRIVE_LIST);
        fillData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_COAST_LIST);
        db.execSQL(DROP_TABLE_COAST_ITEMS);
        db.execSQL(DROP_TABLE_DRIVE_LIST);
        onCreate(db);
    }

    /**
     * Берет тестовые данные из файла для заполнения тестовыми данными БД
     * @return Список строк с данными
     */
    private ArrayList<Coast> getData() {
        ArrayList<Coast> list = new ArrayList<Coast>();
        final Resources r = mContext.getResources();
        final InputStream stream = r.openRawResource(R.raw.import_data);
        InputStreamReader sr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(sr);

        String data;
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
        coast.setCoastType(CoastType.getCoastTypeById(Integer.valueOf(split[2])));
        return coast;
    } 

    /** Заполняем таблицу элементов покупок тестовыми данными */
    private void fillData(SQLiteDatabase db){
        ArrayList<Coast> data = getData();
        for(Coast coast : data) Log.d(DBConstants.DEBUG_TAG,"item="+coast);

        if( db != null ){
            ContentValues values;

            for(Coast dat : data){
                values = new ContentValues();
                values.put("coastName", dat.getName());
                values.put("coastPrice", dat.getPrice());
                values.put("coastType", dat.getCoastType().getId());
                db.insert(TABLE_COAST_ITEMS, null, values);
            }
        }
        else {
            Log.d(DBConstants.DEBUG_TAG,"db null");
        }
    }

    /**
     * Добавление сделанной покупки
     * @param data Покупка
     */
    public void insertIntoCoastList(Data data) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("coastSum", data.getSum());
        values.put("coastDate", sdf.format(data.getDate()));
        db.insert(TABLE_COAST_LIST, null, values);
    }

    /**
     * Добавление сделанной поездки
     * @param drive Поездка
     */
    public void insertIntoDriveList(Drive drive) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("coastId", drive.getDriveWay().getId());
        values.put("driveDate", sdf.format(drive.getDate()));
        db.insert(TABLE_DRIVE_LIST, null, values);
    }

    /**
     * Добавление элментов покупки
     * @param coast элемент покупки
     */
    public void insertIntoCoastItems(Coast coast) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("coastName", coast.getName());
        values.put("coastPrice", coast.getPrice());
        values.put("coastType", coast.getCoastType().getId());
        db.insert(TABLE_COAST_ITEMS, null, values);
    }

    /**
     * Возвращает LIMIT последних покупок
     * @return список последних покупок
     */
    public List<Data> getCoastListLast() {
        List<Data> list = new ArrayList<Data>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_COAST_LIST, new String[] { "id", "coastSum", "coastDate" },
        null, null, null, null, "coastDate DESC", LIMIT);
        if (cursor.moveToFirst()) {
            do {
                final Data data;
                try {
                    data = new Data(cursor.getInt(0), cursor.getDouble(1), sdf.parse(cursor.getString(2)));
                    list.add(data);
                } catch (ParseException e) {
                    Log.d(DBConstants.DEBUG_TAG, e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    /**
     * Возвращает LIMIT последних поездок
     * @return список последних поездок
     */
    public List<Drive> getDriveListLast() {
        List<Drive> list = new ArrayList<Drive>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_DRIVE_LIST + ", " + TABLE_COAST_ITEMS,
                new String[] { TABLE_DRIVE_LIST + ".id", "driveDate", TABLE_COAST_ITEMS + ".id", "coastName", "coastPrice" },
                "coastId = " + TABLE_COAST_ITEMS + ".id", null, null, null, "driveDate DESC", LIMIT);
        if (cursor.moveToFirst()) {
            do {
                final Drive drive;
                try {
                    Coast driveWay = new Coast(cursor.getInt(2), cursor.getString(3), cursor.getDouble(4), CoastType.BUS);
                    drive = new Drive(cursor.getInt(0), sdf.parse(cursor.getString(1)), driveWay);
                    list.add(drive);
                } catch (ParseException e) {
                    Log.d(DBConstants.DEBUG_TAG, e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    /**
     * Возвращает список элементов покупок
     * @return список элементов покупок
     */
    public List<Coast> getCoastItems() {
        List<Coast> list = new ArrayList<Coast>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_COAST_ITEMS, new String[] { "id", "coastName", "coastPrice", "coastType" },
                null, null, null, null, "id", null);
        if (cursor.moveToFirst()) {
            do {
                final Coast data = new Coast(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), CoastType.getCoastTypeById(cursor.getInt(3)));
                list.add(data);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    /**
     * Возвращает список элементов покупок соответствующих типу
     * @param coastType Тип элемента покупки
     * @return Список элементов покупки
     */
    public List<Coast> getCoastItemsByTypeId(int coastType) {
        List<Coast> list = new ArrayList<Coast>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_COAST_ITEMS, new String[] { "id", "coastName", "coastPrice", "coastType" },
                "coastType = ?", new String[]{ Integer.toString(coastType) }, null, null, "id", null);
        if (cursor.moveToFirst()) {
            do {
                final Coast data = new Coast(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), CoastType.getCoastTypeById(cursor.getInt(3)));
                list.add(data);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    /**
     * Возвращает сумму покупок за последний месяц
     * @return
     */
    public double getCoastSumLastMonth() {
        SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_COAST_LIST, new String[]{"SUM(coastSum)"}, "strftime('%Y-%m', coastDate) = strftime('%Y-%m', 'now')", null, null, null, null);

        double sum = 0;
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(0);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return sum;
    }

    /**
     * Возвращает сумму поездок за последний месяц
     * @return
     */
    public double getDriveSumLastMonth() {
        SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_COAST_ITEMS + ", " + TABLE_DRIVE_LIST, new String[]{"SUM(coastPrice)"},
                "strftime('%Y-%m', driveDate) = strftime('%Y-%m', 'now') AND coastId = " + TABLE_COAST_ITEMS + ".id",
                null, null, null, null);

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
    /** Возвращает даты покупок */
    public List<String> getDates() {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_COAST_LIST, new String[] { "strftime('%Y-%m', coastDate)" },
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
