module com.example.echecs {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.echecs to javafx.fxml;
    exports com.example.echecs;
}