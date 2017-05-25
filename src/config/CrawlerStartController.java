package config;

import controlers.Crawler;
import controlers.Crawler_Generator;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import javafx.concurrent.Task;
import models.CrawControllerWrapper;
import models.MappingArticle;
import models.WebPage;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import services.MongoDB_Connector;
import services.MySQL_Connector;
import view.ConfigCrawlerView;
import view.CrawlingProgressView;
import view.MainView;

import java.io.IOException;
import java.util.*;

public class CrawlerStartController {
    public static MySQL_Connector mySQL_connector;
    public static MongoDB_Connector mongoDB_connector;
    public static Map<String, MappingArticle> maps = new HashMap<>();
    private static List<CrawControllerWrapper> controlerWrappers = new ArrayList<>();

    private static void createCrawlers() throws Exception {
        int i =0;
        Date today = Calendar.getInstance().getTime();
        for(MappingArticle map : MainView.mapsList){
            if(map.isIsSelected()){
                String crawlStorageFolder = "./models";
//                int numberOfCrawlers = 1;

                CrawlConfig config = new CrawlConfig();
                String page = map.pageURL;

                config.setFollowRedirects(true);
                config.setCrawlStorageFolder(crawlStorageFolder + "/crawler" + i);
                config.setMaxPagesToFetch(ConfigCrawlerView.noPagesToCrawl);
                config.setMaxDepthOfCrawling(ConfigCrawlerView.crawlerDepth);
                if(!ConfigCrawlerView.useRobotPolitnes)
                    config.setPolitenessDelay(ConfigCrawlerView.politnesDelay);
                else{
                    int delay = getCrawlingDelayFromRobot(page);
                    config.setPolitenessDelay(delay != 0 ? delay : ConfigCrawlerView.politnesDelay);
                }

                PageFetcher pageFetcher = new PageFetcher(config);
                RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
                RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
                CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

                String[] crawler1Domains = new String[]{page,  map.puslapisName, map.pageURL};
                HashMap<String, Object> mapp = new HashMap<>();
                mapp.put("puslapisName", map.puslapisName);
                mapp.put("crawler1Domains", crawler1Domains);
                mapp.put("puslapioID", i);

                WebPage psl = new WebPage();
                psl.setPuslapio_vardas(map.puslapisName);
                psl.setPaemimo_data(today);
                if(MySQL_Connector.mySQL_selected) {
                    MySQL_Connector.insertPuslapis(psl, i);
                } else {
                    MongoDB_Connector.insertPuslapis(psl, i);
                }

                controller.setCustomData(mapp);
                controller.addSeed(page);
                controlerWrappers.add(new CrawControllerWrapper(controller, page));
                i++;
            }
        }
    }

    public static void crawlSelectedSites(){
        if(MySQL_Connector.mySQL_selected){
            mySQL_connector = new MySQL_Connector();
            MySQL_Connector.clearDatabase();
        }
        else if (MongoDB_Connector.mongoDB_selected){
            mongoDB_connector = new MongoDB_Connector();
            MongoDB_Connector.clearDatabase();
        }

        try {
            createCrawlers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int index = 0;
        for(CrawControllerWrapper crawlerWrapper : controlerWrappers){
            CrawlController crawler = crawlerWrapper.controller;
            try {
                int finalIndex = index;
                Task<Void> task = new Task<Void>() {
                    @Override public Void call() {

                        CrawlingProgressView.progressListGet(finalIndex).addListener((obs, oldProgress, newProgress) ->
                                updateProgress(newProgress.doubleValue(), 1));

                        return null;
                    }
                };
                CrawlingProgressView.progressBars.get(index).progressProperty().bind(task.progressProperty());

                Thread th = new Thread(task);
                th.setDaemon(true);
                th.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            crawler.startNonBlocking(Crawler.class, ConfigCrawlerView.crawlerThreadsCount);

            System.out.println("Finished " + crawlerWrapper.webURL);
            System.out.println("###########################################################");
            index++;
        }
    }

    private static int getCrawlingDelayFromRobot(String page){
        Document doc = null;
        int delay = 0;

        try {
            doc = Jsoup.connect(page+"robots.txt").userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
        } catch (IOException e) {
            delay = 10;
            e.printStackTrace();
            return delay;
        }
        String robotStr = doc.toString();
        String tempString = StringUtils.substringAfterLast(robotStr, "Crawl-delay: ");
        String delayStr = StringUtils.substringBefore(tempString, " ");
        try{
            delay = Integer.valueOf(delayStr);
        } catch (Exception e){
            delay = 10;
        }
        return delay;
    }

    public static void generateMaps(String siteUrl){
        String crawlStorageFolder = "./models";

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder + "/crawler");
        config.setMaxPagesToFetch(30);
        config.setMaxDepthOfCrawling(1);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = null;
        try {
            controller = new CrawlController(config, pageFetcher, robotstxtServer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] crawler1Domains = new String[]{siteUrl};
        assert controller != null;
        controller.setCustomData(crawler1Domains);
        controller.addSeed(siteUrl);
        controller.startNonBlocking(Crawler_Generator.class, 5);
        controller.waitUntilFinish();
        System.out.println("Finished " + siteUrl);
        System.out.println("###########################################################");
    }

    public CrawlerStartController() throws Exception {

    }
}