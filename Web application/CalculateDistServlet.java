package com.download.data;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CalculateDistServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
	try {
		String zipcode = request.getParameter("zipcode");
		CalculateDist calculateDistclass = new CalculateDist();
		List<Object[]> results = calculateDistclass.calculateDist(zipcode);
		GetGeocodeDao getGeocodeDao = new GetGeocodeDao();
		List<Object[]> center = getGeocodeDao.getGeocode(zipcode);
		
		request.setAttribute("center", center);
		request.setAttribute("results", results);
		request.getRequestDispatcher("index.jsp").forward(request, response);
		//request.getRequestDispatcher("map.jsp").forward(request, response);
	} catch (Throwable theException) {
		System.out.println(theException);
	}
	}

	@Override  
	protected void doGet(HttpServletRequest request, HttpServletResponse response)  
			throws ServletException, IOException {  
		doPost(request, response);  
	}  
}
