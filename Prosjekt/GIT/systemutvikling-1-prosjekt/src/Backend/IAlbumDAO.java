package Backend;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface for the ALbumDAO and AlbumDAOMock classes
 */

public interface IAlbumDAO {
    public EntityManager getEm();

    public boolean storeNewAlbum(Album album);

    public Album findAlbum(int id);

    public boolean deleteAlbum(int id);

    public ArrayList<Album> getAllAlbums();

    public Album getAlbumByName(String name);

    public boolean addImageToAlbum(Image image, int albumId);

    public boolean editAlbum(int albumId, int newImagesPerRow, int newImagesPerPage, String newName);
}
