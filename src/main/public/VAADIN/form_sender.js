/**
 * FormSender
 */
window.org_vaadin_spring_sample_security_ui_formsender_FormSender = function() {
	//var element = $(this.getElement());	
	var element = this.getElement();
	var provider = "";
	var action = "";
	
  
  	this.onStateChange = function() {
  		element.style.display="none";
  		
  		provider = this.getState().provider; 
  		action = this.getState().action;
  		console.log("On state change: provider[" + provider + "] action[" + action + "]");  		  								
  		
  		$(this.getElement()).empty();
  		
  		if (provider=="facebook") {
  			if (action=="connect") {
  		  		$(this.getElement()).append(
  		  				'<form id="facebook" action="/connect/facebook" method="POST">' +
  		  				'<input type="hidden" name="scope" value="email,publish_stream,user_photos,offline_access" />' +
  		  				'</form>'
  		  				);
  	  		} else {
	  	  		$(this.getElement()).append(
		  	  		'<form id="facebook" action="/connect/facebook" method="POST">' +			
					'<input type="hidden" name="_method" value="delete" />' +
		  	  		'</form>');
	  	  	}
  		} else {
  			//google
  			if (action=="connect") {
  		  		$(this.getElement()).append(
  		  				'<form id="google" action="/connect/google" method="POST">' +
  		  				'<input type="hidden" name="scope" value="profile email https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/tasks https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/latitude.all.best" />' +
  		  				'</form>'
  		  				);
  	  		} else {
  	  			$(this.getElement()).append(
		  				'<form id="google" action="/connect/google" method="POST">' +
		  				'<input type="hidden" name="_method" value="delete" />' +
		  				'</form>'
		  				)
  	  		}
  		}
  		
  	}
  	
  	this.postForm = function() {  
  		console.log(this.getElement());
  		$('#'+provider).submit();
  	}
}