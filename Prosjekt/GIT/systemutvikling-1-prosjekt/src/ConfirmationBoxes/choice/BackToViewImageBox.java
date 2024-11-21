package ConfirmationBoxes.choice;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BackToViewImageBox
{
    public static boolean answer = false;
    private Stage window = new Stage();

    /**
     * Method that displays the confirmation box (backToViewImage)
     * @throws Exception
     */
    public void display() throws Exception {
        Parent backToViewImage = FXMLLoader.load(getClass().getResource("/FXML/confirmationBoxes/choice/backToViewImage.fxml"));
        this.window.setTitle("Comfirmation on going back");

        this.window.setOnCloseRequest(e -> close());

        this.window.initModality(Modality.APPLICATION_MODAL);
        this.window.setResizable(false);
        this.window.setScene(new Scene(backToViewImage));
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