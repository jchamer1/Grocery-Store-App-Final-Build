package edu.hfcc.grocery.client;

import com.sun.source.tree.Tree;
import edu.hfcc.grocery.Objects.Cart;
import edu.hfcc.grocery.Objects.Grocery;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class ClientTools {
    static DecimalFormat df = new DecimalFormat("#.00");
    static DecimalFormat round = new DecimalFormat("#.#");

    public static void itemSelection(Client client) {
        int menuInput;
        int quantity;
        int fruitMin = 1;
        int fruitMax = client.getFruitAmount();
        int vegetableMin = client.getFruitAmount() + 1;
        int vegetableMax = client.getVegetableAmount();
        List<Integer> userItems = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the ID for the item you want or enter -1 to exit: ");
        System.out.print("Selection: ");
        menuInput = in.nextInt();
        if(client.getMenuOption() == 1) {
            menuInput = isValidInput(fruitMin, fruitMax, menuInput);
        } else if(client.getMenuOption() == 2) {
            menuInput = isValidInput(vegetableMin, vegetableMax, menuInput);
        }
        checkIfGroceryItemExists(client, menuInput);
        while(menuInput != -1) {
            userItems.add(menuInput);
            System.out.println("How many would you like: ");
            System.out.print("Selection: ");
            quantity = in.nextInt();
            while(quantity < 0) {
                System.out.println("Invalid input. Please try again: ");
                quantity = in.nextInt();
            }
            while(!checkQuantity(client, menuInput, quantity)) {
                System.out.println("Invalid input. Please try again: ");
                quantity = in.nextInt();
            }
            quantities.add(quantity);
            System.out.println("Enter the ID for the item you want or enter -1 to exit: ");
            System.out.print("Selection: ");
            menuInput = in.nextInt();
            if(menuInput == -1) {
                break;
            } else if(client.getMenuOption() == 1) {
                menuInput = isValidInput(fruitMin, fruitMax, menuInput);
            } else if(client.getMenuOption() == 2) {
                menuInput = isValidInput(vegetableMin, vegetableMax, menuInput);
            }
            checkIfGroceryItemExists(client, menuInput);
        }

        client.setIdSelection(userItems);
        client.setQuantityOfItem(quantities);
    }

    public static void outputItems(Client client) {
        for(Grocery g : client.getGroceriesSelected()) {
            System.out.println("ID: " + g.id + " Name: " + g.name + " Type: " + g.type + " Price: $" + df.format(Double.parseDouble(g.price)) + " Quantity: " + g.quantity);
        }
    }

    public static void outputCart(Client client) {
        System.out.println("Number of items in cart: " + client.getCart().size());
        for(Cart c : client.getCart()) {
            System.out.println("ID: " + c.id + " Name: " + c.name + " Type: " + c.type + " Price: $" + df.format(Double.parseDouble(c.price)) + " Quantity: " + c.quantity);
        }
        System.out.println("Total: $" + df.format(client.getOrderTotal()));
    }

    public static void cartRemovalMenu(Client client) {
        List<Integer> idOfItems = new ArrayList<>();
        List<Integer> quantityToRemove = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        int menuInput = 0;
        int quantity;

        outputCart(client);
        System.out.println("Enter the ID of the item you want to remove or -1 to exit: ");
        System.out.print("Selection: ");
        menuInput = in.nextInt();
        while (!checkIfCartItemExists(client, menuInput)) {
            System.out.println("Item ID doesn't match any items in the cart. Please try again.");
            System.out.print("Selection: ");
            menuInput = in.nextInt();
        }

        while (menuInput != -1) {
            idOfItems.add(menuInput);
            System.out.println("Enter the quantity you want to remove: ");
            quantity = in.nextInt();
            while (quantity < 0) {
                System.out.println();
                System.out.println("Invalid quantity selected. Please try again.");
                System.out.print("Selection: ");
                quantity = in.nextInt();
            }
            while (!checkCartQuantity(client, menuInput, quantity)) {
                System.out.println("Invalid quantity selected. Please try again.");
                System.out.print("Selection: ");
                quantity = in.nextInt();
            }
            quantityToRemove.add(quantity);
            System.out.println("Enter the ID of the item you want to remove or -1 to exit: ");
            System.out.print("Selection: ");
            menuInput = in.nextInt();
            while (!checkIfCartItemExists(client, menuInput)) {
                System.out.println("Item ID doesn't match any items in the cart. Please try again.");
                System.out.print("Selection: ");
                menuInput = in.nextInt();
            }
        }

        client.setGroceriesToRemove(idOfItems);
        client.setQuantityToRemove(quantityToRemove);
    }

    public static boolean checkIfCartItemExists(Client client, int itemId) {
        boolean itemExists = false;
        if(itemId == -1) {
            return true;
        }
        for(Cart c : client.getCart()) {
            if(itemId == c.id) {
                itemExists = true;
                break;
            }
        }

        return itemExists;
    }

    public static boolean checkCartQuantity(Client client, int itemId, int quantity) {
        boolean isQuantityValid = false;
        for(Cart c : client.getCart()) {
            if(c.id == itemId) {
                if(c.quantity >= quantity) {
                    isQuantityValid = true;
                    break;
                }
            }
        }
        return isQuantityValid;
    }

    public static boolean checkIfGroceryItemExists(Client client, int menuInput) {
        boolean itemExists = false;
        for(Grocery g : client.getGroceriesSelected()) {
            if(g.id == menuInput) {
                itemExists = true;
                break;
            }
        }

        return itemExists;
    }

    public static int isValidInput(int min, int max, int menuInput) {
        Scanner in = new Scanner(System.in);
        while(menuInput < min || menuInput > max) {
            System.out.println("Invalid input please try again:");
            System.out.print("Selection: ");
            menuInput = in.nextInt();
        }

        return menuInput;
    }

    public static boolean checkQuantity(Client client, int groceryId, int quantity) {
        boolean quantityIsEnough = false;

        for(Grocery g : client.getGroceriesSelected()) {
            if(g.id == groceryId) {
                if(g.quantity >= quantity) {
                    quantityIsEnough = true;
                    break;
                }
            }
        }

        return quantityIsEnough;
    }

    public static void setDenominations(Client client) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader("src/main/resources/data/money.txt"));
        String line;
        String[] splitStr;
        TreeMap<String, Double> map = new TreeMap<>();

        while((line = file.readLine()) != null) {
            splitStr = line.split(",");
            map.put(splitStr[0], Double.parseDouble(splitStr[1]));
        }

        map = valueSort(map);

        LinkedHashMap<String, Double> hashMap = new LinkedHashMap<>(map);

        client.setDenomination(hashMap);
    }


    public static void checkout(Client client) {
        if(client.getOrderTotal() != 0) {
            calculateChange(client);

            System.out.println("Change: $" + df.format(client.getCustomerChange()));
            System.out.println("Number of bills returned: " + client.getNumberOfBills());
        }
    }

    public static double getCustomerMoney(Client client) {
        Scanner in = new Scanner(System.in);
        double money;
        System.out.println("Enter the amount of money you are using: ");
        money = in.nextDouble();
        while(money < client.getOrderTotal()) {
            System.out.println("Not enough money. Please try again: ");
            money = in.nextDouble();
        }


        return money;
    }

    public static void calculateChange(Client client) {
        LinkedHashMap<String, Integer> numberOfBills = createListOfBills(client);
        double money = getCustomerMoney(client);
        double change = money - client.getOrderTotal();
        change = Double.parseDouble(round.format(change));
        List<String> keys = new ArrayList<>(client.getDenominations().keySet());
        Collections.reverse(keys);

        while (change != 0) {
            for (String s : keys) {
                double d = client.getDenominations().get(s);
                if (change >= d) {
                    int count = numberOfBills.get(s);
                    change = (change - d);
                    change = Math.round(change * 100.0) / 100.0;
                    numberOfBills.put(s, count + 1);
                    break;
                }
            }
        }

        client.setCustomerChange(money - client.getOrderTotal());
        client.setNumberOfBills(numberOfBills);
    }

    public static LinkedHashMap<String, Integer> createListOfBills(Client client) {
        LinkedHashMap<String, Integer> numberOfBills = new LinkedHashMap<>();

        numberOfBills.put("fifty-dollar", 0);
        numberOfBills.put("twenty-dollar", 0);
        numberOfBills.put("one-dollar", 0);
        numberOfBills.put("quarter", 0);
        numberOfBills.put("dime", 0);
        numberOfBills.put("nickel", 0);

        return numberOfBills;
    }

    public static <K, V extends Comparable<V> > TreeMap<K, V>
    valueSort(final TreeMap<K, V> map)
    {
        Comparator<K> valueComparator = new Comparator<K>()
        {

            public int compare(K k1, K k2)
            {

                int comp = map.get(k1).compareTo(map.get(k2));

                if (comp == 0)
                    return 1;

                else
                    return comp;
            }
        };

        TreeMap<K, V> sorted = new TreeMap<K, V>(valueComparator);

        sorted.putAll(map);

        return sorted;
    }

}
