package ConfirmationBoxes.message;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationAddImageBox {

    private Stage window = new Stage();

    /**
     * Method that displays a confirmation box (confirmationAddImageBox).
     * @throws Exception
     */
    public void display() throws Exception {
        Parent confirmAddImage = FXMLLoader.load(getClass().getResource("/FXML/confirmationBoxes/message/confirmationAddImage.fxml"));
        this.window.setTitle("Image added comfirmation");

        this.window.setOnCloseRequest(e -> {
            e.consume();
            close();
        });

        this.window.initModality(Modality.APPLICATION_MODAL);
        this.window.setResizable(false);
        this.window.setScene(new Scene(confirmAddImage));
        this.window.showAndWait();
    }

    /**
     * Method that closes the window.
     */
    public void close(){
        this.window.close();
    }
}
