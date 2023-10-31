package edu.hfcc.grocery.server;

import edu.hfcc.grocery.Objects.Cart;
import edu.hfcc.grocery.client.Client;
import edu.hfcc.grocery.database.ConnectToDatabase;
import edu.hfcc.grocery.Objects.Grocery;
import edu.hfcc.grocery.database.GroceryCreation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static edu.hfcc.grocery.server.GroceryStatus.*;

public class ServerTask implements Runnable {
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;


    public ServerTask(Socket client) throws IOException{
        this.client = client;
        this.input = new ObjectInputStream(client.getInputStream());
        this.output = new ObjectOutputStream(client.getOutputStream());

        try {
            ServerTools.createTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (!this.client.isClosed()) {

            System.out.println("\nInside server!!");


            Client client = null;

            try {
                client = (Client) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            if(this.client.isClosed())
            {
                break;
            }

            serverSelections(client);

            try {
                sendMessage(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void serverSelections(Client client) {
        if (client.getStatus().equals(START)) {
            try {
                ServerTools.getSizeOfDatabase(client);
                client.setStatus(MAIN_MENU);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if(client.getStatus().equals(SHOPPING)) {
            try {
                ServerTools.queryGroceries(client);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if(client.getStatus().equals(ADD_TO_CART)) {
            try {
                ServerTools.createCart(client);
                ServerTools.updateStock(client);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if(client.getStatus().equals(VIEW_CART)) {
            try {
                ServerTools.viewCart(client);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if(client.getStatus().equals(EDIT_CART)) {
            try {
                ServerTools.removeFromCart(client);
                ServerTools.removeZeroQuantityItems(client);
                ServerTools.addRemovedItemToStock(client);
                ServerTools.updateOrderTotal(client);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


    private void sendMessage(Client clientData) throws IOException {
        output.writeObject(clientData);
        output.flush();
    }

}

