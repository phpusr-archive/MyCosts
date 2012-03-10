package org.dyndns.phpusr.store;

import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: phpusr
 * Date: 09.03.12
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */

/** Хранилище данных для формы добавления покупок */
public class Store {
    private static DatePicker date;

    private static final SimpleDateFormat formatter = new SimpleDateFormat( "dd.MM.yyyy" );

    public static void setDate(DatePicker dateIn) {
        date = dateIn;
    }
    
    public Date getDate() {
        return new Date(date.getYear(), date.getMonth(), date.getDayOfMonth());
    }
    
    public static String getDateString() {
        return date != null ? date.getDayOfMonth()+"."+(date.getMonth()+1)+"."+date.getYear() : formatter.format(new Date());
    }
}
