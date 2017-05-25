package Utils;

/**
 * Created by daini on 3/5/2017.
 */
public class SQL_queries {
    public final static String DELETE_ALL_STRAIPSNIS = "DELETE FROM straipsnis;";
    public final static String DELETE_ALL_PUSLAPIS = "DELETE FROM puslapis;";
    public final static String DELETE_ALL_KOMENTARAS = "DELETE FROM komentaras;";

    public final static String INSERT_TO_PUSLAPIS =
            "INSERT INTO puslapis(puslapio_ID, puslapio_vardas, paemimo_data) " +
                    "VALUES (:puslapio_ID, :puslapio_vardas, :paemimo_data)";

    public final static String INSERT_TO_STRAIPSNIS =
            "INSERT INTO straipsnis(straipsnis_ID, puslapio_ID, antraste, kategorija, turinys, url, tagai, parasymoData, " +
                    "autorius, paemimoData) VALUES " +
                    "( :straipsnis_ID, :puslapio_ID, :antraste, :kategorija, :turinys, :url, :tagai, :parasymoData, " +
                    ":autorius, :paemimoData)";

    public final static String INSERT_TO_KOMENTARAS =
            "INSERT INTO komentaras(komentaro_ID, straipsnio_ID, komentaras, atsakomoKoment_ID, slapyvardis, komentaroData, IP_adresas, email, " +
                    "puslapio_ID, paemimoData, url) VALUES " +
                    "( :komentaro_ID, :straipsnio_ID, :komentaras, :atsakomoKoment_ID, :slapyvardis, :komentaroData, :IP_adresas, :email, " +
                    ":puslapio_ID, :paemimoData, :url)";
}
