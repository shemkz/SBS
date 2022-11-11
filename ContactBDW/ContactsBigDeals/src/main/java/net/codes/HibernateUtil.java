package net.codes;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import net.tables.Suppliers;
import net.tables.Suppliers_deals;
import net.tables.Types;
import net.tables.Users;
import net.tables.Customers;
import net.tables.Customers_deals;
import net.tables.Permission_level;
import net.tables.Brands;
import net.tables.Products;
import net.tables.Stock;

import net.views.Customers_by_sum;
import net.views.Customers_deals_by_sum;
import net.views.Suppliers_by_sum;
import net.views.Suppliers_deals_by_sum;


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
            //settings.put(Environment.URL, "jdbc:postgresql://185.146.1.110:5432/postgres");
            //settings.put(Environment.URL, "jdbc:postgresql://postgres.c4e6qun8njds.ap-southeast-1.rds.amazonaws.com:5432/postgres");
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
            				sources.addAnnotatedClass(Suppliers.class);
            				sources.addAnnotatedClass(Types.class);
            				sources.addAnnotatedClass(Users.class);
            				sources.addAnnotatedClass(Suppliers_deals.class);
            				sources.addAnnotatedClass(Customers.class);
            				sources.addAnnotatedClass(Customers_deals.class);
            				sources.addAnnotatedClass(Suppliers_by_sum.class);
            				sources.addAnnotatedClass(Customers_by_sum.class);
            				sources.addAnnotatedClass(Customers_deals_by_sum.class);
            				sources.addAnnotatedClass(Suppliers_deals_by_sum.class);
            				sources.addAnnotatedClass(Permission_level.class);
            				sources.addAnnotatedClass(Brands.class);
            				sources.addAnnotatedClass(Products.class);
            				sources.addAnnotatedClass(Stock.class);

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
