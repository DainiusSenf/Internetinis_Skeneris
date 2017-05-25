package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.Main;
import services.MongoDB_Connector;
import services.MySQL_Connector;

import java.util.Objects;
import java.util.prefs.Preferences;

public class DataBaseView {

    private static final String MYSQL_HOST = "MySQL_DB_HOST";
    private static final String MYSQL_PORT = "MySQL_DB_PORT";
    private static final String MYSQL_USERNAME = "MySQL_DB_Username";
    private static final String MYSQL_PASSWORD = "MySQL_DB_Password";
    private static final String MYSQL_DB_NAME = "MySQL_DB_DatabaseName";

    private static final String MONGO_DB_HOST = "Mongo_DB_Host";
    private static final String MONGO_DB_NAME = "Mongo_DB_Name";
    private static final String MONGO_DB_PORT = "Mongo_DB_Port";
    private static final String MONGO_USER = "Mongo_DB_Username";
    private static final String MONGO_PASS = "Mongo_DB_Password";

    private static final String IS_SAVED_MYSQL = "is_saved_mysql";
    private static final String IS_SAVED_MONGO = "is_saved_mongo";

    static FlowPane pane1, pane2;
    static Scene scene, scene2;
    static Button btn;
    private static TextField userTextField, pwBox, dbHost,dbPortText, dbName;

    public static Scene loadMainWindow(boolean fromConfigs){

        Boolean isDbSaved = loadMySQLDataBasesPrefs();
        if(isDbSaved && !fromConfigs)
            return MainView.loadWebCrawler();

        final ComboBox priorityComboBox = new ComboBox();
        priorityComboBox.getItems().addAll(
                "MySQL",
                "MongoDB"
        );

        priorityComboBox.setValue("MySQL");

        priorityComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                if(Objects.equals(t1, "MongoDB")){
                    loadMongoDBPrefs();
                    userTextField.setText(MongoDB_Connector.MONGO_USER);
                    pwBox.setText(MongoDB_Connector.MONGO_PASS);
                    dbHost.setText(MongoDB_Connector.MONGO_DB_HOST);
                    dbPortText.setText(String.valueOf(MongoDB_Connector.MONGO_DB_PORT));
                    dbName.setText(MongoDB_Connector.MONGO_DB_NAME);
                    MongoDB_Connector.mongoDB_selected = true;
                    MySQL_Connector.mySQL_selected = false;
                } else if (Objects.equals(t1, "MySQL")) {
                    loadMySQLDataBasesPrefs();
                    userTextField.setText(MySQL_Connector.USER);
                    pwBox.setText(MySQL_Connector.PASS);
                    dbHost.setText(MySQL_Connector.DB_HOST);
                    dbPortText.setText(MySQL_Connector.DB_PORT);
                    dbName.setText(MySQL_Connector.DB_NAME);
                    MySQL_Connector.mySQL_selected = true;
                    MongoDB_Connector.mongoDB_selected = false;
                }
            }
        });

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        scene = new Scene(grid, 500  , 500);;

        grid.add(priorityComboBox, 0, 0, 2, 1);
        grid.add(new Label("User Name:"), 0, 1);

        userTextField = new TextField();
        if(MySQL_Connector.USER != null && !MySQL_Connector.USER.isEmpty())
            userTextField.setText(MySQL_Connector.USER);
        grid.add(userTextField, 1, 1);

        grid.add(new Label("Password:"), 0, 2);
        pwBox = new PasswordField();
        if(MySQL_Connector.PASS != null && !MySQL_Connector.PASS.isEmpty())
            pwBox.setText(MySQL_Connector.PASS);
        grid.add(pwBox, 1, 2);

        grid.add(new Label("DataBase host:"), 0, 3);

        dbHost = new TextField();
        if(MySQL_Connector.DB_HOST != null && !MySQL_Connector.DB_HOST.isEmpty())
            dbHost.setText(MySQL_Connector.DB_HOST);
        grid.add(dbHost, 1, 3);

        grid.add(new Label("DataBase port:"), 0, 4);

        dbPortText = new TextField();
        if(MySQL_Connector.DB_PORT != null && !MySQL_Connector.DB_PORT.isEmpty())
            dbPortText.setText(MySQL_Connector.DB_PORT);
        grid.add(dbPortText, 1, 4);

        grid.add(new Label("Database name:"), 0, 5);

        dbName = new TextField();
        if(MySQL_Connector.DB_NAME != null && !MySQL_Connector.DB_NAME.isEmpty())
            dbName.setText(MySQL_Connector.DB_NAME);
        grid.add(dbName, 1, 5);

        btn = new Button("Save and close");
        grid.add(btn, 1, 6);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(event -> {
            String selectedValue = (String) priorityComboBox.getValue();
            if(Objects.equals(selectedValue, "MySQL")){
                if(userTextField.getText().isEmpty() || dbPortText.getText().isEmpty() || dbHost.getText().isEmpty() ||
                        dbName.getText().isEmpty() || pwBox.getText().isEmpty()){

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("All fields must be filled!");

                    alert.showAndWait();
                    return;
                }
                saveMySQLdbPrefs();

            }
            else if (Objects.equals(selectedValue, "MongoDB")){

                if(dbPortText.getText().isEmpty() || dbHost.getText().isEmpty() || dbName.getText().isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Host, Port and DataBase name fields must be filled!");

                    alert.showAndWait();
                    return;
                }
                saveMongoDbPrefs();
            }

            Main.thestage.setScene(MainView.loadWebCrawler());
            MainView.dbSettingsStage.close();
        });

        return scene;
    }

    private static void saveMySQLdbPrefs(){
        Preferences prefs = Preferences.userNodeForPackage(DataBaseView.class);
        prefs.put(MYSQL_HOST, dbHost.getText());
        prefs.put(MYSQL_PORT, dbPortText.getText());
        prefs.put(MYSQL_USERNAME, userTextField.getText());
        prefs.put(MYSQL_PASSWORD, pwBox.getText());
        prefs.put(MYSQL_DB_NAME, dbName.getText());
        prefs.put(IS_SAVED_MYSQL, "true");
        loadMySQLDataBasesPrefs();
    }

    private static void saveMongoDbPrefs(){
        Preferences prefs = Preferences.userNodeForPackage(DataBaseView.class);
        prefs.put(MONGO_DB_HOST, dbHost.getText());
        prefs.put(MONGO_DB_PORT, dbPortText.getText());
        prefs.put(MONGO_DB_NAME, dbName.getText());
        prefs.put(MONGO_PASS, pwBox.getText());
        prefs.put(MONGO_USER, userTextField.getText());
        prefs.put(IS_SAVED_MONGO, "true");
        loadMongoDBPrefs();
    }

    private static boolean loadMongoDBPrefs(){

        Preferences prefs = Preferences.userNodeForPackage(DataBaseView.class);
        //
        //// Preference key name
        String defaultValue = "default string";
        String isSaved = prefs.get(IS_SAVED_MONGO, defaultValue);
        if(Objects.equals(isSaved, "true")){
            MongoDB_Connector.MONGO_DB_HOST = prefs.get(MONGO_DB_HOST, defaultValue);
            MongoDB_Connector.MONGO_DB_NAME = prefs.get(MONGO_DB_NAME, defaultValue);
            try{
                MongoDB_Connector.MONGO_DB_PORT = Integer.valueOf(prefs.get(MONGO_DB_PORT, defaultValue));
            } catch (Exception e) {
                e.printStackTrace();
            }
            MongoDB_Connector.MONGO_PASS = prefs.get(MONGO_PASS, defaultValue);
            MongoDB_Connector.MONGO_USER = prefs.get(MONGO_USER, defaultValue);
            return true;
        } else {
            prefs.put(IS_SAVED_MONGO, "false");
            return false;
        }
    }

    private static boolean loadMySQLDataBasesPrefs(){

        Preferences prefs = Preferences.userNodeForPackage(DataBaseView.class);
        //
        //// Preference key name
        String defaultValue = "default string";
        String isSaved = prefs.get(IS_SAVED_MYSQL, defaultValue);
        if(Objects.equals(isSaved, "true")){
            MySQL_Connector.DB_NAME = prefs.get(MYSQL_DB_NAME, defaultValue);
            MySQL_Connector.DB_HOST = prefs.get(MYSQL_HOST, defaultValue);
            MySQL_Connector.DB_PORT = prefs.get(MYSQL_PORT, defaultValue);
            MySQL_Connector.USER = prefs.get(MYSQL_USERNAME, defaultValue);
            MySQL_Connector.PASS = prefs.get(MYSQL_PASSWORD, defaultValue);
            return true;
        } else {
            prefs.put(IS_SAVED_MYSQL, "false");
            return false;
        }
    }
}
