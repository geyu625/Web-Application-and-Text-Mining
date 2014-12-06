package com.download.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SaveData {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("http.proxyHost", "10.110.17.6");
		System.setProperty("http.proxyPort", "80");
		
//		try {
//			String strUrl = "http://data.hud.gov/Housing_Counselor/search?AgencyName=&City=&State=&RowLimit=&Services=&Languages=";
//			//String strUrl = "http://data.hud.gov/Housing_Counselor/getServices";
//			URL url = new URL(strUrl);
//			InputStreamReader isr = new InputStreamReader(url.openStream(), "utf-8");
//			BufferedReader br = new BufferedReader(isr);
//
//            String strRead = br.readLine();
//            //System.out.println(strRead);
//            
//            GsonBuilder gsonBuilder = new GsonBuilder();
//            
//            //gsonBuilder.registerTypeAdapter(HUDList.class, new HUDListDeserializer());
//            Gson gson = gsonBuilder.create();
//            
//            Type hudLists = new TypeToken<List<HUDList>>() {}.getType();
//            List<HUDList> lists = gson.fromJson(strRead, hudLists);
//            System.out.println(lists.get(2).getAdr1());
            
            // loads configuration and mappings
            Configuration cfg = new Configuration().configure("hibernate.cfg.xml");

            
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
            // builds a session factory from the service registry
            SessionFactory factory = cfg.buildSessionFactory(serviceRegistry);
            
            Session session = factory.openSession();
            
            Transaction t = session.beginTransaction();
            
//            //create table for very first time
//            for (int i = 0; i < lists.size(); i++) {
//            	session.save(lists.get(i));
//            	if (i % 20 == 0) {
//            		session.flush();
//            		session.clear();
//            	}
//            }
           
            Query query = session.createQuery("from HUDList");
            query.setFirstResult(0);
            query.setMaxResults(3);
            List<HUDList> lists = query.list();
            
            for (HUDList list : lists) {
            	System.out.println(list.getAgcid());
            	System.out.println(list.getAdr1());
            	System.out.println(list.getNme());
            	System.out.println(list.getServices());
            }
            
            System.out.println(lists.get(2).getAdr1());
            
//            for (HUDList1 hudList1 : lists) {
//            	session.save(hudList1);
//            }
            
            t.commit();
            session.close();
            System.out.println("Success!!");
            
//		} catch (IOException e){
//			e.printStackTrace();
//		}
	}
}
