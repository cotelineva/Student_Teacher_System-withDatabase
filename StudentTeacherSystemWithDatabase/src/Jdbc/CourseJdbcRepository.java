package Jdbc;

import Model.Course;
import Model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseJdbcRepository implements CRUD_Repo<Course> {
    List<Course> courses = new ArrayList<>();

    @Override
    public Course findOne(int id) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            PreparedStatement stmt = con.prepareStatement("select * from Course where Course.idC = ?");
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                String name = rs.getString("name");
                int maxE = rs.getInt("maxEnrollment");
                int credits = rs.getInt("credits");
                int idT = rs.getInt("idT");
                int idC = rs.getInt("idC");

                PreparedStatement stmt2 = con.prepareStatement("select * from Teacher where Teacher.idT = ?");
                stmt2.setInt(1,idT);
                ResultSet rs2 = stmt2.executeQuery();

                if(rs2.next()){
                    int idP = rs2.getInt("idP");

                    PreparedStatement stmt3 = con.prepareStatement("select * from Person where Person.idP = ?");
                    stmt3.setInt(1,idP);
                    ResultSet rs3 = stmt3.executeQuery();

                    if(rs3.next()){
                        String firstName = rs3.getString("firstName");
                        String lastName = rs3.getString("lastName");

                        Course c = new Course(name,maxE,credits,new Teacher(firstName,lastName),idC);
                        return c;
                    }
                }
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Iterable<Course> findAll() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Course");

            Course c = null;
            this.courses.clear();

            if(rs.next()){
                do{
                    String name = rs.getString("name");
                    int maxE = rs.getInt("maxEnrollment");
                    int credits = rs.getInt("credits");
                    int idT = rs.getInt("idT");
                    int idC = rs.getInt("idC");

                    PreparedStatement stmt2 = con.prepareStatement("select * from Teacher where Teacher.idT = ?");
                    stmt2.setInt(1,idT);
                    ResultSet rs2 = stmt2.executeQuery();

                    if(rs2.next()){
                        int idP = rs2.getInt("idP");

                        PreparedStatement stmt3 = con.prepareStatement("select * from Person where Person.idP = ?");
                        stmt3.setInt(1,idP);
                        ResultSet rs3 = stmt3.executeQuery();

                        if(rs3.next()){
                            String firstName = rs3.getString("firstName");
                            String lastName = rs3.getString("lastName");

                            c = new Course(name,maxE,credits,new Teacher(firstName,lastName),idC);
                            if(courses.contains(c) == false){
                                courses.add(c);
                            }
                        }
                    }
                }while(rs.next());
                return courses;
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
    public Course save(Course entity) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");


            String name = entity.getName();
            int maxE = entity.getMaxEnrollment();
            int credits = entity.getCredits();
            String firstName = entity.getTeacher().getFirstName();
            String lastName = entity.getTeacher().getLastName();


            PreparedStatement stmt2 = con.prepareStatement("select * from Person where Person.firstName = ?" +
                    "and Person.lastName = ?;");
            stmt2.setString(1,firstName);
            stmt2.setString(2,lastName);
            ResultSet rs2 = stmt2.executeQuery();

            int idP = 0;
            int idT = 0;

            if(rs2.next()){
                idP = rs2.getInt("idP");

                PreparedStatement stmt3 = con.prepareStatement("select * from Teacher where Teacher.idP = ?");
                stmt3.setInt(1,idP);
                ResultSet rs3 = stmt3.executeQuery();

                if(rs3.next()){
                    idT = rs3.getInt("idT");
                }
            }

            PreparedStatement stmt = con.prepareStatement("insert into Course(name,maxEnrollment,credits,idT) values(?,?,?,?);");
            stmt.setString(1,name);
            stmt.setInt(2,maxE);
            stmt.setInt(3,credits);
            stmt.setInt(4,idT);
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
    public Course delete(int id) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            PreparedStatement stmt = con.prepareStatement("select * from Course where Course.idC = ?;");
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            Course c = null;

            if(rs.next()){
                String name = rs.getString("name");
                int maxE = rs.getInt("maxEnrollment");
                int credits = rs.getInt("credits");
                int idT = rs.getInt("idT");
                int idC = rs.getInt("idC");

                PreparedStatement stmt2 = con.prepareStatement("select * from Teacher where Teacher.idT = ?");
                stmt2.setInt(1,idT);
                ResultSet rs2 = stmt2.executeQuery();

                if(rs2.next()){
                    int idP = rs2.getInt("idP");

                    PreparedStatement stmt3 = con.prepareStatement("select * from Person where Person.idP = ?");
                    stmt3.setInt(1,idP);
                    ResultSet rs3 = stmt3.executeQuery();

                    if(rs3.next()){
                        String firstName = rs3.getString("firstName");
                        String lastName = rs3.getString("lastName");

                        c = new Course(name,maxE,credits,new Teacher(firstName,lastName),idC);
                    }
                }
            }

            PreparedStatement stmt2 = con.prepareStatement("delete from Course where Course.idC = ?");
            stmt2.setInt(1,id);
            int row = stmt2.executeUpdate();


            if(row != 0){
                this.courses.remove(c);
                return c;
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Course update(Course entity) {
        return null;
    }
}
