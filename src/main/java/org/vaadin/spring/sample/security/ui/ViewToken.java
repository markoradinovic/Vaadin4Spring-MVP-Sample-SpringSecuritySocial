package org.vaadin.spring.sample.security.ui;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public interface ViewToken extends Serializable {
	
	public static final String HOME="";
	public static final String SOCIAL="/social";
	
	public static final String ADMIN="/admin";
	
	public static final List<String> VALID_TOKENS = Arrays.asList(new String[] {SOCIAL});		

}
