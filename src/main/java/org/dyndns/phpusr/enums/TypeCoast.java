package org.dyndns.phpusr.enums;

/**
 * @author phpusr
 *         Date: 25.03.12
 *         Time: 22:21
 */

/**
 * Типы покупок
 */
public enum TypeCoast {

    DRINK(1, "Напиток"),
    GARNISH(2, "Гарнир"),
    MEAT(3, "Мясные изделия");

    private int id;

    /**Описание покупки*/
    private String desctiption;

    private TypeCoast(int id, String desctiption) {
        this.id = id;
        this.desctiption = desctiption;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }

    @Override
    public String toString() {
        return "TypeCoast{" +
                "id=" + id +
                ", desctiption='" + desctiption + '\'' +
                '}';
    }
}
