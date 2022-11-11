package codes;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

//import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import tables.Customers;
import tables.CustomersDao;
import tables.Customers_deals;
import tables.Customers_dealsDao;
import tables.Products;
import tables.ProductsDao;
import tables.Users;
import tables.UsersDao;


@RestController
public class SecondaryController {
	
	String globalPermitLevel = "error";// error - no permission
	String globalUserId = "error";// error - no user
	Short gDatasetActive = 1;// global active dataset
	Short gDatasetArchive = 0;// global archive dataset
	
	String[] strArray = {"Единица","Комплект","Метр","М_погон","Кг","Коробка","Прочее"};
    List<String> listMeasure = Arrays.asList(strArray);
   
    @GetMapping("/api/customers/{last_download_str}")
    public String getCustomers(Model model, @PathVariable String last_download_str, @RequestBody String body_str){
    	List < Customers > listCustomers;
    	String strCustomers = "";
    	String strJson = "";
    	String returnString = "";
    	JSONArray arrJson = new JSONArray(); 
   		Transaction transaction = null;
   		//LocalDateTime l_d = LocalDateTime.parse(last_download_str);
   		//last_download_str = "1986-04-08 12:30:12";
   		//System.out.print("!67!");
   		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   		LocalDateTime l_d = LocalDateTime.parse(last_download_str, formatter);
   		System.out.print("!69!");
   		
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
   			
   			String str = "from Customers where last_changed > :l_d order by id";// and last_changed > :l_d
   			//System.out.print("!!!"+str);
   			
   			//listTypes = session.createSQLQuery(str).getResultList();
   			listCustomers = session.createQuery(str, Customers.class)
   					.setParameter("l_d", l_d)
   					.getResultList();
   			//strJson = new Gson().toJson(listTypes);
   			System.out.print("!72!");
   			for(Customers l : listCustomers) {
   				//System.out.println("888+ List of Types::"+l.toString());
   				strCustomers = strCustomers + l.toString();
   				JSONObject obj=new JSONObject();    
   					obj.put("id",l.getId());
   					obj.put("type_name",l.getType_name());  
   					obj.put("first_name",l.getFirst_name());
   					obj.put("middle_name",l.getMiddle_name());
   					obj.put("last_name",l.getLast_name());
   					
   					obj.put("TIN",l.getTIN());
   					obj.put("mobile_p",l.getMobile_p());
   					obj.put("station_p",l.getStation_p());
   					obj.put("address",l.getAddress());
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
   	        
   			transaction.commit();
   			} //(globalPermitLevel != "error")
   			else { returnString = "Error of authentication! Recheck user_id and pswd (fields names and values)!";}
   			    
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
   		return returnString;// 
   	}
//*****************
    
    @GetMapping("/api/customers_deals/{last_download_str}")
    public String getCustomers_deals(Model model, @PathVariable String last_download_str, @RequestBody String body_str){
    	List < Customers_deals > listCustomers_deals;
    	String strCustomers_deals = "";
    	String strJson = "";
    	String returnString = "";
    	JSONArray arrJson = new JSONArray(); 
   		Transaction transaction = null;
   		//LocalDateTime l_d = LocalDateTime.parse(last_download_str);
   		//last_download_str = "1986-04-08 12:30:12";
   		//System.out.print("!67!");
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
   			
   			String str = "from Customers_deals where last_changed > :l_d order by id";// and last_changed > :l_d
   			
   			//listTypes = session.createSQLQuery(str).getResultList();
   			listCustomers_deals = session.createQuery(str, Customers_deals.class)
   					.setParameter("l_d", l_d)
   					.getResultList();
   			//strJson = new Gson().toJson(listTypes);
   			for(Customers_deals l : listCustomers_deals) {
   				//System.out.println("888+ List of Types::"+l.toString());
   				strCustomers_deals = strCustomers_deals + l.toString();
   				JSONObject obj=new JSONObject();    
   					obj.put("id",l.getId());
   					obj.put("id_customer",l.getId_customer());  
   					obj.put("name_good",l.getName_good());
   					obj.put("price",l.getPrice());
   					obj.put("quantity",l.getQuantity());
   					
   					obj.put("sum",l.getSum());
   					obj.put("date_deal",l.getDate_deal().toString());
   					obj.put("details",l.getDetails());    
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
//***********************    

    @GetMapping("/api/products/{last_download_str}")
    public String getProducts(Model model, @PathVariable String last_download_str, @RequestBody String body_str){
    	List < Products > listProducts;
    	String strProducts = "";
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
   			
   			String str = "from Products where last_changed > :l_d order by id";// and last_changed > :l_d
   			//listTypes = session.createSQLQuery(str).getResultList();
   			listProducts = session.createQuery(str, Products.class)
   					.setParameter("l_d", l_d)
   					.getResultList();
   			//strJson = new Gson().toJson(listTypes);
   			for(Products l : listProducts) {
   				////System.out.println("888+ List of Types::"+l.toString());
   				strProducts = strProducts + l.toString();
   				JSONObject obj=new JSONObject();    
   					obj.put("id",l.getId());
   					obj.put("type_name",l.getType_name());  
   					obj.put("brand_name",l.getBrand_name());
   					obj.put("product_name",l.getProduct_name());
   					obj.put("measure",l.getMeasure());
   					
   					obj.put("price",l.getPrice());
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
}

