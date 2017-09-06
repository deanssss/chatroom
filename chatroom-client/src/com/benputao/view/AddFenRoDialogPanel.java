package com.benputao.view;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.benputao.listen.Client;
import com.benputao.model.Chatroom;
import com.benputao.model.User;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import java.awt.Color;
import java.awt.FlowLayout;

public class AddFenRoDialogPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final int ROOM=1;
	public static final int FRIEND=2;
	
	private JTextField txtf_username;
	private JTextArea  txta_msg;

	public AddFenRoDialogPanel(JDialog root,Client client,int kind) {
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);
		this.setBounds(100, 100, 346, 372);
		root.getContentPane().add(this, BorderLayout.CENTER);
		
		txtf_username = new JTextField();
		txtf_username.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		txtf_username.setBounds(95, 70, 220, 32);
		add(txtf_username);
		txtf_username.setColumns(10);
		
		JLabel lblNewLabel = new JLabel(kind==FRIEND?"用  户  名：":"房  间  号：");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 69, 75, 32);
		add(lblNewLabel);
		
		JLabel label = new JLabel("验证信息：");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label.setBounds(10, 111, 75, 32);
		add(label);
		
		txta_msg = new JTextArea();
		txta_msg.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		txta_msg.setBorder(new LineBorder(Color.LIGHT_GRAY));
		txta_msg.setBounds(95, 123, 220, 95);
		add(txta_msg);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		root.getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			JButton okButton = new JButton("发送请求");
			okButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			okButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
			okButton.setForeground(Color.white);
			okButton.setToolTipText("你会认识谁呢？");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//向服务器发请求
					if(kind==AddFenRoDialogPanel.FRIEND){
						User user=new User();
						String username=txtf_username.getText();
						String msg=txta_msg.getText();
						user.setUsername(username);
						user.setNickname(msg);
						client.addFriend(user);
					}else {
						String id=txtf_username.getText();
						String msg=txta_msg.getText();
						Chatroom room=new Chatroom(id,msg,null);
						client.joinChatroom(room);
					}
					root.dispose();
				}
			});
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		}
		{
			JButton cancelButton = new JButton("还是算了");
			cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			cancelButton.setToolTipText("你可能错过了某dalao哦，少年");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					root.setVisible(false);
				}
			});
			buttonPane.add(cancelButton);
		}
	}
}
