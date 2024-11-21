package Backend;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class ImageDAO implements IImageDAO {
    private EntityManagerFactory emf;

    /**
     * Constructs an ImageDAO-object containing of an EntityManagerFactory object for further use.
     *
     * @param emf
     */
    public ImageDAO(EntityManagerFactory emf) {

        this.emf = emf;
    }

    /**
     * Method uses the EntityManagerObject to create an EntityManager object.
     *
     * @return An EntityManager-object
     */
    public EntityManager getEm() {
        return emf.createEntityManager();
    }

    /**
     * Method store a new Image-object in the IMAGE-table in the databse.
     * The method first check if an Image-object with the same path as the image-object passed exist,
     * if so, the image won't be stored.
     *
     * @param image The image wanting to be stored in the database
     * @return True if the image were successfully stored in the database, and false if it were not.
     */
    public boolean storeNewImage(Image image) {
        EntityManager em = this.getEm();
        try {
            em.getTransaction().begin();
            if (this.getImageByPath(image.getPath()) != null) {
                //Image already exist in table
                em.getTransaction().rollback();
                em.close();
                return false;
            } else {
                em.persist(image);
                em.getTransaction().commit();
                em.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            em.close();
            return false;
        }
    }

    /**
     * The method searches for an image-object in the IMAGE-table with an id corresponding to the id
     * given by the parameter, and deletes this from the database.
     *
     * @param id the id of the image wanting to be deleted.
     * @return True if the image were successfully removed, and false if it were not.
     */
    public boolean deleteImage(int id) {
        EntityManager em = getEm();
        try {
            Image i = em.find(Image.class, id);
            if (i != null) {
                em.getTransaction().begin();
                em.remove(i);
                em.getTransaction().commit();
                em.close();
                return true;
            } else {
                em.getTransaction().rollback();
                em.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            em.close();
            return false;
        }
    }

    /**
     * The method searches for an image-object in the IMAGE-table with an id corresponding to the id
     * given by the parameter, and returns this.
     *
     * @param id the id of the image wanting to be found
     * @return The Image-object from the database with the corresponding image-id, or null if no image has that id.
     */
    public Image findImage(int id) {
        EntityManager em = getEm();
        Image foundImage = null;
        try {
            foundImage = em.find(Image.class, id);
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return foundImage;
    }

    /**
     * The method returns every object stored in the IMAGE-table in the database.
     *
     * @return An Arraylist of all the image-objects stored in the database, or null if no images are stored in the database.
     */
    public ArrayList<Image> getAllImages() {
        EntityManager em = getEm();
        List<Image> allImages = null;
        ArrayList<Image> returnedImages = new ArrayList<>();
        try {
            Query q = em.createQuery("SELECT i FROM Image i");
            allImages = q.getResultList();
        } catch (NoResultException e) {
            System.out.println("No images are stored in the database");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        returnedImages.addAll(allImages);
        return returnedImages;
    }

    /**
     * The method searches through the IMAGE-table looking for an image-object with a name corresponding to the
     * name given by the parameters, and returns this,
     *
     * @param path The path of the image wanting to be found.
     * @return Returns the image with the path corresponding to the path-parameter, or null if image is not found.
     */
    public Image getImageByPath(String path) {
        EntityManager em = getEm();
        Image foundImage = null;
        try {
            Query q = em.createQuery("SELECT i FROM Image i WHERE i.path = :path");
            foundImage = (Image) q.setParameter("path", path).getSingleResult();

        } catch (IllegalStateException e) {
            System.out.println("No images with given path exist, an image with this path can be stored");
        } catch (NoResultException e) {
            System.out.println("No images with given path exist, an image with this path can be stored");
        } finally {
            em.close();
        }
        return foundImage;
    }

    /**
     * Method adds a new tag to the list of tags contained by the image given by the parameters.
     * The method first check if the tag already exist in the list, if so, the tag won't be added.
     *
     * @param tag   tag-object being added.
     * @param imageId Id to the image being tagged.
     * @return True if the tag were successfully added, and false if the tag already exist in the image's list of tags.
     */
    public boolean addTagToImage(Tag tag, int imageId) {
        int tagId = tag.getId();
        EntityManager em = getEm();
        Image imageFound = null;
        Tag tagFound = null;
        try {
            em.getTransaction().begin();
            imageFound = this.findImage(imageId);
            tagFound = em.find(Tag.class, tagId);
            if (!imageFound.getTags().contains(tagFound)) {
                imageFound.addTag(tagFound);
                em.merge(imageFound);
                em.getTransaction().commit();
                em.close();
                return true;
            } else {
                em.getTransaction().rollback();
                em.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            em.close();
            return false;
        }
    }

    /**
     * Method removes a tag from the list of tags contained by the image given by the parameters.
     *
     * @param tag   the tag-object being removed.
     * @param imageId Id to the image being containing the tag.
     * @return True if the tag were successfully removed from the image, and false if it were not.
     */
    public boolean removeTagFromImage(Tag tag, int imageId) {
        int tagId = tag.getId();
        EntityManager em = getEm();
        Image imageFound;
        Tag tagFound;
        try {
            em.getTransaction().begin();
            imageFound = this.findImage(imageId);
            tagFound = em.find(Tag.class, tagId);
            if (imageFound.getTags().contains(tagFound)) {
                imageFound.removeTag(tagFound);
                em.merge(imageFound);
                em.getTransaction().commit();
                em.close();
                return true;
            } else {
                em.getTransaction().rollback();
                em.close();
                return false;
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            em.close();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method edit all the metdata of a chosen image in the IMAGE-table in the database.
     * @param imageId Id to the image being edited.
     * @param newName The image's new name.
     * @return true if the edit was successfull, and false if it were not.
     */
    public boolean editImage(int imageId, String newName) {
        EntityManager em = this.getEm();
        Image foundImage;
        try {
            em.getTransaction().begin();
            foundImage = this.findImage(imageId);
            if (foundImage != null && newName != null) {
                foundImage.setName(newName);
                em.merge(foundImage);
                em.getTransaction().commit();
                em.close();
                return true;
            } else {
                em.getTransaction().rollback();
                em.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            em.close();
            return false;
        }
    }

    @Override
    public String toString() {
        return "ImageDAO{" +
                "emf=" + emf +
                '}';
    }
}








