package org.dyndns.phpusr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import org.dyndns.phpusr.dao.DBHelper;
import org.dyndns.phpusr.domains.Coast;
import org.dyndns.phpusr.domains.Drive;
import org.dyndns.phpusr.enums.CoastType;
import org.dyndns.phpusr.store.Store;

import java.util.List;

/**
 * @author phpusr
 *         Date: 07.05.12
 *         Time: 14:06
 */

/**
 * Добавление поездки на автобусе
 */
public class AddDrive extends Activity {
    private Spinner spinnerBus;
    private TextView driveDate;

    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_drive);

        spinnerBus = (Spinner) findViewById(R.id.spinnerBus);
        driveDate = (TextView) findViewById(R.id.driveDate);

        mDbHelper = new DBHelper(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<Coast> adapter = new MyCustomAdapter( this, R.layout.list, mDbHelper.getCoastItemsByTypeId(CoastType.BUS.getId()));
        spinnerBus.setAdapter(adapter);
        driveDate.setText(Store.getDateString());
    }

    public void onClickAddDrive(View view) {
        mDbHelper.insertIntoDriveList(new Drive(Store.getDate(), (Coast) spinnerBus.getSelectedItem()));
        onBackPressed();
    }

    public void onClickCancelDrive(View view) {
        onBackPressed();
    }

    public void onClickDriveDate(View view) {
        DateDialog.callMe(this);
    }

    class MyCustomAdapter extends ArrayAdapter<Coast> {

        public MyCustomAdapter(Context context, int textViewResourceId, List<Coast> items) {
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
            label.setText(getItem(pos).getName() +
                    " (" +  + getItem(pos).getPrice() + " " + getResources().getString(R.string.currencyPostfix) + ")");

            return row;
        }
    }

    public static void callMe(Context context) {
        Intent intent = new Intent( context, AddDrive.class );
        context.startActivity(intent);
    }
}
