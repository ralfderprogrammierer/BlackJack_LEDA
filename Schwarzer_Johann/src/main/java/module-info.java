module at.spengergasse.schwarzer_johann {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.spengergasse.schwarzer_johann to javafx.fxml;
    exports at.spengergasse.schwarzer_johann;
}