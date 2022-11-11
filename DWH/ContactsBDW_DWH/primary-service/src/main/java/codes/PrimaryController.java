package codes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.*;

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
import tables.Types;
import tables.TypesDao;
import tables.Brands;
import tables.BrandsDao;
import tables.Users;
import tables.UsersDao;

@RestController
public class PrimaryController {
	
	String globalPermitLevel = "error";// error - no permission
	String globalUserId = "error";// error - no user
	Short gDatasetActive = 1;// global active dataset
	Short gDatasetArchive = 0;// global archive dataset
	
	String[] strArray = {"Единица","Комплект","Метр","М_погон","Кг","Коробка","Прочее"};
    List<String> listMeasure = Arrays.asList(strArray);

    @GetMapping("/api/types/{last_download_str}")
    public String getTypes(Model model, @PathVariable String last_download_str, @RequestBody String body_str){
    	List < Types > listTypes;
    	String strTypes = "";
    	String strJson = "";
    	String returnString = "";
    	JSONArray arrJson = new JSONArray(); 
   		Transaction transaction = null;
   		//LocalDateTime l_d = LocalDateTime.parse(last_download_str);
   		//String str_dop = "1986-04-08 12:30:12";
   		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   		LocalDateTime l_d = LocalDateTime.parse(last_download_str, formatter);
   		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
   			transaction = session.beginTransaction();
   			
   	    	// Authentication
   	   		Object body_obj=JSONValue.parse(body_str);
   	   		String pswd = (String) ((HashMap) body_obj).get("pswd");
   	   		String user_id = (String) ((HashMap) body_obj).get("user_id");
   	    	//System.out.println("After get********: user_id -" + user_id);
   	    	
   	    	Users users = new Users();
   	        users.setUser_id(user_id);
   	        users.setPswd(pswd);
   	        UsersDao uDao = new UsersDao();
   			globalPermitLevel = uDao.loginUsers(users).trim();
   			if (globalPermitLevel != "error") {globalUserId = user_id;
   			//System.out.println(":"+globalPermitLevel+":error:"+globalUserId);
   			
   			String str = "from Types where last_changed > :l_d order by id";// and last_changed > :l_d
   			//System.out.print("!!!"+str);
   			//listTypes = session.createSQLQuery(str).getResultList();
   			listTypes = session.createQuery(str, Types.class)
   					.setParameter("l_d", l_d)
   					.getResultList();
   			//strJson = new Gson().toJson(listTypes);
   			System.out.print("!72!");
   			for(Types l : listTypes) {
   				////System.out.println("888+ List of Types::"+l.toString());
   				strTypes = strTypes + l.toString();
   				JSONObject obj=new JSONObject();    
   					obj.put("id",l.getId());
   					obj.put("type_name",l.getType_name());    
   					obj.put("description",l.getDescription());    
   					obj.put("date_create",l.getDate_create().toString());  
   					obj.put("dataset",l.getDataset());
   					obj.put("last_user",l.getLast_user());
   					obj.put("last_changed",l.getLast_changed().toString());
   					arrJson.add(obj);
   				//System.out.print(obj);
   			}
   			
   			strJson = arrJson.toString();
   	   		byte[] bytes = strJson.getBytes(StandardCharsets.UTF_8);
   	   		returnString = new String(bytes, StandardCharsets.UTF_8);
   	   		assertEquals(strJson, returnString);
   	   		//System.out.println("After get********");
   	        System.out.print(returnString);
   			
   			} //(globalPermitLevel != "error")
   			else { returnString = "Error of authentication! Recheck user_id and pswd (fields names and values)!";}
   			transaction.commit();
              } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
   		return returnString;// 
   	}

//***************************************************
    
    @GetMapping("/api/brands/{last_download_str}")
    public String getBrands(Model model, @PathVariable String last_download_str, @RequestBody String body_str){
    	List < Brands > listBrands;
    	String strBrands = "";
    	String strJson = "";
    	String returnString = "";
    	JSONArray arrJson = new JSONArray(); 
   		Transaction transaction = null;
   		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
   			transaction = session.beginTransaction();
   		
    	// Authentication
   		Object body_obj=JSONValue.parse(body_str);
   		String pswd = (String) ((HashMap) body_obj).get("pswd");
   		String user_id = (String) ((HashMap) body_obj).get("user_id");
    	//System.out.println("After get********: user_id -" + user_id);
    	
    	Users users = new Users();
        users.setUser_id(user_id);
        users.setPswd(pswd);
        UsersDao uDao = new UsersDao();
		globalPermitLevel = uDao.loginUsers(users).trim();
		if (globalPermitLevel != "error") {globalUserId = user_id;
		//System.out.println(":"+globalPermitLevel+":error:"+globalUserId);
		
   		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   		LocalDateTime l_d = LocalDateTime.parse(last_download_str, formatter);
   		
   			String str = "from Brands where last_changed > :l_d order by id";// and last_changed > :l_d
   			//System.out.print("!!!"+str);
   			//listTypes = session.createSQLQuery(str).getResultList();
   			listBrands = session.createQuery(str, Brands.class)
   					.setParameter("l_d", l_d)
   					.getResultList();
   			//strJson = new Gson().toJson(listTypes);
   			//System.out.print("!72!");
   			for(Brands l : listBrands) {
   				////System.out.println("888+ List of Types::"+l.toString());
   				strBrands = strBrands + l.toString();
   				JSONObject obj=new JSONObject();    
   					obj.put("id",l.getId());
   					obj.put("brand_name",l.getBrand_name());    
   					obj.put("description",l.getDescription());    
   					obj.put("date_create",l.getDate_create().toString());  
   					obj.put("dataset",l.getDataset());
   					obj.put("last_user",l.getLast_user());
   					obj.put("last_changed",l.getLast_changed().toString());
   					arrJson.add(obj);
   				//System.out.print(obj);
   			}
   			
   		// from Json array into string
   		strJson = arrJson.toString();
   		byte[] bytes = strJson.getBytes(StandardCharsets.UTF_8);
   		returnString = new String(bytes, StandardCharsets.UTF_8);
   		assertEquals(strJson, returnString);
   		//System.out.println("After get********");
        System.out.print(returnString);
   		 
		} //(globalPermitLevel != "error")
		else { returnString = "Error of authentication! Recheck user_id and pswd (fields names and values)!";}
		transaction.commit();
		 } catch (Exception e) {
	            if (transaction != null) {
	            	//System.out.println(e);
	                transaction.rollback();
	            }
	        }
		return returnString;//
   	}// public String getBrands
}
