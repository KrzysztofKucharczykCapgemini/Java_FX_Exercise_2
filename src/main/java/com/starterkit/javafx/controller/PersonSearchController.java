package com.starterkit.javafx.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.starterkit.javafx.texttospeech.Speaker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PersonSearchController {

	private static final Logger LOG = Logger.getLogger(PersonSearchController.class);

	private File folder = new File("");

	private String[] files;
	
	@FXML
	private AnchorPane anchorPane;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField titleField;

	@FXML
	private TextField authorsField;

	@FXML
	private Button searchButton;

	@FXML
	private ListView<String> list = new ListView<>();
	
	@SuppressWarnings("unused")
	private final Speaker speaker = Speaker.INSTANCE;

	public PersonSearchController() {
		LOG.debug("Constructor");
	}

	@FXML
	private void initialize() {
		LOG.debug("initialize()");
		list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String imageLocalization = "file:\\\\\\" + folder.getAbsolutePath() + "\\";
				int pictureValue = 0;
				
				for(int i = 0; i< files.length; i++) {
					if(newValue.equals(files[i]))
						pictureValue = i;
				}
					
				try {
					showImageDialog(pictureValue, imageLocalization, files);

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	private Stage showImageDialog(int index, String filepath, String[] files) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/starterkit/javafx/view/imageViewer.fxml"));
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setHeight(500);
		stage.setWidth(600);
		stage.setResizable(false);
		stage.setScene(new Scene((Pane) loader.load()));
		ImageController controller = loader.<ImageController>getController();
		controller.initData(index, filepath, files);
		stage.show();
		
		return stage;
	}

	private static void configureFileChooser(final DirectoryChooser fileChooser) {
		fileChooser.setTitle("View Pictures");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	}

	@FXML
	private void searchButtonAction(ActionEvent event) {
		LOG.debug("'Search' button clicked");
		Stage searchButtonStage = (Stage) searchButton.getScene().getWindow();
		DirectoryChooser directoryChooser = new DirectoryChooser();
		configureFileChooser(directoryChooser);
		directoryChooser.setTitle("Open Resource File");
		folder = directoryChooser.showDialog(searchButtonStage);

		File[] d = folder.listFiles();
		List<String> b = new ArrayList<>();

		for (File c : d) {
			if (c.getName().contains(".") && c.getName().charAt(0) != '.') {
				if (c.getName().contains(".png") || c.getName().contains(".jpg"))
					b.add(c.getName());
			}
		}

		ObservableList<String> q = FXCollections.observableArrayList(b);
		list.setItems(q);
		
		files = new String[q.size()];
		
		for(int i = 0; i < q.size(); i++)
			files[i] = q.get(i);
	}

}
