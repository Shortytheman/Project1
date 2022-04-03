package com.example.project1.repository;

import com.example.project1.model.Item;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

@Repository
public class WebshopRepository {

  private static Connection connection;
  private static Environment environment;

  public ArrayList<Item> showAll() {
    connectToSql();
    ArrayList<Item> itemList = new ArrayList<>();

    try {
      String query = "select * from heroku_2ba92db6c587479.webshop";
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next()) {
      int itemID = resultSet.getInt("item_id");
      String name = resultSet.getString("name");
      double price = resultSet.getDouble("price");
      itemList.add(new Item(itemID,name,price));
      }

    } catch (Exception e) {
      System.out.println("fejl" + e);
    }
    return itemList;
  }


  static Connection connectToSql() {
    if (connection != null) {
      return connection;
    } else {
      try {
        connection = DriverManager.getConnection(environment.getProperty("spring.datasource.url"), environment.getProperty("spring.datasource.username"),
            environment.getProperty("spring.datasource.password"));
        System.out.println("Forbundet");
      } catch (Exception e) {
        System.out.println("Fejl: " + e);
      }
      return connection;
    }
  }

}
