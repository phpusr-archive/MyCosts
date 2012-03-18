package org.dyndns.phpusr.domains;

import java.util.Date;

/**
 * @author Sergey Doronin
 *         Date: 10.03.12
 *         Time: 17:40
 */

/** Класс для хранения содержимого покупки, даты и стоимости покупки */
//TODO поменять название и добавить поля
public class Data {
    private int id;
    private double sum;
    private Date date;

    public Data() {
    }

    public Data(double sum) {
        this.sum = sum;
    }

    public Data(int id, double sum, Date date) {
        this.id = id;
        this.sum = sum;
        this.date = date;
    }

    public Data(double sum, Date date) {
        this.sum = sum;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", sum=" + sum +
                ", date=" + date +
                '}';
    }
}
