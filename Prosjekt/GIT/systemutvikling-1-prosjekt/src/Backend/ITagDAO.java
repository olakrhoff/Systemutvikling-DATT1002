package Backend;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Interface for the TagDAO and TagMock classes
 */

public interface ITagDAO {
    public EntityManager getEm();

    public boolean storeNewTag(Tag tag);

    public Tag getTagByName(String name);

    public List<Tag> getAllTags();
}
