package edu.hfcc.grocery.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Scanner;

import static edu.hfcc.grocery.server.GroceryStatus.*;

public class ClientTask {

    private Socket socketClient;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    static DecimalFormat df = new DecimalFormat("#.00");

    public ClientTask(String ip, int port) throws IOException {
        socketClient = new Socket(ip,port);
        System.out.println("Connected");
        this.output = new ObjectOutputStream(socketClient.getOutputStream());
    }

    public void mainProgram() throws IOException, ClassNotFoundException {
        Client client = new Client();
        ClientTools clientTools = new ClientTools();
        int menuInput;
        client.setStatus(START);

        Scanner in = new Scanner(System.in);
        this.input = new ObjectInputStream(socketClient.getInputStream());
        System.out.println("Welcome to the HFC Grocery Store");

        while(!socketClient.isClosed()) {
            if (client.getStatus().equals(MAIN_MENU)) {
                menu();
                menuInput = in.nextInt();
                menuInput = clientTools.isValidInput(-1, 4, menuInput);
                client.setMenuOption(menuInput);
                setStatus(client, clientTools);
            }

            if (client.getStatus().equals(ADD_TO_CART)) {
                clientTools.itemSelection(client);
            } else if (client.getStatus().equals(EDIT_CART)) {
                clientTools.cartRemovalMenu(client);
            } else if(client.getStatus().equals(CHECKOUT)) {
                clientTools.setDenominations(client);
                clientTools.checkout(client);
            } else if(client.getStatus().equals(EXIT)) {
                System.out.println("Thank you for shopping at the HFC Grocery Store!");
                socketClient.close();
                break;
            }

            sendMessage(client);
            client = (Client) input.readObject();


            outputAndReturnToMainMenu(client, clientTools);
            
        }
    }

    public void menu() {
        System.out.println("1. Shop 2. View Cart 3. Edit Cart 4. Checkout 0. Exit");
        System.out.print("Selection: ");
    }



    public void setStatus(Client client, ClientTools clientTools) {
        Scanner in = new Scanner(System.in);
        switch(client.getMenuOption()) {
            case -1:
                client.setStatus(MAIN_MENU);
            case 1:
                int input;
                client.setStatus(SHOPPING);
                System.out.println("What category would you like see 1. Fruit or 2. Vegetables: ");
                System.out.print("Selection: ");
                input = in.nextInt();
                input = clientTools.isValidInput(1,2,input);
                client.setMenuOption(input);
                break;
            case 2:
                client.setStatus(VIEW_CART);
                break;
            case 3:
                client.setStatus(EDIT_CART);
                break;
            case 4:
                client.setStatus(CHECKOUT);
                break;
            case 0:
                client.setStatus(EXIT);
                break;
            default:
                client.setStatus(MAIN_MENU);
                break;
        }
    }

    public void outputAndReturnToMainMenu(Client client, ClientTools clientTools) {
        if (client.getStatus().equals(SHOPPING)) {
            clientTools.outputItems(client);
            client.setStatus(ADD_TO_CART);
        } else if (client.getStatus().equals(ADD_TO_CART)) {
            client.setStatus(MAIN_MENU);
        } else if (client.getStatus().equals(VIEW_CART)) {
            clientTools.outputCart(client);
            client.setStatus(MAIN_MENU);
        } else if (client.getStatus().equals(EDIT_CART)) {
            clientTools.outputCart(client);
            client.setStatus(MAIN_MENU);
        } else if(client.getStatus().equals(CHECKOUT)) {
            client.setStatus(MAIN_MENU);
        }
    }


    private void sendMessage(Client client) throws IOException{
        output.writeObject(client);
        output.flush();
    }

    private static void closeSocket(ClientTask clientTask) {
        try {
            clientTask.socketClient.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ClientTask clientTask = new ClientTask("127.0.0.1", 2222);
        clientTask.mainProgram();
    }

}
