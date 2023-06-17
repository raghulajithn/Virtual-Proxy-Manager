import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class urlforwardserver {
    public static void main(String[] args) {
        int forwardServerPort = 8000;
        String remoteServerIP = "localhost"; // Replace with the IP address or hostname of the remote server
        int remoteServerPort = 8080; // Replace with the port number used by the remote server

        try {
            ServerSocket serverSocket = new ServerSocket(forwardServerPort);
            System.out.println("Forward server started on port " + forwardServerPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connection established.");

                // Read the URL from the client
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String url = reader.readLine();
                System.out.println("Received URL: " + url);

                // Establish connection with the remote server
                Socket remoteSocket = new Socket(remoteServerIP, remoteServerPort);

                // Send the URL to the remote server
                PrintWriter writer = new PrintWriter(remoteSocket.getOutputStream(), true);
                writer.println(url);

                // Receive the contents from the remote server
                BufferedReader remoteReader = new BufferedReader(new InputStreamReader(remoteSocket.getInputStream()));
                String contents = remoteReader.readLine();

                // Send the contents back to the client
                PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriter.println(contents);

                remoteReader.close();
                writer.close();
                clientWriter.close();
                remoteSocket.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}