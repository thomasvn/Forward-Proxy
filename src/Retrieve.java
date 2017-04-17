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

        PrintWriter s_out = null;
        BufferedReader s_in = null;

        // Instantiate the TCP client socket
        Socket s = new Socket();

        try {
            s.connect(new InetSocketAddress(host, 80));
            System.out.println("Successful connection to: (" + host + ")");

            // Instantiate the objects to write/read to the socket
            s_out = new PrintWriter(s.getOutputStream(), true);
            s_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        }

        // Exception is thrown if it is unable to properly connect to host
        catch (UnknownHostException e) {
            System.err.println("Unable to connect to host :(" + host + ")");
            System.exit(1);
        }

        // Sends an HTTP GET request to the web server
        String message = "GET " + path + " HTTP/1.1\r\n\r\n";
        s_out.println(message);
        System.out.println("GET request has been sent!");

        // Retrieve the response from the server and print
        String response;
        while ((response = s_in.readLine()) != null) {
            System.out.println(response);
        }

        // Close the IO streams
        s_out.close();
        s_in.close();

        // Close the socket
        s.close();
    }
}


