package models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.avaje.ebean.Model;
import helpers.PasswordHelper;

import java.security.NoSuchAlgorithmException;

@Entity
public class User extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @Column(unique = true)
    @NotNull
    private String apiToken;

    @NotNull
    private boolean vetod;

    @NotNull
    private boolean admin;

    public User(String email, String name, String password) {
        super();

        this.email = email;
        this.name = name;
        this.setPassword(password);
        this.setApiToken(PasswordHelper.generateToken());
        this.setVetod(false);
        this.setAdmin(false);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        try {
            this.password = PasswordHelper.createPassword(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public boolean hasVetod() {
        return vetod;
    }

    public void setVetod(boolean vetod) {
        this.vetod = vetod;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public static User authenticate(String email, String password) {
        User user = find.where().eq("email", email).findUnique();
        return user == null ? null : PasswordHelper.checkPassword(password, user.password) ? user : null;
    }

    public static Finder<Integer, User> find = new Finder<>(User.class);
}