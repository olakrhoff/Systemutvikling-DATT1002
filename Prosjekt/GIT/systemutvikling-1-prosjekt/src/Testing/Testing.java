package Testing;

import Backend.Gallery;
import Backend.Image;
import Backend.Tag;
import Backend.Album;

import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * The test class tests the public methods in gallery
 */
public class Testing {
    private TagDAOMock tagDAOMock = new TagDAOMock();
    private ImageDAOMock imageDAOMock = new ImageDAOMock();
    private AlbumDAOMock albumDAOMock = new AlbumDAOMock();
    private Gallery gallery = new Gallery(imageDAOMock, albumDAOMock, tagDAOMock);
    private Backend.Image imageWithNullValues = new Backend.Image("C:\\Users\\Leagr\\OneDrive\\Bilder\\chicken.png", null, null, null, null, null, "Chicken");
    private Backend.Image image1 = new Backend.Image("C:\\Users\\Leagr\\OneDrive\\Bilder\\IMG_6049.CR2", "200", "1/789 sec", "f/5.7", "300.0", Date.valueOf("2020-03-20"), "Deers");
    private Backend.Image image2 = new Backend.Image("C:\\Users\\Leagr\\OneDrive\\Bilder\\anna.jpg", "500", "1/999963864 sec", "f/1.6", "3.95", Date.valueOf("2020-03-11"), "Anna");
    private Backend.Image image3 = new Backend.Image("C:\\Users\\Leagr\\OneDrive\\Bilder\\bmxbilde.jpg", "400", "1/999963864 sec", "f/1.8", "3.95", Date.valueOf("2019-10-19"), "Bmxbilde");

    public Testing() {
    }

    /**
     * The method tests the getImage() method from gallery
     */
    @Test
    public void testGetImages() {
        //Makes a list of what the list is supposed to look like
        ArrayList<Backend.Image> images = new ArrayList<>();
        images.add(this.image1);
        images.add(this.image2);
        images.add(this.image3);
        images.add(this.imageWithNullValues);

        //Checks that the two lists are not equal before the images are added in gallery
        assert (!(images.equals(this.gallery.getImages())));

        //Uses the set method in gallery to add images to the gallery
        this.gallery.setImages(images);

        //Checks if the list equals the list in gallery
        assert (images.equals(this.gallery.getImages()));
    }

    /**
     * The method tests the getAlbums() method in gallery
     */
    @Test
    public void testGetAlbums() {
        //Creates a new list with albums
        Album album = new Album("Name", 1, 1);
        ArrayList<Album> albums = new ArrayList<>();
        albums.add(album);

        //Checks that the two lists of albums are not equal before the albums are added to the gallery
        assert (!(albums.equals(this.gallery.getAlbums())));

        //Uses the setAlbum() method from gallery to add albums to the gallery
        this.gallery.setAlbums(albums);

        //Checks that the two lists are equal
        assert (albums.equals(this.gallery.getAlbums()));

    }

    /**
     * The method tests the getImage() method from gallery
     */
    @Test
    public void testGetImagesToShow() {
        //Makes a list of what the list is supposed to look like
        ArrayList<String> images = new ArrayList<>();
        images.add(this.image1.getPath());
        images.add(this.image2.getPath());
        images.add(this.image3.getPath());
        images.add(this.imageWithNullValues.getPath());

        //Checks that the two lists are not equal before the images are added in gallery
        assert (!(images.equals(this.gallery.getImagesToShow())));

        //Uses the set method in gallery to add images to the gallery
        this.gallery.setImagesToShow(images);

        //Checks if the list equals the list in gallery
        assert (images.equals(this.gallery.getImagesToShow()));
    }

    /**
     * The method tests the getImageByPath() method in gallery
     */
    @Test
    public void testGetImageByPath() {
        //Creates a list with images
        ArrayList<Backend.Image> images = new ArrayList<>();
        images.add(this.image1);
        images.add(this.image2);
        images.add(this.image3);

        //Checks that the list in gallery is not equal to the List images
        assert (!(this.image2.equals(this.gallery.getImageByPath(this.image2.getPath()))));

        //Adds the list in gallery
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Yo");

        this.gallery.addImage(this.image2.getPath(), this.image2.getName(), tags);
        Tag tag = new Tag("Yo");
        this.image2.addTag(tag);

        //Checks that the two lists are equal
        assert (this.image2.equals(this.gallery.getImageByPath(this.image2.getPath())));
    }

    /**
     * The method tests the getAlbumByName() method in gallery
     */
    @Test
    public void testGetAlbumByName(){
        //Adds an album
        ArrayList<Image> images = new ArrayList<>();
        images.add(this.imageWithNullValues);
        this.gallery.addAlbum(1, 1, "Smile", images);

        Album album = new Album("Smile", 1, 1);
        album.addImage(this.imageWithNullValues);

        //Checks that the method returns the expected albums
        assert (album.equals(this.gallery.getAlbumByName(album.getName())));
    }

    /**
     * The method test the addImage() method from gallery
     */
    @Test
    public void testAddImage() {
        //Makes an ArrayList with the tagnames for the images
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Deer");
        tags.add("Summer");

        //Makes an List with equal to the list that is expected
        List<Backend.Image> expectedImages = new ArrayList<>();
        expectedImages.add(this.image1);
        expectedImages.add(this.imageWithNullValues);

        //Checks that the method returns false if name or path equals null
        assert (!(this.gallery.addImage(null, this.image1.getName(), tags)));
        assert (!(this.gallery.addImage(this.image1.getPath(), null, tags)));

        //Checks that the addImage() method returns true
        assert (this.gallery.addImage(this.image1.getPath(), this.image1.getName(), tags));
        assert (this.gallery.addImage(this.imageWithNullValues.getPath(), this.imageWithNullValues.getName(), tags));

        //Checks if the two lists of images are equal
        assert (this.gallery.getImages().equals(expectedImages));

        //Checks that the image is added even though the tags equals null
        assert ((this.gallery.addImage(this.image1.getPath(), this.image1.getName(), null)));
    }

    /**
     * The method tests the getGetImageById() method in gallery
     */
    @Test
    public void testGetImageById(){
        //Adds an image
        ArrayList<String> tagNames = new ArrayList<>();
        tagNames.add("YOLO");
        this.gallery.addImage(this.imageWithNullValues.getPath(), this.imageWithNullValues.getName(), tagNames);

        Image image = new Image(this.imageWithNullValues.getPath(), this.imageWithNullValues.getIso(), this.imageWithNullValues.getShutterspeed(), this.imageWithNullValues.getAperture(), this.imageWithNullValues.getFocallength(), this.imageWithNullValues.getDate(), this.imageWithNullValues.getName());
        Tag tag = new Tag("YOLO");
        image.addTag(tag);

        //Checks that the image equals the expected image
        assert (image.equals(this.gallery.getImageById(0)));
    }

    /**
     * The method tests the deleteImage() method in gallery
     */
    @Test
    public void testDeleteImage() {
        //Makes a deep copy of the list of images in galley
        List<Backend.Image> allImages = new ArrayList<>(this.gallery.getImages());

        //Arraylist with tags for image
        ArrayList<String> tagsImage = new ArrayList<>();
        tagsImage.add("Anna");
        //Adds the image
        this.gallery.addImage(this.image2.getPath(), this.image2.getName(), tagsImage);

        //Checks that the lists are not equal
        assert (!(this.gallery.getImages().equals(allImages)));
        //Checks that the deleteImage() method returns true
        assert (this.gallery.deleteImage(this.gallery.getImages().size() - 1));
        //Checks that the lists are equal after the image has been deleted
        assert (this.gallery.getImages().equals(allImages));
    }

    /**
     * The method tests the editImage() method in gallery
     */
    @Test
    public void testEditImage() {
        //ArrayList with tags for image
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Deer");
        this.gallery.addImage(this.image1.getPath(), this.image1.getName(), tags);

        //Checks that the method returns null if the image id does not exist
        assert (!(this.gallery.editImage(10,  null)));

        //Checks that the method returns true
        assert (this.gallery.editImage(0, this.image2.getName()));

        List<Backend.Image> images = this.gallery.getImages();
        //Checks that the image values equals the values the new values
        assert (images.get(0).getName().equals(this.image2.getName()));
    }

    /**
     * The method tests the addTagToImage() method in gallery
     */
    @Test
    public void testAddTagToImage() {
        //An ArrayList with the tagnames for the images
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Deer");
        //Adds an image to the gallery
        this.gallery.addImage(this.image1.getPath(), this.image1.getName(), tags);

        //Checks that the method returns false when null is used as an argument for tagname
        assert (!(this.gallery.addTagToImage(0, null)));

        //Checks that the method returns true when a tag is added
        assert (this.gallery.addTagToImage(0, "Hest"));
        assert (this.gallery.addTagToImage(0, "Mus"));

        //Makes an ArrayList with the expected tagnames
        //The ArrayList does not yet contain all the expected tagnames
        ArrayList<String> expectedTags = new ArrayList<>();
        expectedTags.add("Deer");
        expectedTags.add("Hest");

        //Gets all the tags and find all their names
        List<Tag> tagsFromImage = new ArrayList<>(this.gallery.getImages().get(0).getTags());
        ArrayList<String> tagName = new ArrayList<>();
        for (Tag t : tagsFromImage) {
            tagName.add(t.getName());
        }

        //Checks that the Lists are not equal
        assert (!(tagName.equals(expectedTags)));

        //Adds the last expected tagname
        expectedTags.add("Mus");
        //Checks that the lists are equal
        assert (tagName.equals(expectedTags));
    }

    /**
     * The method tests the removeTagFromImage() method in gallery
     */
    @Test
    public void testRemoveTagFromImage() {
        //An ArrayList with the tagnames for the image
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Deer");
        tags.add("Hest");
        tags.add("Mus");
        this.gallery.addImage(this.image1.getPath(), this.image1.getName(), tags);

        //Checks that the method returns true
        assert (this.gallery.removeTagFromImage(0, "Hest"));

        //Creates an ArrayList with the exopected tags
        ArrayList<String> expectedTags = new ArrayList<>();
        expectedTags.add("Deer");
        expectedTags.add("Mus");

        //Gets all the tagnames that are registered to the image in gallery
        List<Tag> tagsFromImage = new ArrayList<>(this.gallery.getImages().get(0).getTags());
        ArrayList<String> tagName = new ArrayList<>();
        for (Tag t : tagsFromImage) {
            tagName.add(t.getName());
        }

        //Checks that the lists are equal
        assert (tagName.equals(expectedTags));
    }

    /**
     * The method tests the addAlbum() methos in gallery
     */
    @Test
    public void testAddAlbum() {
        //Adds an album in gallery
        ArrayList<Backend.Image> imagesAlbum = new ArrayList<>();
        imagesAlbum.add(this.image1);
        imagesAlbum.add(this.image2);
        imagesAlbum.add(this.image3);
        //Adds a null image to make sure the program does not register this image
        imagesAlbum.add(null);
        //Checks if the method returns true
        assert (this.gallery.addAlbum(3, 3, "Tur", imagesAlbum));

        //Finds the newly added album
        Album newAlbum = this.gallery.getAlbums().get(0);

        //Creates an album that is equal to what the newly added album is supposed to be
        //The album does not yet contain all the images that the newly added album does
        Album expectedAlbum = new Album("Tur", 3, 3);
        expectedAlbum.setId(0);
        expectedAlbum.addImage(this.image1);
        expectedAlbum.addImage(this.image2);

        //Checks that the albums are not equal
        assert (!expectedAlbum.equals(newAlbum));

        //Adds the last image
        expectedAlbum.addImage(this.image3);

        //Checks that the albums are equal
        assert (expectedAlbum.equals(newAlbum));
    }

    /**
     * The method tests the deleteAlbum() method in gallery
     */
    @Test
    public void testDeleteAlbum() {
        //Makes a deep copy of the list with the albums in the gallery
        List<Album> albums = new ArrayList<>(this.gallery.getAlbums());

        //Adds a new album
        ArrayList<Backend.Image> imagesAlbum = new ArrayList<>();
        imagesAlbum.add(this.image1);
        imagesAlbum.add(this.image2);
        imagesAlbum.add(this.image3);
        this.gallery.addAlbum(3, 3, "Tur", imagesAlbum);

        //Checks that the lists are not equal
        assert (!(this.gallery.getAlbums().equals(albums)));

        //Checks if the method returns true when the album is deleted
        assert (this.gallery.deleteAlbum(this.gallery.getAlbums().get(0)));

        //Checks that the two lists are equal
        assert (gallery.getAlbums().equals(albums));

    }

    /**
     * The method tests the createPdfFromAlbum() method and displays the created pdf
     *
     * @throws IOException throws exception if the file is not readable
     */
    @Test
    public void testCreatePdfFromAlbum() throws IOException {
        //Creates an album
        ArrayList<Backend.Image> imagesForAlbum = new ArrayList<>();
        imagesForAlbum.add(this.image3);
        imagesForAlbum.add(this.image2);
        imagesForAlbum.add(this.image3);
        this.gallery.addAlbum(2, 2, "Anna", imagesForAlbum);


        //get the pdf name from the createPdfFromAlbum method
        String pdf = this.gallery.createPdfFromAlbum(0);

        //Checks that the name of the pdf equals the name of the album pluss .pfd
        assert (pdf.equals("Anna2203.pdf"));

        //Opens the file
        Desktop.getDesktop().open(new File(pdf));
    }

    /**
     * The method tests the editAlbum() method in gallery
     */
    @Test
    public void testEditAlbum(){
        //Creates an album
        ArrayList<Backend.Image> imagesForAlbum = new ArrayList<>();
        imagesForAlbum.add(this.image3);
        imagesForAlbum.add(this.image2);
        imagesForAlbum.add(this.image3);
        this.gallery.addAlbum(2, 2, "Anna", imagesForAlbum);

        //Creates an album equal to the one expected to get after editing the album
        Album newAlbum = new Album("Bmx", 3, 3);
        newAlbum.setId(0);
        newAlbum.addImage(this.image3);
        newAlbum.addImage(this.image2);
        newAlbum.addImage(this.image3);
        ArrayList<Album> expectedAlbums = new ArrayList<>();
        expectedAlbums.add(newAlbum);

        //Checks that the method returns true
        assert (gallery.editAlbum(0, 3, 3, "Bmx"));

        ArrayList<Album> albums = new ArrayList<>(gallery.getAlbums());

        //Checks that the two lists are equal
        assert (albums.equals(expectedAlbums));
    }

    /**
     * The method tests the sortImages() method in gallery
     */
    @Test
    public void testSortImages() {
        //Adds images to the gallery
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Fun");
        this.gallery.addImage(this.image3.getPath(), this.image3.getName(), tags);
        this.gallery.addImage(this.image2.getPath(), this.image2.getName(), tags);
        this.gallery.addImage(this.imageWithNullValues.getPath(), this.imageWithNullValues.getName(), tags);
        this.gallery.addImage(this.image1.getPath(), this.image1.getName(), tags);

        //Creates a list with the expected order
        //The newest image is added first
        ArrayList<Image> imagesNewestExpected = new ArrayList<>();
        imagesNewestExpected.add(this.image1);
        imagesNewestExpected.add(this.image2);
        imagesNewestExpected.add(this.image3);
        imagesNewestExpected.add(this.imageWithNullValues);

        //Sorts the images in gallery by the newest date
        this.gallery.sortImages(1);
        ArrayList<Image> imagesNewest = new ArrayList<>(this.gallery.getSortedImages());

        //Checks if the lists are equal
        assert (imagesNewestExpected.equals(imagesNewest));

        //Creates a list with the expected order
        //The oldest image is added first
        List<Image> imagesOldestExpected = new ArrayList<>();
        imagesOldestExpected.add(this.image3);
        imagesOldestExpected.add(this.image2);
        imagesOldestExpected.add(this.image1);
        imagesOldestExpected.add(this.imageWithNullValues);

        //Sorts the images in the gallery by the oldest date
        this.gallery.sortImages(2);
        ArrayList<Image> imagesOldest = new ArrayList<>(this.gallery.getSortedImages());

        //Checks id the lists are equal
        assert (imagesOldestExpected.equals(imagesOldest));

        //Creates a list with the expected order
        //The images are added from A to Z by their name
        List<Image> imagesAlphabeticAZExpected = new ArrayList<>();
        imagesAlphabeticAZExpected.add(this.image2);
        imagesAlphabeticAZExpected.add(this.image3);
        imagesAlphabeticAZExpected.add(this.imageWithNullValues);
        imagesAlphabeticAZExpected.add(this.image1);

        //Sorts the images in the gallery from A to Z by their name
        this.gallery.sortImages(3);
        ArrayList<Image> imagesAlphabeticalAZ = new ArrayList<>(this.gallery.getSortedImages());

        //Checks if the lists are equal
        assert (imagesAlphabeticAZExpected.equals(imagesAlphabeticalAZ));

        //Creates a list with the expected order
        //The images are added from Z to A by their name
        List<Image> imagesAlphabeticalZAExpected = new ArrayList<>();
        imagesAlphabeticalZAExpected.add(this.image1);
        imagesAlphabeticalZAExpected.add(this.imageWithNullValues);
        imagesAlphabeticalZAExpected.add(this.image3);
        imagesAlphabeticalZAExpected.add(this.image2);

        //Sorts the images in the gallery from Z to A by their name
        this.gallery.sortImages(4);
        ArrayList<Image> imagesAlphabeticalZA = new ArrayList<>(this.gallery.getSortedImages());

        //Checks if the lists are equal
        assert (imagesAlphabeticalZAExpected.equals(imagesAlphabeticalZA));
    }

    /**
     * The method tests the sortAlbums() method in gallery
     */
    @Test
    public void testSortAlbums(){
        //Adds albums to the gallery
        ArrayList<Image> imagesForAlbums = new ArrayList<>();
        imagesForAlbums.add(this.image2);
        this.gallery.addAlbum(1, 1, "B album", imagesForAlbums);
        this.gallery.addAlbum(2,2,"A album", imagesForAlbums);

        //Makes albums for comparison
        Album bAlbum = new Album("B album", 1, 1);
        bAlbum.addImage(this.image2);
        bAlbum.setId(0);
        Album aAlbum = new Album("A album", 2, 2);
        aAlbum.addImage(this.image2);
        aAlbum.setId(1);

        //Makes ArrayList equal to the ones expected from the method
        ArrayList<Album> expectedOrderAZ = new ArrayList<>();
        expectedOrderAZ.add(aAlbum);
        expectedOrderAZ.add(bAlbum);

        ArrayList<Album> expectedOrderZA = new ArrayList<>();
        expectedOrderZA.add(bAlbum);
        expectedOrderZA.add(aAlbum);

        //Gets the ArrayLists from the method
        this.gallery.sortAlbums(1);
        ArrayList<Album> orderAZ = new ArrayList<>(this.gallery.getSortedAlbums());

        this.gallery.sortAlbums(2);
        ArrayList<Album> orderZA = new ArrayList<>(this.gallery.getSortedAlbums());

        //Checks that order from ZA are not equal to expected AZ
        assert (!(expectedOrderAZ.equals(orderZA)));
        //Checks that order from AZ are equal to expected AZ
        assert (expectedOrderAZ.equals(orderAZ));

        //Checks that order from AZ are not equal to expected ZA
        assert (!(expectedOrderZA.equals(orderAZ)));
        //Checks that order from ZA are equal to expected ZA
        assert (expectedOrderZA.equals(orderZA));
    }

    /**
     * The method tests the filterByDate() method in gallery
     */
    @Test
    public void testFilterByDate() {
        //Adds images to the gallery
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Fun");
        this.gallery.addImage(this.image2.getPath(), this.image2.getName(), tags);
        this.gallery.addImage(this.image3.getPath(), this.image3.getName(), tags);
        this.gallery.addImage(this.image1.getPath(), this.image1.getName(), tags);
        this.gallery.addImage(this.imageWithNullValues.getPath(), this.imageWithNullValues.getName(), tags);

        //Makes a list with the expected result
        ArrayList<Image> imagesFilteredExpected = new ArrayList<>();
        imagesFilteredExpected.add(this.image1);

        this.gallery.filterByDate(Date.valueOf("2020-03-19"), Date.valueOf("2020-03-30"));
        ArrayList<Image> imagesFiltered = new ArrayList<>(this.gallery.getFilteredByDateImages());
        //Checks if the lists are equal
        assert (imagesFilteredExpected.equals(imagesFiltered));

        this.gallery.filterByDate(Date.valueOf("2000-03-19"), Date.valueOf("2020-03-30"));
        ArrayList<Image> imagesFiltered2 = new ArrayList<>(this.gallery.getFilteredByDateImages());
        //Checks that the lists are not equal
        assert (!(imagesFilteredExpected.equals(imagesFiltered2)));
    }

    /**
     * The method tests the SearchAlbums() method in gallery
     */
    @Test
    public void testFindAlbumsByName(){
        //Adds albums to the gallery
        ArrayList<Image> imagesForAlbums = new ArrayList<>();
        imagesForAlbums.add(this.image2);
        this.gallery.addAlbum(1, 1, "Cabin", imagesForAlbums);
        this.gallery.addAlbum(3, 3, "Cabinet", imagesForAlbums);
        this.gallery.addAlbum(2,2,"Moose", imagesForAlbums);

        //Makes albums for comparison
        Album cabin= new Album("Cabin", 1, 1);
        cabin.addImage(this.image2);
        cabin.setId(0);
        Album cabinet = new Album("Cabinet", 3, 3);
        cabinet.addImage(this.image2);
        cabinet.setId(1);

        //Makes an ArrayList equal to the one expected to get from the method
        ArrayList<Album> expectedArrayList = new ArrayList<>();
        expectedArrayList.add(cabin);
        expectedArrayList.add(cabinet);

        ArrayList<String> searchWords = new ArrayList<>();
        searchWords.add("ca");
        searchWords.add("biN");

        this.gallery.searchAlbums(searchWords);
        ArrayList<Album> listAfterSearch = new ArrayList<>(this.gallery.getSearchedAlbums());

        //Checks that the two lists are equal
        assert (expectedArrayList.equals(listAfterSearch));
    }

    /**
     * The method tests the searchImagesByMetadata() method in gallery
     */
    @Test
    public void testFindImagesByMetadata() {
        //Adds images to the gallery
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Fun");
        this.gallery.addImage(this.image2.getPath(), this.image2.getName(), tags);
        this.gallery.addImage(this.image3.getPath(), this.image3.getName(), tags);
        this.gallery.addImage(this.image1.getPath(), this.image1.getName(), tags);

        this.gallery.addTagToImage(0, "Jippi");

        //Makes an ArrayList with the search words
        ArrayList<String> searchWords = new ArrayList<>();
        searchWords.add("fun");
        searchWords.add("ANNA");
        searchWords.add("50");

        this.gallery.searchImages(searchWords);
        ArrayList<Image> imagesAfterSearch = new ArrayList<>(this.gallery.getSearchedImages());

        //Makes an ArrayList with the expected results
        ArrayList<Image> expectedAfterSearch = new ArrayList<>();
        expectedAfterSearch.add(this.image2);

        //Checks if the lists are equal
        assert (expectedAfterSearch.equals(imagesAfterSearch));
    }

    @Override
    public String toString() {
        return "Testing{" +
                "tagDAOMock=" + this.tagDAOMock +
                ", imageDAOMock=" + this.imageDAOMock +
                ", albumDAOMock=" + this.albumDAOMock +
                ", gallery=" + this.gallery +
                ", imagesWithNullValues=" + this.imageWithNullValues +
                ", image1=" + this.image1 +
                ", image2=" + this.image2 +
                ", image3=" + this.image3 +
                '}';
    }
}
