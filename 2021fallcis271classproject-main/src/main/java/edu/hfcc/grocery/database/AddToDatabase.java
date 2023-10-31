package edu.hfcc.grocery.database;

import edu.hfcc.grocery.Objects.Grocery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AddToDatabase {

    public void addGroceries(List<Grocery> groceries) throws SQLException {
        Connection connection = ConnectToDatabase.connectToDatabase();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO groceries(name, type, price, quantity) VALUES(?,?,?,?)");

        for(Grocery g : groceries) {
            preparedStatement.setString(1, g.name);
            preparedStatement.setString(2, g.type);
            preparedStatement.setDouble(3, Double.parseDouble(g.price));
            preparedStatement.setInt(4, g.quantity);
            preparedStatement.addBatch();
        }

        preparedStatement.executeBatch();

        connection.close();
    }
}
