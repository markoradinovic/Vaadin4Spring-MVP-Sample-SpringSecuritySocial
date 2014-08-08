package org.vaadin.spring.sample.security.ui.social;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.mvp.view.AbstractMvpView;
import org.vaadin.spring.sample.security.ui.formsender.FormSender;
import org.vaadin.spring.sample.security.ui.formsender.FormSender.SocialProviderAction;
import org.vaadin.spring.sample.security.ui.social.SocialPresenter.SocialView;
import org.vaadin.spring.sample.security.ui.social.SocialViewImpl.ConnectionInfo.ConnectionInfoEvent;
import org.vaadin.spring.sample.security.ui.social.SocialViewImpl.ConnectionInfo.ConnectionInfoEventListener;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.util.ReflectTools;

@SuppressWarnings("serial")
@UIScope
@VaadinComponent
public class SocialViewImpl extends AbstractMvpView implements SocialView, ClickListener {

	private SoacialPresenterHandlers mvpPresenterHandlers;
	
	private VerticalLayout content;
	
	private Label caption;
	
	private ConnectionInfo facebookConnectionInfo;
	private ConnectionInfo googleConnectionInfo;
	
	private FormSender formSender;
	
	@Override
	public void postConstruct() {	
		super.postConstruct();
		setSizeFull();
						
		content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);
		setCompositionRoot(content);
		content.setDefaultComponentAlignment(Alignment.TOP_CENTER);
								
		caption = new Label("My social networks connections", ContentMode.HTML);
		caption.addStyleName(ValoTheme.LABEL_H1);
		caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		caption.setSizeUndefined();
		content.addComponent(caption);
		
		facebookConnectionInfo = new ConnectionInfo("Facebook");
		facebookConnectionInfo.addConnectionInfoEventListener(connectionInfoEventListener);
		
		googleConnectionInfo = new ConnectionInfo("Google");
		googleConnectionInfo.addConnectionInfoEventListener(connectionInfoEventListener);
		content.addComponents(facebookConnectionInfo, googleConnectionInfo);
		
		formSender = new FormSender();
		content.addComponent(formSender);
								
	}
	
	@Override
	public void setPresenterHandlers(
			SoacialPresenterHandlers mvpPresenterHandlers) {
		this.mvpPresenterHandlers = mvpPresenterHandlers;
		
	}
	
	@Override
	public void setConnectionInfo(String providerName, boolean connected,
			String displayName, String profileUrl, String profileImageUrl) {
		
		if (providerName.equals("Facebook")) {
			facebookConnectionInfo.setConnected(connected);
			facebookConnectionInfo.setDisplayName(displayName);
			facebookConnectionInfo.setProfileUrl(profileUrl);
			facebookConnectionInfo.setProfileImageUrl(profileImageUrl);
			facebookConnectionInfo.update();
		} else {
			googleConnectionInfo.setConnected(connected);
			googleConnectionInfo.setDisplayName(displayName);
			googleConnectionInfo.setProfileUrl(profileUrl);
			googleConnectionInfo.setProfileImageUrl(profileImageUrl);
			googleConnectionInfo.update();
		}
				
	}
	
	private ConnectionInfoEventListener connectionInfoEventListener = new ConnectionInfoEventListener() {
		
		@Override
		public void onConnectionInfoEvent(ConnectionInfoEvent event) {
			
			mvpPresenterHandlers.connectToSocialNetwork(event.getSocialProviderAction());			
		}
	};  

	@Override
	public FormSender getFormSender() {	
		return formSender;
	}

	
	
	@Override
	public void buttonClick(ClickEvent event) {
				
	}
	
	
	
	public static class ConnectionInfo extends Panel implements ClickListener {
		
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
			
			public static final Method METHOD = ReflectTools.findMethod(ConnectionInfoEventListener.class, "onConnectionInfoEvent", ConnectionInfoEvent.class);
			
			public void onConnectionInfoEvent(ConnectionInfoEvent event);
		}
		
		public void addConnectionInfoEventListener(ConnectionInfoEventListener listener) {
			super.addListener(ConnectionInfoEvent.class, listener, ConnectionInfoEventListener.METHOD);
		}
		
		public void removeConnectionInfoEventListener(ConnectionInfoEventListener listener) {
			super.removeListener(ConnectionInfoEvent.class, listener, ConnectionInfoEventListener.METHOD);
		}

		
	}



	



	

	


}
