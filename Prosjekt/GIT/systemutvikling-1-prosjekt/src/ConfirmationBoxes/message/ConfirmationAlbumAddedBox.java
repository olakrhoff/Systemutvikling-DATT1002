package ConfirmationBoxes.message;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationAlbumAddedBox {

    private Stage window = new Stage();

    /**
     * Method that displays the confirmation box (confirmationAlumbAddedBox)
     * @throws Exception
     */
    public void display() throws Exception {
        Parent confirmAlbumAdded = FXMLLoader.load(getClass().getResource("/FXML/confirmationBoxes/message/confirmationAlbumAdded.fxml"));
        this.window.setTitle("Create album confirmation");

        this.window.setOnCloseRequest(e -> {
            e.consume();
            close();
        });

        this.window.initModality(Modality.APPLICATION_MODAL);
        this.window.setResizable(false);
        this.window.setScene(new Scene(confirmAlbumAdded));
        this.window.showAndWait();
    }

    /**
     * Method that closes the window.
     */
    public void close(){
        this.window.close();
    }
}
