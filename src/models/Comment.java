package models;

/**
 * Created by daini on 3/4/2017.
 */
public class Comment {
    public String komentaro_ID;
    public int straipsnio_ID;
    public String atsakomoKoment_ID;
    public int puslapio_ID;
    public String slapyvardis;
    public java.util.Date komentaroData;
    public String IP_adresas;
    public String email;
    public java.util.Date paemimoData;
    public String komentaras;
    public String url;

    public int getStraipsnio_id() {
        return straipsnio_ID;
    }

    public void setStraipsnio_id(int straipsnio_ID) {
        this.straipsnio_ID = straipsnio_ID;
    }

    public String getAtsakomokoment_id() {
        return atsakomoKoment_ID;
    }

    public void setAtsakomokoment_id(String atsakomoKoment_ID) {
        this.atsakomoKoment_ID = atsakomoKoment_ID;
    }

    public String getSlapyvardis() {
        return slapyvardis;
    }

    public void setSlapyvardis(String slapyvardis) {
        this.slapyvardis = slapyvardis;
    }

    public java.util.Date getKomentarodata() {
        return komentaroData;
    }

    public void setKomentarodata(java.util.Date komentaroData) {
        this.komentaroData = komentaroData;
    }

    public String getIp_adresas() {
        return IP_adresas;
    }

    public void setIp_adresas(String IP_adresas) {
        this.IP_adresas = IP_adresas;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPuslapio_id() {
        return puslapio_ID;
    }

    public void setPuslapio_id(int puslapio_ID) {
        this.puslapio_ID = puslapio_ID;
    }

    public java.util.Date getPaemimodata() {
        return paemimoData;
    }

    public void setPaemimodata(java.util.Date paemimoData) {
        this.paemimoData = paemimoData;
    }

    public String getKomentaras() {
        return komentaras;
    }

    public void setKomentaras(String komentaras) {
        this.komentaras = komentaras;
    }

    public String getKomentaro_ID() {
        return komentaro_ID;
    }

    public void setKomentaro_ID(String komentaro_ID) {
        this.komentaro_ID = komentaro_ID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}