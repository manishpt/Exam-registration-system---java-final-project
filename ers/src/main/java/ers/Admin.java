package ers;

import static com.mongodb.client.model.Filters.eq;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class Admin extends JFrame {

	private JPanel contentPane;
	private JTextField examSubjectTextField;
	private JTextField examTimeTextField;
	private JTextField examMaxMarksTextField;
	private JButton logoutButton;

	
	public Admin() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 542, 469);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel examSubjectLabel = new JLabel("subject");
		examSubjectLabel.setBounds(50, 99, 89, 25);
		contentPane.add(examSubjectLabel);
		
		JLabel examDateLabel = new JLabel("exam date");
		examDateLabel.setBounds(50, 157, 89, 25);
		contentPane.add(examDateLabel);
		
		JLabel examMaxMarksLabel = new JLabel("max marks");
		examMaxMarksLabel.setBounds(50, 220, 89, 25);
		contentPane.add(examMaxMarksLabel);
		
		examSubjectTextField = new JTextField();
		examSubjectTextField.setBounds(207, 102, 96, 19);
		contentPane.add(examSubjectTextField);
		examSubjectTextField.setColumns(10);
		
		examTimeTextField = new JTextField();
		examTimeTextField.setBounds(207, 160, 96, 19);
		contentPane.add(examTimeTextField);
		examTimeTextField.setColumns(10);
		
		examMaxMarksTextField = new JTextField();
		examMaxMarksTextField.setBounds(207, 223, 96, 19);
		contentPane.add(examMaxMarksTextField);
		examMaxMarksTextField.setColumns(10);
		JButton checkBtn = new JButton("check");
		checkBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MongoClient mongoClient = new MongoClient("localhost",27017);
				MongoDatabase db = mongoClient.getDatabase("ExamsDB");
				MongoCollection<Document> collection=db.getCollection("exams");
				String su=examSubjectTextField.getText();
				Document d=collection.find(eq("subject",su)).first();
				if (d!=null) {
					examTimeTextField.setText(d.getString("time"));
					examMaxMarksTextField.setText(d.getString("maxMarks"));
					
					
				}
				
				
				
				 
				
				
				
			}
		});
		checkBtn.setBounds(351, 99, 85, 21);
		contentPane.add(checkBtn);
		
		JButton submitButton = new JButton("submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String subject = examSubjectTextField.getText();
				String time = examTimeTextField.getText();
				String maxMarks = examMaxMarksTextField.getText();
				MongoClient mongoClient = new MongoClient("localhost",27017);
				MongoDatabase db = mongoClient.getDatabase("ExamsDB");
				MongoCollection<Document> collection=db.getCollection("exams");
				Document d=collection.find(eq("subject",subject)).first();
				if (d!=null) {
					collection.updateMany(
						    Filters.eq("subject",subject),
						    Updates.combine(
						        Updates.set("time",time),
						        Updates.set("maxMarks",maxMarks)
			));
				}
				else {
					
					Document doc =new Document("subject",subject);
					doc.append("time",time);
					doc.append("maxMarks",maxMarks);  
					collection.insertOne(doc);
					JOptionPane.showMessageDialog(new JFrame(), "Success","exam added Successfully",JOptionPane.OK_OPTION);
					
					//Welcome frame = new Welcome(req);
					//frame.setVisible(true);
					mongoClient.close();
					
				}
			}
		});
		submitButton.setBounds(50, 279, 85, 21);
		contentPane.add(submitButton);
		
		logoutButton = new JButton("logout");
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login l=new Login();
				l.setVisible(true);
				dispose();
			}
		});
		logoutButton.setBounds(50, 325, 85, 21);
		contentPane.add(logoutButton);
		
	
	}
}
