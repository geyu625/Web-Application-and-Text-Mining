package com.download.data;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class UpdateGeocodeDao {
	public static void main(String[] args) {
		UpdateGeocodeDao updateGeocodeDao = new UpdateGeocodeDao();
		updateGeocodeDao.UpdateGeocode();
		System.out.println("updateGeocodeDao successful");
	}
	
	public List getNullGeocode() {
		Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
        // builds a session factory from the service registry
        SessionFactory factory = cfg.buildSessionFactory(serviceRegistry);
        
        Session session = factory.openSession();
        
        Transaction t = session.beginTransaction();
        //GetGeocodeDao getGeocodeDao = new GetGeocodeDao();
		//List<Object[]> center = getGeocodeDao.getGeocode(zipcode);
		Query getQuery = session.createQuery("select agcid zipcd from HUDList1 as h where h.agc_ADDR_LATITUDE is null or h.agc_ADDR_LATITUDE = '0'");
        //Query getQuery = session.createQuery("update HUDList1 as h set agc_ADDR_LATITUDE = :latitude, agc_ADDR_LONGITUDE = :longitude where h.zipcd is null");
        //String lat = getGeocodeDao.getGeocode()        
        //getQuery.setParameter("zip_input", zipcode);

        List<Object[]> nullGeocodes = (List<Object[]>)getQuery.list();
        
        t.commit();
        session.close();
        System.out.println("getNullGeocode is Success!!");
        return nullGeocodes;
	}
	
	public void UpdateGeocode() {
		UpdateGeocodeDao updateGeocodeDao = new UpdateGeocodeDao();
		List nullGeocodes = updateGeocodeDao.getNullGeocode();
		Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
        // builds a session factory from the service registry
        SessionFactory factory = cfg.buildSessionFactory(serviceRegistry);
        
        Session session = factory.openSession();
        
        Transaction t = session.beginTransaction();
        GetGeocodeDao getGeocodeDao = new GetGeocodeDao();
        List<Object[]> newGeocodes = null;
//        for (int i = 0; i < nullGeocodes.size(); i++) {
//        	//newGeocodes = getGeocodeDao.getGeocode(nullGeocodes.get(i)[0].toString());
//        	System.out.println(nullGeocodes.get(i)[0].toString());
//        }
        for (Object objects : nullGeocodes) {
        	newGeocodes = getGeocodeDao.getGeocode(objects.toString());
        	System.out.println(objects);
        }
        
        Object[] latvalues = new Object[newGeocodes.size()];
        Object[] logvalues = new Object[newGeocodes.size()];
        for (int i = 0; i < newGeocodes.size(); i++) {
        	latvalues[i] = newGeocodes.get(i)[0];
        	logvalues[i] = newGeocodes.get(i)[1];
        }
		Query getQuery = session.createQuery("update HUDList1 as h set agc_ADDR_LATITUDE = :latitude, agc_ADDR_LONGITUDE = :longitude where h.agc_ADDR_LATITUDE is null");
        getQuery.setParameterList("latitude", latvalues);
        getQuery.setParameterList("longitude", logvalues);
        int result = getQuery.executeUpdate();
        t.commit();
        session.close();
        System.out.println("Changed NULL geocode Rows affected is: " + result);
	}
}
