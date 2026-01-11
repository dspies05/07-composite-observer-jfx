package ohm.softa.a07.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import ohm.softa.a07.api.OpenMensaAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import javafx.application.Platform;
import ohm.softa.a07.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.LinkedList;
import java.util.List;

public class MainController implements Initializable {
	Retrofit retrofit = new Retrofit.Builder()
							.baseUrl("https://openmensa.org/api/v2/")
							.addConverterFactory(GsonConverterFactory.create())
							.build();

	OpenMensaAPI api = retrofit.create(OpenMensaAPI.class);

	// use annotation to tie to component in XML
	@FXML
	private Button btnRefresh;
	@FXML
	private Button btnClose;
	@FXML
	private CheckBox chkVegetarian;

	private ObservableList<String> obsList;

	@FXML
	private ListView<String> mealsList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set the event handler (callback)
		btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// create a new (observable) list and tie it to the view
				try{
					if(obsList == null){
						List<Meal> meals = api.getMeals(269, "2023-11-20").execute().body();
						List<String> mealStrings = new LinkedList<>();
						for (Meal m: meals){
							mealStrings.add(m.toString());
						}
						obsList = FXCollections.observableArrayList(mealStrings);
					}
					mealsList.setItems(obsList);
				}
				catch(IOException e){

				}
			}
		});

		btnClose.setOnAction(event->{
			Platform.exit();
		});

		chkVegetarian.setOnAction(event ->{
			if (event.getSource() instanceof CheckBox){
				CheckBox chk = (CheckBox) event.getSource();
				if(chk.isSelected()){
					try{
						List<Meal> meals = api.getMeals(269, "2023-11-20").execute().body();
						List<String> mealStrings = new LinkedList<>();
						for (Meal m: meals){
							if(m.getCategory().equals("Vegetarisch") || m.getCategory().equals("Vegan")){
								mealStrings.add(m.toString());
							}
						}
						obsList = FXCollections.observableArrayList(mealStrings);
					}
					catch(IOException e){

					}
				}
				else{
					try{
						List<Meal> meals = api.getMeals(269, "2023-11-20").execute().body();
						List<String> mealStrings = new LinkedList<>();
						for (Meal m: meals){
							mealStrings.add(m.toString());
						}
						obsList = FXCollections.observableArrayList(mealStrings);
					}
					catch(IOException e){

					}
				}
			}
			
		});
	}
}
