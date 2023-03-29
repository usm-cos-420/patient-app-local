package edu.usm.cos420.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.usm.cos420.dao.PatientSqlDao;
import edu.usm.cos420.dao.PatientDao;
import edu.usm.cos420.domain.Patient;


@WebServlet(urlPatterns = {"/read"})
public class ReadPatientServlet extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		Long id = Long.decode(req.getParameter("id"));

		String dbUrl = constructDBurl();
		PatientDao dao = null;
		try {
			dao = new PatientSqlDao(dbUrl);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Patient patient = dao.readPatient(id);
			req.setAttribute("patient", patient);
			req.setAttribute("page", "view");
			req.getRequestDispatcher("/base.jsp").forward(req, resp);
		} catch (Exception e) {
			throw new ServletException("Error reading patient", e);
		}
	}
	
	public String constructDBurl() throws IOException
	{
		//Get DB information
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
		String strDbUser = properties.getProperty("sql.userName");        // database login username
        String strDbPassword = properties.getProperty("sql.password");    // database login password
        String remoteHost = properties.getProperty("sql.urlRemote");     // remote host url (ie cloud server url	
        String remotePort = properties.getProperty("sql.remotePort");     // remote post number
        String databaseName = properties.getProperty("sql.dbName");
        
        String DBurl = "jdbc:postgresql://" + remoteHost +":" + remotePort + "/"+databaseName + "?user="+ strDbUser + "&password="+ strDbPassword;  
	    return DBurl;
	    		
	}
}
