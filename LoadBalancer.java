import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LoadBalancer {
    private static final int LB_PORT = 8888;
    private static List<Integer> serverAddresses = new ArrayList<>();
    private static int currentIndex = 0;
    public static void main(String[] args) {
        serverAddresses.add(12345);
//        serverAddresses.add(12346);

        try {
            ServerSocket lbSocket = new ServerSocket(LB_PORT);
            System.out.println("Load Balancer listening on port " + LB_PORT);

            while (true) {
                System.out.println("waiting Client to connect : ");
                Socket clientSocket = lbSocket.accept();
                System.out.print("Client was assigned Server : ");
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                int s = serverAddresses.get(currentIndex);
                currentIndex = (currentIndex + 1) % serverAddresses.size();
                System.out.println(s);
                out.println(serverAddresses.get(currentIndex));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
