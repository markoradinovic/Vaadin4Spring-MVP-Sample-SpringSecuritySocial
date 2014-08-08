package org.vaadin.spring.sample.security.ui.formsender;

import java.io.Serializable;

import com.vaadin.ui.AbstractJavaScriptComponent;

@SuppressWarnings("serial")
/*
 * This is not working
 * @JavaScript({"https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js", "form_sender.js"})
 * 
 * Because of
 * Refused to display 'http://localhost:8080/ui/APP/PUBLISHED/form_sender.js' in a frame because it set 'X-Frame-Options' to 'DENY'.
 * 
 * Workaround - Modify Vaadin startup page to include required js.
 */
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
	
	public static class SocialProviderAction implements Serializable {
		
		public static final SocialProviderAction FACEBOOK_CONNECT = new SocialProviderAction("facebook", "connect");
		public static final SocialProviderAction FACEBOOK_DISCONNECT = new SocialProviderAction("facebook", "disconnect");
		public static final SocialProviderAction GOOGLE_CONNECT = new SocialProviderAction("google", "connect");
		public static final SocialProviderAction GOOGLE_DISCONNECT = new SocialProviderAction("google", "disconnect");
		
		private String provider;
		private String action;
		
		public SocialProviderAction() {		
		}
		

		public SocialProviderAction(String provider, String action) {
			super();
			this.provider = provider;
			this.action = action;
		}


		public String getProvider() {
			return provider;
		}

		public void setProvider(String provider) {
			this.provider = provider;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((action == null) ? 0 : action.hashCode());
			result = prime * result
					+ ((provider == null) ? 0 : provider.hashCode());
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SocialProviderAction other = (SocialProviderAction) obj;
			if (action == null) {
				if (other.action != null)
					return false;
			} else if (!action.equals(other.action))
				return false;
			if (provider == null) {
				if (other.provider != null)
					return false;
			} else if (!provider.equals(other.provider))
				return false;
			return true;
		}


		
		
	}
	
}
