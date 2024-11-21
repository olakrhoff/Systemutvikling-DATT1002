package ImageAndAlbumBoxes;

import ConfirmationBoxes.message.ConfirmationEditBox;
import ConfirmationBoxes.choice.BackToViewImageBox;
import ControlsAndLaunch.Controller;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class EditImageBox {
    @FXML
    private TextField textFieldName;
    @FXML
    private TableView tableViewSpecs;
    @FXML
    private ListView listViewTags;
    @FXML
    private TextField textFieldTag;
    @FXML
    private ImageView imageViewPreview;


    private Backend.Image image;
    private Controller controller;
    private BackToViewImageBox backToViewImageBox;
    private ConfirmationEditBox confirmationEditBox;


    /**
     * Button that saves the changes to an image and displays a confirmation box, to inform that the changes were saved.
     * Takes the user back to viewImageBox.
     * @param event
     * @throws Exception
     */
    @FXML
    public void buttonSaveClicked(Event event) throws Exception {
        String name = this.textFieldName.getText().trim();
        if (name.equals("")) {
            name = this.image.getName();
        }
        this.image = this.controller.saveEditedImage(this.image.getPath(), name, new ArrayList<>(this.listViewTags.getItems()));
        buttonSavedClickedConfirmation();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/viewImage/viewImage.fxml"));
            Parent parent = fxmlLoader.load();
            Stage window2 = new Stage();
            window2.setScene(new Scene(parent));
            if (window2.getModality() != Modality.APPLICATION_MODAL) {
                window2.initModality(Modality.APPLICATION_MODAL);
            }
            window2.setResizable(false);
            window2.setTitle("Viewing image");
            window2.show();
            ViewImageBox viewImageBox = fxmlLoader.getController();
            viewImageBox.getContext(this.image, this.controller);
            viewImageBox.addMetadata(this.image.getIso(), this.image.getShutterspeed(), this.image.getAperture(), this.image.getFocallength(),this.image.getDate().toString());
        } catch (Exception e){
            System.out.println("Something went wrong when running the click event for this image : ");
            e.printStackTrace();
        }
        controller.refresh();
    }

    /**
     * Displays a confirmation message, that the changes made to an image/album were saved.
     * @throws Exception
     */
    @FXML
    public void buttonSavedClickedConfirmation() throws Exception {
        this.confirmationEditBox = new ConfirmationEditBox();
        this.confirmationEditBox.display();
    }

    /**
     * Button that closes the window.
     * @param event
     * @throws Exception
     */
    @FXML
    public void confirmationEditBoxOkButtonClicked(Event event) {
        generalButtonCancelClicked(event);
    }

    /**
     * Button on editImageBox.
     * Displays a confirmation box (backToViewImageBox) to avoid going back from editing an
     * image without saving the changes.
     * Takes the user back to viewImageBox.
     * @param event
     * @throws Exception
     */
    @FXML
    public void buttonBackClicked(ActionEvent event) throws Exception {
        editImageButtonBackClickedConfirmation(event);
        if (this.backToViewImageBox.getReturn()){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/viewImage/viewImage.fxml"));
                Parent parent = fxmlLoader.load();
                Stage window = new Stage();
                window.setScene(new Scene(parent));
                if (window.getModality() != Modality.APPLICATION_MODAL) {
                    window.initModality(Modality.APPLICATION_MODAL);
                }
                window.setResizable(false);
                window.setTitle("Viewing image");
                window.show();
                ViewImageBox viewImageBox = fxmlLoader.getController();
                viewImageBox.getContext(this.image, this.controller);
                viewImageBox.addMetadata(this.image.getIso(), this.image.getShutterspeed(), this.image.getAperture(), this.image.getFocallength(),this.image.getDate().toString());
            } catch (Exception e){
                System.out.println("Something went wrong when running the click event for this image : ");
                e.printStackTrace();
            }
        } else {
            this.backToViewImageBox.close();
        }
    }

    /**
     * Displays a confirmation box (backToViewImageBox) to ask if the user
     * wants to go back without saving the changes.
     * @param event
     * @throws Exception
     */
    public void editImageButtonBackClickedConfirmation(Event event) throws Exception {
        this.backToViewImageBox = new BackToViewImageBox();
        this.backToViewImageBox.display();
        if (this.backToViewImageBox.getReturn()) {
            generalButtonCancelClicked(event);
        }
    }

    /**
     * Generally closes the Stage on which the Event occurs on
     * @param event
     */
    @FXML
    private void generalButtonCancelClicked(Event event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    /**
     * The method takes the selected tag and removes it from the image
     * tag-list, then places it in the input filed for the tags, giving the user
     * the opportunity to change it before adding it again
     */
    @FXML
    public void buttonEditSelectedTagClicked() {
        int selectedIndex = this.listViewTags.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            String tag = this.listViewTags.getItems().get(selectedIndex).toString();
            this.textFieldTag.setText(tag);
            this.listViewTags.getItems().remove(selectedIndex);
        }
    }

    /**
     * The method adds the text in the tag-input filed as
     * a tag to the image, by clicking the "Add Tag" button
     */
    @FXML
    public void buttonAddTagClicked() {
        String text = this.textFieldTag.getText().trim();
        if (!text.equals("")) {
            this.listViewTags.getItems().add(text);
            this.textFieldTag.setText("");
        }
    }

    /**
     * The method adds the text in the tag-input filed as
     * a tag to the image, by pressing the enter key
     * @param event
     */
    public void editImageTextFieldAddTagsEnter(KeyEvent event) {
        String text = textFieldTag.getText().trim();
        if (event.getCode() == KeyCode.ENTER) {
            if (!textFieldTag.getText().equals("")) {
                listViewTags.getItems().add(text);
                textFieldTag.setText("");
            }
        }
    }


    /**
     * Button on editImageBox.
     * Button to delete a selected tag from an image.
     */
    @FXML
    public void buttonDeleteTagClicked(){
        int selectedIndex = this.listViewTags.getSelectionModel().getSelectedIndex();
        listViewTags.getItems().remove(selectedIndex);
    }

    /**
     * The method set all the layout for the preview of the image,
     * by taking the controller and the image in question as parameters
     * @param controller
     * @param image
     */
    public void setContex(Controller controller, Backend.Image image) {
        this.controller = controller;
        this.image = image;
        imageViewPreview.setImage(new Image("file:" + this.image.getPath()));
        listViewTags.getItems().addAll(this.image.getTagsString());
        addMetadata(this.image.getIso(), this.image.getShutterspeed(), this.image.getAperture(), this.image.getFocallength(),this.image.getDate().toString());
        textFieldName.setText(this.image.getName());
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