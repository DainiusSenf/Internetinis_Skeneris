package models;

import edu.uci.ics.crawler4j.crawler.CrawlController;

/**
 * Created by daini on 3/28/2017.
 */
public class CrawControllerWrapper {

    public CrawlController controller;
    public String webURL;

    public CrawControllerWrapper(CrawlController controller, String webURL){
        this.controller = controller;
        this.webURL = webURL;
    }
}
