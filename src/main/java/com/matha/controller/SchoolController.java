package com.matha.controller;

import static com.matha.util.UtilConstants.NEW_LINE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.matha.domain.District;
import com.matha.domain.School;
import com.matha.domain.State;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

@Controller
public class SchoolController {

	private final String addSchoolFxmlFile = "/fxml/addSchool.fxml";

	private Parent root;

	// @Autowired
	// FXMLLoader fxmlLoader;

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
	protected void initialize() {

		// getClass().getClassLoader().
		// SchoolService srvc = Main.getContext().getBean(SchoolService.class);
		
		district.setConverter(Converters.getDistrictConverter());
		states.setConverter(Converters.getStateConverter());
		this.initData();
	}

	private void initData() {
		List<School> schools = srvc.fetchAllSchools();
		tableView.setItems(FXCollections.observableList(schools));

		List<State> statesList = srvc.fetchAllStates();		
		ObservableList<State> stateObsList = FXCollections.observableList(statesList);
		states.setItems(stateObsList);
	}

	@FXML
	void nameSearch(KeyEvent event) {

		String schoolNamePart = this.schoolName.getText() + event.getCharacter();
		String pin = this.pin.getText();

		loadSchools(schoolNamePart, pin);

	}

	@FXML
	void pinSearch(KeyEvent event) {
		String schoolNamePart = this.schoolName.getText();
		String pin = this.pin.getText() + event.getCharacter();

		loadSchools(schoolNamePart, pin);
	}

	private void loadSchools(String schoolNamePart, String pin) {

		System.out.println(this.states.getSelectionModel().getSelectedItem() + " - "
				+ this.district.getSelectionModel().getSelectedItem() + " - " + schoolNamePart + " - " + pin);
		List<School> schools = srvc.fetchByStateAndPart(this.states.getSelectionModel().getSelectedItem(),
				this.district.getSelectionModel().getSelectedItem(), schoolNamePart, null, pin);
		tableView.setItems(FXCollections.observableList(schools));

	}

	@FXML
	void addSchool(ActionEvent ev) {

		try {
			FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, addSchoolFxmlFile);
			root = fxmlLoader.load();
			Scene scene = new Scene(root);

			Stage stage = LoadUtils.loadChildStage(ev, scene);
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				@Override
				public void handle(final WindowEvent event) {
					System.out.println("Calling close");
					initData();
				}
			});
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void editSchool(ActionEvent event) {

		try {
			FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, addSchoolFxmlFile);
			root = fxmlLoader.load();
			Scene scene = new Scene(root);

			AddSchoolController ctrl = fxmlLoader.getController();
			ctrl.initEdit(tableView.getSelectionModel().getSelectedItem());

			Stage stage = LoadUtils.loadChildStage(event, scene);
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				@Override
				public void handle(final WindowEvent event) {
					System.out.println("Calling close");
					initData();
				}
			});
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openSchool(ActionEvent event) throws IOException {

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
	void deleteSchool(ActionEvent event) {

		School selectedOrder = tableView.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Order Confirmation");
		alert.setHeaderText("Are you sure you want to delete the school: " + selectedOrder.getName() + NEW_LINE
				+ selectedOrder.getAddress1());
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			srvc.deleteSchool(selectedOrder);
			initData();
		}
	}

	@FXML
	void changedState(ActionEvent evt) {
		
		Set<District> districtSet = this.states.getSelectionModel().getSelectedItem().getDistricts();		
		List<District> distList = new ArrayList<>(districtSet);
		this.district.setItems(FXCollections.observableList(distList));
		
		String schoolNamePart = this.schoolName.getText();
		String pin = this.pin.getText();

		loadSchools(schoolNamePart, pin);
	}

	@FXML
	void changedDistrict(ActionEvent evt) {

		String schoolNamePart = this.schoolName.getText();
		String pin = this.pin.getText();

		loadSchools(schoolNamePart, pin);
	}
}
