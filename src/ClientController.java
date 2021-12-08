import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientController implements Runnable {

    //-------------------------------
    public static ArrayList<ClientController> clientController = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userId;

    //--------------------------------

    public ClientController(Socket socket){

        try{
            this.socket = socket;
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userId = bufferedReader.readLine();
            clientController.add(this);
            BroadcastMessage("SERVIDOR: " + userId + "HA INGRESADO AL CHAT");

        } catch (IOException e) {
           CloseAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void CloseAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

        RemoveClientController();

        try {

            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }

            if (socket != null) {
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RemoveClientController(){
        clientController.remove(this);
        BroadcastMessage("SERVIDOR: " + userId + "HA SALIDO DEL CHAT");
    }

    public void BroadcastMessage(String message)  {
        for(ClientController clientController: clientController){

           try{
               if(!clientController.userId.equals(userId)){
                   clientController.bufferedWriter.write(message);
                   clientController.bufferedWriter.newLine();
                   clientController.bufferedWriter.flush();
               }
           } catch (IOException e) {
              CloseAll(socket, bufferedReader, bufferedWriter);
           }
        }
    }

    @Override
    public void run() {
        String message;

        while(socket.isConnected()){
            try{
                message = bufferedReader.readLine();
                BroadcastMessage(message);

            } catch (Exception e) {
                CloseAll(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
}
