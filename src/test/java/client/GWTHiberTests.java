package client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import shared.User;

/**
 * Created by Vadim on 19.05.2016.
 *
 */
public class GWTHiberTests extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "Main";
    }

    public void testGreetingService() {
      /* create the service that we will test. */
        final MainRpcServiceAsync rpcService = GWT.create(MainRpcService.class);
        ServiceDefTarget target = (ServiceDefTarget) rpcService;
        target.setServiceEntryPoint(GWT.getModuleBaseURL());

      /* since RPC calls are asynchronous, we will need to wait
       for a response after this test method returns. This line
       tells the test runner to wait up to 10 seconds
       before timing out. */
//        delayTestFinish(10000);

      /* send a request to the server. */
        final String login = "ivan";
        final String password = "secret";
        rpcService.loginUser(login, password,
                new AsyncCallback<User>() {
                    public void onFailure(Throwable caught) {
                        fail("Request failure: " + caught.getMessage());
                        assertTrue("ivan".equals(""));
                    }

                    public void onSuccess(User result) {
                        assertTrue("ivan".equals(result.getLogin()));
                        finishTest();
                    }
                });
    }

    public void testGetGreeting() {
        Main testMain = new Main();
        String name = "Ivan";
        String expectedGreeting = "Hello " + name + "!";
        assertEquals(expectedGreeting, testMain.getGreeting(name));
    }



}
