package org.dyndns.phpusr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MyCoastsMain extends Activity {

    private static String TAG = "MyCosts";

    private Button addCoast;

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

    }

}

