package org.example.mainappui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.service.Bill;
import org.example.service.Loggable;
import org.example.service.TenantApp;

import java.io.IOException;
import java.util.List;

public class TenantController {

    private static TenantApp tenant;

    @FXML
    private Label heatSystemText;
    @FXML
    private Button updateBillsButton;
    @FXML
    private TextField insertBillId;
    @FXML
    private Button payBillButton;
    @FXML
    private Button logOutButton;
    @FXML
    private TableView<Bill> bills;
    @FXML
    private TableColumn<Bill, String> billNumberColumn;
    @FXML
    private TableColumn<Bill, String> toPayColumn;
    @FXML
    private TableColumn<Bill, String> statusColumn;
    @FXML
    private TableColumn<Bill, String> usedKWHColumn;
    @FXML
    private TableColumn<Bill, String> pricePerKWHColumn;
    @FXML
    private TableColumn<Bill, String> dueDateColumn;
    @FXML
    private TableColumn<Bill, String> buildingNumberColumn;
    @FXML
    private TableColumn<Bill, String> apartmentNumberColumn;

    @FXML
    public void initialize() {
        toPayColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        usedKWHColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        pricePerKWHColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerKwh"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        buildingNumberColumn.setCellValueFactory(new PropertyValueFactory<>("buildingNumber"));
        apartmentNumberColumn.setCellValueFactory(new PropertyValueFactory<>("apartmentNumber"));
        billNumberColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    public static Loggable getTenant() {
        return tenant;
    }

    @FXML
    protected void onClickUpdateBills() {
        List<Bill> billList = tenant.getBills();
        bills.setItems(FXCollections.observableArrayList(billList));
    }

    public static void setTenant(TenantApp tenant) {
        TenantController.tenant = tenant;
    }

    @FXML
    protected void onClickPayBill() {
        int billId = Integer.parseInt(insertBillId.getText());
        tenant.payBill(billId);
    }

    @FXML
    protected void onClickLogOut() throws IOException {
        switchToLoginScene();
        tenant.logout();
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
