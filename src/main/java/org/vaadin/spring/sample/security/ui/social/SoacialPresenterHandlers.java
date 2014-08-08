package org.vaadin.spring.sample.security.ui.social;

import org.vaadin.spring.mvp.MvpPresenterHandlers;
import org.vaadin.spring.sample.security.ui.formsender.SocialProviderAction;

public interface SoacialPresenterHandlers extends MvpPresenterHandlers {

	void connectToSocialNetwork(SocialProviderAction socialProviderAction);
	
}
