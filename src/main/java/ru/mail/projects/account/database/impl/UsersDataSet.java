package ru.mail.projects.account.database.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class UsersDataSet {

	@Id
	@Column(name="idUsers")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="Name")
	private String Name;
	
	@Column(name = "GameNum")
	private int GameNum;

	@Column(name = "WinNum")
	private int WinNum;
	
	@Column(name = "LostNum")
	private int LostNum;
	
	public UsersDataSet (int id, String name) {
		
		this.id = id;
		this.Name = name;
	}
	
	public UsersDataSet(String name) {
		
		this.id = -1; 
		this.Name = name; 
	}

	public String getName() {
		
		return Name;
	}
	
	public int getId() {
		
		return id;
	}

}
