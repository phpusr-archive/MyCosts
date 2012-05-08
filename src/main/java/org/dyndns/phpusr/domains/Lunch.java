package org.dyndns.phpusr.domains;

import java.util.Date;

/**
 * @author Sergey Doronin
 *         Date: 10.03.12
 *         Time: 17:40
 */

/** Класс для хранения купленной еды на обеде, даты и стоимости покупки */
public class Lunch {
    private int id;
    private double sum;
    private Date date;

    //Еда
    private Coast drink;
    private Coast garnish;
    private Coast meat;
    private Coast salad;
    private Coast flour;

    public Lunch() {
    }

    public Lunch(double sum) {
        this.sum = sum;
    }

    public Lunch(int id, double sum, Date date) {
        this.id = id;
        this.sum = sum;
        this.date = date;
    }

    public Lunch(double sum, Date date) {
        this.sum = sum;
        this.date = date;
    }

    public Lunch(double sum, Date date, Coast drink, Coast garnish, Coast meat, Coast salad, Coast flour) {
        this.sum = sum;
        this.date = date;
        this.drink = drink;
        this.garnish = garnish;
        this.meat = meat;
        this.salad = salad;
        this.flour = flour;
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

    public Coast getDrink() {
        return drink;
    }

    public void setDrink(Coast drink) {
        this.drink = drink;
    }

    public Coast getGarnish() {
        return garnish;
    }

    public void setGarnish(Coast garnish) {
        this.garnish = garnish;
    }

    public Coast getMeat() {
        return meat;
    }

    public void setMeat(Coast meat) {
        this.meat = meat;
    }

    public Coast getSalad() {
        return salad;
    }

    public void setSalad(Coast salad) {
        this.salad = salad;
    }

    public Coast getFlour() {
        return flour;
    }

    public void setFlour(Coast flour) {
        this.flour = flour;
    }

    @Override
    public String toString() {
        return "Lunch{" +
                "id=" + id +
                ", sum=" + sum +
                ", date=" + date +
                ", drink=" + drink +
                ", garnish=" + garnish +
                ", meat=" + meat +
                ", salad=" + salad +
                ", flour=" + flour +
                '}';
    }
}
