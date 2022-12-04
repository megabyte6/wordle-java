module wordle {
    requires javafx.fxml;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    opens com.megabyte6.wordle to javafx.fxml;

    exports com.megabyte6.wordle;
}