package Helpers;

import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class MetadataHelper {
    public static ArrayList<Metadata> getMetadata(String iso, String shutterspeed, String aperture, String focallength, String date) {

        ArrayList<Metadata> meta = new ArrayList<>();
        meta.add(new Metadata("Iso", iso));
        meta.add(new Metadata("Shutterspeed", shutterspeed));
        meta.add(new Metadata("Aperture", aperture));
        meta.add(new Metadata("Focallength", focallength));
        meta.add(new Metadata("Date", date));

        return meta;
    }
    public static ArrayList<TableColumn> getMetaColoumns(){
        TableColumn<Metadata, String> col1 = new TableColumn<>("Specs");
        col1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        TableColumn<Metadata, String> col2 = new TableColumn<>("Value");
        col2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getValue()));
        ArrayList<TableColumn> coloumns = new ArrayList<>();
        coloumns.add(col1);
        coloumns.add(col2);
        return coloumns;
    }

    static class Metadata {
        private String name;
        private String value;

        public Metadata(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return this.name;
        }

        public String getValue() {
            return this.value;
        }
    }
}
