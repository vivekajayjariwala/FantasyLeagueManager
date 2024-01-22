package se2203b.lab6.tennisballgames;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddScoreController implements Initializable {

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField homeScoreField;

    @FXML
    private ComboBox<String> matchesBox;

    @FXML
    private Button saveBtn;

    // The data variable is used to populate the ComboBoxes
    final ObservableList<String> data = FXCollections.observableArrayList();

    @FXML
    private TextField visitorScoreField;
    private MatchesAdapter matchesAdapter;
    private TeamsAdapter teamsAdapter;
    public void setModel(MatchesAdapter match, TeamsAdapter team) {
        matchesAdapter = match;
        teamsAdapter = team;
        buildComboBoxData();
    }

    public void buildComboBoxData() {
        try {
            data.addAll(matchesAdapter.getMatchesNamesList());
        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void save() {
        // Do some work here
        String match = matchesBox.getValue();
        String[] info = match.split("\\s*-\\s*");
        int matchIndex = Integer.parseInt(info[0]);
        String houseTeam = info[1];
        String visitorTeam = info[2];

        try {
            matchesAdapter.setTeamsScore(matchIndex,Integer.parseInt(homeScoreField.getText()),Integer.parseInt(visitorScoreField.getText()));
            teamsAdapter.setStatus(houseTeam,visitorTeam,Integer.parseInt(homeScoreField.getText()),Integer.parseInt(visitorScoreField.getText()));

        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }


    private void displayAlert(String msg) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/main/resources/se2203b/lab6/tennisballgames/WesternLogo.png"));
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }
    public void initialize(URL url, ResourceBundle rb) {
        matchesBox.setItems(data);
    }

}
