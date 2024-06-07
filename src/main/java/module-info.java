module com.example.echecs {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.echecs to javafx.fxml;
    exports com.example.echecs;
    exports com.example.echecs.controllers;
    opens com.example.echecs.controllers to javafx.fxml;
    exports com.example.echecs.controllers.computerClass;
    opens com.example.echecs.controllers.computerClass to javafx.fxml;
    exports com.example.echecs.controllers.friendClass;
    opens com.example.echecs.controllers.friendClass to javafx.fxml;
    exports com.example.echecs.controllers.tournamentClass;
    opens com.example.echecs.controllers.tournamentClass to javafx.fxml;
}