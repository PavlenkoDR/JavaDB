package common.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import common.jdbc.DB_interface;
 
public class DBFrame  extends JFrame{
	
	private static final long serialVersionUID = 8981378992342875948L;

    static int i = 0;
    DB_interface db;
    String user;
 
    JTable table;
    JComboBox<Object> DBComboBox;
    JComboBox<Object> TableComboBox;
    JTextField id;
    JTextField name;
    JTextField description;
    JTextField price;
    
    public DBFrame() {
        super("Магаин \"Любые товары с алика\"");

		db= new DB_interface();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        Font font = new Font("Verdana", Font.PLAIN, 10);
        
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(font);
        JPanel DB_SELECT_Panel = new JPanel();
        JPanel PRODUCT_ADD_Panel = new JPanel();
        tabbedPane.addTab("Выбор базы данных", 	DB_SELECT_Panel);
        tabbedPane.addTab("Добавление товара", 	PRODUCT_ADD_Panel);
        JMenuBar menu = new JMenuBar();
        JMenu menuItems = new JMenu("Польователь");
        JMenuItem signup = new JMenuItem("Регистрация");
        signup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFrame signinForm = new JFrame("Регистрация");
				signinForm.setLayout(new BorderLayout());
				JPanel ttt = new JPanel();
		        final JTextField email = new JTextField();
		        final JTextField password = new JTextField();
		        final JTextField reeat_password = new JTextField();
		        JButton ok = new JButton("OK");
		        ok.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String exc = null;
						try {
							user = email.getText();
							db.createConnecion(db.dao);
							exc = "CREATE USER '" + email.getText() + "'@'localhost' IDENTIFIED BY '"+ password.getText().toString() + "';";
							db.statement.execute(exc);
							System.out.println(exc);
							exc = email.getText();
							db.dao.createSchema(exc);
							System.out.println(exc);
							exc = "GRANT SELECT ON db.Items TO '"+ email.getText() + "'@'localhost';";
							db.statement.execute(exc);
							System.out.println(exc);
							exc = "GRANT ALL PRIVILEGES ON "+ email.getText() + ".Items TO '"+ email.getText() + "'@'localhost';";
							db.statement.execute(exc);
							System.out.println(exc);
							exc = "FLUSH PRIVILEGES;";
							db.statement.execute(exc);
							System.out.println(exc);
							db.statement.execute("USE " + user + ";");
							exc = "create table items (id_item int (10) NOT NULL, name varchar(20) NOT NULL, description varchar(100) NOT NULL, price FLOAT NOT NULL, PRIMARY KEY (id_item));";
							db.statement.execute(exc);
							System.out.println("exc");
							db.createConnecion(db.dao, email.getText(), password.getText().toString());
							System.out.println("!");
							signinForm.dispose();
						} catch (SQLException e1) {
							System.err.println("Регистрация не удалась: " + exc);
						}
					}
				});
		        ttt.add(email);
		        email.setPreferredSize(new Dimension(100, 25));
		        email.setToolTipText("email");
		        ttt.add(password);
		        password.setPreferredSize(new Dimension(100, 25));
		        password.setToolTipText("password");
		        ttt.add(reeat_password);
		        reeat_password.setPreferredSize(new Dimension(100, 25));
		        reeat_password.setToolTipText("reeat password");
		        ttt.add(ok);
		        signinForm.add(ttt);
		        signinForm.setPreferredSize(new Dimension(450, 80));
		        signinForm.pack();
		        signinForm.setLocationRelativeTo(null);
		        signinForm.setVisible(true);
			}
		});
        menuItems.add(signup);
        JMenuItem signin = new JMenuItem("Авториация");
        signin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFrame signinForm = new JFrame("Авториация");
				signinForm.setLayout(new BorderLayout());
				JPanel ttt = new JPanel();
		        final JTextField email = new JTextField();
		        final JTextField password = new JTextField();
		        JButton ok = new JButton("OK");
		        ok.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						db.createConnecion(db.dao, email.getText(), password.getText().toString());
						user = email.getText();
						signinForm.dispose();
					}
				});
		        ttt.add(email);
		        email.setPreferredSize(new Dimension(100, 25));
		        email.setToolTipText("email");
		        ttt.add(password);
		        password.setPreferredSize(new Dimension(100, 25));
		        password.setToolTipText("password");
		        ttt.add(ok);
		        signinForm.add(ttt);
		        signinForm.setPreferredSize(new Dimension(450, 80));
		        signinForm.pack();
		        signinForm.setLocationRelativeTo(null);
		        signinForm.setVisible(true);
			}
		});
        menuItems.add(signin);
        menu.add(menuItems);
        setJMenuBar(menu);
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
 
        JButton remove = new JButton("Удалить");
        remove.setFont(font);
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        tabbedPane.setPreferredSize(new Dimension(260, 100));
        content.add(tabbedPane, BorderLayout.NORTH);
 
        getContentPane().add(content);
 
        table = new JTable();
        table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				id.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
				name.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
				description.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
				price.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        JButton add = new JButton("Добавить");
        JButton update = new JButton("Обновить");
        JButton delete = new JButton("Удалить");
        JButton korzinaadd = new JButton("Добавить в корзину");
        JButton korzina = new JButton("Корзина");
        JButton baza = new JButton("База");
        id = new JTextField();
        id.setPreferredSize(new Dimension(100, 25));
        name = new JTextField();
        name.setPreferredSize(new Dimension(100, 25));
        description = new JTextField();
        description.setPreferredSize(new Dimension(100, 25));
        price = new JTextField();
        price.setPreferredSize(new Dimension(100, 25));
        JPanel DBPanel = new JPanel();
        JScrollPane pane = new JScrollPane(DBPanel);
        DBPanel.add(table, BorderLayout.CENTER);
        PRODUCT_ADD_Panel.add(id, BorderLayout.CENTER);
        PRODUCT_ADD_Panel.add(name, BorderLayout.CENTER);
        PRODUCT_ADD_Panel.add(description, BorderLayout.CENTER);
        PRODUCT_ADD_Panel.add(price, BorderLayout.CENTER);
        PRODUCT_ADD_Panel.add(add, BorderLayout.CENTER);
        PRODUCT_ADD_Panel.add(update, BorderLayout.CENTER);
        PRODUCT_ADD_Panel.add(delete, BorderLayout.CENTER);
        PRODUCT_ADD_Panel.add(korzinaadd, BorderLayout.CENTER);
        PRODUCT_ADD_Panel.add(korzina, BorderLayout.CENTER);
        PRODUCT_ADD_Panel.add(baza, BorderLayout.CENTER);
        add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				db.InsertValue(db.statement, "items", Arrays.asList(new Integer(id.getText()), name.getText(), description.getText(), new Double(price.getText())));

        		ArrayList<String> tmpArr = db.getDescribe(db.statement, TableComboBox.getSelectedItem().toString());
        		ArrayList<ArrayList<Object> > obj = db.getTable(db.statement, TableComboBox.getSelectedItem().toString(), tmpArr);
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(tmpArr.toArray());
                for(int i = 0; i < obj.size(); i++){
                	ArrayList<Object> tmp = obj.get(i);
                    Object[] rowData = new Object[tmp.size()];
                    for(int j = 0; j < tmp.size(); j++)
                    	rowData[j] = obj.get(i).get(j);
                    model.addRow(rowData);
                }
                table.setModel(model);
			}
		});
        update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				db.UpdateValue(db.statement, "items", Arrays.asList("id_item = " + table.getValueAt(table.getSelectedRow(), 0)), Arrays.asList("id_item = " + new Integer(id.getText()), "name = \"" + name.getText()+"\"", "description = \"" + description.getText()+"\"", "price = " + new Double(price.getText())));

        		ArrayList<String> tmpArr = db.getDescribe(db.statement, TableComboBox.getSelectedItem().toString());
        		ArrayList<ArrayList<Object> > obj = db.getTable(db.statement, TableComboBox.getSelectedItem().toString(), tmpArr);
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(tmpArr.toArray());
                for(int i = 0; i < obj.size(); i++){
                	ArrayList<Object> tmp = obj.get(i);
                    Object[] rowData = new Object[tmp.size()];
                    for(int j = 0; j < tmp.size(); j++)
                    	rowData[j] = obj.get(i).get(j);
                    model.addRow(rowData);
                }
                table.setModel(model);
			}
		});
        delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				db.DeleteValue(db.statement, "items", Arrays.asList("id_item = " + id.getText()));
        		ArrayList<String> tmpArr = db.getDescribe(db.statement, TableComboBox.getSelectedItem().toString());
        		ArrayList<ArrayList<Object> > obj = db.getTable(db.statement, TableComboBox.getSelectedItem().toString(), tmpArr);
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(tmpArr.toArray());
                for(int i = 0; i < obj.size(); i++){
                	ArrayList<Object> tmp = obj.get(i);
                    Object[] rowData = new Object[tmp.size()];
                    for(int j = 0; j < tmp.size(); j++)
                    	rowData[j] = obj.get(i).get(j);
                    model.addRow(rowData);
                }
                table.setModel(model);
			}
		});
        korzina.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				db.SelectDB(db.statement, user);
        		ArrayList<String> tmpArr = db.getDescribe(db.statement, "items");
        		ArrayList<ArrayList<Object> > obj = db.getTable(db.statement, "items", tmpArr);
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(tmpArr.toArray());
                for(int i = 0; i < obj.size(); i++){
                	ArrayList<Object> tmp = obj.get(i);
                    Object[] rowData = new Object[tmp.size()];
                    for(int j = 0; j < tmp.size(); j++)
                    	rowData[j] = obj.get(i).get(j);
                    model.addRow(rowData);
                }
                table.setModel(model);
			}
		});
        korzinaadd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				db.SelectDB(db.statement, user);
				db.InsertValue(db.statement, "items", Arrays.asList(new Integer(id.getText()), name.getText(), description.getText(), new Double(price.getText())));
				db.SelectDB(db.statement, "db");
			}
		});
        baza.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				db.SelectDB(db.statement, "db");
        		ArrayList<String> tmpArr = db.getDescribe(db.statement, TableComboBox.getSelectedItem().toString());
        		ArrayList<ArrayList<Object> > obj = db.getTable(db.statement, TableComboBox.getSelectedItem().toString(), tmpArr);
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(tmpArr.toArray());
                for(int i = 0; i < obj.size(); i++){
                	ArrayList<Object> tmp = obj.get(i);
                    Object[] rowData = new Object[tmp.size()];
                    for(int j = 0; j < tmp.size(); j++)
                    	rowData[j] = obj.get(i).get(j);
                    model.addRow(rowData);
                }
                table.setModel(model);
			}
		});
        content.add(pane, BorderLayout.CENTER);
        JButton refresh = new JButton("Обновить");
        refresh.setFont(font);
        
        DBComboBox = new JComboBox<Object>(db.getDB(db.statement).toArray());
        TableComboBox = new JComboBox<Object>();
        DBComboBox.setEditable(true);
        TableComboBox.setEditable(true);
        DBComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
                String item = (String) DBComboBox.getSelectedItem();
                db.SelectDB(db.statement, item);
                ArrayList<String> tmp = db.getTables(db.statement, item);
                TableComboBox.removeAllItems();
                for (int i = 0; i < tmp.size(); i++)
                	TableComboBox.addItem(tmp.get(i));
                TableComboBox.setEditable(true);
                TableComboBox.setVisible(true);
			}
		});
        TableComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
        		ArrayList<String> tmpArr = db.getDescribe(db.statement, TableComboBox.getSelectedItem().toString());
        		ArrayList<ArrayList<Object> > obj = db.getTable(db.statement, TableComboBox.getSelectedItem().toString(), tmpArr);
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(tmpArr.toArray());
                for(int i = 0; i < obj.size(); i++){
                	ArrayList<Object> tmp = obj.get(i);
                    Object[] rowData = new Object[tmp.size()];
                    for(int j = 0; j < tmp.size(); j++)
                    	rowData[j] = obj.get(i).get(j);
                    model.addRow(rowData);
                }
                table.setModel(model);
			}
		});
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		ArrayList<String> tmpArr = db.getDescribe(db.statement, TableComboBox.getSelectedItem().toString());
        		ArrayList<ArrayList<Object> > obj = db.getTable(db.statement, TableComboBox.getSelectedItem().toString(), tmpArr);
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(tmpArr.toArray());
                for(int i = 0; i < obj.size(); i++){
                	ArrayList<Object> tmp = obj.get(i);
                    Object[] rowData = new Object[tmp.size()];
                    for(int j = 0; j < tmp.size(); j++)
                    	rowData[j] = obj.get(i).get(j);
                    model.addRow(rowData);
                }
                table.setModel(model);
            }
        });
        DB_SELECT_Panel.add(DBComboBox);
        DB_SELECT_Panel.add(TableComboBox);
        DB_SELECT_Panel.add(refresh);
        DB_SELECT_Panel.add(remove);
        
        setPreferredSize(new Dimension(1280, 720));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                new DBFrame();
            }
        });
    }
}
