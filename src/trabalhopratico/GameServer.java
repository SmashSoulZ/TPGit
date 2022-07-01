
package trabalhopratico;

/**
 *
 * @author 35191
 */
import java.io.*;
import java.net.*;

public class GameServer {
    
    private ServerSocket ss;
    private int numPlayers;
    private ServerSideConnection player1;
    private ServerSideConnection player2;
  
    
    public GameServer(){
        
        System.out.println("-----Game Server------");
        numPlayers = 0;
        try {
            ss=new ServerSocket(51734);
            
        } catch (IOException ex ) {
            System.out.println("IOException from GameServer");
        }
    }
    
    public void acceptConnections(){
        try {
           System.out.println("Espera de Conexões");
           while (numPlayers < 2) {
               Socket s = ss.accept();
               numPlayers ++;
               System.out.println("Jogador # " + numPlayers + "conectou-se");
               ServerSideConnection ssc = new ServerSideConnection(s, numPlayers);
               if (numPlayers == 1) {
                   player1=ssc;
                   
               }else {
                   player2 = ssc;
               }
               Thread t = new Thread(ssc);
               t.start();
               
               
           }
          System.out.println("Já tem 2 jogadores. Já não aceitamos mais conexões");

        }  catch (IOException ex ) {
            System.out.println("IOException from acceptConnections");
        }
    }
    
    private class ServerSideConnection implements Runnable {
        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;
        private int playerID;
        
        public ServerSideConnection(Socket s, int id){
            socket = s;
            playerID= id;
            try {
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
                
            } catch (IOException ex){
                System.out.println("IO Exception from run() CSS");
            }
        }
        public void run(){
            try{
                dataOut.writeInt(playerID);
                dataOut.flush();
                while (true){
                    
                }
            } catch (IOException ex){
                System.out.println("IO Exception from run() CSS");
            }
        }
    }
    public static void main (String [] args){
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
    
    
}
