package org.example.mainappui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.service.*;

import java.io.IOException;
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
    private TextField nameInput1;
    @FXML
    private TextField surnameInput;
    @FXML
    private TextField loginInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private TextField buildingInput1;
    @FXML
    private TextField apartmentInput;
    @FXML
    private Button registerTenantButton;
    @FXML
    private Button confirmRegisterTenantButton;
    @FXML
    private TextField areaInput;
    @FXML
    private TextField buildingInput2;
    @FXML
    private TextField priceBuildingKWH;
    @FXML
    private Button setBuildingPriceButton;
    @FXML
    private Button setPriceButton;


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
    protected void onClickUpdateApartments() {
        buildingNumber.setCellValueFactory(new PropertyValueFactory<>("buildingNumber"));
        apartmentNumber.setCellValueFactory(new PropertyValueFactory<>("apartmentNumber"));
        tenant.setCellValueFactory(new PropertyValueFactory<>("tenant"));
        area.setCellValueFactory(new PropertyValueFactory<>("area"));
        List<Apartment> apartments = admin.getApartments();
        this.apartments.setItems(FXCollections.observableArrayList(apartments));
    }

    public static void setAdmin(DirectorApp admin) {
        AdminController.admin = admin;
    }

    @FXML
    protected void onLoadTasksButtonClick() {
        executor.setCellValueFactory(new PropertyValueFactory<>("executor"));
        task.setCellValueFactory(new PropertyValueFactory<>("task"));
        taskDescription.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        taskStatus.setCellValueFactory(new PropertyValueFactory<>("taskStatus"));
        dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        assignedDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));
        buildingNr.setCellValueFactory(new PropertyValueFactory<>("buildingNumber"));
        apartmentNr.setCellValueFactory(new PropertyValueFactory<>("apartmentNumber"));
        List<TaskInfo> tasks = admin.getTasks();
        taskList.setItems(FXCollections.observableArrayList(tasks));
    }

    @FXML
    protected void onRegisterTenantButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registerTenantScene.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Register Tenant");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onConfirmRegisterTenant() {
        String name = nameInput1.getText();
        String surname = surnameInput.getText();
        String login = loginInput.getText();
        String password = passwordInput.getText();
        int buildingNumber = Integer.parseInt(buildingInput1.getText());
        int apartmentNumber = Integer.parseInt(apartmentInput.getText());
        double area = Double.parseDouble(areaInput.getText());
        Tenant t = new Tenant(name, surname, login, password, buildingNumber, apartmentNumber, area);
        admin.registerTenant(t);
    }

    @FXML
    protected void onSetBuildingPriceButton() {
        int buildingNumber = Integer.parseInt(buildingInput2.getText());
        double price = Double.parseDouble(priceBuildingKWH.getText());
        admin.setPricePerKwh(price, new Building(buildingNumber, price));
    }

    @FXML
    protected void onSetPriceButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("setBuildingKWH.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Set price");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
