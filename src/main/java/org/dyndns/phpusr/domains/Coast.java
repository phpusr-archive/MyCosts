package org.dyndns.phpusr.domains;

/**
 * @author Sergey Doronin
 *         Date: 09.03.12
 *         Time: 18:00
 */

/** Покупка */
public class Coast {
    private int id;
    private String name;
    private double price;
    private int coastType;

    public Coast() {
    }

    public Coast(int id, String name, double price, int coastType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.coastType = coastType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCoastType() {
        return coastType;
    }

    public void setCoastType(int coastType) {
        this.coastType = coastType;
    }

    @Override
    public String toString() {
        return "Coast{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", coastType=" + coastType +
                '}';
    }
}
