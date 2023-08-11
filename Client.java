import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_IP = "127.0.0.1"; // Change to your server's IP address
    private static int SERVER_PORT = 8888; // Change to your load balancer's port

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Connected to server.");

            System.out.println("Waiting to connect to Server : ");
            SERVER_PORT = Integer.parseInt(in.readLine());
            System.out.println("Connected to Server : " + SERVER_PORT);
            socket = new Socket(SERVER_IP,SERVER_PORT);

            userInput = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {

                System.out.println("Choose operation (GET, PUT, REMOVE) or 'QUIT' to exit:");
                String operation = userInput.readLine().toUpperCase();

                if ("QUIT".equals(operation)) {
                    break;
                }

                out.println(operation);

                String response;
                switch (operation) {
                    case "GET" , "REMOVE" -> {
                        System.out.println("Enter key:");
                        String getKey = userInput.readLine();
                        out.println(getKey);
                        response = in.readLine();
                    }
                    case "PUT" -> {
                        System.out.println("Enter key:");
                        String putKey = userInput.readLine();
                        System.out.println("Enter value:");
                        String putValue = userInput.readLine();
                        out.println(putKey);
                        out.println(putValue);
                        response = in.readLine();
                    }
                    default -> response = "Invalid operation";
                }

                System.out.println("Server response: " + response);
            }

            socket.close();
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
