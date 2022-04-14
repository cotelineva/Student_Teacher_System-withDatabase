
import Jdbc.CourseJdbcRepository;
import Jdbc.StudentJdbcRepository;
import Model.Course;
import Model.Student;
import Model.Teacher;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * die teste funktionieren, aber die daten muessen mit denen aus der tabellen aus der
 * database aus mysql uberpruefen.
 */

class RegistrationSystem2Test {

    @Test
    void register() {
        Student s1 = new Student("Paul","Marian",1,17);
        Course c1 = new Course("BD",10,5,new Teacher("RADU","MAN"),3);
        RegistrationSystem2 r = new RegistrationSystem2();

        assertEquals(r.register(c1,s1),true);
    }

    @Test
    void retrieveCoursesWithFreePlaces() {
        Course c1 = new Course("BD",10,5,new Teacher("RADU","MAN"),3);
        Course c2 = new Course("MAP",8,6,new Teacher("DANA","ALB"),4);

        CourseJdbcRepository c = new CourseJdbcRepository();
        RegistrationSystem2 r = new RegistrationSystem2();

        assertEquals(r.retrieveCoursesWithFreePlaces(c),List.of(c1,c2));

    }

    @Test
    void retrieveStudentsEnrolledForACourse() {
        Student s1 = new Student("Paul","Marian",1,22);
        Course c1 = new Course("BD",10,5,new Teacher("RADU","MAN"),3);
        StudentJdbcRepository s = new StudentJdbcRepository();
        RegistrationSystem2 r = new RegistrationSystem2();

        List<Student> list = List.of(s1);

        assertEquals(r.retrieveStudentsEnrolledForACourse(s,c1),list);

    }

    @Test
    void getAllCourses() {
        Course c1 = new Course("BD",10,5,new Teacher("RADU","MAN"),3);
        Course c2 = new Course("MAP",8,6,new Teacher("DANA","ALB"),4);

        RegistrationSystem2 r = new RegistrationSystem2();
        CourseJdbcRepository c = new CourseJdbcRepository();

        assertEquals(r.getAllCourses(c),List.of(c1,c2));
    }

    @Test
    void changeCredit() {
        Course c1 = new Course("BD",10,5,new Teacher("RADU","MAN"),3);

        RegistrationSystem2 r = new RegistrationSystem2();
        StudentJdbcRepository s = new StudentJdbcRepository();
        CourseJdbcRepository c = new CourseJdbcRepository();

        assertEquals(c.findOne(c1.getIdCourse()).getCredits(),5);
        r.changeCredit(c1,4,s,c);
        assertEquals(c.findOne(c1.getIdCourse()).getCredits(),4);
    }

    @Test
    void deleteCourse() {
        Course c1 = new Course("BD",10,4,new Teacher("RADU","MAN"),3);
        Course c2 = new Course("MAP",8,6,new Teacher("DANA","ALB"),4);

        RegistrationSystem2 r = new RegistrationSystem2();
        StudentJdbcRepository s = new StudentJdbcRepository();
        CourseJdbcRepository c = new CourseJdbcRepository();

        assertEquals(r.getAllCourses(c),List.of(c1,c2));
        r.deleteCourse(c1,s,c);
        assertEquals(r.getAllCourses(c),List.of(c2));
    }
}
