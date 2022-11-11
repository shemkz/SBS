package tables;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import codes.HibernateUtil;
import tables.Log_dwh;

public class Log_dwhDao {
    public void updateCustomers(Log_dwh log_dwh) {

        Transaction transaction = null;

        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // start the transaction
            transaction = session.beginTransaction();

            String hqlUpdate = "update Log_dwh t set t.actions = :actionsV, t.description = :descriptionV, "
            		+ " t.last_user = :last_userV, t.last_changed = now() where t.id = :idV";
          //, t.price = :priceV, t.quantity = :quantityV, t.sum = :sumV, t.details = :detailsV
                   int updatedEntities = session.createQuery( hqlUpdate )
                  		 .setInteger( "idV", log_dwh.getId() )
                           .setString( "actionsV", log_dwh.getActions() )
                           .setString( "descriptionV", log_dwh.getDescription() )
                           .setString( "last_userV", log_dwh.getLast_user() )
                           .executeUpdate();

            // commit transction
            transaction.commit();
            //System.out.println("After update");
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
    }
    
    //***********************
    
    public long insertLog_dwh(Log_dwh log_dwh) {
    	int id1 = 0;	
        Transaction transaction = null;
        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            // save student object
            id1 =  (int) session.save(log_dwh);
            // commit transction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
		return id1;
    }
    
    //************************
    
    public void deleteCustomers(Log_dwh log_dwh) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            String sql = "update Log_dwh set actions='" + log_dwh.getActions() + "#" + log_dwh.getId() +"' , dataset=" + log_dwh.getDataset() +", last_user='" + log_dwh.getLast_user() +
            				"', last_changed=now() where id=" + log_dwh.getId();
            session.createNativeQuery(sql).executeUpdate();
            // commit transction
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
