package org.dyndns.phpusr.enums;

/**
 * @author phpusr
 *         Date: 25.03.12
 *         Time: 22:21
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Типы покупок
 */
public enum CoastType {

    DRINK(1, "Напиток"),
    GARNISH(2, "Гарнир"),
    MEAT(3, "Мясные изделия"),
    BUS(4, "Автобусные поездки");

    private int id;

    /**Описание покупки*/
    private String desctiption;

    private CoastType(int id, String desctiption) {
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

    /**
     * Список всех типов покупок
     * @return список
     */
    public static List<CoastType> getList() {
        List<CoastType> list = new ArrayList<CoastType>();
        list.add(DRINK);
        list.add(GARNISH);
        list.add(MEAT);
        list.add(BUS);

        return list;
    }

    public static CoastType getCoastTypeById(int id) {
        for (CoastType coastType : getList()) {
            if (coastType.getId() == id) {
                return coastType;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "CoastType{" +
                "id=" + id +
                ", desctiption='" + desctiption + '\'' +
                '}';
    }
}
