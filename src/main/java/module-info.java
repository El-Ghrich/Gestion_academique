module org.kooralik.miniprojetjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;


    opens org.kooralik.miniprojetjavafx to javafx.fxml;
    opens org.kooralik.miniprojetjavafx.model to javafx.base;
    opens org.kooralik.miniprojetjavafx.controller to javafx.fxml;
    exports org.kooralik.miniprojetjavafx;
}