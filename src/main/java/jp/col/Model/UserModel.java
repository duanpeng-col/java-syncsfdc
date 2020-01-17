package jp.col.Model;

public class UserModel {
 
    private String id;
    private String email;
    private String password;
    private String userName;
 
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public String getUserName() {
        return userName;
    }

	public void setEmail(String email) {
        this.email = email;
    }
 
    public String getEmail() {
        return email;
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