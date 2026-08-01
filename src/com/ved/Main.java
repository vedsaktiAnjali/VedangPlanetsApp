package com.ved;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.FileReader;
import  java.io.InputStreamReader;


public class Main extends Application {

    Pane root = new Pane();
   Boolean iFlag = true;
  
ArrayList<ImageView> planets = new ArrayList<>();
ArrayList<Double> radius = new ArrayList<>();
ArrayList<Double> angle = new ArrayList<>();
ArrayList<Double> speed = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {

        root.setPrefSize(900,700);
        Button pauseBtn = new Button("Pause");
        

        pauseBtn.setLayoutX(20);
        pauseBtn.setLayoutY(20);
    
        Label info = new Label("Click a planet");

        info.setLayoutX(20);
        info.setLayoutY(60);

        // Sun
        Circle sun = new Circle(450,350,30);
        sun.setFill(Color.GOLD);

        root.getChildren().add(sun);
       
        root.getChildren().add(pauseBtn);
        root.getChildren().add(info);

        BufferedReader br = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/planets.txt")));

        String line;
        int iCounter =0;

        while((line=br.readLine())!=null){

            String[] p=line.split("\\|");

             String name=p[0];
 //System.out.println("plnet name read frm file "+ name);
            double orbit=Double.parseDouble(p[1]);
            int days[] = {
    88,      // Mercury
    225,     // Venus
    365,     // Earth
    687,     // Mars
    4333,    // Jupiter
    10759,   // Saturn
    30687,   // Uranus
    60190    // Neptune
};
            String imgFile=p[3];

            //Image img=new Image("file:images/"+imgFile);
            //System.out.println("plnet imge read frm file nme "+ imgFile);
            //Image img= new Image(getClass().getResourceAsStream("/images/"+imgFile));
            Image img= new Image(Main.class.getResource("/images/"+imgFile).toString());
            ImageView iv=new ImageView(img);
            System.out.println(name + " error = " + img.isError());
            String fact = p[4];

            iv.setFitWidth(30);
            iv.setFitHeight(30);
            if (iCounter == 0) {
                
            iv.setFitWidth(14);
            iv.setFitHeight(14);
            }
            iv.setLayoutX(450+orbit);

            iv.setLayoutY(350);

            root.getChildren().add(iv);
            planets.add(iv);

            radius.add(orbit);

            angle.add(0.0);
            speed.add(90.0 / Math.cbrt(days[iCounter]));
            //speed.add(360.0 / Math.cbrt(days[iCounter]));
            iCounter++;
            iv.setOnMouseEntered(e -> {

    info.setText(name + "\n" +"orbit:" +orbit + "\n" +fact + "\n" +"Nikku Yajju Aashi Ved");

});
     

        }

        br.close();
       AnimationTimer timer =  new AnimationTimer() {
    private long lastUpdate = 0;
    @Override
    public void handle(long now) {
         if (now - lastUpdate < 100_000_000L) {
            return;      // wait until 0.5 second has passed
        }

        lastUpdate = now;

            
        for (int i = 0; i < planets.size(); i++) {

            angle.set(i, angle.get(i) + speed.get(i));

            double x = 450 + radius.get(i) *
                    Math.cos(Math.toRadians(angle.get(i)));

            double y = 350 + radius.get(i) *
                    Math.sin(Math.toRadians(angle.get(i)));
                   
   // System.out.println(x + " , " + y+","+ i);


            planets.get(i).setLayoutX(x);

            planets.get(i).setLayoutY(y);



        }

    }

};
        timer.start();

        pauseBtn.setOnAction(e -> {
    if (iFlag == true )
        {
         timer.stop();
        iFlag = false;
        pauseBtn.setText("Start");
        }
            else 
            {
        timer.start();
        iFlag = true;
                pauseBtn.setText("Pause");
                
            }

});

        Scene scene=new Scene(root);

        stage.setTitle("Solar System");

        stage.setScene(scene);

        stage.show();

    }

    public static void main(String[] args){

        launch();

    }

}
