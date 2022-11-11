package net.codes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import net.codes.HibernateUtil;

import net.tables.Suppliers;
import net.tables.SuppliersDao;
import net.tables.Types;
import net.tables.TypesDao;
import net.tables.Users;
import net.tables.UsersDao;
import net.tables.Suppliers_deals;
import net.tables.Suppliers_dealsDao;
import net.tables.Customers;
import net.tables.CustomersDao;
import net.tables.Customers_deals;
import net.tables.Customers_dealsDao;
import net.tables.Permission_level;
import net.tables.Brands;
import net.tables.BrandsDao;
import net.tables.Products;
import net.tables.ProductsDao;
import net.tables.Stock;
import net.tables.StockDao;

import net.views.Customers_by_sum;
import net.views.Customers_deals_by_sum;
import net.views.Suppliers_by_sum;
import net.views.Suppliers_deals_by_sum;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
 
@Controller
public class MvcController {
	String globalPermitLevel = "error";// error - no permission
	String globalUserId = "error";// error - no user
	Short gDatasetActive = 1;// global active dataset
	Short gDatasetArchive = 0;// global archive dataset
	
	String[] strArray = {"Единица","Комплект","Метр","М_погон","Кг","Коробка","Прочее"};
    List<String> listMeasure = Arrays.asList(strArray);
     
    @RequestMapping("/")
    public String home(Model model) {
        //System.out.println("Going home...");
       
        return "index";
    }
    
    //***********
    
    @RequestMapping("/index_login")
    public String index_login(Model model, String p1, String p2) {
        
        Users users = new Users();
        users.setUser_id(p1);
        users.setPswd(p2);
        UsersDao uDao = new UsersDao();
		globalPermitLevel = uDao.loginUsers(users).trim();
		if (globalPermitLevel != "error") {globalUserId = p1;}
		//System.out.println(":"+globalPermitLevel+":error:"+globalUserId);
		model.addAttribute("permitLevel", globalPermitLevel);
        return "index_login";
    }
    
//************************************
    
    @GetMapping("/suppliers")
   	public String showSuppliers(Model model) {
    if (globalUserId != "error" && globalPermitLevel != "error") {
       	List < Suppliers > listSuppliers;
       	List < Types > listTypes;
   		Transaction transaction = null;
   		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

   			transaction = session.beginTransaction();
   			listSuppliers = session.createQuery("from Suppliers where dataset = " + gDatasetActive + " order by id", Suppliers.class).getResultList();
   			listTypes = session.createQuery("from Types where dataset = " + gDatasetActive + " order by id", Types.class).getResultList();
   			for(Types l : listTypes) {
   				//System.out.println("888+ List of Types::"+l.toString());
   			}

   			model.addAttribute("listSuppliers", listSuppliers);
   			model.addAttribute("listTypes", listTypes);
   			model.addAttribute("globalPermitLevel", globalPermitLevel);
   			model.addAttribute("globalUserId", globalUserId);
   			transaction.commit();
            //System.out.println("After update");
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
   		
   		return "suppliers";
    } else { return "index";
    }
   	}
    
//*************
    
    @RequestMapping("/suppliers_save")
    public String suppliers_save(String p1, String p2, String p3, String p4, String p5,
    								String p6, String p7, String p8, String p9, String p10) {//, String param2

    	//System.out.println("*!*"+p1+":"+p2+":"+p3+":"+p4+":");
    	Suppliers suppliers = new Suppliers(Integer.parseInt(p1),p2,p3,p4,p5,p6,p7,p8,p9,p10,globalUserId);
    	SuppliersDao sDao = new SuppliersDao();
		sDao.updateSuppliers(suppliers);
		
        return "suppliers_save";
    }
//*************    
    
    @RequestMapping("/suppliers_insert")
    public String suppliers_insert(Model model, String p1) {//, String param

    	Suppliers suppliers = new Suppliers();
    	LocalDate dt =  LocalDate.now();
    	LocalDateTime dtt = LocalDateTime.now();
    	suppliers.setDate_create(dt);
    	suppliers.setType_name(p1);
    	suppliers.setDataset(gDatasetActive);
    	suppliers.setLast_user(globalUserId);
    	suppliers.setLast_changed(dtt);
    	SuppliersDao sDao = new SuppliersDao();
		int id1 = (int) sDao.insertSuppliers(suppliers);
		//System.out.println("***:");
		//System.out.println(id1);
		model.addAttribute("id1", id1);
		
        return "suppliers_insert";
    }    
    
//************* 
    
    @RequestMapping("/suppliers_delete")
    public String suppliers_delete(String p1, String p2) {//, String param

    	Suppliers suppliers = new Suppliers();
    	suppliers.setId(Integer.parseInt(p1));
    	suppliers.setFirst_name(p2);
    	suppliers.setDataset(gDatasetArchive);
    	suppliers.setLast_user(globalUserId);
    	SuppliersDao sDao = new SuppliersDao();
		sDao.deleteSuppliers(suppliers);
		////System.out.println("***:");

        return "suppliers_delete";
    }    
    
//*************************************************

    @GetMapping("/suppliers_deals")
   	public String showSuppliers_deals(Model model, String pId) {
    if (globalUserId != "error" && globalPermitLevel != "error") {
       	List < Suppliers_deals > listSuppliers_deals;
   		Transaction transaction = null;
   		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
               // start the transaction
   			transaction = session.beginTransaction();
            listSuppliers_deals = session.createQuery("from Suppliers_deals where dataset = " + gDatasetActive + " and id_supplier = " + (String) pId
            		+ " order by id", Suppliers_deals.class).getResultList();
   		
            Suppliers suppliers = new Suppliers();
            suppliers = session.createQuery("from Suppliers where dataset = " + gDatasetActive + " and id = " + (String) pId, Suppliers.class).getSingleResult();
            String nameSupplier = suppliers.getLast_name() + " " + suppliers.getFirst_name() + " " + suppliers.getMiddle_name();
            String typeSupplier = suppliers.getType_name();
            
            for(Suppliers_deals l : listSuppliers_deals) {
   		    //System.out.println("888+ List of Suppliers_deals::"+l.toString());
   		 	}
            
            model.addAttribute("typeSupplier", typeSupplier);
            model.addAttribute("nameSupplier", nameSupplier);
   		 	model.addAttribute("listSuppliers_deals", listSuppliers_deals);
   		 	model.addAttribute("pId", pId);
   		 	model.addAttribute("globalPermitLevel", globalPermitLevel);
			model.addAttribute("globalUserId", globalUserId);
   		 	transaction.commit();
            //System.out.println("After update");
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
   		
   		return "suppliers_deals";
    } else { return "index";
    }
   	}    
    
  //*************
    
    @RequestMapping("/suppliers_deals_save")
    public String suppliers_deals_save(String p1, String p2, String p3, String p4, String p5,
    								String p6, String p7, String p8) {//, String param2

    	//System.out.println("*!*"+p1+":"+p2+":"+p3+":"+p8+":");
    	
    	Suppliers_deals suppliers_deals = new Suppliers_deals(Integer.parseInt(p1),Integer.parseInt(p2),p3,Long.parseLong(p4),
    											Integer.parseInt(p5),Long.parseLong(p6),p7,gDatasetActive,globalUserId);
    	Suppliers_dealsDao sDao = new Suppliers_dealsDao();
		sDao.updateSuppliers_deals(suppliers_deals);
		
        return "suppliers_deals_save";
    }
    
  //************* 
    
    @RequestMapping("/suppliers_deals_insert")
    public String suppliers_deals_insert(Model model, String p1) {//, String param

    	Suppliers_deals suppliers_deals = new Suppliers_deals();
    	suppliers_deals.setId_supplier(Integer.parseInt(p1));
    	LocalDate dt =  LocalDate.now();
    	LocalDateTime dtt = LocalDateTime.now();
    	suppliers_deals.setPrice((long) 0);
    	suppliers_deals.setQuantity(0);
    	suppliers_deals.setSum((long) 0);
    	suppliers_deals.setDate_deal(dt);
    	suppliers_deals.setDataset(gDatasetActive);
    	suppliers_deals.setLast_user(globalUserId);
    	suppliers_deals.setLast_changed(dtt);
    	
    	Suppliers_dealsDao sdDao = new Suppliers_dealsDao();
		int id1 = (int) sdDao.insertSuppliers_deals(suppliers_deals);
		////System.out.println("***:");
		//System.out.println(id1);
		model.addAttribute("id1", id1);
        return "suppliers_deals_insert";
    }    
 
  //************* 
    
    @RequestMapping("/suppliers_deals_delete")
    public String suppliers_deals_delete(String p1, String p2) {//, String param

    	Suppliers_deals suppliers_deals = new Suppliers_deals();
    	suppliers_deals.setId(Integer.parseInt(p1));
    	suppliers_deals.setName_good(p2);
    	suppliers_deals.setDataset(gDatasetArchive);
    	suppliers_deals.setLast_user(globalUserId);

    	Suppliers_dealsDao sDao = new Suppliers_dealsDao();
		sDao.deleteSuppliers_deals(suppliers_deals);
		
        return "suppliers_deals_delete";
    } 
//**********************************************************************

    @GetMapping("/customers")
   	public String showCustomers(Model model) {
    if (globalUserId != "error" && globalPermitLevel != "error") {
       	List < Customers > listCustomers;
       	List < Types > listTypes;
   		Transaction transaction = null;
   		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

   			transaction = session.beginTransaction();
   			listCustomers = session.createQuery("from Customers where dataset = " + gDatasetActive + " order by id", Customers.class).getResultList();
   			listTypes = session.createQuery("from Types where dataset = " + gDatasetActive + " order by id", Types.class).getResultList();
   			for(Types l : listTypes) {
   				//System.out.println("888+ List of Types::"+l.toString());
   			}

   			model.addAttribute("listCustomers", listCustomers);
   			model.addAttribute("listTypes", listTypes);
   			model.addAttribute("globalPermitLevel", globalPermitLevel);
   			model.addAttribute("globalUserId", globalUserId);
   			transaction.commit();
            //System.out.println("After update");
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
   		
   		return "customers";
    } else { return "index";
    }
   	}
//*************
    
    @RequestMapping("/customers_save")
    public String customers_save(String p1, String p2, String p3, String p4, String p5,
    								String p6, String p7, String p8, String p9, String p10) {//, String param2

    	//System.out.println("*!*"+p1+":"+p2+":"+p3+":"+p4+":");
    	Customers customers = new Customers(Integer.parseInt(p1),p2,p3,p4,p5,p6,p7,p8,p9,p10,globalUserId);
    	CustomersDao sDao = new CustomersDao();
		sDao.updateCustomers(customers);
		
        return "customers_save";
    }
//*************    
    
    @RequestMapping("/customers_insert")
    public String customers_insert(Model model, String p1) {//, String param

    	Customers customers = new Customers();
    	LocalDate dt =  LocalDate.now();
    	LocalDateTime dtt = LocalDateTime.now();
    	customers.setDate_create(dt);
    	customers.setType_name(p1);
    	customers.setDataset(gDatasetActive);
    	customers.setLast_user(globalUserId);
    	customers.setLast_changed(dtt);
    	CustomersDao sDao = new CustomersDao();
		int id1 = (int) sDao.insertCustomers(customers);
		//System.out.println("***:");
		//System.out.println(id1);
		model.addAttribute("id1", id1);
		
        return "customers_insert";
    }    
    
//************* 
    
    @RequestMapping("/customers_delete")
    public String customers_delete(String p1, String p2) {//, String param

    	Customers customers = new Customers();
    	customers.setId(Integer.parseInt(p1));
    	customers.setFirst_name(p2);
    	customers.setDataset(gDatasetArchive);
    	customers.setLast_user(globalUserId);
    	CustomersDao sDao = new CustomersDao();
		sDao.deleteCustomers(customers);
		////System.out.println("***:");

        return "customers_delete";
    }    
    
    //*************************************************************************
    
    @GetMapping("/customers_deals")
   	public String showCustomers_deals(Model model, String pId) {
    if (globalUserId != "error" && globalPermitLevel != "error") {
       	List < Customers_deals > listCustomers_deals;
   		Transaction transaction = null;
   		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
               // start the transaction
   			transaction = session.beginTransaction();
            listCustomers_deals = session.createQuery("from Customers_deals where dataset = " + gDatasetActive + " and id_customer = " + (String) pId + " order by id", Customers_deals.class).getResultList();
   		
            Customers customers = new Customers();
            customers = session.createQuery("from Customers where dataset = " + gDatasetActive + " and id = " + (String) pId, Customers.class).getSingleResult();
            String nameCustomer = customers.getLast_name() + " " + customers.getFirst_name() + " " + customers.getMiddle_name();
            String typeCustomer = customers.getType_name();
            
            for(Customers_deals l : listCustomers_deals) {
   		    //System.out.println("888+ List of Customers_deals::"+l.toString());
   		 	}
            
            model.addAttribute("typeCustomer", typeCustomer);
            model.addAttribute("nameCustomer", nameCustomer);
   		 	model.addAttribute("listCustomers_deals", listCustomers_deals);
   		 	model.addAttribute("pId", pId);
   		 	model.addAttribute("globalPermitLevel", globalPermitLevel);
			model.addAttribute("globalUserId", globalUserId);
   		 	transaction.commit();
            //System.out.println("After update");
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
   		
   		return "customers_deals";
    } else { return "index";
    }
   	}    
    
  //*************
    
    @RequestMapping("/customers_deals_save")
    public String customers_deals_save(String p1, String p2, String p3, String p4, String p5,
    								String p6, String p7, String p8) {//, String param2

    	//System.out.println("*!*"+p1+":"+p2+":"+p3+":"+p8+":");
    	Customers_deals customers_deals = new Customers_deals(Integer.parseInt(p1),Integer.parseInt(p2),p3,Long.parseLong(p4),
    															Integer.parseInt(p5),Long.parseLong(p6),p7,gDatasetActive,globalUserId);
    	Customers_dealsDao sDao = new Customers_dealsDao();
		sDao.updateCustomers_deals(customers_deals);
		
        return "customers_deals_save";
    }
    
  //************* 
    
    @RequestMapping("/customers_deals_insert")
    public String customers_deals_insert(Model model, String p1) {//, String param

    	Customers_deals customers_deals = new Customers_deals();
    	customers_deals.setId_customer(Integer.parseInt(p1));
    	LocalDate dt =  LocalDate.now();
    	LocalDateTime dtt = LocalDateTime.now();
    	customers_deals.setPrice((long) 0);
    	customers_deals.setQuantity(0);
    	customers_deals.setSum((long) 0);
    	customers_deals.setDate_deal(dt);
    	customers_deals.setDataset(gDatasetActive);
    	customers_deals.setLast_user(globalUserId);
    	customers_deals.setLast_changed(dtt);
    	
    	Customers_dealsDao sdDao = new Customers_dealsDao();
		int id1 = (int) sdDao.insertCustomers_deals(customers_deals);
		//System.out.println("***:");
		//System.out.println(id1);
		model.addAttribute("id1", id1);
        return "customers_deals_insert";
    }    
 
  //************* 
    
    @RequestMapping("/customers_deals_delete")
    public String customers_deals_delete(String p1, String p2) {//, String param

    	Customers_deals customers_deals = new Customers_deals();
    	customers_deals.setId(Integer.parseInt(p1));
    	customers_deals.setName_good(p2);
    	customers_deals.setDataset(gDatasetArchive);
    	customers_deals.setLast_user(globalUserId);
    	Customers_dealsDao sDao = new Customers_dealsDao();
		sDao.deleteCustomers_deals(customers_deals);
		
        return "customers_deals_delete";
    } 
    
    //************************************************************** 
    //************************************************************** 
    //************************************************************** 

  //**********************************************************************

      @GetMapping("/brands")
     	public String showBrands(Model model) {
      if (globalUserId != "error" && globalPermitLevel != "error") {
         	List < Brands > listBrands;
     		Transaction transaction = null;
     		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

     			transaction = session.beginTransaction();
     			listBrands = session.createQuery("from Brands where dataset = " + gDatasetActive + " order by id", Brands.class).getResultList();
     			for(Brands l : listBrands) {
     				//System.out.println("888+ List of Types::"+l.toString());
     			}

     			model.addAttribute("listBrands", listBrands);
     			model.addAttribute("globalPermitLevel", globalPermitLevel);
     			model.addAttribute("globalUserId", globalUserId);
     			transaction.commit();
              //System.out.println("After update");
          } catch (Exception e) {
              if (transaction != null) {
              	//System.out.println(e);
                  transaction.rollback();
              }
          }
     		
     		return "brands";
      } else { return "index";
      }
     	}
  //*************
      
      @RequestMapping("/brands_save")
      public String brands_save(String p1, String p2, String p3) {//, String param2

      	//System.out.println("*!*"+p1+":"+p2+":"+p3+":");
      	Brands brands = new Brands(Integer.parseInt(p1),p2,p3,globalUserId);
      	BrandsDao sDao = new BrandsDao();
  		sDao.updateBrands(brands);
  		
          return "brands_save";
      }
  //*************    
      
      @RequestMapping("/brands_insert")
      public String brands_insert(Model model, String p1) {//, String param

    	Brands brands = new Brands();
      	LocalDate dt =  LocalDate.now();
      	LocalDateTime dtt = LocalDateTime.now();
      	brands.setDate_create(dt);
      	brands.setBrand_name(p1);
      	brands.setDataset(gDatasetActive);
      	brands.setLast_user(globalUserId);
      	brands.setLast_changed(dtt);
      	BrandsDao sDao = new BrandsDao();
  		int id1 = (int) sDao.insertBrands(brands);
  		//System.out.println("***:");
  		//System.out.println(id1);
  		model.addAttribute("id1", id1);
  		
          return "customers_insert";
      }    
      
  //************* 
      
      @RequestMapping("/brands_delete")
      public String brands_delete(String p1, String p2) {//, String param

    	  Brands brands = new Brands();
    	  brands.setId(Integer.parseInt(p1));
    	  brands.setBrand_name(p2);
    	  brands.setDataset(gDatasetArchive);
    	  brands.setLast_user(globalUserId);
      	BrandsDao sDao = new BrandsDao();
  		sDao.deleteBrands(brands);
  		////System.out.println("***:");

          return "customers_delete";
      }    
      
    //*****************************************
      

      @GetMapping("/products")
     	public String showProducts(Model model) {
      if (globalUserId != "error" && globalPermitLevel != "error") {
         	List < Products > listProducts;
         	List < Types > listTypes;
         	List < Brands > listBrands;
     		Transaction transaction = null;
     		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

     			transaction = session.beginTransaction();
     			listProducts = session.createQuery("from Products where dataset = " + gDatasetActive + " order by id", Products.class).getResultList();
     			listTypes = session.createQuery("from Types where dataset = " + gDatasetActive + " order by id", Types.class).getResultList();
     			listBrands = session.createQuery("from Brands where dataset = " + gDatasetActive + " order by id", Brands.class).getResultList();
     			for(String l : listMeasure) {
     				//System.out.println("888+ List of Brands::"+l.toString());
     			}

     			model.addAttribute("listProducts", listProducts);
     			model.addAttribute("listTypes", listTypes);
     			model.addAttribute("listBrands", listBrands);
     			model.addAttribute("listMeasure", listMeasure);
     			model.addAttribute("globalPermitLevel", globalPermitLevel);
     			model.addAttribute("globalUserId", globalUserId);
     			transaction.commit();
              //System.out.println("After update");
          } catch (Exception e) {
              if (transaction != null) {
              	//System.out.println(e);
                  transaction.rollback();
              }
          }
     		
     		return "products";
      } else { return "index";
      }
     	}
  //*************
      
      @RequestMapping("/products_save")
      public String products_save(String p1, String p2, String p3, String p4, String p5,
      								String p6, String p7) {//, String param2

      	//System.out.println("*!*"+p1+":"+p2+":"+p3+":"+p4+":");
      	Products customers = new Products(Integer.parseInt(p1),p2,p3,p4,p5,Long.parseLong(p6),p7,globalUserId);
      	ProductsDao sDao = new ProductsDao();
  		sDao.updateProducts(customers);
  		
          return "products_save";
      }
  //*************    
      
      @RequestMapping("/products_insert")
      public String products_insert(Model model, String p1, String p2, String p3, String p4) {//, String param

    	  Products products = new Products();
      	LocalDate dt =  LocalDate.now();
      	LocalDateTime dtt = LocalDateTime.now();
      	products.setDate_create(dt);
      	products.setType_name(p1);
      	products.setBrand_name(p2);
      	products.setPrice(Long.parseLong(p3));
      	products.setMeasure(p4);
      	products.setProduct_name("");
      	products.setDataset(gDatasetActive);
      	products.setLast_user(globalUserId);
      	products.setLast_changed(dtt);
      	ProductsDao sDao = new ProductsDao();
  		int id1 = (int) sDao.insertProducts(products);
  		//System.out.println("***:");
  		//System.out.println(id1);
  		model.addAttribute("id1", id1);
  		
          return "products_insert";
      }    
      
  //************* 
      
      @RequestMapping("/products_delete")
      public String products_delete(String p1, String p2) {//, String param

    	  Products products = new Products();
    	  products.setId(Integer.parseInt(p1));
    	  products.setProduct_name(p2);
    	  products.setDataset(gDatasetArchive);
    	  products.setLast_user(globalUserId);
      	ProductsDao sDao = new ProductsDao();
  		sDao.deleteProducts(products);
  		////System.out.println("***:");

          return "products_delete";
      }    
      
    //*************
      
      @GetMapping("/stock")
     	public String showStock(Model model) {
      if (globalUserId != "error" && globalPermitLevel != "error") {
         	List < Stock > listStock;
         	List < Products > listProducts;
     		Transaction transaction = null;
     		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

     			transaction = session.beginTransaction();
     			listStock = session.createQuery("from Stock where dataset = " + gDatasetActive + " order by id", Stock.class).getResultList();
     			listProducts = session.createQuery("from Products where dataset = " + gDatasetActive + " order by id", Products.class).getResultList();
     			for(Products l : listProducts) {
     				//System.out.println("888+ List of Products::"+l.toString());
     			}

     			model.addAttribute("listStock", listStock);
     			model.addAttribute("listProducts", listProducts);
     			model.addAttribute("listMeasure", listMeasure);
     			model.addAttribute("globalPermitLevel", globalPermitLevel);
     			model.addAttribute("globalUserId", globalUserId);
     			transaction.commit();
              //System.out.println("After update");
          } catch (Exception e) {
              if (transaction != null) {
              	//System.out.println(e);
                  transaction.rollback();
              }
          }
     		
     		return "stock";
      } else { return "index";
      }
     	}
      
  //*************
      
      @RequestMapping("/stock_save")
      public String stock_save(String p1, String p2, String p3, String p4, String p5,
      								String p6, String p7) {//, String param2

      	//System.out.println("*!*"+p1+":"+p2+":"+p3+":"+p4+":");
      	Stock stock = new Stock(Integer.parseInt(p1),p2,p3,Long.parseLong(p4),Long.parseLong(p5),Long.parseLong(p6),p7,globalUserId);
      	StockDao sDao = new StockDao();
  		sDao.updateStock(stock);
  		
          return "stock_save";
      }
  //*************    
      
      @RequestMapping("/stock_insert")
      public String stock_insert(Model model, String p1, String p2, String p3, String p4, String p5) {//, String param

    	  Stock stock = new Stock();
      	LocalDate dt =  LocalDate.now();
      	LocalDateTime dtt = LocalDateTime.now();
      	stock.setDate_create(dt);
      	stock.setProduct_name(p1);
      	stock.setMeasure(p2);
      	stock.setPrice(Long.parseLong(p3));
      	stock.setQuantity(Long.parseLong(p4));
      	stock.setAmount(Long.parseLong(p5));
      	
      	stock.setDataset(gDatasetActive);
      	stock.setLast_user(globalUserId);
      	stock.setLast_changed(dtt);
      	StockDao sDao = new StockDao();
  		int id1 = (int) sDao.insertStock(stock);
  		//System.out.println("***:");
  		//System.out.println(id1);
  		model.addAttribute("id1", id1);
  		
          return "stock_insert";
      }    
      
  //************* 
      
      @RequestMapping("/stock_delete")
      public String stock_delete(String p1) {//, String param

    	  Stock stock = new Stock();
      	stock.setId(Integer.parseInt(p1));
      	stock.setDataset(gDatasetArchive);
      	stock.setLast_user(globalUserId);
      	StockDao sDao = new StockDao();
  		sDao.deleteStock(stock);
  		////System.out.println("***:");

          return "stock_delete";
      }    
      
      //**************
    //************************************************************** 
    //************************************************************** 
    //************************************************************** 
    
    @GetMapping("/users")
   	public String showUsers(Model model) {
    if (globalUserId != "error" && globalPermitLevel != "error") {
       	List < Users > listUsers;
       	List < Permission_level > listPermission_level;
   		Transaction transaction = null;
   		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

   			transaction = session.beginTransaction();
   			listUsers = session.createQuery("from Users where dataset = " + gDatasetActive + " order by id", Users.class).getResultList();
   			listPermission_level = session.createQuery("from Permission_level where dataset = " + gDatasetActive + " order by id", Permission_level.class).getResultList();
   			for(Permission_level l : listPermission_level) {
   				//System.out.println("888+ List of Types::"+l.toString());
   			}

   			model.addAttribute("listUsers", listUsers);
   			model.addAttribute("listPermission_level", listPermission_level);
   			model.addAttribute("globalPermitLevel", globalPermitLevel);
   			model.addAttribute("globalUserId", globalUserId);
   			transaction.commit();
            //System.out.println("After update");
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
   		return "users";
    } else { return "index";
    }
   	}
    
//*************
    
    @RequestMapping("/users_save")
    public String users_save(String p1, String p2, String p3, String p4) {//, String param2

    	//System.out.println("*!*"+p1+":"+p2+":"+p3+":"+p4+":");
    	Users users = new Users(Integer.parseInt(p1),p2,p3,p4,globalUserId);
    	UsersDao sDao = new UsersDao();
		sDao.updateUsers(users);
		
        return "users_save";
    }
    
//*************    
    
    @RequestMapping("/users_insert")
    public String users_insert(Model model, String p1, String p2) {//, String param

    	Users users = new Users();
    	LocalDate dt =  LocalDate.now();
    	LocalDateTime dtt = LocalDateTime.now();
    	users.setDate_create(dt);
    	users.setPermit_level(p1);
    	users.setUser_id(p2);
    	users.setDataset(gDatasetActive);
    	users.setLast_user(globalUserId);
    	users.setLast_changed(dtt);
    	UsersDao sDao = new UsersDao();
		int id1 = (int) sDao.insertUsers(users);
		//System.out.println("***:");
		//System.out.println(id1);
		model.addAttribute("id1", id1);
		
        return "users_insert";
    }    
    
//************* 
    
    @RequestMapping("/users_delete")
    public String users_delete(String p1, String p2) {//, String param

    	Users users = new Users();
    	users.setId(Integer.parseInt(p1));
    	users.setUser_id(p2);
    	users.setDataset(gDatasetArchive);
    	users.setLast_user(globalUserId);
    	UsersDao sDao = new UsersDao();
		sDao.deleteUsers(users);
		////System.out.println("***:");

        return "users_delete";
    }    
    
//********************************************************
    
    @RequestMapping("change_password")
    public String change_password(Model model) {
    	 if (globalUserId != "error" && globalPermitLevel != "error") {
		model.addAttribute("UserId", globalUserId);
		model.addAttribute("permitLevel", globalPermitLevel);
    return "change_password";
    	 } else { return "index";
 	    }
    }
    
    //***********
    
    @RequestMapping("/change_password_update")
    public String change_password_update(Model model, String p1, String p2) {
        
        Users users = new Users();
        users.setUser_id(globalUserId);
        users.setPswd(p1);
        UsersDao uDao = new UsersDao();
		globalPermitLevel = uDao.loginUsers(users).trim();
		//System.out.println("***563***");
		if (globalPermitLevel != "error") {
			Users users2 = new Users();
			users2.setUser_id(globalUserId);
			users2.setLast_user(globalUserId);
			uDao.changePassword(users2, p2);
		} else {
			
		}
		//System.out.println(":"+globalPermitLevel+":error:"+globalUserId);
		model.addAttribute("permitLevel", globalPermitLevel);
		model.addAttribute("UserId", globalUserId);
        return "change_password_update";
    }

//**********************************************************************

    @GetMapping("/types")
   	public String showTypes(Model model) {
    if (globalUserId != "error" && globalPermitLevel != "error") {
       	List < Types > listTypes;
   		Transaction transaction = null;
   		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

   			transaction = session.beginTransaction();
   			listTypes = session.createQuery("from Types where dataset = " + gDatasetActive + " order by id", Types.class).getResultList();
   			for(Types l : listTypes) {
   				//System.out.println("888+ List of Types::"+l.toString());
   			}

   			model.addAttribute("listTypes", listTypes);
   			model.addAttribute("globalPermitLevel", globalPermitLevel);
   			model.addAttribute("globalUserId", globalUserId);
   			transaction.commit();
            //System.out.println("After update");
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
   		
   		return "types";
    } else { return "index";
    }
   	}
//*************
    
    @RequestMapping("/types_save")
    public String types_save(String p1, String p2, String p3) {//, String param2

    	//System.out.println("*!*"+p1+":"+p2+":"+p3+":");

    	Types types = new Types(Integer.parseInt(p1),p2,p3,globalUserId);
    	types.setDataset(gDatasetActive);
    	TypesDao sDao = new TypesDao();
		sDao.updateTypes(types);
		
        return "types_save";
    }
//*************    
    
    @RequestMapping("/types_insert")
    public String types_insert(Model model, String p1) {//, String param

    	Types types = new Types();
    	//cars.setprice(Integer.parseInt(param4));
    	LocalDate dt =  LocalDate.now();
    	LocalDateTime dtt = LocalDateTime.now();
    	types.setType_name(p1);
    	types.setDate_create(dt);
    	types.setDataset(gDatasetActive);
    	types.setLast_user(globalUserId);
    	types.setLast_changed(dtt);
    	TypesDao sDao = new TypesDao();
		int id1 = (int) sDao.insertTypes(types);
		//System.out.println("**639**:");
		//System.out.println(id1);
		model.addAttribute("id1", id1);
		
        return "types_insert";
    }    
    
//************* 
    
    @RequestMapping("/types_delete")
    public String types_delete(String p1, String p2) {//, String param

    	Types types = new Types();
    	types.setId(Integer.parseInt(p1));
    	types.setType_name(p2);
    	types.setDataset(gDatasetArchive);
    	types.setLast_user(globalUserId);
    	//cars.setprice(Integer.parseInt(param4));
    	TypesDao sDao = new TypesDao();
		sDao.deleteTypes(types);
		////System.out.println("***:");

        return "types_delete";
    }    
    
    //*************************************************************************
  
	@GetMapping("/report_suppliers_by_sum")
	public String showReport_suppliers_by_sum(Model model) {
	    if (globalUserId != "error" && globalPermitLevel != "error") {
	       	List < Suppliers_by_sum > listSuppliers_by_sum;
	       	List < Suppliers_by_sum > listSuppliers_by_sum_10;
	   		Transaction transaction = null;
	   		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	   			transaction = session.beginTransaction();
	   			//System.out.println("***check 561");
	   			listSuppliers_by_sum = session.createQuery("from Suppliers_by_sum where dataset = " + gDatasetActive, Suppliers_by_sum.class).setMaxResults(50).getResultList();
	   			listSuppliers_by_sum_10 = session.createQuery("from Suppliers_by_sum where dataset = " + gDatasetActive, Suppliers_by_sum.class).setMaxResults(10).getResultList();

	   			for(Suppliers_by_sum l : listSuppliers_by_sum) {
	   				//System.out.println("888+ List of Types::"+l.toString());
	   			}
	   			model.addAttribute("listSuppliers_by_sum_10", listSuppliers_by_sum_10);
	   			model.addAttribute("listSuppliers_by_sum", listSuppliers_by_sum);
	   			model.addAttribute("globalPermitLevel", globalPermitLevel);
	   			model.addAttribute("globalUserId", globalUserId);
	   			transaction.commit();
	            //System.out.println("After update");
	        } catch (Exception e) {
	            if (transaction != null) {
	            	//System.out.println(e);
	                transaction.rollback();
	            }
	        }
	   		
	   		return "report_suppliers_by_sum";
	    } else { return "index";
	    }
	}
	
	//*************

		@GetMapping("/report_customers_by_sum")
		public String showReport_customers_by_sum(Model model) {
		    if (globalUserId != "error" && globalPermitLevel != "error") {
		       	List < Customers_by_sum > listCustomers_by_sum;
		       	List < Customers_by_sum > listCustomers_by_sum_10;
		   		Transaction transaction = null;
		   		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

		   			transaction = session.beginTransaction();
		   			//System.out.println("***check 561");
		   			listCustomers_by_sum = session.createQuery("from Customers_by_sum where dataset = " + gDatasetActive, Customers_by_sum.class).setMaxResults(50).getResultList();
		   			listCustomers_by_sum_10 = session.createQuery("from Customers_by_sum where dataset = " + gDatasetActive, Customers_by_sum.class).setMaxResults(10).getResultList();
		   			for(Customers_by_sum l : listCustomers_by_sum) {
		   				//System.out.println("888+ List of Types::"+l.toString());
		   			}
		   			model.addAttribute("listCustomers_by_sum_10", listCustomers_by_sum_10);
		   			model.addAttribute("listCustomers_by_sum", listCustomers_by_sum);
		   			model.addAttribute("globalPermitLevel", globalPermitLevel);
		   			model.addAttribute("globalUserId", globalUserId);
		   			transaction.commit();
		            //System.out.println("After update");
		        } catch (Exception e) {
		            if (transaction != null) {
		            	//System.out.println(e);
		                transaction.rollback();
		            }
		        }
		   		
		   		return "report_customers_by_sum";
		    } else { return "index";
		    }
		}
		
		//*************
		
		@GetMapping("/report_customers_deals_by_sum")
		public String showReport_customers_deals_by_sum(Model model) {
		    if (globalUserId != "error" && globalPermitLevel != "error") {
		       	List < Customers_deals_by_sum > listCustomers_deals_by_sum;
		       	List < Customers_deals_by_sum > listCustomers_deals_by_sum_10;
		   		Transaction transaction = null;
		   		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

		   			transaction = session.beginTransaction();
		   			//System.out.println("***check 561");
		   			listCustomers_deals_by_sum = session.createQuery("from Customers_deals_by_sum where dataset = " + gDatasetActive, Customers_deals_by_sum.class).setMaxResults(50).getResultList();
		   			listCustomers_deals_by_sum_10 = session.createQuery("from Customers_deals_by_sum where dataset = " + gDatasetActive, Customers_deals_by_sum.class).setMaxResults(10).getResultList();
		   			for(Customers_deals_by_sum l : listCustomers_deals_by_sum) {
		   				//System.out.println("888+ List of Types::"+l.toString());
		   			}
		   			model.addAttribute("listCustomers_deals_by_sum_10", listCustomers_deals_by_sum_10);
		   			model.addAttribute("listCustomers_deals_by_sum", listCustomers_deals_by_sum);
		   			model.addAttribute("globalPermitLevel", globalPermitLevel);
		   			model.addAttribute("globalUserId", globalUserId);
		   			transaction.commit();
		            //System.out.println("After update");
		        } catch (Exception e) {
		            if (transaction != null) {
		            	//System.out.println(e);
		                transaction.rollback();
		            }
		        }
		   		
		   		return "report_customers_deals_by_sum";
		    } else { return "index";
		    }
		}
		
		//*************
		
		@GetMapping("/report_suppliers_deals_by_sum")
		public String showReport_suppliers_deals_by_sum(Model model) {
			if (globalUserId != "error" && globalPermitLevel != "error") {
				List < Suppliers_deals_by_sum > listSuppliers_deals_by_sum;
				List < Suppliers_deals_by_sum > listSuppliers_deals_by_sum_10;
				Transaction transaction = null;
				try (Session session = HibernateUtil.getSessionFactory().openSession()) {
				   	transaction = session.beginTransaction();
				   	//System.out.println("***check 561");
				   	listSuppliers_deals_by_sum = session.createQuery("from Suppliers_deals_by_sum where dataset = " + gDatasetActive, Suppliers_deals_by_sum.class).setMaxResults(50).getResultList();
				   	listSuppliers_deals_by_sum_10 = session.createQuery("from Suppliers_deals_by_sum where dataset = " + gDatasetActive, Suppliers_deals_by_sum.class).setMaxResults(10).getResultList();
				   	for(Suppliers_deals_by_sum l : listSuppliers_deals_by_sum) {
				   		//System.out.println("888+ List of Types::"+l.toString());
				   	}
				   	model.addAttribute("listSuppliers_deals_by_sum_10", listSuppliers_deals_by_sum_10);
				   	model.addAttribute("listSuppliers_deals_by_sum", listSuppliers_deals_by_sum);
				   	model.addAttribute("globalPermitLevel", globalPermitLevel);
				   	model.addAttribute("globalUserId", globalUserId);
				   	transaction.commit();
				    //System.out.println("After update");
				} catch (Exception e) {
				    if (transaction != null) {
				         //System.out.println(e);
				         transaction.rollback();
				     }
				  }
				   		
				   return "report_suppliers_deals_by_sum";
				} else { return "index";
		}
	}
		//*************
	      
	      @GetMapping("/report_stock_current_status")
	     	public String Report_stock_current_status(Model model) {
	      if (globalUserId != "error" && globalPermitLevel != "error") {
	         	List < Stock > listStock;
	         	List < Stock > listStock10;
	     		Transaction transaction = null;
	     		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

	     			transaction = session.beginTransaction();
	     			listStock = session.createQuery("from Stock where dataset = " + gDatasetActive + " order by amount desc", Stock.class).getResultList();
	     			listStock10 = session.createQuery("from Stock where dataset = " + gDatasetActive + " order by amount desc", Stock.class).setMaxResults(10).getResultList();
	     			Long sum_stock = Long.parseLong(String.valueOf(session.createNativeQuery("select sum(amount) from stock where dataset = "+gDatasetActive).getSingleResult()));
	     			model.addAttribute("listStock", listStock);
	     			model.addAttribute("listStock10", listStock10);
	     			model.addAttribute("sum_stock", sum_stock);
	     			model.addAttribute("globalPermitLevel", globalPermitLevel);
	     			model.addAttribute("globalUserId", globalUserId);
	     			transaction.commit();
	              //System.out.println("After update");
	          } catch (Exception e) {
	              if (transaction != null) {
	              	//System.out.println(e);
	                  transaction.rollback();
	              }
	          }
	     		
	     		return "report_stock_current_status";
	      } else { return "index";
	      }
	     	}
}