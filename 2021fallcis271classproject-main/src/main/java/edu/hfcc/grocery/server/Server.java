package edu.hfcc.grocery.server;

import edu.hfcc.grocery.client.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket server;

    public void start(int port) {

        ExecutorService service = Executors.newFixedThreadPool(3);

        try {
            Client client = null;
            server = new ServerSocket(port);
            while(true) {
                service.execute(new ServerTask(server.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Not able to create server: " + e.getMessage());
        } finally {
            service.shutdownNow();
        }


    }

    public void stop() {
        try {
            server.close();
        } catch(IOException e) {
            System.out.println("Problem stopping server: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(2222);
        server.stop();
    }
}
