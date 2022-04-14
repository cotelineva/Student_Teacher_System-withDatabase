package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
    public String name;
    public int maxEnrollment;
    public int credits;
    public Teacher teacher;
    public int idCourse;

    public Course(String name, int maxEnrollment, int credits, Teacher teacher, int idCourse) {
        this.name = name;
        this.maxEnrollment = maxEnrollment;
        this.credits = credits;
        this.teacher = teacher;
        this.idCourse = idCourse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", maxEnrollment=" + maxEnrollment +
                ", credits=" + credits +
                ", teacher=" + teacher +
                ", idCourse=" + idCourse +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return maxEnrollment == course.maxEnrollment && credits == course.credits && idCourse == course.idCourse && Objects.equals(name, course.name) && Objects.equals(teacher, course.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, maxEnrollment, credits, teacher, idCourse);
    }
}
