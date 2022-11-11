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
import tables.Brands;

public class BrandsDao {
    public void updateBrands(Brands brands) {

        Transaction transaction = null;

        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // start the transaction
            transaction = session.beginTransaction();

            String hqlUpdate = "update Brands t set t.brand_name = :brand_nameV, t.description = :descriptionV, "
            		+ " t.last_user = :last_userV, t.last_changed = now() where t.id = :idV";
          //, t.price = :priceV, t.quantity = :quantityV, t.sum = :sumV, t.details = :detailsV
                   int updatedEntities = session.createQuery( hqlUpdate )
                  		 .setInteger( "idV", brands.getId() )
                           .setString( "brand_nameV", brands.getBrand_name() )
                           .setString( "descriptionV", brands.getDescription() )
                           .setString( "last_userV", brands.getLast_user() )
                           .executeUpdate();

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
    
    public long insertBrands(Brands brands) {
    	int id1 = 0;	
        Transaction transaction = null;
        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            ////System.out.println("***49");
            id1 =  (int) session.save(brands);
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
    
    public void deleteBrands(Brands brands) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            String sql = "update Brands set brand_name='" + brands.getBrand_name() + "#" + brands.getId() +"', dataset=" + brands.getDataset() +", last_user='" + brands.getLast_user() +
            				"', last_changed=now() where id=" + brands.getId();
            session.createNativeQuery(sql).executeUpdate();
            // commit transction
            transaction.commit();
            //System.out.println("After delete:" + sql);
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
    }

}
