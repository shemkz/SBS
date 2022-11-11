package codes;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import tables.Customers;
import tables.Customers_deals;
import tables.Products;
import tables.Users;

public class HibernateUtil {
	   private static StandardServiceRegistry registry;
	   private static SessionFactory sessionFactory;

	   public static SessionFactory getSessionFactory() {
	      if (sessionFactory == null) {
	         try {

	            // Create registry builder
	            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

	            // Hibernate settings equivalent to hibernate.cfg.xml's properties
	            Map<String, String> settings = new HashMap<>();
	            settings.put(Environment.DRIVER, "org.postgresql.Driver");
	            //settings.put(Environment.URL, "jdbc:postgresql://ies2.c4e6qun8njds.ap-southeast-1.rds.amazonaws.com:5432/postgres");
	            settings.put(Environment.URL, "jdbc:postgresql://localhost:5432/postgres");
	            settings.put(Environment.USER, "postgres");
	            settings.put(Environment.PASS, "********");
	            settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL9Dialect");
	            settings.put(Environment.HBM2DDL_AUTO, "update");
	            // Apply settings
	            registryBuilder.applySettings(settings);

	            // Create registry
	            registry = registryBuilder.build();

	            // Create MetadataSources
	            MetadataSources sources = new MetadataSources(registry);
	            				sources.addAnnotatedClass(Customers.class);
	            				sources.addAnnotatedClass(Customers_deals.class);
	            				sources.addAnnotatedClass(Products.class);
	            				sources.addAnnotatedClass(Users.class);

	            // Create Metadata
	            Metadata metadata = sources.getMetadataBuilder().build();

	            // Create SessionFactory
	            sessionFactory = metadata.getSessionFactoryBuilder().build();

	         } catch (Exception e) {
	            e.printStackTrace();
	            if (registry != null) {
	               StandardServiceRegistryBuilder.destroy(registry);
	            }
	         }
	      }
	      return sessionFactory;
	   }

	   public static void shutdown() {
	      if (registry != null) {
	         StandardServiceRegistryBuilder.destroy(registry);
	      }
	   }
	}
