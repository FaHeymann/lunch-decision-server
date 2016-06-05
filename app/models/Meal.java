package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String image;

    @NotNull
    private int currentPriority;

    @NotNull
    private int priorityGain;

    @NotNull
    private boolean vetod;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCurrentPriority() {
        return currentPriority;
    }

    public void setCurrentPriority(int currentPriority) {
        this.currentPriority = currentPriority;
    }

    public int getPriorityGain() {
        return priorityGain;
    }

    public void setPriorityGain(int priorityGain) {
        this.priorityGain = priorityGain;
    }

    public boolean isVetod() {
        return vetod;
    }

    public void setVetod(boolean vetod) {
        this.vetod = vetod;
    }

    public static Model.Finder<Integer, Meal> find = new Model.Finder<Integer, Meal>(Meal.class);
}
