package edu.usm.cos420.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
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

@WebServlet(urlPatterns = {"/create"})
public class CreatePatientServlet extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	IOException {
		req.setAttribute("action", "Add");          // Part of the Header in form.jsp
		req.setAttribute("destination", "create");  // The urlPattern to invoke (this Servlet)
		req.setAttribute("page", "form");           // Tells base.jsp to include form.jsp
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	IOException {
		Patient patient = new Patient();
		patient.setFirstName(req.getParameter("firstName"));
		patient.setLastName(req.getParameter("lastName"));
		patient.setGender(req.getParameter("gender"));
		patient.setAddress(req.getParameter("address"));
		patient.setBirthDate(Date.valueOf(req.getParameter("birthDate")));	
		
		String dbUrl = constructDBurl();
		resp.getWriter().append(dbUrl);
		
		PatientDao dao = null;
		try {
			dao = new PatientSqlDao(dbUrl);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Long id = dao.createPatient(patient);
			resp.sendRedirect(req.getContextPath() + "/read?id=" + id.toString());   // read what we just wrote
		} catch (Exception e) {
			throw new ServletException("Error creating patient", e);
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
