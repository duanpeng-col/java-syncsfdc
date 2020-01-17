
package jp.col.controller.lead;

import jp.col.Model.LeadModel;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Controller
public class LeadController {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  @RequestMapping("/newlead")
  String newlead(LeadModel model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("INSERT INTO Lead(lastname,firstname,email) VALUES ('" + model.getLastName() + "','" + model.getFirstName() + "','" + model.getEmail() + "')");

      return "newlead";
    } catch (Exception e) {
      return "error";
    }
  }

  @RequestMapping("/leadlist")
  String leadlist(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT id,sfid,name,lastname,firstname,phone,postalcode,company,email,description FROM salesforce.lead order by id");

      ArrayList<LeadModel> leadList = new ArrayList<LeadModel>();
      while (rs.next()) {
        LeadModel lead = new LeadModel();
        lead.setId(rs.getString("id"));
        lead.setSfid(rs.getString("sfid"));
        lead.setName(rs.getString("name"));
        lead.setLastName(rs.getString("lastname"));
        lead.setFirstName(rs.getString("firstname"));
        lead.setPhone(rs.getString("phone"));
        lead.setPostal(rs.getString("postalcode"));
        lead.setCompany(rs.getString("company"));
        lead.setEmail(rs.getString("email"));
        lead.setDescription(rs.getString("description"));
        leadList.add(lead);
      }

      model.put("records", leadList);
      return "leadlist";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/leaddetail")
  String leaddetail(Map<String, Object> model , HttpServletRequest req) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String id = req.getParameter("id");
      ResultSet rs = stmt.executeQuery("SELECT id,sfid,name,lastname,firstname,phone,postalcode,company,email,description FROM salesforce.lead where id=" + id );
      LeadModel lead = new LeadModel();
      if (rs.next()) {
        lead.setId(rs.getString("id"));
        lead.setSfid(rs.getString("sfid"));
        lead.setName(rs.getString("name"));
        lead.setLastName(rs.getString("lastname"));
        lead.setFirstName(rs.getString("firstname"));
        lead.setPhone(rs.getString("phone"));
        lead.setPostal(rs.getString("postalcode"));
        lead.setCompany(rs.getString("company"));
        lead.setEmail(rs.getString("email"));
        lead.setDescription(rs.getString("description"));
      	model.put("record", lead);
        return "leaddetail";
      } else {
        model.put("message", "data not found");
        return "error";
      }

    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/leadsave")
  String leadsave(LeadModel lead , Map<String, Object> model,HttpSession ses) {
  	Object userName = ses.getAttribute("UserName");
  	if(userName == null) {
      model.put("message", "登録してください。");
      return "login";
  	}
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("UPDATE salesforce.Lead set name='" +lead.getName()+"',lastname='"+lead.getLastName()+"',firstname='"+lead.getFirstName()+"',email='"+lead.getEmail()+"',phone='"+lead.getPhone()+"',company='"+lead.getCompany()+"',postalcode='"+lead.getPostal()+"',description='"+lead.getDescription()+"',HerokuModifiedBy__c='"+userName.toString()+"'  WHERE ID=" + lead.getId());
      return "redirect:leadlist";

    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }
}
