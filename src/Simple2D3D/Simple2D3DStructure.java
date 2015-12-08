package Simple2D3D;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Window;

/**
 * Created by Robin on 08.12.2015.
 */
public class Simple2D3DStructure {

    private final Group root = new Group();
    private final SubScene bottomLayer = new SubScene(root, 500, 500, true, SceneAntialiasing.DISABLED);

    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Xform world = new Xform();
    private final Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();
    private final Xform cameraXform3 = new Xform();
    private final double cameraDistance = 450;

    private final Group groupAxis = new Group();
    private final Group objectGroup = new Group()
            ;
    private final Pane topLayer = new Pane();
    private final Group rectangleGroup = new Group();

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    /**
     * Basic constructor
     */
    public Simple2D3DStructure() {
        buildRoot();
        buildCamera();
        buildAxes();
        buildObjects();
    }

    private void buildRoot() {
        root.getChildren().add(world);
    }

    /**
     * creates the camera for the scene
     */
    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-cameraDistance * 2);
        cameraXform.ry.setAngle(270.0);
        cameraXform.rx.setAngle(40);
    }

    private void buildAxes() {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(240.0, 1, 1);
        final Box yAxis = new Box(1, 240.0, 1);
        final Box zAxis = new Box(1, 1, 240.0);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        groupAxis.getChildren().addAll(xAxis, yAxis, zAxis);
        world.getChildren().addAll(groupAxis);
    }


    private void buildObjects() {
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial cyanMaterial = new PhongMaterial();
        cyanMaterial.setDiffuseColor(Color.DARKCYAN);
        cyanMaterial.setSpecularColor(Color.CYAN);

        Box cube = new Box();
        cube.setDepth(60);
        cube.setHeight(60);
        cube.setWidth(60);
        cube.setMaterial(blueMaterial);
        cube.setTranslateX(50.0);
        cube.setTranslateZ(-40.0);

        Sphere sphere = new Sphere();
        sphere.setRadius(55);
        sphere.setMaterial(redMaterial);
        sphere.setTranslateX(-50.0);
        sphere.setTranslateZ(60.0);

        Cylinder cylinder = new Cylinder();
        cylinder.setHeight(90);
        cylinder.setRadius(20);
        cylinder.setMaterial(cyanMaterial);
        cylinder.setTranslateX(0.0);
        cylinder.setRotationAxis(Rotate.Z_AXIS);
        cylinder.setRotate(45.0);
        cylinder.setRotationAxis(Rotate.X_AXIS);
        cylinder.setRotate(-45.0);

        Button button3D = new Button("sample text");

        objectGroup.getChildren().addAll(cube, cylinder, sphere);
        world.getChildren().add(objectGroup);
    }

    private void handleMouse(Scene scene, final Node root) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;
                double modifierFactor = 0.1;

                if (me.isShiftDown()) {
                    modifier = 10.0;
                }
                if (me.isControlDown()) {
                    modifier = .1;
                }
                if (me.isPrimaryButtonDown() || me.isSecondaryButtonDown()) {
                    if (me.isShiftDown()) {
                        double z = camera.getTranslateZ();
                        double newZ = z + mouseDeltaX * modifierFactor * modifier;
                        camera.setTranslateZ(newZ);
                    } else {
                        cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX * modifierFactor * modifier * 2.0);  // +
                        cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY * modifierFactor * modifier * 2.0);  // -
                    }
                } else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
                    cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
                }
            }
        });

        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                for (Node n : rectangleGroup){
                    
                }
            }
        });
    }

    /**
     * Builds a Scene for presantation
     *
     * @return
     */
    public Scene buildScene() {
        Scene scene = new Scene(root, 600, 600, true);
        scene.setFill(Color.GRAY);
        scene.setCamera(camera);
        handleMouse(scene, world);
        return scene;
    }

    public static javafx.scene.shape.Rectangle getBoundingBox2D(Node shape) {
        final Window window = shape.getScene().getWindow();
        final Bounds bounds = shape.getBoundsInLocal();
        final Bounds screenBounds = shape.localToScreen(bounds);
        javafx.scene.shape.Rectangle rectangle = new javafx.scene.shape.Rectangle
                ((int) Math.round(screenBounds.getMinX() - window.getX()),
                        (int) Math.round(screenBounds.getMinY() - window.getY()) - 20,
                        (int) Math.round(screenBounds.getWidth()),
                        (int) Math.round(screenBounds.getHeight()));
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.LIGHTBLUE);
        rectangle.setMouseTransparent(true);
        return rectangle;
    }
}