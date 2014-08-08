package org.vaadin.spring.sample.security.signin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.security.Security;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SignInContent extends VerticalLayout implements ClickListener {
	
	private final Security security;
	
	private final Label caption;	
	private final TextField username;
	private final PasswordField password;
	private final Button btnLogin;
	private final Button btnFacebookLogin;
	private final Button btnGoogleLogin;
	private final VerticalLayout loginPanel;
	private final Label errorMessage;
	
	
	public SignInContent(Security security) {
		super();
		setSizeFull();
		setSpacing(true);
		
		this.security = security;
		
		caption = new Label("Sign in to Vaadin4Spring Security Social Demo");
		caption.addStyleName(ValoTheme.LABEL_H2);
		caption.setSizeUndefined();		
		addComponent(caption);
		setComponentAlignment(caption, Alignment.MIDDLE_CENTER);
		
		loginPanel = new VerticalLayout();
		loginPanel.setSizeUndefined();
		loginPanel.setSpacing(true);
		loginPanel.setMargin(true);
		addComponent(loginPanel);
		setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		setExpandRatio(loginPanel, 1);
		
		errorMessage = new Label();
		errorMessage.setWidth("300px");
		errorMessage.addStyleName(ValoTheme.LABEL_FAILURE);		
		errorMessage.setVisible(false);
		loginPanel.addComponent(errorMessage);
		
		username = new TextField("Username");
		username.setImmediate(true);
		username.setWidth("300px");
		loginPanel.addComponent(username);
		
		password = new PasswordField("Password");
		password.setImmediate(true);
		password.setWidth("300px");
		loginPanel.addComponent(password);
		
		btnLogin = new Button("Signin", FontAwesome.UNLOCK);
		btnLogin.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnLogin.addClickListener(this);
		btnLogin.setWidth("100%");
		loginPanel.addComponent(btnLogin);						
		
		btnFacebookLogin = new Button("Signin with Facebook", FontAwesome.FACEBOOK);
		btnFacebookLogin.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnFacebookLogin.setWidth("100%");
		btnFacebookLogin.addClickListener(this);
		loginPanel.addComponent(btnFacebookLogin);
				
		btnGoogleLogin = new Button("Signin with Google", FontAwesome.GOOGLE_PLUS);
		btnGoogleLogin.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnGoogleLogin.setWidth("100%");
		btnGoogleLogin.addClickListener(this);
		loginPanel.addComponent(btnGoogleLogin);		
		
		final Label infoLabel = new Label(FontAwesome.INFO_CIRCLE.getHtml() + " You can sign in with username \"user\" and password \"user\".", ContentMode.HTML);
		infoLabel.setWidth("300px");
		loginPanel.addComponent(infoLabel);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		
		/*
		 * Signin using username and password
		 */
		if (event.getButton() == btnLogin) {
			
			try {
			
				security.login(username.getValue(), password.getValue());
				
				//Redirect to MainUI
				Page.getCurrent().setLocation("/ui");								
									
			} catch (AuthenticationException e) {
				errorMessage.setValue(e.getMessage());
				errorMessage.setVisible(true);
			}
		}
		
		/*
		 * Signin using Facebook
		 */
		if (event.getButton() == btnFacebookLogin) {
			
			Page.getCurrent().setLocation("/auth/facebook");
		}
		
		/*
		 * Signin using Google
		 */
		if (event.getButton() == btnGoogleLogin) {
			/*
			 * Workaround
			 * Google require scope parameter, but spring-social-google does't handle this correctly
			 * 
			 * scope =email https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/tasks https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/latitude.all.best />
			 */			
			String scope = "profile email https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/tasks https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/latitude.all.best";
			try {
				Page.getCurrent().setLocation("/auth/google?scope=" + URLEncoder.encode(scope, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Page.getCurrent().setLocation("/auth/google?scope=profile%20email");
		}
		
	}
	
	public void setSocialErrorMessage(String message) {
		errorMessage.setValue(message);
		errorMessage.setVisible(true);
	}
	

}
