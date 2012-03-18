package org.dyndns.phpusr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.dyndns.phpusr.dao.DBHelper;
import org.dyndns.phpusr.domains.Coast;
import org.dyndns.phpusr.domains.Data;
import org.dyndns.phpusr.store.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: phpusr
 * Date: 08.03.12
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */


public class AddCoast extends Activity {
    private TextView coastDateLabel;
    private TextView coastDate;
    private TextView expectedPrice;
    private TextView price;
    
    private Spinner spinnerDrink;
    private Spinner spinnerGarnish;
    private Spinner spinnerMeat;
    
    private double priceDrink = 0, priceGarnish = 0, priceMeat = 0, coastsSum = 0;

    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_coast);
        
        coastDateLabel = (TextView) findViewById(R.id.coastDateLabel);
        coastDate = (TextView) findViewById(R.id.coastDate);
        expectedPrice = (TextView) findViewById(R.id.expectedPrice);
        price = (TextView) findViewById(R.id.price);

        spinnerDrink = (Spinner) findViewById(R.id.spinnerDrink);
        spinnerGarnish = (Spinner) findViewById(R.id.spinnerGarnish);
        spinnerMeat = (Spinner) findViewById(R.id.spinnerMeat);

        mDbHelper = new DBHelper(getApplicationContext());

        coastDateLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), DateDialog.class);
                startActivity(intent);
            }
        });

        spinnerDrink.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long row) {
                Coast coast = (Coast) spinnerDrink.getAdapter().getItem(position);
                if (coast != null) {
                    priceDrink = coast.getPrice();
                    generateSum();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinnerGarnish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long row) {
                Coast coast = (Coast) spinnerGarnish.getAdapter().getItem(position);
                if (coast != null) {
                    priceGarnish = coast.getPrice();
                    generateSum();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinnerMeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long row) {
                Coast coast = (Coast) spinnerMeat.getAdapter().getItem(position);
                if (coast != null) {
                    priceMeat = coast.getPrice();
                    generateSum();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        ((Button)findViewById(R.id.addCoastCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();                
            }
        });

        ((Button)findViewById(R.id.addCoastOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbHelper.insetIntoCoastList(new Data(getCoastSum(), Store.getDate()));
                //TODO сохранение покупки в базу
                onBackPressed();
            }
        });
    }

    private double getCoastSum() {
        if (price.getText() != null && price.getText().equals("")) {
            price.setText("0");
        }
        return Double.valueOf(price.getText().toString());
    }

    private void generateSum() {
        coastsSum = priceDrink + priceGarnish + priceMeat;
        expectedPrice.setText(Double.toString(coastsSum));
    }

    private void fillList() {
        List<Coast> coastList = new ArrayList<Coast>();
        coastList.add(new Coast(23, "Компот", 12));
        coastList.add(new Coast(48, "Сок", 14));
        coastList.add(new Coast(53, "Чай", 5));
        ArrayAdapter<Coast> adapter = new MyCustomAdapter( this, R.layout.list, coastList);
        spinnerDrink.setAdapter(adapter);

        coastList = new ArrayList<Coast>();
        coastList.add(new Coast(23, "Картофельное пюре", 8));
        coastList.add(new Coast(48, "Плов", 40));
        coastList.add(new Coast(53, "Гречка", 13));
        adapter = new MyCustomAdapter( this, R.layout.list, coastList);
        spinnerGarnish.setAdapter(adapter);

        coastList = new ArrayList<Coast>();
        coastList.add(new Coast(23, "Гуляш", 40));
        coastList.add(new Coast(48, "Катлета", 20));
        coastList.add(new Coast(53, "Печень", 18));
        adapter = new MyCustomAdapter( this, R.layout.list, coastList);
        spinnerMeat.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillList();
        coastDate.setText(Store.getDateString());

    }

    public class MyCustomAdapter extends ArrayAdapter<Coast>{

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

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.list, parent, false);
            TextView label=(TextView)row.findViewById(R.id.list);
            label.setText(getItem(position).getName());

            return row;
        }
    }

}
