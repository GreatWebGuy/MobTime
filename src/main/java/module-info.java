module com.greatwebguy.application {
    requires javafx.base;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    exports com.greatwebguy.application to javafx.graphics, javafx.fxml;

    opens com.greatwebguy.application to javafx.fxml;
}