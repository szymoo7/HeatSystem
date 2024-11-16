package org.example.mainappui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.service.ControllerApp;
import org.example.service.TaskInfo;

import java.util.List;

public class ControllerController {

    private static ControllerApp controller;

    @FXML
    private Label heatSystemText;
    @FXML
    private TextField readingInfo;

    @FXML
    private TableView<TaskInfo> taskList;
    @FXML
    private Button loadTasksButton;
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
    public void initialize() {
        executor.setCellValueFactory(new PropertyValueFactory<TaskInfo, String>("executor"));
        task.setCellValueFactory(new PropertyValueFactory<TaskInfo, String>("task"));
        taskDescription.setCellValueFactory(new PropertyValueFactory<TaskInfo, String>("taskDescription"));
        taskStatus.setCellValueFactory(new PropertyValueFactory<TaskInfo, String>("taskStatus"));
        dueDate.setCellValueFactory(new PropertyValueFactory<TaskInfo, String>("dueDate"));
        assignedDate.setCellValueFactory(new PropertyValueFactory<TaskInfo, String>("assignedDate"));
    }

    public static void setController(ControllerApp controller) {
        ControllerController.controller = controller;
    }

    public static ControllerApp getController() {
        return controller;
    }

    @FXML
    protected void onLoadTasksButtonClick() {
        List<TaskInfo> tasks = controller.getTasks();
        taskList.setItems(FXCollections.observableArrayList(tasks));
    }
}