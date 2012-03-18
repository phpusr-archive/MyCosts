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
    private static Date date;

    private static final SimpleDateFormat sdf = new SimpleDateFormat( "dd.MM.yyyy" );

    public static void setDate(DatePicker dateIn) {
        date = new Date(date.getYear(), date.getMonth(), date.getDay());
    }
    
    public static Date getDate() {
        if (date == null) {
            date = new Date();
        }
        return date;
    }
    
    public static String getDateString() {
        if (date == null) {
            date = new Date();
        }
        return sdf.format(date);
    }
}
