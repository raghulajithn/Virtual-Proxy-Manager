import java.io.*;
import java.net.*;

public class urlclient {
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter the URL: ");
            String url = reader.readLine();
            System.out.print(url);
            System.out.print("Enter the save directory path: ");
            String savePath = reader.readLine();

            System.out.print("Enter the desired filename: ");
            String filename = reader.readLine();

            URL remoteUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) remoteUrl.openConnection();

            InputStream inputStream = connection.getInputStream();

            String fileExtension = getFileExtension(remoteUrl);

            saveContentsToFile(inputStream, savePath, filename, fileExtension);

            reader.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveContentsToFile(InputStream inputStream, String savePath, String filename, String fileExtension) {
        try {
            File directory = new File(savePath);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new IOException("Failed to create directory: " + directory.getAbsolutePath());
                }
            }

            String filePath = directory.getAbsolutePath() + File.separator + filename + fileExtension;

            FileOutputStream outputStream = new FileOutputStream(filePath);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            System.out.println("File downloaded successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save the file: " + e.getMessage());
        }
    }

    private static String getFileExtension(URL url) {
        String file = url.getFile();
        return file.substring(file.lastIndexOf('.'));
    }
}