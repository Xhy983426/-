module com.datastructurevisualizer {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.datastructurevisualizer to javafx.fxml;
    opens com.datastructurevisualizer.controller to javafx.fxml;
    opens com.datastructurevisualizer.model to javafx.fxml;
    opens com.datastructurevisualizer.view to javafx.fxml;
    opens com.datastructurevisualizer.view.components to javafx.fxml;

    exports com.datastructurevisualizer;
    exports com.datastructurevisualizer.controller;
    exports com.datastructurevisualizer.model;
    exports com.datastructurevisualizer.view.components;
}