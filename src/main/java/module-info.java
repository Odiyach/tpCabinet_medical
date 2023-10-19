module sio.tp2cabinetmedical {
    requires javafx.controls;
    requires javafx.fxml;


    opens sio.tp2cabinetmedical to javafx.fxml;
    exports sio.tp2cabinetmedical;
}