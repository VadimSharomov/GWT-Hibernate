package server;

import client.MainRpcService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.gwt.crypto.util.SecureRandom;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import shared.User;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Vadim on 16.05.2016.
 *
 */

public class MainRpcServiceImpl extends RemoteServiceServlet implements MainRpcService {
    private Session session;
    private Logger logger = Logger.getLogger("MyLogger");

    public MainRpcServiceImpl() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        session = sessionFactory.openSession();
    }

    @Override
    public List<User> getAllUsers() {
        Query query = session.createQuery("from User");
        return query.list();
    }

    @Override
    public User loginUser(String login, String password) {

//      for administration purposes. Update the password for an existing user
        String keyWord = "SetUserPassword:";
        if (login.contains(keyWord)) {
            login = login.replace(keyWord, "");
            logger.log(Level.INFO, "user logging for update password: " + login);
            updatePassword(login, password);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "password change failed: " + login);
                e.printStackTrace();
            }
        }

        String hql = "FROM User WHERE login = :login";
        Query query = session.createQuery(hql);
        query.setParameter("login", login);
        List usersList = query.list();

        for (Object us : usersList) {
            User user = (User) us;
            if (login.equals(user.getLogin())) {
                byte[] hashPassword = hashPassword(password.toCharArray(), user.getSaltPassword(), 1, 256);
                logger.log(Level.INFO, "user logging: " + user);
                if (Arrays.equals(hashPassword, user.getPassword())) {
                    logger.log(Level.INFO, "user logging success: " + user);
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Test method
     */
    @Override
    public String getGreeting(String greeting) {
        return greeting + ", World!";
    }

    /**
    This method can create or update password.
    Password is creating with PBKDF2 and HMACSHA512 and salt.
    It is creating new salt when is updating or creating user password.
    Salt is creating using SecureRandom.
    New password and salt are storing in data base.
    For password and salt data base must have field with BLOB type.
     */
    private void updatePassword(String login, String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        byte[] hashPassword = hashPassword(password.toCharArray(), salt, 1, 256);

        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("update User set password = :password, saltPassword =:salt where login = :login");
        query.setParameter("login", login);
        query.setParameter("salt", salt);
        query.setParameter("password", hashPassword);
        int rowCount = query.executeUpdate();
        transaction.commit();

        logger.log(Level.INFO, "update user password login: " + login);
        logger.log(Level.SEVERE, "update user password result: " + (rowCount > 0 ? "success" : "failed"));
    }

    /**
     Password is creating with PBKDF2 and HMACSHA512 and salt.
     */
    public byte[] hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey key = skf.generateSecret(spec);
            return key.getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User saveUser(User user) {
        session.save(user);
        return null;
    }
}
