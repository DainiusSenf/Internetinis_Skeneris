package controlers;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import config.CrawlerStartController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import models.Article;
import models.Comment;
import models.MappingArticle;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import services.MongoDB_Connector;
import services.MySQL_Connector;
import view.AddNewMapView;
import view.ConfigCrawlerView;
import view.CrawlingProgressView;
import view.MainView;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dovile on 2016-04-19.
 */
public class Crawler extends WebCrawler
{
    private final static Pattern FILTERS = Pattern.compile(".*\\?comments|.*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");

    private int straipsnioID = 0;
    private int commentPageCount = 0;
    private int commentID = 0;
    private int puslapioID;
    private String puslapisName;
    private String nextKomentPageURL = "";

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
            } else if (href.contains(crawlDomain)) {
                return true;
            }
        }

        return false;
    }

    private String[] myCrawlDomains;

    @Override
    public void onStart() {
        HashMap<String, Object> customDataMap = (HashMap<String, Object>) myController.getCustomData();
        myCrawlDomains = (String[]) customDataMap.get("crawler1Domains");
        puslapisName = (String) customDataMap.get("puslapisName");
        puslapioID = (Integer) customDataMap.get("puslapioID");
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */


    @Override
    public void visit(Page page) {

        String url = page.getWebURL().getURL();
        System.out.println("page.getWebURL().getDocid(): " + page.getWebURL().getDocid());
        double percentage = (double) page.getWebURL().getDocid() / ConfigCrawlerView.noPagesToCrawl;

        try {
            CrawlingProgressView.performWorkOnDbList(percentage, puslapioID);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String pageType = htmlParseData.getMetaTags().get("og:type");
            if (pageType != null && Objects.equals(pageType, "article")) {

                int index = AddNewMapView.getIndexByProperty(puslapisName);
                MappingArticle map = MainView.mapsList.get(index);

                saveStraipsnis(htmlParseData, url, map);
                try {
                    List<Comment> parsedComments = new ArrayList<>();
                    if(Objects.equals(page.getWebURL().getParentUrl(), "http://www.15min.lt/")){
                        //15min
                        parsedComments = parseComment(htmlParseData, url+"?comments", straipsnioID, puslapioID, map);
                        insertKomentarai(parsedComments);
                    } else if(Objects.equals(page.getWebURL().getParentUrl(), "http://www.delfi.lt/")){
                        //delfi
                        String commentURL = url+"&com=1&reg=0&s=2&no=0";
                        parsedComments.addAll(parseComment( htmlParseData, commentURL, straipsnioID, puslapioID, map));
                        for(int i = 2; i<= commentPageCount;i+=2){
                            commentURL = url+"&com=1&reg=0&s=2&no="+i+"0";
                            parsedComments.addAll(parseComment( htmlParseData,commentURL, straipsnioID, puslapioID, map));
                        }
                        commentPageCount = 0;
                        commentURL = url+"&com=1&s=2&reg=1&no=0";
                        parsedComments.addAll(parseComment( htmlParseData,commentURL, straipsnioID, puslapioID, map));
                        for(int i = 2; i<= commentPageCount;i+=2){
                            commentURL = url+"&com=1&reg=1&s=2&no=" + i + "0";
                            parsedComments.addAll(parseComment( htmlParseData,commentURL, straipsnioID, puslapioID, map));
                        }
                        insertKomentarai(parsedComments);
                        commentID = 0;
                    } else if (Objects.equals(page.getWebURL().getParentUrl(), "http://www.tv3.lt/")){
                        String commentURL = url.replace("naujiena", "komentarai");
                        parsedComments.addAll(parseComment( htmlParseData, commentURL, straipsnioID, puslapioID, map));
                        insertKomentarai(parsedComments);
                    } else if (page.getWebURL().getParentUrl().contains("lrytas")){
                        commentPageCount = 1;
                        String articleId = htmlParseData.getMetaTags().get("cxenseparse:recs:articleid");
                        String psl = StringUtils.substringBetween(url, "//", "/");
                        String commentURL;
                        do {
                            commentURL = "http://" + psl + "?id=" + articleId + "&view=6&p=" + commentPageCount + "&order=0";
                            List<Comment> koments = parseComment( htmlParseData, commentURL, straipsnioID, puslapioID, map);
                            if(koments != null)
                                parsedComments.addAll(koments);
                            if(commentPageCount != 0)
                                commentPageCount++;
                        }while(commentPageCount != 0);
                        insertKomentarai(parsedComments);
                    } else if (page.getWebURL().getParentUrl().contains("vz")){
                        String commentURL =getVZcommentURL(url + "&template=comments");
                        if(!Objects.equals(commentURL, "No Comments")){
                            List<Comment> koments = parseComment( htmlParseData, commentURL, straipsnioID, puslapioID, map);
                            insertKomentarai(koments);
                        }
                    } else {
                        do{
                            String commentURL;
                            if(Objects.equals(nextKomentPageURL, ""))
                                commentURL = url + map.koment_URL_ending;
                            else
                                commentURL = nextKomentPageURL;
                            parsedComments.addAll(parseComment( htmlParseData, commentURL, straipsnioID, puslapioID, map));
                            insertKomentarai(parsedComments);
                        } while (!Objects.equals(nextKomentPageURL, ""));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                straipsnioID++;
            }
        }
    }

    private void saveStraipsnis(HtmlParseData htmlParseData, String url, MappingArticle map)
    {
        String html = htmlParseData.getHtml();
        Document doc = Jsoup.parse(html);
        Article article = new Article();
        article.antraste = getTextFromElement(htmlParseData,doc, map.antraste);
        article.tags = getTextFromElement(htmlParseData,doc, map.tags);
        if(map.isStraipsnisJSON){
            article.turinys = getStraipsnisInfoFromJSON(doc, map.turinys);
            String articleDateStr = getStraipsnisInfoFromJSON(doc, map.parasymoData);
            article.parasymoData = parseDateFromString(articleDateStr);
            article.kategorija = getTextFromElement(htmlParseData,doc, map.kategorija);
        } else {
            article.turinys = getTextFromElement(htmlParseData,doc, map.turinys);
            String articleDateStr = getTextFromElement(htmlParseData,doc, map.parasymoData);
            article.parasymoData = parseDateFromString(articleDateStr);
            article.kategorija = getTextFromElement(htmlParseData,doc, map.kategorija);
        }
        article.url = url;
        article.autorius = getTextFromElement(htmlParseData,doc, map.autorius);
        Date today = Calendar.getInstance().getTime();
        article.paemimoData = today;
        article.id = puslapioID+ "-" + String.valueOf(straipsnioID) ;

        insertStraipsnis(article);
    }

    private void insertStraipsnis(Article article){
        if(MySQL_Connector.mySQL_selected)
            CrawlerStartController.mySQL_connector.insertStraipsnis(article);
        else if (MongoDB_Connector.mongoDB_selected)
            CrawlerStartController.mongoDB_connector.insertStraipsnis(article);
    }

    private void insertKomentarai(List<Comment> comments){
        if(MySQL_Connector.mySQL_selected && comments != null && !comments.isEmpty())
            CrawlerStartController.mySQL_connector.insertABunchOfKomentarai(comments);
        else if (MongoDB_Connector.mongoDB_selected && comments != null && !comments.isEmpty())
            CrawlerStartController.mongoDB_connector.insertABunchOfKomentarai(comments);
    }

    private static String getVZcommentURL(String url){
        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
        } catch (IOException e) {
            return "No Comments";
        }
        if(doc == null)
            return "No Comments";

        Elements scriptElements = doc.getElementsByTag("script");
        String commentURL = "";
        for (Element element : scriptElements) {
            for (DataNode node : element.dataNodes()) {
                if (node.getWholeData().contains("$.ajax({")) {
                    commentURL = StringUtils.substringBetween(node.getWholeData(),  "url: \"", "\"");
                    return commentURL;
                }
            }
        }
        return commentURL;
    }


    private static int getCommentsPageCount(Document doc ){
        Elements pages = doc.select(".comments-pager-page");
        if(pages.isEmpty())
            return 0;
        int count = (Integer.valueOf(pages.get(pages.size()-1).text())-1) * 2;
        return count;
    }

    private List<Comment> parseComment(HtmlParseData htmlParseData, String url, int straipsnio_id, int puslapio_id, MappingArticle map) throws IOException {
        if(map.isCommentJSON){
            return getCommentsFromJS(htmlParseData, url,straipsnio_id, puslapio_id, map);
        } else {
            return getCommentsHTML(htmlParseData ,url,straipsnio_id, puslapio_id, map);
        }
    }

    private List<Comment> getCommentsHTML(HtmlParseData htmlParseData , String url, int straipsnio_id, int puslapio_id, MappingArticle map) throws IOException {
        Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").followRedirects(false).get();
        if(commentPageCount == 0 && Objects.equals(map.puslapisName, "delfi"))
            commentPageCount = getCommentsPageCount(doc);
        Date today = Calendar.getInstance().getTime();
        Elements komentarai = doc.select(map.koment_Tag);
        List<Comment> commentsToSave = new ArrayList<Comment>();
        if(map.hasPaging){
            try{
                Elements el = doc.select(map.koment_NextPageTag);
                nextKomentPageURL = el.attr("href");
            } catch (Exception e){
                nextKomentPageURL = "";
            }
        }
        if(komentarai == null || komentarai.isEmpty()){
            commentPageCount = 0;
            return commentsToSave;
        }

        if(Objects.equals(map.puslapisName, "tv3")){
            komentarai = komentarai.get(0).children();
        }
        for(Element komentaras : komentarai){
            commentID++;
            Comment parsedKoment = new Comment();
            parsedKoment.komentaras = getTextFromElement(htmlParseData,komentaras, map.koment_Komentaras);
            parsedKoment.slapyvardis = getTextFromElement(htmlParseData,komentaras, map.koment_Slapyvardis);
            String dateIPinfo = getTextFromElement(htmlParseData,komentaras, map.koment_commentDate);
            parsedKoment.komentaroData = parseDateFromString(dateIPinfo);
            if(Objects.equals(map.puslapisName, "delfi")){
                parsedKoment.IP_adresas = getDelfiCommentIP(dateIPinfo);
            } else {
                String ip = getTextFromElement(htmlParseData,komentaras, map.koment_userIP);
                parsedKoment.IP_adresas = getDelfiCommentIP(ip);
            }
            parsedKoment.paemimoData = today;
            parsedKoment.puslapio_ID = puslapio_id;
            parsedKoment.straipsnio_ID = straipsnio_id;
            parsedKoment.url = url;
            String komentaroID = String.valueOf(puslapio_id)+"-"+String.valueOf(straipsnio_id)+"-"+String.valueOf(commentID);
            parsedKoment.komentaro_ID = komentaroID;
            if(parsedKoment.komentaras != null && !Objects.equals(parsedKoment.komentaras, "")){
                commentsToSave.add(parsedKoment);
            }
        }
        return commentsToSave;
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

    private static String getTextFromElement(HtmlParseData htmlParseData ,Element element ,List<String> elemsList){
        String text = "";
        if(elemsList.size() == 1){
            try{
                Elements ele  = element.select(elemsList.get(0));
                if(ele != null && Objects.equals(ele.get(0).nodeName(), "meta")){
                    text = ele.get(0).attr("content");
                    return text;
                } else {
                    text = htmlParseData.getMetaTags().get(elemsList.get(0));
                    if(text != null && !text.isEmpty())
                        return text;
                    else
                        return element.select(elemsList.get(0)).text();
                }

            } catch (Exception e) {
                    text = htmlParseData.getMetaTags().get(elemsList.get(0));
                    if(text != null && !text.isEmpty())
                        return text;
                    else
                        return element.select(elemsList.get(0)).text();
                //System.out.println(e.getStackTrace());
            }
        } else {
            for(String elem : elemsList){
                text = htmlParseData.getMetaTags().get(elem);
                if(text != null && !text.isEmpty())
                    return text;
                try {
                    Elements autoriusElements = element.select(elem);
                    if(autoriusElements != null && Objects.equals(autoriusElements.get(0).nodeName(), "meta")) {
                        text = autoriusElements.get(0).attr("content");
                        return text;
                    } else if(autoriusElements != null ){
                        text = autoriusElements.text();
                        if(text != null && !text.isEmpty())
                            return text;
                    }
                } catch (Exception e){
//                    System.out.println(e.getMessage());
                }
            }
        }
        return text;
    }

    private static List<Comment> getReplies(JSONArray replies, String url, int puslapio_id, int straipsnio_id, String komentaroID, MappingArticle map) throws IOException {
        if(map.isCommentJSON){
            List<Comment> commentsToSave = new ArrayList<Comment>();
            for (int i = 0; i < replies.length(); i++) {
                String comment = replies.getJSONObject(i).getString(map.koment_Komentaras.get(0));
                String user_name = replies.getJSONObject(i).getString(map.koment_Slapyvardis.get(0));
                String user_ip = replies.getJSONObject(i).getString(map.koment_userIP.get(0));
                String commentDate = replies.getJSONObject(i).getString(map.koment_commentDate.get(0));

                Comment komentaras = new Comment();
                komentaras.setKomentaras(comment);
                komentaras.setSlapyvardis(user_name);
                komentaras.setIp_adresas(user_ip);
                Date date = parseDateFromString(commentDate);
                if(date != null)
                    komentaras.setKomentarodata(date);
                Date today = Calendar.getInstance().getTime();
                komentaras.setPaemimodata(today);
                komentaras.setPuslapio_id(puslapio_id);
                komentaras.setStraipsnio_id(straipsnio_id);
                komentaras.setAtsakomokoment_id(komentaroID);
                komentaras.setKomentaro_ID(komentaroID+"-"+i);
                komentaras.setUrl(url);
                commentsToSave.add(komentaras);
//                System.out.println(i + " reply " + comment);

            }
            return commentsToSave;
        } else {
            //TODO
            return null;
        }
    }

    private static String getStraipsnisInfoFromJSON(Element element ,List<String> elemsList) {
        Elements scriptElements = element.getElementsByTag("script");
        String script = "";
        for (Element scriptElement : scriptElements) {
            for (DataNode node : scriptElement.dataNodes()) {
                if (node.getWholeData().contains("var article = ")) {
                    script = node.getWholeData();
                    script = script.replace("var article = ", "");
                    script = script.substring(0, script.length() - 1);
                }
            }
        }
        JSONObject obj = null;
        if (script.length() > 0) {
            obj = new JSONObject(script);
            JSONObject strObj = obj.getJSONObject("str");
            String objString = strObj.getString(elemsList.get(0));
            return objString;
        }
        return "";
    }

    private static List<Comment> getCommentsFromJS(HtmlParseData htmlParseData , String url, int straipsnio_id, int puslapio_id, MappingArticle map) {
        Document doc;
        try{
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
//           System.out.println("No comments");
            return null;
        }
        List<Comment> commentsToSave = new ArrayList<>();
        Elements scriptElements = doc.getElementsByTag("script");
        String script = "";
        for (Element element : scriptElements) {
            for (DataNode node : element.dataNodes()) {
                if (node.getWholeData().contains("div.comments-placeholder")) {
                    script = node.getWholeData();
                    script = script.substring(41);
                    script = script.substring(0, script.length() - 1);
                }
            }
        }
        JSONObject obj = null;
        if(script.length() > 0){
            obj = new JSONObject(script);
            JSONArray comments = obj.getJSONArray(map.commentJSONkey);
            for (int i = 0; i < comments.length(); i++) {
                Comment komentaras = new Comment();

                String comment = comments.getJSONObject(i).getString(map.koment_Komentaras.get(0));
                String user_name = comments.getJSONObject(i).getString(map.koment_Slapyvardis.get(0));
                String user_ip = comments.getJSONObject(i).getString(map.koment_userIP.get(0));
                String commentDate = comments.getJSONObject(i).getString(map.koment_commentDate.get(0));

                komentaras.setKomentaras(comment);
                komentaras.setSlapyvardis(user_name);
                komentaras.setIp_adresas(user_ip);
                Date date = parseDateFromString(commentDate);
                if(date != null)
                    komentaras.setKomentarodata(date);
                Date today = Calendar.getInstance().getTime();
                komentaras.setPaemimodata(today);
                komentaras.setPuslapio_id(puslapio_id);
                komentaras.setStraipsnio_id(straipsnio_id);
                komentaras.setUrl(url);
                String komentaroID = String.valueOf(puslapio_id)+"-"+String.valueOf(straipsnio_id)+"-"+String.valueOf(i);
                komentaras.setKomentaro_ID(komentaroID);
                JSONArray replies = comments.getJSONObject(i).getJSONArray(map.repliesJSONkey);
                commentsToSave.add(komentaras);
                List<Comment> commentReplies = null;
                try {
                    commentReplies = getReplies(replies, url, puslapio_id, straipsnio_id, komentaroID, map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert commentReplies != null;
                commentsToSave.addAll(commentReplies);
//                System.out.println(i + " " + comment);
            }
        } else {
//            System.out.println("Scriptas null " + url);
//            System.out.println(script);
        }

        return commentsToSave;
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








