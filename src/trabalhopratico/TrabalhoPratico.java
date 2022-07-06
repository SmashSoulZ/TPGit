
package trabalhopratico;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.scene.media.MediaPlayer.Status.PLAYING;
import javafx.scene.text.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javax.swing.JLabel;

/**
 *
 * @author 35191
 */
public class TrabalhoPratico extends Application {
    
    public static final int Tile_Size=100;
    public static final int Width=5;
    public static final int Height=5;
    public static final Label label = new Label("padrao");
    public  Tile[][] board = new Tile[Width][Height];
    public  int x1;
    public  int y1;
    public double x1d;  
    public double y1d;
    public double oldXPC;
    public double oldYPC;
    public int oldXInt;
    public int oldYInt;
    private Group tileGroup = new Group();   
    private Group pieceGroupW = new Group();
    private Group pieceGroupB = new Group();
    
    private ClientSideConnection csc;
    private int playerID;
    private int otherPlayer; 
    private int turnsMade;
    private int player;
    
    public int idmorto;
    
 
    
    Alert a = new Alert(Alert.AlertType.NONE);
    private boolean moveDisabled = false;
    
    Button button = new Button("Ajuda");
    Button button2 = new Button("Sair");
    Button button3 = new Button("C/som");
    Button button4 = new Button("S/som");
    
    Media media = null;
    
    Label labelPec = new Label("Peças");
    Label labelBra = new Label("Brancas -  " + pieceGroupW.getChildren().size());
    Label labelPre = new Label("Pretas   -  " + pieceGroupB.getChildren().size());
    Label labelSuas = new Label("Suas Peças");
        
     Circle circleBrancas = new Circle();
     Circle circlePretas = new Circle();
    
     
     
    public void conectToServer(){
       
        csc = new ClientSideConnection(this);
        
        this.playerID = csc.getPlayerID();
        if (this.playerID==1){
            for (Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
            }
            this.otherPlayer = 2;
            
            this.label.setText("És o jogador 1. Começa a partida");
            
            
         } else {
            this.label.setText("És o jogador 2. Espere pelo seu turno");
            this.otherPlayer = 1;
           
         }
        
         
    }
    
    /**public void closeConnection(){
        try{
            socket.close();
            System.out.println("Conexão Terminada")
        } catch (IOException ex ) {
            System.out.println("IOException from closeConnection()");
        }
    }**/
     public Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(Width * Tile_Size + 120, Height * Tile_Size + 40);
        label.setLayoutY(505);
        label.setLayoutX(5);
        label.setFont(new Font ("Verdana", 18));
        
        labelSuas.setLayoutY(180);
        labelSuas.setLayoutX(510);
        labelSuas.setFont(new Font ("Verdana", 18));
        
        
        turnsMade = 0;
        
        button.setLayoutY(470);
        button.setLayoutX(505);
        button2.setLayoutY(470);
        button2.setLayoutX(560);
        button3.setLayoutY(430);
        button3.setLayoutX(505);
        button4.setLayoutY(430);
        button4.setLayoutX(560);
        
        root.getChildren().addAll(tileGroup, pieceGroupW, pieceGroupB, label,labelSuas, button, button2,button3, button4);
      
         
       
        
        
        
        for (int y = 0; y < Height; y++) {
            for (int x = 0; x < Width; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);

                Piece piece = null;
  
                if (y <= 0) {
                    piece = makePiece(PieceType.Black, x, y);
                }
                if (y >= 4) {
                    piece = makePiece(PieceType.White, x, y);
                }
                if (piece != null) {
                    tile.setPiece(piece);

                    if (tile.getPiece().getType() == PieceType.Black) {
                        //System.out.println("Peça preta com exito");
                        if(playerID == 1)
                            piece.setMoveDisabled(true);
                        else
                            piece.setMoveDisabled(false);
                        pieceGroupB.getChildren().add(piece);
                    } else {
                        //System.out.println("Peça branca com exito"); 
                        if(playerID == 2)
                            piece.setMoveDisabled(true);
                        else
                            piece.setMoveDisabled(false);
                        pieceGroupW.getChildren().add(piece);
                    }

                }

                tile.setOnMouseClicked(event -> {

                    System.out.println("------------------");
                    System.out.println("X : " + tile.getTileX());
                    System.out.println("Y : " + tile.getTileY());
                    System.out.println("------------------");

                });
            }

        }
        
        // Texto Lateral
        labelBra.setText("Brancas -  " + pieceGroupW.getChildren().size());
        labelPre.setText("Pretas   -  " + pieceGroupB.getChildren().size());
        
        labelPec.setLayoutX(510);
        labelPec.setLayoutY(10);
        labelBra.setLayoutX(510);
        labelBra.setLayoutY(50);
        labelPre.setLayoutX(510);
        labelPre.setLayoutY(90);
        labelPec.setFont(new Font("Verdana", 18));
        
        
        //Suas Peças
        if(this.playerID==1){
            circleBrancas.setCenterX(560);
            circleBrancas.setCenterY(250);
            circleBrancas.setRadius(30);
            circleBrancas.setFill(Color.WHITE);
        }else{
            circlePretas.setCenterX(560);
            circlePretas.setCenterY(250);
            circlePretas.setRadius(30); 
            circlePretas.setFill(Color.BLACK);
        }
        
        
        
        
        root.getChildren().addAll(labelPec, labelBra, labelPre, circlePretas, circleBrancas);
        
        
        return root;
    }
    
   
    
    private MoveResult tryMove(Piece piece, int newX, int newY){
        if (board[newX][newY].hasPiece()){
           System.out.println("None");
           this.label.setText("Jogada Inválida. Repete!");
           turnsMade++;
          
                       
            System.out.println("Turnos feitos: " + turnsMade);
            
//             if (this.playerID==1){
//                 
//            for (Node p : pieceGroupW.getChildren()){
//                    ((Piece)p).setMoveDisabled(true);
//                }
//                for(Node p : pieceGroupB.getChildren()){
//                    ((Piece)p).setMoveDisabled(true);
//            }}
//            if (this.playerID==2){
//            for (Node p : pieceGroupW.getChildren()){
//                    ((Piece)p).setMoveDisabled(true);
//                }
//                for(Node p : pieceGroupB.getChildren()){
//                    ((Piece)p).setMoveDisabled(true);
//            }}
//            
            return new MoveResult(MoveType.None);
            
        }
       int x0 = toBoard(piece.getOldX());
       int y0 = toBoard(piece.getOldY());
       
       if ((Math.abs(newX - x0) == 0 && Math.abs(newY - y0) == 1) || (Math.abs(newY - y0) == 0 && Math.abs(newX - x0) == 1) || (Math.abs(newX - x0) == 1 && Math.abs(newY - y0) != 2) ){
                
           this.label.setText("Fizeste a tua jogada. Espera pela jogada do jogador " + otherPlayer);
          turnsMade++;
          System.out.println("Turnos feitos: " + turnsMade);
          Media media = new Media("file:/C:/Users/35191/Documents/move.wav"); //replace /Movies/test.mp3 with your file
          MediaPlayer move = new MediaPlayer(media); 
          move.play();
            if (this.playerID==1){
            for (Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
            }}
                           if (this.playerID==2){
            for (Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
            }}
            System.out.println("Normal");
           
           return new MoveResult(MoveType.Normal);
           
       } else if (Math.abs(newY - y0) == 2 && !(Math.abs(newX - x0) == 1) || Math.abs(newX - x0) == 2 && !(Math.abs(y0 - newY) == 1)) {
                System.out.println("Pre Kill");
           this.label.setText("Fizeste a tua jogada. Espera pela jogada do jogador " + otherPlayer);
           turnsMade++;
            Media media = new Media("file:/C:/Users/35191/Documents/move.wav"); //replace /Movies/test.mp3 with your file
          MediaPlayer move = new MediaPlayer(media); 
          move.play();
            System.out.println("Turnos feitos: " + turnsMade);
             if (this.playerID==1){
            for (Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
            }}
           x1 = x0 + (newX - x0) / 2;
           y1 = y0 + (newY - y0) / 2;
           
           x1d=x1*100;  
           y1d=y1*100; 
           
            
           
           oldXPC = pieceGroupW.getChildren().get(1).getLayoutX();
           oldYPC = pieceGroupW.getChildren().get(1).getLayoutY();
           oldXInt=(int)oldXPC;
           oldYInt=(int)oldYPC;
           
                   
                   
           System.out.println("x1 - " + x1 + " e y1 - " + y1 );
           System.out.println("oldXPC " + oldXPC );
           System.out.println("oldX " + board[x0][y0].getPiece().getOldX());
           
           
                   
           System.out.println(pieceGroupW.getChildren().get(1).getLayoutX());   
           System.out.println(x1d);
           System.out.println(pieceGroupW.getChildren().get(1).getLayoutY());
           System.out.println(y1d);
           
           int q;
           if (this.playerID == 1){
              for(q = 0; q < 4; q++){
               
                if (pieceGroupB.getChildren().get(q).getLayoutX() == x1d && pieceGroupB.getChildren().get(q).getLayoutY() == y1d){
                    idmorto=q;
                }
            }
           } else {
                   for(q = 0; q < 4; q++){
               
                if (pieceGroupW.getChildren().get(q).getLayoutX() == x1d && pieceGroupW.getChildren().get(q).getLayoutY() == y1d){
                    idmorto=q;
                }
                }
              
           }
          
            System.out.println("idmorto - " + idmorto);
           //if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()){
           if ((pieceGroupW.getChildren().get(idmorto).getLayoutX() == x1d && pieceGroupW.getChildren().get(idmorto).getLayoutY() == y1d) || (pieceGroupB.getChildren().get(idmorto).getLayoutX() == x1d && pieceGroupB.getChildren().get(idmorto).getLayoutY() == y1d) ){
               System.out.println("Kill");
           this.label.setText("Fizeste a tua jogada. Espera pela jogada do jogador " + otherPlayer);
           turnsMade++;
            System.out.println("Turnos feitos: " + turnsMade);
            
               if (this.playerID==1){
            for (Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
            }}
                      if (this.playerID==2){
            for (Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
            }}
               System.out.println(" x0 - " + x0 + " e y0 - " +y0 );
               System.out.println("x1 - " + x1 + " e y1 - " +y1 );
               System.out.println("newX - " + newX + " e newY - " +newY );
               
                Media media2 = new Media("file:/C:/Users/35191/Documents/captura.mp3"); //replace /Movies/test.mp3 with your file
                MediaPlayer captura = new MediaPlayer(media2); 
                captura.play();
                enviaDados();
                
               return new MoveResult(MoveType.Kill);
               
           }
       }
       
       
      
       return new MoveResult(MoveType.None);
    }
    
    private int toBoard(double pixel){
        return (int)(pixel+Tile_Size / 2) / Tile_Size;
        
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        
        Media media4 = new Media("file:/C:/Users/35191/Documents/background.mp3"); //replace /Movies/test.mp3 with your file
        MediaPlayer player = new MediaPlayer(media4); 
        player.play();
        
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
         EventHandler<ActionEvent> Evento = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                Stage stage = new Stage();
                stage.setTitle("Ajuda");
                stage.setScene(new Scene(root1));  
                stage.show();
               
             /**  try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLAjuda.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Ajuda");
                    stage.setScene(new Scene(root1));  
                    stage.show();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                } **/
            }
        };
         
        EventHandler<ActionEvent> evento2 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
               Platform.exit();
              
          
            }
        };
        
        EventHandler<ActionEvent> evento3 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                     if (player.getStatus()==PLAYING){
                        player.stop(); 
                         player.play();
                     }else {
                     player.play();
    }
              
          
            }
        };
        
       EventHandler<ActionEvent> evento4 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
               player.stop();
              
          
            }
        };

        button.setOnAction(Evento);
        button2.setOnAction(evento2);
        button3.setOnAction(evento3);
        button4.setOnAction(evento4);
        Scene scene2 = new Scene(createContent());
        
        stage.setTitle("Alquerque");
        stage.setScene(scene2);
        stage.show();
        
        
        
    }
/**
    private Piece makePiece(PieceType type, int x, int y){
        Piece piece = new Piece(type, x, y);
        
        piece.setOnMouseReleased(e -> {
            if(!piece.isDisabled()){
            int newX = toBoard(piece.getLayoutX());
            int newY = toBoard(piece.getLayoutY());
            
            MoveResult result = tryMove(piece, newX, newY);
            
            int x0 = toBoard(piece.getOldX());
            int y0 = toBoard(piece.getOldY());
            
            switch(result.getType()){
                case None:
                    piece.abortMove();
                    break;
                case Normal:            
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                    break;
                case Kill:
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                    
                    Piece otherPiece = result.getPiece();
                    if (otherPiece.getType() == PieceType.Black){
                        board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                        pieceGroupB.getChildren().remove(otherPiece);
                    } else {
                        board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                        pieceGroupW.getChildren().remove(otherPiece);
                    }
                    atualizaDados();
                 
                    enviaDados();
                    break;
            }
            }  
        });
        
        return piece;
    }
    **/
    private Piece makePiece(PieceType type, int x, int y) {
        Piece piece = new Piece(type, x, y);

        piece.setOnMouseReleased(e -> {
            if (!piece.isMoveDisabled()) {
                int newX = toBoard(piece.getLayoutX());
                int newY = toBoard(piece.getLayoutY());

                MoveResult result = tryMove(piece, newX, newY);

                int x0 = toBoard(piece.getOldX());
                int y0 = toBoard(piece.getOldY());

                switch (result.getType()) {
                    case None:
                        piece.abortMove();
                        break;
                    case Normal:
                        piece.x = newX;
                        piece.y = newY;
                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);
                        board[newX][newY].setPiece(piece);
                        enviaDados();
                        System.out.println("MK Move Normal");
                        break;
                    case Kill:
                        piece.x = newX;
                        piece.y = newY;
                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);
                        board[newX][newY].setPiece(piece);
                        //board[oldXInt][oldYInt].setPiece(null);
                        //System.out.println("board to board oldXpc"+ oldXPC + " - " + oldYPC);
                        if (this.playerID == 1){
                            pieceGroupB.getChildren().remove(idmorto);
                        } else {
                            pieceGroupW.getChildren().remove(idmorto);
                        }
                        atualizaDados();
                        enviaDados();
                        System.out.println("MK Move Kill");
                        verificaFim(pieceGroupW.getChildren().size(), piece);
                        verificaFim(pieceGroupB.getChildren().size(), piece);
                        break;
//                    case Kill:
//                    piece.move(newX, newY);
//                    board[x0][y0].setPiece(null);
//                    board[newX][newY].setPiece(piece);
//                    
//                    Piece otherPiece = result.getPiece();
//                    if (otherPiece.getType() == PieceType.Black){
//                        board[][toBoard(otherPiece.getOldY())].setPiece(null);
//                        pieceGroupB.getChildren().remove(otherPiece);
//                    } else {
//                        board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
//                        pieceGroupW.getChildren().remove(otherPiece);
//                    }
//                    atualizaDados();
//                 
//                    enviaDados();
//                    break;

                }
            }
        });

        return piece;
    }
     
    public void verificaFim(int n,Piece piece){
        if (n == 0){ 
            a.setAlertType(Alert.AlertType.INFORMATION);
            if (piece.getType() == PieceType.White){
                a.setContentText("O Jogador 1 (Brancas) - foi o vencedor!");
            } else {
                a.setContentText("O Jogador 2 (Pretas) - foi o vencedor!");
            }  
            a.show(); //por favor funciona push
        }
    }
    
    public void enviaDados() {
        try {
            int qntPecas = this.pieceGroupB.getChildren().size() + this.pieceGroupW.getChildren().size(); 
            csc.getDataOutputStream().writeInt(qntPecas);

            // enviar peças pretas
            for(Node nodePeca : this.pieceGroupB.getChildren()){
                Piece peca = ((Piece)nodePeca);
                // quantidade de valores do array (X,Y,COR)
                csc.getDataOutputStream().writeInt(3);

                csc.getDataOutputStream().writeInt(peca.x);
                csc.getDataOutputStream().writeInt(peca.y);
                csc.getDataOutputStream().writeInt(0);
            }

            // enviar peças brancas
            for(Node nodePeca : this.pieceGroupW.getChildren()){
                Piece peca = ((Piece)nodePeca);
                // quantidade de valores do array (X,Y,COR)
                csc.getDataOutputStream().writeInt(3);

                csc.getDataOutputStream().writeInt(peca.x);
                csc.getDataOutputStream().writeInt(peca.y);
                csc.getDataOutputStream().writeInt(1);
            }
            

            // enviar
            csc.getDataOutputStream().flush();
        } catch(Exception e){
            
        }
    }
    
    private void atualizaDados(){
        labelBra.setText("Brancas -  " + pieceGroupW.getChildren().size());
        labelPre.setText("Pretas   -  " + pieceGroupB.getChildren().size());
    }
    public TrabalhoPratico (){
        this.conectToServer();
        
       
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
               
        launch(args);
    }

    void adicionarPecasRecebidas(int[][] posicoesPecas){
        ArrayList<Node> pPretas = new ArrayList();
        ArrayList<Node> pBrancas = new ArrayList();
        int x = 0;
        int y = 0;
        // para cada peça
        for(int[] peca : posicoesPecas){
            //System.out.println(peca[0] + " | " + peca[1] + " | " + peca[2]);
            if(peca[2] == 0){
                // preta
                
                Piece p = makePiece(PieceType.Black, peca[0], peca[1]);
                if(playerID == 1)
                    p.setMoveDisabled(true);
                
                else
                    p.setMoveDisabled(false);
                pPretas.add(p);
            } else {
                // branca
                Piece p = makePiece(PieceType.White, peca[0], peca[1]);
                if(playerID == 2)
                    p.setMoveDisabled(true);
                else
                    p.setMoveDisabled(false);
                pBrancas.add(p);
            }
        }
        
        System.out.println(pieceGroupB.getChildren().size());
        System.out.println(pieceGroupW.getChildren().size());
                
                
        Platform.runLater(new Runnable() {
            @Override public void run() {
                
               
                // apagar
                pieceGroupB.getChildren().clear();
                pieceGroupW.getChildren().clear();
                label.setText("O jogador " + otherPlayer + " jogou. É a tua vez!");
                // adicionar
                pieceGroupB.getChildren().addAll(pPretas);
                
                pieceGroupW.getChildren().addAll(pBrancas);
                 pPretas.removeAll(pPretas);
                pBrancas.removeAll(pBrancas);
              
            }
        });
    }
}
    
    
    
