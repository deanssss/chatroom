package com.benputao.view;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class EnterDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPanel;
	/**
	 * Create the dialog.
	 */
	public EnterDialog(String title) {
		setTitle(title);
		setBounds(100, 100, 346, 372);
		getContentPane().setLayout(new BorderLayout());
	}
	
	public void setContentPanel(JPanel contentPanel){
		this.contentPanel=contentPanel;
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
	}
}
