package Jdbc;

import Model.Student;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class StudentJdbcRepository implements CRUD_Repo<Student> {
    private List<Student> students = new ArrayList<>();

    @Override
    public Student findOne(int id) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            PreparedStatement stmt = con.prepareStatement("select * from Student where Student.studentId = ?");
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            Student s;

            if(rs.next()) {
                int idS = rs.getInt("studentId");
                int credits = rs.getInt("totalCredits");
                int idP = rs.getInt("idP");

                PreparedStatement stmt2 = con.prepareStatement("select * from Person where Person.idP = ?");
                stmt2.setInt(1,idP);
                ResultSet rs2 = stmt2.executeQuery();

                if(rs2.next()){
                    String name = rs2.getString("firstName");
                    String lastName = rs2.getString("lastName");

                    s = new Student(name,lastName,idS,credits);
                    return s;
                }

            }

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Iterable<Student> findAll() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Student");

            Student s = new Student("","",0,0);
            this.students.clear();

            if(rs.next()){
                do{
                    int idS = rs.getInt("studentId");
                    int credits = rs.getInt("totalCredits");
                    int idP = rs.getInt("idP");

                    PreparedStatement stmt2 = con.prepareStatement("select * from Person where Person.idP = ?");
                    stmt2.setInt(1,idP);
                    ResultSet rs2 = stmt2.executeQuery();

                    if(rs2.next()){
                        String name = rs2.getString("firstName");
                        String lastName = rs2.getString("lastName");

                        s = new Student(name,lastName,idS,credits);
                    }
                    if(students.contains(s) == false){
                        students.add(s);
                    }
                }while(rs.next());
                return students;
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
    public Student save(Student entity) {
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


            PreparedStatement stmt = con.prepareStatement("insert into Student(studentId,totalCredits,idP) values(?,?,?);");
            stmt.setLong(1,entity.getStudentId());
            stmt.setInt(2,entity.getTotalCredits());
            stmt.setInt(3,idP);
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
    public Student delete(int id) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            PreparedStatement stmt = con.prepareStatement("select * from Student where Student.studentId = ?;");
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            Student s = new Student("","",0,0);
            int idS = 0;

            if(rs.next()){
                    idS = rs.getInt("studentId");
                    int credits = rs.getInt("totalCredits");
                    int idP = rs.getInt("idP");

                    PreparedStatement stmt2 = con.prepareStatement("select * from Person where Person.idP = ?");
                    stmt2.setInt(1,idP);
                    ResultSet rs2 = stmt2.executeQuery();

                    if(rs2.next()){
                        String name = rs2.getString("firstName");
                        String lastName = rs2.getString("lastName");

                        s = new Student(name,lastName,idS,credits);
                    }
            }

            PreparedStatement stmt2 = con.prepareStatement("delete from Student where Student.studentId = ?");
            stmt2.setInt(1,idS);
            int row = stmt2.executeUpdate();


            if(row != 0){
                this.students.remove(s);
                return s;
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Student update(Student entity) {
        return null;
    }
}
