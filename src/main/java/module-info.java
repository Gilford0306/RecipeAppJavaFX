module org.example.recipeappjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.recipeappjavafx to javafx.fxml;
    exports org.example.recipeappjavafx;
}