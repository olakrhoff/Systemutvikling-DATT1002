package Backend;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TagDAO implements ITagDAO{
    private EntityManagerFactory emf;

    /**
     * Constructs an TagDAO-object containing of an EntityManagerFactory object for further use.
     * @param emf
     */
    public TagDAO(EntityManagerFactory emf) {
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
     * Method store a new Tag-object in the TAG-table in the databse.
     * The method first check if a Tag-object with the same name as the tag-object passed exist,
     * if so, the tag won't be stored.
     * @param tag The tag wanting to be stored in the database
     * @return True if the tag were successfully stored in the database, and false if it were not.
     */
    public boolean storeNewTag(Tag tag) {
        EntityManager em = this.getEm();
        try {
            em.getTransaction().begin();
            if (this.getTagByName(tag.getName()) != null) {
                //Tag is already stored in database
                em.getTransaction().rollback();
                em.close();
                return false;
            } else {
                em.persist(tag);
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
     * The method searches through the TAG-table looking for a tag-object with a name corresponding to the
     * name given by the parameters, and returns this,
     * @param name The name of the tag wanting to be found.
     * @return Returns the tag with the name corresponding to the name-parameter, or null if image is not found.
     */
    public Tag getTagByName(String name) {
        EntityManager em = getEm();
        Tag foundTag = null;
        try {
            Query q = em.createQuery("SELECT t FROM Tag t WHERE t.name = :name");
            foundTag = (Tag) q.setParameter("name", name).getSingleResult();

        } catch (IllegalStateException e) {
            System.out.println("No tag with given name exist in the database, a tag with this name can be stored");
        } catch (NoResultException e) {
            System.out.println("No tag with given name exist in the database, a tag with this name can be stored");
        } finally {
            em.close();
        }
        return foundTag;
    }

    /**
     * The method returns every object stored in the TAG-table in the database.
     * @return A list of all the tag-objects stored in the database, or null if no tags are stored in the database.
     */
    public List<Tag> getAllTags() {
        EntityManager em = getEm();
        List<Tag> allTags = null;
        try {
            Query q = em.createQuery("SELECT t FROM Tag t");
            allTags = q.getResultList();

        } catch(NoResultException e) {
            System.out.println("No tags are stored in the database");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return allTags;
    }


    @Override
    public String toString() {
        return "TagDAO{" +
                "emf=" + emf +
                '}';
    }
}