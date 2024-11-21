package ConfirmationBoxes.choice;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DeleteBox
{
    public static boolean answer = false;
    private Stage window = new Stage();

    /**
     * Method that displays a delete box (deleteBox)
     * @throws Exception
     */
    public void display() throws Exception {
        Parent deleteBox = FXMLLoader.load(getClass().getResource("/FXML/confirmationBoxes/choice/deleteImage.fxml"));
        this.window.setTitle("Comfirmation on deleting");
        this.window.initModality(Modality.APPLICATION_MODAL);
        this.window.setResizable(false);
        this.window.setScene(new Scene(deleteBox));
        this.window.showAndWait();
    }

    /**
     * Method that returns the answer value
     * @return
     */
    public boolean getReturn()
    {
        return this.answer;
    }

    /**
     * Method that sets the answer value to either true or false
     * @param value
     */
    public static void setAnswer(boolean value)
    {
        answer = value;
    }

    /**
     * Method that closes the window.
     */
    public void close(){
        this.window.close();
    }
}


