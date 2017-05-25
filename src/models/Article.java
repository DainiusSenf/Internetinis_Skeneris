package models;

import java.util.Date;

/**
 * Created by daini on 3/4/2017.
 */

public class Article {
    public String id;
    public int puslapio_id;
    public String antraste;
    public String kategorija;
    public String url;
    public String autorius;
    public String tags;
    public String turinys;
    public Date paemimoData;
    public Date parasymoData;

    public int getPuslapio_id() {
        return puslapio_id;
    }

    public void setPuslapio_id(int puslapio_id) {
        this.puslapio_id = puslapio_id;
    }

    public String getAntraste() {
        return antraste;
    }

    public void setAntraste(String antraste) {
        this.antraste = antraste;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String URL) {
        this.url = URL;
    }

    public String getAutorius() {
        return autorius;
    }

    public void setAutorius(String autorius) {
        this.autorius = autorius;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTurinys() {
        return turinys;
    }

    public void setTurinys(String turinys) {
        this.turinys = turinys;
    }

    public Date getPaemimoData() {
        return paemimoData;
    }

    public void setPaemimoData(Date today) {
        this.paemimoData = today;
    }

    public Date getParasymoData() {
        return parasymoData;
    }

    public void setParasymoData(Date parasymoData) {
        this.parasymoData = parasymoData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}