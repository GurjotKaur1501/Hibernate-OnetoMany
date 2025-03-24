import se.yrgo.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test {
    public static void main(String[] args) {
        // Create a SessionFactory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml") // Load Hibernate configuration
                .addAnnotatedClass(Parent.class)  // Add Parent entity
                .addAnnotatedClass(Child.class)   // Add Child entity
                .buildSessionFactory();

        // Create a Session

        try (factory) {
            Session session = factory.getCurrentSession();
            // Start a transaction
            session.beginTransaction();

            // Create a Parent object
            Parent parent = new Parent();
            parent.setName("John Doe");

            // Create Child objects
            Child child1 = new Child();
            child1.setName("Alice");

            Child child2 = new Child();
            child2.setName("Bob");

            // Add children to the parent
            parent.addChild(child1);
            parent.addChild(child2);

            // Save the parent (this will also save the children due to cascade)
            System.out.println("Saving parent and children...");
            session.save(parent);

            // Commit the transaction
            session.getTransaction().commit();
            System.out.println("Parent and children saved successfully!");

            // Retrieve the parent and print the children
            session = factory.getCurrentSession();
            session.beginTransaction();

            // Retrieve the parent by ID
            int parentId = parent.getId();
            System.out.println("Retrieving parent with ID: " + parentId);
            Parent savedParent = session.get(Parent.class, parentId);

            // Print the parent and children
            System.out.println("Parent: " + savedParent.getName());
            System.out.println("Children:");
            for (Child child : savedParent.getChildren()) {
                System.out.println(" - " + child.getName());
            }

            // Commit the transaction
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Close the SessionFactory
    }
}