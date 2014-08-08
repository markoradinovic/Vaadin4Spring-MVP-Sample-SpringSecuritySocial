package org.vaadin.spring.sample.security.ui.social;

import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.mvp.view.AbstractMvpView;
import org.vaadin.spring.sample.security.ui.formsender.FormSender;
import org.vaadin.spring.sample.security.ui.social.ConnectionInfo.ConnectionInfoEvent;
import org.vaadin.spring.sample.security.ui.social.ConnectionInfo.ConnectionInfoEventListener;
import org.vaadin.spring.sample.security.ui.social.SocialPresenter.SocialView;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@UIScope
@VaadinComponent
public class SocialViewImpl extends AbstractMvpView implements SocialView, ClickListener, ConnectionInfoEventListener {

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
		facebookConnectionInfo.addConnectionInfoEventListener(this);
		
		googleConnectionInfo = new ConnectionInfo("Google");
		googleConnectionInfo.addConnectionInfoEventListener(this);
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
	
	@Override
	public void onConnectionInfoEvent(ConnectionInfoEvent event) {
		mvpPresenterHandlers.connectToSocialNetwork(event.getSocialProviderAction());
		
	}

	@Override
	public FormSender getFormSender() {	
		return formSender;
	}

	
	
	@Override
	public void buttonClick(ClickEvent event) {
				
	}

	



	



	

	


}
