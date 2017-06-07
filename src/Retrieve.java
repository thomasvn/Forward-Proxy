import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Retrieve implements Runnable {
    // Global variables to share information about browser with thread
    private static String browserRequest = "";
    private static Socket browserSocket;


    /**
     * This thread is instantiated whenever our forward proxy receives a request from the browser.
     *
     * The thread is responsible for serving the browser the page that was requested by either providing content from
     * the locally created cache, or by requesting the content from the target server.
     */
    @Override
    public void run() {
        Socket threadSocket = browserSocket;

        // Parse the request from the browser to identify elements of the URL (the host and the path)
        String host = browserRequest.split("/")[2];
        String path = "";
        int slashCount = 0;
        for(int i = 0; i < browserRequest.length(); i++) {
            if(browserRequest.charAt(i) == '/') {
                slashCount++;
                if(slashCount == 3) {
                    path = browserRequest.substring(i);
                    path = path.split(" ")[0];
                }
            }
        }
        System.out.println("host: " + host);
        System.out.println("path: " + path);

        // Filter out special characters to create a file name for the potentially cached object
        String filename = host + path;
        filename = filename.replaceAll("[^a-zA-Z]", "").toLowerCase();
        System.out.println("filename: " + filename);

        // Check if request is already in the cache
        File file = new File(filename + ".txt");
        if (file.exists()) {
            System.out.println("Already in cache");

            BufferedReader br = null;
            FileReader fr = null;
            String html = "";
            String line;
            String lastModified = "";
            Date dateLastModified = new Date();
            String lastAccessed = "";
            Date dateLastAccessed = new Date();
            boolean isStale = false;
            String staleness = "";

            // Sends cached data to browser if requested object is already in cache
            try {
                br = new BufferedReader(new FileReader(filename + ".txt"));

                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    if(line.contains("Last-Modified:")) {
                        lastModified = line;
                        lastModified = lastModified.replace("Last-Modified: ", "");
                        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                        dateLastModified = format.parse(lastModified);

                        BufferedReader br_date = new BufferedReader(new FileReader(filename + "_date.txt"));
                        lastAccessed = "" + br_date.readLine();
                        format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                        System.out.println(lastAccessed);

//                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM HH:mm:ss z yyyy");
                        dateLastAccessed = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(lastAccessed);

//                        dateLastAccessed = format.parse(lastAccessed);

                        if (dateLastAccessed.before(dateLastModified)) {
                            isStale = true;
                            staleness = "Cached file is stale.";
                        } else {
                            isStale = false;
                            staleness = "Cached file is not stale.";
                        }
                    }
                    html += (line + "\n");
                }
                if (!isStale) {
                    OutputStream os = threadSocket.getOutputStream();
                    os.write(html.getBytes());
                    os.close();
                    System.out.println("End of HTTP request");
                    System.out.println("Retrieved from cache");
                    System.out.println("Last Modified: " + dateLastModified.toString());
                    System.out.println("Last Access: " + dateLastAccessed.toString());
                    System.out.println(staleness);
                }
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (isStale || !file.exists()) {
            System.out.println("Does not yet exist in cache");

            String originalPath = path;
            path += "?coen168=1234"; // Added parameters to GET request for demo

            try {
                // Instantiate the TCP client socket
                Socket socket = new Socket(host, 80);

                // Instantiate the objects to write/read to the socket
                PrintWriter outStream = new PrintWriter(socket.getOutputStream());
                InputStream inStream = socket.getInputStream();

                // Log the HTTP GET request to the terminal
                System.out.println("\nGET " + path + " HTTP/1.1\r\n" +
                        "Host: " + host + "\r\n" +
                        "Connection: close\r\n" +
                        "coen168: 1234\r\n\r\n");

                // Sends an HTTP GET request to the web server
                outStream.print("GET " + path + " HTTP/1.1\r\n" +
                        "Host: " + host + "\r\n" +
                        "Connection: close\r\n\r\n");

                outStream.flush();

                // Read the response from the stream using the "extract" method and print
                String document = extract(inStream);
                String html = document;

                OutputStream os = threadSocket.getOutputStream();
                os.write(html.getBytes());
                os.close();
                System.out.println("End of HTTP request");

                // Close the IO streams
                outStream.close();
                inStream.close();

                // Close the socket
                socket.close();

                // Store in cache, no cache replacement policy
                BufferedWriter bw = null;
                BufferedWriter bw_date = null;

                try {
                    // Cache http response
                    File newFile = new File(filename + ".txt");
                    if (!newFile.exists()) {
                        newFile.createNewFile();
                    }

                    FileWriter fw = new FileWriter(newFile);
                    bw = new BufferedWriter(fw);
                    bw.write(html);
                    System.out.println("File cached successfully: " + filename + ".txt");

                    // Cache date
                    Date dateNow = new Date();
                    File dateFile = new File(filename + "_date.txt");
                    if (!dateFile.exists()) {
                        dateFile.createNewFile();
                    }

                    FileWriter fw_date = new FileWriter(dateFile);
                    bw_date = new BufferedWriter(fw_date);
                    bw_date.write(dateNow.toString());
                    System.out.println("File date cached successfully: " + filename + "_date.txt");
                    System.out.println(dateNow.toString());
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                finally {
                    try{
                        if(bw!=null)
                            bw.close();
                        if(bw_date!=null)
                            bw_date.close();
                    } catch(Exception ex){
                        System.out.println("Error in closing the BufferedWriter"+ex);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * This is the main thread which acts as our server which is listening for requests. When requests are made to the
     * forward proxy, we will create a new thread to handle that request.
     *
     * @param args
     */
    public static void main(String[] args) {
        int PORT_NUMBER = 3000;
        String IP_ADDRESS;
        ServerSocket serverSocket;
        Socket socket;
        Thread handleRequest;

        try {
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
                if(command == null) {
                    System.out.println("null command!");
                    command = "";
                }
                System.out.println("Command: " + command);

                // Pass the request to the other thread by placing it into the global scope
                if (!command.contains("sophos") && command != "") {
                    browserRequest = command;
                    browserSocket = socket;
                    System.out.println("SOCKET PORT: " + socket.getPort());

                    handleRequest = new Thread(new Retrieve());
                    handleRequest.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method takes an input stream and extracts all bytes that were sent. It places everything from the stream
     * into a byte array. Then returns a conversion of this byte array to a string.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String extract(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, read);
        }
        baos.flush();
        return  new String(baos.toByteArray(), "UTF-8");
    }
}


