import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Retrieve {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner( System.in );
        System.out.print( "Enter URL or the server's address: " );
        String host = scanner.nextLine();

        System.out.print( "Enter the file path/name: " );
        String path = scanner.nextLine();


        // Instantiate the TCP client socket
        Socket socket = new Socket( host, 80 );

        // Instantiate the objects to write/read to the socket
        PrintWriter outStream = new PrintWriter(socket.getOutputStream());
        InputStream inStream = socket.getInputStream( );

        // Sends an HTTP GET request to the web server
        outStream.print("GET " + path + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "Connection: close\r\n\r\n");
        outStream.flush();

        // Read the response from the stream and print
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(inStream));
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }

        System.out.println("End of HTTP request");

        // Close the IO streams
        outStream.close();
        inStream.close();

        // Close the socket
        socket.close();
    }
}


