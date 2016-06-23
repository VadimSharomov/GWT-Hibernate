package tests;

import org.junit.Test;
import server.MainRpcServiceImpl;
import shared.User;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestJunit {

    @Test
    public void testLoginUser() {
        MainRpcServiceImpl mock = mock(MainRpcServiceImpl.class);
        User user = new User("ivan");

        when(mock.loginUser("ivan", "secret")).thenReturn(user);
        assertEquals(user.getLogin(), mock.loginUser("ivan", "secret").getLogin());

        user = new User();
        when(mock.loginUser("ivan", "")).thenReturn(user);
        assertEquals(user.getLogin(), mock.loginUser("ivan", "").getLogin());
    }

    @Test
    public void testHashPassword() {
        MainRpcServiceImpl mainRpcService = new MainRpcServiceImpl();
        String password = "secret";

        //for iteration=1000 and for this salt
        byte[] resultHashPassword = new byte[]{60, -30, 40, 61, -37, -92, -68, 127, -34, -65, 74, -67, -89, 20, 49, 36, -50, 100, -59, -123, -60, 51, 127, -121, 122, 26, 85, 62, 34, -54, 58, 6};
        byte[] saltPassword = new byte[]{107, 49, -64, -43, 11, -40, -48, -110, -10, -12, -47, -58, -60, -22, -21, 47, -91, -49, -48, 26, -24, 62, -61, 77, -78, -16, 24, -52, 62, 34, -83, -33};

        assertTrue(Arrays.equals(resultHashPassword, mainRpcService.getHashPassword(password.toCharArray(), saltPassword)));
    }



}
