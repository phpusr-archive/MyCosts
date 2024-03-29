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
import org.dyndns.phpusr.enums.CoastType;

import java.util.List;

/**
 * @author Sergey Doronin
 *         Date: 27.03.12
 *         Time: 18:50
 */
public class AddProduct extends Activity {
    private Spinner spinnerProductType;
    private TextView productName;
    private TextView productPrice;

    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        spinnerProductType = (Spinner) findViewById(R.id.spinnerProductType);
        productName = (TextView) findViewById(R.id.productName);
        productPrice = (TextView) findViewById(R.id.productPrice);

        mDbHelper = new DBHelper(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillProductType();
    }

    private void fillProductType() {
        ArrayAdapter<CoastType> adapter = new MyCustomAdapter(this, R.layout.list, CoastType.getList());
        spinnerProductType.setAdapter(adapter);
    }

    public class MyCustomAdapter extends ArrayAdapter<CoastType> {

        public MyCustomAdapter(Context context, int textViewResourceId, List<CoastType> items) {
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
            View row = inflater.inflate(R.layout.list, parent, false);
            TextView label=(TextView)row.findViewById(R.id.list);
            label.setText(getItem(position).getDesctiption());

            return row;
        }
    }

    public void onClickAddCoastItem(View view) {
        Coast coast = new Coast();
        coast.setName(productName.getText().toString());
        coast.setPrice(Double.valueOf(productPrice.getText().toString()));
        coast.setCoastType((CoastType) spinnerProductType.getSelectedItem());
        mDbHelper.insertIntoCoastItems(coast);

        onBackPressed();
    }

    public void onClickCancelCoastItem(View view) {
        onBackPressed();
    }

    public static void callMe(Context context) {
        Intent intent = new Intent( context, AddProduct.class );
        context.startActivity(intent);
    }
}
