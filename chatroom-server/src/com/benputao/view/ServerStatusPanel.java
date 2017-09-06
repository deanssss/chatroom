package com.benputao.view;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import com.benputao.dtos.Notification;
import com.benputao.listen.Server;

import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class ServerStatusPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private int user_count=0;
	private int online_user_count=0;
	private int chatroom_count=0;
	private int server_port=0;
	private boolean server_status=false;
	private List<String>online_user_list=new ArrayList<>();
	
	private JList<Object>list_onlineuser;
	private JLabel label_usercount;
	private JLabel label_onlineusercount;
	private JLabel label_chatroomcount;
	private JLabel label_serverstatus;
	private JLabel label_serverport;
	private JTextArea textArea_notification;
	private JTextField textField_notifaction;
	
	private Server server;
	private ServerFrame root;
	public ServerStatusPanel(Server server,ServerFrame root) {
		this.server=server;
		this.root=root;
		setLayout(null);
		initialize();
	}
	public ServerStatusPanel(int user_count, int online_user_count, 
							 int chatroom_count, int server_port,
							 boolean server_status) {
		this.user_count = user_count;
		this.online_user_count = online_user_count;
		this.chatroom_count = chatroom_count;
		this.server_port = server_port;
		this.server_status = server_status;
		setLayout(null);
		initialize();
	}
	
	/**
	 * �����ʼ��
	 */
	private void initialize() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(10, 10, 534, 284);
		add(panel);
		
		JPanel panel_onlineuser = new JPanel();
		panel_onlineuser.setBackground(SystemColor.controlHighlight);
		panel_onlineuser.setLayout(null);
		Border border=BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		border=BorderFactory.createTitledBorder(border, "�����û��б�");
		panel_onlineuser.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_onlineuser.setBounds(0, 0, 110, 284);
		panel.add(panel_onlineuser);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		scrollPane.setBounds(0, 20, 110, 264);
		panel_onlineuser.add(scrollPane);
		
		list_onlineuser = new JList<>();
		list_onlineuser.setCellRenderer(new ColorfulCellRenderer());
		scrollPane.setViewportView(list_onlineuser);
		
		JLabel lblNewLabel = new JLabel("�����û�");
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 12));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 0, 90, 22);
		panel_onlineuser.add(lblNewLabel);
		
		JPanel panel_serverstatus = new JPanel();
		panel_serverstatus.setBackground(SystemColor.menu);
		panel_serverstatus.setLayout(null);
		panel_serverstatus.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_serverstatus.setBounds(424, 0, 110, 284);
		panel.add(panel_serverstatus);
		
		JLabel label = new JLabel("ע���û�����");
		label.setForeground(Color.BLACK);
		label.setFont(new Font("��Բ", Font.PLAIN, 14));
		label.setBounds(10, 10, 90, 21);
		panel_serverstatus.add(label);
		
		label_usercount = new JLabel(""+user_count);
		label_usercount.setHorizontalAlignment(SwingConstants.CENTER);
		label_usercount.setFont(new Font("��Բ", Font.PLAIN, 15));
		label_usercount.setBounds(10, 36, 90, 21);
		panel_serverstatus.add(label_usercount);
		
		JLabel label_2 = new JLabel("�����û�����");
		label_2.setForeground(Color.BLACK);
		label_2.setFont(new Font("��Բ", Font.PLAIN, 14));
		label_2.setBounds(10, 67, 90, 21);
		panel_serverstatus.add(label_2);
		
		label_onlineusercount = new JLabel(""+online_user_count);
		label_onlineusercount.setHorizontalAlignment(SwingConstants.CENTER);
		label_onlineusercount.setFont(new Font("��Բ", Font.PLAIN, 15));
		label_onlineusercount.setBounds(10, 92, 90, 21);
		panel_serverstatus.add(label_onlineusercount);
		
		JLabel label_4 = new JLabel("�� �� ������");
		label_4.setForeground(Color.BLACK);
		label_4.setFont(new Font("��Բ", Font.PLAIN, 14));
		label_4.setBounds(10, 121, 90, 21);
		panel_serverstatus.add(label_4);
		
		label_chatroomcount = new JLabel(""+chatroom_count);
		label_chatroomcount.setHorizontalAlignment(SwingConstants.CENTER);
		label_chatroomcount.setFont(new Font("��Բ", Font.PLAIN, 15));
		label_chatroomcount.setBounds(10, 154, 90, 21);
		panel_serverstatus.add(label_chatroomcount);
		
		JLabel label_6 = new JLabel("������״̬��");
		label_6.setForeground(Color.BLACK);
		label_6.setFont(new Font("��Բ", Font.PLAIN, 14));
		label_6.setBounds(10, 185, 90, 21);
		panel_serverstatus.add(label_6);
		
		label_serverstatus = new JLabel(server_status?"������":"ֹͣ");
		label_serverstatus.setHorizontalAlignment(SwingConstants.CENTER);
		label_serverstatus.setForeground(Color.red);
		label_serverstatus.setFont(new Font("��Բ", Font.BOLD, 15));
		label_serverstatus.setBounds(10, 216, 90, 21);
		panel_serverstatus.add(label_serverstatus);
		
		JLabel label_1 = new JLabel("�˿ڣ�");
		label_1.setForeground(Color.BLACK);
		label_1.setFont(new Font("��Բ", Font.PLAIN, 14));
		label_1.setBounds(10, 253, 42, 21);
		panel_serverstatus.add(label_1);
		
		label_serverport = new JLabel(""+server_port);
		label_serverport.setHorizontalAlignment(SwingConstants.CENTER);
		label_serverport.setFont(new Font("��Բ", Font.PLAIN, 15));
		label_serverport.setBounds(58, 253, 42, 21);
		panel_serverstatus.add(label_serverport);
		
		JPanel panel_ontifaction = new JPanel();
		panel_ontifaction.setBackground(SystemColor.menu);
		panel_ontifaction.setLayout(null);
		panel_ontifaction.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		panel_ontifaction.setBounds(107, 0, 319, 284);
		panel.add(panel_ontifaction);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(null);
		scrollPane_1.setBounds(10, 10, 299, 220);
		panel_ontifaction.add(scrollPane_1);
		
		textArea_notification = new JTextArea();
		textArea_notification.setBorder(null);
		textArea_notification.setLineWrap(true);
		textArea_notification.setEnabled(false);
		textArea_notification.setText("");
		textArea_notification.setFont(new Font("����ϸ��", Font.PLAIN, 15));
		textArea_notification.setEditable(false);
		scrollPane_1.setViewportView(textArea_notification);
		
		JButton button_sendnotice = new JButton("����֪ͨ");
		button_sendnotice.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_sendnotice.setForeground(Color.white);
		button_sendnotice.setFont(new Font("΢���ź�", Font.PLAIN, 15));
		button_sendnotice.addActionListener(this);
		button_sendnotice.setBounds(225, 240, 84, 31);
		button_sendnotice.setToolTipText("<html><div bgcolor='#91e1d6'>��������������û����ͷ�������Ϣ��</div></html>");
		panel_ontifaction.add(button_sendnotice);
		
		textField_notifaction = new JTextField();
		textField_notifaction.setColumns(10);
		textField_notifaction.setBounds(10, 239, 205, 34);
		textField_notifaction.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER&&e.isControlDown()){
					actionPerformed(null);
					button_sendnotice.setText("����֪ͨ");
				}
				if(e.getKeyCode()==KeyEvent.VK_S&&e.isControlDown()){
					button_sendnotice.setText("����");
					textField_notifaction.setText("#");
				}
			}
		});
		panel_ontifaction.add(textField_notifaction);
	}
	
	/**
	 * ˢ�·�����״̬
	 * @param user_count
	 * @param online_user_count
	 * @param chatroom_count
	 * @param server_port
	 * @param server_status
	 */
	public void refreshStatus(int user_count, int online_user_count, 
							  int chatroom_count, int server_port,
							  boolean server_status){
		this.user_count = user_count;
		this.online_user_count = online_user_count;
		this.chatroom_count = chatroom_count;
		this.server_port = server_port;
		this.server_status = server_status;
		refreshAll();
	}
	
	/**
	 * ������������
	 */
	public void resetStatus(){
		online_user_list=new ArrayList<>();
		refreshStatus(0, 0, 0, 0, false);
	}
	
	/**
	 * �û���¼
	 * @param username
	 */
	public void userLogin(String username){
		online_user_list.add(username);
		this.online_user_count++;
		appendMsg("�û�:"+username+"�����ߣ�");
		refreshAll();
	}
	
	/**
	 * �û�����
	 * @param username
	 */
	public void userLogout(String username){
		online_user_list.remove(username);
		this.online_user_count--;
		appendMsg("�û�:"+username+"�����ߣ�");
		refreshAll();
	}
	
	/**
	 * ����������Ӵ��ڷ�����Ϣ
	 * @param msg
	 */
	public void appendMsg(String msg){
		textArea_notification.append(msg+"\n");
	}
	
	/**
	 * ˢ������״̬�ؼ�
	 */
	public void refreshAll(){
		label_usercount.setText(""+user_count);
		label_onlineusercount.setText(""+online_user_count);
		label_chatroomcount.setText(""+chatroom_count);
		if(server_status){
			label_serverstatus.setForeground(Color.green);
			label_serverstatus.setText("������");
		}else {
			label_serverstatus.setForeground(Color.red);
			label_serverstatus.setText("ֹͣ");
		}
		label_serverport.setText(""+server_port);
		list_onlineuser.setListData(online_user_list.toArray());
		list_onlineuser.revalidate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String notice=textField_notifaction.getText();
		if (!notice.equals("")) {
			textField_notifaction.setText("");
			//���Ϊ����������ָ��
			if(notice.charAt(0)=='#'){
				dealSettings(notice.substring(1, notice.length()));
			}else {
				textArea_notification.append("��������Ϣ:"+notice+'\n');
			}
			sendNotification(notice);
		}
	}
	
	/**
	 * �������������ָ��
	 * @param substring
	 */
	private void dealSettings(String stttings) {
		String[]ps=stttings.split(" ");
		if ("set-port".equals(ps[0])&&ps[1]!=null) {
			int port=Integer.parseInt(ps[1]);
			server.setServerPort(port);
			root.setServerport(port);
			appendMsg("�ɹ����ö˿ں�Ϊ��"+port);
		}else if ("help".equals(ps[0])) {
			appendMsg("#set-port portnum    ���ö˿ں�\n#help    �鿴����");
		}else {
			appendMsg("δָ֪�");
		}
	}
	
	/**
	 * ���ͷ�����֪ͨ
	 * @param notice
	 */
	public void sendNotification(String notice){
		Notification notify=new Notification(Notification.OTHER, ""+notice);
		server.sendNotification(notify);
	}
}
