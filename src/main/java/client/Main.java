package client;

import client.login.LoginViewImpl;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import shared.User;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main implements EntryPoint {
    private static Logger logger = Logger.getLogger("MyLogger");
    private UserMessages messages = GWT.create(UserMessages.class);
    private static String currentLocal = com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().getLocaleName();
    private MainRpcServiceAsync rpcService = GWT.create(MainRpcService.class);

    /**
     * Entry point method.
     */
    public void onModuleLoad() {
        final LoginViewImpl loginView = new LoginViewImpl();
        logger.log(Level.INFO, "onModuleLoad() - success");
        logger.log(Level.INFO, "Current locale: " + currentLocal);

        loginView.getLoginLabel().setText(messages.login());
        loginView.getPasswordLabel().setText(messages.password());
        loginView.getButtonSubmit().setText(messages.submit());

        loginView.getImageLogo().setUrl("imageLogo.jpg");
        loginView.getLogoLabel().setText("GWT Productivity for developers, performance for users");
        loginView.getLabelFooter().setText("© 2016 Company Name");
        loginView.getLogoutLink().setVisible(false);

//        loginView.getLoginBox().addKeyDownHandler(new KeyDownHandler() {
//            @Override
//            public void onKeyDown(KeyDownEvent event) {

//            }
//        });

        /**
         * Add click handler to button Submit.
         */
        loginView.getButtonSubmit().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                LogginUser();
            }

            private void LogginUser() {
                final String login = loginView.getLoginBox().getValue();
                final String password = loginView.getPasswordBox().getValue();

                loginView.getCompletionLabel1().setText("");
                loginView.getCompletionLabel2().setText("");

                //send login and password to server
                rpcService.loginUser(login, password, new AsyncCallback<User>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        loginView.getCompletionLabel2().setText("rpcService.loginUser() - onFailure ");
                        logger.log(Level.SEVERE, "Fail: onFailure(Throwable caught) in rpcService.loginUser(login, password, new AsyncCallback<User>()");
                    }

                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            String greeting = getGreetingOfDay();

                            loginView.getLoginLabel().setVisible(false);
                            loginView.getPasswordLabel().setVisible(false);
                            loginView.getLoginBox().setVisible(false);
                            loginView.getPasswordBox().setVisible(false);
                            loginView.getButtonSubmit().setVisible(false);

                            if (currentLocal.contains("default")) {
                                loginView.getLogoutLink().setHref("Main.html");
                            } else {
                                loginView.getLogoutLink().setHref("Main.html?locale=" + currentLocal);
                            }
                            loginView.getLogoutLink().setText(messages.logout());
                            loginView.getLogoutLink().setVisible(true);

                            loginView.getCompletionLabel1().setText(greeting + ", " + user.getName() + ".");

                            logger.log(Level.INFO, "Login success");
                        } else {
                            loginView.getCompletionLabel2().setText(messages.loginFailed());
                            logger.log(Level.SEVERE, "Login failed");
                        }
                    }
                });
            }
        });

        RootPanel.get().add(loginView);
    }

    /**
     * Generation greetings depending on the time of day
     */
    private String getGreetingOfDay() {
        Date date = new Date();
        DateTimeFormat dtf = DateTimeFormat.getFormat("HH:mm");
        String time = dtf.format(date, TimeZone.createTimeZone(date.getTimezoneOffset()));
        logger.log(Level.INFO, "Local client time: " + time);
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);

        if ((hour > 16) && (minute > 0) && (hour < 21)) {
            return messages.goodEvening();
        } else if ((hour > 21) && (minute > 0) && (hour < 23) && (minute < 59)) {
            return messages.goodNight();
        } else if ((hour > 6) && (minute > 0) && (hour < 11)) {
            return messages.goodMorning();
        } else return messages.goodDay();
    }

    /**
     * Test method
     */
    public String getGreeting(String name) {
        return "Hello " + name + "!";
    }
}
