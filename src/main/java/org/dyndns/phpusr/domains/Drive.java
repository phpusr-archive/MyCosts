package org.dyndns.phpusr.domains;

import java.util.Date;

/**
 * @author phpusr
 *         Date: 07.05.12
 *         Time: 14:43
 */

/**
 * Информация о поездке на автобусе
 */
public class Drive {
    private int id;
    private Date date;
    /** Проезд */
    private Coast driveWay;

    public Drive() {
    }

    public Drive(Date date, Coast driveWay) {
        this.date = date;
        this.driveWay = driveWay;
    }

    public Drive(int id, Date date, Coast driveWay) {
        this.id = id;
        this.date = date;
        this.driveWay = driveWay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Coast getDriveWay() {
        return driveWay;
    }

    public void setDriveWay(Coast driveWay) {
        this.driveWay = driveWay;
    }

    @Override
    public String toString() {
        return "Drive{" +
                "id=" + id +
                ", date=" + date +
                ", driveWay=" + driveWay +
                '}';
    }
}
