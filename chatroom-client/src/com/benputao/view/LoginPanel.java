package com.benputao.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import com.benputao.listen.Client;

public class LoginPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField textField_username;
	private JPasswordField passField_password;
	private Client client;
	/**
	 * Create the panel.
	 */
	public LoginPanel(Client client) {
		this.client=client;
		setBounds(0, 0, 317, 487);
		setBackground(UIManager.getColor("Button.light"));
		setLayout(null);
		
		JPanel panel_loginpad = new JPanel();
		panel_loginpad.setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
		panel_loginpad.setBounds(33, 191, 256, 212);
		panel_loginpad.setLayout(null);
		add(panel_loginpad);
		
		JLabel label = new JLabel("ÓÃ»§Ãû£º");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		label.setBounds(10, 32, 68, 32);
		panel_loginpad.add(label);
		
		JLabel label_1 = new JLabel("ÃÜ    Âë£º");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		label_1.setBounds(10, 74, 68, 32);
		panel_loginpad.add(label_1);
		
		textField_username = new JTextField();
		textField_username.setBounds(80, 37, 156, 25);
		panel_loginpad.add(textField_username);
		textField_username.setColumns(10);
		
		passField_password = new JPasswordField();
		passField_password.setBounds(80, 79, 156, 25);
		panel_loginpad.add(passField_password);
		
		JButton btn_login = new JButton("µÇ     Â¼");
		btn_login.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
		btn_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String username=textField_username.getText();
				char[] passchar=passField_password.getPassword();
				CharSequence password=new String(passchar);
				if(LoginPanel.this.client!=null){
					LoginPanel.this.client.login(username, password);
				}
			}
		});
		btn_login.setForeground(Color.white);
		btn_login.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 18));
		btn_login.setBounds(59, 137, 150, 31);
		panel_loginpad.add(btn_login);
		
		JLabel lbl_regist = new JLabel("ÂíÉÏ×¢²á");
		EnterDialog dialog=new EnterDialog("×¢²á");
		JPanel registPanel=new RegistDialogPanel(dialog, client);
		dialog.setContentPanel(registPanel);
		
		dialog.setLocation(getLocation().x+115, getLocation().y+300);
		
		lbl_regist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lbl_regist.setForeground(new Color(255, 102, 255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lbl_regist.setForeground(new Color(0, 102, 255));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(dialog!=null){
					dialog.setVisible(true);
				}
			}
		});
		lbl_regist.setForeground(new Color(0, 102, 255));
		lbl_regist.setHorizontalTextPosition(SwingConstants.CENTER);
		lbl_regist.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_regist.setFont(new Font("»ªÎÄÏ¸ºÚ", Font.PLAIN, 14));
		lbl_regist.setBounds(59, 178, 68, 24);
		lbl_regist.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panel_loginpad.add(lbl_regist);
		
		JLabel lbl_forgotpass = new JLabel("¸ãÍüÃÜÂë");
		lbl_forgotpass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lbl_forgotpass.setForeground(new Color(255, 102, 255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lbl_forgotpass.setForeground(new Color(0, 102, 255));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		lbl_forgotpass.setForeground(new Color(0, 102, 255));
		lbl_forgotpass.setHorizontalTextPosition(SwingConstants.CENTER);
		lbl_forgotpass.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_forgotpass.setFont(new Font("»ªÎÄÏ¸ºÚ", Font.PLAIN, 14));
		lbl_forgotpass.setBounds(137, 178, 72, 24);
		lbl_forgotpass.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panel_loginpad.add(lbl_forgotpass);
		
		JButton btn_avatar = new JButton("");
		btn_avatar.setToolTipText("<html><div bgcolor='#91e1d6'>»¶Ó­Ê¹ÓÃBiu Biu£¡</div></html>");
		btn_avatar.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_avatar.setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
		ImageIcon icon=new ImageIcon(LoginPanel.class.getResource("/resources/icon.gif"));
		Image tp=icon.getImage().getScaledInstance(83, 83, Image.SCALE_DEFAULT);
		icon=new ImageIcon(tp);
		btn_avatar.setIcon(icon);
		btn_avatar.setBounds(115, 86, 83, 83);
		add(btn_avatar);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.LIGHT_GRAY);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(156, 168, 35, 27);
		add(separator);
	}

}
