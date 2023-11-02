module bulkupload.tp1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires gson;
    requires org.apache.commons.csv;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;


    opens bulkupload.tp1 to javafx.fxml;

    // Open the bulkupload.tp1.data package to make it accessible to Gson
    opens bulkupload.tp1.data;

    exports bulkupload.tp1;
}
