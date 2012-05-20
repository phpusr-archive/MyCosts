package org.dyndns.phpusr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.dyndns.phpusr.constants.Constants;
import org.dyndns.phpusr.dao.DBHelper;
import org.dyndns.phpusr.domains.Drive;
import org.dyndns.phpusr.domains.Lunch;
import org.dyndns.phpusr.enums.DateDialogType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author phpusr
 *         Date: 20.05.12
 *         Time: 12:42
 */

/**
 * Активити для просмотра списков обедов и поездок
 */
public class ListReport extends Activity {
    private ListView listReport;
    private TextView lblLunchFor;

    private DBHelper mDbHelper;
    private SimpleDateFormat sdf = new SimpleDateFormat(Constants.PEOPLE_DATE_FORMAT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_report);

        listReport = (ListView) findViewById(R.id.listReport);
        lblLunchFor = (TextView) findViewById(R.id.lblLunchFor);

        mDbHelper = new DBHelper(getApplicationContext());
    }

    public static void callMe(Context context, DateDialogType type, Date date, boolean month) {
        Intent intent = new Intent(context, ListReport.class);
        intent.putExtra(Constants.DATE_DIALOG_TYPE, type);
        intent.putExtra(Constants.DATE, date);
        intent.putExtra(Constants.MONTH, month);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final DateDialogType dialogType = (DateDialogType) getIntent().getExtras().get(Constants.DATE_DIALOG_TYPE);
        final Date date = (Date) getIntent().getExtras().get(Constants.DATE);
        final boolean month = getIntent().getExtras().getBoolean(Constants.MONTH);

        if (dialogType == DateDialogType.LUNCH_LIST) {
            lblLunchFor.setText(getApplicationContext().getResources().getString(R.string.lunchFor) + " (" + sdf.format(date) + ")");

            final List<Lunch> lunchList = mDbHelper.getLunchListByDate(date, month);
            ArrayAdapter<Lunch> adapter = new LunchAdapter( this, R.layout.lunch, lunchList );
            listReport.setAdapter(adapter);
        } else {
            if (dialogType == DateDialogType.DRIVE_LIST) {
                final List<Drive> driveList = mDbHelper.getDriveListByDate(date);
                ArrayAdapter<Drive> adapter = new DriveAdapter( this, R.layout.lunch, driveList );
                listReport.setAdapter(adapter);
            }
        }
    }

    /** Адаптер для списка обедов */
    private class LunchAdapter extends ArrayAdapter<Lunch> {

        public LunchAdapter(Context context, int textViewResourceId, List<Lunch> items) {
            super( context, textViewResourceId, items );
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent ) {
            View v = convertView;
            if ( v == null ) {
                LayoutInflater vi = ( LayoutInflater ) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                v = vi.inflate( R.layout.lunch, null );
            }

            Lunch lunch = getItem( position );

            fillText(v, R.id.lunchDate, sdf.format(lunch.getDate()));
            fillText(v, R.id.lunchSum, Double.toString(lunch.getSum()));
            fillText(v, R.id.lunchDrink, lunch.getDrink().getName());
            fillText(v, R.id.lunchGarnish, lunch.getGarnish().getName());
            fillText(v, R.id.lunchMeat, lunch.getMeat().getName());
            fillText(v, R.id.lunchSalad, lunch.getSalad().getName());
            fillText(v, R.id.lunchFlour, lunch.getFlour().getName());

            return v;
        }

        private void fillText( View v, int id, String text ) {
            TextView textView = ( TextView ) v.findViewById( id );
            textView.setText( text == null ? "" : text );
        }
    }

    /** Адаптер для списка поездок */
    private class DriveAdapter extends ArrayAdapter<Drive> {

        public DriveAdapter(Context context, int textViewResourceId, List<Drive> items) {
            super( context, textViewResourceId, items );
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent ) {
            View v = convertView;
            if ( v == null ) {
                LayoutInflater vi = ( LayoutInflater ) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                v = vi.inflate( R.layout.lunch, null );
            }

            Drive drive = getItem( position );
            /* //TODO
            fillText(v, R.id.lunchDate, sdf.format(drive.getDate()));
            fillText(v, R.id.lunchSum, Double.toString(drive.getSum()));
            fillText(v, R.id.lunchDrink, drive.getDrink().getName());
            fillText(v, R.id.lunchGarnish, drive.getGarnish().getName());
            fillText(v, R.id.lunchMeat, drive.getMeat().getName());
            fillText(v, R.id.lunchSalad, drive.getSalad().getName());
            fillText(v, R.id.lunchFlour, drive.getFlour().getName());
            */
            return v;
        }

        private void fillText( View v, int id, String text ) {
            TextView textView = ( TextView ) v.findViewById( id );
            textView.setText( text == null ? "" : text );
        }
    }
}
