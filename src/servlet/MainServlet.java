package servlet;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.PlayerInfoImpl;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doService(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}


	private void doService(HttpServletRequest request, HttpServletResponse response) {
		String act = request.getParameter("act");
		System.out.println(act);
		switch (act) {
		case "searchProfile":
			searchProfile(request,response);
			break;
		case "twitterTest":
			twitterTest(request,response);
			break;

		default:
			break;
		}
		
	}

	private void twitterTest(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			PlayerInfoImpl.getInstance().twitterTest();;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void searchProfile(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String code = request.getParameter("code");
		System.out.println("input : "+ code);
		try {
//			String url = "http://game.granbluefantasy.jp/#profile/"+code;
//			response.sendRedirect(url);
			PlayerInfoImpl.getInstance().test();
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}


	
	
}
