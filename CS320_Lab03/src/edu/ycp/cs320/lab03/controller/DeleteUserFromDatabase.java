package edu.ycp.cs320.lab03.controller;

import java.util.Scanner;

import edu.ycp.cs320.lab03.DBpersist.DatabaseProvider;
import edu.ycp.cs320.lab03.DBpersist.DerbyDatabase;
import edu.ycp.cs320.lab03.DBpersist.IDatabase;

public class DeleteUserFromDatabase {
	private IDatabase db = null;
	public DeleteUserFromDatabase() {
		
		// Create the default IDatabase instance
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	
	}
	public void DeleteUser(String name, String pswd){
		
		db.DeleteUserFromDatabase(name, pswd);
	}
}
