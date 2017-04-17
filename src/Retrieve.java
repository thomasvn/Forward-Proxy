public class Retrieve {

}
//    /**
//     * This method listens and handles any of the data streamed from other hosts
//     * @param args
//     */
//    public void listener(String[] args) {
//        ServerSocket serverSocket;
//        Socket socket;
//        Thread lindaTerminal;
//
//        try {
//            HOSTNAME = args[0];
//        } catch (ArrayIndexOutOfBoundsException e) {
//            System.out.println("Please specify the host name when running the executable");
//            return;
//        }
//
//        try {
//            // Create a TCP server socket on a random available port
//            while(true) {
//                PORT_NUMBER = ThreadLocalRandom.current().nextInt(1024, 65535 + 1);
//                try {
//                    serverSocket = new ServerSocket(PORT_NUMBER);
//                    break;
//                } catch(IOException e) {
//                    continue;
//                }
//            }
//
//            // Get & display IP of the current machine
//            IP_ADDRESS = InetAddress.getLocalHost().getHostAddress();
//            System.out.println(IP_ADDRESS + " at port number: " + PORT_NUMBER);
//
//            // Remove old tuple space if it exists
//            String tupleSpaceFilePath = "/tmp/" + LOGIN + "/linda/" + HOSTNAME + "/tuples/tuples.txt";
//            File dir = new File(tupleSpaceFilePath);
//            Files.deleteIfExists(dir.toPath());
//
//            // Add this host to the list of hosts in our network
//            listOfHosts += (HOSTNAME + " " + IP_ADDRESS + " " + PORT_NUMBER + ",");
//            add();
//
//            // Create a new thread to accept Linda Terminal Commands
//            lindaTerminal = new Thread(new P1());
//            lindaTerminal.start();
//
//            // Listen for new socket connections to from hosts that request it
//            while (true) {
//                socket = serverSocket.accept();
//
//                // Read the input stream of messages from other hosts
//                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String dataStreamCommand = br.readLine();
//                parseDataStreamCommand(dataStreamCommand);
//
//                socket.close();
//            }
//
//            // Close the sockets?
//            // TODO: CHeck for when input stream is null. Once it is, then we close the socket
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

