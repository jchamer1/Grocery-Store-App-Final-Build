package edu.hfcc.grocery.client;

import edu.hfcc.grocery.Objects.Cart;
import edu.hfcc.grocery.Objects.Grocery;
import edu.hfcc.grocery.server.GroceryStatus;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

public class Client implements Serializable {

    private int menuOption;
    private List<Integer> idSelection;
    private Enum<GroceryStatus> status;
    private List<Grocery> groceriesSelected;
    private double orderTotal;
    private List<Cart> cart;
    private List<Integer> quantityOfItem;
    private List<Integer> groceriesToRemove;
    private List<Integer> quantityToRemove;
    private double customerChange;
    private LinkedHashMap<String, Double> denominations;
    private LinkedHashMap<String, Integer> numberOfBills;
    private String roundOrderTotal;
    private DecimalFormat df = new DecimalFormat("#.#");
    private int fruitAmount;
    private int vegetableAmount;

    public Client() {
        this.menuOption = getMenuOption();
        this.idSelection = getIdSelection();
    }

    public void setMenuOption(int menuOption) { this.menuOption = menuOption; }
    public int getMenuOption() { return menuOption; }

    public void setIdSelection(List<Integer> idSelection) { this.idSelection = idSelection; }
    public List<Integer> getIdSelection() { return idSelection; }

    public Enum<GroceryStatus> getStatus() { return status; }
    public void setStatus(Enum<GroceryStatus> status) { this.status = status; }

    public List<Grocery> getGroceriesSelected() { return groceriesSelected; }
    public void setGroceriesSelected(List<Grocery> groceriesSelected) { this.groceriesSelected = groceriesSelected; }

    public double getOrderTotal() { return orderTotal; }
    public void setOrderTotal(double orderTotal) {
        roundOrderTotal = df.format(orderTotal);
        this.orderTotal = Double.parseDouble(roundOrderTotal);
    }

    public List<Cart> getCart() { return cart; }
    public void setCart(List<Cart> cart) { this.cart = cart; }

    public List<Integer> getQuantityOfItem() { return quantityOfItem; }
    public void setQuantityOfItem(List<Integer> quantityOfItem) { this.quantityOfItem = quantityOfItem; }

    public List<Integer> getGroceriesToRemove() { return groceriesToRemove; }
    public void setGroceriesToRemove(List<Integer> groceriesToRemove) { this.groceriesToRemove = groceriesToRemove; }

    public List<Integer> getQuantityToRemove() { return quantityToRemove; }
    public void setQuantityToRemove(List<Integer> quantityToRemove) { this.quantityToRemove = quantityToRemove; }


    public void setCustomerChange(double customerChange) { this.customerChange = customerChange; }
    public double getCustomerChange() { return customerChange; }

    public LinkedHashMap<String, Double> getDenominations() { return denominations; }
    public void setDenomination(LinkedHashMap<String, Double> denominations) { this.denominations = denominations; }

    public LinkedHashMap<String, Integer> getNumberOfBills() { return numberOfBills; }
    public void setNumberOfBills(LinkedHashMap<String, Integer> numberOfBills) { this.numberOfBills = numberOfBills; }

    public int getFruitAmount() { return fruitAmount; }
    public void setFruitAmount(int fruitAmount) { this.fruitAmount = fruitAmount; }

    public int getVegetableAmount() { return vegetableAmount; }
    public void setVegetableAmount(int vegetableAmount) { this.vegetableAmount = vegetableAmount; }


}
