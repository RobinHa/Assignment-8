package Simple3D;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Simple3DViewer extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        Simple3DStructure view = new Simple3DStructure();
        Scene scene = view.buildScene();

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
