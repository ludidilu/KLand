import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import panel.Panel;
import playerData.PlayerData;
import data.dataDB.DB;
import data.dataDB.MainDB;
import data.dataDB.user.DB_user;


public class Main {

	private static JFrame frame;
	private static JPanel panel;
	private static JTextField userNameTf;
	private static JList<Integer> list;
	private static DefaultListModel<Integer> listModel;
	private static JScrollPane scrollPane;
	private static JButton loginBt;
	private static JTextField heroIDTf;
	private static JButton addHeroBt;
	private static JButton delHeroBt;
	
	private static PlayerData playerData;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub


		Panel.init();
		
		MainDB.init("localhost", 6379);
			
		
		frame = new JFrame("GMTools");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(500,500);
		
		
		panel = new JPanel();
		
		panel.setLayout(null);
		
		frame.add(panel);
		
		
		
		userNameTf = new JTextField();
		
		userNameTf.setBounds(10, 80, 100, 30);
		
		panel.add(userNameTf);
		
		
		list = new JList<>();
		
		listModel = new DefaultListModel<>();
		
		list.setModel(listModel);
		
		scrollPane = new JScrollPane(list);
		
		scrollPane.setVisible(true);
		
		scrollPane.setBackground(new Color(100));
		
		scrollPane.setBounds(10,150,100,100);
		
		panel.add(scrollPane);
		
		frame.setVisible(true);
		
		
		loginBt = new JButton("Login");
		
		loginBt.setBounds(10, 10, 100, 30);
		
		panel.add(loginBt);
		
		
		loginBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				listModel.clear();
				
				try{
				
					loginBtClick();
					
				}catch(Exception e){
					
					
				}
			}
		});
		
		heroIDTf = new JTextField();
		
		heroIDTf.setBounds(10, 300, 100, 30);
		
		panel.add(heroIDTf);
		
		
		addHeroBt = new JButton("Add Hero");
		
		addHeroBt.setBounds(10, 340, 100, 30);
		
		panel.add(addHeroBt);
		
		addHeroBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				try{
				
					addHeroBtClick();
					
				}catch(Exception e){
					
					e.printStackTrace();
				}
			}
		});
		
		
		delHeroBt = new JButton("Del Hero");
		
		delHeroBt.setBounds(10, 380, 100, 30);
		
		panel.add(delHeroBt);
		
		delHeroBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				try{
					
					delHeroBtClick();
					
				}catch(Exception e){
					
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void loginBtClick() throws Exception{
		
		if(!userNameTf.getText().equals("")){
		
			String pwd = DB.jedis.get(DB_user.PWD + userNameTf.getText());
			
			if(pwd != null){
				
				playerData = new PlayerData();
				
				playerData.loadAllDataFromDB(userNameTf.getText());
				
				for(int npcID : playerData.heroData.heros){
					
					listModel.addElement(npcID);
				}
			}
		}
	}

	private static void addHeroBtClick() throws Exception{
		
		if(playerData != null && !heroIDTf.getText().equals("")){
			
			playerData.heroData.heros.add(Integer.parseInt(heroIDTf.getText()));
			
			playerData.saveAllDataToDB(userNameTf.getText());
			
			listModel.addElement(Integer.parseInt(heroIDTf.getText()));
		}
	}
	
	private static void delHeroBtClick() throws Exception{
		
		if(playerData != null && list.getSelectedIndex() != -1){
			
			playerData.heroData.heros.remove(list.getSelectedIndex());
			
			playerData.saveAllDataToDB(userNameTf.getText());
			
			listModel.remove(list.getSelectedIndex());
		}
	}
}
