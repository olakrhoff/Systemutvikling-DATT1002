//viewImageBox
package ImageAndAlbumBoxes;

import ConfirmationBoxes.choice.DeleteBox;
import ControlsAndLaunch.Controller;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ViewImageBox {

    private Controller controller;
    private Backend.Image image;
    private DeleteBox deleteBox;


    @FXML
    private Text labelName;
    @FXML
    private ImageView viewingImage;

    @FXML
    private TableView tableViewSpecs;

    @FXML
    private ListView listViewTags;

    /**
     * The method takes the controller and image objects to have access
     * to the same data as stored in those instances of classes
     * @param image
     * @param controller
     */
    public void getContext(Backend.Image image, Controller controller) {
        this.controller = controller;
        this.image = image;
        this.viewingImage.setImage(new Image("file:" + image.getPath()));
        this.labelName.setText(image.getName());
        this.listViewTags.getItems().addAll(this.image.getTagsString());

    }

    /**
     * The method takes the user to the edit image page.
     * It also passes the controller and image object along too
     * @param event
     * @throws IOException
     */
    @FXML
    private void viewImageButtonEditClicked(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/viewImage/editImage.fxml"));
        Parent editImage = fxmlLoader.load();
        Scene scene = new Scene(editImage);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        ((EditImageBox) fxmlLoader.getController()).setContex(getContex(), getImage());
    }

    /**
     * Button on viewImageBox (Edit scene), displays a
     * confirmation box to avoid accidentally deleting
     * the image. Takes you to the confirmation box, deleteBox.
     *
     * @param event
     * @throws Exception //@see deleteBoxButtonDeleteClicked
     *                   //@see deleteBoxButtonCancelClicked
     */
    @FXML
    private void viewImageButtonDeleteClicked(Event event) throws Exception {
        viewImageButtonDeleteClickedConfirmation(event);
        if (this.deleteBox.getReturn()){
            this.controller.viewImageBoxDeleteImage(this.image.getId());
            generalButtonCancelClicked(event);
        } else {
            this.deleteBox.close();
        }
    }

    /**
     * Button on view image box.
     * This method displays the delete box.
     * If the user wants to delete the image, it will be deleted and the user will be
     * brought back to the homepage.
     * @param event
     * @throws Exception
     */
    public void viewImageButtonDeleteClickedConfirmation(Event event) throws Exception {
        this.deleteBox = new DeleteBox();
        this.deleteBox.display();
        if (this.deleteBox.getReturn()) {
            generalButtonCancelClicked(event);
        }
    }

    /**
     * Button on viewImageBox.
     * Closes the window.
     * @param event
     */
    @FXML
    private void generalButtonCancelClicked(Event event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    public Controller getContex() {
        return this.controller;
    }

    public Backend.Image getImage() {
        return this.image;
    }

    /**
     *
     * @param iso
     * @param shutterspeed
     * @param aperture
     * @param focallength
     * @param date
     */
    public void addMetadata(String iso, String shutterspeed, String aperture, String focallength,String date) {
        this.tableViewSpecs.getColumns().clear();
        this.tableViewSpecs.getItems().clear();
        ArrayList<TableColumn> cols = Helpers.MetadataHelper.getMetaColoumns();
        for (TableColumn t:cols) {
            t.prefWidthProperty().bind(this.tableViewSpecs.widthProperty().divide(cols.size()));
        }
        this.tableViewSpecs.getColumns().addAll(cols);
        this.tableViewSpecs.getItems().addAll(Helpers.MetadataHelper.getMetadata(iso,shutterspeed,aperture,focallength,date));
    }
}


