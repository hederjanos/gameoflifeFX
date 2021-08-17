module hu.hj.gameoflifefx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens hu.hj.gameoflifefx to javafx.fxml;
    exports hu.hj.gameoflifefx;
    exports hu.hj.gameoflifefx.controller;
    opens hu.hj.gameoflifefx.controller to javafx.fxml;
}