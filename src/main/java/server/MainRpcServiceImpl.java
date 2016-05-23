package server;

import client.MainRpcService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import shared.User;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Vadim on 16.05.2016.
 *
 */
public class MainRpcServiceImpl extends RemoteServiceServlet implements MainRpcService {

    private SessionFactory sessionFactory;
    private Session session;
    private static Logger logger = Logger.getLogger("MyLogger");

    public MainRpcServiceImpl() {
        sessionFactory = HibernateUtil.getSessionFactory();
        session = sessionFactory.openSession();
    }

    @Override
    public List<User> getAllUsers() {
        Query query = session.createQuery("from User");
        return query.list();
    }

    @Override
    public User loginUser(String login, String password) {
        String hql = "FROM User WHERE login = :login";
        Query query = session.createQuery(hql);
        query.setParameter("login", login);
        List usersList = query.list();

        for (Object us : usersList) {
            User user = (User) us;
            if (login.equals(user.getLogin())) {
                byte[] hashPassword = hashPassword(password.toCharArray(), user.getSaltPassword(), 1, 256);
                String hashUser = new String(hashPassword);
                logger.log(Level.SEVERE, "user logging:" + user);
                if (Arrays.equals(hashPassword, user.getPassword())) {
                    logger.log(Level.SEVERE, "user logging success: " + user);
                    return user;
                }
            }
        }
        return null;
    }

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

    @Test
    public void testLoginUser() {
        MainRpcServiceImpl mock = mock(MainRpcServiceImpl.class);
        User user = new User();

        when(mock.loginUser("ivan", "secret")).thenReturn(user);
        assertEquals(user, mock.loginUser("ivan", "secret"));

        when(mock.loginUser("ivan", "")).thenReturn(null);
        assertEquals(null, mock.loginUser("ivan", ""));
    }

    @Test
    public void testGetAllUsers() {
        MainRpcServiceImpl mock = mock(MainRpcServiceImpl.class);
        List<User> userList = new ArrayList<>();

        when(mock.getAllUsers()).thenReturn(userList);
        assertEquals(userList, mock.getAllUsers());
    }

    @Test
    public void testHashPassword() {
        MainRpcServiceImpl mock = mock(MainRpcServiceImpl.class);

        String password = "secret";
        byte[] hashPassword = new byte[]{90, -56, 63, 74, 51, 93, 87, -92, 27, 91, -34, 71, -1, 47, -103, 5, -98, -51, 94, 35, 73, -120, -22, -93, -123, 7, -49, 13, 20, -24, -105, 42};
        byte[] saltPassword = new byte[]{-38, 104, -19, -95, 23, 70, -83, 63, -54, 77, -64, 25, 20, -26, -21, 79, 118, -65, -36, 99, 45, -55, 93, 89, 71, -105, 97, 26, 64, -11, -65, 15};
        when(mock.hashPassword(password.toCharArray(), saltPassword, 1, 256)).thenReturn(hashPassword);
        assertEquals(hashPassword, mock.hashPassword(password.toCharArray(), saltPassword, 1, 256));

        password = "smith";
        hashPassword = new byte[]{-74, 37, 81, -37, -22, 67, 2, 96, 89, -85, 74, -11, 113, 107, -2, 3, -97, -57, 40, -74, 72, -61, -50, -83, -102, -69, -63, 77, 3, 125, 115, -70};
        saltPassword = new byte[]{-82, 114, 10, 67, 59, 94, 34, 123, 94, 89, -14, 17, 73, 68, -32, 122, 30, 57, -100, -43, -79, -92, -57, -87, 9, -55, 80, 35, 9, -83, -1, 75};
        when(mock.hashPassword(password.toCharArray(), saltPassword, 1, 256)).thenReturn(hashPassword);
        assertEquals(hashPassword, mock.hashPassword(password.toCharArray(), saltPassword, 1, 256));

//            secret:
//              pasw hex:91 5D DF B9 D3 FF 3C 50 AB 9B 3C 70 87 A6 C3 B3 34 98 BE FC B0 C2 34 07 16 78 3A EA 8D F5 52 EE
//              salt hex:DA 68 ED A1 17 46 AD 3F CA 4D C0 19 14 E6 EB 4F 76 BF DC 63 2D C9 5D 59 47 97 61 1A 40 F5 BF 0F
//            smith:
//              pasw hex:EE F1 97 6D 53 26 99 9A 0B 71 07 AB 3D B4 76 18 4A 31 DD 28 E4 3A FF F4 6C 5F DF 4C CA 0B 42 C4
//              salt hex:AE 72 0A 43 3B 5E 22 7B 5E 59 F2 11 49 44 E0 7A 1E 39 9C D5 B1 A4 C7 A9 09 C9 50 23 09 AD FF 4B
    }

}
