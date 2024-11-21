package ImageAndAlbumBoxes;

import ConfirmationBoxes.message.ConfirmationAddImageBox;
import ConfirmationBoxes.message.ConfirmationErrorAddImageBox;
import Backend.Gallery;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class AddOneImageBox {
    private Gallery gallery;

    @FXML
    private ImageView addOneImageImageViewPreviewImage;
    @FXML
    private TextField addOneImageTextFieldName;
    @FXML
    private TextField addOneImageTextAddTag;
    @FXML
    private ListView addOneImageListViewTags;
    @FXML
    private TableView tableViewSpecs;

    private ConfirmationAddImageBox confirmationAddImageBox;
    private ConfirmationErrorAddImageBox confirmationErrorAddImageBox;

    /**
     *
     * @param imagePath
     */
    public void init(File imagePath) {
        this.addOneImageImageViewPreviewImage.setImage(new Image("file:" + imagePath.getPath()));
        this.addOneImageTextFieldName.setPromptText(imagePath.getName());
        addMetadata(imagePath.getPath());
    }

    /**
     * Button on addOneImageBox, adds the image to
     * the database and gallery.
     * If the images were added successfully, the method will display
     * a confirmation box (confirmationAddImageBox).
     * Then it takes the user
     * back to the homepage.
     *
     * @param event
     * @throws Exception
     */
    public void addOneImageBoxButtonAddImageClicked(Event event) throws Exception {
        String path = this.addOneImageImageViewPreviewImage.getImage().getUrl().replaceAll("file:", "");//To remove "file:" from path
        ArrayList<String> tags = new ArrayList<>(this.addOneImageListViewTags.getItems());
        String name = this.addOneImageTextFieldName.getText();
        if (name.equals("")) {
            String[] pathsplit = path.split("/");
            name = pathsplit[pathsplit.length - 1]; //If the user did not choose a name. name = filename
        }
        if(this.gallery.addImage(path, name, tags)){
            this.confirmationAddImageBox = new ConfirmationAddImageBox();
            this.confirmationAddImageBox.display();
        }
        else{
            this.confirmationErrorAddImageBox = new ConfirmationErrorAddImageBox();
            this.confirmationErrorAddImageBox.display();
        }
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    /**
     *
     */
    public void addOneImageButtonAddTagClicked() {
        if (!this.addOneImageTextAddTag.getText().equals("")) {
            this.addOneImageListViewTags.getItems().add(this.addOneImageTextAddTag.getText());
            this.addOneImageTextAddTag.setText("");
        }
    }

    /**
     *
     * @param event
     */
    public void addOneImageTextFieldAddTagsEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!this.addOneImageTextAddTag.getText().equals("")) {
                this.addOneImageListViewTags.getItems().add(this.addOneImageTextAddTag.getText());
                this.addOneImageTextAddTag.setText("");
            }
        }
    }

    /**
     *
     * @param event
     */
    public void generalButtonCancelClicked(Event event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    /**
     * Gets the gallery object that the other classes are using
     * @param gallery
     */
    public void getContex(Gallery gallery) {
        this.gallery = gallery;
    }

    /**
     *
     * @param path
     */
    private void addMetadata(String path) {
        ArrayList<String> meta = this.gallery.getMetadataPreview(path);
        this.tableViewSpecs.getColumns().clear();
        this.tableViewSpecs.getItems().clear();
        ArrayList<TableColumn> cols = Helpers.MetadataHelper.getMetaColoumns();
        for (TableColumn t : cols) {
            t.prefWidthProperty().bind(this.tableViewSpecs.widthProperty().divide(cols.size()));
        }
        this.tableViewSpecs.getColumns().addAll(cols);
        String[] dateSplit;
        try {
             dateSplit = meta.get(4).split("[: ]");
             this.tableViewSpecs.getItems().addAll(Helpers.MetadataHelper.getMetadata(meta.get(0), meta.get(1), meta.get(2), meta.get(3), dateSplit[0] + "-" + dateSplit[1] + "-" + dateSplit[2]));
        } catch (Exception e){

        }
    }
}
