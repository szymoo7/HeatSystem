package org.example.mainappui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.service.Apartment;
import org.example.service.DirectorApp;
import org.example.service.Loggable;
import org.example.service.TaskInfo;

import java.util.List;

public class AdminController {

    private static DirectorApp admin;

    @FXML
    private Label heatSystemText;
    @FXML
    private TableView<Apartment> apartments;
    @FXML
    private TableColumn<Apartment, Integer> buildingNumber;
    @FXML
    private TableColumn<Apartment, Integer> apartmentNumber;
    @FXML
    private TableColumn<Apartment, String> tenant;
    @FXML
    private TableColumn<Apartment, Double> area;
    @FXML
    private Button updateTasksButton;
    @FXML
    private Button updateApartments;

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
    private TableColumn<TaskInfo, String> buildingNr;
    @FXML
    private TableColumn<TaskInfo, String> apartmentNr;

    public static Loggable getAdmin() {
        return admin;
    }

    @FXML
    protected void initialize() {
        buildingNumber.setCellValueFactory(new PropertyValueFactory<>("buildingNumber"));
        apartmentNumber.setCellValueFactory(new PropertyValueFactory<>("apartmentNumber"));
        tenant.setCellValueFactory(new PropertyValueFactory<>("tenant"));
        area.setCellValueFactory(new PropertyValueFactory<>("area"));
        executor.setCellValueFactory(new PropertyValueFactory<>("executor"));
        task.setCellValueFactory(new PropertyValueFactory<>("task"));
        taskDescription.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        taskStatus.setCellValueFactory(new PropertyValueFactory<>("taskStatus"));
        dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        assignedDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));
        buildingNr.setCellValueFactory(new PropertyValueFactory<>("buildingNumber"));
        apartmentNr.setCellValueFactory(new PropertyValueFactory<>("apartmentNumber"));
    }

    @FXML
    protected void onClickUpdateApartments() {
        List<Apartment> apartments = admin.getApartments();
        this.apartments.setItems(FXCollections.observableArrayList(apartments));
    }

    public static void setAdmin(DirectorApp admin) {
        AdminController.admin = admin;
    }

    @FXML
    protected void onLoadTasksButtonClick() {
        List<TaskInfo> tasks = admin.getTasks();
        taskList.setItems(FXCollections.observableArrayList(tasks));
    }


}
