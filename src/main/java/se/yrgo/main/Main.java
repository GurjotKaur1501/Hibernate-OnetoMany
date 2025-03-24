package se.yrgo.main;

import se.yrgo.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        // Create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Subject.class)
                .buildSessionFactory();

        // Create session

        try (factory) {
            Session session = factory.getCurrentSession();
            // Create a student
            Student student = new Student("John Doe");

            // Create subjects
            javax.security.auth.Subject subject1 = new javax.security.auth.Subject();
            javax.security.auth.Subject subject2 = new javax.security.auth.Subject();

            // Add subjects to student
            student.addSubject(subject1);
            student.addSubject(subject2);

            // Start a transaction
            session.beginTransaction();

            // Save the student (this will also save the subjects due to cascade)
            session.save(student);

            // Commit transaction
            session.getTransaction().commit();

            // Retrieve the student and print subjects
            session = factory.getCurrentSession();
            session.beginTransaction();
            Student retrievedStudent = session.get(Student.class, student.getId());
            System.out.println("Retrieved Student: " + retrievedStudent.getName());
            System.out.println("Subjects: ");
            for (javax.security.auth.Subject subject : retrievedStudent.getSubjects()) {
                System.out.println(subject.getClass());
            }

            // Commit transaction
            session.getTransaction().commit();
        }
    }
}