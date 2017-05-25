package config;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import models.MappingArticle;
import org.hildan.fxgson.FxGson;
import view.MainView;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by daini on 3/7/2017.
 */
public class MappingController {


    public MappingController(){

    }


    public static MappingArticle allMap() {
        MappingArticle map = new MappingArticle();
        map.antraste = Lists.newArrayList("og:title");
        map.kategorija = Lists.newArrayList("cxenseparse:recs:category", "article:section");
        map.tags = Lists.newArrayList("keywords");
        map.turinys = Lists.newArrayList("div.articleBody p", "[itemprop=articleBody]", "div.article_content p");
        map.autorius = Lists.newArrayList("og:article:author", "article:author", ".author-wrap");
        map.parasymoData = Lists.newArrayList("[itemprop=datePublished]");
        map.koment_commentDate = Lists.newArrayList(".comment-date", "date");
        map.koment_Komentaras = Lists.newArrayList(".comment-content-inner", "comment");
        map.koment_Slapyvardis = Lists.newArrayList(".comment-author", "user_name");
        map.koment_userIP = Lists.newArrayList(".comment-date", "user_ip");
        map.isCommentJSON = false;
        return map;
    }

    public static MappingArticle loadDienaMap()
    {
        MappingArticle map = new MappingArticle();
        map.pageURL = "http://www.diena.lt/";
        map.antraste = Lists.newArrayList("og:title");
        map.kategorija =  Lists.newArrayList(".custom-breadcrumbs-item-1");
        map.tags = Lists.newArrayList("name=keywords");
        map.turinys = Lists.newArrayList("[itemprop=articleBody]");
        map.autorius = Lists.newArrayList("itemprop=author");
        map.parasymoData = Lists.newArrayList("itemprop=datePublished");
        map.koment_commentDate = Lists.newArrayList("time");
        map.koment_Komentaras = Lists.newArrayList(".user-comment-comment");
        map.koment_Slapyvardis = Lists.newArrayList("property=dc:title");
        map.koment_userIP = Lists.newArrayList(".ip");
        map.isCommentJSON = false;
        map.puslapisName = "diena";
        map.koment_Tag = ".comment-by-anonymous ";
        map.isStraipsnisJSON = false;
        map.puslapisNameProperty = new SimpleStringProperty("diena");
        return  map;
    }

    public static MappingArticle loadAlfasMap()
    {
        MappingArticle map = new MappingArticle();
        map.pageURL = "http://www.alfa.lt/";
        map.antraste = Lists.newArrayList("og:title");
        map.kategorija =  Lists.newArrayList("article:section");
        map.tags = Lists.newArrayList("keywords");
        map.turinys = Lists.newArrayList(".article__content p");
        map.autorius = Lists.newArrayList("author");
        map.parasymoData = Lists.newArrayList("itemprop=datePublished");
        map.koment_commentDate = Lists.newArrayList(".date");
        map.koment_Komentaras = Lists.newArrayList(".break-word");
        map.koment_Slapyvardis = Lists.newArrayList(".name");
        map.koment_userIP = Lists.newArrayList(".ip");
        map.isCommentJSON = false;
        map.puslapisName = "alfa";
        map.koment_Tag = ".one-comment";
        map.isStraipsnisJSON = false;
        map.puslapisNameProperty = new SimpleStringProperty("alfa");
        return  map;
    }

    public static MappingArticle loadVZsMap()
    {
        MappingArticle map = new MappingArticle();
        map.pageURL = "http://www.vz.lt/";
        map.antraste = Lists.newArrayList("og:title");
        map.kategorija =  Lists.newArrayList("og:article:section");
        map.tags = Lists.newArrayList("keywords");
        map.turinys = Lists.newArrayList("[itemprop=articleBody]");
        map.autorius = Lists.newArrayList("og:article:author");
        map.parasymoData = Lists.newArrayList(".date-current");
        map.koment_commentDate = Lists.newArrayList(".date");
        map.koment_Komentaras = Lists.newArrayList(".break-word");
        map.koment_Slapyvardis = Lists.newArrayList(".name");
        map.koment_userIP = Lists.newArrayList(".ip");
        map.isCommentJSON = false;
        map.puslapisName = "vz";
        map.koment_Tag = ".one-comment";
        map.isStraipsnisJSON = false;
        map.puslapisNameProperty = new SimpleStringProperty("vz");
        return  map;
    }

    public static MappingArticle loadLRytasMap()
    {
        MappingArticle map = new MappingArticle();
        map.pageURL = "http://www.lrytas.lt/";
        map.antraste = Lists.newArrayList("og:title");
        map.kategorija =  Lists.newArrayList("BLOKAS_SLUG");
        map.tags = Lists.newArrayList("keywords");
        map.turinys = Lists.newArrayList("FULL_TEXT");
        map.autorius = Lists.newArrayList("og:article:author");
        map.parasymoData = Lists.newArrayList("DATA");
        map.koment_commentDate = Lists.newArrayList(".time");
        map.koment_Komentaras = Lists.newArrayList(".com p");
        map.koment_Slapyvardis = Lists.newArrayList("h2");
        map.koment_userIP = Lists.newArrayList(".report");
        map.isCommentJSON = false;
        map.puslapisName = "lrytas";
        map.koment_Tag = ".com";
        map.isStraipsnisJSON = true;
        map.puslapisNameProperty = new SimpleStringProperty("lrytas");
        return  map;
    }

    public static MappingArticle loadTv3Map()
    {
        MappingArticle map = new MappingArticle();
        map.pageURL = "http://www.tv3.lt/";
        map.antraste = Lists.newArrayList("og:title");
        map.kategorija =  Lists.newArrayList( "articleSection");
        map.tags = Lists.newArrayList("keywords");
        map.turinys = Lists.newArrayList("div.arcticle_content  p");
        map.autorius = Lists.newArrayList(".authorFullName", "[itemprop=author]");
        map.parasymoData = Lists.newArrayList("[itemprop=datePublished]");
        map.koment_commentDate = Lists.newArrayList(".date");
        map.koment_Komentaras = Lists.newArrayList(".cont");
        map.koment_Slapyvardis = Lists.newArrayList(".user_name");
        map.koment_userIP = Lists.newArrayList(".user_ip");
        map.isCommentJSON = false;
        map.puslapisName = "tv3";
        map.koment_Tag = ".jst_comments_list";
        map.puslapisNameProperty = new SimpleStringProperty("tv3");

        return  map;

    }

    public static MappingArticle load15minMap()
    {
        MappingArticle map = new MappingArticle();
        map.pageURL = "http://www.15min.lt/";
        map.antraste = Lists.newArrayList("og:title");
        map.kategorija =  Lists.newArrayList( "article:section");
        map.tags = Lists.newArrayList("keywords");
        map.turinys = Lists.newArrayList("div.article_content p");
        map.autorius = Lists.newArrayList("article:author", ".author-wrap");
        map.parasymoData = Lists.newArrayList("[itemprop=datePublished]");
        map.koment_commentDate = Lists.newArrayList("date");
        map.koment_Komentaras = Lists.newArrayList("comment");
        map.koment_Slapyvardis = Lists.newArrayList("user_name");
        map.koment_userIP = Lists.newArrayList("user_ip");
        map.isCommentJSON = true;
        map.commentJSONkey = "comments";
        map.repliesJSONkey = "replies";
        map.puslapisName = "15min";
        map.puslapisNameProperty = new SimpleStringProperty("15min");

        return  map;

    }

    public static MappingArticle loadDelfiMap()
    {
        MappingArticle map = new MappingArticle();
        map.pageURL = "http://www.delfi.lt/";
        map.antraste = Lists.newArrayList("og:title");
        map.kategorija =  Lists.newArrayList("cxenseparse:recs:category");
        map.tags = Lists.newArrayList("keywords");
        map.turinys = Lists.newArrayList("div.articleBody p", "[itemprop=articleBody]");
        map.autorius = Lists.newArrayList("og:article:author","article:author", ".author-wrap");
        map.parasymoData = Lists.newArrayList("[itemprop=datePublished]");
        map.koment_commentDate = Lists.newArrayList(".comment-date");
        map.koment_Komentaras = Lists.newArrayList(".comment-content-inner");
        map.koment_Slapyvardis = Lists.newArrayList(".comment-author");
        map.koment_userIP = Lists.newArrayList(".comment-date");
        map.puslapisName = "delfi";
        map.koment_Tag = ".comment-post";
        map.isCommentJSON = false;
        map.puslapisNameProperty = new SimpleStringProperty("delfi");
        return  map;
    }

    public static void readCreateDefaultMappings(){
        Writer writer = null;
        File mappingJsonFile = new File("maps.json");

        try {
            mappingJsonFile.createNewFile();
            Gson fxGson = FxGson.create();

            if (mappingJsonFile.length() == 0) {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(mappingJsonFile), "utf-8"));

                MainView.mapsList.add(MappingController.load15minMap());
                MainView.mapsList.add(MappingController.loadDelfiMap());
                MainView.mapsList.add(MappingController.loadTv3Map());
                MainView.mapsList.add(MappingController.loadLRytasMap());
                MainView.mapsList.add(MappingController.loadVZsMap());
                MainView.mapsList.add(MappingController.loadAlfasMap());
                MainView.mapsList.add(MappingController.loadDienaMap());
                String jsonString = fxGson.toJson(MainView.mapsList);
                writer.write(jsonString);
                writer.flush();
                writer.close();
            } else {
                Type listType = new TypeToken<ArrayList<MappingArticle>>(){}.getType();
                byte[] encoded = Files.readAllBytes(Paths.get("maps.json") );
                String json = new String(encoded,  Charset.defaultCharset());
                MainView.mapsList = fxGson.fromJson(json, listType);
                // not empty
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveMapping(){
        File mappingJsonFile = new File("maps.json");
        Writer writer = null;
        try {
            writer = new FileWriter(mappingJsonFile, false);;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson fxGson = FxGson.create();
        String jsonString = fxGson.toJson(MainView.mapsList);
        try {
            assert writer != null;
            writer.write(jsonString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
