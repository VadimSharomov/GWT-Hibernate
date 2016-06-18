package server;

import client.MainRpcService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.gwt.crypto.util.SecureRandom;
import org.apache.log4j.varia.NullAppender;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import shared.User;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainRpcServiceImpl extends RemoteServiceServlet implements MainRpcService {
    private Session session;
    private Logger logger = Logger.getLogger("MyLogger");

    public MainRpcServiceImpl() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        session = sessionFactory.openSession();
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getAllUsers() {
        Query query = session.createQuery("from User");
        return query.list();
    }

    @Override
    public void saveUser(User user) {
        session.save(user);
    }

    @Override
    public User loginUser(String login, String password) {
        String commandToUpdateUserPassword = "SetUserPassword:";
        if (login.contains(commandToUpdateUserPassword)) {
            login = login.replace(commandToUpdateUserPassword, "");
            logging("Server: User logging for update password: " + login);
            updateUserPassword(login, password);
        }

        String hql = "FROM User WHERE login = :login";
        Query query = session.createQuery(hql);
        query.setParameter("login", login);
        List usersList = query.list();
        for (Object us : usersList) {
            User user = (User) us;
            if (login.equals(user.getLogin())) {
                logging("Server: User logging: " + user);
                byte[] hashPassword = getHashPassword(password.toCharArray(), user.getSaltPassword());
                if (Arrays.equals(hashPassword, user.getPassword())) {
                    User resUser = new User(user.getId(), user.getLogin(), user.getName(), getSession().getId());
                    storeUserInSession(resUser);
                    logging("Server: User logging success: " + resUser);
                    return resUser;
                }
            }
        }
        return new User();
    }

    private void storeUserInSession(User user) {
        getSession().setAttribute(user.getLogin(), user);
        getSession().setMaxInactiveInterval(24 * 3600);
    }

    @Override
    public void logOut(String login) {
        User user = (User) getSession().getAttribute(login);
        logging("Server: User logout: " + login);
        if (user != null) {
            getSession().removeAttribute(login);
            getSession().invalidate();
        }
    }

    /*
     This method can create or update password.
     Password is creating with PBKDF2 and HMACSHA512 and salt.
     It is creating new salt when is updating or creating user password.
     Salt is creating using SecureRandom.
     New password and salt are storing in data base.
     For password and salt data base must have field with BLOB type.
    */
    private void updateUserPassword(String login, String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];

        random.nextBytes(salt);
        byte[] hashPassword = getHashPassword(password.toCharArray(), salt);

        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("update User set password = :password, saltPassword = :salt where login = :login");
        query.setParameter("login", login);
        query.setParameter("salt", salt);
        query.setParameter("password", hashPassword);
        int rowCount = query.executeUpdate();
        transaction.commit();

        logging("Server: update user password login: " + login);
        if (rowCount > 0) {
            logging("Server: update user password result: success");
        } else {
            logging("Server: update user password result: failed");
        }
    }

    /**
     * Password is creating with PBKDF2 and HMACSHA512 and salt.
     */
    public byte[] getHashPassword(final char[] password, final byte[] salt) {
        int iterations = 1000;
        int keyLength = 256;
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey key = skf.generateSecret(spec);
            return key.getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logging("Server: getHashPassword: failed");
            throw new RuntimeException(e);
        }
    }

    private void logging(String msg) {
        logger.log(Level.SEVERE, msg);
    }

    private HttpSession getSession() {
        return this.getThreadLocalRequest().getSession(true);
    }

    /**
     * Method for testing RPC service
     */
    @Override
    public String getGreeting(String greeting) {
        return greeting + ", World!";
    }
}
