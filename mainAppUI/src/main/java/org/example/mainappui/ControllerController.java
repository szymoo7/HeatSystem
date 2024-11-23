package org.example.mainappui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.service.ControllerApp;
import org.example.service.Loggable;
import org.example.service.TaskInfo;

import java.io.IOException;
import java.util.List;

public class ControllerController {

    private static ControllerApp controller;

    @FXML
    private Label heatSystemText;
    @FXML
    private TextField readingInfo;
    @FXML
    private Button loadTasksButton;
    @FXML
    private Button insertReadingButton;
    @FXML
    private Button logOutButton;
    @FXML
    private TextField readBuilding;
    @FXML
    private TextField readApart;
    @FXML
    private TextField readMmm;
    @FXML
    private TextField readAmm;

    @FXML
    private TableView<TaskInfo> taskList;
    @FXML
    private TableColumn<TaskInfo, String> executor;
    @FXML
    private TableColumn<TaskInfo, String> task;
    @FXML
    private TableColumn<TaskInfo, String> taskDescription;
    @FXML
    private TableColumn<TaskInfo, String> taskStatus;
    @FXML
    private TableColumn<TaskInfo, String> dueDate;
    @FXML
    private TableColumn<TaskInfo, String> assignedDate;
    @FXML
    private TableColumn<TaskInfo, String> buildingNumber;
    @FXML
    private TableColumn<TaskInfo, String> apartmentNumber;

    @FXML
    public void initialize() {
        executor.setCellValueFactory(new PropertyValueFactory<>("executor"));
        task.setCellValueFactory(new PropertyValueFactory<>("task"));
        taskDescription.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        taskStatus.setCellValueFactory(new PropertyValueFactory<>("taskStatus"));
        dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        assignedDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));
        buildingNumber.setCellValueFactory(new PropertyValueFactory<>("buildingNumber"));
        apartmentNumber.setCellValueFactory(new PropertyValueFactory<>("apartmentNumber"));
    }

    public static void setController(ControllerApp controller) {
        ControllerController.controller = controller;
    }

    public static Loggable getController() {
        return controller;
    }

    @FXML
    protected void onLoadTasksButtonClick() {
        List<TaskInfo> tasks = controller.getTasks();
        taskList.setItems(FXCollections.observableArrayList(tasks));
    }

    @FXML
    protected void insertReading() {
        int buildingNumber = Integer.parseInt(readBuilding.getText());
        int apartmentNumber = Integer.parseInt(readApart.getText());
        double mainMeterMeasure = Double.parseDouble(readMmm.getText());
        double apartmentMeterMeasure = Double.parseDouble(readAmm.getText());
        controller.insertReading(buildingNumber, apartmentNumber, mainMeterMeasure, apartmentMeterMeasure, controller.getCurrentId());
    }

    @FXML
    protected void onLogOutButtonClick() throws IOException {
        controller.logout();
        switchToLoginScene();
    }

    @FXML
    protected void switchToLoginScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("loginScene.fxml"));
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}