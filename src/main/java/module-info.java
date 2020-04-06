module com.greatwebguy.application {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.kordamp.iconli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;
    exports com.greatwebguy.application to javafx.graphics, javafx.fxml;
    opens com.greatwebguy.application to javafx.fxml;
}