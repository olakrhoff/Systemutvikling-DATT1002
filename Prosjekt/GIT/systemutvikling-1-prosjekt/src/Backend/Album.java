package Backend;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *Entityrepresentation for Album
 */
@Entity
public class Album implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToMany
    private List<Image> images = new ArrayList<>();
    private String name;
    private int imagesPerRow;
    private int pages;

    public Album(){}

    public Album(String name, int imagesPerRow, int pages){
        this.name = name;
        this.imagesPerRow = imagesPerRow;
        this.pages = pages;
        this.images = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getImagesPerRow() {
        return this.imagesPerRow;
    }

    public int getPages() {
        return this.pages;
    }

    public List<Image> getImages(){
        return this.images;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImagesPerRow(int imagesPerRow) {
        this.imagesPerRow = imagesPerRow;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    /**
     * The method stores the image-object, passed in the method-head, in the list of image-objects, in addition to storing
     * the album-object in use to the list of album-objects contained by the image-object passed.
     * @param image
     */
    public void addImage(Image image){
        this.images.add(image);
        image.getAlbums().add(this);
    }

    /**
     * Compares Album objects by their object variables, returns true if
     * they are equal on every term, and false otherwise.
     * @param o
     * @return
     */
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof Album)){
            return false;
        }

        Album newAlbum = (Album)o;

        return newAlbum.getId() == this.getId() && newAlbum.getName().equals(this.getName()) && newAlbum.getImagesPerRow() == this.getImagesPerRow() && newAlbum.getPages() == this.getPages() && newAlbum.getImages().equals(this.getImages());
    }

    @Override
    public String toString() {
        return "Backend.Album{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", imagesPerRow=" + this.imagesPerRow +
                ", pages=" + this.pages +
                '}';
    }
}
