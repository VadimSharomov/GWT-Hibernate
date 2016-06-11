package tests;

import client.Main;
import client.MainRpcService;
import client.MainRpcServiceAsync;
import client.login.LoginViewImpl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;



public class TestGWTTestCase extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "Main";
    }

    public void testRPCService() {
        MainRpcServiceAsync rpcService = GWT.create(MainRpcService.class);
        ServiceDefTarget target = (ServiceDefTarget) rpcService;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MainRpcServiceImpl");

        delayTestFinish(10000);

        rpcService.getGreeting("Hello",
                new AsyncCallback<String>() {
                    public void onFailure(Throwable caught) {
                        fail("Request failure: " + caught.getMessage());
                    }

                    public void onSuccess(String result) {
                        assertEquals("Hello, World!", result);
                        finishTest();
                    }
                });
    }


    public void testGetGreeting() {
        Main testMain = new Main();
        String name = "World";
        String expectedGreeting = "Hello " + name + "!";
        assertEquals(expectedGreeting, testMain.getGreeting(name));
    }

    public void testView() {
        String greeting = "Hello, World!";
        LoginViewImpl loginView = new LoginViewImpl();
        loginView.getCompletionLabel1().setText(greeting);
        assertEquals(greeting, loginView.getCompletionLabel1().getText());
    }
}

