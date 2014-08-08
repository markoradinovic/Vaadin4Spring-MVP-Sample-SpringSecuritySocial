package org.vaadin.spring.sample.security.ui.social;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.google.api.Google;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.mvp.MvpHasPresenterHandlers;
import org.vaadin.spring.mvp.MvpView;
import org.vaadin.spring.mvp.presenter.AbstractMvpPresenterView;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.spring.sample.security.account.AccountRepository;
import org.vaadin.spring.sample.security.ui.ViewToken;
import org.vaadin.spring.sample.security.ui.formsender.FormSender;
import org.vaadin.spring.sample.security.ui.formsender.FormSender.SocialProviderAction;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@SuppressWarnings("serial")
@UIScope
@VaadinView(name=ViewToken.SOCIAL)
public class SocialPresenter extends AbstractMvpPresenterView<SocialPresenter.SocialView> implements SoacialPresenterHandlers {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	public interface SocialView extends MvpView, MvpHasPresenterHandlers<SoacialPresenterHandlers> {
		void setConnectionInfo(String providerName, boolean connected, String displayName, String profileUrl, String profileImageUrl);
		FormSender getFormSender();
	}
		
	@Autowired
	private Provider<ConnectionRepository> connectionRepositoryProvider;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public void postConstruct() {	
		super.postConstruct();			
	}
	
	
	@Autowired
	public SocialPresenter(SocialView view, EventBus eventBus) {
		super(view, eventBus);	
		getView().setPresenterHandlers(this);
	}

	@Override
	public void enter(ViewChangeEvent event) {		
		loadConnections();
	}

	
	public void loadConnections() {
//		MultiValueMap<String, Connection<?>> connections = getConnectionRepository().findAllConnections();
						
		Connection<Facebook> facebookConnection = getConnectionRepository().findPrimaryConnection(Facebook.class);		
		if (facebookConnection != null) {
			getView().setConnectionInfo("Facebook", true, facebookConnection.getApi().userOperations().getUserProfile().getEmail(), facebookConnection.getProfileUrl(), facebookConnection.getImageUrl());
		} else {
			getView().setConnectionInfo("Facebook", false, "", "", null);
		}
				
		Connection<Google> googleConnection = getConnectionRepository().findPrimaryConnection(Google.class);
		if (googleConnection != null) {
			getView().setConnectionInfo("Google", true, googleConnection.getDisplayName(), googleConnection.getProfileUrl(), googleConnection.getImageUrl());
		} else {
			getView().setConnectionInfo("Google", false, "", "", null);
		}
		
	}
	
	
	private ConnectionRepository getConnectionRepository() {
		return connectionRepositoryProvider.get();
	}

	@Override
	public void connectToSocialNetwork(SocialProviderAction socialProviderAction) {
		LOG.debug("Attempting to " + socialProviderAction.getAction() + "  - " + socialProviderAction.getProvider());
		getView().getFormSender().setSocialProviderAction(socialProviderAction);
		getView().getFormSender().postForm();
	}
	
	
	
	

}
