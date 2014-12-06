package com.download.data;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class GetGeocodeListDao {
	public List<Object[]> getGeocodeList() {
		Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
        // builds a session factory from the service registry
        SessionFactory factory = cfg.buildSessionFactory(serviceRegistry);
        
        Session session = factory.openSession();
        
        Transaction t = session.beginTransaction();
        Query getQuery = session.createQuery("select zipcd, agc_ADDR_LATITUDE, agc_ADDR_LONGITUDE, nme, adr1, weburl, phone1, email, languages, services, distance, adj, vP from HUDList1");
//        getQuery.setFirstResult(0);
//        getQuery.setMaxResults(20);
        
        List<Object[]> geocodeList = (List<Object[]>)getQuery.list();
        
        t.commit();
        session.close();
        System.out.println("Geocodelist is Success!!");
        return geocodeList;
	}
}
