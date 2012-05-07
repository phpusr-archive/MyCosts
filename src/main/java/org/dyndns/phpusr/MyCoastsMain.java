package org.dyndns.phpusr;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.dyndns.phpusr.dao.DBHelper;
import org.dyndns.phpusr.domains.Data;
import org.dyndns.phpusr.domains.Drive;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyCoastsMain extends Activity {

    private static String TAG = "MyCosts";

    private ListView listView;
    private ListView driveListVew;
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

        listView = (ListView) findViewById(R.id.coastList);
        driveListVew = (ListView) findViewById(R.id.driveList);
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
        coastSumMonth.setText(Double.toString(mDbHelper.getCoastSumLastMonth()));
        driveSumMonth.setText(Double.toString(mDbHelper.getDriveSumLastMonth()));
    }
    
    private void fillList() {
        List<Data> dataList = mDbHelper.getCoastListLast();
        ArrayAdapter<Data> adapter = new DataAdapter( this, R.layout.list, dataList);
        listView.setAdapter(adapter);

        List<Drive> driveList = mDbHelper.getDriveListLast();
        ArrayAdapter<Drive> driveAdapter = new DriveAdapter( this, R.layout.list, driveList);
        driveListVew.setAdapter(driveAdapter);
    }

    class DataAdapter extends ArrayAdapter<Data> {

        public DataAdapter(Context context, int textViewResourceId, List<Data> items) {
            super(context, textViewResourceId, items);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.list, parent, false);
            TextView label=(TextView)row.findViewById(R.id.list);
            label.setText(sdf.format(getItem(position).getDate())+" - "+Double.toString(getItem(position).getSum()));

            return row;
        }
    }

    class DriveAdapter extends ArrayAdapter<Drive> {

        public DriveAdapter(Context context, int textViewResourceId, List<Drive> items) {
            super(context, textViewResourceId, items);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int pos, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.list, parent, false);
            TextView label=(TextView)row.findViewById(R.id.list);
            label.setText(sdf.format(getItem(pos).getDate())+" - "+getItem(pos).getDriveWay().getName() +
            " (" + getItem(pos).getDriveWay().getPrice() + " " + getResources().getString(R.string.currencyPostfix) + ")");

            return row;
        }
    }

    /**
     * Обработчик кнопки "Добавить за проезд"
     * @param view
     */
    public void addForPassageOnClick(View view) {
        //TODO сделать нормальный обработчик
        List<Data> dataList = mDbHelper.getCoastListLast();
        ArrayAdapter<Data> adapter = new DataAdapter( this, R.layout.list, dataList);
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

