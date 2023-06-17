import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class urlremoteserver {
    public static void main(String[] args) {
        int port = 8080; // Replace with the desired port number

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Remote server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New forward server connection established.");

                // Create a new thread to handle the client request
                Thread thread = new Thread(() -> handleClientRequest(socket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(Socket socket) {
        try {
            // Read the URL from the forward server
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String url = reader.readLine();
            System.out.println("Received URL: " + url);

            // Retrieve the contents of the URL
            String contents = getURLContents(url);

            // Send the contents back to the forward server
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(contents);

            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getURLContents(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } else {
            System.out.println("Failed to retrieve URL contents. Response code: " + responseCode);
            return "";
        }
    }
}