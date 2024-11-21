package Backend;

import java.util.Comparator;

public class SortAlbumByNameAZ implements Comparator<Album> {
    /**
     *
     * @param a1 first album for comparing
     * @param a2 second album for comparing
     * @return returns an integer that will be either -1, 0 or 1
     */
    public int compare(Album a1, Album a2) {
        String albumName = a1.getName().toLowerCase();
        String albumName2 = a2.getName().toLowerCase();

        return albumName.compareTo(albumName2);
    }
}
