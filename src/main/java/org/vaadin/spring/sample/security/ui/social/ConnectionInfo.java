package org.vaadin.spring.sample.security.ui.social;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.vaadin.spring.sample.security.ui.formsender.SocialProviderAction;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.util.ReflectTools;

public class ConnectionInfo extends Panel implements ClickListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5016539681392143932L;
	private Label info;		
	private HorizontalLayout layout;
	private Image image;
	
	private String providerName;
	
	private boolean connected = false;
	private String displayName;
	private String profileImageUrl;
	private String profileUrl;
	
	private Button btnConnect;
	private Button btnDisconnect;
	
	public ConnectionInfo(String providerName) {
		super(providerName);
		setSizeUndefined();
		setWidth("100%");
		this.providerName = providerName;
		if (providerName.equals("Facebook")) {
			setIcon(FontAwesome.FACEBOOK);
			btnConnect = new Button("Connect to " + providerName, FontAwesome.FACEBOOK_SQUARE);
			btnDisconnect = new Button("Disconnect from " + providerName, FontAwesome.FACEBOOK_SQUARE);
		} else {
			setIcon(FontAwesome.GOOGLE_PLUS);
			btnConnect = new Button("Connect to " + providerName, FontAwesome.GOOGLE_PLUS_SQUARE);
			btnDisconnect = new Button("Disconnect from " + providerName, FontAwesome.GOOGLE_PLUS_SQUARE);
		}
				
		
		layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);
		setContent(layout);
					
		image = new Image();
		image.setWidth("60px");
		image.setHeight("60px");
		
		info = new Label();
		info.setContentMode(ContentMode.HTML);
		info.setSizeUndefined();
		info.addStyleName(ValoTheme.LABEL_LARGE);
		
		layout.addComponents(image, info, btnConnect, btnDisconnect);
		layout.setComponentAlignment(info, Alignment.MIDDLE_LEFT);
		layout.setComponentAlignment(btnConnect, Alignment.MIDDLE_RIGHT);
		layout.setComponentAlignment(btnDisconnect, Alignment.MIDDLE_RIGHT);
		layout.setExpandRatio(info, 1);
		
		btnConnect.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnDisconnect.addStyleName(ValoTheme.BUTTON_DANGER);
		btnConnect.addClickListener(this);
		btnDisconnect.addClickListener(this);
		
	}
			
	
	public void update() {
		btnConnect.setVisible(!connected);
		btnDisconnect.setVisible(connected);
		
		if (connected) {
			info.setValue("You are connected to " + providerName + " as " + displayName + ".<br/><a href=\"" +profileUrl + "\">Go to " + providerName + " profile.</a>");
			
		} else {
			info.setValue("You are not yet connected to " + providerName + ".");				
		}
		
		image.setSource((getProfileImageUrl() != null) ? new ExternalResource(profileImageUrl) : null);
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		
		if (event.getButton() == btnConnect) {
			if (providerName.equals("Facebook")) {
				fireEvent(new ConnectionInfoEvent(ConnectionInfo.this, SocialProviderAction.FACEBOOK_CONNECT));
			} else {
				fireEvent(new ConnectionInfoEvent(ConnectionInfo.this, SocialProviderAction.GOOGLE_CONNECT));
			}
			
		}
		
		if (event.getButton() == btnDisconnect) {
			if (providerName.equals("Facebook")) {
				fireEvent(new ConnectionInfoEvent(ConnectionInfo.this, SocialProviderAction.FACEBOOK_DISCONNECT));
			} else {
				fireEvent(new ConnectionInfoEvent(ConnectionInfo.this, SocialProviderAction.GOOGLE_DISCONNECT));
			}
		}
		
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
	
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	
	public String getProfileUrl() {
		return profileUrl;
	}
	
	public static class ConnectionInfoEvent extends Event {

		
		private static final long serialVersionUID = 7683177955730954373L;
		private final SocialProviderAction socialProviderAction;
					
		public ConnectionInfoEvent(Component source, SocialProviderAction socialProviderAction) {
			super(source);
			this.socialProviderAction = socialProviderAction;		
		}
		
		public SocialProviderAction getSocialProviderAction() {
			return socialProviderAction;
		}
		
	}
	
	public interface ConnectionInfoEventListener extends Serializable {
		
		public static final Method METHOD = ReflectTools.findMethod(ConnectionInfo.ConnectionInfoEventListener.class, "onConnectionInfoEvent", ConnectionInfo.ConnectionInfoEvent.class);
		
		public void onConnectionInfoEvent(ConnectionInfo.ConnectionInfoEvent event);
	}
	
	public void addConnectionInfoEventListener(ConnectionInfo.ConnectionInfoEventListener listener) {
		super.addListener(ConnectionInfo.ConnectionInfoEvent.class, listener, ConnectionInfoEventListener.METHOD);
	}
	
	public void removeConnectionInfoEventListener(ConnectionInfo.ConnectionInfoEventListener listener) {
		super.removeListener(ConnectionInfo.ConnectionInfoEvent.class, listener, ConnectionInfoEventListener.METHOD);
	}

	
}