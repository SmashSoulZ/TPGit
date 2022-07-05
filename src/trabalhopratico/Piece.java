/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhopratico;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static trabalhopratico.TrabalhoPratico.Tile_Size;

/**
 *
 * @author MSI Gaming
 */
public class Piece extends StackPane {
    private PieceType type;
    private boolean moveDisabled = false;
    
    private double mouseX, mouseY;
    private double oldX, oldY;
    public int x,y;
    
    public PieceType getType(){
        return type;
    }
    
    public double getOldX(){
        return oldX;
    }
    public double getOldY(){
        return oldY;
    }
    
    public Piece(PieceType type, int x, int y){
        this.type= type;
        this.x = x;
        this.y = y;
        
        move(x, y);
        
        Ellipse bg = new Ellipse(Tile_Size*0.33, Tile_Size*0.33);
                bg.setFill(Color.BLACK);
                bg.setStroke(Color.BLACK);
                bg.setStrokeWidth(Tile_Size*0.03);
        
                bg.setTranslateX((Tile_Size - Tile_Size * 0.33 * 2 ) / 2);
                bg.setTranslateY((Tile_Size - Tile_Size * 0.33 * 2 ) / 2);
                
        Ellipse ellipse = new Ellipse(Tile_Size*0.33, Tile_Size*0.33);
                bg.setFill(type == PieceType.Black 
                        ? Color.valueOf("#000000") : Color.valueOf("#ffffff"));
                bg.setStroke(type == PieceType.Black 
                        ? Color.valueOf("#ffffff") : Color.valueOf("#000000"));
                bg.setStrokeWidth(Tile_Size*0.03);
        
                bg.setTranslateX((Tile_Size - Tile_Size * 0.33 * 2 ) / 2);
                bg.setTranslateY((Tile_Size - Tile_Size * 0.33 * 2 ) / 2);
                
                getChildren().addAll(bg);
                
                setOnMousePressed(e -> {
                    mouseX = e.getSceneX();
                    mouseY = e.getSceneY();
                });
                setOnMouseDragged(e -> {
                    if(!moveDisabled)
                        relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
                    else
                        System.out.println("Movimento da peça desativado");
                    
                    
                });
    }
    
    public void setMoveDisabled(boolean m){
        this.moveDisabled = m;
    }
    
    public void move(int x, int y){
        oldX = x * Tile_Size;
        oldY = y * Tile_Size;
        relocate(oldX, oldY);
    }
    public boolean isMoveDisabled(){
        return this.moveDisabled;
    }
    
    public void abortMove(){
        relocate(oldX, oldY);
    }
}