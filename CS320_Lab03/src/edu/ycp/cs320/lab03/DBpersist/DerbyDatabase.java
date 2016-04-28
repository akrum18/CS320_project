package edu.ycp.cs320.lab03.DBpersist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.lab03.model.Menu;
import edu.ycp.cs320.lab03.model.Order;
import edu.ycp.cs320.lab03.model.Owner;
import edu.ycp.cs320.lab03.model.Patron;
import edu.ycp.cs320.lab03.model.Restaurant;
import edu.ycp.cs320.lab03.model.User;

public class DerbyDatabase implements IDatabase {
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load Derby driver");
		}
	}

	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 100;
	//*********************************
	//find restaurants by a given city
	//*********************************
	@Override
	public List<Restaurant> getListOfRestaurantsByCity(final String city) {
		return executeTransaction(new Transaction<List<Restaurant>>() {
			@Override
			public List<Restaurant> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {


					stmt = conn.prepareStatement(
							"select * from restaurants " +
									" where rest_city = ? "
							);
					stmt.setString(1, city);
					List<Restaurant> result = new ArrayList<Restaurant>();
					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					while (resultSet.next()) {
						found = true;

						Restaurant rest = new Restaurant();
						loadRestaurant(rest, resultSet, 1);
						result.add(rest);
					}

					// check if the title was found
					if (!found) {
						System.out.println("<" + city + "> was not found in the restaurants table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	//********************
	//adding a user to the database
	//********************
	@Override
	public List<User> addUserToDatabase(final String name, final String pswd, final String email, final String type, final String first,
			final String last) {
		return executeTransaction(new Transaction<List<User>>() {
			@Override
			public List<User> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement(
							"insert into users(user_userName, user_passWord, user_email, user_accountType, user_firstName, user_lastName) " +
									" values(?, ?, ?, ?, ?, ?) "
							);
					stmt.setString(1, name);
					stmt.setString(2, pswd);
					stmt.setString(3, email);
					stmt.setString(4, type);
					stmt.setString(5, first);
					stmt.setString(6, last);
					stmt.executeUpdate();
					
					stmt2 = conn.prepareStatement(
							"select * " +
									" from users " +
									" where user_userName = ?"
							);
					stmt2.setString(1, name);
					
					resultSet = stmt2.executeQuery();

					// for testing that a result was returned
					Boolean found = false;
					List<User> result = new ArrayList<User>();
					while (resultSet.next()) {
						found = true;
						User u = new User();
						loadUser(u, resultSet, 1);
						result.add(u);
					}

					// check if the title was found
					if (!found) {
						System.out.println("<" + name + "> was not found in the users table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	//*************************************************
	//match user name with password for authentication
	//*************************************************
	@Override
	public List<User> matchUsernameWithPassword(final String name) {
		
		return executeTransaction(new Transaction<List<User>>() {
			@Override
			public List<User> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {


					stmt = conn.prepareStatement(
							"select * from Users " +
									" where user_userName = ? "
							);
					stmt.setString(1, name);
					List<User> result = new ArrayList<User>();
					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					while (resultSet.next()) {
						found = true;

						User u = new User();
						loadUser(u, resultSet, 1);
						result.add(u);
					}

					// check if the title was found
					if (!found) {
						System.out.println("<" + name + "> was not found in the Users table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	//********************************
	//Delete a user from the database
	//********************************
	@Override
	public List<User> DeleteUserFromDatabase(final String name, final String pswd) {
		return executeTransaction(new Transaction<List<User>>() {
			@Override
			public List<User> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;

				try {


					stmt = conn.prepareStatement(
							"delete from users " +
									" where user_userName = ? " +
									" and user_userpassWord = ? "
							);
					stmt.setString(1, name);
					stmt.setString(2, pswd);
					stmt.executeUpdate();

					// return all users and see that the one entered was deleted
					
					stmt2 = conn.prepareStatement(
							"select * from users " 		
							);
					resultSet = stmt2.executeQuery();
					List<User> result = new ArrayList<User>();
					
					Boolean found = false;

					while (resultSet.next()) {
						found = true;

						User u = new User();
						loadUser(u, resultSet, 1);
						result.add(u);
					}

					// check if the title was found
					if (!found) {
						System.out.println("<" + name + "> users list is empty");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	//************************
	//Change username
	//***********************
	@Override
	public List<User> changeUsername(final String name, final String newName, final String pswd) {
		return executeTransaction(new Transaction<List<User>>() {
			@Override
			public List<User> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;

				try {


					stmt = conn.prepareStatement(
							"update users " +
									" set user_userName = ? " +
									" where user_userName = ? " +
									" and user_userpassWord = ? "
							);
					stmt.setString(1, newName);
					stmt.setString(2, name);
					stmt.setString(3, pswd);
					stmt.executeUpdate();

					// return all users and see that the one entered was deleted
					
					stmt2 = conn.prepareStatement(
							"select * from users " 	+
									" where user_userName = ? "
							);
					//ensure new userName is in database
					stmt2.setString(1, newName);
					resultSet = stmt2.executeQuery();
					List<User> result = new ArrayList<User>();
					
					Boolean found = false;

					while (resultSet.next()) {
						found = true;

						User u = new User();
						loadUser(u, resultSet, 1);
						result.add(u);
					}

					// check if the title was found
					if (!found) {
						System.out.println("<" + name + "> users list is empty");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	//***********************
	//Add item to menu
	//************************
	@Override
	public List<Menu> addItemToMenu(final String item, final Double price, final int rest_id) {
		return executeTransaction(new Transaction<List<Menu>>() {
			@Override
			public List<Menu> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement(
							"insert into menu(rest_id, menu_item, menu_price) " +
									" values(?, ?, ?) "
							);
					stmt.setInt(1, rest_id);
					stmt.setString(2, item);
					stmt.setDouble(3, price);
					stmt.executeUpdate();
					
					stmt2 = conn.prepareStatement(
							"select * " +
									" from menu " +
									" where menu_name = ?"
							);
					stmt2.setString(1, item);
					
					resultSet = stmt2.executeQuery();

					// for testing that a result was returned
					Boolean found = false;
					List<Menu> result = new ArrayList<Menu>();
					while (resultSet.next()) {
						found = true;
						Menu m = new Menu();
						loadMenu(m, resultSet, 1);
						result.add(m);
					}

					// check if the title was found
					if (!found) {
						System.out.println("<" + item + "> was not found in the menu table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	//************************************
	//Show user a menu
	//**************************************
	@Override
	public List<Menu> menuByRestName(final String rest) {
		return executeTransaction(new Transaction<List<Menu>>() {
			@Override
			public List<Menu> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {


					stmt = conn.prepareStatement(
							"select menu.*  " +
									" from menu, restaurants "  +
									" where rest_name = ? "  +
									" and menu.rest_id = restaurants.rest_id; "
							);
					stmt.setString(1, rest);
					List<Menu> result = new ArrayList<Menu>();
					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					while (resultSet.next()) {
						found = true;

						Menu m = new Menu();
						loadMenu(m, resultSet, 1);
						result.add(m);
					}

					// check if the title was found
					if (!found) {
						System.out.println("<" + rest + "> was not found in the restaurant table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	//***********************************************
	//Get the price of an item to build an order
	//***********************************************
	@Override
	public List<Menu> getPriceOfMenuItem(final String item) {
		return executeTransaction(new Transaction<List<Menu>>() {
			@Override
			public List<Menu> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {


					stmt = conn.prepareStatement(
							"select menu.*  " +
									" from menu "  +
									" where menu_item = ? "  
							);
					stmt.setString(1, item);
					List<Menu> result = new ArrayList<Menu>();
					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					while (resultSet.next()) {
						found = true;

						Menu m = new Menu();
						loadMenu(m, resultSet, 1);
						result.add(m);
					}

					// check if the title was found
					if (!found) {
						System.out.println("<" + item + "> was not found in the restaurant table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	//******************************************
	//build an order with items and prices
	//******************************************
	@Override
	public List<Order> ceateOrderInTable(final int patId, final int orderNum, final String item, final Double price) {
		return executeTransaction(new Transaction<List<Order>>() {
			@Override
			public List<Order> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement(
							" insert into orders(patron_id, order_number, item, price) " +
									" values(?, ?, ?, ?) "
							);
					stmt.setInt(1, patId);
					stmt.setInt(2, orderNum);
					stmt.setString(3, item);
					stmt.setDouble(4, price);
					stmt.executeUpdate();
					
					stmt2 = conn.prepareStatement(
							" select * from orders " +
									" where order_number = ? " +
									" and patron_id = ? "
							);
					stmt2.setString(1, item);
					
					resultSet = stmt2.executeQuery();

					// for testing that a result was returned
					Boolean found = false;
					List<Order> result = new ArrayList<Order>();
					while (resultSet.next()) {
						found = true;
						Order o = new Order();
						loadOrder(o, resultSet, 1);
						result.add(o);
					}

					// check if the title was found
					if (!found) {
						System.out.println("<" + item + "> was not found in the menu table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}



	

	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}

	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();

		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;

			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}

			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}

			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}

	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:derby:H:/workspace.newDBarea;create=true");

		// Set autocommit to false to allow multiple the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);

		return conn;
	}

	private void loadUser(User user, ResultSet resultSet, int index) throws SQLException {
		user.setUserId(resultSet.getInt(index++));
		user.setUserName(resultSet.getString(index++));
		user.setPassWord(resultSet.getString(index++));
		user.setEmail(resultSet.getString(index++));
		user.setAccountType(resultSet.getString(index++));
		user.setFirstName(resultSet.getString(index++));
		user.setLastName(resultSet.getString(index++));
	}

	private void loadRestaurant(Restaurant rest, ResultSet resultSet, int index) throws SQLException {
		rest.setRestID(resultSet.getInt(index++));
		rest.setUserId(resultSet.getInt(index++));
		rest.setName(resultSet.getString(index++));
		rest.setAddress(resultSet.getString(index++));
		rest.setCity(resultSet.getString(index++));
		rest.setZipCode(resultSet.getString(index++));
	}
	
	private void loadMenu(Menu m, ResultSet resultSet, int index) throws SQLException {
		m.setMenuId(resultSet.getInt(index++));
		m.setRestId(resultSet.getInt(index++));
		m.setItem(resultSet.getString(index++));
		m.setPrice(resultSet.getDouble(index++));
	}
	private void loadOrder(Order o, ResultSet resultSet, int index) throws SQLException {
		o.setOrderId(resultSet.getInt(index++));
		o.setPatronId(resultSet.getInt(index++));
		o.setorderNumber(resultSet.getInt(index++));
		o.setItem(resultSet.getString(index++));
		o.setPrice(resultSet.getDouble(index++));
	}



	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;

				try {
					stmt1 = conn.prepareStatement(
							"create table users (" +
									"	user_id integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +									
									"	user_userName varchar(40),"     +
									"	user_passWord varchar(40), "     +
									"   user_email varchar(40), "        +
									"   user_accountType varchar(30), " +
									"    user_firstName varchar(50), "  +
									"    user_lastNAme varchar(50) "    +
									")"
							);	
					stmt1.executeUpdate();


					stmt2 = conn.prepareStatement(
							"create table restaurants (" +
									"	rest_id integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +
									"	user_id integer constraint user_id references users, " +
									"	rest_name varchar(40),"    +
									"	rest_address varchar(90)," +
									"   rest_city varchar(30),"	   +
									"   rest_zipcode varchar(10)"  +
									")"
							);
					stmt2.executeUpdate();
					
					stmt3 = conn.prepareStatement(
							" create table menu (" +
									" menu_id integer primary key " +
									" 		generated always as identity (start with 1, increment by 1), " +
									" rest_id integer, "   +
									" menu_item varchar(40), "      +
									" menu_price double"      +
									")"
							);
					stmt3.executeUpdate();
					
					stmt4 = conn.prepareStatement(
							" create table orders (" +
									" order_id integer primary key " +
									" 		generated always as identity (start with 1, increment by 1), " +
									" patron_id integer, "    +
									" order_number integer, " +
									" item varchar(40), "		  +
									" price double"		  +
									")"
							);
					stmt4.executeUpdate();


					return true;
				} finally {
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
				}
			}
		});
	}

	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				List<Restaurant> restList;
				List<User> userList;
				List<Menu> menuList;
				//initial data for order will be built when an order is made
				try {
					restList = InitialData.getRestaurants();
					userList = InitialData.getUsers();
					menuList = InitialData.getMenus();
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				PreparedStatement insertRestaurants = null;
				PreparedStatement insertUsers = null;
				PreparedStatement insertMenus = null;
				try {


					insertUsers = conn.prepareStatement("insert into users (user_userName, user_passWord, user_email, user_accountType, user_firstName, user_Lastname) "
							+ "		values (?, ?, ?, ?, ?, ?)");
					for (User u : userList) {
						//						insertUsers.setInt(1, u.getUserId());
						insertUsers.setString(1, u.getUserName());
						insertUsers.setString(2, u.getPassWord());
						insertUsers.setString(3, u.getEmail());
						insertUsers.setString(4, u.getAccountType());
						insertUsers.setString(5, u.getFirstName());
						insertUsers.setString(6, u.getLastName());
						insertUsers.addBatch();
					}
					insertUsers.executeBatch();
					System.out.println("Users table populated");


					insertRestaurants = conn.prepareStatement("insert into restaurants (user_id, rest_name, rest_address, rest_city, rest_zipcode) "
							+ "	values (?, ?, ?, ?, ?)");
					for (Restaurant rest: restList) {
						insertRestaurants.setInt(1, rest.getUserId());
						insertRestaurants.setString(2,rest.getName());
						insertRestaurants.setString(3, rest.getAddress());
						insertRestaurants.setString(4, rest.getCity());
						insertRestaurants.setString(5, rest.getZipCode());
						insertRestaurants.addBatch();
					}
					insertRestaurants.executeBatch();
					System.out.println("restaurants table populated");
					
					
					insertMenus = conn.prepareStatement("insert into menu (rest_id, menu_item, menu_price) " + "values (?, ?, ?)");
					for (Menu m : menuList) {
						insertMenus.setInt(1, m.getRestId());
						insertMenus.setString(2, m.getItem());
						insertMenus.setDouble(3, m.getPrice());
						insertMenus.addBatch();
					}
					insertMenus.executeBatch();
					System.out.println("menu created");

					return true;
				} finally {
					DBUtil.closeQuietly(insertUsers);
					DBUtil.closeQuietly(insertRestaurants);
					DBUtil.closeQuietly(insertMenus);
					
					
				}
			}
		});
	}

	// The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		DerbyDatabase db = new DerbyDatabase();
		db.createTables();
		System.out.println("Loading initial data...");
		db.loadInitialData();
		System.out.println("loaded intial data");
		System.out.println("dropped again loser");
	}

}