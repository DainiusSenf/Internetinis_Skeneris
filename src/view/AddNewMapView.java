package view;

import config.CrawlerStartController;
import controlers.Crawler_Generator;
import javafx.scene.control.Button;
import models.MappingArticle;

/**
 * Created by daini on 3/19/2017.
 */
public class AddNewMapView extends MappingView {

       public AddNewMapView(){
            super();


       }

       public void addNewMapView()
       {
           super.addNewMapView();
           Button autoFillStraipsnisBtn = new Button("Auto fill map");
           autoFillStraipsnisBtn.setPrefSize(120, 20);
           autoFillStraipsnisBtn.setOnAction(event -> autoFillStraipsnis());

           Button autoFillKomentarasBtn = new Button("Auto fill map");
           autoFillKomentarasBtn.setPrefSize(120, 20);
           autoFillKomentarasBtn.setOnAction(event -> autoFillKomentaras());

           straipsnisHbox.getChildren().add(autoFillStraipsnisBtn);
           komentarasHbox.getChildren().add(autoFillKomentarasBtn);
       }

    private void autoFillKomentaras(){
        Crawler_Generator.isKomentaras = true;
        CrawlerStartController.generateMaps(komentaroURLText.getText());
        MappingArticle map = Crawler_Generator.genKomentarasMap;
        komentarasTextArea.setText(String.join(";", map.koment_Komentaras));
        komentaroDataTextArea.setText(String.join(";", map.koment_commentDate));
        ipAdressTextArea.setText(String.join(";", map.koment_userIP));
        emailTextArea.setText(String.join(";", map.koment_email));
        slapyvardisTextArea.setText(String.join(";", map.koment_Slapyvardis));
    }

    private void autoFillStraipsnis(){
        Crawler_Generator.isKomentaras = false;
        CrawlerStartController.generateMaps(straipsnioURLText.getText());
        MappingArticle map = Crawler_Generator.genStraipsnisMap;
        antrasteTextArea.setText(String.join(";", map.antraste));
        kategorijaTextArea.setText(String.join(";", map.kategorija));
        tagsTextArea.setText(String.join(";", map.tags));
        turinysTextArea.setText(String.join(";", map.turinys));
        autoriusTextArea.setText(String.join(";", map.autorius));
        parasymoDataTextArea.setText(String.join(";", map.parasymoData));
    }
}
