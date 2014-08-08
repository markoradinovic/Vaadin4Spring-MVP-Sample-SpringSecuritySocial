package org.vaadin.spring.sample.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ReconnectFilter;
import org.springframework.social.facebook.web.DisconnectController;
import org.vaadin.spring.sample.security.account.AccountRepository;
import org.vaadin.spring.sample.security.controller.VaadinRedirectingView;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public UserIdSource getUserIdSource() {
		return new UserIdSource() {			
			@Override
			public String getUserId() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication == null) {
					throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
				}
				return authentication.getName();
			}
		};
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(
			ConnectionFactoryLocator connectionFactoryLocator) {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
		
		/*
		 * To allow implicit signup, uncomment this line 
		 */
		//repository.setConnectionSignUp(new AccountConnectionSignUp(accountRepository));
		
		return repository;
	}		
		
	@Bean
	public DisconnectController disconnectController(UsersConnectionRepository usersConnectionRepository, Environment environment) {
		return new DisconnectController(usersConnectionRepository, environment.getProperty("spring.social.facebook.app-secret"));
	}

	@Bean
	public ReconnectFilter apiExceptionHandler(UsersConnectionRepository usersConnectionRepository, UserIdSource userIdSource) {
		return new ReconnectFilter(usersConnectionRepository, userIdSource);
	}
	
	@Bean(name={"connect/facebookConnect", "connect/facebookConnected", "connect/googleConnect", "connect/googleConnected"})
	public VaadinRedirectingView facebookConnectedRedirectView() {
		VaadinRedirectingView view = new VaadinRedirectingView("/ui");
		return view;
	}	
	

}
