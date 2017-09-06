package com.benputao.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import com.benputao.listen.Client;
import com.benputao.model.User;

public class RegistDialogPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField txtf_username;
	private JLabel lbl_wrongum;
	private JPasswordField pswf_password;
	private JPasswordField pswf_repswd;
	private JLabel lbl_wrongrp;
	private JTextField txtf_nickname;
	
	public RegistDialogPanel(JDialog root,Client client) {
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);
		root.getContentPane().add(this, BorderLayout.CENTER);
		
		JLabel label = new JLabel("用  户  名:");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label.setBounds(41, 29, 76, 24);
		this.add(label);
		
		txtf_username = new JTextField();
		txtf_username.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				lbl_wrongum.setText("");
			}
		});
		txtf_username.setBounds(127, 29, 127, 25);
		this.add(txtf_username);
		txtf_username.setColumns(10);
		
		JLabel label_1 = new JLabel("密       码：");
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_1.setBounds(41, 82, 76, 24);
		this.add(label_1);
		
		pswf_password = new JPasswordField();
		pswf_password.setBounds(127, 83, 127, 25);
		this.add(pswf_password);
		
		JLabel label_2 = new JLabel("确认密码：");
		label_2.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_2.setBounds(41, 135, 76, 24);
		this.add(label_2);
		
		pswf_repswd = new JPasswordField();
		pswf_repswd.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent arg0) {
				lbl_wrongrp.setText("");
			}
		});
		pswf_repswd.setBounds(127, 137, 127, 25);
		this.add(pswf_repswd);
		
		JLabel lblNewLabel = new JLabel("昵       称：");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lblNewLabel.setBounds(41, 188, 76, 24);
		this.add(lblNewLabel);
		
		txtf_nickname = new JTextField();
		txtf_nickname.setBounds(127, 191, 127, 25);
		this.add(txtf_nickname);
		txtf_nickname.setColumns(10);
		
		lbl_wrongrp = new JLabel("");
		lbl_wrongrp.setForeground(Color.RED);
		lbl_wrongrp.setBounds(127, 166, 127, 15);
		this.add(lbl_wrongrp);
		
		lbl_wrongum = new JLabel("");
		lbl_wrongum.setForeground(Color.RED);
		lbl_wrongum.setBounds(127, 58, 127, 15);
		this.add(lbl_wrongum);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			root.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("马上注册");
				okButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
				okButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
				okButton.setForeground(Color.white);
				okButton.setToolTipText("注册Biu biu账号。");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String username=txtf_username.getText();
						CharSequence password=new String(pswf_password.getPassword());
						CharSequence repswd=new String(pswf_repswd.getPassword());
						String nickname=txtf_nickname.getText();
						if(!username.equals("")&&password.equals(repswd)){
							if(nickname.equals("")){
								nickname="biu_"+username;
							}
							User user=new User(username,password,nickname);
							boolean res=client.regist(user);
							System.out.println(res?"success":"failure");
							root.setVisible(false);
						}else if (username.equals("")) {
							lbl_wrongum.setText("用户名不可以为空！");
						}else if(!password.equals(repswd)){
							lbl_wrongrp.setText("两次密码输入不一致！");
						}
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("还是算了");
				cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
				cancelButton.setToolTipText("别啊，我这么好玩你怎么可以错过？");
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

}
