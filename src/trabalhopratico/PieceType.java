/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhopratico;

/**
 *
 * @author MSI Gaming
 */
public enum PieceType {
    Black(1), White(-1);
    
    final int moveDir;
    PieceType(int moveDir){
        this.moveDir = moveDir;
    }
    
}
