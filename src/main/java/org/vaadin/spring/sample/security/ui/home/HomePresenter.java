package org.vaadin.spring.sample.security.ui.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.util.StringUtils;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.mvp.MvpHasPresenterHandlers;
import org.vaadin.spring.mvp.MvpView;
import org.vaadin.spring.mvp.presenter.AbstractMvpPresenterView;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.spring.sample.security.ui.ViewToken;
import org.vaadin.spring.security.Security;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@SuppressWarnings("serial")
@UIScope
@VaadinView(name=ViewToken.HOME)
public class HomePresenter extends AbstractMvpPresenterView<HomePresenter.HomeView> implements HomePresenterHandlers {
	
	public interface HomeView extends MvpView, MvpHasPresenterHandlers<HomePresenterHandlers> {
		public void initView(String userName, String loginType);
	}		
	
	@Autowired
	Security security;
	
	@Autowired
	public HomePresenter(HomeView view, EventBus eventBus) {
		super(view, eventBus);
		getView().setPresenterHandlers(this);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		/*
		 * UsernamePasswordAuthenticationToken OR
		 * SocialAuthenticationToken
		 */
		Authentication a = security.getAuthentication(); //SocialAuthenticationToken //UsernamePassword
		String userName = a.getName();
		
		/*
		 * SocialUser or User
		 */
//		Object principial = a.getPrincipal(); //SocialUser //User
		
		String loginType = "";
		if (a instanceof UsernamePasswordAuthenticationToken) {
			loginType = "username and password";
			
		} else if (a instanceof SocialAuthenticationToken) {
			SocialAuthenticationToken token = (SocialAuthenticationToken) a;
			loginType = StringUtils.capitalize(token.getProviderId()) + " account";			
		}		
		getView().initView(userName, loginType);
		
	}	
	
}
