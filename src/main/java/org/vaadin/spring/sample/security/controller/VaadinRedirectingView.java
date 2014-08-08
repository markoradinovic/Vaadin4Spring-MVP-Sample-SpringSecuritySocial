package org.vaadin.spring.sample.security.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.view.AbstractView;
import org.vaadin.spring.sample.security.ui.ViewToken;

/**
 * <p>
 * Use this view to redirect to Vaadin UI
 * </p>
 * 
 * <p>
 * Configure as a bean whose name is "connect/{providerId}Connect" and/or "connect/{providerId}Connected".
 * For example:
 * </p>
 * 
 * <code>
 * &#064;Bean(name={"connect/facebookConnect", "connect/facebookConnected"})
 * public View facebookConnectView() {
 * 	return new VaadinRedirectingView ("/ui");
 * }
 * </code>
 * <p>
 * 	Form more details look up {@link ConnectController} and {@link SocialWebAutoConfiguration} Bean BeanNameResolver
 * </p>
 * 
 * 
 */
public class VaadinRedirectingView extends AbstractView {
	
	private static final String DUPLICATE_CONNECTION_ATTRIBUTE = "social_addConnection_duplicate";
	
	private static final String PROVIDER_ERROR_ATTRIBUTE = "social_provider_error";

	private static final String AUTHORIZATION_ERROR_ATTRIBUTE = "social_authorization_error";
	
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	private final String redirectUrl;
	
	public VaadinRedirectingView(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {				
		
		/*
		 * Handle ConnectController exceptions 
		 */
		String errorMessage = null;
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		if (sessionStrategy.getAttribute(attrs, PROVIDER_ERROR_ATTRIBUTE) != null) {
			
			Exception error = (Exception) sessionStrategy.getAttribute(attrs, PROVIDER_ERROR_ATTRIBUTE);
			errorMessage = error.getMessage();
			sessionStrategy.removeAttribute(attrs, PROVIDER_ERROR_ATTRIBUTE);
									
						
		} else if (sessionStrategy.getAttribute(attrs, AUTHORIZATION_ERROR_ATTRIBUTE) != null) {
			
			@SuppressWarnings("unchecked")
			Map<String, String> error = (Map<String, String>) sessionStrategy.getAttribute(attrs, AUTHORIZATION_ERROR_ATTRIBUTE);			
			sessionStrategy.removeAttribute(attrs, AUTHORIZATION_ERROR_ATTRIBUTE);
			
			errorMessage = error.get("error");
			
			if (error.get("errorDescription")!=null) {
				errorMessage += "<br/>" + error.get("errorDescription");
			}
			
			if (error.get("errorUri")!=null) {
				errorMessage += "<br/> Error Uri: " + error.get("errorUri");
			}						
			
		} else if (sessionStrategy.getAttribute(attrs, DUPLICATE_CONNECTION_ATTRIBUTE) != null) {
			
			DuplicateConnectionException error = (DuplicateConnectionException) sessionStrategy.getAttribute(attrs, DUPLICATE_CONNECTION_ATTRIBUTE);
			errorMessage = error.getMessage();
			sessionStrategy.removeAttribute(attrs, DUPLICATE_CONNECTION_ATTRIBUTE);						
		}
		
		/*
		 * Handle MainUI uri fragment
		 */
		sessionStrategy.setAttribute(attrs, VaadinRedirectObject.REDIRECT_OBJECT_SESSION_ATTRIBUTE, new VaadinRedirectObject(ViewToken.SOCIAL, errorMessage));				
		
		response.sendRedirect(redirectUrl);	
	}

}
