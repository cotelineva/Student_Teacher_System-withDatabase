package Jdbc;

import Model.Person;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonJdbcRepository implements CRUD_Repo<Person> {
    private List<Person> people = new ArrayList<>();

    @Override
    public Person findOne(int id) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            PreparedStatement stmt = con.prepareStatement("select * from Person where Person.idP = ?");
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                String name = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                Person p = new Person(name, lastName);
                return p;
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Iterable<Person> findAll() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Person");

            Person p = null;
            this.people.clear();

            if(rs.next()){
                do{
                    String name = rs.getString("firstName");
                    String lastName = rs.getString("lastName");

                    p = new Person(name,lastName);
                    if(people.contains(p) == false){
                        people.add(p);
                    }
                }while(rs.next());
                return people;
            }
            else{
                return null;
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Person save(Person entity) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");


            PreparedStatement stmt = con.prepareStatement("insert into Person(firstName,lastName) values(?,?);");
            stmt.setString(1,entity.getFirstName());
            stmt.setString(2,entity.getLastName());
            int row = stmt.executeUpdate();

            if(row == 0) {
                return entity;
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Person delete(int id) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            Statement stmt1 = con.createStatement();
            ResultSet rs = stmt1.executeQuery("select * from Person");

            Person p = new Person("","");
            while(rs.next()) {
                int idP = rs.getInt("idP");
                String name = rs.getString("firstName");
                String lastName = rs.getString("lastName");

                if(idP == id){
                    p = new Person(name, lastName);
                }
            }

            PreparedStatement stmt = con.prepareStatement("delete from Person where Person.idP = ?");
            stmt.setInt(1,id);
            int row = stmt.executeUpdate();


            if(row != 0){
                this.people.remove(p);
                return p;
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Person update(Person entity) {

        return null;
    }
}
