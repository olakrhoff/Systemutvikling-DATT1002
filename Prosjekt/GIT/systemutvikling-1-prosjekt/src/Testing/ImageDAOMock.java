package Testing;

import Backend.IImageDAO;
import Backend.Image;
import Backend.Tag;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ImageDAOMock implements IImageDAO {
    private ArrayList<Backend.Image> images = new ArrayList<>();

    public ImageDAOMock(){

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
     * Stores a new image in the
     *
     * @param image the new image that is to be stored
     */
    @Override
    public boolean storeNewImage(Backend.Image image) {
        image.setId(this.images.size());
        return this.images.add(image);
    }

    /**
     * Deletes image from ArrayList
     *
     * @param index index of the image that is to be removed
     */
    @Override
    public boolean deleteImage(int index) {
        return this.images.remove(this.findImage(index));
    }

    /**
     * Find image by index
     *
     * @param index index of the image that is to be found
     * @return      the image that was found
     */
    @Override
    public Backend.Image findImage(int index) {
        return this.images.get(index);
    }

    /**
     * Finds all the images
     *
     * @return the ArrayList with all the images
     */
    @Override
    public ArrayList<Backend.Image> getAllImages() {
        return this.images;
    }

    /**
     * Finds an image by its path
     *
     * @param path the path of the image that is to be found
     * @return     the image
     */
    @Override
    public Backend.Image getImageByPath(String path) {
        for (Backend.Image image : this.images) {
            if(image.getPath().equals(path)){
                return image;
            }
        }
        return null;
    }

    /**
     * Adds a tag to an image
     *
     * @param tag     the tag that is going to be added to the image
     * @param imageId the image ID of the image the tag is going to be added to
     * @return        true if successful and false if the process failed
     */
    @Override
    public boolean addTagToImage(Tag tag, int imageId) {
        try{
            for(int i = 0; i < this.images.size(); i++){
                if(this.images.get(i).getId() == imageId){
                    this.images.get(i).addTag(tag);
                    return true;
                }
            }
            return false;
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removed tag from an image
     *
     * @param tag     the tag that is going to be removed
     * @param imageId the image ID to the image that the tag is going to be removed from
     * @return        true if successful and false if the process failed
     */
    @Override
    public boolean removeTagFromImage(Tag tag, int imageId) {
        try{
            for(int i = 0; i < this.images.size(); i++){
                if(this.images.get(i).getId() == i){
                    for(int j = 0; j < this.images.get(i).getTags().size(); j++){
                        if(this.images.get(i).getTags().get(j).equals(tag)){
                            this.images.get(i).removeTag(tag);
                            return true;
                        }
                    }
                }
            }

            return false;
        }
        catch (IllegalArgumentException e){
            return false;
        }
    }

    /**
     * Edits the values in a image
     *
     * @param imageId         the id of the image that is going to be edited
     * @param newName         the new dame
     * @return                true if the image is edited successfully and false if the process failed
     */
    @Override
    public boolean editImage(int imageId, String newName) {
        try{
            if(imageId >= this.images.size()){
                return false;
            }
            this.images.get(imageId).setName(newName);

            return true;
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
    }
}
