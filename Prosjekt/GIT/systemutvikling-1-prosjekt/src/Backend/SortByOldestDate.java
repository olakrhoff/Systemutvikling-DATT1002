package Backend;

import java.sql.Date;
import java.util.Comparator;

public class SortByOldestDate implements Comparator<Image> {
    /**
     *
     * @param i1 first image for comparing
     * @param i2 second image for comparing
     * @return returns an integer that will tell if which date is the latest
     */
    public int compare(Image i1, Image i2){
        Date date1 = i1.getDate();
        Date date2 = i2.getDate();

        return date1.compareTo(date2);
    }
}
