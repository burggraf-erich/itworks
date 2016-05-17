package application;

import application.MyFlightController;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("MyFlight.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			application.MyFlightController controller = loader.getController();
			// controller.setMyFlightstart(this);

			// AnchorPane root =
			// (AnchorPane)FXMLLoader.load(getClass().getResource("MyFlightV2.fxml"));
			Scene scene = new Scene(root, 800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// Scene scene = new Scene(root,800,600);
			primaryStage.setScene(scene);
			primaryStage.setTitle("MyFlight!");

			/*
			 * //btn.setOnAction(new EventHandler<ActionEvent>() {
			 * 
			 * @Override public void handle(ActionEvent event) {
			 * System.out.println("Hello World!"); } });
			 */

			// StackPane root = new StackPane();
			// root.getChildren().add(btn);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}