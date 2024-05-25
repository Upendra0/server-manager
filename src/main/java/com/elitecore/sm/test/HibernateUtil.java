package com.elitecore.sm.test;
 
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
 
public class HibernateUtil {
 
	//private static final SessionFactory sessionFactory = buildSessionFactory();
 
	private static SessionFactory sessionFactory;
	private static SessionFactory buildSessionFactory() {
		System.out.println("HibernateUtil.buildSessionFactory()");
		
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration config= new Configuration();
			// not needed to specify path now 
			//config.configure(new File("F:\\Mediation\\workspaces\\sm_revamp\\SMRevamp\\modules\\servermanager\\src\\com\\elitecore\\sm\\test\\hibernate.cfg.xml"));
			config.configure();
			ServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
			;
			
		return config.buildSessionFactory(serviceRegistry);
		
		} catch (Throwable ex) {//NOSONAR
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex); 
		}finally{
			
		}
	}
 
	public static SessionFactory getSessionFactory() {
		if(sessionFactory == null) sessionFactory = buildSessionFactory();
        return sessionFactory;
	}
 
	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}
 
}