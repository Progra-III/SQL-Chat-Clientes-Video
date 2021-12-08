import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userId;

    public Client(Socket socket, String userId){

        try{
            this.socket = socket;
            this.userId = userId;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            CloseAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void CloseAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

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

    public void sendMessage(){
        try{
            bufferedWriter.write(userId);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){

                String sendMessage = scanner.nextLine();
                bufferedWriter.write(userId +": "+sendMessage);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        } catch (IOException e) {
            CloseAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void receiveMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                String message;                                             //mensaje al chat
                while(socket.isConnected()){
                    try{
                        message = bufferedReader.readLine();
                        System.out.println(message);
                    } catch (Exception e) {
                        CloseAll(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner =  new Scanner(System.in);
        System.out.println("Ingrese su id para ingresar al chat");
        String userId = scanner.nextLine();

        Socket socket = new Socket("local host", 1234);

        Client client = new Client(socket, userId);
        client.sendMessage();
        client.receiveMessage();

    }
}
