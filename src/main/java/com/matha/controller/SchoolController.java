package com.matha.controller;

import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.showConfirmation;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.util.*;

import com.matha.domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.matha.service.SchoolService;
import com.matha.util.Converters;
import com.matha.util.LoadUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

@Controller
public class SchoolController
{

	private static final Logger LOGGER = LogManager.getLogger(SchoolController.class);
	
	private Parent root;

	@Value("${defaultState}")
	private String defaultState;

	@Autowired
	SchoolService srvc;

	@FXML
	private TextField pin;

	@FXML
	private TextField phone;

	@FXML
	private ChoiceBox<District> district;

	@FXML
	private TextField schoolName;

	@FXML
	private TableView<School> tableView;

	@FXML
	private ChoiceBox<State> states;

	@FXML
	protected void initialize()
	{
		district.setConverter(Converters.getDistrictConverter());
		states.setConverter(Converters.getStateConverter());
		this.initData();

		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
				{
					if (mouseEvent.getClickCount() == 2)
					{
						openSchool(mouseEvent);
					}
				}
			}

			private void openSchool(MouseEvent mouseEvent)
			{
				try
				{
					FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, "/fxml/SchoolDetails.fxml");
					root = fxmlLoader.load();
					School school = tableView.getSelectionModel().getSelectedItem();
					SchoolDetailsController ctrl = fxmlLoader.getController();
					ctrl.initData(school);

					SplitPane aPane = (SplitPane) ((Node) mouseEvent.getSource()).getParent().getParent().getParent();
					Scene scene = new Scene(root, aPane.getWidth(), aPane.getHeight());
					Stage stage = LoadUtils.loadChildStage(mouseEvent, scene);
					stage.show();
				}
				catch (Exception e)
				{
					LOGGER.error("Error...", e);
					e.printStackTrace();
				}
			}
		});

		List<State> allStates = srvc.fetchAllStates();
		State state = allStates.get(0);
		if(defaultState != null)
		{
			Optional<State> stateOpt = allStates.stream().filter(st -> st.getId().equalsIgnoreCase(defaultState)).findFirst();
			state = stateOpt.get();
		}
		states.getSelectionModel().select(state);
	}

	private void initData()
	{
		List<School> schools = srvc.fetchAllSchools();
		tableView.setItems(FXCollections.observableList(schools));

		List<State> statesList = srvc.fetchAllStates();
		ObservableList<State> stateObsList = FXCollections.observableList(statesList);
		states.setItems(stateObsList);
	}

	@FXML
	void nameSearch(KeyEvent event)
	{
		String schText = this.schoolName.getText();
		String schoolNamePart = schText;
		if(event != null && event.getCharacter() != null && !event.getCharacter().isEmpty())
		{
			char charVal = event.getCharacter().charAt(0);
			int intVal = event.getCharacter().codePointAt(0);
			if(charVal > 31 && charVal < 127)
			{
				schoolNamePart = schText + charVal;
			}
		}
		String pin = this.pin.getText();

		loadSchools(schoolNamePart, pin);

	}

	@FXML
	void pinSearch(KeyEvent event)
	{
		String schoolNamePart = this.schoolName.getText();
		String pin = this.pin.getText() + event.getCharacter();

		loadSchools(schoolNamePart, pin);
	}

	private void loadSchools(String schoolNamePart, String pin)
	{

		LOGGER.info(this.states.getSelectionModel().getSelectedItem() + " - " + this.district.getSelectionModel().getSelectedItem() + " - " + schoolNamePart + " - " + pin);
		List<School> schools = srvc.fetchByStateAndPart(this.states.getSelectionModel().getSelectedItem(), this.district.getSelectionModel().getSelectedItem(), schoolNamePart, null, pin);
		tableView.setItems(FXCollections.observableList(schools));

	}

	@FXML
	void addSchool(ActionEvent ev)
	{

		try
		{
			FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, addSchoolFxmlFile);
			root = fxmlLoader.load();
			Scene scene = new Scene(root);

			Stage stage = LoadUtils.loadChildStage(ev, scene);
			stage.setOnHiding(event -> {
				nameSearch(null);
			});
			stage.show();
		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void editSchool(ActionEvent event)
	{

		try
		{
			FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, addSchoolFxmlFile);
			root = fxmlLoader.load();
			Scene scene = new Scene(root);

			AddSchoolController ctrl = fxmlLoader.getController();
			ctrl.initEdit(tableView.getSelectionModel().getSelectedItem());

			Stage stage = LoadUtils.loadChildStage(event, scene);
			stage.setOnHiding(ev -> nameSearch(null) );
			stage.show();
		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void openSchool(ActionEvent event) throws IOException
	{

		FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, "/fxml/SchoolDetails.fxml");
		root = fxmlLoader.load();
		School school = tableView.getSelectionModel().getSelectedItem();
		SchoolDetailsController ctrl = fxmlLoader.getController();
		ctrl.initData(school);

		SplitPane aPane = (SplitPane) ((Node) event.getSource()).getParent().getParent().getParent();
		Scene scene = new Scene(root, aPane.getWidth(), aPane.getHeight());
		Stage stage = LoadUtils.loadChildStage(event, scene);
		stage.show();
	}

	@FXML
	void deleteSchool(ActionEvent event)
	{

		School selectedOrder = tableView.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Order Confirmation");
		alert.setHeaderText("Are you sure you want to delete the school: " + selectedOrder.getName() + NEW_LINE + selectedOrder.getAddress1());
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			List<Sales> bills = srvc.fetchBills(selectedOrder);
			List<SchoolReturn> returns = srvc.fetchReturnsForSchool(selectedOrder);
			List<SchoolPayment> payments = srvc.fetchPayments(selectedOrder);

			List<Order> ordersIn = srvc.fetchOrderForSchool(selectedOrder);
			List<PurchaseDet> purchasesIn = srvc.fetchPurDetForOrders(ordersIn);
//			Set<PurchaseDet> purchasesIn = ordersIn.stream().map(o -> o.getOrderItem()).flatMap(List::stream).map(oi -> oi.getPurchaseDet()).flatMap(Set::stream).collect(toSet());

			if((bills != null && !bills.isEmpty()) ||
					(returns != null && !returns.isEmpty()) ||
					(payments != null && !payments.isEmpty()) ||
					(purchasesIn != null && !purchasesIn.isEmpty()))
			{
				if(!showConfirmation("Bill/Return/Payment transactions available",
						"There are Bill/Return/Payment transactions already created for the school getting removed. Are you sure you want to remove?",
						"Click Ok to Delete"))
				{
					return;
				}

			}

			srvc.deleteSchool(selectedOrder, ordersIn, bills, returns, payments, purchasesIn);
			initData();
			nameSearch(null);
		}
	}

	@FXML
	void changedState(ActionEvent evt)
	{
		if(this.states.getSelectionModel().getSelectedItem() != null)
		{
			Set<District> districtSet = this.states.getSelectionModel().getSelectedItem().getDistricts();
			List<District> distList = new ArrayList<>(districtSet);
			this.district.setItems(FXCollections.observableList(distList));
		}
		String schoolNamePart = this.schoolName.getText();
		String pin = this.pin.getText();

		loadSchools(schoolNamePart, pin);
	}

	@FXML
	void changedDistrict(ActionEvent evt)
	{

		String schoolNamePart = this.schoolName.getText();
		String pin = this.pin.getText();

		loadSchools(schoolNamePart, pin);
	}
}
