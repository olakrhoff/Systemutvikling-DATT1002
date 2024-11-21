package Backend;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface for the ImageDAO and ImageDAOMock classes
 */

public interface IImageDAO {
    public EntityManager getEm();

    public boolean storeNewImage(Backend.Image image);

    public boolean deleteImage(int id);

    public Backend.Image findImage(int id);

    public ArrayList<Image> getAllImages();

    public Backend.Image getImageByPath(String path);

    public boolean addTagToImage(Tag tag, int imageId);

    public boolean removeTagFromImage(Tag tag, int imageId);

    public boolean editImage(int imageId, String newName);



}
