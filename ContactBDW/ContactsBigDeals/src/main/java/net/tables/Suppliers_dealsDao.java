package net.tables;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import net.codes.HibernateUtil;
import net.tables.Suppliers_deals;

public class Suppliers_dealsDao {
	public void updateSuppliers_deals(Suppliers_deals suppliers_deals) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            String hqlUpdate = "update Suppliers_deals t set t.name_good = :name_goodV, t.price = :priceV, t.quantity = :quantityV, "
            		+ "t.sum = :sumV, t.details = :detailsV, t.last_user = :last_userV, t.last_changed = now() where t.id = :idV";
            int updatedEntities = session.createQuery( hqlUpdate )
        		 .setLong( "idV", suppliers_deals.getId() )
                 .setString( "name_goodV", suppliers_deals.getName_good() )
                 .setLong( "priceV", suppliers_deals.getPrice() )
                 .setInteger( "quantityV", suppliers_deals.getQuantity() )
                 .setLong( "sumV", suppliers_deals.getSum() )
                 .setString( "detailsV", suppliers_deals.getDetails() )
                 .setString( "last_userV", suppliers_deals.getLast_user())
                 .executeUpdate();
            // commit transction
            transaction.commit();
            ////System.out.println("After update 365");
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println("******* error");
            	//System.out.println(e);
                transaction.rollback();
            }
        }
    }
	
	//***************************
    
    public long insertSuppliers_deals(Suppliers_deals suppliers_deals) {
    	int id1 = 0;	
        Transaction transaction = null;
        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            ////System.out.println("***49");
            id1 =  (int) session.save(suppliers_deals);
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
    
    //************
    
    public void deleteSuppliers_deals(Suppliers_deals suppliers_deals) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String sql = "update Suppliers_deals set name_good='" + suppliers_deals.getName_good() + "#" + suppliers_deals.getId() +"' , dataset=" + suppliers_deals.getDataset() +", last_user='" + suppliers_deals.getLast_user() +
            		"', last_changed=now() where id=" + suppliers_deals.getId();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            //System.out.println("After delete");
            //System.out.println(suppliers_deals.getId());
        } catch (Exception e) {
            if (transaction != null) {
            	////System.out.println("******* error");
            	//System.out.println(e);
                transaction.rollback();
            }
        }
    }

}
