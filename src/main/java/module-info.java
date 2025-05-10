module com.example.blanza {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.mail;
    requires io.github.cdimascio.dotenv.java;

    opens com.example.blanza to javafx.fxml;
    exports com.example.blanza;
}