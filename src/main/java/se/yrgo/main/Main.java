import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        // Create a SessionFactory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Parent.class)
                .addAnnotatedClass(Child.class)
                .buildSessionFactory();

        // Create a Session
        Session session = factory.getCurrentSession();

        try {
            // Start a transaction
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

            // Save the parent (this will also save the children due to cascade)
            session.save(parent);

            // Commit the transaction
            session.getTransaction().commit();

            // Retrieve the parent and print the children
            session = factory.getCurrentSession();
            session.beginTransaction();

            Parent savedParent = session.get(Parent.class, parent.getId());
            System.out.println("Parent: " + savedParent.getName());
            System.out.println("Children:");
            for (Child child : savedParent.getChildren()) {
                System.out.println(child.getName());
            }

            session.getTransaction().commit();

        } finally {
            factory.close();
        }
    }
}