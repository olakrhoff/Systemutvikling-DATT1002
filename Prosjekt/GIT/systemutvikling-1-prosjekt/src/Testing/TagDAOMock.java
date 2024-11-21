package Testing;

import Backend.ITagDAO;
import Backend.Tag;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class TagDAOMock implements ITagDAO {
    private ArrayList<Tag> tags = new ArrayList<>();

    public TagDAOMock(){
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
     * Stores a new tag
     *
     * @param tag the new tag that is to be stored
     */
    @Override
    public boolean storeNewTag(Tag tag) {
        return this.tags.add(tag);
    }

    /**
     * Finds a tag by the tag name
     *
     * @param name the tag name of the tag that is to be found
     * @return     the found tag
     */
    @Override
    public Tag getTagByName(String name) {
        for (Tag tag : this.tags) {
            if(tag.getName().equals(name)){
                return tag;
            }
        }
        return null;
    }

    /**
     * Finds all tags
     *
     * @return all tags
     */
    @Override
    public List<Tag> getAllTags() {
        return this.tags;
    }

    @Override
    public String toString() {
        return "TagDAOMock{" +
                "tags=" + tags +
                '}';
    }
}
