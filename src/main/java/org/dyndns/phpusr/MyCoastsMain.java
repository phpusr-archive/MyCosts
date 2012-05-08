package org.dyndns.phpusr;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import org.dyndns.phpusr.constants.Constants;
import org.dyndns.phpusr.dao.DBHelper;
import org.dyndns.phpusr.domains.Data;
import org.dyndns.phpusr.domains.Drive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCoastsMain extends Activity {

    private static String TAG = "MyCosts";

    private ExpandableListView lastActionsExpandableListView;
    private TextView coastSumMonth;
    private TextView driveSumMonth;

    private DBHelper mDbHelper;
    
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);

        lastActionsExpandableListView = (ExpandableListView) findViewById(R.id.lastActionsList);
        coastSumMonth = (TextView) findViewById(R.id.sumMonth);
        driveSumMonth = (TextView) findViewById(R.id.driveSumMonth);

        mDbHelper = new DBHelper(getApplicationContext());
    }

    public void onClickAddCoast(View view) {
        AddCoast.callMe(MyCoastsMain.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillList();
        coastSumMonth.setText(Double.toString(mDbHelper.getCoastSumLastMonth()) + " " +
                getResources().getString(R.string.currencyPostfix));
        driveSumMonth.setText(Double.toString(mDbHelper.getDriveSumLastMonth()) + " " +
                getResources().getString(R.string.currencyPostfix));
    }
    
    private void fillList() {
        List<Data> dataList = mDbHelper.getCoastListLast();
        List<Drive> driveList = mDbHelper.getDriveListLast();

        String groupFrom[] = new String[] {Constants.GROUP};
        int groupTo[] = new int[] {android.R.id.text1};

        String childForm[] = new String[] {Constants.ELEMENT};
        int childTo[] = new int[] {android.R.id.text1};

        SimpleExpandableListAdapter expandableAdapter = new SimpleExpandableListAdapter(
                this,
                getGroupData(),
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,
                groupTo,
                getChildData(dataList, driveList),
                android.R.layout.simple_list_item_1,
                childForm,
                childTo
        );

        lastActionsExpandableListView.setAdapter(expandableAdapter);
    }

    /** Возвращает список с заголовками */
    private ArrayList<Map<String, String>> getGroupData() {
        final String[] groups = {getResources().getString(R.string.lastCoast), getResources().getString(R.string.lastDrive)};
        final ArrayList<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        for (String group : groups) {
            Map<String, String> map = new HashMap<String, String>();
            map.put(Constants.GROUP, group);
            groupData.add(map);
        }

        return groupData;
    }

    /** Возвращает список с элементами для заголовков */
    private ArrayList<ArrayList<Map<String, String>>> getChildData(List<Data> dataList, List<Drive> driveList) {
        //Последние покупки
        ArrayList<ArrayList<Map<String, String>>> childData = new ArrayList<ArrayList<Map<String, String>>>();
        ArrayList<Map<String, String>> childDataItem = new ArrayList<Map<String, String>>();
        for (Data data : dataList) {
            Map<String, String> map = new HashMap<String, String>();
            map.put(Constants.ELEMENT, sdf.format(data.getDate())+" - "+Double.toString(data.getSum()));
            childDataItem.add(map);
        }
        childData.add(childDataItem);

        //Последние поездки
        childDataItem = new ArrayList<Map<String, String>>();
        for (Drive drive : driveList) {
            Map<String, String> map = new HashMap<String, String>();
            map.put(Constants.ELEMENT, sdf.format(drive.getDate()) + " - " + drive.getDriveWay().getName() +
                    " (" + drive.getDriveWay().getPrice() + " " + getResources().getString(R.string.currencyPostfix) + ")");
            childDataItem.add(map);
        }
        childData.add(childDataItem);

        return childData;
    }

    /**
     * Обработчик кнопки "Добавить за проезд"
     * @param view
     */
    public void addForPassageOnClick(View view) {
        //TODO сделать нормальный обработчик
        List<Data> dataList = mDbHelper.getCoastListLast();
        //new AlertDialog.Builder(this).setTitle("Title").setAdapter(adapter, null).show();
        //Toast.makeText(this, "Зачем вы нажали?", Toast.LENGTH_SHORT).show();

        AddDrive.callMe(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**Обработчик выбора пункта меню "Выход"*/
    public void onClickExit(MenuItem menuItem) {
        onBackPressed();
    }

    /**Обработчик выбора пункта меню "Добавления продукта"*/
    public void onClickAddProduct(MenuItem menuItem) {
        AddProduct.callMe(MyCoastsMain.this);
    }
}

