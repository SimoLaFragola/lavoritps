import java.io.*;
import java.net.*;

public class TcpServer {

    public static void main(String[] args) throws Exception {

        int serverPort = 8784;
        String clientMsg = "";

        try {

            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Server in ascolto sulla porta: " + serverPort);

            Socket clientSocket = serverSocket.accept();
            DataInputStream inStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outStream = new DataOutputStream(clientSocket.getOutputStream());    

            while (!clientMsg.equals("quit")) {

                clientMsg = inStream.readUTF();

                String vocali = "aeiouAEIOU";
                int contaVocali = 0;
                int contaConsonanti = 0;

                for (int i = 0; i < clientMsg.length(); i++) {
                    char c = clientMsg.charAt(i);

                    // Conta solo le lettere
                    if (Character.isLetter(c)) {
                        if (vocali.indexOf(c) != -1) {
                            contaVocali++;
                        } else {
                            contaConsonanti++;
                        }
                    }
                }

                String risposta = 
                    "Numero Vocali: " + contaVocali + 
                    "\nNumero Consonanti: " + contaConsonanti;

                outStream.writeUTF(risposta);
                outStream.flush();
            }

            inStream.close();
            outStream.close();
            clientSocket.close();
            serverSocket.close();

        } catch (Exception e) {
            System.out.println("Errore: " + e);
        }
    }
}
