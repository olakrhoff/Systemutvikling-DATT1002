package Backend;

import java.util.Comparator;

public class SortAlphabeticalZA implements Comparator<Image> {
    /**
     *
     * @param i1 first image for comparing
     * @param i2 second image for comparing
     * @return returns an integer that will be either -1, 0 or 1
     */
    public int compare(Image i1, Image i2) {
        String imageName1 = i1.getName().toLowerCase();
        String imageName2 = i2.getName().toLowerCase();

        return imageName2.compareTo(imageName1);
    }
}
