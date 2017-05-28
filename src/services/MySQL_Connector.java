package services; /**
 * Created by daini on 2/22/2017.
 */

import Utils.SQL_queries;
import models.Article;
import models.Comment;
import models.WebPage;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.List;

public class MySQL_Connector {
    //= "jdbc:mysql://localhost:3306/webcrawler";
    public static String DB_HOST = "localhost";    //localhost
    public static String DB_PORT = "3306";    //3306
    public static String DB_NAME = "";    //webcrawler
    public static String USER = "";      //= ."root";
    public static String PASS = "";      // = "123456";
    private static Sql2o db_connection;

    public static boolean mySQL_selected = true;


    public MySQL_Connector() {
        db_connection = new Sql2o("jdbc:mysql://" +DB_HOST +":" +DB_PORT +"/"+DB_NAME, USER, PASS);
        createTablesIfNotExisits();
        clearDatabase();
    }

    public static void clearDatabase(){
        try (org.sql2o.Connection con = db_connection.open()) {
            con.createQuery(SQL_queries.DELETE_ALL_STRAIPSNIS)
                    .executeUpdate();
            con.createQuery(SQL_queries.DELETE_ALL_PUSLAPIS)
                    .executeUpdate();
            con.createQuery(SQL_queries.DELETE_ALL_KOMENTARAS)
                    .executeUpdate();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void insertPuslapis(WebPage webPage, int pslID)
    {
        try (org.sql2o.Connection con = db_connection.open()) {

            con.createQuery(SQL_queries.INSERT_TO_PUSLAPIS)
                    .addParameter("puslapio_ID", pslID)
                    .addParameter("puslapio_vardas", webPage.getPuslapio_vardas())
                    .addParameter("paemimo_data", webPage.getPaemimo_data())
                    .executeUpdate();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void insertStraipsnis(Article article)
    {
        try (org.sql2o.Connection con = db_connection.open()) {
            con.createQuery(SQL_queries.INSERT_TO_STRAIPSNIS)
                    .addParameter("straipsnis_ID", article.id)
                    .addParameter("puslapio_ID", article.puslapio_id)
                    .addParameter("antraste", article.antraste)
                    .addParameter("kategorija", article.kategorija)
                    .addParameter("url", article.url)
                    .addParameter("tagai", article.tags)
                    .addParameter("autorius", article.autorius)
                    .addParameter("paemimoData", article.paemimoData)
                    .addParameter("turinys", article.turinys)
                    .addParameter("parasymoData", article.parasymoData)
                    .executeUpdate();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void insertABunchOfKomentarai(List<Comment> comments){
        if(comments != null){
            try (Connection con = db_connection.beginTransaction()) {
                Query query = con.createQuery(SQL_queries.INSERT_TO_KOMENTARAS);

                for (Comment comment : comments) {
                    query.addParameter("komentaro_ID", comment.komentaro_ID)
                            .addParameter("straipsnio_ID", comment.straipsnio_ID)
                            .addParameter("komentaras", comment.komentaras)
                            .addParameter("atsakomoKoment_ID", comment.atsakomoKoment_ID)
                            .addParameter("slapyvardis", comment.slapyvardis)
                            .addParameter("komentaroData", comment.komentaroData)
                            .addParameter("IP_adresas", comment.IP_adresas)
                            .addParameter("email", comment.email)
                            .addParameter("puslapio_ID", comment.puslapio_ID)
                            .addParameter("paemimoData", comment.paemimoData)
                            .addParameter("url", comment.url)
                            .addToBatch();
                }

                query.executeBatch(); // executes entire batch
                con.commit();         // remember to call commit(), else sql2o will automatically rollback.
            }
        }
    }

    private String createSelectTableQuery(String tableName){
        return "SELECT COUNT(*)\n" +
                "FROM information_schema.tables\n" +
                "WHERE table_schema = '"+DB_NAME+"'\n" +
                "AND table_name = '"+tableName+"'";
    }

    private void createTablesIfNotExisits() {
        Integer isKomentaras, isStraipsnis, isPuslapis;
        String kom = createSelectTableQuery("komentaras");
        String psl = createSelectTableQuery("puslapis");
        String strp = createSelectTableQuery("straipsnis");
        try (Connection con = db_connection.open()) {
            isKomentaras = con.createQuery(kom).executeScalar(Integer.class);
            isStraipsnis = con.createQuery(psl).executeScalar(Integer.class);
            isPuslapis = con.createQuery(strp).executeScalar(Integer.class);
        }
        if(isKomentaras != 1)
            createKomentaraTable();
        if(isStraipsnis != 1)
            createStraipsnisTable();
        if(isPuslapis != 1)
            createPuslapisTable();
    }

    private void createPuslapisTable(){
        String sql =
                "CREATE TABLE `puslapis` (\n" +
                        "`puslapio_ID` int(11) NOT NULL,\n" +
                        "`puslapio_vardas` varchar(45) DEFAULT NULL,\n" +
                        "`paemimo_data` datetime DEFAULT NULL,\n" +
                        "PRIMARY KEY (`puslapio_ID`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        try (Connection con = db_connection.open()) {
            con.createQuery(sql).executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createKomentaraTable(){
        String sql =
            "CREATE TABLE `komentaras` (\n" +
                "  `komentaro_ID` varchar(100) NOT NULL,\n" +
                "  `straipsnio_ID` int(11) DEFAULT NULL,\n" +
                "  `komentaras` text,\n" +
                "  `atsakomoKoment_ID` varchar(100) DEFAULT NULL,\n" +
                "  `slapyvardis` varchar(100) DEFAULT NULL,\n" +
                "  `komentaroData` date DEFAULT NULL,\n" +
                "  `IP_adresas` varchar(45) DEFAULT NULL,\n" +
                "  `email` varchar(45) DEFAULT NULL,\n" +
                "  `puslapio_ID` varchar(45) DEFAULT NULL,\n" +
                "  `paemimoData` date DEFAULT NULL,\n" +
                "  `URL` varchar(512) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`komentaro_ID`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
        try (Connection con = db_connection.open()) {
            con.createQuery(sql).executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createStraipsnisTable(){
        String sql =
                "CREATE TABLE `straipsnis` (\n" +
                        "  `straipsnis_id` int(11) NOT NULL,\n" +
                        "  `puslapio_ID` int(11) DEFAULT NULL,\n" +
                        "  `antraste` varchar(512) DEFAULT NULL,\n" +
                        "  `kategorija` varchar(512) DEFAULT NULL,\n" +
                        "  `URL` varchar(512) DEFAULT NULL,\n" +
                        "  `tagai` varchar(512) DEFAULT NULL,\n" +
                        "  `autorius` varchar(512) DEFAULT NULL,\n" +
                        "  `paemimoData` date DEFAULT NULL,\n" +
                        "  `turinys` text,\n" +
                        "  `parasymoData` date DEFAULT NULL,\n" +
                        "  PRIMARY KEY (`straipsnis_id`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";
        try (Connection con = db_connection.open()) {
            con.createQuery(sql).executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
