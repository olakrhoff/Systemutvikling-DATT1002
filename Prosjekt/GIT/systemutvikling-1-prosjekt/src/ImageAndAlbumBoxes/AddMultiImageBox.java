package ImageAndAlbumBoxes;

import ConfirmationBoxes.message.ConfirmationAddImageBox;
import ConfirmationBoxes.message.ConfirmationErrorAddMultipleImageBox;
import Backend.Gallery;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class AddMultiImageBox {

    @FXML
    private ImageView imageViewPreview;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldTag;
    @FXML
    private ListView listViewTags;
    @FXML
    private TableView tableViewSpecs;
    @FXML
    private ListView listViewCommonTags;
    @FXML
    private TextField textFieldCommonTag;
    @FXML
    private Text numberOfSelectedImagesText;
    @FXML
    private VBox VboxAddMultiImage;
    private ConfirmationAddImageBox confirmationAddImageBox;

    private Gallery gallery;
    private int atIndex;
    private ArrayList<Image> images;
    private String[] names;
    private String[] promtNames;
    private ArrayList<String>[] tags; //Number of images is static, number of tags is dynamic
    private ArrayList<String>[] metadata;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private ArrayList<HBox> layoutImageViews = new ArrayList<>();

    /**
     * Button on addMultiImageBox, adds the image to
     * the database and gallery.
     * If the images were added successfully, the method will display
     * a confirmation box (confirmationAddImageBox).
     * Then it takes the user
     * back to the homepage.
     *
     * @param event
     * @throws Exception
     */
    public void addMultiButtonClicked(Event event) throws Exception {
        preRefresh();
        for (int i = 0; i < this.tags.length; i++) {
            this.tags[i].addAll(this.listViewCommonTags.getItems());
        }

        int numberOfFailed = 0;
        ArrayList<String> failedPaths = new ArrayList<>();
        for (int i = 0; i < this.images.size(); i++) {
            if (this.names[i].equals("")) {
                this.names[i] = this.promtNames[i];
            }
            if (!this.gallery.addImage(this.images.get(i).getUrl().replaceAll("file:", ""), this.names[i], this.tags[i])) {
                numberOfFailed++;
                failedPaths.add(this.images.get(i).getUrl().replaceAll("file:",""));
            }
        }
        if (numberOfFailed > 0) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/confirmationBoxes/message/confirmationErrorAddMultiple.fxml"));
            Parent confirmAddImage = fxmlLoader.load();
            Stage window = new Stage();
            window.setTitle("ERROR");

            window.setOnCloseRequest(e -> {
                e.consume();
                window.close();
            });
            window.initModality(Modality.APPLICATION_MODAL);
            window.setResizable(false);
            window.setScene(new Scene(confirmAddImage));
            ((ConfirmationErrorAddMultipleImageBox) fxmlLoader.getController()).display(numberOfFailed,  failedPaths);
            window.showAndWait();
        }
        else {
            this.confirmationAddImageBox = new ConfirmationAddImageBox();
            this.confirmationAddImageBox.display();
        }
        generalButtonCancelClicked(event);
    }

    public void setNumberOfImagesSelected() {
        this.numberOfSelectedImagesText.setText((this.atIndex + 1) + "/" + this.images.size());
    }

    /**
     * The method takes the user to the preview of the previous image
     */
    public void previousButtonClicked() {
        preRefresh();
        if (this.atIndex > 0) {
            this.atIndex--;
        } else {
            this.atIndex = this.images.size() - 1;
        }
        refreshImage();
    }

    /**
     * The method takes the user to the preview of the next image
     */
    public void nextButtonClicked() {
        preRefresh();
        if (this.atIndex < this.images.size() - 1) {
            this.atIndex++;
        } else {
            this.atIndex = 0;
        }
        refreshImage();
    }

    /**
     *
     */
    private void preRefresh() {
        this.layoutImageViews.get(this.atIndex).setStyle("-fx-border-color: black;" + "-fx-border-width: 0;");
        if (!this.textFieldName.getText().trim().equals("")) {
            this.names[this.atIndex] = this.textFieldName.getText().trim();
        }
    }

    /**
     *
     */
    private void refreshImage() {
        this.listViewTags.getItems().clear();
        this.listViewTags.getItems().addAll(this.tags[this.atIndex]);
        this.imageViewPreview.setImage(this.images.get(this.atIndex));
        this.textFieldName.setText(this.names[this.atIndex]); //if no name is set, names is default ""
        this.textFieldName.setPromptText(this.promtNames[this.atIndex]);
        this.numberOfSelectedImagesText.setText(this.atIndex + " / " + this.images.size());
        setNumberOfImagesSelected();
        this.layoutImageViews.get(this.atIndex).setStyle("-fx-border-color: lightBlue;" + "-fx-border-width: 5;");
        this.tableViewSpecs.getColumns().clear();
        this.tableViewSpecs.getItems().clear();
        ArrayList<TableColumn> cols = Helpers.MetadataHelper.getMetaColoumns();
        for (TableColumn t : cols) {
            t.prefWidthProperty().bind(this.tableViewSpecs.widthProperty().divide(cols.size()));
        }
        this.tableViewSpecs.getColumns().addAll(cols);
        String[] dateSplit;
        try {
            dateSplit = this.metadata[this.atIndex].get(4).split("[: ]");
            this.tableViewSpecs.getItems().addAll(Helpers.MetadataHelper.getMetadata(this.metadata[this.atIndex].get(0), this.metadata[this.atIndex].get(1), this.metadata[this.atIndex].get(2), this.metadata[this.atIndex].get(3), dateSplit[0] + "-" + dateSplit[1] + "-" + dateSplit[2]));
        }
        catch (Exception e)
        {
            
        }
    }

    /**
     *
     */
    public void buttonAddTag() {
        String text = this.textFieldTag.getText().trim();
        if (!text.equals("")) {
            this.listViewTags.getItems().add(text);
            this.tags[this.atIndex].add(text);
            this.textFieldTag.setText("");
        }
    }

    /**
     *
     * @param event
     */
    public void addMultiImageTextFieldAddTagEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String text = this.textFieldTag.getText().trim();
            if (!text.equals("")) {
                this.listViewTags.getItems().add(text);
                this.tags[this.atIndex].add(text);
                this.textFieldTag.setText("");
            }
        }
    }

    /**
     *
     */
    public void buttonRemoveTag() {
        int selected = this.listViewTags.getSelectionModel().getSelectedIndex();
        if (selected != -1) {
            this.listViewTags.getItems().remove(selected);
        }
    }

    /**
     *
     */
    public void buttonAddCommonTag() {
        if (!this.textFieldCommonTag.getText().trim().equals("")) {
            this.listViewCommonTags.getItems().add(this.textFieldCommonTag.getText().trim());
            this.textFieldCommonTag.setText("");
        }
    }

    /**
     *
     */
    public void commonTagsListViewClicked(){
        int selected = this.listViewCommonTags.getSelectionModel().getSelectedIndex();
        if (selected != -1){
            this.textFieldCommonTag.setText(this.listViewCommonTags.getItems().get(selected).toString()); //Even though the listViewItems are string, it returns obj, therefore a quick cast to string
            this.listViewCommonTags.getItems().remove(selected);
        }
    }

    /**
     *
     * @param event
     */
    public void addMultiImageTextFieldAddCommonTagEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!this.textFieldCommonTag.getText().equals("")) {
                this.listViewCommonTags.getItems().add(this.textFieldCommonTag.getText());
                this.textFieldCommonTag.setText("");
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
     *
     * @param files
     * @param gallery
     */
    public void setFiles(ArrayList<File> files, Gallery gallery) {
        this.gallery = gallery;
        this.images = new ArrayList<>();
        for (File f : files) {
            this.images.add(new Image("file:" + f.getPath())); //Paths have already been verified to be images
        }

        this.tags = new ArrayList[this.images.size()];
        this.names = new String[this.images.size()];
        this.metadata = new ArrayList[images.size()];
        this.promtNames = new String[this.images.size()];
        for (int i = 0; i < this.tags.length; i++) {
            this.tags[i] = new ArrayList<>();
            this.names[i] = "";
            String[] promtNamesSplit = images.get(i).getUrl().split("/");
            this.promtNames[i] = promtNamesSplit[promtNamesSplit.length - 1];
            this.metadata[i] = new ArrayList<>();
            this.metadata[i] = gallery.getMetadataPreview(this.images.get(i).getUrl().replaceAll("file:", ""));
        }

        this.atIndex = 0;
        previewOfImages();
        refreshImage();
        setNumberOfImagesSelected();
    }

    /**
     *
     */
    public void previewOfImages() {
        Font userFont = Font.getDefault();
        double userFontSize = userFont.getSize();
        for (Image i : this.images) {
            this.imageViews.add(new ImageView(i));

        }
        for (ImageView iv : this.imageViews) {
            HBox hbox = new HBox(iv);
            this.layoutImageViews.add(hbox);
        }

        for (int i = 0; i < this.imageViews.size(); i++) {
            this.imageViews.get(i).setFitHeight(6*userFontSize + 30);
            this.imageViews.get(i).setFitWidth(6*userFontSize + 30);
            this.imageViews.get(i).setPreserveRatio(true);

            int finalI = i;
            this.imageViews.get(i).setOnMouseClicked(e -> {
                preRefresh();
                this.atIndex = finalI;
                refreshImage();
            });
        }
        this.VboxAddMultiImage.getChildren().clear();
        this.VboxAddMultiImage.getChildren().addAll(this.layoutImageViews);
    }
}
