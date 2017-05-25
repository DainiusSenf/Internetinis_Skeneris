package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Created by daini on 3/26/2017.
 */
public class ConfigCrawlerView {


    public static int crawlerThreadsCount = 1;
    public static int noPagesToCrawl = 20;
    public static int crawlerDepth = 3;
    public static int politnesDelay = 10;
    public static Boolean useRobotPolitnes = true;
    public static Boolean cleanDatabase = true;
    static Scene scene;
    private static TextField crawlerThreadsCountText, noPagesToCrawlText, crawlerDepthText,
            politnesDelayText;
    private static CheckBox useRobotPolitnesChkBox, cleanDBChkBox;

    public static Scene loadMainWindow(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        scene = new Scene(grid, 500  , 500);

        grid.add(new Label("Crawlers count"), 0, 1);
        crawlerThreadsCountText = new TextField();
        crawlerThreadsCountText.setText(String.valueOf(crawlerThreadsCount));
        grid.add(crawlerThreadsCountText, 1, 1);

        grid.add(new Label("Total number of pages to crawl"), 0, 2);
        noPagesToCrawlText = new TextField();
        noPagesToCrawlText.setText(String.valueOf(noPagesToCrawl));
        grid.add(noPagesToCrawlText, 1, 2);

        grid.add(new Label("Crawler depth"), 0, 3);
        crawlerDepthText = new TextField();
        crawlerDepthText.setText(String.valueOf(crawlerDepth));
        grid.add(crawlerDepthText, 1, 3);

        grid.add(new Label("Politnes delay (ms)"), 0, 4);
        politnesDelayText = new TextField();
        politnesDelayText.setText(String.valueOf(politnesDelay));
        grid.add(politnesDelayText, 1, 4);

        useRobotPolitnesChkBox = new CheckBox("Use robots.txt politnes delay ");
        grid.add(useRobotPolitnesChkBox, 1, 5);
        useRobotPolitnesChkBox.setSelected(true);

        cleanDBChkBox = new CheckBox("Clean DataBase on cralw start? ");
        grid.add(cleanDBChkBox, 1, 6);
        cleanDBChkBox.setSelected(true);

        Button btn = new Button("Save and close");
        grid.add(btn, 1, 7);

        btn.setOnAction(event -> {
            crawlerThreadsCount = Integer.valueOf(crawlerThreadsCountText.getText());
            noPagesToCrawl = Integer.valueOf(noPagesToCrawlText.getText());
            crawlerDepth = Integer.valueOf(crawlerDepthText.getText());
            politnesDelay = Integer.valueOf(politnesDelayText.getText());
            useRobotPolitnes = useRobotPolitnesChkBox.isSelected();
            cleanDatabase = cleanDBChkBox.isSelected();
            MainView.crawlerSettingsStage.close();
        });

        return scene;
    }

}
