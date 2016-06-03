package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
	try {
	    BorderPane root = new BorderPane();
	    Scene scene = new Scene(root, 400, 400);
	    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    primaryStage.setScene(scene);
	    InfiniteWorld infiniteWorld = new InfiniteWorld();
	    root.setCenter(infiniteWorld);
	    primaryStage.show();
	    primaryStage.setTitle("Infinite World");

	    primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, evt -> {
		Cache.save();
		System.exit(0);
	    });

	    scene.widthProperty()
		    .addListener((observable, oldValue, newValue) -> infiniteWorld.setWidth((double) newValue));
	    scene.heightProperty()
		    .addListener((observable, oldValue, newValue) -> infiniteWorld.setHeight((double) newValue));
	} catch (Exception e)

	{
	    e.printStackTrace();
	}

    }

    public static void main(String[] args) {
	launch(args);
    }
}
