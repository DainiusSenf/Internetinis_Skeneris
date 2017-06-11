package view;

import config.CrawlerStartController;
import config.MappingController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Main;
import models.MappingArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daini on 3/19/2017.
 */
public class MainView {

    public static List<MappingArticle> mapsList = new ArrayList<>();

    public static Stage dbSettingsStage, crawlerSettingsStage;

    public static Scene loadWebCrawler() {
        MappingController.readCreateDefaultMappings();
        return generateLayout();
    }

    public static Scene generateLayout(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        Button startBtn = new Button("Start crawler");
        startBtn.setPrefSize(100, 20);
        startBtn.setOnAction(event -> {
            Boolean mapSelected = false;
            for(MappingArticle map : MainView.mapsList) {
                if(map.isIsSelected()){
                    mapSelected = true;
                    break;

                }
            }
            if(!mapSelected){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Atleast one page must be selected!");
                alert.showAndWait();
                return;
            }

            CrawlingProgressView prg = new CrawlingProgressView();

            try {
                Main.thestage.setScene(prg.startScene());
                CrawlerStartController.crawlSelectedSites();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Button addMapBtn = new Button("Add new map");
        addMapBtn.setPrefSize(100, 20);
        addMapBtn.setOnAction(event ->
                new AddNewMapView().addNewMapView());

        Button dbConfigsBtn = new Button("Database settings");
        dbConfigsBtn.setPrefSize(120, 20);
        dbConfigsBtn.setOnAction(event ->{
            Scene scene = DataBaseView.loadMainWindow(true);
            dbSettingsStage = new Stage();

            dbSettingsStage.setTitle("DataBase Settings");
            dbSettingsStage.setScene(scene);
            dbSettingsStage.show();
                }

        );

        Button crawlerConfigsBtn= new Button("WebCrawler settings");
        crawlerConfigsBtn.setPrefSize(140, 20);
        crawlerConfigsBtn.setOnAction(event -> {
                Scene scene = ConfigCrawlerView.loadMainWindow();
                crawlerSettingsStage = new Stage();
                crawlerSettingsStage.setTitle("DataBase Settings");
                crawlerSettingsStage.setScene(scene);
                crawlerSettingsStage.show();
                }
        );

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.getChildren().addAll(startBtn, addMapBtn, dbConfigsBtn, crawlerConfigsBtn);
        grid.add(hbox, 0, 0);

        TableView<MappingArticle> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems( FXCollections.observableList(MainView.mapsList));

        table.setEditable(true);

        TableColumn firstNameCol = new TableColumn("Selected");
        TableColumn lastNameCol = new TableColumn("Website");
        TableColumn previewCol = new TableColumn("Action");

        firstNameCol.setCellValueFactory(new PropertyValueFactory<MappingArticle,String>("isSelected"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<MappingArticle,String>("puslapisNameProperty"));
        previewCol.setCellValueFactory( new PropertyValueFactory<>( "DUMMY" ) );
        previewCol.setStyle( "-fx-alignment: CENTER;");
        lastNameCol.setStyle( "-fx-alignment: CENTER;");

        previewCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MappingArticle, Boolean>, ObservableValue<Boolean>>() {
            @Override public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<MappingArticle, Boolean> features) {
                return new SimpleBooleanProperty(features.getValue() != null);
            }
        });

        previewCol.setCellFactory(new Callback<TableColumn<MappingArticle, Boolean>, PreviewMappingView>() {
            @Override
            public PreviewMappingView call(TableColumn<MappingArticle, Boolean> param) {
                return new PreviewMappingView();
            }
        });


        firstNameCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<MappingArticle,Boolean>,ObservableValue<Boolean>>()
                {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<MappingArticle, Boolean> param) {
                        return param.getValue().isSelected();
                    }
                });

        firstNameCol.setCellFactory( CheckBoxTableCell.forTableColumn(firstNameCol) );



        table.getColumns().addAll(firstNameCol, lastNameCol, previewCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
      //  final Label label = new Label("Preconfigured pages");
       // label.setFont(new Font("Arial", 20));
        vbox.getChildren().addAll( table);
//        grid.addAll(vbox);
        StackPane sp = new StackPane();
        sp.getChildren().addAll(vbox);
        grid.add(sp, 0, 1);


        Scene scene = new Scene(grid, 500  , 500);;
        return scene;
    }

}
