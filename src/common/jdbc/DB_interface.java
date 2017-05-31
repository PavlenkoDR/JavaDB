package common.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DB_interface {
	public Statement statement;
	public dao_base dao;
	// � дентификаторы параметров подключения
	private final int HOST = 0; // хост (компьютер)
	private final int SCHEMA = 1; // схема (база данных)
	private final int LOGIN = 2; // логин подключения
	private final int PWD = 3; // пароль подключения
	private String[] params = { "localhost", "db", "root", "lalkoed228"}; // MySQL
	private final int port = 3306; // порты СУБД

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Поцедура подключения к серверу БД
	 * 
	 * @param dao модуль доступа
	 */
	public void createConnecion(dao_base dao) {
		// Формирование строки подключения
		dao.setURL(params[HOST], params[SCHEMA], port);
		// Подключение к серверу
		dao.Connect(params[LOGIN], params[PWD]);
	}
	public void createConnecion(dao_base dao, String user, String password) {
		// Формирование строки подключения
		dao.setURL(params[HOST], params[SCHEMA], port);
		// Подключение к серверу
		dao.Connect(user, password);
	}
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Функция выбора БД
	 * @param statement идентификатор доступа открытого подключения БД
	 * @param db навание БД
	 */
	public void SelectDB(Statement statement, String db)
	{
		try {
			statement.execute("USE " + db + ";");
		} catch (SQLException e) {
			System.err.println("DB not selected!");
		}
	}
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Функция удаления объектов list из таблицы table
	 * @param statement идентификатор доступа открытого подключения БД
	 * @param table навание таблицы
	 * @param list удаляемые объекты таблицы
	 */
	public void DeleteValue(Statement statement, String table, List<String> list)
	{
		//DELETE FROM Laptop WHERE screen < 12;
		String valusestr = "";
		for (int i = 0; i < list.size(); i++)
		{
			valusestr += list.get(i);
			if (i != list.size() - 1) valusestr += " AND ";
		}
		String exs = "DELETE FROM " + table + " WHERE (" + valusestr + ");";
		try {
			statement.execute(exs);
		} catch (SQLException e) {
			System.err.println("Value not deleted: " + exs);
		}
	}
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Функция вставки объектов list в таблицу table
	 * @param statement идентификатор доступа открытого подключения БД
	 * @param table навание таблицы
	 * @param list удаляемые объекты таблицы
	 */
	public void InsertValue(Statement statement, String table, List<? extends Object> list)
	{
		String valusestr = "";
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).getClass() == String.class)
				 valusestr += "\""+list.get(i)+"\"";
			else valusestr += ""+list.get(i)+"";
			if (i != list.size() - 1) valusestr += ", ";
		}
		String exs = "INSERT INTO " + table + " VALUES (" + valusestr + ");";
		try {
			statement.execute(exs);
		} catch (SQLException e) {
			System.err.println("Value not inserted: " + exs);
		}
	}
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Функция амены объектов oldvalues на newvalues в таблицу table
	 * @param statement идентификатор доступа открытого подключения БД
	 * @param table навание таблицы
	 * @param oldvalues старые значения
	 * @param newvalues новые значения
	 */
	public void UpdateValue(Statement statement, String table, List<String> oldvalues, List<String> newvalues)
	{
		String oldvaluesstr = "";
		for (int i = 0; i < oldvalues.size(); i++)
		{
			oldvaluesstr += oldvalues.get(i);
			if (i != oldvalues.size() - 1) oldvaluesstr += ", ";
		}
		String newvaluesstr = "";
		for (int i = 0; i < newvalues.size(); i++)
		{
			newvaluesstr += newvalues.get(i);
			if (i != newvalues.size() - 1) newvaluesstr += ", ";
		}
		String exs = "UPDATE " + table + " SET " + newvaluesstr + " WHERE " + oldvaluesstr + ";";
		try {
			statement.execute(exs);
		} catch (SQLException e) {
			System.err.println("Value not udated: " + exs);
		}
	}
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Конструктор
	 */
	public DB_interface() {
		// Определение "модуля доступа к СУБД"
		dao = new dao_mysql();
		createConnecion(dao);
    	try {
			statement = dao.getConnection().createStatement();
		} catch (SQLException e) {
			System.err.println("Connection dont created!");
		}
	}
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Функция закрытия соединения связи с БД
	 */
	public void CloseConnection()
	{
       	try {
           	statement.close();
           	statement = null;
		} catch (SQLException e) {
			System.err.println("Connection dont close!");
		}
		dao.Disconnect(dao.getConnection());
		
	}
	
	public ArrayList<ArrayList<Object> > getTable(Statement statement, String table, List<? extends Object> list2){
        
		ArrayList<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();

		try {
			ResultSet rs = statement.executeQuery("SELECT *  FROM "+ table + ";");
			while (rs.next()) 
			{
				ArrayList<Object> tmp = new ArrayList<Object>();
				for (int i = 0; i < list2.size(); i++)
				{
					tmp.add(rs.getString(list2.get(i).toString()));
				}
				list.add(tmp);
			}
		} catch (SQLException e) {
			System.err.println("getTable error!");
		}
        return list;
    }
	
	public ArrayList<String> getDB(Statement statement){
        
		ArrayList<String> list = new ArrayList<String>();

		try {
			ResultSet rs = statement.executeQuery("SHOW DATABASES;");
			while (rs.next()) 
			{
				list.add(rs.getString("Database"));
			}
		} catch (SQLException e) {
			System.err.println("getDB error!");
		}
        return list;
    }
	
	public ArrayList<String> getTables(Statement statement, String db){
        
		ArrayList<String> list = new ArrayList<String>();

		try {
			ResultSet rs = statement.executeQuery("SHOW TABLES;");
			while (rs.next()) 
			{
				list.add(rs.getString("Tables_in_"+db));
			}
		} catch (SQLException e) {
			System.err.println("getTables error!");
		}
        return list;
    }
	
	public ArrayList<String> getDescribe(Statement statement, String table){
        
		ArrayList<String> list = new ArrayList<String>();

		try {
			ResultSet rs = statement.executeQuery("DESCRIBE " + table + ";");
			while (rs.next()) 
			{
				list.add(rs.getString("Field"));
			}
		} catch (SQLException e) {
			System.err.println("getDescribe error!");
		}
        return list;
    }

	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Точка входа
	 */
	/*
	public static void main(String[] args) {
		DB_interface db= new DB_interface();
		db.SelectDB(db.statement, "db");
		db.InsertValue(db.statement, "Books", Arrays.asList(0, "2sdde123", "0.1sup3"));
		db.DeleteValue(db.statement, "Books", Arrays.asList("id = 1"));
		db.UpdateValue(db.statement, "Books", Arrays.asList("id = 0") , Arrays.asList("id = 7666"));
    	
		try {
			ResultSet rs = db.statement.executeQuery("SELECT id  FROM Books");
			while (rs.next()) 
			{
				  System.out.println(rs.getString("id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
		db.CloseConnection();
		System.exit(0);
	}
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
*/

}
