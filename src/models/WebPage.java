package models;

import javafx.beans.property.BooleanProperty;

import java.util.Date;

/**
 * Created by daini on 3/4/2017.
 */

public class WebPage {
    public String puslapio_vardas;
    public Date paemimo_data;
    public Integer puslapio_ID;
    public BooleanProperty isSelected;

    public String getPuslapio_vardas() {
        return puslapio_vardas;
    }

    public void setPuslapio_vardas(String puslapio_vardas) {
        this.puslapio_vardas = puslapio_vardas;
    }

    public Date getPaemimo_data() {
        return paemimo_data;
    }

    public void setPaemimo_data(Date paemimo_data) {
        this.paemimo_data = paemimo_data;
    }

    public Integer getPuslapio_ID() {
        return puslapio_ID;
    }

    public void setPuslapio_ID(Integer puslapio_ID) {
        this.puslapio_ID = puslapio_ID;
    }
}