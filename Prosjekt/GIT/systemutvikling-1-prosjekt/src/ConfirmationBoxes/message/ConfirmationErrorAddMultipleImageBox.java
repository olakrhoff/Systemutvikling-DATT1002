package ConfirmationBoxes.message;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;


public class ConfirmationErrorAddMultipleImageBox {

    @FXML
    private Text confirmationAddImageTextLabel;
    @FXML
    private Text imagePaths;


    public ConfirmationErrorAddMultipleImageBox(){

    }

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
     * Displays the message box, with the given message, tells the user
     * how many of the images that failed to be added.
     * @param numberOfFailed
     * @param paths
     */
    public void display(int numberOfFailed, ArrayList<String> paths)  {
        this.confirmationAddImageTextLabel.setText(numberOfFailed + " images where not added, the rest was added succsessfully");
        for (int i = 0; i < paths.size(); i++) {
            this.imagePaths.setText(imagePaths.getText() + "\n" + paths.get(i));
        }

    }
}
