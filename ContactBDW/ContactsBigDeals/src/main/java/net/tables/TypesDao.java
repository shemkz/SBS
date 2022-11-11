package net.tables;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import net.codes.HibernateUtil;
import net.tables.Types;

public class TypesDao {
    public void updateTypes(Types types) {

        Transaction transaction = null;

        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // start the transaction
            transaction = session.beginTransaction();
            String hqlUpdate = "update Types t set t.type_name = :type_nameV, t.description = :descriptionV, "
            		+ " t.last_user = :last_userV, t.last_changed = now() where t.id = :idV";
          //, t.price = :priceV, t.quantity = :quantityV, t.sum = :sumV, t.details = :detailsV
                   int updatedEntities = session.createQuery( hqlUpdate )
                  		 .setInteger( "idV", types.getId() )
                           .setString( "type_nameV", types.getType_name() )
                           .setString( "descriptionV", types.getDescription() )
                           .setString( "last_userV", types.getLast_user() )
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
    
    public long insertTypes(Types types) {
    	int id1 = 0;	
        Transaction transaction = null;
        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            ////System.out.println("***49");

            id1 =  (int) session.save(types);
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
    public void deleteTypes(Types types) {

        Transaction transaction = null;

        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // start the transaction
            transaction = session.beginTransaction();

            String sql = "update Types set type_name='" + types.getType_name() + "#" + types.getId() +"', dataset=" + types.getDataset() +", last_user='" + types.getLast_user() +
            		"', last_changed=now() where id=" + types.getId();
            session.createNativeQuery(sql).executeUpdate();

            // commit transction
            transaction.commit();
            //System.out.println("After delete");
            ////System.out.println(types.getId());
        } catch (Exception e) {
            if (transaction != null) {
            	////System.out.println("******* error");
            	//System.out.println(e);
                transaction.rollback();
            }
        }
    }
}
