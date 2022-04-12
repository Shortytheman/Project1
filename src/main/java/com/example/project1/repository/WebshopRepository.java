package com.example.project1.repository;

import com.example.project1.model.Item;
import com.example.project1.utility.ConnectionManager;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

@Repository
public class WebshopRepository {

  private ArrayList<Item> finishedOrder = new ArrayList<>();


  public HashMap<Item, Integer> showItems() {
    finishedOrder.clear();
    HashMap<Item, Integer> counting = new HashMap<>();
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
          //In some way this loop has to make sure that when one object gets put into the hashmap (Line 45),
          // it removes the rest of the same instances of that item. BUT HOW?!? Maybe i fixed with the j=j-1???
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

  //public int amount(String name){
    //int amountCounter = 0;
    //Skal kunne finde hvor mange der er af hvert item efter item_name og returnere antallet.
    //}

  public void addAnother(String name) {
    double price = 0;
    String thisName = "";
    for (int i = 0; i < finishedOrder.size(); i++) {
      if (finishedOrder.get(i).name.equalsIgnoreCase(name)) {
        price = finishedOrder.get(i).price;
        thisName = finishedOrder.get(i).name;
      }
      finishedOrder.add(new Item(name, price));
    }
  }

  public int total(){
    int total = 0;
    for (int i = 0; i < finishedOrder.size();i++){
      total += finishedOrder.get(i).price;
    }
    return total;
  }
}