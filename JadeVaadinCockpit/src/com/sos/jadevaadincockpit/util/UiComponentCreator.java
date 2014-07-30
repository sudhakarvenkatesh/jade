package com.sos.jadevaadincockpit.util;

import java.io.File;
import java.util.HashMap;

import com.sos.JSHelper.Options.IValueChangedListener;
import com.sos.JSHelper.Options.SOSOptionElement;
import com.sos.JSHelper.Options.SOSValidationError;
import com.sos.jadevaadincockpit.globals.Globals;
import com.sos.jadevaadincockpit.i18n.JadeCockpitMsg;
import com.sos.jadevaadincockpit.view.components.JadeComboBox;
import com.sos.jadevaadincockpit.viewmodel.ProfileContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

/**
 * 
 * @author JS
 *
 */
public class UiComponentCreator {
	
	private HashMap<String, SOSOptionElement> options;
	
	@SuppressWarnings("unchecked")
	public UiComponentCreator(Item profile) {
		options = (HashMap<String, SOSOptionElement>) profile.getItemProperty(ProfileContainer.PROPERTY.OPTIONS).getValue();
	}
	
	/**
	 * Creates a component according to the optionElement's component type, connects it to the optionElement and sets the control's caption and description.
	 * @param optionElement
	 * @return The component
	 */
	public AbstractField<?> getComponentWithCaption(final SOSOptionElement optionElement) {

		AbstractField<?> comp = getComponent(optionElement);
		
		String shortKey = optionElement.getShortKey();
		
//		VaadinMsg msg = Globals.vaadinMessages.getNewVaadinMsg("jade_l_" + shortKey);
//		comp.setCaption(msg.label());
//		comp.setDescription(msg.tooltip() + " - " + shortKey);
		
//		JadeCockpitMessages messages = Globals.getMessages();
//		comp.setCaption(messages.getLabel(shortKey));
//		comp.setDescription(messages.getTooltip(shortKey));
		
		JadeCockpitMsg msg = new JadeCockpitMsg("jade_l_" + shortKey);
		comp.setCaption(msg.label());
		comp.setDescription(msg.tooltip() + " - " + shortKey);
		
		return comp;
	}

	/**
	 * Creates a component according to the optionElement's component type and connects it to the optionElement.
	 * @param optionElement
	 * @return The component
	 */
	public AbstractField<?> getComponent(SOSOptionElement optionElement) {
		
		AbstractField<?> comp = null;
		
		if (optionElement != null) {
			
			comp = getComponentByType(optionElement);
			
			comp.setData(optionElement);
			
			comp.setWidth("80%");
			comp.setImmediate(true);
			
			addToOptionsMap(optionElement);

//			new UiComponentHelper(comp, optionElement);
		}
		
		return comp;
	}
	
	/**
	 * Adds the optionElement to the map of all optionElements
	 * @param optionElement
	 */
	private void addToOptionsMap(SOSOptionElement optionElement) {
		
		String shortKey = optionElement.getShortKey();
		
		if (!options.containsKey(shortKey)) {
			options.put(shortKey, optionElement);
		}
	}
	
	/**
	 * Checks the component type and creates an according component.
	 * @param optionElement
	 * @return The component according to the optionElement's component type.
	 */
	private AbstractField<?> getComponentByType(SOSOptionElement optionElement) {
		
		AbstractField<?> comp = null;
		String componentType = optionElement.getControlType();
		
		if (componentType.equalsIgnoreCase("text")) {
			comp = getTextField(optionElement);

		} else if (componentType.equalsIgnoreCase("checkbox")) {
			comp = getCheckBox(optionElement);
			
		} else if (componentType.equalsIgnoreCase("combo")) {
			comp = getComboBox(optionElement);
			
		} else { // component types which are not covered by the previous checks
			
			TextField textField = getTextField(optionElement);
			FileResource resource = new FileResource(new File(Globals.getBasePath() + "/WEB-INF/icons/Delete.gif"));
			textField.setIcon(resource);
			comp = textField;
		}
		
		return comp;
	}
	

	/**
	 * Creates a ComboBox with the value list from the optionElement and connects it to the optionElement.
	 * @param optionElement
	 * @return The ComboBox
	 */
	@SuppressWarnings("unchecked")
	private ComboBox getComboBox(final SOSOptionElement optionElement) {
		
		final JadeComboBox comboBox = new JadeComboBox();
		
		for (String value : optionElement.getValueList()) {
			Item item = comboBox.addItem(value);
			item.getItemProperty(JadeComboBox.PROPERTY.VALUE).setValue(value);
		}
		
		comboBox.setValue(optionElement.Value());
		
		comboBox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4494994397731811284L;

			@Override
			public void valueChange(ValueChangeEvent event) {

				Object currentItemId = comboBox.getValue();

				String value = null;
				if (currentItemId != null && !currentItemId.equals("null") && !currentItemId.equals("")) {
					
					try {
						value = (String) comboBox.getItem(currentItemId).getItemProperty(JadeComboBox.PROPERTY.VALUE).getValue();
						
					} catch (Exception e) {
						Notification.show("ERROR! currentItemId is: '" + currentItemId.toString() + "'", Notification.Type.ERROR_MESSAGE);
						e.printStackTrace();
					}
					
					optionElement.Value(value);
					
				} else {
					optionElement.setNull(); // TODO was passiert dann?
				}
				
				changeStyleName(comboBox, optionElement.isDefault());
			}
		});
		
		optionElement.addValueChangedListener(new IValueChangedListener() {
			@Override
			public void ValueHasChanged(String pstrNewValue) {
				comboBox.setValue(pstrNewValue);
			}

			@Override
			public void ValidationError(
					SOSValidationError pobjVE) {
				// TODO Auto-generated method stub
			}
		});
		
		changeStyleName(comboBox, optionElement.isDefault());
		
		return comboBox;
	}

	/**
	 * Creates a CheckBox and connects it to the optionElement.
	 * @param optionElement
	 * @return The CheckBox
	 */
	private CheckBox getCheckBox(final SOSOptionElement optionElement) {
		
		final CheckBox checkBox = new CheckBox();
		
		checkBox.setValue(optionElement.String2Bool(optionElement.Value()));
		
		checkBox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4494994397731811284L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Object value = checkBox.getValue();
				
				if (value != null) {
					optionElement.Value(optionElement.boolean2String((Boolean) value));
					
				} else {
					optionElement.setNull();
				}
				
				changeStyleName(checkBox, optionElement.isDefault());
			}
		});
		
		optionElement.addValueChangedListener(new IValueChangedListener() {
			@Override
			public void ValueHasChanged(String pstrNewValue) {
				
				checkBox.setValue(optionElement.String2Bool(pstrNewValue));
			}

			@Override
			public void ValidationError(
					SOSValidationError pobjVE) {
				// TODO Auto-generated method stub
			}
		});
		
		changeStyleName(checkBox, optionElement.isDefault());
		
		return checkBox;
	}

	/**
	 * Creates a TextField and connects it to the optionElement.
	 * @param optionElement
	 * @return The TextField
	 */
	private TextField getTextField(final SOSOptionElement optionElement) {
		
		final TextField textField = new TextField();
		
		textField.setValue(optionElement.Value());
		
		textField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4494994397731811284L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				String value = (String) textField.getValue();
				
				if (value != null) {
					optionElement.Value(value);
					
				} else {
					optionElement.setNull();
				}
				
				changeStyleName(textField, optionElement.isDefault());
			}
		});
		
		optionElement.addValueChangedListener(new IValueChangedListener() {
			@Override
			public void ValueHasChanged(String pstrNewValue) {
				
				if (!textField.isReadOnly()) {
					textField.setValue(pstrNewValue);
				}
			}

			@Override
			public void ValidationError(
					SOSValidationError pobjVE) {
				// TODO Auto-generated method stub
			}
		});
		
		changeStyleName(textField, optionElement.isDefault());
		
		return textField;
	}
	
	/**
	 * 
	 * @param component
	 * @param isDefault
	 */
	private void changeStyleName(AbstractField<?> component, boolean isDefault) {
		if (!isDefault) {
			component.addStyleName("default-option-value");
		} else {
			component.removeStyleName("default-option-value");
		}
	}
	
}

//class A extends CustomComponent {
//	GridLayout baseLayout = new GridLayout(2, 1);
//	
//	public A(Label label, AbstractComponent component) {
//		setCompositionRoot(baseLayout);
//		baseLayout.addComponent(label);
//		label.setSizeUndefined();
//		baseLayout.addComponent(component);
//		component.setSizeUndefined();
//		baseLayout.setWidth("100%");
//		baseLayout.setColumnExpandRatio(0, 5);
//		baseLayout.setColumnExpandRatio(1, 2);
//	}
//}
