package view;

import config.MappingController;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.MappingArticle;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by daini on 17/04/26.
 */
public class MappingView extends TableCell<MappingArticle, Boolean>{
    public TextArea turinysTextArea,antrasteTextArea,kategorijaTextArea,tagsTextArea, autoriusTextArea,
            parasymoDataTextArea, komentarasTextArea, slapyvardisTextArea, komentaroDataTextArea, ipAdressTextArea,
            emailTextArea, komentTagTextArea;

    public TextField nameTextArea, straipsnioURLText, komentaroURLText, nextCommentPageTAGText,
            pageURLText;

    public CheckBox hasPagingChkBox;
    public  static Boolean hasPaging = false;
    public Label nextPageTagLabel;
    public HBox komentarasHbox, straipsnisHbox;

    public MappingView(){

    }

    public void addNewMapView(){
        BorderPane pane = createMappingTabs();
        Group root = new Group();

        Scene scene = new Scene(root, 625, 600, Color.WHITE);

        root.getChildren().addAll(pane);
        Stage myDialog = new Stage();
        myDialog.setScene(scene);
        myDialog.show();
    }


    public BorderPane createMappingTabs(){

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //straipsnis
        nameTextArea = new TextField();
        grid.add(new Label("Website name"), 0,0,1,1);
        grid.add(nameTextArea, 1, 0, 1, 1);

        pageURLText = new TextField();
        grid.add(new Label("Website URL"), 0,1,1,1);
        grid.add(pageURLText, 1, 1, 1, 1);

        turinysTextArea = new TextArea();
        turinysTextArea.setPrefRowCount(2);
        grid.add(new Label("WebPage"), 0,2,1,1);
        grid.add(turinysTextArea, 1, 2, 1, 1);

        antrasteTextArea = new TextArea();
        antrasteTextArea.setPrefRowCount(2);
        grid.add(new Label("Title"), 0,3,1,1);
        grid.add(antrasteTextArea, 1, 3, 1, 1);

        kategorijaTextArea = new TextArea();
        kategorijaTextArea.setPrefRowCount(2);
        grid.add(new Label("Category"), 0,4,1,1);
        grid.add(kategorijaTextArea, 1, 4, 1, 1);

        tagsTextArea = new TextArea();
        tagsTextArea.setPrefRowCount(2);
        grid.add(new Label("TAGs"), 0,5,1,1);
        grid.add(tagsTextArea, 1, 5, 1, 1);

        autoriusTextArea = new TextArea();
        autoriusTextArea.setPrefRowCount(2);
        grid.add(new Label("Author"), 0,6,1,1);
        grid.add(autoriusTextArea, 1, 6, 1, 1);

        parasymoDataTextArea = new TextArea();
        parasymoDataTextArea.setPrefRowCount(2);
        grid.add(new Label("Publish date"), 0,7,1,1);
        grid.add(parasymoDataTextArea, 1, 7, 1, 1);

        straipsnioURLText = new TextField();
        grid.add(new Label("WebPage Article URL"), 0,8,1,1);
        grid.add(straipsnioURLText, 1, 8, 1, 1);

        Button saveBtnStraipsnis = new Button("Save");
        saveBtnStraipsnis.setPrefSize(100, 20);
        saveBtnStraipsnis.setOnAction(event -> saveMapping());
        grid.add(saveBtnStraipsnis, 0,9,5,1);

        straipsnisHbox = new HBox();
        straipsnisHbox.setPadding(new Insets(15, 12, 15, 12));
        straipsnisHbox.setSpacing(10);
        straipsnisHbox.getChildren().addAll(saveBtnStraipsnis);
        grid.add(straipsnisHbox, 0,10,15,1);

        GridPane komentaroGrid = new GridPane();
        komentaroGrid.setHgap(10);
        komentaroGrid.setVgap(10);
        komentaroGrid.setPadding(new Insets(25, 25, 25, 25));
        //komentaras
        komentarasTextArea = new TextArea();
        komentarasTextArea.setPrefRowCount(2);
        komentaroGrid.add(new Label("Comment"), 0,1,1,1);
        komentaroGrid.add(komentarasTextArea, 1, 1, 1, 1);

        slapyvardisTextArea = new TextArea();
        slapyvardisTextArea.setPrefRowCount(2);
        komentaroGrid.add(new Label("Nickname"), 0,2,1,1);
        komentaroGrid.add(slapyvardisTextArea, 1, 2, 1, 1);

        komentaroDataTextArea = new TextArea();
        komentaroDataTextArea.setPrefRowCount(2);
        komentaroGrid.add(new Label("Comment date"), 0,3,1,1);
        komentaroGrid.add(komentaroDataTextArea, 1, 3, 1, 1);

        ipAdressTextArea = new TextArea();
        ipAdressTextArea.setPrefRowCount(2);
        komentaroGrid.add(new Label("IP address"), 0,4,1,1);
        komentaroGrid.add(ipAdressTextArea, 1, 4, 1, 1);

        komentTagTextArea = new TextArea();
        komentTagTextArea.setPrefRowCount(2);
        komentaroGrid.add(new Label("Comment Tag"), 0,5,1,1);
        komentaroGrid.add(komentTagTextArea, 1, 5, 1, 1);

        komentaroURLText = new TextField();
        komentaroGrid.add(new Label("Comment URL"), 0,6,1,1);
        komentaroGrid.add(komentaroURLText, 1, 6, 1, 1);

        hasPagingChkBox = new CheckBox("Has paging?");
        komentaroGrid.add(hasPagingChkBox, 1, 7, 1, 1);

        nextCommentPageTAGText = new TextField();
        nextPageTagLabel= new Label("Next page tag");
        komentaroGrid.add(nextPageTagLabel, 0 ,9,1,1);
        komentaroGrid.add(nextCommentPageTAGText,1,9,1,1);

        setNextPageVisibility(hasPagingChkBox.isSelected());

        hasPagingChkBox.setOnAction(e -> {
            setNextPageVisibility(hasPagingChkBox.isSelected());
        });

        Button saveBtnKomentarasBtn = new Button("Save");
        saveBtnKomentarasBtn.setPrefSize(100, 20);
        saveBtnKomentarasBtn.setOnAction(event -> saveMapping());

        komentarasHbox = new HBox();
        komentarasHbox.setPadding(new Insets(15, 12, 15, 12));
        komentarasHbox.setSpacing(10);
        komentarasHbox.getChildren().addAll(saveBtnKomentarasBtn);
        komentaroGrid.add(komentarasHbox, 0,10,15,1);

        Stage myDialog = new Stage();
        myDialog.initModality(Modality.WINDOW_MODAL);

        myDialog.setTitle("MappingController");

        TabPane tabPane = new TabPane();
        BorderPane mainPane = new BorderPane();

        //Create Tabs
        Tab straipsnisTab = new Tab();
        straipsnisTab.setText("WebPage");
        //Add something in Tab
        straipsnisTab.setContent(grid);
        tabPane.getTabs().add(straipsnisTab);

        Tab komentarasTab = new Tab();
        komentarasTab.setText("Comment");
        komentarasTab.setContent(komentaroGrid);

        //Add something in Tab
        tabPane.getTabs().add(komentarasTab);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        mainPane.setCenter(tabPane);
        return mainPane;
    }

    private void setNextPageVisibility(Boolean visible){
        nextPageTagLabel.setVisible(visible);
        nextCommentPageTAGText.setVisible(visible);
    }

    public void saveMapping(){
        if(Objects.equals(nameTextArea.getText(), "")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Website name field must be filled!");
            alert.showAndWait();
            return;
        }
        if(Objects.equals(pageURLText.getText(), "")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Website URL field must be filled!");
            alert.showAndWait();
            return;
        }
        MappingArticle map = new MappingArticle();
        map.turinys =  Arrays.asList(turinysTextArea.getText().split(";"));
        map.antraste =  Arrays.asList(antrasteTextArea.getText().split(";"));
        map.autorius =  Arrays.asList(autoriusTextArea.getText().split(";"));
        map.parasymoData =  Arrays.asList(parasymoDataTextArea.getText().split(";"));
        map.tags =  Arrays.asList(tagsTextArea.getText().split(";"));
        map.kategorija = Arrays.asList(kategorijaTextArea.getText().split(";"));
        map.koment_Komentaras =  Arrays.asList(komentarasTextArea.getText().split(";"));
        map.koment_Slapyvardis =  Arrays.asList(slapyvardisTextArea.getText().split(";"));
        map.koment_userIP =  Arrays.asList(ipAdressTextArea.getText().split(";"));
        map.koment_commentDate =  Arrays.asList(komentaroDataTextArea.getText().split(";"));
//        map.koment_email =  Arrays.asList(emailTextArea.getText().split(";"));
        map.puslapisName = nameTextArea.getText();
        map.straipsnioURL = straipsnioURLText.getText();
        map.puslapisName = nameTextArea.getText();
        map.puslapisNameProperty = new SimpleStringProperty(nameTextArea.getText());
        map.komentaroURL = komentaroURLText.getText();
        map.hasPaging = hasPagingChkBox.isSelected();
        map.koment_Tag = komentTagTextArea.getText();
        map.koment_NextPageTag = nextCommentPageTAGText.getText();

        map.koment_URL_ending =  StringUtils.difference(map.straipsnioURL,map.komentaroURL);

        if(AddNewMapView.doMappingAlreadyContains(map.puslapisName)){
            int index = AddNewMapView.getIndexByProperty(map.puslapisName);
            MainView.mapsList.remove(index);
            MainView.mapsList.add(map);
        } else {
            MainView.mapsList.add(map);
        }
        MappingController.saveMapping();
    }

    public static boolean doMappingAlreadyContains(String puslapisName) {
        return MainView.mapsList.stream().filter(o -> o.puslapisName.equals(puslapisName)).findFirst().isPresent();
    }

    public static int getIndexByProperty(String yourString) {
        for (int i = 0; i < MainView.mapsList.size(); i++) {
            if (MainView.mapsList.get(i) !=null && MainView.mapsList.get(i).puslapisName.equals(yourString)) {
                return i;
            }
        }
        return -1;// not there is list
    }
}
