import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Retrieve implements Runnable {
    private static String REQUEST = "";

    @Override
    public void run() {
        System.out.println(REQUEST);
    }

    public static void main(String[] args) throws IOException {
        int PORT_NUMBER = 3000;
        String IP_ADDRESS;
        ServerSocket serverSocket;
        Socket socket;
        Thread handleRequest;

        // Get & display IP of the current machine
        serverSocket = new ServerSocket(PORT_NUMBER);
        IP_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        System.out.println(IP_ADDRESS + " at port number: " + PORT_NUMBER);

        // Listen for new socket connections from hosts/browsers that make requests to it
        while (true) {
            socket = serverSocket.accept();

            // Read the input stream of messages from other hosts
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String command = br.readLine();

            // Pass the request to the other thread by placing it into the global scope
            REQUEST = command;

            handleRequest = new Thread(new Retrieve());
            handleRequest.start();

            socket.close();
        }
//
//        Scanner scanner = new Scanner( System.in );
//        System.out.print( "Enter URL or the server's address: " );
//        String host = scanner.nextLine();
//
//        System.out.print( "Enter the file path/name: " );
//        String path = scanner.nextLine();
//
//
//        // Instantiate the TCP client socket
//        socket = new Socket( host, 80 );
//
//        // Instantiate the objects to write/read to the socket
//        PrintWriter outStream = new PrintWriter(socket.getOutputStream());
//        InputStream inStream = socket.getInputStream( );
//
//        // Log the HTTP GET request to the terminal
//        System.out.println("\nGET " + path + " HTTP/1.1\r\n" +
//                "Host: " + host + "\r\n" +
//                "Connection: close\r\n\r\n");
//
//        // Sends an HTTP GET request to the web server
//        outStream.print("GET " + path + " HTTP/1.1\r\n" +
//                "Host: " + host + "\r\n" +
//                "Connection: close\r\n\r\n");
//        outStream.flush();
//
//        // Read the response from the stream and print
//        BufferedReader rd = new BufferedReader(
//                new InputStreamReader(inStream));
//        String line;
//        while ((line = rd.readLine()) != null) {
//            System.out.println(line);
//        }
//
//        System.out.println("End of HTTP request");
//
//        // Close the IO streams
//        outStream.close();
//        inStream.close();
//
//        // Close the socket
//        socket.close();
    }
}


