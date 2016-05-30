package application;
// V1.08
// import application.MyFlightController;
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
    // behebt windows 10 Fehler f�r Combobox
    	System.setProperty("glass.accessible.force", "false");
	// l�dt MyFlight.fxml -datei 
    	try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("MyFlight.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			application.MyFlightController controller = loader.getController();
	
	// definiert Gr��e des Anwendungsfensters		
			Scene scene = new Scene(root, 800, 600);
	// l�dt CSS-Datei f�r Designvorgaben		
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// Scene scene = new Scene(root,800,600);
			primaryStage.setScene(scene);
			primaryStage.setTitle("MyFlight!");

			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}