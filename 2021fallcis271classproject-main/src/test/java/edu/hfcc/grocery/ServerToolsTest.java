package edu.hfcc.grocery;

import edu.hfcc.grocery.Objects.Cart;
import edu.hfcc.grocery.Objects.Grocery;
import edu.hfcc.grocery.client.Client;
import edu.hfcc.grocery.database.ConnectToDatabase;
import edu.hfcc.grocery.server.Server;
import edu.hfcc.grocery.server.ServerTools;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerToolsTest {
    Client client = new Client();

    @Test
    public void testQueries() throws SQLException {
        ServerTools.emptyCartTable();
        Connection connection = ConnectToDatabase.connectToDatabase();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM groceries WHERE type = 'fruit'");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Grocery> groceries = new ArrayList<>();
        client.setMenuOption(1);
        ServerTools.queryGroceries(client);

        while(resultSet.next()) {
            Grocery grocery = new Grocery();
            grocery.id = resultSet.getInt(1);
            grocery.name = resultSet.getString(2);
            grocery.type = resultSet.getString(3);
            grocery.price = Double.toString(resultSet.getDouble(4));
            grocery.quantity = resultSet.getInt(5);
            groceries.add(grocery);
        }

        for(int x = 0; x < groceries.size(); x++) {
            Assertions.assertThatObject(groceries.get(x)).isEqualToComparingFieldByField(client.getGroceriesSelected().get(x));
        }

        connection.close();
    }

    @Test
    public void testCreateCart() throws SQLException {
        ServerTools.emptyCartTable();
        List<Integer> idSelection = new ArrayList<>();
        idSelection.add(1);
        List<Integer> quantity = new ArrayList<>();
        quantity.add(1);
        client.setQuantityOfItem(quantity);
        client.setIdSelection(idSelection);
        client.setMenuOption(1);
        ServerTools.queryGroceries(client);
        ServerTools.createCart(client);

        Connection connection = ConnectToDatabase.connectToDatabase();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cart");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Cart> cartItems = new ArrayList<>();

        while(resultSet.next()) {
            Cart cart = new Cart();
            cart.id = resultSet.getInt("id");
            cart.name = resultSet.getString("name");
            cart.type = resultSet.getString("type");
            cart.price = Double.toString(resultSet.getDouble("price"));
            cart.quantity = resultSet.getInt("quantity");
            cartItems.add(cart);
        }

        System.out.println(cartItems.get(0).quantity);
        System.out.println(client.getCart().get(0).quantity);



        for(Cart c : client.getCart()) {
            Assertions.assertThatObject(c).isEqualToComparingFieldByField(cartItems.get(client.getCart().indexOf(c)));
        }

        connection.close();
    }

    @Test
    public void testDeleteFromCart() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        List<Integer> quantities = new ArrayList<>();
        quantities.add(1);
        client.setGroceriesToRemove(ids);
        client.setQuantityToRemove(quantities);
        ServerTools.removeFromCart(client);
        ServerTools.removeZeroQuantityItems(client);

        Connection connection = ConnectToDatabase.connectToDatabase();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cart");
        ResultSet resultSet = preparedStatement.executeQuery();

        Assertions.assertThat(resultSet.next()).isEqualTo(false);
    }

    @Test
    public void testViewCart() throws SQLException {
        testCreateCart();
        List<Cart> cart = new ArrayList<>();
        cart = client.getCart();
        ServerTools.viewCart(client);

        Assertions.assertThatObject(cart.get(0)).isEqualToComparingFieldByField(client.getCart().get(0));
    }

    @Test
    public void testUpdateStock() throws SQLException {
        List<Cart> cart = new ArrayList<>();
        testCreateCart();
        cart.addAll(client.getCart());
        Connection connection = ConnectToDatabase.connectToDatabase();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM groceries");
        ServerTools.updateStock(client);

        ServerTools.emptyCartTable();
    }
}
