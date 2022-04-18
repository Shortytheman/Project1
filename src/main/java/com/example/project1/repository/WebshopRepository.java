package com.example.project1.repository;

import com.example.project1.model.Item;
import com.example.project1.utility.ConnectionManager;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class WebshopRepository {


  //Basket methods -- Vi skal nok også have en basket model som kræver dependency injection i form af en liste af Items.
  //<editor-fold desc="Basket methods">
  private ArrayList<Item> finishedOrder = new ArrayList<>();
  public LinkedHashMap<Item, Integer> showItems() {
    finishedOrder.clear();
    LinkedHashMap<Item, Integer> counting = new LinkedHashMap<>();
    int counter;

    try {
      String query = "select * from heroku_2ba92db6c587479.item";
      Statement statement = ConnectionManager.connectToSql().createStatement();
      ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next()) {
        String name = resultSet.getString("item_name");
        double price = resultSet.getDouble("item_price");
        finishedOrder.add(new Item(name, price));
      }
    } catch (Exception e) {
      System.out.println("fejl" + e);
    }

    for (int i = 0; i < finishedOrder.size(); i++){
      counter = 1;
      for (int j = i + 1; j < finishedOrder.size(); j++){
        if (finishedOrder.get(i).name.equalsIgnoreCase(finishedOrder.get(j).name)) {
          counter++;
          finishedOrder.remove(j);
          j = j - 1;
          //In some way this loop has to make sure that when one object gets put into the hashmap (Line 48),
          // it removes the rest of the same instances of that item. BUT HOW?!? Maybe i fixed with the j=j-1??? looks like it <3
        }
      }
      counting.put(finishedOrder.get(i),counter);
    }
    return counting;
  }


  public void addItem(Item item){
  try {
    String query = "INSERT INTO heroku_2ba92db6c587479.item(item_name, item_price) values (?,?)";
    PreparedStatement prepareStatement = ConnectionManager.connectToSql().prepareStatement(query);
    prepareStatement.setString(1,item.name);
    prepareStatement.setDouble(2,item.price);
    prepareStatement.executeUpdate();
  } catch (SQLException sqlException){
    System.out.println("Error in creation of item");
    sqlException.printStackTrace();
  }
    finishedOrder.add(new Item(item.name, item.price));
  }


  public void deleteItem(String name){
    // Deletes a single instance of an item
    try {
      String query = "DELETE FROM heroku_2ba92db6c587479.item WHERE item_name = ? LIMIT 1";
      PreparedStatement prepareStatement = ConnectionManager.connectToSql().prepareStatement(query);
      prepareStatement.setString(1,name);
      prepareStatement.executeUpdate();

    } catch (SQLException sqlException){
      System.out.println("Error in deletion of item");
      sqlException.printStackTrace();
    }
  }

  public void addAnother(String name) {
    double price = 0;
    String thisName = "";
    for (int i = 0; i < finishedOrder.size(); i++) {
      if (finishedOrder.get(i).name.equalsIgnoreCase(name)) {
        price = finishedOrder.get(i).price;
        thisName = finishedOrder.get(i).name;
      }
    }
    if (price != 0 && !thisName.equalsIgnoreCase("")) {
      try {
        String query = "INSERT INTO heroku_2ba92db6c587479.item(item_name, item_price) VALUES (?,?)";
        PreparedStatement prepareStatement = ConnectionManager.connectToSql().prepareStatement(query);
        prepareStatement.setString(1, thisName);
        prepareStatement.setDouble(2, price);
        prepareStatement.executeUpdate();
      } catch (SQLException sqlException) {
        System.out.println("Error in deletion of item");
        sqlException.printStackTrace();
      }
    }
  }

  public int total(){
    int total = 0;
      for (Map.Entry<Item, Integer> entry : showItems().entrySet()){
      total += (entry.getValue() * entry.getKey().price);
    }
    return total;
  }
  //</editor-fold>



}