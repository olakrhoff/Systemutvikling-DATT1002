package ImageAndAlbumBoxes;

import ConfirmationBoxes.message.ConfirmationAlbumAddedBox;
import ConfirmationBoxes.message.ConfirmationEditBox;
import Backend.Gallery;
import ConfirmationBoxes.choice.DeleteBox;
import ConfirmationBoxes.message.CreateAlbumSameNameErrorBox;
import ControlsAndLaunch.Controller;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class CreateAlbumBox
{
    @FXML
    private FlowPane flowPaneCreateAlbum;
    @FXML
    private Button cancel;
    @FXML
    private TextField nameText;
    private Tooltip nameTextFieldErrorMessage = new Tooltip("* The album must be given a name");
    @FXML
    private TextField imagesPerPageText;
    @FXML
    private TextField imagesPerRowText;
    private Tooltip numberOfImagesErrorMessage = new Tooltip("* The input must be a number");
    @FXML
    private Button deleteButtonAlbum;
    @FXML
    private Button exportPDF;

    private DeleteBox deleteBox;
    private ConfirmationEditBox confirmationEditBox;
    private ConfirmationAlbumAddedBox confirmationAlbumAddedBox;
    private  CreateAlbumSameNameErrorBox createAlbumSameNameErrorBox;

    private Controller controller;
    private ArrayList<String> imagesToAlbum;
    private Gallery gallery;
    private Backend.Album album;

    /**
     * The method sets up all the layout and checks if the user is creating a new album
     * or simply viewing an old one. Also it takes in the gallery and controller classes
     * @param controller
     * @param imagesToAlbum
     * @param gallery
     */
    public void start(Controller controller, ArrayList<String> imagesToAlbum, Gallery gallery)
    {
        this.controller = controller;
        this.imagesToAlbum = imagesToAlbum;
        this.gallery = gallery;
        setSideList();
        if (!(gallery.getAlbums().contains(this.album)))
        {
            this.deleteButtonAlbum.setVisible(false);
            this.exportPDF.setVisible(false);
        }
        else
        {
            this.deleteButtonAlbum.setVisible(true);
            this.exportPDF.setVisible(true);
            this.cancel.setText("Close");
        }
    }

    /**
     * The method sets up all the layout and checks if the user is creating a new album
     * or simply viewing an old one. Also it takes in the gallery and controller classes,
     * but also an album, this is used for viewing an already existing album
     * @param controller
     * @param imagesToAlbum
     * @param gallery
     * @param album
     */
    public void start(Controller controller, ArrayList<String> imagesToAlbum, Gallery gallery, Backend.Album album)
    {
        this.controller = controller;
        this.imagesToAlbum = imagesToAlbum;
        this.gallery = gallery;
        this.album = album;
        setSideList();
        this.nameText.setText(album.getName());
        this.imagesPerPageText.setText(Integer.toString(album.getPages()));
        this.imagesPerRowText.setText(Integer.toString(album.getImagesPerRow()));

        if (!(gallery.getAlbums().contains(album)))
        {
            this.deleteButtonAlbum.setVisible(false);
        }
        else
        {
            this.deleteButtonAlbum.setVisible(true);
            this.cancel.setText("Close");
        }
    }

    /**
     * The method sets the images involved with the album to be shown
     * when creating the album or when viewing an already existing one
     */
    public void setSideList()
    {
        javafx.scene.text.Font usersFont = Font.getDefault();
        double usersFontSize = usersFont.getSize();
        for (int i = 0; i < this.imagesToAlbum.size(); i++)
        {
            this.flowPaneCreateAlbum.getChildren().add(new ImageView(new Image(this.imagesToAlbum.get(i))));
            ((ImageView)this.flowPaneCreateAlbum.getChildren().get(i)).setPreserveRatio(true);
            ((ImageView)this.flowPaneCreateAlbum.getChildren().get(i)).setFitWidth(10*usersFontSize);
            ((ImageView)this.flowPaneCreateAlbum.getChildren().get(i)).setFitHeight(10*usersFontSize);
        }
    }

    /**
     * Button that deletes
     * The method shows a confirmation box (deleteBox) that asks for confirmation
     * from the user, that it wants to delete an image.
     * If yes, the album is deleted.
     * @param event
     * @throws Exception
     */
    @FXML
    public void delete(Event event) throws Exception
    {
        createAlbumBoxButtonDeleteClickedConfirmation(event);
        if (this.deleteBox.getReturn()){
            generalCancel(event);
            this.gallery.getAlbumsToShow().remove(this.album);
            this.gallery.deleteAlbum(this.album);
            this.controller.refresh();
        }
    }


    /**
     * Button on createAlbumBox
     * This method displays the delete box.
     * If the user wants to delete the album, it will be deleted and the user will be
     * brought back to the homepage. If the user clicks no, the delete box closes.
     * @param event
     * @throws Exception
     */
    @FXML
    public void createAlbumBoxButtonDeleteClickedConfirmation(Event event) throws Exception {
        this.deleteBox = new DeleteBox();
        this.deleteBox.display();
        if (this.deleteBox.getReturn()) {
            generalCancel(event);
        } else {
            this.deleteBox.close();
        }
    }

    /**
     * The method closes the Stage the Event belongs to
     * @param event
     */
    @FXML
    public void cancel(Event event)
    {
        generalCancel(event);
    }

    /**
     * Generally closes the Stage on which the Event occurs on
     * @param event
     */
    private void generalCancel(Event event)
    {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    /**
     * Saves the changes to the album, or adds a new one if it does not already exist
     * @param event
     */
    @FXML
    public void save(Event event) throws Exception
    {
        int rows = 0;
        int pages = 0;

        this.nameTextFieldErrorMessage.hide();
        this.numberOfImagesErrorMessage.hide();

        this.nameText.setStyle("-fx-border-color: grey");
        this.imagesPerPageText.setStyle("-fx-border-color: grey");
        this.imagesPerRowText.setStyle("-fx-border-color: grey");
        try
        {
            if (this.nameText.getText().equals(""))
            {
                this.nameText.clear();
                this.controller.showTextFieldsErrorMessageToolTip(this.nameTextFieldErrorMessage, this.nameText, -30);
                throw new IllegalArgumentException("Album must have a name");
            }
            if ((this.gallery.getAlbumByName(this.nameText.getText())) != null)
            {
                throw new IllegalArgumentException("An album with this name already exists");
            }
            if (this.imagesPerRowText.getText().equals(""))
            {
                this.imagesPerRowText.setText("1");
            }
            if (this.imagesPerPageText.getText().equals(""))
            {
                this.imagesPerPageText.setText("1");
            }

            rows = Integer.parseInt(this.imagesPerRowText.getText());
            pages = Integer.parseInt(this.imagesPerPageText.getText());
            buttonSavedClickedConfirmation();
            generalCancel(event);

            //The album is already stored in the db, changes will be added
            if (this.gallery.getAlbums().contains(this.album))
            {
                this.gallery.editAlbum(this.album.getId(), rows, pages, this.nameText.getText());
            }
            else {
                this.gallery.addAlbum(rows, pages, this.nameText.getText(), this.getImages());
                this.gallery.getAlbumsToShow().add(this.gallery.getAlbumByName(this.nameText.getText()));
            }

        }
        catch(NumberFormatException nfe){
            if (rows == 0 && pages == 0) {
                this.imagesPerRowText.clear();
                this.controller.showTextFieldsErrorMessageToolTip(this.numberOfImagesErrorMessage, this.imagesPerRowText, 30);

            } else {
                this.imagesPerPageText.clear();
                this.controller.showTextFieldsErrorMessageToolTip(this.numberOfImagesErrorMessage, this.imagesPerPageText, -30);
            }

        }

        catch (IllegalArgumentException iae)
        {
            if (iae.getMessage().equals("An album with this name already exists"))
            {
                createAlbumSameNameErrorBox = new CreateAlbumSameNameErrorBox();
                createAlbumSameNameErrorBox.display();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * The method exports a PDF of the album
     * @throws Exception
     */
    @FXML
    public void exportPDF() throws Exception
    {
        Desktop.getDesktop().open(new File(this.gallery.createPdfFromAlbum(this.album.getId())));
    }

    /**
     * Button that displays a confirmationEditBox.
     * The display box shows a confirmation that the changes were saved.
     * @throws Exception
     */
    @FXML
    public void buttonSavedClickedConfirmation() throws Exception {
        this.confirmationEditBox = new ConfirmationEditBox();
        this.confirmationAlbumAddedBox = new ConfirmationAlbumAddedBox();
        if (!(this.gallery.getAlbums().contains(this.album))) {
            this.confirmationAlbumAddedBox.display();
            //unselect all images
        } else {
            this.confirmationEditBox.display();
        }
    }

    /**
     * The method gets all the images from the gallery corresponding to the list of url's
     * passed in as a object variable
     * @return      ArrayList<Backend.Image>
     */
    private ArrayList<Backend.Image> getImages()
    {
        ArrayList<Backend.Image> returnList = new ArrayList<>();
        for (String url : this.imagesToAlbum)
        {
            String newURL = ((String[])(url.split(":")))[1];
            Backend.Image newImage = this.gallery.getImageByPath(newURL);
            returnList.add(newImage);
        }
        return returnList;
    }
}
