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
import org.dyndns.phpusr.domains.Drive;
import org.dyndns.phpusr.domains.Lunch;
import org.dyndns.phpusr.enums.CoastType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author phpusr
 *         Date: 10.03.12
 *         Time: 15:54
 */
public class DBHelper extends SQLiteOpenHelper {

    private final static int DB_VER = 27;
    private final static String DB_NAME = "coast.db";

    /** Для вытаскивания 5 последних покупок */
    private static final String LIMIT = "5";

    /** Список покупок */
    private final String TABLE_LUNCH_LIST = "coastList";
    /** Наименования продуктов */
    private final String TABLE_COAST_ITEMS = "coastItems";
    /** Список поездок */
    private final String TABLE_DRIVE_LIST = "driveList";

    private final String CREATE_TABLE_LUNCH_LIST = "CREATE TABLE "+ TABLE_LUNCH_LIST +"( " +
            "id INTEGER PRIMARY KEY"+
            ", drinkId INTEGER " +
            ", garnishId INTEGER " +
            ", meatId INTEGER " +
            ", saladId INTEGER " +
            ", flourId INTEGER " +
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
    private final String DROP_TABLE_COAST_LIST = "DROP TABLE IF EXISTS "+ TABLE_LUNCH_LIST;
    private final String DROP_TABLE_COAST_ITEMS = "DROP TABLE IF EXISTS "+ TABLE_COAST_ITEMS;
    private final String DROP_TABLE_DRIVE_LIST = "DROP TABLE IF EXISTS "+ TABLE_DRIVE_LIST;

    private final SimpleDateFormat sdf = new SimpleDateFormat(DBConstants.DB_DATE_FORMAT);

    private final Context mContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
        Log.d(DBConstants.DEBUG_TAG, "constructor called");
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(DBConstants.DEBUG_TAG, "onCreate() called");
        db.execSQL(CREATE_TABLE_LUNCH_LIST);
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
    private ArrayList<Coast> getCoastListFromFile() {
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
        ArrayList<Coast> coastList = getCoastListFromFile();

        if( db != null ){
            ContentValues values;

            for(Coast coast : coastList){
                Log.d(DBConstants.DEBUG_TAG,"coast=" + coast);
                values = new ContentValues();
                values.put("coastName", coast.getName());
                values.put("coastPrice", coast.getPrice());
                values.put("coastType", coast.getCoastType().getId());
                db.insert(TABLE_COAST_ITEMS, null, values);
            }
        }
        else {
            Log.d(DBConstants.DEBUG_TAG,"db null");
        }
    }

    /**
     * Добавление сделанной покупки
     * @param lunch Покупка
     */
    public void insertIntoCoastList(Lunch lunch) {
        Log.d(DBConstants.DEBUG_TAG,"insertIntoCoastList calling");
        Log.d(DBConstants.DEBUG_TAG,"lunch=" + lunch);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("coastSum", lunch.getSum());
        values.put("coastDate", sdf.format(lunch.getDate()));
        values.put("drinkId", lunch.getDrink().getId());
        values.put("garnishId", lunch.getGarnish().getId());
        values.put("meatId", lunch.getMeat().getId());
        values.put("saladId", lunch.getSalad().getId());
        values.put("flourId", lunch.getFlour().getId());
        db.insert(TABLE_LUNCH_LIST, null, values);
    }

    /**
     * Добавление сделанной поездки
     * @param drive Поездка
     */
    public void insertIntoDriveList(Drive drive) {
        Log.d(DBConstants.DEBUG_TAG,"insertIntoDriveList calling");
        Log.d(DBConstants.DEBUG_TAG,"drive=" + drive);

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
        Log.d(DBConstants.DEBUG_TAG,"insertIntoCoastItems calling");
        Log.d(DBConstants.DEBUG_TAG,"coast=" + coast);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("coastName", coast.getName());
        values.put("coastPrice", coast.getPrice());
        values.put("coastType", coast.getCoastType().getId());
        db.insert(TABLE_COAST_ITEMS, null, values);
    }

    private List<Lunch> getLunchList(String limit, Date date, Boolean month) {
        Log.d(DBConstants.DEBUG_TAG, "getLunchList called");
        Log.d(DBConstants.DEBUG_TAG, "limit=" + limit);
        Log.d(DBConstants.DEBUG_TAG, "date=" + date);
        Log.d(DBConstants.DEBUG_TAG, "month=" + month);

        List<Lunch> list = new ArrayList<Lunch>();
        SQLiteDatabase db = getWritableDatabase();

        String selection = null;
        String[] selectionArgs = null;
        if (date != null) {
            if (month) {
                selection = "strftime('%Y-%m', coastDate) = strftime('%Y-%m', ?)";
            } else {
                selection = "strftime('%Y-%m-%d', coastDate) = strftime('%Y-%m-%d', ?)";
            }
            selectionArgs = new String[] { sdf.format(date) };
        }
        Cursor cursor = db.query(TABLE_LUNCH_LIST, new String[] { "id", "coastSum", "coastDate", "drinkId", "garnishId", "meatId", "saladId", "flourId" },
                selection, selectionArgs, null, null, "coastDate DESC", limit);
        if (cursor.moveToFirst()) {
            do {
                final Lunch lunch;
                try {
                    lunch = new Lunch(cursor.getInt(0), cursor.getDouble(1), sdf.parse(cursor.getString(2)));
                    lunch.setDrink(getCoastById(cursor.getInt(3)));
                    lunch.setGarnish(getCoastById(cursor.getInt(4)));
                    lunch.setMeat(getCoastById(cursor.getInt(5)));
                    lunch.setSalad(getCoastById(cursor.getInt(6)));
                    lunch.setFlour(getCoastById(cursor.getInt(7)));
                    list.add(lunch);
                    Log.d(DBConstants.DEBUG_TAG, "lunch=" + lunch);
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
     * Возвращает список всех совершенных покупок за ...
     * @return Список покупок за ...
     */
    //TODO дописать, обратить внимание на сортировку
    public List<Lunch> getLunchListByDate(Date date, boolean month) {
        Log.d(DBConstants.DEBUG_TAG, "getLunchListByDate called");
        return getLunchList(null, date, month);
    }

    /**
     * Возвращает LIMIT последних покупок
     * @return Список последних покупок
     */
    public List<Lunch> getLunchListLast() {
        Log.d(DBConstants.DEBUG_TAG, "getLunchListLast called");
        return getLunchList(LIMIT, null, null);
    }

    private List<Drive> getDriveList(String limit) {
        List<Drive> list = new ArrayList<Drive>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_DRIVE_LIST + ", " + TABLE_COAST_ITEMS,
                new String[] { TABLE_DRIVE_LIST + ".id", "driveDate", TABLE_COAST_ITEMS + ".id", "coastName", "coastPrice" },
                "coastId = " + TABLE_COAST_ITEMS + ".id", null, null, null, "driveDate DESC", limit);
        if (cursor.moveToFirst()) {
            do {
                final Drive drive;
                try {
                    Coast driveWay = new Coast(cursor.getInt(2), cursor.getString(3), cursor.getDouble(4), CoastType.BUS);
                    drive = new Drive(cursor.getInt(0), sdf.parse(cursor.getString(1)), driveWay);
                    list.add(drive);
                    Log.d(DBConstants.DEBUG_TAG, "drive=" + drive);
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
     * @return Список последних поездок
     */
    public List<Drive> getDriveListLast() {
        Log.d(DBConstants.DEBUG_TAG, "getDriveListLast called");
        return getDriveList(LIMIT);
    }

    /**
     * Возвращает список последних поездок за ...
     * @return Список последних поездок за ...
     */
    public List<Drive> getDriveListByDate(Date date) {
        Log.d(DBConstants.DEBUG_TAG, "getDriveListByDate called");
        return getDriveList(null);
    }

    private List<Coast> getCoastItems(int coastType) {
        List<Coast> list = new ArrayList<Coast>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_COAST_ITEMS, new String[] { "id", "coastName", "coastPrice", "coastType" },
                "coastType = ? OR ? = '-1'", new String[]{ Integer.toString(coastType), Integer.toString(coastType) }, null, null, "id", null);
        if (cursor.moveToFirst()) {
            do {
                final Coast coast = new Coast(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), CoastType.getCoastTypeById(cursor.getInt(3)));
                list.add(coast);
                Log.d(DBConstants.DEBUG_TAG, "coast=" + coast);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    /**
     * Возвращает список всех элементов покупок
     * @return Список всех элементов покупок
     */
    public List<Coast> getCoastItemsAll() {
        Log.d(DBConstants.DEBUG_TAG, "getCoastItemsAll called");

        return getCoastItems(-1);
    }

    /**
     * Возвращает список элементов покупок соответствующих типу
     * @param coastType Тип элемента покупки
     * @return Список элементов покупки
     */
    public List<Coast> getCoastItemsByTypeId(int coastType) {
        Log.d(DBConstants.DEBUG_TAG, "getCoastItemsByTypeId called");

        return getCoastItems(coastType);
    }

    /**
     * Возвращает элемент покупки по id
     * @param coastId id элемента покупки
     * @return элемент покупки
     */
    private Coast getCoastById(int coastId) {
        Log.d(DBConstants.DEBUG_TAG, "getCoastById called");

        Coast coast = null;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_COAST_ITEMS, new String[] { "id", "coastName", "coastPrice", "coastType" },
                "id = ?", new String[]{ Integer.toString(coastId) }, null, null, "id", null);
        if (cursor.moveToFirst()) {
            coast = new Coast(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), CoastType.getCoastTypeById(cursor.getInt(3)));
            Log.d(DBConstants.DEBUG_TAG, "coast=" + coast);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return coast;
    }

    /**
     * Возвращает сумму покупок за последний месяц
     * @return
     */
    public double getCoastSumLastMonth() {
        SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_LUNCH_LIST, new String[]{"SUM(coastSum)"}, "strftime('%Y-%m', coastDate) = strftime('%Y-%m', 'now')", null, null, null, null);

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
        Cursor cursor = db.query(TABLE_LUNCH_LIST, new String[] { "strftime('%Y-%m', coastDate)" },
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
