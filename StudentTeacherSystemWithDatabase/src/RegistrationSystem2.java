import Jdbc.CourseJdbcRepository;
import Jdbc.StudentJdbcRepository;
import Model.Course;
import Model.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class RegistrationSystem2 {

    /**
     * @param course - the course to be registered to, must not be null
     * @param student - the student trying to enroll to course
     * @return true if successfully enrolled or false if not
     */

    public boolean register(Course course, Student student){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo", "root", "eva123");

            PreparedStatement stmt = con.prepareStatement("select * from Student where Student.studentId = ?");
            stmt.setLong(1,student.getStudentId());
            ResultSet rs = stmt.executeQuery();

            //falls der Student zu viele kredite hat
            if(rs.next()){
                int credits = rs.getInt("totalCredits");

                if(credits + course.getCredits() > 30){
                    System.out.println("Du hast zu viele Kredite!");
                    return false;
                }
            }
            //falls der kurs keine platze mehr hat

            PreparedStatement stmt2 = con.prepareStatement("select count(*) as enrolled from Enrolled where Enrolled.idC = ?;");
            stmt2.setLong(1,course.getIdCourse());
            ResultSet rs2 = stmt2.executeQuery();

            if(rs2.next()){
                int enrolled = rs2.getInt(1);

                if(enrolled + 1 >= course.getMaxEnrollment()){
                    System.out.println("Kurs hat keine freie Plaetze mehr!");
                    return false;
                }
            }

            //wir addieren die kredite des curses zu den student

            PreparedStatement stmt3 = con.prepareStatement("update Student set totalCredits = totalCredits + ? where studentId = ?;");
            stmt3.setInt(1,course.getCredits());
            stmt3.setLong(2,student.getStudentId());
            int row = stmt3.executeUpdate();

            //wir fuegen den tupel (Course,Student) in die Enrolled Tabelle

            PreparedStatement stmt4 = con.prepareStatement("insert into Enrolled(studentId,idC) values(?,?)");
            stmt4.setLong(1,student.getStudentId());
            stmt4.setInt(2,course.getIdCourse());
            int row2 = stmt4.executeUpdate();

            if(row2 != 0) {
                return true;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }

    /**
     * @return list of courses with available places
     */

    public List<Course> retrieveCoursesWithFreePlaces(CourseJdbcRepository cr){
        List<Course> courses = new ArrayList<>();

        for(Course c:cr.findAll()){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");

                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/sonoo", "root", "eva123");

                PreparedStatement stmt = con.prepareStatement("select * from Course where idC = ?");
                stmt.setInt(1,c.getIdCourse());
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    //wir zaehlen wie viele studenten enrolled fuer dieses Kurs sind
                    PreparedStatement stmt2 = con.prepareStatement("select count(*) as enrolled from Enrolled where Enrolled.idC = ?;");
                    stmt2.setInt(1,c.getIdCourse());
                    ResultSet rs2 = stmt2.executeQuery();

                    if(rs2.next()){
                        int enrolled = rs2.getInt(1);

                        if(enrolled < c.getMaxEnrollment()){
                            courses.add(c);
                        }
                    }

                }

            }catch(Exception e){
                System.out.println(e);
            }
        }

        return courses;
    }

    /**
     * @param course course with enrolled students
     * @return the enrolled students of the course
     */

    public List<Student> retrieveStudentsEnrolledForACourse(StudentJdbcRepository s, Course course){
        List<Student> students = new ArrayList<>();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo", "root", "eva123");

            for(Student s1:s.findAll()){
                PreparedStatement stmt = con.prepareStatement("select * from Enrolled where idC = ? and studentId = ?");
                stmt.setInt(1,course.getIdCourse());
                stmt.setLong(2,s1.getStudentId());
                ResultSet rs = stmt.executeQuery();

                //if the resultset is not false, that means that we have a tuple (course, student)
                if(rs.next()){
                    students.add(s1);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }

        return students;
    }

    /**
     * @return all courses with available places
     */

    public List<Course> getAllCourses(CourseJdbcRepository cr){
        List<Course> courses = new ArrayList<>();

        for(Course c:cr.findAll()){
            courses.add(c);
        }

        return courses;
    }

    /**
     * @param course course to be updated
     * @param credit new update value for course
     */

    public void changeCredit(Course course, int credit, StudentJdbcRepository sr, CourseJdbcRepository cr){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo", "root", "eva123");

            for(Student s:sr.findAll()){
                PreparedStatement stmt = con.prepareStatement("select * from Enrolled where idC = ? and studentId = ?");
                stmt.setInt(1,course.getIdCourse());
                stmt.setLong(2,s.getStudentId());
                ResultSet rs = stmt.executeQuery();

                //if the resultset is not false, that means that we have a tuple (course, student)
                if(rs.next()){
                    //wir muessen die credite in die liste von studenten von der repo verandern
                    Student S = sr.findOne((int)s.getStudentId());
                    S.setTotalCredits(S.getTotalCredits()-course.getCredits());

                    //wir loeschen die kredite des kurses von jeden enrolled student bevor wir sie verandern
                    PreparedStatement stmt2 = con.prepareStatement("update Student set totalCredits = totalCredits - ? where studentId = ?");
                    stmt2.setInt(1,course.getCredits());
                    stmt2.setLong(2,s.getStudentId());
                    int row = stmt2.executeUpdate();
                }
            }

            //wir verandern die kredite
            PreparedStatement stmt2 = con.prepareStatement("update Course set credits = ? where idC = ?");
            stmt2.setInt(1,credit);
            stmt2.setInt(2,course.getIdCourse());
            int row = stmt2.executeUpdate();


            for(Student s:sr.findAll()){
                PreparedStatement stmt3 = con.prepareStatement("select * from Enrolled where idC = ? and studentId = ?");
                stmt3.setInt(1,course.getIdCourse());
                stmt3.setLong(2,s.getStudentId());
                ResultSet rs2 = stmt3.executeQuery();

                //if the resultset is not false, that means that we have a tuple (course, student)
                if(rs2.next()){
                    //wir muessen die credite in die liste von studenten von der repo verandern
                    Student S = sr.findOne((int)s.getStudentId());
                    S.setTotalCredits(S.getTotalCredits()+course.getCredits());

                    //wir addieren die neue kredite zu den enrolled studenten
                    PreparedStatement stmt4 = con.prepareStatement("update Student set totalCredits = totalCredits + ? where studentId = ?");
                    stmt4.setInt(1,credit);
                    stmt4.setLong(2,s.getStudentId());
                    int row2 = stmt4.executeUpdate();
                }
            }

            //wir muessen auch die kredite von der liste der kurse von der repo verandern
            for(Course c1: cr.findAll()){
                if(c1 == course){
                    c1.setCredits(credit);
                }
            }

        }catch (Exception e){
            System.out.println(e);
        }

    }

    /**
     * @param course must not be null, course will be deleted
     */

    public void deleteCourse(Course course, StudentJdbcRepository sr, CourseJdbcRepository cr){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sonoo", "root", "eva123");

            //wir muessen das repo und die tabelle von studenten verandern
            for(Student s:sr.findAll()){
                PreparedStatement stmt3 = con.prepareStatement("select * from Enrolled where idC = ? and studentId = ?");
                stmt3.setInt(1,course.getIdCourse());
                stmt3.setLong(2,s.getStudentId());
                ResultSet rs2 = stmt3.executeQuery();

                //if the resultset is not false, that means that we have a tuple (course, student)
                if(rs2.next()){
                    //wir muessen die credite in die liste von studenten von der repo verandern
                    Student S = sr.findOne((int)s.getStudentId());
                    S.setTotalCredits(S.getTotalCredits()-course.getCredits());

                    //wir loeschen die kredite des geloeschten kurses von den enrolled studenten
                    PreparedStatement stmt4 = con.prepareStatement("update Student set totalCredits = totalCredits - ? where studentId = ?");
                    stmt4.setInt(1,course.getCredits());
                    stmt4.setLong(2,s.getStudentId());
                    int row2 = stmt4.executeUpdate();
                }
            }

            //wir loescen alle enrolled studenten von den Enrolled table
            PreparedStatement stmt = con.prepareStatement("delete from Enrolled where idC = ?");
            stmt.setInt(1,course.getIdCourse());
            int row = stmt.executeUpdate();

            //wir loeschen den course von den Course table
            PreparedStatement stmt2 = con.prepareStatement("delete from Course where idC = ?");
            stmt2.setInt(1,course.getIdCourse());
            int row2 = stmt2.executeUpdate();

            //wir loeschen den course von der liste von kurse vom repo
            cr.delete(course.getIdCourse());

        }catch(Exception e){
            System.out.println(e);
        }
    }
}

