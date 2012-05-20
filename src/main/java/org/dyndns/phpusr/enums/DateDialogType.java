package org.dyndns.phpusr.enums;

/**
 * @author phpusr
 *         Date: 20.05.12
 *         Time: 12:09
 */

/**
 * Типы вариантов DateDialog
 */
public enum DateDialogType {
    ADD_COAST(1),
    LUNCH_LIST(2),
    DRIVE_LIST(3)
    ;

    private int id;

    private DateDialogType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
