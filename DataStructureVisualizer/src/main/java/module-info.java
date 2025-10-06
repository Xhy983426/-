module com.example.datastructurevisualizer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.datastructure.visualizer to javafx.fxml;
    exports com.datastructure.visualizer;
}