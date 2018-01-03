package com.matha.controller;

import static com.matha.util.LoadUtils.loadPopupScene;

import java.io.IOException;
import java.util.List;

import com.matha.Main;
import com.matha.domain.School;
import com.matha.domain.State;
import com.matha.service.SchoolService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SchoolController {

	private final String fxmlFile = "../view/Schools.fxml";
	private final FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

	private final String addSchoolFxmlFile = "../view/addSchool.fxml";
	private final FXMLLoader addSchoolLoader = new FXMLLoader(getClass().getResource(addSchoolFxmlFile));

	private final String schoolDetFxmlFile = "../view/SchoolDetails.fxml";
	private final FXMLLoader schoolDetailLoader = new FXMLLoader(getClass().getResource(schoolDetFxmlFile));

	Parent root;

	@FXML
	private TextField city;

	@FXML
	private TextField phone;

	@FXML
	private ChoiceBox<String> district;

	@FXML
	private TextField schoolName;

	private ObservableList<School> data;

	@FXML
	private TableView<School> tableView;

	@FXML
	private ChoiceBox<State> states;

	@FXML
	protected void initialize() {
		SchoolService srvc = Main.getContext().getBean(SchoolService.class);
		List<School> schools = srvc.fetchSchoolsLike("");
		data = tableView.getItems();
		for (School school : schools) {
			data.add(school);
		}

		List<State> statesList = srvc.fetchAllStates();
		ObservableList<State> stateObsList = FXCollections.observableList(statesList);
		states.setItems(stateObsList);
	}

	@FXML
	void nameSearch(ActionEvent event) {

	}

	@FXML
	void citySearch(ActionEvent event) {

	}

	@FXML
	void addSchool(ActionEvent event) {

		try {
			root = addSchoolLoader.load();
//			Main.loadScene(new Scene(root));
			AnchorPane aPane = (AnchorPane) ((Node)event.getSource()).getParent();
			Scene scene = new Scene(root);
			
			loadPopupScene(aPane.getScene().getWindow(), scene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void openSchool(ActionEvent event) throws IOException {
		
		School school = tableView.getSelectionModel().getSelectedItem();
		root = schoolDetailLoader.load();
		SchoolDetailsController ctrl = schoolDetailLoader.getController();
		ctrl.initData(school);
		
//		SplitPane one = (SplitPane) ((Node)event.getSource()).getParent().getParent().getParent();
		
//		AnchorPane aPane = Main.getMainPane();
		SplitPane aPane = (SplitPane) ((Node)event.getSource()).getParent().getParent().getParent();
		Scene scene = new Scene(root, aPane.getWidth(), aPane.getHeight());
//		Stage stg = (Stage) one.getScene().getWindow();
//		stg.setScene(scene);
//		Scene scene = new Scene(root);
//		Main.loadScene(scene);
//		aPane.getChildrenUnmodifiable().clear();
//		aPane.getChildrenUnmodifiable().add(scene);
		
		loadPopupScene(aPane.getScene().getWindow(), scene);
	}

	@FXML
	void deleteSchool(ActionEvent event) {

	}

	@FXML
	void loadSales(ActionEvent event) {
		try {
			root = loader.load();
			SchoolService srvc = Main.getContext().getBean(SchoolService.class);
			List<School> schools = srvc.fetchSchoolsLike("y");
			data = tableView.getItems();
			System.out.println(data);
			for (School school : schools) {
				data.add(school);
			}
			Main.loadScene(new Scene(root));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void changeDistrict(ActionEvent evt) {
		List<String> distList = states.getSelectionModel().getSelectedItem().getDistricts();
		district.setItems(FXCollections.observableList(distList));
	}

}
