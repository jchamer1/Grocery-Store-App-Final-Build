package edu.hfcc.grocery.database;

import javax.xml.crypto.Data;
import java.sql.*;

public class CreateTables {
    String createGroceryTable = "CREATE TABLE Groceries(ID SERIAL, name varchar, type varchar, price float, quantity int, PRIMARY KEY(ID))";
    String createCartTable = "CREATE TABLE Cart(ID int, name varchar, type varchar, price float, quantity int, PRIMARY KEY(ID))";
    String dropGroceryTable = "DROP TABLE groceries";
    String dropCartTable = "DROP TABLE cart";

    public void createTables() throws SQLException{
        String tableName = "";
        Connection connection = ConnectToDatabase.connectToDatabase();
        DatabaseMetaData md = connection.getMetaData();
        ResultSet resultSet = md.getTables(null, null, tableName, null);

        if(resultSet != null) {
            dropTables();
        }

        PreparedStatement grocery = connection.prepareStatement(createGroceryTable);
        PreparedStatement cart = connection.prepareStatement(createCartTable);

        grocery.execute();
        cart.execute();

        connection.close();
    }

    public void dropTables() throws SQLException {
        Connection connection = ConnectToDatabase.connectToDatabase();
        PreparedStatement dropGrocery = connection.prepareStatement(dropGroceryTable);
        PreparedStatement dropUserOrder = connection.prepareStatement(dropCartTable);

        dropGrocery.execute();
        dropUserOrder.execute();

        connection.close();
    }
}
