package trabalhopratico;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientSideConnection {
    private Socket socket;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private int playerID;
    private Thread t = null;
    private TrabalhoPratico tp;

    public int getPlayerID(){
        return this.playerID;
    }
    
    public DataOutputStream getDataOutputStream() {
        return this.dataOut;
    }

    public ClientSideConnection (TrabalhoPratico tp){
        this.tp = tp;

        System.out.println("---Cliente---");
        try {
            socket = new Socket ("localhost", 51734);
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
            playerID = dataIn.readInt();
            System.out.println("Conectado ao server como Jogador # " + playerID);

            this.t = new ClientSideThread();
            t.start();


        } catch (IOException ex){
            System.out.println("IO Exception from CSS");
        }
    }
    
    class ClientSideThread extends Thread {
        
        @Override
        public void run() {
            
            while(true){
                try {
                    // primeiro valor = quantidade de arrays
                    int[][] novaspecas = new int[dataIn.readInt()][];

                    // para quantidade de arrays
                    for(int x = 0; x < novaspecas.length; x++){

                        // obter quantidade de elementos do array interno
                        novaspecas[x] = new int[dataIn.readInt()];
                        System.out.println("novaspecas - "+novaspecas[x]);
                        // ler cada elemento interno
                        for(int y = 0; y < novaspecas[x].length; y++){
                            novaspecas[x][y] = dataIn.readInt();
                            
                        }
                        
                    }
                    // adicionar novas peças
                    if(novaspecas.length > 0)
                        tp.adicionarPecasRecebidas(novaspecas);
                    
                } catch (IOException ex) {
                    Logger.getLogger(ClientSideConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("DADOS RECEBIDOS");
            }

        }
    }
    
}