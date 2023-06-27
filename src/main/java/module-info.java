module com.example.gestion_des_pensions_des_retraites {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires org.apache.pdfbox;
    requires jasperreports;
    requires itextpdf;


    opens com.example.gestion_des_pensions_des_retraites to javafx.fxml;
    exports com.example.gestion_des_pensions_des_retraites;
    opens com.java.gestion_des_pensions_des_retraites to javafx.fxml;
}