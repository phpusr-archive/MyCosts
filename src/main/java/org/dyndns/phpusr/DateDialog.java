package org.dyndns.phpusr;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import org.dyndns.phpusr.store.Store;

/**
 * Created by IntelliJ IDEA.
 * User: phpusr
 * Date: 09.03.12
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class DateDialog extends Activity {
    private Button ok;
    private Button cancel;
    private DatePicker date;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_dialog);
        
        date = (DatePicker) findViewById(R.id.datePicker);
        ok = (Button) findViewById(R.id.ok);
        cancel = (Button) findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Store.setDate(date);
                onBackPressed();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
