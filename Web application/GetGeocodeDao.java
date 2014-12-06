package com.download.data;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class GetGeocodeDao {
	public List<Object[]> getGeocode(String zipcode) {
		Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
        // builds a session factory from the service registry
        SessionFactory factory = cfg.buildSessionFactory(serviceRegistry);
        
        Session session = factory.openSession();
        
        Transaction t = session.beginTransaction();
        Query getQuery = session.createQuery("select latitude, longitude from Geocode as G where G.zip = :zip_input");
        getQuery.setParameter("zip_input", zipcode);

        List<Object[]> geocode = (List<Object[]>)getQuery.list();
        
        t.commit();
        session.close();
        System.out.println("Geocode is Success!!");
        return geocode;
	}
}
