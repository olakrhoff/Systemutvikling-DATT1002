package Backend;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO implements IAlbumDAO{
    private EntityManagerFactory emf;

    /**
     * Constructs an AlbumDAO-object containing of an EntityManagerFactory object for further use.
     * @param emf
     */
    public AlbumDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Method uses the EntityManagerObject to create an EntityManager object.
     * @return An EntityManager-object
     */
    public EntityManager getEm() {
        return emf.createEntityManager();
    }

    /**
     * Method store a new Album-object in the ALBUM-table in the database.
     * The method first check if an album-object with the same name as the album-object passed exist,
     * if so, the album won't be stored.
     * @param album The album-object wanting to be stored in the database
     * @return True if the album were successfully stored in the database, and false if it were not.
     */
    public boolean storeNewAlbum(Album album) {
        EntityManager em = this.getEm();
        try {
            em.getTransaction().begin();
            if (this.getAlbumByName(album.getName()) != null) {
                em.getTransaction().rollback();
                em.close();
                return false;
            } else {
                em.persist(album);
                em.getTransaction().commit();
                em.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The method searches for an album-object in the ALBUM-table with an id corresponding to the id
     * given by the parameter, and returns this.
     * @param id the id of the album wanting to be found
     * @return The album-object from the database with the corresponding album-id, or null if the album is not found.
     */
    public Album findAlbum(int id) {
        EntityManager em = getEm();
        Album foundAlbum = null;
        try {
            foundAlbum = em.find(Album.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return foundAlbum;
    }

    /**
     * The method searches for an album-object in the ALBUM-table with an id corresponding to the id
     * given by the parameter, and deletes this from the database.
     * @param id the id of the album wanting to be deleted.
     */
    public boolean deleteAlbum(int id) {
        EntityManager em = getEm();
        try {
            Album a = em.find(Album.class, id);
            if(a != null){
                em.getTransaction().begin();
                em.remove(a);
                em.getTransaction().commit();
                em.close();
                return true;
            }else{
                em.getTransaction().rollback();
                em.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The method changes the values in the Album object,
     * to the values sent in as parameters.
     * It tries to find the album in the database, if it does, it sets the
     * object variables to the new values.
     * @param albumId
     * @param newImagePerRow
     * @param newImagePerPage
     * @param newName
     * @return true if changes went through, false otherwise
     */
    public boolean editAlbum(int albumId, int newImagePerRow, int newImagePerPage, String newName) {
        EntityManager em = this.getEm();
        Album foundAlbum;
        try {
            em.getTransaction().begin();
            foundAlbum = this.findAlbum(albumId);
            if (foundAlbum != null && newImagePerPage != 0 && newImagePerPage != 0 && newName != null) {
                foundAlbum.setImagesPerRow(newImagePerRow);
                foundAlbum.setPages(newImagePerPage);
                foundAlbum.setName(newName);
                em.merge(foundAlbum);
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
     * The method returns every object stored in the ALBUM-table in the database.
     * @return A list of all the album-objects stored in the database, or null if no albums are stored in the database.
     */
    public ArrayList<Album> getAllAlbums() {
        EntityManager em = getEm();
        List<Album> allAlbums = null;
        ArrayList<Album> returnedAlbums = new ArrayList<>();
        try {
            Query q = em.createQuery("SELECT a FROM Album a");
            allAlbums = q.getResultList();
        }catch(NoResultException e) {
            System.out.println("No tags are stored in the database");
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        returnedAlbums.addAll(allAlbums);
        return returnedAlbums;
    }

    /**
     * The method searches through the ALBUM-table looking for an album-object with a name corresponding to the
     * name given by the parameters, and returns this,
     * @param name The name of the album wanting to be found.
     * @return Returns the album with the name corresponding to the name-parameter, or null if no album has that name.
     */
    public Album getAlbumByName(String name) {
        EntityManager em = getEm();
        Album foundAlbum = null;
        try {
            Query q = em.createQuery("SELECT a FROM Album a WHERE a.name = :name");
            foundAlbum = (Album) q.setParameter("name", name).getSingleResult();

        } catch (IllegalStateException e) {
            System.out.println("No album with given name exist, an album with this name can be stored");

        } catch (NoResultException e) {
            System.out.println("No album with given name exist, an album with this name can be stored");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return foundAlbum;
    }

    /**
     * Method adds a new image to the list of images contained by the album given by the parameters.
     * The method first check if the image already exist in the list, if so, the image won't be added.
     * @param image the image-object being added.
     * @param albumId Id to the album being tagged.
     * @return True if the image were successfully added, and false if the image already exist in the albums's list of images.
     */
    public boolean addImageToAlbum(Image image, int albumId) {
        EntityManager em = getEm();
        int imageId = image.getId();
        Album albumFound;
        Image imageFound;
        try{
            em.getTransaction().begin();
            albumFound = this.findAlbum(albumId);
            imageFound = em.find(Image.class, imageId);
            if(!albumFound.getImages().contains(imageFound)){
                albumFound.addImage(imageFound);
                em.merge(albumFound);
                em.getTransaction().commit();
                em.close();
                return true;
            } else {
                em.getTransaction().rollback();
                em.close();
                return false;
            }

        }catch(Exception e){
            System.out.println("This album already exist");
            return false;
        }
    }

    @Override
    public String toString() {
        return "AlbumDAO{" +
                "emf=" + emf +
                '}';
    }
}
