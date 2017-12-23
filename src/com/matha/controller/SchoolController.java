package com.matha.controller;

import java.io.IOException;
import java.util.List;

import com.matha.Main;
import com.matha.domain.State;
import com.matha.domain.School;
import com.matha.service.SchoolService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SchoolController {

	String fxmlFile = "../view/Schools.fxml";
	FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
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
    protected void initialize()
    {
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

    }

    @FXML
    void openSchool(ActionEvent event) {

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

}
