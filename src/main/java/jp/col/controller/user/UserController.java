
package jp.col.controller.user;

import jp.col.Model.UserModel;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class UserController {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  @RequestMapping("/login")
  String login(UserModel user,Map<String, Object> model,HttpSession ses) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("select id,password__c,email__c,name,gender__c,age__c from sfdc.OS_EmployeeInfo__c where email__c = '" + user.getEmail().replace("'", "''") + "' and password__c='" + user.getPassword().replace("'", "''") + "'");
      if (rs.next()) {
        user.setUserName(rs.getString("name"));
        ses.setAttribute("UserName", user.getUserName());
        return "index";
      }else {
      	model.put("message", "error password!");
        return "login";
      }
	
    } catch (Exception e) {
      	model.put("message", e.toString());
      return "error";
    }
  }
}
