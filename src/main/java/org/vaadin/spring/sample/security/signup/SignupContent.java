package org.vaadin.spring.sample.security.signup;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.util.StringUtils;
import org.vaadin.spring.sample.security.account.Account;
import org.vaadin.spring.sample.security.account.AccountRepository;
import org.vaadin.spring.sample.security.account.UsernameAlreadyInUseException;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * <p> Spring Social Facebook is coded against v1.0. 
 * 	If you try to use it against v2.0, then most things will still work, but some things won't 
 * 	(usernames, for example. Also, getting a list of friends will only return friends who have also authorized the same application.)
 * @author marko
 *
 */
@SuppressWarnings("serial")
public class SignupContent extends VerticalLayout implements ClickListener {

	private final AccountRepository accountRepository;
	private final UsersConnectionRepository usersConnectionRepository;
				
	private final VerticalLayout container;
	private final FormLayout form;

	private TextField username;
	private PasswordField password;
	private TextField firstName;	
	private TextField lastName;
	
	private final Label infoLabel;
	
	private final Button signUp;
	
	private BeanFieldGroup<SignupModel> binder = new BeanFieldGroup<SignupModel>(SignupModel.class);
	
	public SignupContent(AccountRepository accountRepository, UsersConnectionRepository usersConnectionRepository) {
		super();
		setSizeFull();
		setSpacing(true);
		this.accountRepository = accountRepository;
		this.usersConnectionRepository = usersConnectionRepository;
		
		infoLabel = new Label();
		infoLabel.addStyleName(ValoTheme.LABEL_H2);
		infoLabel.setSizeUndefined();
		addComponent(infoLabel);
		setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);
		
		container = new VerticalLayout();
		container.setSizeUndefined();
		container.setSpacing(true);
		addComponent(container);
		setComponentAlignment(container, Alignment.MIDDLE_CENTER);
		setExpandRatio(container, 1);
						
		form = new FormLayout();
		form.setWidth("400px");
		form.setSpacing(true);
		container.addComponent(form);
		buildForm();
		
		signUp = new Button("Signup", FontAwesome.FLOPPY_O);
		signUp.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		signUp.addClickListener(this);
		container.addComponent(signUp);
		container.setComponentAlignment(signUp, Alignment.MIDDLE_CENTER);
		
		
		
		ProviderSignInAttempt attempt = (ProviderSignInAttempt) UI.getCurrent().getSession().getSession().getAttribute(ProviderSignInAttempt.SESSION_ATTRIBUTE); 		
		Connection<?> connection = (attempt != null) ? attempt.getConnection() : null;
				
		if (connection != null) {
			infoLabel.setValue("Your " + StringUtils.capitalize(connection.getKey().getProviderId()) + " account is not associated with a Vaadin4Spring account. If you're new, please sign up.");
			SignupModel signupModel = SignupModel.fromProviderUser(connection.fetchUserProfile());
			binder.setItemDataSource(new BeanItem<SignupModel>(signupModel));
		} else {
			infoLabel.setValue("Vaadin4Spring Sign up");
			binder.setItemDataSource(new BeanItem<SignupModel>(new SignupModel()));
		}
				
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		
		try {
			binder.commit();
			
			SignupModel model = binder.getItemDataSource().getBean();			
			Account account = new Account(model.getUsername(), model.getPassword(), model.getFirstName(), model.getLastName());
			accountRepository.createAccount(account);
			
			if (account != null) {
				SignInUtils.signin(account.getUsername());
				
				//Create social connection				
				ProviderSignInAttempt attempt = (ProviderSignInAttempt) UI.getCurrent().getSession().getSession().getAttribute(ProviderSignInAttempt.SESSION_ATTRIBUTE);
				if (attempt != null) {
					usersConnectionRepository.createConnectionRepository(account.getUsername()).addConnection(attempt.getConnection());
					UI.getCurrent().getSession().getSession().removeAttribute(ProviderSignInAttempt.SESSION_ATTRIBUTE);
				}
				
				Page.getCurrent().setLocation("/ui/");
			}
			
		} catch (CommitException e) {
			username.setValidationVisible(true);
			password.setValidationVisible(true);
			firstName.setValidationVisible(true);
			lastName.setValidationVisible(true);
			
		} catch (UsernameAlreadyInUseException eu) {
			username.setComponentError(new UserError(eu.getMessage()));
			username.setValidationVisible(true);
			password.setValidationVisible(true);
			firstName.setValidationVisible(true);
			lastName.setValidationVisible(true);
			
		}
		
	}
	
	
	private void buildForm() {
		username = new TextField("Username");
		username.setWidth("100%");
		username.setImmediate(true);
		username.setValidationVisible(false);
		username.setNullRepresentation("");
		form.addComponent(username);
		
		password = new PasswordField("Password");
		password.setWidth("100%");
		password.setImmediate(true);
		password.setValidationVisible(false);
		password.setNullRepresentation("");
		form.addComponent(password);
		
		firstName = new TextField("First name");
		firstName.setWidth("100%");
		firstName.setValidationVisible(false);
		firstName.setNullRepresentation("");
		firstName.setImmediate(true);
		form.addComponent(firstName);
		
		lastName = new TextField("Last name");
		lastName.setWidth("100%");
		lastName.setImmediate(true);
		lastName.setNullRepresentation("");
		lastName.setValidationVisible(false);
		form.addComponent(lastName);
		
		binder.bind(username, "username");
		binder.bind(password, "password");
		binder.bind(firstName, "firstName");
		binder.bind(lastName, "lastName");
		
	}

}
