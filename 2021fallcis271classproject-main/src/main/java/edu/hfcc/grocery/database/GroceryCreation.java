package edu.hfcc.grocery.database;

import com.github.javafaker.Faker;
import edu.hfcc.grocery.Objects.Grocery;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GroceryCreation {
    static Faker faker = new Faker();
    public static List<Grocery> groceries = new ArrayList<>();
    public static DecimalFormat df = new DecimalFormat("#.00");
    static DecimalFormat df2 = new DecimalFormat("#.#");

    public static void createListOfVegetable() {
        int x = 0;
        while(x < 25) {
            Grocery item = new Grocery();
            boolean itemExists = false;
            String vegetable = faker.food().vegetable();
            item.name = vegetable;
            item.type = "vegetable";
            item.price = df2.format(Math.random() * (6 - 1) + 1);
            item.price = df.format(Double.parseDouble(item.price));
            item.quantity = 1000;
            if(!groceries.isEmpty()) {
                for(Grocery g : groceries) {
                    if(g.name.equals(vegetable)) {
                        itemExists = true;
                        break;
                    }
                }
            }
            if(!itemExists) {
                groceries.add(item);
                x++;
            }
        }
    }

    public static void createListOfFruits() {
        int x = 0;
        while(x < 25) {
            Grocery item = new Grocery();
            boolean itemExists = false;
            String fruit = faker.food().fruit();
            item.name = fruit;
            item.type = "fruit";
            item.price = df2.format(Math.random() * (8 - 3) + 3);
            item.price = df.format(Double.parseDouble(item.price));
            item.quantity = 1000;
            if(!groceries.isEmpty()) {
                for(Grocery g : groceries) {
                    if(g.name.equals(fruit)) {
                        itemExists = true;
                        break;
                    }
                }
            }
            if(!itemExists) {
                groceries.add(item);
                x++;
            }
        }
    }

    public static void createAndAddDb() throws SQLException {
        CreateTables createTables = new CreateTables();
        AddToDatabase addToDb = new AddToDatabase();

        createTables.createTables();
        addToDb.addGroceries(groceries);
    }

    public static void run() throws SQLException {
        createListOfFruits();
        createListOfVegetable();
        createAndAddDb();
    }
}