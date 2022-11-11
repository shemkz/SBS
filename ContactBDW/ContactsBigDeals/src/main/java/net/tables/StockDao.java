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
import net.tables.Customers;

public class StockDao {
    public void updateStock(Stock stock) {

        Transaction transaction = null;

        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // start the transaction
            transaction = session.beginTransaction();

            String hqlUpdate = "update Stock t set t.product_name = :product_nameV, t.measure = :measureV, t.price = :priceV, t.quantity = :quantityV, "
            		+ "t.amount = :amountV, t.description = :descriptionV, "
            		+ " t.last_user = :last_userV, t.last_changed = now() where t.id = :idV";
          //, t.price = :priceV, t.quantity = :quantityV, t.sum = :sumV, t.details = :detailsV
                   int updatedEntities = session.createQuery( hqlUpdate )
                  		 .setInteger( "idV", stock.getId() )
                           .setString( "product_nameV", stock.getProduct_name() )
                           .setString( "measureV", stock.getMeasure() )
                           .setLong( "priceV", stock.getPrice() )
                           .setLong( "quantityV", stock.getQuantity() )
                           
                           .setLong( "amountV", stock.getAmount() )
                           .setString( "descriptionV", stock.getDescription() )
                           .setString( "last_userV", stock.getLast_user() )
                           .executeUpdate();
            //session.update(suppliers);

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
    
    public long insertStock(Stock stock) {
    	int id1 = 0;	
        Transaction transaction = null;
        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            ////System.out.println("***49");
            // save student object
            id1 =  (int) session.save(stock);
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
    
    public void deleteStock(Stock stock) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            String sql = "update Stock set dataset=" + (stock.getDataset() - stock.getId()) +", last_user='" + stock.getLast_user() +
            				"', last_changed=now() where id=" + stock.getId();
            session.createNativeQuery(sql).executeUpdate();
            // commit transction
            transaction.commit();
            //System.out.println("After delete");
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
