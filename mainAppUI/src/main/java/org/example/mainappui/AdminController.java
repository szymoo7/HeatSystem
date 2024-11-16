package org.example.mainappui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.service.Apartment;

public class AdminController {

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
    public void initialize() {
        buildingNumber.setCellValueFactory(new PropertyValueFactory<Apartment, Integer>("buildingNumber"));
        apartmentNumber.setCellValueFactory(new PropertyValueFactory<Apartment, Integer>("apartmentNumber"));
        tenant.setCellValueFactory(new PropertyValueFactory<Apartment, String>());
        area.setCellValueFactory(new PropertyValueFactory<Apartment, Double>("area"));
    }


}
