package org.dyndns.phpusr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import org.dyndns.phpusr.store.Store;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_coast);
        coastDateLabel = (TextView) findViewById(R.id.coastDateLabel);
        coastDate = (TextView) findViewById(R.id.coastDate);

        coastDateLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), DateDialog.class);
                startActivity(intent);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinnerDrink);

        ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(this, R.array.drinks,
                android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        coastDate.setText(Store.getDate());
    }
}
