/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhopratico;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.scene.media.MediaPlayer.Status.PLAYING;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private Button btn_play;
    
    @FXML
    private Button btn_stop;
    
     @FXML
    private MediaView mv;
     
     MediaPlayer mediaplayer;
  
 

     
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        String VUrl = "file:/C:/Users/ferna/Alquerque.mp4";
        //String VUrl = "file:/C:/Users/35191/Documents/Alquerque.mp4";
        Media media = new Media(VUrl);
        mediaplayer = new MediaPlayer(media);
        mv.setFitHeight(400);
        mv.setFitWidth(300);
        mv.setMediaPlayer(mediaplayer);
    }    
    @FXML
    private void onClick_btn_stop(){
        mediaplayer.stop();
    }
    
    @FXML
    private void onClick_btn_play(){
        
        if (mediaplayer.getStatus()==PLAYING){
           mediaplayer.stop(); 
            mediaplayer.play();
        }else {
        mediaplayer.play();
    }
    
}}
