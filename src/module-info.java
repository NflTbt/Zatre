module zatre {
    exports persistentie;
    exports cui;
    exports gui;
    exports main;
    exports domein;
    exports testen;
    exports exceptions;
    exports taal;

    opens gui;

    requires java.sql;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;
    requires java.desktop;
    requires javafx.media;

}