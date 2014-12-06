package com.download.data;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GetHUDListServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
						throws ServletException, IOException {
		try {
			List<HUDList> hudlists = GetDataDao.getData();
			request.setAttribute("lists", hudlists);
			request.getRequestDispatcher("index.jsp").forward(request, response);
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

