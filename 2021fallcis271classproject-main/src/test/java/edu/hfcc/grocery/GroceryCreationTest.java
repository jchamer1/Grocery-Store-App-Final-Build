package edu.hfcc.grocery;

import edu.hfcc.grocery.Objects.Grocery;
import edu.hfcc.grocery.database.ConnectToDatabase;
import edu.hfcc.grocery.database.GroceryCreation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroceryCreationTest {

    @Test
    public void testGroceryTableCreation() throws SQLException {
        List<Grocery> groceries = new ArrayList<>();
        GroceryCreation groceryCreation = new GroceryCreation();
        groceryCreation.run();

        Connection connection = ConnectToDatabase.connectToDatabase();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM groceries");
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()) {
            Grocery grocery = new Grocery();
            grocery.name = resultSet.getString(2);
            grocery.type = resultSet.getString(3);
            grocery.price = groceryCreation.df.format((resultSet.getDouble(4)));
            grocery.quantity = resultSet.getInt(5);
            groceries.add(grocery);
        }

        for(int x = 0; x < groceries.size(); x++) {
            Assertions.assertThatObject(groceries.get(x)).isEqualToComparingFieldByField(groceryCreation.groceries.get(x));
        }
    }
}