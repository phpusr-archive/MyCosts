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
import org.dyndns.phpusr.enums.TypeCoast;

import java.util.List;

/**
 * @author Sergey Doronin
 *         Date: 27.03.12
 *         Time: 18:50
 */
public class AddProduct extends Activity {
    private Spinner spinnerProductType;
    private TextView productName;

    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        spinnerProductType = (Spinner) findViewById(R.id.spinnerProductType);
        productName = (TextView) findViewById(R.id.productName);

        mDbHelper = new DBHelper(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillProductType();
    }

    private void fillProductType() {
        ArrayAdapter<TypeCoast> adapter = new MyCustomAdapter(this, R.layout.list, TypeCoast.getList());
        spinnerProductType.setAdapter(adapter);
    }

    public class MyCustomAdapter extends ArrayAdapter<TypeCoast> {

        public MyCustomAdapter(Context context, int textViewResourceId, List<TypeCoast> items) {
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
            label.setText(getItem(position).getDesctiption());

            return row;
        }
    }

    public void onClickAddCoastItem(View view) {
        Coast coast = new Coast();
        coast.setName(productName.getText().toString());
        coast.setPrice(0);
        coast.setCoastType(((TypeCoast) spinnerProductType.getSelectedItem()).getId());
        mDbHelper.insertIntoCoastItems(coast);

        onBackPressed();
    }

    public static void callMe(Context context) {
        Intent intent = new Intent( context, AddProduct.class );
        context.startActivity(intent);
    }
}
