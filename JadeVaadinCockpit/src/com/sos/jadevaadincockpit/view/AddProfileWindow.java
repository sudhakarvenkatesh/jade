package com.sos.jadevaadincockpit.view;

import com.sos.jadevaadincockpit.globals.Globals;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AddProfileWindow extends Window {
	private static final long serialVersionUID = -5835180354807542093L;
	
	VerticalLayout vLayout;
	HorizontalLayout hLayout;
	
	private TextField nameTextField;
	private CheckBox isFragmentCheckBox;
	private Button okButton;
	private Button cancelButton;
	
	public AddProfileWindow() {
		
		setCaption("New Profile");
		setModal(true);
		
		vLayout = new VerticalLayout();
		hLayout = new HorizontalLayout();
		
		nameTextField = new TextField("Enter the new profile's name");
		isFragmentCheckBox = new CheckBox("Is Fragment");
		
		okButton = new Button("OK");
		cancelButton = new Button("Cancel");
		
		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1601728589946232816L;

			@Override
			public void buttonClick(ClickEvent event) {

				String profileName = nameTextField.getValue();
				Boolean isFragment = isFragmentCheckBox.getValue();

				if (Globals.getJadeDataProvider().addProfile(profileName, isFragment)) {
					close();
				} else {
					Notification.show("The entered name is not valid!", Notification.Type.ERROR_MESSAGE);
				}
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 3084812570022080915L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		
		hLayout.addComponents(okButton, cancelButton);
		vLayout.addComponents(nameTextField, isFragmentCheckBox, hLayout);
		
		setContent(vLayout);
		vLayout.setSizeFull();
		
		setHeight("50%");
		setWidth("30%");
		center();
	}
}
