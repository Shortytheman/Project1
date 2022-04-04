package com.example.project1.repository;

import com.example.project1.model.Item;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class WebshopRepository {

  private Connection connection;
  private Environment environment;
  private ArrayList<Item> finishedOrder = new ArrayList<>();

  public WebshopRepository(Environment environment){
  this.environment = environment;
  }

  public ArrayList<Item> showItems() {
    connectToSql();
    try {
      String query = "select * from heroku_2ba92db6c587479.item";
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next()) {
      int itemID = resultSet.getInt("item_id");
      String name = resultSet.getString("item_name");
      double price = resultSet.getDouble("item_price");
      finishedOrder.add(new Item(itemID,name,price));
      }

    } catch (Exception e) {
      System.out.println("fejl" + e);
    }
    return finishedOrder;
  }

  public void addItem(Item item){
  connectToSql();
  try {
    String query = "INSERT INTO heroku_2ba92db6c587479.item(item_id, item_name, item_price) values (?,?,?)";
    PreparedStatement prepareStatement = connection.prepareStatement(query);
    prepareStatement.setInt(1,item.id);
    prepareStatement.setString(2,item.name);
    prepareStatement.setDouble(3,item.price);
    prepareStatement.executeUpdate();

  } catch (SQLException sqlException){
    System.out.println("Error in creation of item");
    sqlException.printStackTrace();
  }
  }


  public void removeItem(int id){
  connectToSql();
    try {
      String query = "REMOVE FROM heroku_2ba92db6c587479.item WHERE id = ?";
      PreparedStatement prepareStatement = connection.prepareStatement(query);
      prepareStatement.setInt(1,id);
      prepareStatement.executeUpdate();

    } catch (SQLException sqlException){
      System.out.println("Error in deletion of item");
      sqlException.printStackTrace();
    }
  }

  public int total(){
    int total = 0;
    for (int i = 0; i < finishedOrder.size();i++){
      total += finishedOrder.get(i).price;
    }
    return total;
  }


  public void connectToSql() {
    if (connection == null) {
      try {
        connection = DriverManager.getConnection(environment.getProperty("spring.datasource.url"), environment.getProperty("spring.datasource.username"),
            environment.getProperty("spring.datasource.password"));
        System.out.println("Forbundet");
      } catch (Exception e) {
        System.out.println("Fejl: " + e);
      }
    }
  }
}