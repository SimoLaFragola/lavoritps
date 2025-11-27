import java.io.*;
import java.net.*;

public class CalcolatriceServer {

    private static final int PORTA = 7000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Server avviato sulla porta " + PORTA);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connesso: " + clientSocket.getRemoteSocketAddress());

                new Thread(() -> {
                    try (BufferedReader in = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                         PrintWriter out = new PrintWriter(
                                clientSocket.getOutputStream(), true)) {

                        String richiesta;
                        while ((richiesta = in.readLine()) != null) {
                            try {
                                double risultato = calcola(richiesta);
                                out.println("Risultato: " + risultato);
                            } catch (Exception e) {
                                out.println("ERRORE: " + e.getMessage());
                            }
                        }

                    } catch (IOException e) {
                        System.err.println("Errore client: " + e.getMessage());
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            System.err.println("Errore chiusura socket: " + e.getMessage());
                        }
                    }
                }).start();

            }

        } catch (IOException e) {
            System.err.println("Errore server: " + e.getMessage());
        }
    }
  
    private static double calcola(String richiesta) throws Exception {
        String[] parti = richiesta.split(" ");
        if (parti.length != 3) {
            throw new Exception("Formato non valido. Usa: numero operatore numero");
        }

        double num1 = Double.parseDouble(parti[0]);
        String operazione = parti[1];
        double num2 = Double.parseDouble(parti[2]);

        switch (operazione) {
            case "+": return num1 + num2;
            case "-": return num1 - num2;
            case "*": return num1 * num2;
            case "/":
                if (num2 == 0) throw new ArithmeticException("Divisione per zero");
                return num1 / num2;
            default: throw new IllegalArgumentException("Operazione non supportata");
        }
    }
}

// Ciao sono Simone, hey!