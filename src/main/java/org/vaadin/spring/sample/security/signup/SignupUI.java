package org.vaadin.spring.sample.security.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.UsersConnectionRepository;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.sample.security.account.AccountRepository;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@VaadinUI(path="/signup")
@Theme("valo")
public class SignupUI extends UI {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	UsersConnectionRepository usersConnectionRepository;
	
	private SignupContent signupContent;
	
//	private final ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils();

	@Override
	protected void init(VaadinRequest request) {
		signupContent = new SignupContent(accountRepository, usersConnectionRepository);
		setContent(signupContent);
	}

	
	
	
	
	
}
