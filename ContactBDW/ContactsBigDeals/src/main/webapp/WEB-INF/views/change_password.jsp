<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<%@include file="includes/header.jsp" %>

<script type="text/javascript">
//*********************************************

function change_pswd(){
	p1 = document.forms["mylogin"]["psw"].value;
	p2 = document.forms["mylogin"]["new_pswd_1"].value;
	p3 = document.forms["mylogin"]["new_pswd_2"].value;
	alert(":" + p2 + ":" + p3 + ":");
		if (p2 == p3)
	{
	
	var http = new XMLHttpRequest();
	http.open("POST", "/change_password_update", true);
	http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	var params = "p1=" + p1 + "&p2=" + p2;
	http.send(params);
	http.onload = function() 
	{ 
		//alert(http.responseText); 
		globalPermitLevel = http.responseText; 
		globalPermitLevel = globalPermitLevel.trim();
		//alert(":"+globalPermitLevel+":"+"error"+":");
		if (globalPermitLevel == "error")
			{ alert("Recheck user name and old password! Try again!"); }
		else
			{document.forms["mylogin"].innerHTML = "<h2>Login</h2><br>&nbsp<h3>Welcome - " + "${UserId}" + "</h3><br>&nbsp<h3>Permit Level - " + "${permitLevel}" + "</h3><br> Password was changed!<br><br> Your help desk: Mob/WhatsApp - +7(776)2928582";
	}
	}//  http.onload  
	
	}//(p2 == p3)
	else {alert("New passwords do not match! Please retype!");}

} 
//*********************************************
</script>


	<form name="mylogin" >

    <h2>Change password</h2>
	<br>
	
    <b>User name: "${UserId}"</b><br>
	<b>Permission level: "${permitLevel}"</b><br><br>
	
    <label for="psw"><b>Old password</b></label>
    <input type="password" name="psw" id="psw" value="">
    <br>
	
	<label for="psw"><b>New password</b></label>
    <input type="password" name="new_pswd_1" id="new_pswd_1" value="">
    <br>
	
	<label for="psw"><b>Repeat new password</b></label>
    <input type="password" name="new_pswd_2" id="new_pswd_2" value="">
    <br>
	
	<input type="button" value="Login" onclick="change_pswd()">

	</form>
	

<%@include file="includes/footer.jsp" %>


