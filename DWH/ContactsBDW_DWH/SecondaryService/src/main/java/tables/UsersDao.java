package tables;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import codes.HibernateUtil;
import tables.Users;

public class UsersDao {
    public void updateUsers(Users users) {

        Transaction transaction = null;

        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // start the transaction
            transaction = session.beginTransaction();

            String hqlUpdate = "update Users t set t.user_id = :user_idV, t.permit_level = :permit_levelV, t.description = :descriptionV, "
            		+ " t.last_user = :last_userV, t.last_changed = now() where t.id = :idV";
                   int updatedEntities = session.createQuery( hqlUpdate )
                  		 .setInteger( "idV", users.getId() )
                           .setString( "user_idV", users.getUser_id() )
                           .setString( "permit_levelV", users.getPermit_level() )
                           .setString( "descriptionV", users.getDescription() )
                           .setString( "last_userV", users.getLast_user() )
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
//***********
    
    public long insertUsers(Users users) {
    	int id1 = 0;	
        Transaction transaction = null;
        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            
            String str = (String) session.createNativeQuery("Select crypt('1234567', gen_salt('bf'))").getSingleResult();

            users.setPswd(str);
            id1 =  (int) session.save(users);
            transaction.commit();
            //System.out.println("After insert");
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
		return id1;//
    }
//**********
    
    public void deleteUsers(Users users) {

        Transaction transaction = null;

        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // start the transaction
            transaction = session.beginTransaction();

            String sql = "update Users set user_id='" + users.getUser_id() + "#" + users.getId() +"', dataset=" + users.getDataset() +", last_user='" + users.getLast_user() +
            		"', last_changed=now() where id=" + users.getId();
            session.createNativeQuery(sql).executeUpdate();

            // commit transction
            transaction.commit();
            //System.out.println(users.getId());
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
    }
//**********
    
    public String loginUsers(Users users) {

        Transaction transaction = null;
        String returnStr = "";

        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // start the transaction
            transaction = session.beginTransaction();
            
            Users users1 = new Users();
            String sql = "from Users t where t.dataset = 1 and t.user_id = '" + users.getUser_id()+ "' AND t.pswd = crypt('" + users.getPswd() + "', t.pswd)";
            //System.out.println(sql);
            Long count_user = Long.parseLong(String.valueOf(session.createNativeQuery("select count(*) "+sql).getSingleResult()));
            if (count_user == 1) {
            	users1 = session.createQuery(sql, Users.class).getSingleResult();
            	// commit transction
            	//System.out.println("After login");
            	returnStr = users1.getPermit_level();
            } else {
            	returnStr = "error";}
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
                returnStr = "error";
            }
        }
		return returnStr;
    }
//*********
    
    public void changePassword(Users users,String newPswd) {
    	int id1 = 0;	
        Transaction transaction = null;
        // auto close session object
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start the transaction
            transaction = session.beginTransaction();
            
            String str = (String) session.createNativeQuery("Select crypt('" + newPswd + "', gen_salt('bf'))").getSingleResult();
            
            //users.setPswd(str);
            String sql = "update Users set pswd = '" + str + "', last_user='" + users.getLast_user() +
            		"', last_changed=now() where user_id='" + users.getUser_id() + "'";
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            //System.out.println("After update:" + sql);
        } catch (Exception e) {
            if (transaction != null) {
            	//System.out.println(e);
                transaction.rollback();
            }
        }
    }
}
