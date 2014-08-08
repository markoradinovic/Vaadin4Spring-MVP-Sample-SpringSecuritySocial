package org.vaadin.spring.sample.security.ui.formsender;

import com.vaadin.ui.AbstractJavaScriptComponent;


/*
 * This is not working
 * @JavaScript({"https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js", "form_sender.js"})
 * 
 * Because of
 * Refused to display 'http://localhost:8080/ui/APP/PUBLISHED/form_sender.js' in a frame because it set 'X-Frame-Options' to 'DENY'.
 * 
 * Workaround - Modify Vaadin startup page to include required js.
 */
@SuppressWarnings("serial")
public class FormSender extends AbstractJavaScriptComponent {

		
	public void postForm() {
		callFunction("postForm");
	}
	
	public void setSocialProviderAction(SocialProviderAction action) {
		getState().provider = action.getProvider();
		getState().action = action.getAction();
		markAsDirty();
	}
	
	public SocialProviderAction getSocialProviderAction() {
		return new SocialProviderAction(getState().provider, getState().action);
	}
		
	
	@Override
	protected FormSenderState getState() {		
		return (FormSenderState) super.getState();
	}
	
}
