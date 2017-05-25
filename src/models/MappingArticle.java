package models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daini on 3/12/2017.
 */
public class MappingArticle {

    public String puslapisName;
    public String pageURL;
    public List<String> antraste;
    public List<String> kategorija;
    public List<String> tags;
    public List<String> turinys;
    public List<String> autorius;
    public List<String> parasymoData;
    public String straipsnioURL;
    public boolean isStraipsnisJSON;


    public boolean isCommentJSON;
    public String commentJSONkey;
    public String repliesJSONkey;
    public List<String> koment_Komentaras;
    public List<String> koment_Slapyvardis;
    public List<String> koment_userIP;
    public List<String> koment_commentDate;
    public List<String> koment_email;
    private BooleanProperty isSelected;
    public StringProperty puslapisNameProperty;
    public String koment_Tag;
    public String komentaroURL;
    public boolean hasPaging;
    public String koment_URL_ending;
    public String koment_NextPageTag;

    public MappingArticle()    {
        this.isSelected = new SimpleBooleanProperty(false);
        antraste = new ArrayList<>();
        kategorija = new ArrayList<>();
        tags = new ArrayList<>();
        turinys = new ArrayList<>();
        autorius = new ArrayList<>();
        parasymoData = new ArrayList<>();
        koment_Komentaras = new ArrayList<>();
        koment_Slapyvardis = new ArrayList<>();
        koment_commentDate = new ArrayList<>();
        koment_email = new ArrayList<>();
        koment_userIP = new ArrayList<>();

    }

    public BooleanProperty isSelected() { return isSelected; }

    public boolean isIsSelected() { return this.isSelected.get(); }

    public void setIsSelected(boolean value) { this.isSelected.set(value); }

    public StringProperty puslapisNameProperty() { return puslapisNameProperty; }

    public String getPuslapisNameProperty() { return this.puslapisNameProperty.get(); }

    public void setPuslapisNameProperty(String value) { this.puslapisNameProperty.set(value); }


}
