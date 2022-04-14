import Jdbc.CourseJdbcRepository;
import Jdbc.PersonJdbcRepository;
import Jdbc.StudentJdbcRepository;
import Jdbc.TeacherJdbcRepository;
import Model.Course;
import Model.Person;
import Model.Student;
import Model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlCon {
    public static void main(String[] args){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo","root","eva123");

            Statement stmt=con.createStatement();

            String tblPerson = "create table if not exists Person(idP int primary key auto_increment," +
                    "firstName varchar(30),lastName varchar(30));";
            stmt.execute(tblPerson);

            String tblStudent = "create table if not exists Student(studentId int primary key auto_increment," +
                    "totalCredits int,idP int, foreign key (idP) references Person(idP));";
            stmt.execute(tblStudent);


            String tblTeacher = "create table if not exists Teacher(idT int primary key auto_increment," +
                    "idP int," +
                    "foreign key (idP) references Person(idP));";
            stmt.execute(tblTeacher);

            String tblCourse = "create table if not exists Course(idC int primary key auto_increment," +
                    "name varchar(50),maxEnrollment int,credits int,idT int," +
                    "foreign key (idT) references Teacher(idT));";
            stmt.execute(tblCourse);

            String tblEnrolled = "create table if not exists Enrolled(idE int primary key auto_increment," +
                    "studentId int,idC int," +
                    "foreign key (studentId) references Student(studentId)," +
                    "foreign key (idC) references Course(idC));";
            stmt.execute(tblEnrolled);

        }
        catch(Exception e){
            System.out.println(e);
        }

        PersonJdbcRepository p = new PersonJdbcRepository();
        StudentJdbcRepository s = new StudentJdbcRepository();
        TeacherJdbcRepository t = new TeacherJdbcRepository();
        CourseJdbcRepository c = new CourseJdbcRepository();

        /*
        System.out.println("Persoanele:\n\n" + p.findAll() + "\n\n");
        System.out.println("Studentii:\n\n" + s.findAll() + "\n\n");
        System.out.println("Profesorii:\n\n" + t.findAll() + "\n\n");
        System.out.println("Cursurile:\n\n" + c.findAll() + "\n\n");
*/
        RegistrationSystem2 x = new RegistrationSystem2();

        Student s1 = new Student("Paul","Marian",1,14);
        Course c1 = new Course("BD",10,5,new Teacher("DANA","ALB"),15);
        x.register(c1,s1);

    }
}
