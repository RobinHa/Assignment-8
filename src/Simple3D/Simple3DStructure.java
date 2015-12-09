package Simple3D;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
/**
 * Created by Robin on 08.12.2015.
 */
public class Simple3DStructure {

    private final Group root = new Group();
    private final Xform world = new Xform();

    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();
    private final Xform cameraXform3 = new Xform();
    private final double cameraDistance = 450;

    private final Group groupAxis = new Group();

    private final Group objectGroup = new Group();

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    /**
     * Basic constructor
     */
    public Simple3DStructure() {
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
        camera.setTranslateZ(-cameraDistance*2);
        cameraXform.ry.setAngle(270.0);
        cameraXform.rx.setAngle(40);
    }

    /**
     * https://docs.oracle.com/javafx/8/3d_graphics/sampleapp.htm
     */
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


    /**
     * simple Object creation, put together into a group
     */
    private void buildObjects(){

        Xform tree1 = createChristmasTree(-50, -20, -50, 2);
        Xform tree2 = createChristmasTree(60, 5, -40, 1.3);
        Xform christmasBall1 = createChristmasBall(60,60,10,0.3);
        Xform christmasBall2 = createChristmasBall(-50,50,50,1.0);

        objectGroup.getChildren().addAll(christmasBall1, christmasBall2, tree1, tree2);
        world.getChildren().add(objectGroup);
    }

    private void handleMouse(Scene scene, final Node root) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;
                double modifierFactor = 0.1;

                if (me.isShiftDown()){
                    modifier = 10.0;
                }
                if (me.isControlDown()) {
                    modifier = .1;
                }
                if (me.isPrimaryButtonDown() || me.isSecondaryButtonDown()) {
                    if(me.isShiftDown()){
                        double z = camera.getTranslateZ();
                        double newZ = z + mouseDeltaX*modifierFactor*modifier;
                        camera.setTranslateZ(newZ);
                    }else {
                        cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX * modifierFactor * modifier * 2.0);  // +
                        cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY * modifierFactor * modifier * 2.0);  // -
                    }
                }
                else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX*modifierFactor*modifier*0.3);  // -
                    cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY*modifierFactor*modifier*0.3);  // -
                }
            }
        });
    }

    /**
     * Builds a Scene for presantation
     * @return
     */
    public Scene buildScene(){
        Scene scene = new Scene(root, 600, 600, true);
        scene.setFill(Color.GRAY);
        scene.setCamera(camera);
        handleMouse(scene, world);
        return scene;
    }

    /**
     * creates a tree Xform
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param scale scale
     * @return
     */
    public Xform createChristmasTree(double x, double y, double z, double scale){

        final PhongMaterial treeMaterial = new PhongMaterial();
        treeMaterial.setDiffuseColor(Color.DARKGREEN);
        treeMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial tribeMaterial = new PhongMaterial();
        tribeMaterial.setDiffuseColor(Color.BROWN);
        tribeMaterial.setSpecularColor(Color.ROSYBROWN);

        Xform tree = new Xform();

        Double treeHeight = 100.0;

        Cylinder treePart1 = new Cylinder();
        treePart1.setHeight(treeHeight);
        treePart1.setRadius(5);
        treePart1.setMaterial(treeMaterial);
        treePart1.setTranslateY(treeHeight/2+10);

        Cylinder treePart2 = new Cylinder();
        treePart2.setHeight(treeHeight-30);
        treePart2.setRadius(10);
        treePart2.setMaterial(treeMaterial);
        treePart2.setTranslateY((treeHeight-30)/2 +10 );

        Cylinder treePart3 = new Cylinder();
        treePart3.setHeight(treeHeight-60);
        treePart3.setRadius(15);
        treePart3.setMaterial(treeMaterial);
        treePart3.setTranslateY((treeHeight-60)/2 +10);

        Cylinder stem = new Cylinder();
        stem.setHeight(treeHeight);
        stem.setRadius(4);
        stem.setMaterial(tribeMaterial);
        stem.setTranslateY(treeHeight/2);

        tree.getChildren().addAll(treePart1, treePart2, treePart3, stem);
        tree.setTranslate(x,y,z);
        tree.setScale(scale, scale/2, scale);

        return tree;
    }

    public Xform createChristmasBall(double x, double y, double z, double scale){

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial goldMaterial = new PhongMaterial();
        goldMaterial.setDiffuseColor(Color.DARKGOLDENROD);
        goldMaterial.setSpecularColor(Color.GOLDENROD);

        Box cube = new Box();
        cube.setDepth(60);
        cube.setHeight(60);
        cube.setWidth(60);
        cube.setMaterial(blueMaterial);
        cube.setTranslateX(50.0);
        cube.setTranslateZ(-40.0);

        Xform christmasBall = new Xform();

        Sphere sphere= new Sphere();
        sphere.setRadius(55);
        sphere.setMaterial(redMaterial);
        sphere.setTranslateX(0.0);

        Cylinder cylinder1 = new Cylinder();
        cylinder1.setHeight(10);
        cylinder1.setRadius(15);
        cylinder1.setMaterial(goldMaterial);
        cylinder1.setTranslateY(55);

        Cylinder cylinder2 = new Cylinder();
        cylinder2.setHeight(30);
        cylinder2.setRadius(3);
        cylinder2.setMaterial(goldMaterial);
        cylinder2.setTranslateY(55);

        christmasBall.getChildren().addAll(cylinder1, cylinder2, sphere);
        christmasBall.setTranslate(x, y, z);
        christmasBall.setScale(scale,scale,scale);

        return christmasBall;

    }

}
