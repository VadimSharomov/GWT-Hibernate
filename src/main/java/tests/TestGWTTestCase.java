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

    public void testLoginView() {
        String greeting = "Hello, World!";
        LoginViewImpl loginView = new LoginViewImpl();
        loginView.getLabel1().setText(greeting);
        assertEquals(greeting, loginView.getLabel1().getText());
    }

    public void testGreetingDependingOnTimeOfDay() {
        Main main = new Main();
        String morningTime = "07:40";
        String dayTime = "14:25:50";
        String eveningTime = "20:45:00";
        String nightTime = "01:05:00";

        assertEquals(main.getGreetingDependingOnTimeOfDay(morningTime), "Good morning");
        assertEquals(main.getGreetingDependingOnTimeOfDay(dayTime), "Good day");
        assertEquals(main.getGreetingDependingOnTimeOfDay(eveningTime), "Good evening");
        assertEquals(main.getGreetingDependingOnTimeOfDay(nightTime), "Good night");
    }
}

