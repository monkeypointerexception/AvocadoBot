module javaxfx.gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens javaxfx.gui to javafx.fxml;
    exports javaxfx.gui;
}