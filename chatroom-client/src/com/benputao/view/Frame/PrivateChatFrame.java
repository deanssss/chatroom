package com.benputao.view.Frame;

import java.awt.BorderLayout;
import java.awt.Color;
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
import com.benputao.model.User;
import com.benputao.utils.ChatMessage;
import com.benputao.utils.ChatMessageManger;

public class PrivateChatFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField txtf_msg;
	private JPanel contentPane;
	private JButton btn_avatar;
	private JLabel lbl_nickname;
	private JLabel lbl_username;
	private JTextPane txtp_chatpanel;
	private JButton btn_send;
	
	private User user;
    private Client client;

	public PrivateChatFrame(Client client,User user) {
		this.client=client;
		this.user=user;
		setTitle("正在与"+user.getNickname()+"聊天");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 565, 410);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(0, 0, 106, 330);
		contentPane.add(panel);
		panel.setLayout(null);
		
		btn_avatar = new JButton("");
		btn_avatar.setBorder(new LineBorder(Color.LIGHT_GRAY));
		btn_avatar.setIcon(new ImageIcon(PrivateChatFrame.class.getResource("/resources/avatar.png")));
		btn_avatar.setBounds(10, 10, 81, 81);
		panel.add(btn_avatar);
		
		lbl_nickname = new JLabel(user.getNickname());
		lbl_nickname.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		lbl_nickname.setBounds(10, 101, 81, 26);
		panel.add(lbl_nickname);
		
		lbl_username = new JLabel(user.getUsername());
		lbl_username.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		lbl_username.setBounds(10, 137, 81, 26);
		panel.add(lbl_username);
		
		txtp_chatpanel = new JTextPane();
		txtp_chatpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		txtp_chatpanel.setContentType("text/html");
		txtp_chatpanel.setEnabled(false);
		txtp_chatpanel.setEditable(false);
		txtp_chatpanel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		
		JScrollPane scrollPane = new JScrollPane(txtp_chatpanel);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		scrollPane.setBounds(105, 0, 406, 269);
		contentPane.add(scrollPane);
		
		txtf_msg = new JTextField();
		txtf_msg.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		txtf_msg.setBounds(116, 279, 292, 41);
		contentPane.add(txtf_msg);
		txtf_msg.setColumns(10);
		
		btn_send = new JButton("发送");
		btn_send.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String msg=txtf_msg.getText();
				sendMsg(msg);
				txtf_msg.setText("");
			}
		});
		btn_send.setBounds(418, 279, 81, 41);
		contentPane.add(btn_send);
		paintHistoryMessage();
	}
	
	private void paintHistoryMessage() {
		List<ChatMessage>messages=ChatMessageManger.getMsg(user.getUsername(), 3,Message.PRIVATE);
		for (ChatMessage chatMessage : messages) {
			paintMsg(chatMessage);
		}
	}

	public void recieve(ChatMessage message){
		message.setSendUser(user.getNickname());
		paintMsg(message);
	}
	
	private void sendMsg(String msg){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		ChatMessage message=new ChatMessage("我", format.format(new Date()), msg);
		//输出发送消息
		paintMsg(message);
		//发送消息到服务器
		Message mm=new Message(Message.PRIVATE,user.getUsername(), new Date(), msg);
		client.sendChatMessage(mm);
		//添加到消息记录中
		ChatMessageManger.send(user.getUsername(), message,Message.PRIVATE);
	}
	
	private void paintMsg(ChatMessage message){
		JPanel msgbox=new JPanel();
		msgbox.setLayout(new BorderLayout(0, 0));
		
		JLabel lbl_msgstat = new JLabel(message.getSendUser()+"    "+message.getDatetime());
		lbl_msgstat.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		JTextArea lbl_msg=new JTextArea(message.getMessage());
		lbl_msg.setEnabled(false);
		lbl_msg.setEditable(false);
		lbl_msg.setLineWrap(true);
		lbl_msg.setWrapStyleWord(true);
		lbl_msg.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		
		if(message.getSendUser().equals("我")){
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
		
		Document docs = txtp_chatpanel.getDocument();//获得文本对象
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
