package view;

/**
 * Created by daini on 17/04/16.
 */

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.Main;
import models.MappingArticle;

import java.util.ArrayList;
import java.util.List;

public class CrawlingProgressView {

    private final List<Label> labels = new ArrayList<>();
    public static final List<ProgressBar> progressBars = new ArrayList<>();
    private static final List<ProgressIndicator> progressIndicators = new ArrayList<>();

    private static final List<ReadOnlyDoubleWrapper> progressList = new ArrayList<>();

    public static ReadOnlyDoubleProperty progressListGet(int i) {
        return progressList.get(i);
    }

    public static void performWorkOnDbList(double percentage, int i) throws Exception {
        progressList.get(i).set(percentage);
        progressIndicators.get(i).setProgress(percentage);
    }

    private static final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();


    public Scene startScene() throws Exception {

        Group root = new Group();
        Scene scene = new Scene(root, 700, 500);
        scene.getStylesheets().add("progresssample/Style.css");
        final VBox vb = new VBox();
        final List<HBox> hbs = new ArrayList<>();
        int i = 0;
        for(MappingArticle map : MainView.mapsList) {
            if(map.isIsSelected()){
                progressList.add(new ReadOnlyDoubleWrapper());
                final Label label = new Label();
                label.setPrefWidth(130);
                label.setAlignment(Pos.CENTER_RIGHT);
                label.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                Text t = new Text();
                t.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
                t.setText(map.puslapisName + " progress:" );
                label.setText(map.puslapisName + " progress:" );
                labels.add(label);
                final ProgressBar pb = new ProgressBar(0.0);
                pb.setPrefWidth(350);
                progressBars.add(pb);
                final ProgressIndicator pin = new ProgressIndicator(0.0);
                progressIndicators.add(pin);
                final HBox hb = new HBox();

                hb.setSpacing(5);
                hb.setAlignment(Pos.CENTER);
                hb.getChildren().addAll(label, pb, pin);
                hbs.add(hb);
                i++;
            }
        }

        Button cancelBtn = new Button("Cancel crawl");
        cancelBtn.setAlignment(Pos.CENTER);
        cancelBtn.setOnAction(event -> {
            Main.thestage.setScene(MainView.generateLayout());
        });

        final HBox hb = new HBox();

        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(cancelBtn);
        hbs.add(hb);

        vb.setPadding(new Insets(10, 0, 10, 0));
        vb.setSpacing(5);
        vb.getChildren().addAll(hbs);
        scene.setRoot(vb);
        return scene;
    }
}

