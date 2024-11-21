package ConfirmationBoxes.message;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Modality;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ConfirmationErrorAddImageBox {
    private Stage window = new Stage();

    /**
     * The method closes the stage the given Event belongs to.
     * @param event
     */
    @FXML
    public void generalButtonCancelClicked(ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    /**
     * Displays the message box, with the given message
     * @throws Exception
     */
    public void display() throws Exception {
        Parent confirmAddImage = FXMLLoader.load(getClass().getResource("/FXML/confirmationBoxes/message/confimationErrorAddImage.fxml"));
        this.window.setTitle("ERROR");

        this.window.setOnCloseRequest(e -> {
            e.consume();
            this.close();
        });

        this.window.initModality(Modality.APPLICATION_MODAL);
        this.window.setResizable(false);
        this.window.setScene(new Scene(confirmAddImage));
        this.window.showAndWait();
    }

    /**
     * Closes the Stage.
     */
    private void close() {
        this.window.close();
    }


}
