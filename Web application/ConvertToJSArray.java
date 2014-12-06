package com.download.data;

import java.util.List;

public class ConvertToJSArray {

//	public static void main(String[] args) {
//		GetGeocodeListDao getGeocodeListDao = new GetGeocodeListDao();
//		List<Object[]> lists = getGeocodeListDao.getGeocodeList();
//		String str = ConvertToJSArray.toJSArray(lists);
//		System.out.println(str);
//	}
	
	public static String toJSArray (List<Object[]> geocodelists) {
		String jsArray = "[";
		for (int i = 0; i < geocodelists.size(); i++ ) {
			jsArray += ConvertToJSArray.getArrayString(geocodelists.get(i));
			if (i < (geocodelists.size() - 1)) {
				jsArray += ", ";
			}
		}
		jsArray += "]";
		return jsArray;
	}
	
	public static String getArrayString(Object[] geocodelist){
	    String result = "[" + "\"" + geocodelist[3] + "\"" +  ", " + geocodelist[1] +  ", " + geocodelist[2] + "]";
	    return result;
	}
}
