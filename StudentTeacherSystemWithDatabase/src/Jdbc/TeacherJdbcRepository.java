package Jdbc;

import Model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeacherJdbcRepository implements CRUD_Repo<Teacher> {
    List<Teacher> teachers = new ArrayList<>();

    @Override
    public Teacher findOne(int id) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            PreparedStatement stmt = con.prepareStatement("select * from Teacher where Teacher.idT = ?");
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                int idP = rs.getInt("idP");

                PreparedStatement stmt2 = con.prepareStatement("select * from Person where Person.idP = ?");
                stmt2.setInt(1,idP);
                ResultSet rs2 = stmt2.executeQuery();

                if(rs2.next()){
                    String name = rs2.getString("firstName");
                    String lastName = rs2.getString("lastName");

                    Teacher t = new Teacher(name,lastName);
                    return t;
                }
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Iterable<Teacher> findAll() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Teacher");

            Teacher t = null;
            this.teachers.clear();

            if(rs.next()){
                do{
                    int idP = rs.getInt("idP");

                    PreparedStatement stmt2 = con.prepareStatement("select * from Person where Person.idP = ?");
                    stmt2.setInt(1,idP);
                    ResultSet rs2 = stmt2.executeQuery();

                    if(rs2.next()){
                        String name = rs2.getString("firstName");
                        String lastName = rs2.getString("lastName");

                        t = new Teacher(name,lastName);
                    }
                    if(teachers.contains(t) == false){
                        teachers.add(t);
                    }
                }while(rs.next());
                return teachers;
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
    public Teacher save(Teacher entity) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");


            String fName = entity.getFirstName();
            String lName = entity.getLastName();


            PreparedStatement stmt2 = con.prepareStatement("select Person.idP from Person where Person.firstName = ?" +
                                                                "and Person.lastName = ?;");
            stmt2.setString(1,fName);
            stmt2.setString(2,lName);
            ResultSet rs2 = stmt2.executeQuery();

            int idP = 0;

            if(rs2.next()){
                idP = rs2.getInt("idP");
            }
            else{
                System.out.println("Person with that name does not exist!");
            }


            PreparedStatement stmt = con.prepareStatement("insert into Teacher(idP) values(?);");
            stmt.setInt(1,idP);
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
    public Teacher delete(int id) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            PreparedStatement stmt = con.prepareStatement("select * from Teacher where Teacher.idT = ?;");
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            Teacher t = null;
            int idT = 0;

            if(rs.next()){
                idT = rs.getInt("idT");
                int idP = rs.getInt("idP");

                PreparedStatement stmt2 = con.prepareStatement("select * from Person where Person.idP = ?");
                stmt2.setInt(1,idP);
                ResultSet rs2 = stmt2.executeQuery();

                if(rs2.next()){
                    String name = rs2.getString("firstName");
                    String lastName = rs2.getString("lastName");

                    t = new Teacher(name,lastName);
                }
            }

            PreparedStatement stmt2 = con.prepareStatement("delete from Teacher where Teacher.idT = ?");
            stmt2.setInt(1,idT);
            int row = stmt2.executeUpdate();


            if(row != 0){
                this.teachers.remove(t);
                return t;
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Teacher update(Teacher entity) {
        return null;
    }
}
