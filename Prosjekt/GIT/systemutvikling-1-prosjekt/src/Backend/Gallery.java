package Backend;

import Testing.AlbumDAOMock;
import com.google.common.base.Preconditions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.image.ImageParser;
import org.apache.tika.parser.image.TiffParser;
import org.apache.tika.parser.jpeg.JpegParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class Gallery {

    private ArrayList<Backend.Image> images;
    private ArrayList<Album> albums;
    private IImageDAO imageDAO;
    private IAlbumDAO albumDAO;
    private ITagDAO tagDAO;
    private ArrayList<String> imagesToShow;
    private ArrayList<Album> albumsToShow;

    private ArrayList<Image> filteredImages;
    private ArrayList<Album> filteredAlbums;
    private ArrayList<Image> filteredByDateImages;
    private ArrayList<Image> searchedImages;
    private ArrayList<Album> searchedAlbums;

    private ArrayList<Image> sortedImages;
    private ArrayList<Album> sortedAlbums;


    /**
     * @param imageDAO an interface of IImageDAO
     * @param albumDAO an interface of IAlbumDAO
     * @param tagDAO   an interface of ITagDAO
     */
    public Gallery(IImageDAO imageDAO, IAlbumDAO albumDAO, ITagDAO tagDAO) {
        this.imageDAO = imageDAO;
        this.albumDAO = albumDAO;
        this.tagDAO = tagDAO;
        this.images = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.imagesToShow = new ArrayList<>();
        this.albumsToShow = new ArrayList<>();
        this.filteredImages = new ArrayList<>();
        this.filteredAlbums = new ArrayList<>();
        this.filteredByDateImages = new ArrayList<>();
        this.sortedImages = new ArrayList<>();
        this.sortedAlbums = new ArrayList<>();
        this.searchedImages = new ArrayList<>();
        this.searchedAlbums = new ArrayList<>();

        fillImages();
        fillAlbums();

        //Every image and album is to be display when program starts
        for (Backend.Image image : this.images) {
            this.imagesToShow.add(image.getPath());
        }
        this.albumsToShow.addAll(this.albums);

        this.filteredImages.addAll(this.images);
        this.filteredAlbums.addAll(this.albums);
        this.filteredByDateImages.addAll(this.images);
        this.sortedImages.addAll(this.images);
        this.sortedAlbums.addAll(this.albums);
        this.searchedImages.addAll(this.images);
        this.searchedAlbums.addAll(this.albums);
    }

    /**
     * Method make sure the ArrayList of images is up to date with the database by getting
     * all the images from the database and storing them in the ArrayList of images.
     */
    private void fillImages() {
        setImages(this.imageDAO.getAllImages());
    }

    /**
     * Method make sure the ArrayList of albums is up to date with the database by getting
     * all the albums from the database and storing them in the ArrayList of albums.
     */
    private void fillAlbums() {
        this.setAlbums(this.albumDAO.getAllAlbums());

    }

    /**
     * @return the ArrayList images
     */
    public ArrayList<Backend.Image> getImages() {
        return this.images;
    }

    /**
     * @return the ArrayList albums
     */
    public ArrayList<Album> getAlbums() {
        return this.albums;
    }

    /**
     * @param images an ArrayList of images that will replace the existing images ArrayList
     */
    public void setImages(ArrayList<Backend.Image> images) {
        this.images = images;
    }

    /**
     * @param albums an ArrayList of albums that will replace the existing albums ArrayList
     */
    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    /**
     * @return the ArrayList imagesToShow
     */
    public ArrayList<String> getImagesToShow() {
        return imagesToShow;
    }

    /**
     * @param imagesToShow an ArrayList of String that will replace the imagesToShow ArrayList
     */
    public void setImagesToShow(ArrayList<String> imagesToShow) {
        this.imagesToShow = imagesToShow;
    }

    /**
     * @return the ArrayList albumsToShow
     */
    public ArrayList<Album> getAlbumsToShow() {
        return albumsToShow;
    }

    /**
     * @param albumsToShow an ArrayList of albums that will replace the albumsToShow ArrayList
     */
    public void setAlbumsToShow(ArrayList<Album> albumsToShow) {
        this.albumsToShow = albumsToShow;
    }

    /**
     * @return the ArrayList filteredImages
     */
    public ArrayList<Image> getFilteredImages(){ return this.filteredImages; }

    /**
     * @param filteredImages an ArrayList of images that will replace the filteredImages ArrayList
     */
    public void setFilteredImages(ArrayList<Image> filteredImages) { this.filteredImages = filteredImages; }

    /**
     * @return the ArrayList filteredAlbums
     */
    public ArrayList<Album> getFilteredAlbums(){ return this.filteredAlbums; }

    /**
     * @param filteredAlbums an ArrayList of albums that will replace the filteredAlbums ArrayList
     */
    public void setFilteredAlbums(ArrayList<Album> filteredAlbums) { this.filteredAlbums = filteredAlbums; }

    /**
     * @return the ArrayList sortedImages
     */
    public ArrayList<Image> getSortedImages() { return sortedImages; }

    /**
     * @param sortedImages an ArrayList of images that will replace the sortedImages ArrayList
     */
    public void setSortedImages(ArrayList<Image> sortedImages) {
        this.sortedImages = sortedImages;
    }

    /**
     * @return the ArrayList sortedAlbums
     */
    public ArrayList<Album> getSortedAlbums() {
        return sortedAlbums;
    }

    /**
     * @param sortedAlbums an ArrayList of albums that will replace the sortedAlbums ArrayList
     */
    public void setSortedAlbums(ArrayList<Album> sortedAlbums) {
        this.sortedAlbums = sortedAlbums;
    }

    /**
     * @return the ArrayList filteredByDateImages
     */
    public ArrayList<Image> getFilteredByDateImages() {
        return filteredByDateImages;
    }

    /**
     * @param filteredByDateImages an ArrayList of images that will replace the filteredByDateImages ArrayList
     */
    public void setFilteredByDateImages(ArrayList<Image> filteredByDateImages) {
        this.filteredByDateImages = filteredByDateImages;
    }

    /**
     * @return the ArrayList searchedImages
     */
    public ArrayList<Image> getSearchedImages() {
        return searchedImages;
    }

    /**
     * @param searchedImages an ArrayList of images that will replace the searchedImages ArrayList
     */
    public void setSearchedImages(ArrayList<Image> searchedImages) {
        this.searchedImages = searchedImages;
    }

    /**
     * @return the ArrayList searchedAlbums
     */
    public ArrayList<Album> getSearchedAlbums() {
        return searchedAlbums;
    }

    /**
     * @param searchedAlbums an ArrayList of albums that will replace the searchedAlbums ArrayList
     */
    public void setSearchedAlbums(ArrayList<Album> searchedAlbums) {
        this.searchedAlbums = searchedAlbums;
    }

    /**
     * @param path the path of the image wanting to be stored
     * @return the image with the path. If there is no image with the path registered, the return value is null
     */
    public Backend.Image getImageByPath(String path) {
        return imageDAO.getImageByPath(path);
    }

    public Album getAlbumByName(String name){
        return albumDAO.getAlbumByName(name);
    }

    /**
     * Method stores the image from user input into the database and adds it to the galleryView by updating
     * the ArrayList of images.
     *
     * @param path The path of the image wanting to be stored
     * @param name The name of the image wanting to be stored
     * @return true if the image given by the parameter were successfully stored in the database, and false if it were not.
     */
    public boolean addImage(String path, String name, ArrayList<String> tagsForImage) {
        ArrayList<String> updatedImagesToShow = new ArrayList<>();
        try {
            //Returns false is any of either the path or the name is null
            if (name == null || path == null) {
                return false;
            }

            //Extracts the metadata
            Map<String, String> metadata = extractMetadata(path);

            Date dateImage = null;

            //Rewrites the date string to a date object if the value exists
            if (metadata.get("Exif IFD0:Date/Time") != null) {
                String dateString = metadata.get("Exif IFD0:Date/Time");
                String[] info = dateString.split("[: ]");

                String time = info[0] + "-" + info[1] + "-" + info[2];
                dateImage = Date.valueOf(time);
            }

            //Stores the new image
            Backend.Image image = new Backend.Image(path, metadata.get("exif:IsoSpeedRatings"), metadata.get("Exif SubIFD:Shutter Speed Value"), metadata.get("Exif SubIFD:Aperture Value"), metadata.get("exif:FocalLength"), dateImage, name);
            if (!(this.imageDAO.storeNewImage(image))) {
                return false;
            }

            //Fills the ArrayList images so the newly added image is added to the ArrayList
            fillImages();
            //Updates the imagesToShow
            for (Backend.Image i : images) {
                updatedImagesToShow.add(i.getPath());
            }
            setImagesToShow(updatedImagesToShow);

            //Adds the tags to the image
            if (!(tagsForImage == null)) {
                int imageId = image.getId();
                for (String t : tagsForImage) {
                    if (!addTagToImage(imageId, t)) {
                        imageDAO.deleteImage(imageId);
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Image getImageById(int id){
        return imageDAO.findImage(id);
    }

    /**
     * This method extracts the metadata from an image
     *
     * @param path the path to the image
     * @return a Map containing the dataname as key and the value of the metadata as value
     */
    private Map<String, String> extractMetadata(String path) {
        //Makes a new ArrayList with all the names of the metadata that is to be found
        ArrayList<String> metaNames = new ArrayList<String>();
        metaNames.add("exif:FocalLength");
        metaNames.add("Exif SubIFD:Shutter Speed Value");
        metaNames.add("Exif IFD0:Date/Time");
        metaNames.add("exif:IsoSpeedRatings");
        metaNames.add("Exif SubIFD:Aperture Value");

        //returns a Map with all the metadata from the private method getImageMetaData
        return getImageMetadata(path, metaNames);
    }

    /**
     * Finds all the metadata for extractMetadata
     *
     * @param fileName  the path to the image
     * @param metaNames the names of all the wanted metadata
     * @return a Map with the all the metadata values
     */
    private Map<String, String> getImageMetadata(String fileName, ArrayList<String> metaNames) {

        //Creates a new map
        Map<String, String> imageMetadata = new Hashtable<>();

        try {
            //Creates a new file with the path of the image
            File file = new File(fileName);

            //Tika is from an external library
            Tika tika = new Tika();
            final Metadata metadata = new Metadata();
            final ContentHandler handler = new DefaultHandler();
            //The parser will be made specifically for each type of image
            final Parser parser;
            //Splits the path where it finds "."
            String[] splitPath = fileName.split("\\.");
            //Finds the last element in the Array and checks what image type the image is
            if (splitPath[splitPath.length - 1].equals("jpg") || splitPath[splitPath.length - 1].equals("jpeg")) {
                parser = new JpegParser();
            } else if (splitPath[splitPath.length - 1].equals("CR2")) {
                parser = new TiffParser();
            } else if (splitPath[splitPath.length - 1].equals("png") || splitPath[splitPath.length - 1].equals("gif") || splitPath[splitPath.length - 1].equals("bmp")) {
                parser = new ImageParser();
            } else {
                return null;
            }
            final ParseContext context = new ParseContext();
            final String mimeType;
            //Finds the metadata
            try (InputStream stream = Preconditions.checkNotNull(IOUtils.toBufferedInputStream(FileUtils.openInputStream(file)), "Cannot open file '%s'", fileName)) {
                mimeType = tika.detect(stream);
                metadata.set(Metadata.CONTENT_TYPE, mimeType);
            }
            //parses the image
            try (InputStream stream = Preconditions.checkNotNull(IOUtils.toBufferedInputStream(FileUtils.openInputStream(file)), "Cannot open file '%s'", fileName)) {
                parser.parse(stream, handler, metadata, context);
            }
            //loops through all the found metadata
            for (final String name : metadata.names()) {
                //If any of the metadata matches the ArrayList of the metadata names, the metadata will be added to the Map
                if (metaNames.contains(name)) {
                    imageMetadata.put(name, metadata.get(name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageMetadata;
    }

    /**
     * The method gets a preview of the metadata.
     * Used to show the image(s) metadata when the image(s)
     * is/are added.
     * @param path  path to the image that the metadata is extracted from
     * @return      ArrayList<String>, all the metadata that was extracted
     */
    public ArrayList<String> getMetadataPreview(String path) {
        ArrayList<String> data = new ArrayList<>();
        Map<String, String> metadata = extractMetadata(path);
        data.add(metadata.get("exif:IsoSpeedRatings"));
        data.add(metadata.get("Exif SubIFD:Shutter Speed Value"));
        data.add(metadata.get("Exif SubIFD:Aperture Value"));
        data.add(metadata.get("exif:FocalLength"));
        data.add(metadata.get("Exif IFD0:Date/Time"));
        return data;
    }

    /**
     * Deletes a chosen image
     *
     * @param id the id of the chosen image
     * @return true if the image is deleted successfully and false if the image is not deleted
     */
    public boolean deleteImage(int id) {
        try {
            if (imageDAO.deleteImage(id)) {
                fillImages();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method edits an exciting image
     *
     * @param imageId         the id of the image
     * @param newName         the new name
     * @return true if the image is successfully edited and false if it failed
     */
    public boolean editImage(int imageId, String newName) {
        try {
            if (imageDAO.editImage(imageId, newName)) {
                fillImages();
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a tag to an image
     *
     * @param imageId the id of the image
     * @param tagName the name of the tag
     * @return true if the tag was successfully added and false if it failed
     */
    public boolean addTagToImage(int imageId, String tagName) {
        try {
            if (tagName == null) {
                return false;
            }
            //Reformat tag
            char[] tagsplit = tagName.toCharArray();
            String tagName2 = Character.toString(Character.toUpperCase(tagsplit[0]));
            for(int i = 1; i < tagsplit.length; i++){
                tagName2 += Character.toLowerCase(tagsplit[i]);
            }
            //Checks if the tag already exists and adds that tag if found
            List<Tag> allTags = tagDAO.getAllTags();
            for (Tag t : allTags) {
                if (t.getName().equals(tagName2)) {
                    if (imageDAO.addTagToImage(t, imageId)) {
                        fillImages();
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            //Creates a new tag and stores it if the tag does not already exist
            Tag tag = new Tag(tagName2);
            if (!tagDAO.storeNewTag(tag)) {
                return false;
            }
            //Adds the newly added tag
            if (imageDAO.addTagToImage(tag, imageId)) {
                fillImages();
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The method removes a tag from an image
     *
     * @param imageId the id of the image that the user wants to remove a tag from
     * @param tagName the name of the tag that is to be removed
     * @return true if the tag is successfully removed and false if it fails
     */
    public boolean removeTagFromImage(int imageId, String tagName) {
        try {
            Tag tagRemoved = tagDAO.getTagByName(tagName);

            if(imageDAO.removeTagFromImage(tagRemoved, imageId)){
                fillImages();
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The method adds an album
     *
     * @param imagesPerRow   images per row for the pdf
     * @param pages          pages for the pdf
     * @param name           name of the album
     * @param imagesForAlbum ArrayList with the images for the album
     * @return true if the album is added successfully and false is the process failed
     */
    public boolean addAlbum(int imagesPerRow, int pages, String name, ArrayList<Backend.Image> imagesForAlbum) {
        try {
            if (imagesPerRow <= 0 || pages <= 0 || name == null) {
                return false;
            }
            Album newAlbum = new Album(name, imagesPerRow, pages);
            if(!(this.albumDAO.storeNewAlbum(newAlbum))){
                return false;
            }

            if (albumDAO instanceof AlbumDAOMock) {
                int idAlbum = albumDAO.getAllAlbums().size() - 1;
                newAlbum.setId(idAlbum);
            }
            if (imagesForAlbum == null) {
                this.fillAlbums();
                return true;
            }
            for (Backend.Image i : imagesForAlbum) { //Noe her???
                if (i == null) {
                    break;
                } else {
                    if (!(this.albumDAO.addImageToAlbum(i, newAlbum.getId()))) {
                        this.albumDAO.deleteAlbum(newAlbum.getId());
                        return false;
                    }
                }
            }
            this.fillAlbums();
            ArrayList<Album> updatedAlbumsToShow = new ArrayList<>(albums);
            setAlbumsToShow(updatedAlbumsToShow);
            return true;
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return false;
    }

    /**
     * Method for deleting an album
     *
     * @param album the album that is wanted deleted
     * @return      true if the album is deleted successfully and false if the process failed
     */

    public boolean deleteAlbum(Album album) {
        try {
            if (albumDAO.deleteAlbum(album.getId())) {
                fillAlbums();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param id id of the album that will be created a pdf of
     * @return   the name of the pdf
     */
    public String createPdfFromAlbum(int id) {
        //Finds the album
        Album newAlbum = this.albumDAO.findAlbum(id);
        if (newAlbum == null) {
            return null;
        }

        //Creates a pdf name
        String name = newAlbum.getName();
        String namePdf = newAlbum.getName() + newAlbum.getImagesPerRow() + newAlbum.getPages() + newAlbum.getId() + newAlbum.getImages().size() + ".pdf";
        Document document = new Document();
        try {
            //Makes a pdf file
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(namePdf));
            document.open();
            //Adds the name of the album
            document.add(new Paragraph(name));

            //Makes the size for the images
            float size = (document.getPageSize().getWidth() / newAlbum.getImagesPerRow()) - document.leftMargin();

            //Adds all the scaled images to a ArrayList with all the images for the PDF
            ArrayList<com.itextpdf.text.Image> imagesForPDF = new ArrayList<>();
            for (int i = 0; i < newAlbum.getImages().size(); i++) {
                com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(newAlbum.getImages().get(i).getPath());
                img.scaleAbsolute(size, size);
                imagesForPDF.add(img);
            }

            //Finds the withs for the cells in the table
            int[] withs = new int[newAlbum.getImagesPerRow()];
            Arrays.fill(withs, (int) size);

            int currentImage = 0;
            int loop = newAlbum.getImages().size() / newAlbum.getPages();
            if(newAlbum.getImages().size() % newAlbum.getPages() > 0){
                loop++;
            }
            for(int i = 0; i < loop; i++){
                //Creates a table
                PdfPTable table = new PdfPTable(newAlbum.getImagesPerRow());
                table.setWidths(withs);

                int count = 0;
                for(int j = count; j < newAlbum.getPages(); j++){
                    table.addCell(new PdfPCell(imagesForPDF.get(currentImage), true));
                    count++;
                    currentImage++;
                    if(currentImage == imagesForPDF.size()){
                        break;
                    }
                }

                table.completeRow();

                //Adds the table
                document.add(table);

                document.newPage();
            }

            //Finishes the PDF
            document.close();
            writer.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return namePdf;
    }

    /**
     * Method edits an album from the database and store the new values passed in the method
     * @param albumId         the id of the album that is to be edited
     * @param newImagePerRow  the new value of imagePerRow
     * @param newImagePerPage the new value of imagePerPage
     * @param newName         the new name of the album
     * @return true if the album was successfully edited and false if it were not
     */
    public boolean editAlbum(int albumId, int newImagePerRow, int newImagePerPage, String newName) {
        try {
            if (albumDAO.editAlbum(albumId, newImagePerRow, newImagePerPage, newName)){
                fillImages();
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method sorts the images by newest, oldest or alphabetical from A to Z or Z to A
     *
     * @param sortNumber this integer will decide how the ArrayList will be sorted
     */
    public void sortImages(int sortNumber) {
        this.sortedImages.clear();
        //Makes sure the gallery is up to date with the database
        this.fillImages();
        //Sorts only the images in imagestoShow
        ArrayList<Backend.Image> allImages = new ArrayList<>();
        for(String i : imagesToShow){
            allImages.add(getImageByPath(i));
        }
        ArrayList<Backend.Image> imagesWithNullDate = new ArrayList<>();

        //Sort by newest date
        if (sortNumber == 1) {
            for (Backend.Image i : allImages) {
                //Filters out the images with null date
                if (i.getDate() == null) {
                    imagesWithNullDate.add(i);
                }
            }
            for (Backend.Image i : imagesWithNullDate) {
                allImages.remove(i);
            }
            allImages.sort(new SortByNewestDate());
            //Adds all the images with null date at the end of the list
            allImages.addAll(imagesWithNullDate);
        }
        //Sort by oldest date
        else if (sortNumber == 2) {
            for (Backend.Image i : allImages) {
                //Filters out the images with null date
                if (i.getDate() == null) {
                    imagesWithNullDate.add(i);
                }
            }
            for (Backend.Image i : imagesWithNullDate) {
                allImages.remove(i);
            }
            allImages.sort(new SortByOldestDate());
            //Adds all the images with null date at the end of the list
            allImages.addAll(imagesWithNullDate);
        }
        //Sort alphabetical from A to Z
        else if (sortNumber == 3) {
            allImages.sort(new SortAlphabeticalAZ());
        }
        //Sort alphabetical from Z to A
        else if (sortNumber == 4) {
            allImages.sort(new SortAlphabeticalZA());
        }
        //Sort by date added (newest to oldest)
        else if (sortNumber == 5) {
            ArrayList<Backend.Image> reverseAllImages = new ArrayList<>();
            for (int i = allImages.size() - 1; i >= 0; i--) {
                reverseAllImages.add(allImages.get(i));
            }
            allImages = reverseAllImages;
        }
        this.sortedImages.addAll(allImages);
    }

    /**
     * This method sorts the albums by alphabetical from A to Z or Z to A
     *
     * @param sortNumber this integer will decide how the ArrayList will be sorted
     */
    public void sortAlbums(int sortNumber){
        this.sortedAlbums.clear();
        //Makes sure the gallery is up to date with the database
        this.fillAlbums();
        ArrayList<Album> sorted = new ArrayList<>(this.albumsToShow);
        if (sortNumber == 1) {
            sorted.sort(new SortAlbumByNameAZ());
        } else if (sortNumber == 2) {
            sorted.sort(new SortAlbumByNameZA());
        }

        this.sortedAlbums.addAll(sorted);
    }


    /**
     * This method filters the image taken within two dates
     *
     * @param date1 the "from" date
     * @param date2 the "to" date
     * @return      an ArrayList with the paths to the images taken within date1 and date2
     */

    public void filterByDate(Date date1, Date date2) {
        this.filteredByDateImages.clear();
        //Makes sure the ArrayList images is updated
        this.fillImages();
        ArrayList<Backend.Image> allImages = new ArrayList<>(this.images);
        ArrayList<Backend.Image> imageWithNullDate = new ArrayList<>();
        ArrayList<Backend.Image> imagesForRemoval = new ArrayList<>();

        //Removes the images with null date
        for (Backend.Image i : allImages) {
            if (i.getDate() == null) {
                imageWithNullDate.add(i);
            }
        }
        for (Backend.Image i : imageWithNullDate) {
            allImages.remove(i);
        }

        //If the "from" date is bigger than the "to" date the method will return null
        if (date1.getTime() > date2.getTime()) {
            setFilteredByDateImages(null);
        }

        else {
            for (Backend.Image i : allImages) {
                //checks if the image is taken before the first date
                //if the image is taken before the first date, the image will be added to the remove ArrayList
                if (date1.getTime() - i.getDate().getTime() > 0) {
                    imagesForRemoval.add(i);

                }
                //checks if the image is taken after the last date
                //if the image is taken after the last date, the image will be added to the remove ArrayList
                else if (date2.getTime() - i.getDate().getTime() < 0) {
                    imagesForRemoval.add(i);
                }
            }

            for (Backend.Image i : imagesForRemoval) {
                allImages.remove(i);
            }

            this.filteredByDateImages.addAll(allImages);
        }
    }

    /**
     * Method searches through the name of each filtered album and compares the data to the searchData
     *
     * @param inputData an ArrayList with search words
     * @return           an ArrayList with all the albums where their name corresponds with the searchData passed as parameter
     */
    public ArrayList<Album> findAlbumsByName(ArrayList<String> inputData) {
        ArrayList<Album> foundAlbums = new ArrayList<>(this.albums);
        for(String d : inputData){
            for(Album a : this.albums){
                if(!a.getName().toLowerCase().contains(d.toLowerCase())){
                    foundAlbums.remove(a);
                }
            }
        }
        return  foundAlbums;
    }

    /**
     * Method searches through all metadata for each filtered image and compares the data to the inputData
     *
     * @param inputData an ArrayList with search words
     * @return           an ArrayList with all the paths to images where their metadata corresponds with the inputData passed as parameter
     */

    public ArrayList<Image> findImagesByMetadata(ArrayList<String> inputData) {
        ArrayList<Backend.Image> foundImages = new ArrayList<>(this.images);
        ArrayList<Backend.Image> imagesForRemoval = new ArrayList<>();
        for (String d : inputData) {
            for (Backend.Image i : this.images) {
                //Adds the values of each data to a ArrayList if the value is not null
                ArrayList<String> values = new ArrayList<>();

                values.add(i.getPath());

                if (!(i.getIso() == null)) {
                    values.add(i.getIso());
                }
                if (!(i.getShutterspeed() == null)) {
                    values.add(i.getShutterspeed());
                }
                if (!(i.getAperture() == null)) {
                    values.add(i.getAperture());
                }
                if (!(i.getFocallength() == null)) {
                    values.add(i.getFocallength());
                }
                if (!(i.getDate() == null)) {
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    String dateText = df.format(i.getDate());
                    values.add(dateText);
                }
                if (!(i.getName() == null)) {
                    values.add(i.getName());
                }

                //Sends the search word and the ArrayList with all the metadatas from the image to the isMatch() method
                //If the isMatch() method does not find a match, the tags will be searched for a match
                if (!(isMatch(d, values))) {
                    boolean removeImage = true;
                    for (Tag t : i.getTags()) {
                        if (t.getName().toLowerCase().contains(d.toLowerCase())) {
                            removeImage = false;
                            break;
                        }
                    }
                    //If none of the tags match either, the image will be added to the images for removal list
                    if (removeImage) {
                        imagesForRemoval.add(i);
                    }
                }
            }
        }
        //Removes all the images
        for (Backend.Image i : imagesForRemoval) {
            foundImages.remove(i);
        }
        return foundImages;
    }

    /**
     *
     * @param data       the search word that the method try to find a match to
     * @param imagedatas the metadata values from an image
     * @return           true if the method found a match and false if there was no match to be found
     */
    private boolean isMatch(String data, ArrayList<String> imagedatas) {
        for (String imda : imagedatas) {
            if (imda.toLowerCase().contains(data.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void searchImages(ArrayList<String> searchData){
        setSearchedImages(findImagesByMetadata(searchData));
    }

    public void searchAlbums(ArrayList<String> searchData){
        setSearchedAlbums(findAlbumsByName(searchData));
    }


    public void filterImages(ArrayList<String> filterData){
        setFilteredImages(findImagesByMetadata(filterData));
    }

    public void filterAlbums(ArrayList<String> searchData) {
        setFilteredAlbums(findAlbumsByName(searchData));
    }

    /**
     * The method clears the lists of the images and albums to be shown,
     * then fill them up again based on the list of all the images and albums
     * and filter and sort based on the search, filter and sort inputs by the user.
     */
    public void setImagesAndAlbumsToShow(){
        this.imagesToShow.clear();
        this.albumsToShow.clear();

        //Set imagesToShow
        //Check which list is shortest
        if(this.filteredImages.size() < this.filteredByDateImages.size() && this.filteredImages.size() < this.searchedImages.size()){
            //filteredImages is the shortest list
            for(Image i : this.filteredImages){
                if(this.filteredByDateImages.contains(i) && this.searchedImages.contains(i)){
                    this.imagesToShow.add(i.getPath());
                }
            }
        }
        else if(this.filteredByDateImages.size() < this.searchedImages.size()){
            //gallery.getFilteredByDateImages() is the shortest list
            for(Image i : this.filteredByDateImages){
                if(this.searchedImages.contains(i) && this.filteredImages.contains(i)){
                    this.imagesToShow.add(i.getPath());
                }
            }
        }
        else{
            //gallery.getSearchedImages() is the shortest list
            for(Image i : this.searchedImages){
                if(filteredImages.contains(i) && filteredByDateImages.contains(i)){
                    this.imagesToShow.add(i.getPath());
                }
            }
        }

        //Set albumsToShow
        if(this.searchedAlbums.size() < this.filteredAlbums.size()){
            for(Album a : this.searchedAlbums){
                if(this.filteredAlbums.contains(a)){
                    this.albumsToShow.add(a);
                }
            }
        }
        else{
            for(Album a : this.filteredAlbums){
                if(this.searchedAlbums.contains(a)){
                    this.albumsToShow.add(a);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Gallery{" +
                "images=" + this.images +
                ", albums=" + this.albums +
                ", imageDAO=" + this.imageDAO +
                ", albumDAO=" + this.albumDAO +
                ", tagDAO=" + this.tagDAO +
                ", imagesToShow=" + this.imagesToShow +
                '}';
    }
}
