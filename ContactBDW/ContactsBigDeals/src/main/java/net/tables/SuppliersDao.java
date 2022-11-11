package net.tables;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import net.codes.HibernateUtil;
import net.tables.Suppliers;

public class SuppliersDao {
    public void updateSuppliers(Suppliers suppliers) {

        Transaction transaction = null;

        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // start the transaction
            transaction = session.beginTransaction();
            String hqlUpdate = "update Suppliers t set t.type_name = :type_nameV, t.first_name = :first_nameV, t.middle_name = :middle_nameV, t.last_name = :last_nameV, "
            		+ "t.TIN = :TINV, t.mobile_p = :mobile_pV, t.station_p = :station_pV, t.address = :addressV, t.description = :descriptionV, "
            		+ " t.last_user = :last_userV, t.last_changed = now() where t.id = :idV";
                   int updatedEntities = session.createQuery( hqlUpdate )
                  		 .setInteger( "idV", suppliers.getId() )
                           .setString( "type_nameV", suppliers.getType_name() )
                           .setString( "first_nameV", suppliers.getFirst_name() )
                           .setString( "middle_nameV", suppliers.getMiddle_name() )
                           .setString( "last_nameV", suppliers.getLast_name() )
                           
                           .setString( "TINV", suppliers.getTIN() )
                           .setString( "mobile_pV", suppliers.getMobile_p() )
                           .setString( "station_pV", suppliers.getStation_p() )
                           .setString( "addressV", suppliers.getAddress() )
                           .setString( "descriptionV", suppliers.getDescription() )
                           .setString( "last_userV", suppliers.getLast_user() )
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
    
    public long insertSuppliers(Suppliers suppliers) {
    	int id1 = 0;	
        Transaction transaction = null;
        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            ////System.out.println("***49");
            id1 =  (int) session.save(suppliers);
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
    
    public void deleteSuppliers(Suppliers suppliers) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            //session.remove(suppliers);
            String sql = "update Suppliers set first_name='" + suppliers.getFirst_name() + "#" + suppliers.getId() +"', dataset=" + suppliers.getDataset() +", last_user='" + suppliers.getLast_user() +
            		"', last_changed=now() where id=" + suppliers.getId();
            session.createNativeQuery(sql).executeUpdate();
            // commit transction
            transaction.commit();
            //System.out.println("After delete");
            //System.out.println(suppliers.getId());
        } catch (Exception e) {
            if (transaction != null) {
            	////System.out.println("******* error");
            	//System.out.println(e);
                transaction.rollback();
            }
        }
    }
}
