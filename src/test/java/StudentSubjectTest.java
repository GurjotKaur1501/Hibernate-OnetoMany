import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.yrgo.domain.*;

import java.util.List;

import static org.junit.Assert.*;

public class StudentSubjectTest {

    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void setUp() {
        // Create the session factory and session
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Subject.class)
                .buildSessionFactory();

        session = sessionFactory.getCurrentSession();
    }

    @After
    public void tearDown() {
        // Close the session and session factory
        if (session != null) {
            session.close();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testOneToManyRelationship() {
        // Step 1: Create instances of both classes
        Student student = new Student("John Doe");
        javax.security.auth.Subject subject1 = new javax.security.auth.Subject();
        javax.security.auth.Subject subject2 = new javax.security.auth.Subject();

        // Step 2: Add objects to the relationship
        student.addSubject(subject1);
        student.addSubject(subject2);

        // Begin transaction
        session.beginTransaction();

        // Save the student (this will also save the subjects due to cascade)
        session.save(student);

        // Commit transaction
        session.getTransaction().commit();

        // Step 3: Test that you can retrieve the list of objects for a specific instance
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        // Retrieve the student from the database
        Student retrievedStudent = session.get(Student.class, student.getId());

        // Assert that the student was retrieved correctly
        assertNotNull("Retrieved student should not be null", retrievedStudent);
        assertEquals("Student name should match", "John Doe", retrievedStudent.getName());

        // Retrieve the list of subjects for the student
        List<javax.security.auth.Subject> subjects = retrievedStudent.getSubjects();

        // Assert that the list of subjects is not null and has the correct size
        assertNotNull("List of su bjects should not be null", subjects);
        assertEquals("Number of subjects should be 2", 2, subjects.size());

        // Assert that the subjects' names are correct
        assertEquals("First subject should be Math", "Math", subjects.get(0).getClass());
        assertEquals("Second subject should be Science", "Science", subjects.get(1).getClass());

        // Commit transaction
        session.getTransaction().commit();
    }
}