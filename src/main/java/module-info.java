module bulkupload.tp1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    requires org.apache.commons.csv;

    opens bulkupload.tp1 to javafx.fxml;
    exports bulkupload.tp1;
}