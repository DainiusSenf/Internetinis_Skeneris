package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.MappingArticle;


/**
 * Created by daini on 3/20/2017.
 */

public class PreviewMappingView extends MappingView {

    private final Button cellButton = new Button("Preview");
    private Stage myDialog = new Stage();
    private int selectedIndex;

    PreviewMappingView() {

        cellButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                selectedIndex = getTableRow().getIndex();

                myDialog.setTitle("MappingController preview");
                Group root = new Group();
                Scene scene = new Scene(root, 625, 625, Color.WHITE);

                BorderPane mainPane = createMappingTabs();
                fillMapps();
                root.getChildren().add(mainPane);

                myDialog.setScene(scene);
                myDialog.show();
            }
        });
    }

    public void fillMapps()
    {
        MappingArticle selectedMap = MainView.mapsList.get(selectedIndex);
        nameTextArea.setText(selectedMap.puslapisName);
        pageURLText.setText(selectedMap.pageURL);
        turinysTextArea.setText(String.join(";", selectedMap.turinys));
        kategorijaTextArea.setText(String.join(";", selectedMap.kategorija));
        antrasteTextArea.setText(String.join(";", selectedMap.antraste));
        tagsTextArea.setText(String.join(";", selectedMap.tags));
        autoriusTextArea.setText(String.join(";", selectedMap.autorius));
        parasymoDataTextArea.setText(String.join(";", selectedMap.parasymoData));
        straipsnioURLText.setText(selectedMap.straipsnioURL);
        komentarasTextArea.setText(String.join(";", selectedMap.koment_Komentaras));
        slapyvardisTextArea.setText(String.join(";", selectedMap.koment_Slapyvardis));
        komentaroDataTextArea.setText(String.join(";", selectedMap.koment_commentDate));
        ipAdressTextArea.setText(String.join(";", selectedMap.koment_userIP));
        komentTagTextArea.setText(selectedMap.koment_Tag);
        komentaroURLText.setText(selectedMap.komentaroURL);
        hasPagingChkBox.setSelected(selectedMap.hasPaging);
        nextCommentPageTAGText.setText(selectedMap.koment_NextPageTag);
    }

    //Display button if the row is not empty
    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if (!empty) {
            setGraphic(cellButton);
        }
    }

}
