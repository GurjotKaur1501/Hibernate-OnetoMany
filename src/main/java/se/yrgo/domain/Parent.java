package se.yrgo.domain;
import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent implements org.hibernate.annotations.Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> children = new ArrayList<>();

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    // Method to add a child
    public void addChild(Child child) {
        children.add(child);
        child.setParent(this);
    }

    // Method to remove a child
    public void removeChild(Child child) {
        children.remove(child);
        child.setParent(null);
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}