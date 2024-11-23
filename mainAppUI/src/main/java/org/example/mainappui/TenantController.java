package org.example.mainappui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.service.Bill;
import org.example.service.Loggable;
import org.example.service.TenantApp;

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




}
