package org.dyndns.phpusr.store;

import android.widget.DatePicker;

/**
 * Created by IntelliJ IDEA.
 * User: phpusr
 * Date: 09.03.12
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */
public class Store {
    public static DatePicker date;

    public static void setDate(DatePicker dateIn) {
        date = dateIn;
    }
    
    public static String getDate() {
        return date != null ? date.getDayOfMonth()+"."+date.getMonth()+"."+date.getYear() : "";
    }
}
