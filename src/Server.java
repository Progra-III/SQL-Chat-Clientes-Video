import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    //----------------------------
    private ServerSocket serverSocket;

    //-----------------------------

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void serverStart(){

        try{
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("Un nuevo cliente se ha conectado");
                ClientController clientController = new ClientController(socket);
                Thread thread = new Thread(clientController);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }//method

    public void serverClose(){

        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

       try {
           ServerSocket serverSocket = new ServerSocket(1234);
           Server server = new Server(serverSocket);
           server.serverStart();

       } catch (IOException e) {
           e.printStackTrace();
       }

    }


}
