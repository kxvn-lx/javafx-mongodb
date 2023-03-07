module com.example.bjb2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    opens com.example.bjb2 to javafx.fxml;
    opens com.example.bjb2.Controllers to javafx.fxml;
    opens com.example.Database to javafx.model;

    exports com.example.bjb2;
    exports com.example.Database;
    exports com.example.bjb2.Controllers;
    exports com.example.Database.Models;
    opens com.example.Database.Models to javafx.model;
    exports com.example.Database.DAO;
    opens com.example.Database.DAO to javafx.model;
    exports com.example.bjb2.Controllers.Dialogs;
    opens com.example.bjb2.Controllers.Dialogs to javafx.fxml;
}