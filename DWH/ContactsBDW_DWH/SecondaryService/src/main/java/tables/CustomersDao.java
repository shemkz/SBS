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
import tables.Customers;

public class CustomersDao {
    public void updateCustomers(Customers customers) {

        Transaction transaction = null;

        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // start the transaction
            transaction = session.beginTransaction();

            String hqlUpdate = "update Customers t set t.type_name = :type_nameV, t.first_name = :first_nameV, t.middle_name = :middle_nameV, t.last_name = :last_nameV, "
            		+ "t.TIN = :TINV, t.mobile_p = :mobile_pV, t.station_p = :station_pV, t.address = :addressV, t.description = :descriptionV, "
            		+ " t.last_user = :last_userV, t.last_changed = now() where t.id = :idV";
          //, t.price = :priceV, t.quantity = :quantityV, t.sum = :sumV, t.details = :detailsV
                   int updatedEntities = session.createQuery( hqlUpdate )
                  		 .setInteger( "idV", customers.getId() )
                           .setString( "type_nameV", customers.getType_name() )
                           .setString( "first_nameV", customers.getFirst_name() )
                           .setString( "middle_nameV", customers.getMiddle_name() )
                           .setString( "last_nameV", customers.getLast_name() )
                           
                           .setString( "TINV", customers.getTIN() )
                           .setString( "mobile_pV", customers.getMobile_p() )
                           .setString( "station_pV", customers.getStation_p() )
                           .setString( "addressV", customers.getAddress() )
                           .setString( "descriptionV", customers.getDescription() )
                           .setString( "last_userV", customers.getLast_user() )
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
    
    public long insertCustomers(Customers customers) {
    	int id1 = 0;	
        Transaction transaction = null;
        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            // save student object
            id1 =  (int) session.save(customers);
            // commit transction
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
    
//************************
    
    public void deleteCustomers(Customers customers) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            String sql = "update Customers set first_name='" + customers.getFirst_name() + "#" + customers.getId() +"' , dataset=" + customers.getDataset() +", last_user='" + customers.getLast_user() +
            				"', last_changed=now() where id=" + customers.getId();
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
