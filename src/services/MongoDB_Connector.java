package services;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import models.Article;
import models.Comment;
import models.WebPage;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by daini on 17/04/05.
 */
public class MongoDB_Connector {

    public static String MONGO_DB_HOST = "localhost";    //= "jdbc:mysql://localhost:3306/webcrawler";
    public static Integer MONGO_DB_PORT = 27017;
    public static String MONGO_DB_NAME = "";    //= "jdbc:mysql://localhost:3306/webcrawler";
    public static String MONGO_USER = "";      //= ."root";
    public static String MONGO_PASS = "";      // = "123456";
    public static MongoClient mongoClient;
    public static MongoDatabase database;

    public static boolean mongoDB_selected = false;

    public MongoDB_Connector(){
        mongoClient = new MongoClient( MONGO_DB_HOST , MONGO_DB_PORT );
        database = mongoClient.getDatabase(MONGO_DB_NAME);
    }

    public static void clearDatabase(){
        MongoCollection<Document> puslapis_table = database.getCollection("webPage");
        MongoCollection<Document> straipsnis_table = database.getCollection("article");
        MongoCollection<Document> komentaras_table = database.getCollection("komentaras");
        puslapis_table.drop();
        straipsnis_table.drop();
        komentaras_table.drop();
    }



    public static void insertPuslapis(WebPage webPage, int pslID)
    {
        MongoCollection<Document> puslapis_table = database.getCollection("webPage");
        Date today = Calendar.getInstance().getTime();
        Document puslapis_Doc = new Document("puslapio_ID", pslID)
                .append("puslapio_vardas", webPage.puslapio_vardas)
                .append("paemimo_data", today);
        puslapis_table.insertOne(puslapis_Doc);
    }


    public void insertStraipsnis(Article article)
    {
        MongoCollection<Document> straipsnis_table = database.getCollection("article");

        Document straipsnis_Doc = new Document("straipsnis_ID",  article.id)
                .append("puslapio_ID", 1)
                .append("antraste", article.antraste)
                .append("kategorija", article.kategorija)
                .append("url", article.url)
                .append("tagai", article.tags)
                .append("autorius", article.autorius)
                .append("paemimoData", article.paemimoData)
                .append("turinys", article.turinys)
                .append("parasymoData", article.parasymoData);

        straipsnis_table.insertOne(straipsnis_Doc);
    }

    public void insertABunchOfKomentarai(List<Comment> comments){
        if(comments != null){
            MongoCollection<Document> komentaras_table = database.getCollection("komentaras");
            List<Document> komentarai = new ArrayList<Document>();
            for (Comment comment : comments) {
                Document komentaras_Doc = new Document("komentaro_ID", comment.komentaro_ID)
                        .append("straipsnio_ID", comment.straipsnio_ID)
                        .append("komentaras", comment.komentaras)
                        .append("atsakomoKoment_ID", comment.atsakomoKoment_ID)
                        .append("slapyvardis", comment.slapyvardis)
                        .append("komentaroData", comment.komentaroData)
                        .append("IP_adresas", comment.IP_adresas)
                        .append("email", comment.email)
                        .append("puslapio_ID", comment.puslapio_ID)
                        .append("paemimoData", comment.paemimoData)
                        .append("url", comment.url);
                komentarai.add(komentaras_Doc);
            }
            komentaras_table.insertMany(komentarai);
        }
    }


}
