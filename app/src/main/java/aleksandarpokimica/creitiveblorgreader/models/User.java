package aleksandarpokimica.creitiveblorgreader.models;

/**
 * Created by Aleksandar on 2/1/2018.
 */

public class User {
    private int id;
    private String email;
    private String token;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
