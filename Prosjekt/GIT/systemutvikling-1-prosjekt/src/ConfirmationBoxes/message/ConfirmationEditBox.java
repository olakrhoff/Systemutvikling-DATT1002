package ConfirmationBoxes.message;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationEditBox {

    private Stage window = new Stage();

    /**
     * Method that displays a confirmation box (confirmationEditBox)
     * @throws Exception
     */
    public void display() throws Exception {
        Parent confirmEdit = FXMLLoader.load(getClass().getResource("/FXML/confirmationBoxes/message/confirmationEdit.fxml"));
        this.window.setTitle("Comfirmation on edit");

        this.window.setOnCloseRequest(e -> {
            e.consume();
            close();
        });

        this.window.initModality(Modality.APPLICATION_MODAL);
        this.window.setResizable(false);
        this.window.setScene(new Scene(confirmEdit));
        this.window.showAndWait();
    }

    /**
     * Method that closes the window
     */
    public void close(){
        this.window.close();
    }
}
