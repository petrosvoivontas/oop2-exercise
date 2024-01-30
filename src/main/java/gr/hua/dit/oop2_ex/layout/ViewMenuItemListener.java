package gr.hua.dit.oop2_ex.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ViewMenuItemListener implements ItemListener {

	private final Menu viewMenu;
	private final Runnable onFilterSelectedAction;

	public ViewMenuItemListener(Menu viewMenu, Runnable onFilterSelectedAction) {
		this.viewMenu = viewMenu;
		this.onFilterSelectedAction = onFilterSelectedAction;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		CheckboxMenuItem selectedItem = (CheckboxMenuItem) e.getItemSelectable();
		for (int i = 0; i < viewMenu.getItemCount(); i++) {
			MenuItem menuItem = viewMenu.getItem(i);
			if (menuItem instanceof CheckboxMenuItem checkboxMenuItem && checkboxMenuItem != selectedItem) {
				checkboxMenuItem.setState(false);
			}
		}
		if (e.getStateChange() == ItemEvent.SELECTED) {
			SwingUtilities.invokeLater(onFilterSelectedAction);
		}
	}
}
