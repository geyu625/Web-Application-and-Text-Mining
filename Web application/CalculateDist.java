package com.download.data;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class CalculateDist {
	public final static double AVERAGE_RADIUS_OF_EARTH = 6371;
	public static int calculateDistance(String userLatstr, String userLngstr,
			String venueLatstr, String venueLngstr) {
				Double userLat = Double.parseDouble(userLatstr);
				Double userLng = Double.parseDouble(userLngstr);
				Double venueLat = Double.parseDouble(venueLatstr);
				Double venueLng = Double.parseDouble(venueLngstr);
			    double latDistance = Math.toRadians(userLat - venueLat);
			    double lngDistance = Math.toRadians(userLng - venueLng);

			    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
			      + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
			      * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

			    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

			    return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));
			}
	
//	public static Map sortByValue(Map unsortMap) {	 
//		List list = new LinkedList(unsortMap.entrySet());
//	 
//		Collections.sort(list, new Comparator() {
//			public int compare(Object o1, Object o2) {
//				return ((Comparable) ((Map.Entry) (o1)).getValue())
//							.compareTo(((Map.Entry) (o2)).getValue());
//			}
//		});
//	 
//		Map sortedMap = new LinkedHashMap();
//		for (Iterator it = list.iterator(); it.hasNext();) {
//			Map.Entry entry = (Map.Entry) it.next();
//			sortedMap.put(entry.getKey(), entry.getValue());
//		}
//		return sortedMap;
//	}
	
	public List<Object[]> calculateDist(String zip) {
		GetGeocodeDao getGeocodeDao = new GetGeocodeDao();
		List<Object[]> geocode = getGeocodeDao.getGeocode(zip);
		GetGeocodeListDao getGeocodeListDao = new GetGeocodeListDao();
		List<Object[]> geocodeLists = getGeocodeListDao.getGeocodeList();
		

		for (int i = 0; i < geocodeLists.size(); i++) {
			if (geocodeLists.get(i)[1].equals(null) | geocodeLists.get(i)[1].equals(null)) {
				geocodeLists.get(i)[10] = null;
			}
			else {
				Integer dist = calculateDistance((String)geocode.get(0)[0], (String)geocode.get(0)[1], (String)geocodeLists.get(i)[1], (String)geocodeLists.get(i)[2]);
				geocodeLists.get(i)[10] = dist;
			}
		}

//		for (int i = 0; i < geocodeLists.size(); i++) {
//		Integer dist = calculateDistance(geocode.get(0).getLatitude(), geocode.get(0).getLongitude(), geocodeLists.get(i).getAgc_ADDR_LATITUDE(), geocodeLists.get(i).getAgc_ADDR_LONGITUDE());
//			geocodeLists.get(i).setDistance(dist.toString());
//		}
		
//		Collections.sort(geocodeLists, new Comparator<GeocodeList>() {
//	        @Override public int compare(GeocodeList p1, GeocodeList p2) {
//	            return Integer.parseInt(p1.getDistance())  - Integer.parseInt(p2.getDistance());
//	        }
//
//	    });
		
		Collections.sort(geocodeLists, new Comparator<Object[]>() {
	        @Override public int compare(Object[] p1, Object[] p2) {
	            return (int)(p1[10]) - (int)(p2[10]);
	        }

	    });
		
		
		return geocodeLists.subList(0, 10);
		
	} 
}
