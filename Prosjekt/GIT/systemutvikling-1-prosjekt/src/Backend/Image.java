package Backend;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representation for Image
 */
@Entity
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToMany
    private List<Tag> tags = new ArrayList<>();
    @ManyToMany(mappedBy = "images")
    private List<Album> albums;
    private String path;
    private String iso;
    private String shutterspeed;
    private String aperture;
    private String focallength;
    private Date date;
    private String name;

    public Image(){}


    public Image(String path, String iso, String shutterspeed, String aperture, String focallength, Date date, String name){
        this.path = path;
        this.iso = iso;
        this.shutterspeed = shutterspeed;
        this.aperture = aperture;
        this.focallength = focallength;
        this.date = date;
        this.name = name;
        this.albums = new ArrayList<Album>();
    }

    public int getId() {
        return this.id;
    }

    public String getPath() {
        return this.path;
    }


    public String getIso() {
        return this.iso;
    }

    public String getShutterspeed() {
        return this.shutterspeed;
    }

    public String getAperture() {
        return this.aperture;
    }

    public String getFocallength() {
        return this.focallength;
    }

    public Date getDate() {
        return this.date;
    }

    public String getName() {
        return this.name;
    }

    public List<Tag> getTags(){
        return this.tags;
    }

    public List<Album> getAlbums(){
        return this.albums;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public void setShutterspeed(String shutterspeed) {
        this.shutterspeed = shutterspeed;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    public void setFocallength(String focallength) {
        this.focallength = focallength;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The method stores the tag-object, passed in the method-head, in the list of tag-objects, in addition to storing
     * the image-object in use to the list of image-objects contained by the tag-object passed.
     * @param tag
     */
    public void addTag(Tag tag){
        this.tags.add(tag);
        tag.getImages().add(this);
    }

    /**
     * The method removes the tag-object, passed in the method-head, from the list of tag-objects, in addition to removing
     * the image-object in use to the list of image-objects contained by the tag-object passed.
     * @param tag
     */
    public void removeTag(Tag tag){
        this.tags.remove(tag);
        tag.getImages().remove(this);
    }

    /**
     * Gets all the tags the image have.
     * @return      List<String>, list of all the tags the image have
     */
    public List<String> getTagsString(){
        ArrayList<String> s = new ArrayList<>();
        List<Tag> tags = getTags();
        for (Tag t : tags) {
            s.add(t.getName());
        }
        return s;
    }

    /**
     * Checks if two images equal, based on their path.
     * @param o
     * @return true, if equal, otherwise false
     */
    public boolean equals(Object o){
        if (this == o){
            return true;
        }
        if (!(o instanceof Image)){
            return false;
        }
        Image image = (Image)o;

        return image.getPath().equals(this.getPath());
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Backend.Image{" +
                "id=" + this.id +
                ", path='" + this.path + '\'' +
                ", iso=" + this.iso +
                ", shutterspeed=" + this.shutterspeed +
                ", aperture=" + this.aperture +
                ", focallength=" + this.focallength +
                ", date=" + this.date +
                ", name='" + this.name + '\'' +
                '}';
    }
}
