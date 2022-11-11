<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<%@include file="includes/header.jsp" %>

<script type="text/javascript">
//*********************************************
var globalUserName = "";
var globalPermitLevel = "";
function index_login(){

	globalUserName = document.forms["mylogin"]["email"].value;
	p2 = document.forms["mylogin"]["psw"].value;
	
	// send data to MvcController without refreshing page
	var http = new XMLHttpRequest();
	http.open("POST", "/index_login", true);
	http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	var params = "p1=" + globalUserName + "&p2=" + p2;
	http.send(params);
	// fetch data from server
	http.onload = function() 
	{ 
		//alert(http.responseText); 
		globalPermitLevel = http.responseText; 
		globalPermitLevel = globalPermitLevel.trim();
		//alert(":"+globalPermitLevel+":"+"error"+":");
		if (globalPermitLevel == "error")
			{ alert("Recheck user name and password! Try again!"); }
		else
			{document.forms["mylogin"].innerHTML = "<h2>Login</h2><br>&nbsp<h3>Welcome - " + globalUserName + "</h3><br>&nbsp<h3>Permit Level - " + globalPermitLevel + "</h3><br><br> Your help desk: Mob/WhatsApp - +7(776)2928582";
	}
	}//  http.onload  
	

} 
//*********************************************
</script>

	<TABLE CLASS="width100">
	<col CLASS="width25"> <col CLASS="width5"> <col CLASS="width50"> <col CLASS="width5"> <col CLASS="width25">
	<TR>
	<TD>
	<form name="mylogin" >

    <h2>Login</h2>
	<br>&nbsp
    <label for="email"><b>User name</b></label>
    <input type="text" name="email" id="email" value="kanvladi">
	<br>&nbsp
    <label for="psw"><b>Password</b></label>
    <input type="password" name="psw" id="psw" value="********">
    <br>&nbsp
	<input type="button" value="Login" onclick="index_login()">

	</form>
	</TD>
	<TD>&nbsp
	</TD>
	<TD>
	
	<h2>Introduction</h2>
	
	<p>The application Contacts, Big deals will help organize your work with information of suppliers and customers.</p>

	<p>Information about suppliers and customers includes the following data: name (first, middle and last name), type of business, phone number (mobile and stationary), Tax identification number (TIN – ИНН), document details (ID, passport), address and etc.</p>

	<p>Information about deals includes the following data: supplier and customer, type of business, name of product, price, quantity, sum,  details of operation.</p>

	<p>Permission groups:<br>
	- Admin - direct access to data base management system. They can do everything.<br>
	- All permissions users - read, insert, update and delete all data. Exception: user data.<br>
	- Read and insert users -  read all data and insert new data. Exception: user data.<br>
	- Read only users –  read all data. Exception: user data.</p>

	</TD>
	<TD>&nbsp
	</TD>
	<TD>
	<h2>Fitches</h2>
	<br>
	<h3>Minimum Requirements</h3>

	<p>For the full functioning of the program, you only need: a browser, access to the Internet or a local network with a dedicated server.</p>

	<h3>User interface</h3>

	<p>User interface: simple and clear. This will allow you to optimize the use of working time. App do all operations (create, save, delete data) without reload page.</p>

	<h3>Safety</h3>

	<p>High data security is provided by the database server. Blowfish-based encryption algorithm, variation 2a provides high security for users' personal passwords.</p>
	
	<h3>Logging</h3>

	<p>Application don't delete records. App move records from active dataset to archive dataset. App also save last time and user for create, update or delete records.</p>
	
	</TD>
	</TR>
	</TABLE>

<%@include file="includes/footer.jsp" %>


