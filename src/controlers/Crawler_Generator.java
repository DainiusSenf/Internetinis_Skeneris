package controlers;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import config.MappingController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import models.MappingArticle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dovile on 2016-04-19.
 */
public class Crawler_Generator extends WebCrawler
{
    private final static Pattern FILTERS = Pattern.compile(".*\\?comments|.*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");

    public static MappingArticle genStraipsnisMap = new MappingArticle();
    public static MappingArticle genKomentarasMap = new MappingArticle();
    public static Boolean isKomentaras = false;
    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();

//        return !FILTERS.matcher(href).matches()
//                && href.startsWith(pageURL);

        if (FILTERS.matcher(href).matches()) {
            return false;
        }

        for (String crawlDomain : myCrawlDomains) {
            if (href.startsWith(crawlDomain)) {
                return true;
            }
        }
        return false;
    }

    private String[] myCrawlDomains;

    @Override
    public void onStart() {
        myCrawlDomains = (String[]) myController.getCustomData();
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {

        String url = page.getWebURL().getURL();
        System.out.println("url: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            if(!isKomentaras){
                String pageType = htmlParseData.getMetaTags().get("og:type");
                if (pageType != null && Objects.equals(pageType, "article")) {
                    generateMappingForStraipsnis(htmlParseData);
                }
            } else {
                generateMappingForKomentaras(htmlParseData);
            }

        }
    }

    private void generateMappingForStraipsnis(HtmlParseData htmlParseData)
    {
        MappingArticle map = MappingController.allMap();
        String html = htmlParseData.getHtml();
        Document doc = Jsoup.parse(html);

        genStraipsnisMap.antraste = fillList(genStraipsnisMap.antraste,htmlParseData,doc, map.antraste);
        genStraipsnisMap.kategorija = fillList(genStraipsnisMap.kategorija,htmlParseData,doc, map.kategorija);
        genStraipsnisMap.tags = fillList(genStraipsnisMap.tags,htmlParseData,doc, map.tags);
        genStraipsnisMap.turinys = fillList(genStraipsnisMap.turinys,htmlParseData,doc, map.turinys);
        genStraipsnisMap.autorius = fillList(genStraipsnisMap.autorius,htmlParseData,doc, map.autorius);
        genStraipsnisMap.parasymoData = fillList(genStraipsnisMap.parasymoData,htmlParseData,doc, map.parasymoData);
    }

    private void generateMappingForKomentaras(HtmlParseData htmlParseData)
    {
        MappingArticle map = MappingController.allMap();
        String html = htmlParseData.getHtml();
        Document doc = Jsoup.parse(html);

        genKomentarasMap.koment_Slapyvardis = fillList(genKomentarasMap.koment_Komentaras,htmlParseData,doc, map.koment_Komentaras);
        genKomentarasMap.koment_userIP = fillList(genKomentarasMap.koment_userIP,htmlParseData,doc, map.koment_userIP);
        genKomentarasMap.koment_Komentaras = fillList(genKomentarasMap.koment_Komentaras,htmlParseData,doc, map.koment_Komentaras);
        genKomentarasMap.koment_email= fillList(genKomentarasMap.koment_email,htmlParseData,doc, map.koment_email);
        genKomentarasMap.koment_commentDate = fillList(genKomentarasMap.koment_commentDate,htmlParseData,doc, map.koment_commentDate, true);
    }
    private List<String> fillList(List<String> list, HtmlParseData htmlParseData , Element element , List<String> elemsList){
        return fillList(list, htmlParseData, element, elemsList, false);
    }

    private List<String> fillList(List<String> list, HtmlParseData htmlParseData , Element element , List<String> elemsList, Boolean isDate){
        if(isDate)
            list.addAll(getDateTextFromElement(htmlParseData,element, elemsList));
        else {
            try{
                list.addAll(getTextFromElement(htmlParseData, element, elemsList));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        list = removeDuplicates(list);
        return list;
    }

    private List<String> removeDuplicates(List<String> list){
        HashSet hs = new HashSet();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);
        return list;
    }

    private static String getDelfiCommentIP(String dateIPinfo){
        String IPADDRESS_PATTERN =
                "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(dateIPinfo);
        if (matcher.find()) {
            return matcher.group();
        } else{
            return "0.0.0.0";
        }
    }

    private static List<String> getTextFromElement(HtmlParseData htmlParseData ,Element element ,List<String> elemsList){
        String text = "";
        List<String> mappingTags = new ArrayList<>();
        if(elemsList.size() == 1){
            try{
                Elements ele  = element.select(elemsList.get(0));
                if(ele != null && Objects.equals(ele.get(0).nodeName(), "meta")){
                    text = ele.get(0).attr("content");
                    if(text != null && !text.isEmpty()){
                        mappingTags.add(elemsList.get(0));
                        return mappingTags;
                    }
                } else {
                    text = htmlParseData.getMetaTags().get(elemsList.get(0));
                    if(text != null && !text.isEmpty()){
                        mappingTags.add(elemsList.get(0));
                        return mappingTags;
                    }
                }
            } catch (Exception e) {
                text = htmlParseData.getMetaTags().get(elemsList.get(0));
                if(text != null && !text.isEmpty()){
                    mappingTags.add(elemsList.get(0));
                    return mappingTags;
                }
            }
        } else {
            for(String elem : elemsList){
                text = htmlParseData.getMetaTags().get(elem);
                if(text != null && !text.isEmpty()) {
                    mappingTags.add(elem);
                    return mappingTags;
                }
                try {
                    Elements autoriusElements = element.select(elem);
                    if(autoriusElements != null ){
                        text = autoriusElements.text();
                        if(text != null && !text.isEmpty()){
                            mappingTags.add(elem);
                            return mappingTags;
                        }
                    }
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return mappingTags;
    }

    private static List<String> getDateTextFromElement(HtmlParseData htmlParseData ,Element element ,List<String> elemsList){
        String text = "";
        List<String> mappingTags = new ArrayList<>();
        if(elemsList.size() == 1){
            try{
                Elements ele  = element.select(elemsList.get(0));
                if(ele != null && Objects.equals(ele.get(0).nodeName(), "meta")){
                    text = ele.get(0).attr("content");
                    if(text != null && !text.isEmpty()){
                        Date date = parseDateFromString(text);
                        if(date != null){
                            mappingTags.add(elemsList.get(0));
                            return mappingTags;
                        }
                    }
                } else {
                    text = htmlParseData.getMetaTags().get(elemsList.get(0));
                    if(text != null && !text.isEmpty()){
                        Date date = parseDateFromString(text);
                        if(date != null){
                            mappingTags.add(elemsList.get(0));
                            return mappingTags;
                        }
                    }
                }
            } catch (Exception e) {
                    text = htmlParseData.getMetaTags().get(elemsList.get(0));
                    if(text != null && !text.isEmpty()){
                        Date date = parseDateFromString(text);
                        if(date != null){
                            mappingTags.add(elemsList.get(0));
                            return mappingTags;
                        }
                    }
            }
        } else {
            for(String elem : elemsList){
                text = htmlParseData.getMetaTags().get(elem);
                if(text != null && !text.isEmpty()) {
                    Date date = parseDateFromString(text);
                    if(date != null){
                        mappingTags.add(elemsList.get(0));
                        return mappingTags;
                    }
                }
                try {
                    Elements autoriusElements = element.select(elem);
                    if(autoriusElements != null ){
                        text = autoriusElements.text();
                        if(text != null && !text.isEmpty()){
                            Date date = parseDateFromString(text);
                            if(date != null){
                                mappingTags.add(elemsList.get(0));
                                return mappingTags;
                            }
                        }
                    }
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return mappingTags;
    }

    private static Date parseDateFromString(String text) {
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(text);
        for (DateGroup group : groups) {
            List<Date> dates = group.getDates();
            if (dates.size() > 0) {
                return dates.get(0);
            }
        }
        return null;
    }
}








