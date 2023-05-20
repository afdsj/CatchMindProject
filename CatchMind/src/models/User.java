package models;

public class User {
	
	private int id; //private : 해당 클래스에서만 접근 허용, 필드 : id
	private String userName; // 필드 : userName
	private String password; // 필드 : password
	
	// public : 모든 패키지 접근 허용 User : 생성자 , (필드 초기화)
	public User(int id, String userName, String password) {
		// 레퍼런스 변수 : id, userName, password
		this.id = id;
		this.userName = userName;
		this.password = password;
	}
	
	public User(String userName, String password) {
		// 레퍼런스 변수 : userName, password
		this.userName = userName;
		this.password = password;
	}
	
	public User(String userName) {
		// 레퍼런스 변수 : userName
		this.userName = userName;
	}
	
	@Override
	public String toString() {
		return id + ":" + userName + ":" + password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
