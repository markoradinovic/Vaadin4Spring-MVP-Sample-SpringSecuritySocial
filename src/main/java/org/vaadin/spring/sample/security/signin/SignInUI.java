package org.vaadin.spring.sample.security.signin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.social.security.SocialAuthenticationException;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.sample.security.controller.VaadinRedirectObject;
import org.vaadin.spring.sample.security.ui.ViewToken;
import org.vaadin.spring.security.Security;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@VaadinUI(path="/signin")
@Title("Vaadin4Spring Security Social Demo")
@Theme("valo")
public class SignInUI extends UI {

	private SignInContent signInContent;
			
	@Autowired
	Security security;
	
	@Override
	protected void init(VaadinRequest request) {
								
		signInContent = new SignInContent(security);
		setContent(signInContent);	
		
		/*
		 * Handle bookmark request to MainUI view 
		 */
		final String requestedUriFragment = Page.getCurrent().getUriFragment();
		
		if (requestedUriFragment != null && !requestedUriFragment.isEmpty() 
				&& ViewToken.VALID_TOKENS.contains(requestedUriFragment.substring(1, requestedUriFragment.length()))) {
			
			if (getSession().getSession().getAttribute(VaadinRedirectObject.REDIRECT_OBJECT_SESSION_ATTRIBUTE) != null) {
				getSession().getSession().removeAttribute(VaadinRedirectObject.REDIRECT_OBJECT_SESSION_ATTRIBUTE);						
			}
			/*
			 * Save it to session, so that MainUI know where to go after sign in flow.
			 */
			getSession().getSession().setAttribute(VaadinRedirectObject.REDIRECT_OBJECT_SESSION_ATTRIBUTE, 
					new VaadinRedirectObject(requestedUriFragment.substring(1, requestedUriFragment.length()), null));
		}
			
		
				
		/*
		 * Handle Exceptions from SocialAuthenticationFilter 
		 */
		if (getSession().getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) != null) {
			Object exception = getSession().getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			if (exception instanceof SocialAuthenticationException) {
				signInContent.setSocialErrorMessage("Social connection error. " +((SocialAuthenticationException) exception).getMessage());
				
			} else if (exception instanceof AuthenticationException) {
				signInContent.setSocialErrorMessage("AuthenticationException. " +((AuthenticationException) exception).getMessage());
			}						
			//clear last error
			getSession().getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}

}
