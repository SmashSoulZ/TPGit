
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
    private Tile[][] board= new Tile[Width][Height];
    
    private Group tileGroup = new Group();   
    private Group pieceGroupW = new Group();
    private Group pieceGroupB = new Group();
    
    private ClientSideConnection csc;
    private int playerID;
    private int otherPlayer; 
    private int turnsMade;
    private int player;
    
    Alert a = new Alert(Alert.AlertType.NONE);
    private boolean moveDisabled = false;
    
    Button button = new Button("Ajuda");
    Button button2 = new Button("Sair");
    

    
    Label labelPec = new Label("Peças");
    Label labelBra = new Label("Brancas -  " + pieceGroupW.getChildren().size());
    Label labelPre = new Label("Pretas   -  " + pieceGroupB.getChildren().size());
        
     
    public void conectToServer(){
        csc = new ClientSideConnection();
        
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
    
  
    
    private class ClientSideConnection {
        
        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;
        private int playerID;
        
        public int getPlayerID(){
            return this.playerID;
            
        }
        
        public ClientSideConnection (){
            System.out.println("---Cliente---");
            try {
                socket = new Socket ("localhost", 51734);
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
                playerID = dataIn.readInt();
                System.out.println("Conectado ao server como Jogador # " + playerID);
                
                /*if (playerID==1){
                   label.setText("És o jogador 1. Começa a partida");
                   otherPlayer = 2;
                } else {
                    label.setText("És o jogador 2. Espere pelo seu turno");
                    otherPlayer = 1;
                }*/
                
            } catch (IOException ex){
                System.out.println("IO Exception from CSS");
            }
            
            
        }
    }
   

    
     public Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(Width * Tile_Size + 100, Height * Tile_Size + 40);
        label.setLayoutY(505);
        label.setLayoutX(5);
        label.setFont(new Font ("Verdana", 18));
        
        turnsMade = 0;
        
        button.setLayoutY(470);
        button.setLayoutX(505);
        button2.setLayoutY(470);
        button2.setLayoutX(560);
        
        root.getChildren().addAll(tileGroup, pieceGroupW, pieceGroupB, label, button, button2);
      
         
       
        
        
        
        for (int y=0; y< Height; y++){
            for (int x=0; x< Width; x++){
                Tile tile = new Tile((x+y)% 2 == 0, x,y);          
                board[x][y]= tile;
;
                
                
                tileGroup.getChildren().add(tile);
                
                
                
                Piece piece = null;
                
//                if (y <= 1 || x <= 1 && y<=2){
//                    piece = makePiece(PieceType.Black,x ,y);
//                }
//                if (y >= 3 || x >= 3 && y>=2){
//                    piece = makePiece(PieceType.White,x ,y);
//                }
//                if (piece != null){
//                    tile.setPiece(piece);
//                    pieceGroup.getChildren().add(piece);  
//                }   
                
                if (y <= 0){
                    piece = makePiece(PieceType.Black,x ,y);
                }
                if (y >= 4){
                    piece = makePiece(PieceType.White,x ,y);
                }
                if (piece != null){
                    tile.setPiece(piece);
                    
                    if(tile.getPiece().getType() == PieceType.Black ){
                        //System.out.println("Peça preta com exito");
                        pieceGroupB.getChildren().add(piece);
                    } else {
                        //System.out.println("Peça branca com exito"); 
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
        
        root.getChildren().addAll(labelPec, labelBra, labelPre);
        
        
        return root;
    }
    
   
    
    private MoveResult tryMove(Piece piece, int newX, int newY){
        if (board[newX][newY].hasPiece()){
            
            this.label.setText("Fizeste a tua jogada. Espera pela jogada do jogador " + otherPlayer);
            turnsMade++;
            
                       
            System.out.println("Turnos feitos: " + turnsMade);
            
         /**   if(this.playerID == 1){
                // imaginar que o 1 é o branco
                for(Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(false);
                }
            } else {
                // imaginar que o 1 é o branco
                for(Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(false);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
            }**/
            
            return new MoveResult(MoveType.None);
            
        }
       int x0 = toBoard(piece.getOldX());
       int y0 = toBoard(piece.getOldY());
       
       if ((Math.abs(newX - x0) == 0 && Math.abs(newY - y0) == 1) || (Math.abs(newY - y0) == 0 && Math.abs(newX - x0) == 1) || (Math.abs(newX - x0) == 1 && Math.abs(newY - y0) != 2) ){
          this.label.setText("Fizeste a tua jogada. Espera pela jogada do jogador " + otherPlayer);
          turnsMade++;
          System.out.println("Turnos feitos: " + turnsMade);
         /**  if(this.playerID == 1){
                // imaginar que o 1 é o branco
                for(Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(false);
                }
            } else {
                // imaginar que o 1 é o branco
                for(Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(false);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
            }**/
            
           return new MoveResult(MoveType.Normal);
       } else if (Math.abs(newY - y0) == 2 && !(Math.abs(newX - x0) == 1) || Math.abs(newX - x0) == 2 && !(Math.abs(y0 - newY) == 1)) {
           
           this.label.setText("Fizeste a tua jogada. Espera pela jogada do jogador " + otherPlayer);
           turnsMade++;
            System.out.println("Turnos feitos: " + turnsMade);
           /**  if(this.playerID == 1){
                // imaginar que o 1 é o branco
                for(Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(false);
                }
            } else {
                // imaginar que o 1 é o branco
                for(Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(false);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
            }**/
           int x1 = x0 + (newX - x0) / 2;
           int y1 = y0 + (newY - y0) / 2;
           
           
           if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()){
               this.label.setText("Fizeste a tua jogada. Espera pela jogada do jogador " + otherPlayer);
               turnsMade++;
            System.out.println("Turnos feitos: " + turnsMade);
           /**  if(this.playerID == 1){
                // imaginar que o 1 é o branco
                for(Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(false);
                }
            } else {
                // imaginar que o 1 é o branco
                for(Node p : pieceGroupW.getChildren()){
                    ((Piece)p).setMoveDisabled(false);
                }
                for(Node p : pieceGroupB.getChildren()){
                    ((Piece)p).setMoveDisabled(true);
                }
            }**/
               System.out.println(" x0 - " + x0 + " e y0 - " +y0 );
               System.out.println("x1 - " + x1 + " e y1 - " +y1 );
               System.out.println("newX - " + newX + " e newY - " +newY );
               return new MoveResult(MoveType.Kill, board[x1][y1].getPiece());
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

        button.setOnAction(Evento);
        button2.setOnAction(evento2);
        Scene scene2 = new Scene(createContent());
        
        stage.setTitle("Alquerque");
        stage.setScene(scene2);
        stage.show();
        
        
        
    }

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
                    verificaFim(pieceGroupW.getChildren().size(), piece);
                    verificaFim(pieceGroupB.getChildren().size(), piece);
                    break;
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

  
    
    
    
}
