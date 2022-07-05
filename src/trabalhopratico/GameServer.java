package trabalhopratico;

/**
 *
 * @author 35191
 */
import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {
    
    private ServerSocket ss;
    private int numPlayers;
    protected ServerSideConnection player1;
    protected ServerSideConnection player2;
  
    
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
               ServerSideConnection ssc = new ServerSideConnection(s, numPlayers, this);
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
        private boolean idEnviado = false;
        private GameServer gs;
        
        public DataOutputStream getDataOutputStream() {
            return this.dataOut;
        }
        
        public ServerSideConnection(Socket s, int id, GameServer gs){
            this.gs = gs;
            socket = s;
            playerID = id;
            try {
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
                
            } catch (IOException ex){
                System.out.println("IO Exception from run() CSS");
            }
        }
        public void run(){
            try{
                if(!idEnviado){
                    idEnviado = true;
                    dataOut.writeInt(playerID);
                    dataOut.flush();
                }
                
                // ficar à espera de receber dados
                while (true){
                    if(playerID == 1){
                        System.out.println(gs.player2);
                        // se é jogador 1, passar dados para o jogador 2
                        if(gs.player2 != null)
                            gs.player2.getDataOutputStream().writeInt(dataIn.readInt());
                    } else {
                        // se é jogador 2, passar dados para o jogador 1
                        if(gs.player1 != null)
                            gs.player1.getDataOutputStream().writeInt(dataIn.readInt());
                    }
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