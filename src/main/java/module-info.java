module com.example.blanza {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires jdk.jfr;
    requires java.desktop;
    requires jakarta.mail;

    opens com.example.blanza to javafx.fxml;
    exports com.example.blanza;
}