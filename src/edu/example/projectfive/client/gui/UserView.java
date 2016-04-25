package edu.example.projectfive.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.example.projectfive.client.model.Person;
import edu.example.projectfive.client.service.PersonServiceClientImpl;


public class UserView extends Composite{

	private PersonServiceClientImpl clientImpl ;
	private VerticalPanel vPanel = new VerticalPanel ();
	private Button[] delete;
	private Button[] change;
	private Button addUser;
	
	public UserView(PersonServiceClientImpl clientImpl ){

		this.clientImpl = clientImpl;
		HTML label1 = new HTML(new SafeHtmlBuilder().appendEscapedLines("\n  ").toSafeHtml());
		this.vPanel.add(label1);
		initWidget(this.vPanel);
		vPanel.setStyleName("style");
		final Label un = new Label("USERS!");
		vPanel.add(un);
		HTML label = new HTML(new SafeHtmlBuilder().appendEscapedLines("\n").toSafeHtml());
		vPanel.add(label);	
		clientImpl.service.getOperatoers(new AsyncCallback<Person[]>(){

			@Override
			public void onFailure(Throwable caught) {
				un.setText("FUCKING PLEB SHIT");
			}

			@Override
			public void onSuccess(Person[] result) {
				delete = new Button[result.length];
				change = new Button[result.length];
				for(int i = 0; i < result.length; i++){
					Label label = new Label(result[i].getNavn()+" , "+result[i].getOprId()+" , "+result[i].getCpr());
					vPanel.add(label);
					delete[i] = new Button("Delete");
					vPanel.add(delete[i]);
					change[i] = new Button("Change");
					vPanel.add(change[i]);
					vPanel.add(label);	
				}
			}
			
		});
		addUser = new Button("Add user!");
		vPanel.add(label);	
		vPanel.add(addUser);
		
		// insert the different things on the website
		
		
		vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		//btn1.addClickHandler(new BtnClickHandler());
	}
	
	private class BtnClickHandler implements ClickHandler{
		private String username , password;
		private int id ;
		@Override
		public void onClick(ClickEvent event) {
			clientImpl.service.getOperatoer(id, new AsyncCallback<Person>(){
				@Override
				public void onFailure(Throwable caught) {
				}
				@Override
				public void onSuccess(Person result) {
				
				}
			});
		}
	}
	
}