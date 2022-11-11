package net.tables;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import net.codes.HibernateUtil;
import net.tables.Products;

public class ProductsDao {
    public void updateProducts(Products products) {

        Transaction transaction = null;

        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // start the transaction
            transaction = session.beginTransaction();

            String hqlUpdate = "update Products t set t.type_name = :type_nameV, t.brand_name = :brand_nameV, t.product_name = :product_nameV, t.measure = :measureV, "
            		+ " t.price = :priceV, t.description = :descriptionV, "
            		+ " t.last_user = :last_userV, t.last_changed = now() where t.id = :idV";
          //, t.price = :priceV, t.quantity = :quantityV, t.sum = :sumV, t.details = :detailsV
                   int updatedEntities = session.createQuery( hqlUpdate )
                  		 .setInteger( "idV", products.getId() )
                           .setString( "type_nameV", products.getType_name() )
                           .setString( "brand_nameV", products.getBrand_name() )
                           .setString( "product_nameV", products.getProduct_name() )
                           .setString( "measureV", products.getMeasure() )
                           
                           .setLong( "priceV", products.getPrice() )
                           .setString( "descriptionV", products.getDescription() )
                           .setString( "last_userV", products.getLast_user() )
                           .executeUpdate();


            // commit transction
            transaction.commit();
            //System.out.println("After update");
        } catch (Exception e) {
            if (transaction != null) {
            	////System.out.println("******* error");
            	//System.out.println(e);
                transaction.rollback();
            }
        }
    }
    
    //***********************
    
    public long insertProducts(Products products) {
    	int id1 = 0;	
        Transaction transaction = null;
        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            ////System.out.println("***49");
            // save student object
            id1 =  (int) session.save(products);
            // commit transction
            transaction.commit();
            //System.out.println("After insert");
        } catch (Exception e) {
            if (transaction != null) {
            	////System.out.println("******* error");
            	//System.out.println(e);
                transaction.rollback();
            }
        }
		return id1;
    }
    
    //************************
    
    public void deleteProducts(Products products) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            String sql = "update Products set product_name='" + products.getProduct_name() + "#" + products.getId() +"', dataset=" + products.getDataset() +", last_user='" + products.getLast_user() +
            				"', last_changed=now() where id=" + products.getId();
            session.createNativeQuery(sql).executeUpdate();
            // commit transction
            transaction.commit();
            //System.out.println("After delete 95");
            ////System.out.println(customers.getId());
        } catch (Exception e) {
            if (transaction != null) {
            	////System.out.println("******* error");
            	//System.out.println(e);
                transaction.rollback();
            }
        }
    }

}
