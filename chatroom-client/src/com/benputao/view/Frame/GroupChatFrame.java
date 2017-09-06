package com.benputao.view.Frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.benputao.dtos.Message;
import com.benputao.listen.Client;
import com.benputao.model.Chatroom;
import com.benputao.utils.ChatMessage;
import com.benputao.utils.ChatMessageManger;

public class GroupChatFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Client client;
	private Chatroom room;
	
	private JPanel contentPane;
	private JTextField txtf_msg;
	private JButton btn_avatar;
	private JLabel lbl_name;
	private JLabel lbl_id;
	private JTextPane txtp_chatpanel;
	private JButton btn_send;
	private JLabel lbl_groupuser;

	public GroupChatFrame(Client client,Chatroom room) {
		this.room=room;
		this.client=client;
		
		setTitle("ÈºÁÄ-"+this.room.getName());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 565, 410);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(0, 0, 106, 330);
		contentPane.add(panel);
		
		btn_avatar = new JButton("");
		btn_avatar.setIcon(new ImageIcon(GroupChatFrame.class.getResource("/resources/avatar.png")));
		btn_avatar.setBorder(new LineBorder(Color.LIGHT_GRAY));
		btn_avatar.setBounds(10, 10, 81, 81);
		panel.add(btn_avatar);
		
		lbl_name = new JLabel(this.room.getName());
		lbl_name.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 17));
		lbl_name.setBounds(10, 126, 81, 26);
		panel.add(lbl_name);
		
		lbl_id = new JLabel(this.room.getId());
		lbl_id.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		lbl_id.setBounds(10, 187, 81, 26);
		panel.add(lbl_id);
		
		JLabel lbl_1 = new JLabel("ÈºÃû³Æ");
		lbl_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		lbl_1.setBounds(10, 113, 54, 15);
		panel.add(lbl_1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 101, 106, 2);
		panel.add(separator);
		
		JLabel label = new JLabel("ÈººÅÂë");
		label.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		label.setBounds(10, 172, 54, 15);
		panel.add(label);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 162, 106, 2);
		panel.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 223, 106, 2);
		panel.add(separator_2);
		
		JLabel label_1 = new JLabel("ÈºÖ÷");
		label_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		label_1.setBounds(10, 235, 54, 15);
		panel.add(label_1);
		
		lbl_groupuser = new JLabel(this.room.getGroupuser());
		lbl_groupuser.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		lbl_groupuser.setBounds(10, 252, 81, 26);
		panel.add(lbl_groupuser);
		
		JScrollPane scrollPane = new JScrollPane((Component) null);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		scrollPane.setBounds(105, 0, 406, 269);
		contentPane.add(scrollPane);
		
		txtp_chatpanel = new JTextPane();
		txtp_chatpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		txtp_chatpanel.setContentType("text/html");
		txtp_chatpanel.setEnabled(false);
		txtp_chatpanel.setEditable(false);
		txtp_chatpanel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		
		scrollPane.setViewportView(txtp_chatpanel);
		
		txtf_msg = new JTextField();
		txtf_msg.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		txtf_msg.setColumns(10);
		txtf_msg.setBounds(116, 279, 292, 41);
		contentPane.add(txtf_msg);
		
		btn_send = new JButton("·¢ËÍ");
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String msg=txtf_msg.getText();
				sendMsg(msg);
				txtf_msg.setText("");
			}
		});
		btn_send.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		btn_send.setBounds(418, 279, 81, 41);
		contentPane.add(btn_send);
		paintHistoryMessage();
	}
	
	private void paintHistoryMessage() {
		List<ChatMessage>messages=ChatMessageManger.getMsg(room.getId(), 3,Message.GROUP);
		for (ChatMessage chatMessage : messages) {
			paintMsg(chatMessage);
		}
	}

	public void recieve(ChatMessage message){
		paintMsg(message);
	}
	
	private void sendMsg(String msg){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		ChatMessage message=new ChatMessage("ÎÒ", format.format(new Date()), msg);
		//Êä³ö·¢ËÍÏûÏ¢
		paintMsg(message);
		//·¢ËÍÏûÏ¢µ½·þÎñÆ÷
		Message mm=new Message(Message.GROUP,room.getId(), new Date(), msg);
		client.sendChatMessage(mm);
		//Ìí¼Óµ½ÏûÏ¢¼ÇÂ¼ÖÐ
		ChatMessageManger.send(room.getId(), message,Message.GROUP);
	}
	
	private void paintMsg(ChatMessage message){
		JPanel msgbox=new JPanel();
		msgbox.setLayout(new BorderLayout(0, 0));
		
		JLabel lbl_msgstat = new JLabel(message.getSendUser()+"    "+message.getDatetime());
		lbl_msgstat.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		JTextArea lbl_msg=new JTextArea(message.getMessage());
		lbl_msg.setEnabled(false);
		lbl_msg.setEditable(false);
		lbl_msg.setLineWrap(true);
		lbl_msg.setWrapStyleWord(true);
		lbl_msg.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		
		if(message.getSendUser().equals("ÎÒ")){
			lbl_msgstat.setForeground(new Color(0, 128, 0));
			lbl_msg.setBackground(new Color(0x32cd32));
		} else {
			lbl_msgstat.setForeground(new Color(0, 0, 255));
			lbl_msg.setBackground(new Color(0x48decc));
		}
		lbl_msg.setBorder(UIManager.getBorder("Button.border"));
		msgbox.add(lbl_msgstat, BorderLayout.NORTH);
		msgbox.add(lbl_msg, BorderLayout.CENTER);
		msgbox.setBackground(new Color(0xffffcc));
		msgbox.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		Document docs = txtp_chatpanel.getDocument();//»ñµÃÎÄ±¾¶ÔÏó
		txtp_chatpanel.setCaretPosition(docs.getLength());
		txtp_chatpanel.insertComponent(msgbox);
	    try {
	        docs.insertString(docs.getLength(), "\n", null);
	        txtp_chatpanel.setDocument(docs);
	    } catch (BadLocationException e) {
	        e.printStackTrace();
	    }
//		JLabel blank=new JLabel("      ");
//		txtp_chatpanel.insertComponent(blank);
	}
}
