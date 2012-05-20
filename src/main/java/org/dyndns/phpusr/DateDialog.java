package org.dyndns.phpusr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import org.dyndns.phpusr.constants.Constants;
import org.dyndns.phpusr.enums.DateDialogType;
import org.dyndns.phpusr.store.Store;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: phpusr
 * Date: 09.03.12
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class DateDialog extends Activity {
    private Button ok;
    private DatePicker date;
    private LinearLayout pnlOkCancel;
    private LinearLayout pnlReport;

    private DateDialogType dialogType;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_dialog);
        
        date = (DatePicker) findViewById(R.id.datePicker);
        ok = (Button) findViewById(R.id.ok);

        pnlOkCancel = (LinearLayout) findViewById(R.id.pnlOkCancel);
        pnlReport = (LinearLayout) findViewById(R.id.pnlReport);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Store.setDate(date);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        dialogType = (DateDialogType) getIntent().getExtras().get(Constants.DATE_DIALOG_TYPE);
        if (dialogType == DateDialogType.ADD_COAST) {
            pnlOkCancel.setVisibility(View.VISIBLE);
            pnlReport.setVisibility(View.INVISIBLE);
        } else {
            if (dialogType == DateDialogType.LUNCH_LIST || dialogType == DateDialogType.DRIVE_LIST) {
                pnlOkCancel.setVisibility(View.INVISIBLE);
                pnlReport.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void callMe(Context context, DateDialogType type) {
        Intent intent = new Intent( context, DateDialog.class );
        intent.putExtra(Constants.DATE_DIALOG_TYPE, type);
        context.startActivity(intent);
    }

    public void onClickCancelDateDialog(View view) {
        finish();
    }

    /** Обработчик кнопки "За месяц" */
    public void onClickLastMonth(View view) {
        ListReport.callMe(this, dialogType, getDate(), true);
    }

    /** Обработчик кнопки "За день" */
    public void onClickLastDay(View view) {
        ListReport.callMe(this, dialogType, getDate(), false);
    }

    /** Возвращает дату из DatePicker */
    private Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), date.getDayOfMonth());
        return calendar.getTime();
    }

}
