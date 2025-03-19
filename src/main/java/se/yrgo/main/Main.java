package se.yrgo.main;

import se.yrgo.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        // Create a SessionFactory

        try (SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Parent.class)
                .addAnnotatedClass(Child.class)
                .buildSessionFactory()) {
            // Create a Session and start a transaction
            Session session = factory.getCurrentSession();
            session.beginTransaction();

            // Create a Parent object
            Parent parent = new Parent();
            parent.setName("Parent 1");

            // Create Child objects
            Child child1 = new Child();
            child1.setName("Child 1");

            Child child2 = new Child();
            child2.setName("Child 2");

            // Add children to the parent
            parent.addChild(child1);
            parent.addChild(child2);

            // Save the parent (cascading will save children as well)
            session.save(parent);

            // Commit the transaction
            session.getTransaction().commit();

            // Open a new session for fetching the data
            Session newSession = factory.openSession();
            newSession.beginTransaction();

            // Retrieve the parent and print the children
            Parent savedParent = newSession.get(Parent.class, parent.getId());

            if (savedParent != null) {
                System.out.println("Parent: " + savedParent.getName());
                System.out.println("Children:");
                for (Child child : savedParent.getChildren()) {
                    System.out.println(child.getName());
                }
            }

            newSession.getTransaction().commit();
            newSession.close();

        }
        // Ensure the factory is closed to release resources
    }
}
