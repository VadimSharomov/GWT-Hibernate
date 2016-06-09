package tests;

import org.junit.Test;
import server.MainRpcServiceImpl;
import shared.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestJunit {

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
    public void testHashPassword() {
        MainRpcServiceImpl mainRpcService = new MainRpcServiceImpl();
        String password = "secret";

        byte[] resultHashPassword = new byte[]{-115, 100, 114, -127, 91, 105, 93, -42, 19, -68, 39, 33, -105, -30, 1, 60, -46, -37, 40, -6, 73, 55, 53, -18, 7, 83, 77, 91, 58, -32, 43, -120};
        byte[] saltPassword = new byte[]{65, 87, -7, 108, 110, 46, 21, 40, 76, 52, -34, 125, -87, 100, -73, 33, 82, -2, -33, -41, -27, -54, -1, 30, -81, 92, 47, 121, -51, 48, 46, -106};

        assertTrue(Arrays.equals(resultHashPassword, mainRpcService.hashPassword(password.toCharArray(), saltPassword, 1, 256)));
    }

    @Test
    public void testGetAllUsers() {
        MainRpcServiceImpl mock = mock(MainRpcServiceImpl.class);
        List<User> userList = new ArrayList<>();

        when(mock.getAllUsers()).thenReturn(userList);
        assertEquals(userList, mock.getAllUsers());
    }
}
