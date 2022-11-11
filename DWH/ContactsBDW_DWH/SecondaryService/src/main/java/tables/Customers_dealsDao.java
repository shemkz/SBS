package tables;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import codes.HibernateUtil;
import tables.Customers_deals;

public class Customers_dealsDao {
	public void updateCustomers_deals(Customers_deals customers_deals) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            String hqlUpdate = "update Customers_deals t set t.name_good = :name_goodV, t.price = :priceV, t.quantity = :quantityV, "
            		+ "t.sum = :sumV, t.details = :detailsV, t.last_user = :last_userV, t.last_changed = now() where t.id = :idV";
            //, t.price = :priceV, t.quantity = :quantityV, t.sum = :sumV, t.details = :detailsV
            int updatedEntities = session.createQuery( hqlUpdate )
        		 .setLong( "idV", customers_deals.getId() )
                 .setString( "name_goodV", customers_deals.getName_good() )
                 .setLong( "priceV", customers_deals.getPrice() )
                 .setInteger( "quantityV", customers_deals.getQuantity() )
                 .setLong( "sumV", customers_deals.getSum() )
                 .setString( "detailsV", customers_deals.getDetails() )
                 .setString( "last_userV", customers_deals.getLast_user())
                 .executeUpdate();
            // commit transction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
    }
	
	//***************************
    
    public long insertCustomers_deals(Customers_deals customers_deals) {
    	int id1 = 0;	
        Transaction transaction = null;
        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            id1 =  (int) session.save(customers_deals);
            transaction.commit();
            //System.out.println("After insert");
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
		return id1;
    }
    
//************
    
    public void deleteCustomers_deals(Customers_deals customers_deals) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String sql = "update Customers_deals set name_good='" + customers_deals.getName_good() + "#" + customers_deals.getId() +"', dataset=" + customers_deals.getDataset() +", last_user='" + customers_deals.getLast_user() +
            		"', last_changed=now() where id=" + customers_deals.getId();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            //System.out.println("After delete");
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
    }
}
