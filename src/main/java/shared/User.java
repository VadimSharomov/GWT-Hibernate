package shared;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users", schema = "gwthiber")
public class User implements Serializable {
    private long id;
    private String login;
    private byte[] password;
    private byte[] saltPassword;
    private String name;
    private String sessionId;
    private static final long serialVersionUID = 7526472295622776147L;

    public User() {

    }

    public User(long id, String login, String name, String sessionId) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.sessionId = sessionId;
    }

    public User(String login) {
        this.login = login;
    }

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "login", nullable = false, length = 45)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "password")
    @Lob
    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    @Basic
    @Column(name = "salt")
    @Lob
    public byte[] getSaltPassword() {
        return saltPassword;
    }

    public void setSaltPassword(byte[] saltPassword) {
        this.saltPassword = saltPassword;
    }

    @Transient
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return " User: id=" + id + ", login=" + login + ", name=" + name + ", sessionId=" + sessionId;
    }
}
