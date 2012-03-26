package org.dyndns.phpusr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.dyndns.phpusr.dao.DBHelper;
import org.dyndns.phpusr.domains.Data;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyCoastsMain extends Activity {

    private static String TAG = "MyCosts";

    private Button addCoast;
    private Button addForPassage;
    private ListView listView;
    private TextView sumMonth;

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

        addCoast = (Button) findViewById(R.id.addCoast);
        addForPassage = (Button) findViewById(R.id.addForPassage);
        listView = (ListView) findViewById(R.id.coastList);
        sumMonth = (TextView) findViewById(R.id.sumMonth);

        mDbHelper = new DBHelper(getApplicationContext());

        addCoast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyCoastsMain c = MyCoastsMain.this;
//                Intent intent = new Intent( c, AddCoast.class );
//                //intent.putExtra( COMPLEX_ID, id );
//                c.startActivity( intent );

                Intent intent = new Intent(getBaseContext(), AddCoast.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillList();
        sumMonth.setText(Double.toString(mDbHelper.getCoastSumLastMonth()));
    }
    
    private void fillList() {
        List<Data> dataList = mDbHelper.getCoastList();
        
        ArrayAdapter<Data> adapter = new MyCustomAdapter( this, R.layout.list, dataList);
        listView.setAdapter(adapter);

    }

    public class MyCustomAdapter extends ArrayAdapter<Data> {

        public MyCustomAdapter(Context context, int textViewResourceId, List<Data> items) {
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

    /**
     * Обработчик кнопки "Добавить за проезд"
     * @param view
     */
    public void addForPassageOnClick(View view) {
        //TODO сделать нормальный обработчик
        List<Data> dataList = mDbHelper.getCoastList();
        ArrayAdapter<Data> adapter = new MyCustomAdapter( this, R.layout.list, dataList);
        //new AlertDialog.Builder(this).setTitle("Title").setAdapter(adapter, null).show();
        Toast.makeText(this, "Зачем вы нажали?", Toast.LENGTH_SHORT).show();
        addForPassage.setText("Молодес нажал!");
    }
}

