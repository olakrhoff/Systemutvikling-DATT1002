package ControlsAndLaunch;

import ConfirmationBoxes.choice.BackToViewImageBox;
import ConfirmationBoxes.choice.DeleteBox;
import ImageAndAlbumBoxes.*;
import ImageAndAlbumBoxes.AddOneImageBox;
import ConfirmationBoxes.message.ConfirmationErrorAddImageBox;
import ConfirmationBoxes.message.CreateAlbumErrorBox;
import Backend.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import org.apache.poi.ss.formula.functions.Even;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

//NOTES:
//The method names in this class have a system
//They start with the location of the object that has an interaction (e.g. a Button at your main window)
//Then what type of Object (e.g. A Button)
//Then the text of the object
//Then the type of interaction (e.g. Clicked, for a button)
//Do note that some places the keyword "general" has been used, this simple means that the object is accessible from multiple locations

//If you got a Button, with the text "Home" on your preview car page, and it's action is to take you back to the homepage
//Then the method name with this system would be: previewCarPageButtonHomeClicked()


public class Controller implements Initializable {

    /**
     * Alert-boxes that will be initialized later in
     * the code, when they are needed.
     */
    private CreateAlbumErrorBox createAlbumErrorBox;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjectPU");
    private ImageDAO imageDAO = new ImageDAO(emf);
    private AlbumDAO albumDAO = new AlbumDAO(emf);
    private TagDAO tagDAO = new TagDAO(emf);
    private Gallery gallery = new Gallery(imageDAO, albumDAO, tagDAO);

    private ArrayList<String> imagesToAlbum = new ArrayList<>();
    private ArrayList<String> searchWords = new ArrayList<>();
    private ArrayList<String> filterWords = new ArrayList<>();
    private ArrayList<String> imagesToOpenAlbum = new ArrayList<>();
    private HBox filterWordContainer = new HBox();


    //FXML declaration of fields with id that we will use in controller class methods
    //HomePageGalleryView declaration
    @FXML
    private ScrollPane homepageGalleryViewScrollPane;
    @FXML
    private FlowPane homepageGalleryViewFlowPane;
    @FXML
    private TextField searchBarHomepageGalleryView;
    @FXML
    private TextField fromDateHomePage;
    @FXML
    private TextField toDateHomepage;
    private Tooltip dateTextfieldErrorMessage = new Tooltip("* Wrong date-format (YYYY-MM-DD)");
    @FXML
    private ComboBox<String> comboBoxHomepage;
    @FXML
    private ScrollPane homepageScrollFilter;
    @FXML
    private TextField homepageFilterInputField;
    //End of HomePageGalleryView declaration

    //Create Album
    @FXML
    private FlowPane flowPaneAlbum;
    @FXML
    private TextField fromDateCreateAlbum;
    @FXML
    private TextField toDateCreateAlbum;
    @FXML
    private ComboBox<String> comboBoxCreateAlbumWindow;
    @FXML
    private Text numberOfSelectedImagesForAlbum;
    @FXML
    private TextField searchBarCreateAlbum;
    @FXML
    private Button selectDeselectAll;
    @FXML
    private ScrollPane createAlbumWindowScrollFilter;
    @FXML
    private TextField createAlbumWindowFilterInputField;

    public Controller() {
    }
    // End of ViewImageBox declaration
//End of FXML declaration

    /**
     * The method is called by the FXMLLoader when initialization is complete,
     * then refreshes the dynamic elements such as images and albums
     * @param fxmlFileLocation
     * @param resources
     */
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        //This piece of code load the images from the database and display them in the Gallery and in the create-album-window
        try {
            refresh();
            this.homepageGalleryViewScrollPane.setFitToWidth(true);
            this.homepageGalleryViewScrollPane.setFitToHeight(false);
            this.comboBoxHomepage.setValue("Date added (oldest to newest)");
        }
        catch (Exception e)
        {
            try {
                refreshInCreateAlbum();
            }
            catch (Exception ex)
            {

            }
        }
    }

    /**
     * General method to refresh the images and albums shown in homePageGalleryView gets all the
     * images and albums from the gallery in gallery to show the images selected by other methods
     */
    public void refresh() {
        ArrayList<ImageView> imageViews = new ArrayList<>();
        Font usersFont = Font.getDefault();
        double usersFontSize = usersFont.getSize();

        //Get images to show
        ArrayList<Backend.Image> backendImages = new ArrayList<>();
        for (String path : this.gallery.getImagesToShow()) {
            if (path != null && (new File(path).isFile())) //Checks if imagepath is a file on your computer
            {
                backendImages.add(this.gallery.getImageByPath(path));
            }
        }

        //Get albums to show
        ArrayList<Backend.Album> albums = new ArrayList<>(this.gallery.getAlbumsToShow());
        ArrayList<Backend.Album> albumsToView = new ArrayList<>();
        for (Backend.Album album : albums) {
            if (!(album.getImages().isEmpty())) {
                for (Image image : album.getImages()) {
                    if (new File(image.getPath()).isFile() && !(albumsToView.contains(album))) {
                        albumsToView.add(album);
                    }
                }
            }
        }

        //Add imagesToShow into imageviews
        for (Backend.Image im : backendImages) {
            imageViews.add(new ImageView(new javafx.scene.image.Image("file:" + im.getPath())));
        }
        //Adding albums to imageviews
        for (Backend.Album album : albumsToView) {
            imageViews.add(new ImageView(new javafx.scene.image.Image("StockImages/folderTwo.jpg")));
        }

        this.homepageGalleryViewFlowPane.getChildren().clear();

        for (int i = 0; i < imageViews.size(); i++) { //Adds the images to imagevievs, add viewDetails click event, and adds them to the appropriate pane
            int finalI = i;
            imageViews.get(i).setFitHeight(Math.floor(10 * usersFontSize)); // Scales the size to the users prefered font size, for readability
            imageViews.get(i).setFitWidth(Math.floor(10 * usersFontSize));
            imageViews.get(i).setPreserveRatio(true);
            if (imageViews.get(finalI).getImage().getUrl().contains("StockImages/folderTwo.jpg")) //If it's an album
            {
                Backend.Album album = albumsToView.get(i - backendImages.size());
                Label albumName = new Label(album.getName()); //BackendImages and imageviews have the same positions
                albumName.setAlignment(Pos.BOTTOM_CENTER);
                albumName.setContentDisplay(ContentDisplay.TOP);
                albumName.setGraphicTextGap(1);
                albumName.setBackground(this.homepageGalleryViewFlowPane.getBackground());
                albumName.setGraphic(imageViews.get(i));
                albumName.setFont(usersFont); //Uses the users preferred font
                albumName.setOnMouseClicked(e ->
                {
                    try {
                        for (Backend.Image image : album.getImages()) {
                            this.imagesToOpenAlbum.add("file:" + image.getPath());
                        }
                        homepageGalleryViewAlbumClicked(album, e);

                    } catch (Exception ex) {
                        System.out.println("Something happened in 'setOnMouseClicked' for Album");
                        ex.printStackTrace();
                    }
                });
                this.homepageGalleryViewFlowPane.getChildren().add(albumName);
            } else {
                Label imageName = new Label(backendImages.get(i).getName()); //BackendImages and imageviews have the same positions
                imageName.setAlignment(Pos.BOTTOM_CENTER);
                imageName.setContentDisplay(ContentDisplay.TOP);
                imageName.setGraphicTextGap(1);
                imageName.setGraphic(imageViews.get(i));
                imageName.setFont(usersFont); //Uses the users preferred font
                imageName.setOnMouseClicked(e -> {
                    try {
                        Backend.Image img = backendImages.get(finalI);
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
                        viewImageBox.getContext(img, this);
                        viewImageBox.addMetadata(img.getIso(), img.getShutterspeed(), img.getAperture(), img.getFocallength(), img.getDate().toString());
                    } catch (Exception ex) {
                        System.out.println("Something went wrong when running the click event for this image : " + imageViews.get(finalI).getImage().getUrl() + "\nM: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                });
                this.homepageGalleryViewFlowPane.getChildren().add(imageName);
            }
        }
    }

    /**
     * This method loads in the images in the at the createAlbum page.
     * It also adds the selecting functionality.
     */
    public void refreshInCreateAlbum()
    {
        ArrayList<ImageView> imageViews = new ArrayList<>();
        ArrayList<VBox> finalImageViews = new ArrayList<>();
        String style_selected = "-fx-border-color: lightblue;" + "-fx-border-width: 10;";
        Font usersFont = Font.getDefault();
        double usersFontSize = usersFont.getSize();

        //Get images to show
        ArrayList<Backend.Image> images = new ArrayList<>();
        for (String path : this.gallery.getImagesToShow()) {
            if (path != null && (new File(path).isFile())) //Checks if imagepath is a file on your computer
            {
                images.add(this.gallery.getImageByPath(path));
            }
        }

        //Add imagesToShow into imageviews
        for (Backend.Image im : images) {
            ImageView imageView = new ImageView(new javafx.scene.image.Image("file:" + im.getPath()));
            imageViews.add(imageView);
        }
        for (ImageView iv : imageViews) {
            VBox vbox = new VBox(iv);
            finalImageViews.add(vbox);
        }
        for (int i = 0; i < finalImageViews.size(); i++) {
            int finalI = i;
            imageViews.get(i).setOnMouseClicked(e -> {
                try {
                    if (finalImageViews.get(finalI).getStyle().equals(style_selected)) //If it's selected
                    {
                        this.imagesToAlbum.remove(imageViews.get(finalI).getImage().getUrl()); //Removes image URL from "imagesToAlbum"
                    }

                    else //If it's not selected
                    {
                        this.imagesToAlbum.add(imageViews.get(finalI).getImage().getUrl()); //Adding image URL to "imagesToAlbum"
                    }
                    refreshSelectedImages(finalImageViews);
                } catch (Exception ex) {
                    System.out.println("Something went wrong when running the click event for this image : " + imageViews.get(finalI).getImage().getUrl() + "\nM: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            imageViews.get(i).setFitHeight(10*usersFontSize);
            imageViews.get(i).setFitWidth(10*usersFontSize);
            imageViews.get(i).setPreserveRatio(true);
        }

        this.flowPaneAlbum.getChildren().clear();
        displayNameToImagesInAlbum(finalImageViews, images);
        refreshSelectedImages(finalImageViews);
    }

    /**
     * The method sets all the names on the images shown in the createAlbum page
     * @param finalImageViews
     * @param images
     */
    private void displayNameToImagesInAlbum(ArrayList<VBox> finalImageViews, ArrayList<Backend.Image> images)
    {
        for (int i = 0; i < finalImageViews.size(); i++)
        {
            Backend.Image image = images.get(i);
            Text imageName = new Text(image.getName());
            imageName.setWrappingWidth(110);
            imageName.setFont(Font.font(null, FontWeight.BOLD, 14));
            finalImageViews.get(i).getChildren().add(imageName);
            this.flowPaneAlbum.getChildren().add(finalImageViews.get(i));
        }
    }

    /**
     * The method checks and refreshes the images that are in the selection list,
     * either by removing or adding the selected-border
     * @param finalImageViews
     */
    private void refreshSelectedImages(ArrayList<VBox> finalImageViews)
    {
        String style_selected = "-fx-border-color: lightblue;" + "-fx-border-width: 10;";
        String style_deselected = "-fx-border-color: black;" + "-fx-border-width: 0;";
        for (int i = 0; i < finalImageViews.size(); i++)
        {
            if (this.imagesToAlbum.contains(((ImageView)(finalImageViews.get(i).getChildren().get(0))).getImage().getUrl()))
            {
                finalImageViews.get(i).setStyle(style_selected);
            }
            else
            {
                finalImageViews.get(i).setStyle(style_deselected);
            }
        }
        this.numberOfSelectedImagesForAlbum.setText("Selected: " + this.imagesToAlbum.size());
    }

    /**
     * The method selects/deselects all the images in the createAlbum page,
     * depending on what i did last time, starts at "Select all"
     */
    public void createAlbumButtonSelectAllClicked()
    {
        if (selectDeselectAll.getText().equals("Select all"))
        {
            this.imagesToAlbum.clear();
            String style_selected = "-fx-border-color: lightblue;" + "-fx-border-width: 10;";
            for (int i = 0; i < this.flowPaneAlbum.getChildren().size(); i++) {
                this.imagesToAlbum.add(((ImageView) (((VBox) (this.flowPaneAlbum.getChildren().get(i))).getChildren().get(0))).getImage().getUrl());
                this.flowPaneAlbum.getChildren().get(i).setStyle(style_selected);
            }
            this.numberOfSelectedImagesForAlbum.setText("Selected: " + this.imagesToAlbum.size());
            selectDeselectAll.setText("Deselect all");
        }
        else
        {
            String style_deselected = "-fx-border-color: black;" + "-fx-border-width: 0;";
            for (int i = 0; i < this.flowPaneAlbum.getChildren().size(); i++)
            {
                this.flowPaneAlbum.getChildren().get(i).setStyle(style_deselected);
            }
            this.imagesToAlbum.clear();
            this.numberOfSelectedImagesForAlbum.setText("Selected: " + this.imagesToAlbum.size());
            selectDeselectAll.setText("Select all");
        }
    }



    /**
     * General button, takes user back to homepage (Gallery view)
     * Found at:
     * homepage (Gallery view)
     * create album (Gallery view)
     *
     * @param event
     * @throws Exception
     */
    public void galleryViewGeneralButtonHomeClicked(Event event) throws Exception {
        Parent homepage = FXMLLoader.load(getClass().getResource("/FXML/homepage/homepageGalleryView.fxml"));
        Scene scene = new Scene(homepage);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        try
        {
            refresh();
        }
        catch (Exception e)
        {

        }
    }

    /**
     * Button on homepage (Gallery view), refreshes
     * the homepage.
     *
     * @throws Exception
     */
    public void homepageGalleryViewButtonRefreshClicked(Event event) {
        try {
            galleryViewGeneralButtonHomeClicked(event);
        }catch(Exception e){

        }
    }

    /**
     * Refreshes the images to be shown in the createAlbum page
     */
    public void createAlbumRefreshButtonClicked()
    {
        //Resets filtered-, filtered by date- and searched images
        this.searchBarCreateAlbum.setText("");
        createAlbumWindowSearchButtonPressed();

        removeAllFilters(this.createAlbumWindowScrollFilter, this.createAlbumWindowFilterInputField);

        this.fromDateCreateAlbum.setText("");
        this.toDateCreateAlbum.setText("");
        filterByDate(this.fromDateCreateAlbum, this.toDateCreateAlbum);

        this.gallery.setImagesAndAlbumsToShow();
        sortImages(comboBoxCreateAlbumWindow);
        refreshInCreateAlbum();
    }

    /**
     * General button, closes the window.
     *
     * @param event
     * @throws Exception
     */
    public void generalButtonCancelClicked(Event event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();

    }

    /**
     * General method that opens finder/files (depending on OS)
     * where the user will choose one or more files, which will be returned
     * as a List<File> object. Then it checks if all the elements are of the supported image format
     * if not, they are removed, and user will be notified about the removal.
     *
     * @param event
     * @throws Exception
     */
    public void homepageButtonAddImagesClicked(Event event) {
        FileChooser fileChooser = new FileChooser();
        try {
            ArrayList<File> foundFiles = new ArrayList<>(fileChooser.showOpenMultipleDialog((Stage) ((Node) event.getSource()).getScene().getWindow()));
            //Drops all files that are not images
            //If filename is a valid image file. description: [Does not start with whitespace] + (any character(Ignore case)(jpg or png or...))endline)
            ArrayList<File> files = foundFiles.stream().filter(f -> (f).getPath().matches("([^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg|exif|tiff|hdr|heif|bat|webp|jfif|cr2))$)")).collect(Collectors.toCollection(ArrayList::new));

            if (files.size() == 1) //With one image chosen, open addOneImageBox
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/addImage(s)/addOneImage.fxml"));
                Parent addOneImage = fxmlLoader.load();
                AddOneImageBox addOneImageBox = fxmlLoader.getController();
                addOneImageBox.getContex(this.gallery);
                addOneImageBox.init(foundFiles.get(0));
                Stage window = new Stage();
                window.setTitle("Adding image");
                window.initModality(Modality.APPLICATION_MODAL);
                window.setScene(new Scene(addOneImage));
                window.showAndWait();
            }
            else if (files.size() > 1) //with multiple images chosen, open AddMultipleImages
            {
                Stage window = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/addImage(s)/addMultiImage.fxml"));
                Parent addMultiImage = fxmlLoader.load();
                window.setTitle("Adding images");
                window.initModality(Modality.APPLICATION_MODAL);
                window.setResizable(false);
                window.setScene(new Scene(addMultiImage));
                ((AddMultiImageBox) fxmlLoader.getController()).setFiles(files, this.gallery);
                window.showAndWait();

            } else {
                ConfirmationErrorAddImageBox error = new ConfirmationErrorAddImageBox();
                error.display();
            }
            refresh();
        } catch (Exception e) {
            System.out.println("Something went wrong when collecting the files");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        refresh();
    }


     //Section for creating an album
    /**
     * Button on homepage (Gallery view), takes the user to the
     * create album (Gallery view) Scene.
     *
     * @param event
     * @throws Exception
     */
    public void homepageGalleryViewButtonCreateAlbumClicked(Event event) throws Exception {
        Parent createAlbumPage = FXMLLoader.load(getClass().getResource("/FXML/createAlbum/createAlbumHomepageGalleryView.fxml"));
        Scene scene = new Scene(createAlbumPage, 800, 550);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * General button, takes the user to the
     * create album alert-box
     * Found at:
     * create album (Gallery view) page
     * create album (Map view) page
     *
     * @throws Exception
     */
    public void createAlbumGeneralButtonCreateAlbumClicked(Event event) throws Exception {
        int noAlbumsStored = this.gallery.getAlbums().size();
        if (this.imagesToAlbum.size() != 0) {
            Stage window = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/createAlbum/createAlbum.fxml"));
            Parent createAlbum = fxmlLoader.load();
            window.setTitle("Creating album");
            window.initModality(Modality.APPLICATION_MODAL);
            window.setResizable(false);
            window.setScene(new Scene(createAlbum));
            CreateAlbumBox newCreateAlbumBox = fxmlLoader.getController();
            newCreateAlbumBox.start(this, this.imagesToAlbum, this.gallery);
            window.showAndWait();

            if (noAlbumsStored == this.gallery.getAlbums().size() - 1){
                FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/FXML/homepage/homepageGalleryView.fxml"));
                Parent homepage = fxmlLoader2.load();
                Controller controller = fxmlLoader2.getController();
                controller.refresh();

                Scene scene = new Scene(homepage, 800, 550);
                Stage window2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window2.setScene(scene);
                window2.show();
            }

        }
        else
        {
            this.createAlbumErrorBox = new CreateAlbumErrorBox();
            this.createAlbumErrorBox.display();
        }
    }

    /**
     * The method shows informative messages at the input fields that were not valid
     * @param tooltip
     * @param tooltipOwner
     * @param addToyCoPlacement
     */
    public void showTextFieldsErrorMessageToolTip(Tooltip tooltip, TextField tooltipOwner, int addToyCoPlacement){ //adjusts the y coordinate of tooltip placement
        tooltipOwner.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        Point2D p = tooltipOwner.localToScene(0.0, 0.0);
        double xCo = p.getX() + tooltipOwner.getScene().getX() + tooltipOwner.getScene().getWindow().getX();
        double yCo = p.getY() + tooltipOwner.getScene().getY() + tooltipOwner.getScene().getWindow().getY() + addToyCoPlacement;
        tooltip.show(tooltipOwner, xCo, yCo);
    }

    /**
     * Saves the changes done to the image, and updates this in the
     * gallery, hens the database too
     * @param name
     * @param tags
     */
    public Backend.Image saveEditedImage(String path, String name, ArrayList<String> tags) {
        this.gallery.deleteImage(gallery.getImageByPath(path).getId());
        this.gallery.addImage(path, name, tags);
        return gallery.getImageById(this.gallery.getImageByPath(path).getId());
    }

    /**
     * Deletes the selected image, from the gallery, hens
     * the database too, then refreshes the homepage
     * @param id
     */
    public void viewImageBoxDeleteImage(int id) {
        Backend.Image imageToBeDeleted = this.gallery.getImageById(id);
        this.gallery.getImagesToShow().remove(imageToBeDeleted.getPath());
        this.gallery.deleteImage(id);
        refresh();
    }

    /**
     * This method opens up already existing albums.
     * @param album
     * @throws Exception
     */
    public void homepageGalleryViewAlbumClicked(Backend.Album album, Event event) throws Exception
    {
        Stage window = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/createAlbum/createAlbum.fxml"));
        Parent createAlbum = fxmlLoader.load();
        window.setTitle("Edit album");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setScene(new Scene(createAlbum));
        ((CreateAlbumBox) fxmlLoader.getController()).start(this, this.imagesToOpenAlbum, this.gallery, album);
        window.showAndWait();

        this.imagesToOpenAlbum.clear();

        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/FXML/homepage/homepageGalleryView.fxml"));
        Parent homepage = fxmlLoader2.load();
        Controller controller = fxmlLoader2.getController();
        controller.refresh();

        Scene scene = new Scene(homepage, 800, 550);
        Stage window2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window2.setScene(scene);
        window2.show();

    }

    //Section for viewing and editing images
    /**
     * Button on deleteBox, it confirms whether or not the user wants to delete the image
     * and takes the user back to the edit page.
     *
     * @param event
     */
    public void deleteBoxButtonDeleteClicked(Event event) {
        DeleteBox.setAnswer(true);
        generalButtonCancelClicked(event);
    }

    /**
     * Button on deleteBox, closes deleteBox and
     * takes the user back to the viewImageBox.
     *
     * @param event
     */
    public void deleteBoxButtonCancelClicked(Event event){
        DeleteBox.setAnswer(false);
        generalButtonCancelClicked(event);
    }


    /**
     * Button on backToViewImageBox, it confirms that the user wants
     * to go back to viewing the image without saving.
     * Takes the user back to the viewing page.
     *
     * @param event
     */
    public void backToViewImageBoxButtonYesClicked(Event event) {
        BackToViewImageBox.setAnswer(true);
        generalButtonCancelClicked(event);
    }

    /**
     * Button on backToViewImageBox, it confirms that the user does not want
     * to go back to viewing the image without saving.
     * Takes the user back to the edit page.
     *
     * @param event
     */
    public void backToViewImageBoxButtonNoClicked(Event event) {
        BackToViewImageBox.setAnswer(false);
        generalButtonCancelClicked(event);
    }


    //Section for sorting, filtering and searching images

    /**
     * Method collect searchdata through a textfield
     * and searches through the albums' and images' metdata and tags to find images relating to the search
     * @param searchBar
     */
    public void searchImagesAndAlbumsFromTextInput(TextField searchBar){
        this.searchWords.clear();
        String searchData = searchBar.getText();
        if (searchData.equals("")){
            //Empty string sets gallery.searchedImages to every image stored
            this.gallery.setSearchedImages(this.gallery.getImages());
            this.gallery.setSearchedAlbums(this.gallery.getAlbums());
        }
        else {
            String[] searchDataArray = searchData.split(" ");
            for (String searchWord : searchDataArray) {
                this.searchWords.add(searchWord);
            }

            //Sets gallery.searchedImages and gallery.searchedAlbums
            this.gallery.searchImages(this.searchWords);
            this.gallery.searchAlbums(this.searchWords);
        }
        searchBar.clear();
    }

    /**
     * Method collects searchwords from searchbar on the homepage
     * when enter is pressed on the searchbar, and searches for images and albums based on this
     * @see searchImagesAndAlbumsFromTextInput
     * @param event
     */
    public void homepageSearchEnterPressed(Event event)
    {
        if (((KeyEvent)event).getCode() == KeyCode.ENTER)
        {
            searchImagesAndAlbumsFromTextInput(this.searchBarHomepageGalleryView);
            this.gallery.setImagesAndAlbumsToShow();
            sortImages(this.comboBoxHomepage);
            sortAlbums(this.comboBoxHomepage);
            refresh();
            //Resets searched images and -albums after every search (after searched images/albums are displayed).
            this.gallery.setSearchedImages(this.gallery.getImages());
            this.gallery.setSearchedAlbums(this.gallery.getAlbums());
        }
    }

    /**
     *Method collects searchwords from searchbar on the homepage when the
     *search-button is clicked, and searches for images and albums based on this
     *@see searchImagesAndAlbumsFromTextInput
     */
    public void homepageSearchSearchButtonPressed()
    {
        searchImagesAndAlbumsFromTextInput(this.searchBarHomepageGalleryView);
        this.gallery.setImagesAndAlbumsToShow();
        sortImages(this.comboBoxHomepage);
        sortAlbums(this.comboBoxHomepage);
        refresh();
        this.gallery.setSearchedImages(this.gallery.getImages());
        this.gallery.setSearchedAlbums(this.gallery.getAlbums());
    }

    /**
     *Method collects searchwords from searchbar in the create album-window
     *when enter is pressed on the searchbar, and searches for images and albums based on this
     *@see searchImagesAndAlbumsFromTextInput
     * @param event
     */
    public void createAlbumWindowSearchEnterPressed(Event event)
    {
        if (((KeyEvent)event).getCode() == KeyCode.ENTER)
        {
            searchImagesAndAlbumsFromTextInput(this.searchBarCreateAlbum);
            this.gallery.setImagesAndAlbumsToShow();
            sortImages(this.comboBoxCreateAlbumWindow);
            refreshInCreateAlbum();
            this.gallery.setSearchedImages(this.gallery.getImages());
        }
    }

    /**
     *Method collects searchwords from searchbar in the create album-window when the
     *search-button is clicked, and searches for images and albums based on this
     *@see searchImagesAndAlbumsFromTextInput
     */
    public void createAlbumWindowSearchButtonPressed()
    {
        searchImagesAndAlbumsFromTextInput(this.searchBarCreateAlbum);
        this.gallery.setImagesAndAlbumsToShow();
        sortImages(this.comboBoxCreateAlbumWindow);
        refreshInCreateAlbum();
        this.gallery.setSearchedImages(this.gallery.getImages());
    }


    /**
     * Method for filter images, by metadata and tags.
     * Collects filter-data from the input-textfield and displays it on the page's scrollpane
     * @param filterInputField
     * @param scrollPane
     */
    public void filterImagesAndAlbumsByInput(TextField filterInputField, ScrollPane scrollPane)
    {
        String filterData = filterInputField.getText();
        ArrayList<String> checkList = new ArrayList<>();

        Button removefilterwordButton = new Button("x");
        removefilterwordButton.setMaxSize(6,7);
        removefilterwordButton.setStyle("-fx-border-color: lightGrey;" + "-fx-font-size: 7;" + "-fx-text-fill: grey;");

        Text newFilter = new Text(filterData);
        newFilter.setFont(Font.font(null, FontWeight.NORMAL, 12));

        if (!(newFilter.getText().equals("")))
        {
            this.filterWordContainer.getChildren().addAll(newFilter, removefilterwordButton);
            HBox.setMargin(newFilter, new Insets(5, 5, 0, 22));
            HBox.setMargin(removefilterwordButton, new Insets(4, 0, 0, 0));
        }

        removefilterwordButton.setOnMouseEntered(e ->
        {
            newFilter.setFont(Font.font(null, FontWeight.BOLD, 14));
            newFilter.setFill(Paint.valueOf("Red"));
        });

        //Makes the word smaller and black again
        newFilter.setOnMouseExited(e ->
        {
            newFilter.setFont(Font.font(null, FontWeight.NORMAL, 12));
            newFilter.setFill(Paint.valueOf("Black"));
        });

        removefilterwordButton.setOnMouseExited(e ->
        {
            newFilter.setFont(Font.font(null, FontWeight.NORMAL, 12));
            newFilter.setFill(Paint.valueOf("Black"));
        });


        //Deletes the word from the filter-container
        newFilter.setOnMouseClicked(e ->
        {
            this.filterWordContainer.getChildren().removeAll(newFilter, removefilterwordButton);
            checkList.clear();
            filterImagesAndAlbumsByInput(filterInputField, scrollPane);
            this.gallery.setImagesAndAlbumsToShow();
            if(scrollPane == this.homepageScrollFilter){
                sortImages(this.comboBoxHomepage);
                sortAlbums(this.comboBoxHomepage);
                refresh();
            }
            else{
                sortImages(this.comboBoxCreateAlbumWindow);
                refreshInCreateAlbum();
            }

        });

        removefilterwordButton.setOnMouseClicked(e ->
        {
            this.filterWordContainer.getChildren().removeAll(newFilter, removefilterwordButton);
            checkList.clear();
            filterImagesAndAlbumsByInput(filterInputField, scrollPane);
            this.gallery.setImagesAndAlbumsToShow();
            if(scrollPane == this.homepageScrollFilter){
                sortImages(this.comboBoxHomepage);
                sortAlbums(this.comboBoxHomepage);
                refresh();
            }
            else {
                sortImages(this.comboBoxCreateAlbumWindow);
                refreshInCreateAlbum();
            }
        });


        //Gets all the words in the HBox, including duplicates
        for (int i = 0; i < this.filterWordContainer.getChildren().size(); i++)
        {
            if (this.filterWordContainer.getChildren().get(i).getClass() == newFilter.getClass() && ((Text)this.filterWordContainer.getChildren().get(i)).getText() != "")
            {
                for (String word : ((Text)this.filterWordContainer.getChildren().get(i)).getText().split(" "))
                {
                    checkList.add(word);
                }
            }
        }

        //Gets all the words for the search, excluding duplicates
        this.filterWords.clear();
        for (String check : checkList)
        {
            if (!(this.filterWords.contains(check)))
            {
                this.filterWords.add(check);
            }
        }

        if(this.filterWords.isEmpty()){
            //If no filterwords are stored, every image and album is included in the list of filtered images and -albums.
            this.gallery.setFilteredImages(this.gallery.getImages());
            this.gallery.setFilteredAlbums(this.gallery.getAlbums());
        }
        else {
            this.gallery.filterImages(this.filterWords);
            this.gallery.filterAlbums(this.filterWords);
        }

        filterInputField.clear();
        scrollPane.setContent(this.filterWordContainer);
    }

    /**
     * Method collects filterdata from the filter-textfield on the homepage
     * whenever enter is pressed in the textfield.
     * Method displays the filtered images and album in sorted order
     * @see filterImagesAndAlbumsByInput
     * @param event
     */
    public void homepageFilterInputFieldEnterPressed(Event event)
    {
        if (((KeyEvent)event).getCode() == KeyCode.ENTER) {
            filterImagesAndAlbumsByInput(this.homepageFilterInputField, this.homepageScrollFilter);
            this.gallery.setImagesAndAlbumsToShow();
            sortImages(this.comboBoxHomepage);
            sortAlbums(this.comboBoxHomepage);
            refresh();
        }
    }

    /**
     * Method collects filterdata from the filter-textfield on the homepage
     * whenever the add filter-button is clicked.
     * Method displays the filtered images and album in sorted order
     * @see filterImagesAndAlbumsByInput
     */
    public void homepageAddFilterButtonClicked(){
        filterImagesAndAlbumsByInput(this.homepageFilterInputField, this.homepageScrollFilter);
        this.gallery.setImagesAndAlbumsToShow();
        sortImages(this.comboBoxHomepage);
        sortAlbums(this.comboBoxHomepage);
        refresh();
    }

    /**
     * Method collects filterdata from the filter-textfield in the create album-window
     * whenever enter is pressed in the textfield.
     * Method displays the filtered images and album in sorted order
     * @see filterImagesAndAlbumsByInput
     * @param event
     */
    public void createAlbumWindowFilterInputFieldEnterPressed(Event event)
    {
        if (((KeyEvent)event).getCode() == KeyCode.ENTER) {
            filterImagesAndAlbumsByInput(this.createAlbumWindowFilterInputField, this.createAlbumWindowScrollFilter);
            this.gallery.setImagesAndAlbumsToShow();
            sortImages(this.comboBoxCreateAlbumWindow);
            refreshInCreateAlbum();
        }
    }

    /**
     * Method collects filterdata from the filter-textfield in the create album-window
     * whenever the add filter-button is clicked.
     * Method displays the filtered images and album in sorted order
     * @see filterImagesAndAlbumsByInput
     */
    public void createAlbumWindowAddFilterButtonClicked(){
        filterImagesAndAlbumsByInput(this.createAlbumWindowFilterInputField, this.createAlbumWindowScrollFilter);
        this.gallery.setImagesAndAlbumsToShow();
        sortImages(this.comboBoxCreateAlbumWindow);
        refreshInCreateAlbum();
    }


    /**
     * Method removes all filters added
     * @param scrollPane
     * @param filterInput
     */
    public void removeAllFilters(ScrollPane scrollPane, TextField filterInput){
        //Resets filtered images and albums
        this.gallery.setFilteredImages(this.gallery.getImages());
        this.gallery.setFilteredAlbums(this.gallery.getAlbums());

        ((HBox)scrollPane.getContent()).getChildren().clear();
        scrollPane.setContent(null);
        this.searchWords.clear();
        filterInput.clear();
    }


    /**
     * Button on homepage removes all filters when clicked and resets albums and images displayed
     * @see removeAllFilters
     */
    public void homepageRemoveAllFiltersButtonClicked()
    {
        removeAllFilters(this.homepageScrollFilter, this.homepageFilterInputField);
        this.gallery.setImagesAndAlbumsToShow();
        sortImages(this.comboBoxHomepage);
        sortAlbums(this.comboBoxHomepage);
        refresh();
    }

    /**
     * Button in the create album-window removes all filters when clicked and resets albums and images displayed
     * @see removeAllFilters
     */
    public void createAlbumWindowRemoveAllFiltersButtonClicked()
    {
        removeAllFilters(this.createAlbumWindowScrollFilter, this.createAlbumWindowFilterInputField);
        this.gallery.setImagesAndAlbumsToShow();
        sortImages(this.comboBoxCreateAlbumWindow);
        refreshInCreateAlbum();
    }


    /**
     * Method filters images within an date-interval based on the date stored in their metadata.
     * Displays error if date-input has wrong format
     * @param fromDateTextField
     * @param toDateTextField
     */
    public void filterByDate(TextField fromDateTextField, TextField toDateTextField){
        Date from = null;
        Date to = null;
        fromDateTextField.setStyle("-fx-border-color: grey");
        toDateTextField.setStyle("-fx-border-color: grey");

        this.dateTextfieldErrorMessage.hide();
        String fromDateInput = fromDateTextField.getText();
        String toDateInput = toDateTextField.getText();

        if (fromDateInput.equals("") && toDateInput.equals("")) {
            //Set filtered images to every image
            this.gallery.setFilteredByDateImages(this.gallery.getImages());
        } else {
            if (fromDateInput.equals("")) {
                fromDateInput = "0000-01-01";
            } else if (toDateInput.equals("")) {
                toDateInput = "9999-12-31";
            }

            try {
                from = Date.valueOf(fromDateInput);
                to = Date.valueOf(toDateInput);
                this.gallery.filterByDate(from, to);

            }catch(IllegalArgumentException e) {
                //If the date input has wrong format, every image is shown
                this.gallery.setFilteredByDateImages(this.gallery.getImages());

                if(from == null && to == null){
                    //Textfield fromDate has invalid input
                    fromDateTextField.clear();
                    showTextFieldsErrorMessageToolTip(this.dateTextfieldErrorMessage, fromDateTextField, -30);
                }else{
                    //Textfield toDate has invalid input
                    toDateTextField.clear();
                    showTextFieldsErrorMessageToolTip(this.dateTextfieldErrorMessage, toDateTextField, -30);
                }
            }
        }
    }

    /**
     * Method collects date-inputs from textfield on homepage when enter is pressed in either textfield.
     * Display filtered images on homepage
     * @see filterByDate
     * @param event
     */
    public void homepageGalleryViewTextFieldFilterByDateTextInput(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            filterByDate(this.fromDateHomePage, this.toDateHomepage);
            this.gallery.setImagesAndAlbumsToShow();
            sortImages(this.comboBoxHomepage);
            sortAlbums(this.comboBoxHomepage);
            refresh();
        }
    }

    /**
     * Method collects date-inputs from textfield in the create album-window when enter is pressed in either textfield.
     * Display filtered images on the create album-window
     * @see filterByDate
     * @param event
     */
    public void createAlbumTextFieldFilterByDateTextInput(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            filterByDate(this.fromDateCreateAlbum, this.toDateCreateAlbum);
            this.gallery.setImagesAndAlbumsToShow();
            sortImages(this.comboBoxCreateAlbumWindow);
            refreshInCreateAlbum();
        }
    }

    /**
     * Method sorts the images in different orders based on choice.
     * Sorts imagesToShow in the chosen order
     * @param comboBox
     */
    public void sortImages(ComboBox comboBox){
        if(comboBox.getValue().equals("Date added (newest to oldest)")) {
            this.gallery.sortImages(5);

        }else if(comboBox.getValue().equals("Date added (oldest to newest)")){
            this.gallery.sortImages(-1);

        }else if(comboBox.getValue().equals("Date taken (newest to oldest)")){
            this.gallery.sortImages(1);

        }else if(comboBox.getValue().equals("Date taken (oldest to newest)")){
            this.gallery.sortImages(2);

        }else if(comboBox.getValue().equals("Alphabetical AZ")){
            this.gallery.sortImages(3);

        }else if(comboBox.getValue().equals("Alphabetical ZA")){
            this.gallery.sortImages(4);
        }

        ArrayList<String> sortedImagesToShow = new ArrayList<>();
        for(Image i : this.gallery.getSortedImages()){
            sortedImagesToShow.add(i.getPath());
        }
        this.gallery.setImagesToShow(sortedImagesToShow);
    }


    /**
     * Method sorts the albums in different orders based on choice.
     * Sorts albumsToShow in the chosen order
     * @param comboBox
     */
    public void sortAlbums(ComboBox comboBox){
        if(comboBox.getValue().equals("Alphabetical AZ")){
            this.gallery.sortAlbums(1);

        }else if(comboBox.getValue().equals("Alphabetical ZA")){
            this.gallery.sortAlbums(2);

        }else{
            this.gallery.setSortedAlbums(this.gallery.getAlbumsToShow());
        }
        this.gallery.setAlbumsToShow(this.gallery.getSortedAlbums());
    }

    /**
     * Combobox on homepage gives user sort-alternatives
     * Method sorts the images and albums according to choice
     * @see sortImages
     * @see sortAlbums
     */
    public void homepageGalleryViewComboBoxSortBySelection(){
        sortImages(this.comboBoxHomepage);
        sortAlbums(this.comboBoxHomepage);
        refresh();
    }

    /**
     * Combobox in the create album-window gives user sort-alternatives
     * Method sorts the images according to choice
     * @see sortImages
     */
    public void createAlbumComboBoxSortBySelection(){
        sortImages(this.comboBoxCreateAlbumWindow);
        refreshInCreateAlbum();
    }
}
