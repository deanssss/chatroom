package com.benputao.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MyListItem extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JButton btn_avatar;
	private JLabel lbl_nickname;
	private JLabel lbl_discribe;
	private JLabel lbl_time;
	
	private String avatar_path="/resources/avatar.png";
	private String nickname="Nickname";
	private String discribe="一条有个性的个性签名！";
	private String time="2000-01-01 00:00:00";
	public MyListItem(String path,String name,String disc,String t) {
		avatar_path=path!=null?path:avatar_path;
		nickname=name!=null?name:nickname;
		discribe=disc!=null?disc:discribe;
		time=t;
		
		setLayout(new BorderLayout(0, 0));
		btn_avatar = new JButton("");
		btn_avatar.setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
		ImageIcon icon=new ImageIcon(MyListItem.class.getResource(avatar_path));
		Image tp=icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		icon=new ImageIcon(tp);
		btn_avatar.setIcon(icon);
		if (disc.equals("OFFLINE")) {
			btn_avatar.setEnabled(false);
		}
		add(btn_avatar, BorderLayout.WEST);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 2, 10, 2));
		panel.setLayout(new BorderLayout(0, 0));
		panel.setBackground(new Color(0, 0, 0, 0));
		add(panel, BorderLayout.CENTER);
		
		lbl_nickname = new JLabel(nickname);
		panel.add(lbl_nickname, BorderLayout.CENTER);
		
		lbl_discribe = new JLabel(discribe);
		panel.add(lbl_discribe, BorderLayout.SOUTH);
		
		lbl_time = new JLabel(time==null?"":time);
		panel.add(lbl_time, BorderLayout.EAST);
	}
}
