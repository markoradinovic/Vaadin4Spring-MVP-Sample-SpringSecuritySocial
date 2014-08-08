package org.vaadin.spring.sample.security.account;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

/**
 * Class that enables implicit signup - No redirect to sigup form !
 * @author marko
 *
 */
public class AccountConnectionSignUp implements ConnectionSignUp {

	private final AccountRepository accountRepository;
	
	public AccountConnectionSignUp(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@Override
	public String execute(Connection<?> connection) {
		UserProfile profile = connection.fetchUserProfile();
		Account account = new Account(profile.getName(), "", profile.getFirstName(), profile.getLastName());
		try {
			accountRepository.createAccount(account);
		} catch (UsernameAlreadyInUseException e) {			
			e.printStackTrace();
		}		
		return account.getUsername();
	}

}
