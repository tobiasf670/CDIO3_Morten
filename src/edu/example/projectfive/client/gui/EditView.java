package edu.example.projectfive.client.gui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.example.projectfive.client.model.Person;
import edu.example.projectfive.client.service.PersonServiceClientImpl;
import edu.example.projectfive.shared.FieldVerifier;




public class EditView extends Composite {
	VerticalPanel editPanel;
	FlexTable t;


	// editing text boxes
	TextBox IDText;
	TextBox nameTxt;
	TextBox cprTxt;
	TextBox passwordTxt;
	TextBox adminTxt;
	TextBox operatoerTxt;
	TextBox farmaceutTxt;

	// valid fields - initially a field is valid
	boolean IDValid = true;
	boolean nameValid = true;
	boolean cprValid = true;
	boolean passwordValid = true;
	boolean adminValid = true;
	boolean operatoerValid = true;
	boolean farmaceutValid = true;

	int eventRowIndex;

	Anchor ok ;
	// V.1 reference to data layer
	// IPersonDAO iPersonDAO;

	// V.2
	PersonServiceClientImpl clientImpl;


	// person list
	List<Person> personer;

	// previous cancel anchor
	Anchor previousCancel = null;

	public EditView(PersonServiceClientImpl clientImpl) {
		// V.1 this.iPersonDAO = iPersonDAO;
		// v.2
		this.clientImpl = clientImpl;

		editPanel = new VerticalPanel();
		initWidget(this.editPanel);

		t = new FlexTable();

		// adjust column widths
	//	t.getFlexCellFormatter().setWidth(0, 0, "50px");
		//t.getFlexCellFormatter().setWidth(0, 1, "100px");
		//t.getFlexCellFormatter().setWidth(0, 2, "50px");

		// style table
		t.addStyleName("FlexTable");
		t.getRowFormatter().addStyleName(0,"FlexTable-Header");

		// set headers in flextable
		t.setText(0, 0, "Id");
		t.setText(0, 1, "Navn");
		t.setText(0, 2, "Cpr");
		t.setText(0, 3, "Password");
		t.setText(0, 4, "Admin");
		t.setText(0, 5, "Operatoer");
		t.setText(0, 6, "farmaceut");

		// V.1 fetch persons from data layer
		// personer = iPersonDAO.getPersons();

		// V.1 populate table and add edit anchor to each row
		//		for (int rowIndex=0; rowIndex < personer.size(); rowIndex++) {
		//			t.setText(rowIndex+1, 0, personer.get(rowIndex).getNavn());
		//			t.setText(rowIndex+1, 1, "" + personer.get(rowIndex).getAlder());
		//			Anchor edit = new Anchor("edit");
		//			t.setWidget(rowIndex+1, 2, edit);
		//
		//			edit.addClickHandler(new EditHandler());
		//		}


		// V.2
		clientImpl.service.getPersons(new AsyncCallback<List<Person>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Server fejl!" + caught.getMessage());
			}

			@Override
			public void onSuccess(List<Person> result) {
				// populate table and add delete anchor to each row
				for (int rowIndex=0; rowIndex < result.size(); rowIndex++) {
					t.setText(rowIndex+1, 0, "" + result.get(rowIndex).getOprId());
					t.setText(rowIndex+1, 1, result.get(rowIndex).getNavn());
					t.setText(rowIndex+1, 2, "" + result.get(rowIndex).getCpr());
					t.setText(rowIndex+1, 3, "" + result.get(rowIndex).getPassword());
					t.setText(rowIndex+1, 4, "" + result.get(rowIndex).isAdmin());
					t.setText(rowIndex+1, 5, "" + result.get(rowIndex).isOperatoer());
					t.setText(rowIndex+1, 6, "" + result.get(rowIndex).isFarmaceut());
					Anchor edit = new Anchor("edit");
					t.setWidget(rowIndex+1, 7, edit);

					edit.addClickHandler(new EditHandler());
				}

			}

		});



		editPanel.add(t);

		// text boxes
		IDText = new TextBox();
		IDText.setWidth("20px");
		nameTxt = new TextBox();
		nameTxt.setWidth("60px");
		cprTxt = new TextBox();
		cprTxt.setWidth("80px");
		passwordTxt = new TextBox();
		passwordTxt.setWidth("70px");
		adminTxt = new TextBox();
		adminTxt.setWidth("45px");
		adminTxt.setStyleName("styleLowercase");
		operatoerTxt = new TextBox();
		operatoerTxt.setWidth("45px");
		operatoerTxt.setStyleName("styleLowercase");
		farmaceutTxt = new TextBox();
		farmaceutTxt.setWidth("45px");
		farmaceutTxt.setStyleName("styleLowercase");
		
		
		
	}

	private class EditHandler implements ClickHandler {
		public void onClick(ClickEvent event) {

			// if previous edit open - force cancel operation¨
			if (previousCancel != null)
				previousCancel.fireEvent(new ClickEvent(){});

			// get rowindex where event happened
			eventRowIndex = t.getCellForEvent(event).getRowIndex();

			// populate textboxes
			IDText.setText(t.getText(eventRowIndex, 0));
			nameTxt.setText(t.getText(eventRowIndex, 1));
			cprTxt.setText(t.getText(eventRowIndex, 2));
			passwordTxt.setText(t.getText(eventRowIndex, 3));
			adminTxt.setText(t.getText(eventRowIndex, 4));
			operatoerTxt.setText(t.getText(eventRowIndex, 5));
			farmaceutTxt.setText(t.getText(eventRowIndex, 6));
			// show text boxes for editing
			
		//	t.setWidget(eventRowIndex, 0, IDText);
			t.setWidget(eventRowIndex, 1, nameTxt);
			t.setWidget(eventRowIndex, 2, cprTxt);
			t.setWidget(eventRowIndex, 3, passwordTxt);
			t.setWidget(eventRowIndex, 4, adminTxt);
			t.setWidget(eventRowIndex, 5, operatoerTxt);
			t.setWidget(eventRowIndex, 6, farmaceutTxt);

			// start editing here
			nameTxt.setFocus(true);

			// get edit anchor ref for cancel operation
			final Anchor edit =  (Anchor) event.getSource();

			// get textbox contents for cancel operation
			final String ID = IDText.getText();
			final String name = nameTxt.getText();
			final String cpr = cprTxt.getText();
			final String password = passwordTxt.getText();
			final String admin =  adminTxt.getText();
			final String operatoer = operatoerTxt.getText();
			final String farmaceut = farmaceutTxt.getText();
			


			ok = new Anchor("ok");
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					// remove inputboxes
				//	t.setText(eventRowIndex, 0, IDText.getText());
					t.setText(eventRowIndex, 1, nameTxt.getText());
					t.setText(eventRowIndex, 2, cprTxt.getText());
					t.setText(eventRowIndex, 3, passwordTxt.getText());
					t.setText(eventRowIndex, 4,	adminTxt.getText());
					t.setText(eventRowIndex, 5, operatoerTxt.getText());
					t.setText(eventRowIndex, 6, farmaceutTxt.getText());


					// here you will normally fetch the primary key of the row 
					// and use it for location the object to be edited

					// fill DTO with id and new values 
					Person person = new Person(Integer.parseInt (IDText.getText()), nameTxt.getText(), nameTxt.getText().substring(0, 2), cprTxt.getText(), passwordTxt.getText(), Boolean.parseBoolean(adminTxt.getText()), Boolean.parseBoolean(operatoerTxt.getText()),Boolean.parseBoolean(farmaceutTxt.getText()));

					// V.1 update object in backend
					// iPersonDAO.updatePerson(personDTO, eventRowIndex-1);


					// V.2
					clientImpl.service.updatePerson(person, new AsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {

						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Server fejl!" + caught.getMessage());
						}

					});

					// restore edit link
					t.setWidget(eventRowIndex, 7, edit);
					t.clearCell(eventRowIndex, 8);

					previousCancel = null;

				}

			});

			Anchor cancel = new Anchor("cancel");
			previousCancel = cancel;
			cancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					// restore original content of textboxes and rerun input validation
					IDText.setText(ID);
					IDText.fireEvent(new KeyUpEvent() {});
					
					nameTxt.setText(name);
					nameTxt.fireEvent(new KeyUpEvent() {}); // validation

					cprTxt.setText(cpr);
					cprTxt.fireEvent(new KeyUpEvent() {});  // validation
					
					passwordTxt.setText(password);
					passwordTxt.fireEvent(new KeyUpEvent() {});
					
					adminTxt.setText(admin);
					adminTxt.fireEvent(new KeyUpEvent() {});
					
					operatoerTxt.setText(operatoer);
					operatoerTxt.fireEvent(new KeyUpEvent() {});
					
					farmaceutTxt.setText(farmaceut);
					farmaceutTxt.fireEvent(new KeyUpEvent() {});

					t.setText(eventRowIndex, 0, ID);
					t.setText(eventRowIndex, 1, name);
					t.setText(eventRowIndex, 2, cpr);
					t.setText(eventRowIndex, 3, password);
					t.setText(eventRowIndex, 4, admin);
					t.setText(eventRowIndex, 5, operatoer);
					t.setText(eventRowIndex, 6, farmaceut);

					// restore edit link
					t.setWidget(eventRowIndex, 7, edit);
					t.clearCell(eventRowIndex, 8);

					previousCancel = null;
				}

			});

			
			IDText.addKeyUpHandler(new KeyUpHandler(){

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (!FieldVerifier.isValidOprId(IDText.getText())) {
						IDText.setStyleName("gwt-TextBox-invalidEntry");
						IDValid = false;
					}
					else {
						IDText.removeStyleName("gwt-TextBox-invalidEntry");
						IDValid = true;
					}

					// enable/disable ok depending on form status 
					checkFormValid();;				
				}

			});
			

			nameTxt.addKeyUpHandler(new KeyUpHandler(){

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (!FieldVerifier.isValidName(nameTxt.getText())) {
						nameTxt.setStyleName("gwt-TextBox-invalidEntry");
						nameValid = false;
					}
					else {
						nameTxt.removeStyleName("gwt-TextBox-invalidEntry");
						nameValid = true;
					}

					// enable/disable ok depending on form status 
					checkFormValid();;				
				}

			});

			cprTxt.addKeyUpHandler(new KeyUpHandler(){

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (!FieldVerifier.isValidCpr(cprTxt.getText())) {
						cprTxt.setStyleName("gwt-TextBox-invalidEntry");
						cprValid = false;
					}
					else {
						cprTxt.removeStyleName("gwt-TextBox-invalidEntry");
						cprValid = true;
					}

					// enable/disable ok depending on form status 
					checkFormValid();;
				}

			});
			
			passwordTxt.addKeyUpHandler(new KeyUpHandler(){

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (!FieldVerifier.isVaildPassword(passwordTxt.getText())) {
						passwordTxt.setStyleName("gwt-TextBox-invalidEntry");
						passwordValid = false;
					}
					else {
						passwordTxt.removeStyleName("gwt-TextBox-invalidEntry");
						passwordValid = true;
					}

					// enable/disable ok depending on form status 
					checkFormValid();
				}

			});
			
			adminTxt.addKeyUpHandler(new KeyUpHandler(){

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (!FieldVerifier.isValidRolle(adminTxt.getText())) {
						adminTxt.setStyleName("gwt-TextBox-invalidEntry");
						adminValid = false;
					}
					else {
						adminTxt.removeStyleName("gwt-TextBox-invalidEntry");
						adminValid = true;
					}

					// enable/disable ok depending on form status 
					checkFormValid();
				}

			});
			
			operatoerTxt.addKeyUpHandler(new KeyUpHandler(){

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (!FieldVerifier.isValidRolle(operatoerTxt.getText())) {
						operatoerTxt.setStyleName("gwt-TextBox-invalidEntry");
						operatoerValid = false;
					}
					else {
						operatoerTxt.removeStyleName("gwt-TextBox-invalidEntry");
						operatoerValid = true;
					}

					// enable/disable ok depending on form status 
					checkFormValid();
				}

			});
			
			farmaceutTxt.addKeyUpHandler(new KeyUpHandler(){

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (!FieldVerifier.isValidRolle(operatoerTxt.getText())) {
						farmaceutTxt.setStyleName("gwt-TextBox-invalidEntry");
						farmaceutValid = false;
					}
					else {
						farmaceutTxt.removeStyleName("gwt-TextBox-invalidEntry");
						farmaceutValid = true;
					}

					// enable/disable ok depending on form status 
					checkFormValid();
				}
			});
			

			// showing ok and cancel widgets
			t.setWidget(eventRowIndex, 7 , ok);
			t.setWidget(eventRowIndex, 8 , cancel);		
		}
	}
	
	private void checkFormValid(){
		if (IDValid&&nameValid&&cprValid&&passwordValid&&adminValid&&operatoerValid&&farmaceutValid)
			t.setWidget(eventRowIndex, 7, ok);
		else
			t.setText(eventRowIndex, 7, "ok");
	}
	}

