package Testing;

import Backend.Album;
import Backend.IAlbumDAO;
import Backend.Image;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAOMock implements IAlbumDAO {
    private ArrayList<Album> albums = new ArrayList<>();

    public AlbumDAOMock(){

    }

    /**
     *
     * @return null
     */
    @Override
    public EntityManager getEm() {
        return null;
    }

    /**
     * Stores a new album
     *
     * @param album an album which is added to the list of albums
     */
    @Override
    public boolean storeNewAlbum(Album album) {
        album.setId(this.albums.size());
        return this.albums.add(album);
    }

    /**
     * Finds a specific album
     *
     * @param index the index of which album that is to be found
     * @return      the album with the chosen index
     */
    @Override
    public Album findAlbum(int index) {
        return this.albums.get(index);
    }

    /**
     * Deletes a specific album
     *
     * @param index the index of which album that is to be deleted
     */
    @Override
    public boolean deleteAlbum(int index) {
        return this.albums.remove(this.albums.get(index));
    }

    /**
     * Finds all albums
     *
     * @return all the registered albums
     */
    @Override
    public ArrayList<Album> getAllAlbums() {
        return this.albums;
    }

    /**
     * Gets album with certain name
     *
     * @param name the name of the album that is to be found
     * @return     the album with the specific name or null if it is not found
     */
    @Override
    public Album getAlbumByName(String name) {
        for (Album album : this.albums) {
            if(album.getName().equals(name)){
                return album;
            }
        }
        return null;
    }

    /**
     * Adds an image to an album
     *
     * @param image   the image that is going to be added to the album
     * @param albumId the id of the album the image is added to
     * @return        true if successful and false if the process failed
     */
    @Override
    public boolean addImageToAlbum(Backend.Image image, int albumId) {
        try{
            this.albums.get(albumId).addImage(image);
            return true;
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Edits a selected album
     *
     * @param albumId          the id of the album that is to be edited
     * @param newImagesPerRow  the new value for images per row
     * @param newImagesPerPage the new value for images per page
     * @param newName          the new name for the album
     * @return                 true
     */
    @Override
    public boolean editAlbum(int albumId, int newImagesPerRow, int newImagesPerPage, String newName){
        this.albums.get(albumId).setImagesPerRow(newImagesPerRow);
        this.albums.get(albumId).setPages(newImagesPerPage);
        this.albums.get(albumId).setName(newName);
        return true;
    }

    @Override
    public String toString() {
        return "AlbumDAOMock{" +
                "albums=" + albums +
                '}';
    }
}
