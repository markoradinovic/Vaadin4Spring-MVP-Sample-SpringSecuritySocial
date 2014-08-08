package org.vaadin.spring.sample.security.ui;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;
import org.vaadin.spring.sample.security.controller.VaadinRedirectObject;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

@VaadinUI
@Title("Vaadin4Spring Security Soacial Demo")
@Theme("valo")
@SuppressWarnings("serial")
public class MainUI extends UI {
	
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	@Autowired
	SpringViewProvider springViewProvider;
	
	@Autowired
	MainLayout mainLayout;

    @Override
    protected void init(VaadinRequest request) {
    	setLocale(new Locale.Builder().setLanguage("sr").setScript("Latn").setRegion("RS").build());
    	
        Navigator navigator = new Navigator(MainUI.this, (ViewDisplay)mainLayout);
        navigator.addProvider(springViewProvider);
        navigator.addViewChangeListener(mainLayout);
        
        setContent(mainLayout);
        
        /*
         * Handling redirections
         */        
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();        
        if (sessionStrategy.getAttribute(attrs, VaadinRedirectObject.REDIRECT_OBJECT_SESSION_ATTRIBUTE) != null) {        	
        	VaadinRedirectObject redirectObject = (VaadinRedirectObject) sessionStrategy.getAttribute(attrs, VaadinRedirectObject.REDIRECT_OBJECT_SESSION_ATTRIBUTE);
        	sessionStrategy.removeAttribute(attrs, VaadinRedirectObject.REDIRECT_OBJECT_SESSION_ATTRIBUTE);
        	 
        	navigator.navigateTo(redirectObject.getRedirectViewToken());
        	
        	if (redirectObject.getErrorMessage() != null) {
        		Notification.show("Error", redirectObject.getErrorMessage(), Type.ERROR_MESSAGE);
        	}
        	
        }
               
    }

}
