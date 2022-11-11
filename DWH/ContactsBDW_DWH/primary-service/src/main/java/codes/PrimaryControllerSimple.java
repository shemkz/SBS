package codes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


import static org.junit.jupiter.api.Assertions.assertEquals;

//import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

import codes.HibernateUtil;
import tables.Users;
import tables.UsersDao;
import tables.Log_dwh;
import tables.Log_dwhDao;

@RestController
public class PrimaryControllerSimple {
	String globalPermitLevel = "error";// error - no permission
	String globalUserId = "error";// error - no user
	Short gDatasetActive = 1;// global active dataset
	Short gDatasetArchive = 0;// global archive dataset
	
	String[] strArray = {"Единица","Комплект","Метр","М_погон","Кг","Коробка","Прочее"};
    List<String> listMeasure = Arrays.asList(strArray);
	
	// Data received in String format       
    @PostMapping("/api/log_dwh")
    public String postLog_dwh(Model model, @RequestBody String body_str) {
    	String returnString = "";
    	String table_name = "";
    	String id1 = "";
    	Transaction transaction = null;
    	try (Session session = HibernateUtil.getSessionFactory().openSession()) {

   			transaction = session.beginTransaction();
   			
   	    	// Authentication
   	   		Object body_obj=JSONValue.parse(body_str);
   	   		String pswd = (String) ((HashMap) body_obj).get("pswd");
   	   		String user_id = (String) ((HashMap) body_obj).get("user_id");
   	   		table_name = (String) ((HashMap) body_obj).get("table_name");
   	    	//System.out.println("After get********: user_id -" + user_id);
   	    	
   	    	Users users = new Users();
   	        users.setUser_id(user_id);
   	        users.setPswd(pswd);
   	        UsersDao uDao = new UsersDao();
   			globalPermitLevel = uDao.loginUsers(users).trim();
   			if (globalPermitLevel != "error") {globalUserId = user_id;
   			//System.out.println(":"+globalPermitLevel+":error:"+globalUserId);
   			
    	Log_dwh log_dwh = new Log_dwh();
    	LocalDate dt =  LocalDate.now();
    	LocalDateTime dtt = LocalDateTime.now();
    	log_dwh.setDate_create(dt);
    	log_dwh.setActions("Downloaded table " + table_name);
    	log_dwh.setDataset(gDatasetActive);
    	log_dwh.setLast_user(globalUserId);
    	log_dwh.setLast_changed(dtt);
    	Log_dwhDao sDao = new Log_dwhDao();
    	id1 = Long. toString(sDao.insertLog_dwh(log_dwh));
    	returnString = "New record in log_dwh (";// + table_name + "). Id: " + id1;
    	//System.out.println(returnString);
   			} //(globalPermitLevel != "error")
   			else { returnString = "Error of authentication! Recheck user_id and pswd (fields names and values)!";}
   			transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
        return returnString;
    }
}
