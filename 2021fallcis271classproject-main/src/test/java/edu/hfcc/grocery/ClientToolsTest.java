package edu.hfcc.grocery;

import edu.hfcc.grocery.Objects.Cart;
import edu.hfcc.grocery.Objects.Grocery;
import edu.hfcc.grocery.client.Client;
import edu.hfcc.grocery.client.ClientTools;
import org.assertj.core.api.Assertions;
import org.assertj.core.internal.bytebuddy.dynamic.scaffold.MethodGraph;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

import static edu.hfcc.grocery.client.ClientTools.*;

public class ClientToolsTest {
    Client client = new Client();

    @Test
    public void testGroceriesOutput() {
        List<Grocery> groceries = new ArrayList<>();
        Grocery g = new Grocery();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        g.id = 1;
        g.name = "name";
        g.type = "type";
        g.price = "3.33";
        g.quantity = 1;

        groceries.add(g);
        client.setGroceriesSelected(groceries);

        String test = "ID: " + g.id + " Name: " + g.name + " Type: " + g.type + " Price: $" + g.price + " Quantity: " + g.quantity + "\r\n";

        System.setOut(printStream);
        System.out.flush();

        ClientTools.outputItems(client);

        String actual = baos.toString();

        Assertions.assertThat(actual).isEqualTo(test);
    }

    @Test
    public void testOutputCart() {
        List<Cart> cart = new ArrayList<>();
        Cart c = new Cart();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        c.id = 1;
        c.name = "name";
        c.type = "type";
        c.price = "3.33";
        c.quantity = 1;

        cart.add(c);
        client.setCart(cart);

        String test = "Number of items in cart: 1\r\nID: " + c.id + " Name: " + c.name + " Type: " + c.type + " Price: $" + c.price + " Quantity: " + c.quantity + "\r\nTotal: $.00\r\n";

        System.setOut(printStream);
        System.out.flush();

        ClientTools.outputCart(client);

        String actual = baos.toString();

        Assertions.assertThat(actual).isEqualTo(test);
    }

    @Test
    public void testCheckIfItemExists() {
        Cart c = new Cart();
        List<Cart> cart = new ArrayList<>();
        c.id = 1;
        c.name = "name";
        c.type = "type";
        c.price = "3.33";
        c.quantity = 1;
        cart.add(c);
        client.setCart(cart);

        Assertions.assertThat(ClientTools.checkIfCartItemExists(client, 1)).isEqualTo(true);
    }

    @Test
    public void testIfItemDoesNotExist() {
        Cart c = new Cart();
        List<Cart> cart = new ArrayList<>();
        c.id = 1;
        c.name = "name";
        c.type = "type";
        c.price = "3.33";
        c.quantity = 1;
        cart.add(c);
        client.setCart(cart);

        Assertions.assertThat(ClientTools.checkIfCartItemExists(client, 2)).isEqualTo(false);
    }

    @Test
    public void testCheckCartQuantity() {
        Cart c = new Cart();
        List<Cart> cart = new ArrayList<>();
        c.id = 1;
        c.name = "name";
        c.type = "type";
        c.price = "3.33";
        c.quantity = 1;
        cart.add(c);
        client.setCart(cart);

        Assertions.assertThat(ClientTools.checkCartQuantity(client, 1, 1)).isEqualTo(true);
    }

    @Test
    public void testWhenQuantityIsNotEnough() {
        Cart c = new Cart();
        List<Cart> cart = new ArrayList<>();
        c.id = 1;
        c.name = "name";
        c.type = "type";
        c.price = "3.33";
        c.quantity = 1;
        cart.add(c);
        client.setCart(cart);

        Assertions.assertThat(ClientTools.checkCartQuantity(client, 1, 2)).isEqualTo(false);
    }

    @Test
    public void testCheckIfGroceryItemExists() {
        List<Grocery> groceries = new ArrayList<>();
        Grocery g = new Grocery();
        g.id = 1;
        g.name = "name";
        g.type = "type";
        g.price = "3.33";
        g.quantity = 1;

        groceries.add(g);
        client.setGroceriesSelected(groceries);

        Assertions.assertThat(ClientTools.checkIfGroceryItemExists(client, 1)).isEqualTo(true);
    }

    @Test
    public void testWhenGroceryDoesNotExist() {
        List<Grocery> groceries = new ArrayList<>();
        Grocery g = new Grocery();
        g.id = 1;
        g.name = "name";
        g.type = "type";
        g.price = "3.33";
        g.quantity = 1;

        groceries.add(g);
        client.setGroceriesSelected(groceries);

        Assertions.assertThat(ClientTools.checkIfGroceryItemExists(client, 2)).isEqualTo(false);
    }

    @Test
    public void testIsValidInput() {
        Assertions.assertThat(ClientTools.isValidInput(1,2, 2)).isEqualTo(2);
    }

    @Test
    public void testCheckQuantity() {
        List<Grocery> groceries = new ArrayList<>();
        Grocery g = new Grocery();
        g.id = 1;
        g.name = "name";
        g.type = "type";
        g.price = "3.33";
        g.quantity = 1;

        groceries.add(g);
        client.setGroceriesSelected(groceries);

        Assertions.assertThat(ClientTools.checkQuantity(client, 1, 1)).isEqualTo(true);
    }

    @Test
    public void checkWhenStockIsNotEnough() {
        List<Grocery> groceries = new ArrayList<>();
        Grocery g = new Grocery();
        g.id = 1;
        g.name = "name";
        g.type = "type";
        g.price = "3.33";
        g.quantity = 1;

        groceries.add(g);
        client.setGroceriesSelected(groceries);

        Assertions.assertThat(ClientTools.checkQuantity(client, 1, 2)).isEqualTo(false);
    }

    @Test
    public void checkSetDenominations() throws IOException {
        ClientTools.setDenominations(client);

        TreeMap<String, Double> denominations = new TreeMap<>();
        denominations.put("quarter", 0.25);
        denominations.put("dime", 0.10);
        denominations.put("nickel", 0.05);
        denominations.put("fifty-dollar", 50.0);
        denominations.put("twenty-dollar", 20.0);
        denominations.put("one-dollar", 1.0);

        denominations = ClientTools.valueSort(denominations);
        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>(denominations);

        Assertions.assertThatObject(sortedMap.keySet()).isEqualToComparingFieldByField(client.getDenominations().keySet());
        Assertions.assertThatObject(sortedMap.values()).isEqualToComparingFieldByField(client.getDenominations().values());
    }

    @Test
    public void testCalculateChange() throws IOException {
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream("10".getBytes()));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        PrintStream stdout = System.out;
        System.setOut(ps);

        client.setOrderTotal(5.40);
        ClientTools.setDenominations(client);
        ClientTools.calculateChange(client);
        System.setIn(stdin);

        Assertions.assertThat(client.getCustomerChange()).isEqualTo(4.60);
    }


}
