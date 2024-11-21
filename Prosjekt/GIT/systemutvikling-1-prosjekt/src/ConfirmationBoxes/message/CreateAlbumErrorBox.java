package ConfirmationBoxes.message;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateAlbumErrorBox {
    private Stage window = new Stage();

    /**
     * Displays the message box, with the given message
     * @throws Exception
     */
    public void display() throws Exception {
        Parent errorAlbum = FXMLLoader.load(getClass().getResource("/FXML/confirmationBoxes/message/createAlbumImages.fxml"));
        window.setTitle("Create album error");

        window.setOnCloseRequest(e -> {
            e.consume();
            close();
        });

        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setScene(new Scene(errorAlbum));
        window.showAndWait();
    }

    /**
     * Method that closes the window
     */
    public void close(){
        window.close();
    }
}
