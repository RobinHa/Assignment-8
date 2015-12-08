package Simple2D3D;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Simple2D3DViewer extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        Simple2D3DStructure view = new Simple2D3DStructure();
        Scene scene = view.buildScene();

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
