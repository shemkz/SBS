<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<%@include file="includes/header.jsp" %>

<script type="text/javascript">
//*********************************************
 
function take_values(){
	var flag=1; // element exists
	i= - minusI;
	
	while ((flag == 1) && (i < 1000)) {
 
		if (i >= 0) {iStr = ""+i;}// for positive index
			else {iStr = "S"+i*(-1);} // for negative index
 	
		nId = "id"+iStr;
		nTypeName = "type_name"+iStr;
		nFirstName = "first_name"+iStr;
		nMiddleName = "middle_name"+iStr;
		nLastName = "last_name"+iStr;
 
		nTIN = "TIN"+iStr;
		nMobileP = "mobile_p"+iStr;
		nStationP = "station_p"+iStr;
		nAddress = "address"+iStr;
		nDateCreate = "date_create"+iStr;
		nDesc = "description"+iStr;
		//alert("|||:"+nId+":");//+document.forms["myform"][nId].value
		if(typeof(document.forms["myform"][nTypeName]) == 'undefined' || document.forms["myform"][nTypeName] == null)
			{flag=0;} // element not exist
 
		if ((i < 0) || // negative index
		((document.forms["myform"][nId]) && // element exists
		(document.forms["myform"][nTypeName].style.color == 'red' ||
		document.forms["myform"][nFirstName].style.color == 'red' ||
		document.forms["myform"][nMiddleName].style.color == 'red' ||
		document.forms["myform"][nLastName].style.color == 'red' ||
		document.forms["myform"][nTIN].style.color == 'red' ||
		document.forms["myform"][nMobileP].style.color == 'red' ||
		document.forms["myform"][nStationP].style.color == 'red' ||
		document.forms["myform"][nAddress].style.color == 'red' ||
		document.forms["myform"][nDesc].style.color == 'red')))
		{//.style.color == 'red' - changed
			p1=document.forms["myform"][nId].value;
			p2=document.forms["myform"][nTypeName].value;
			p3=document.forms["myform"][nFirstName].value;
			p4=document.forms["myform"][nMiddleName].value;
			p5=document.forms["myform"][nLastName].value;

			p6=document.forms["myform"][nTIN].value;
			p7=document.forms["myform"][nMobileP].value;
			p8=document.forms["myform"][nStationP].value;
			p9=document.forms["myform"][nAddress].value;
			p10=document.forms["myform"][nDesc].value;
 
			if (p1==null || p1=="") {
				alert("Errors on the table!");
				return false;
				}else{
				var http = new XMLHttpRequest();
				http.open("POST", "/customers_save", true);
				http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				var params = "p1=" + p1 + "&p2=" + p2 + "&p3=" + p3 + "&p4=" + p4 + "&p5=" + p5
 				 + "&p6=" + p6 + "&p7=" + p7 + "&p8=" + p8 + "&p9=" + p9 + "&p10=" + p10;
				http.send(params);
				http.onload = function() {  }//alert("+++"+http.responseText);
				} 
		}//.style.color == 'red' - changed
	i++; }//loop while
}//function take_values()

//***********

function myChangeFunction(name){
	//var x=document.getElementById('id_1');  
	var x=document.forms["myform"][name];
        x.style.color='red';
} 

//***********

var minusI = 0;// for negative index - new row
var newId = "";
function new_row(){
	
	// add new row and cells
	minusI += 1;
	var table = document.getElementById("mytable").getElementsByTagName('tbody')[0];
	var row = table.insertRow(-1);
	var cell1 = row.insertCell(0);
	var cell2 = row.insertCell(1);
	var cell3 = row.insertCell(2);
	var cell4 = row.insertCell(3);
	var cell5 = row.insertCell(4);
	var cell6 = row.insertCell(5);
	var cell7 = row.insertCell(6);
	var cell8 = row.insertCell(7);
	var cell9 = row.insertCell(8);
	var cell10 = row.insertCell(9);
	var cell11 = row.insertCell(10);
	var cell12 = row.insertCell(11);

	// fill new cells
	cell1.innerHTML = '<input type="text" class="id" name="idS' + minusI + '" value="' + newId + '" disabled>';
	cell2.innerHTML = `<select class="types" name="type_nameS` + minusI + `" ><option selected value="Текстиль">Текстиль</option><c:forEach items="${listTypes}" var="typ"><option value="${typ.type_name}">${typ.type_name}</option></c:forEach></select>`;
	cell3.innerHTML = '<input type="text" class="name" name="first_nameS' + minusI + '" value="">';
	cell4.innerHTML = '<input type="text" class="name" name="middle_nameS' + minusI + '" value="">';
	cell5.innerHTML = '<input type="text" class="name" name="last_nameS' + minusI + '" value="">';
	cell6.innerHTML = '<input type="text" class="tin" name="TINS' + minusI + '" value="">';
	cell7.innerHTML = '<input type="text" class="mobile" name="mobile_pS' + minusI + '" value="">';
	cell8.innerHTML = '<input type="text" class="station" name="station_pS' + minusI + '" value="">';
	cell9.innerHTML = '<input type="text" name="addressS' + minusI + '" value="">';
	cell10.innerHTML = '<input type="text" name="descriptionS' + minusI + '" value="">';
	cell11.innerHTML = '<input type="text" class="dt" name="date_createS' + minusI + '" value="TODAY">';

	// send data to MvcController without refreshing page
	var http = new XMLHttpRequest();
	http.open("POST", "/customers_insert", true);
	http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	var params='p1='+'Текстиль';
	http.send(params);

	// fetch data from server
	http.onload = function() { 
		newId=http.responseText;  
		nId = "idS" + minusI;
		document.forms["myform"][nId].value = newId;
		cell12.innerHTML = `<input type="button" value="Deals..." 
                        	onclick="window.location.replace('/customers_deals?pId=` + newId.trim() + `');">`;
		}//  http.onload            
}

//***********

function delete_row(i){

	if (i >= 0) {iStr = ""+i;}// for positive index
		else {iStr = "S"+i*(-1);} // for negative index
 	
	nId = "id"+iStr;
	nTypeName = "type_name"+iStr;
	nFirstName = "first_name"+iStr;
	nMiddleName = "middle_name"+iStr;
	nLastName = "last_name"+iStr;
 
	nTIN = "TIN"+iStr;
	nMobileP = "mobile_p"+iStr;
	nStationP = "station_p"+iStr;
	nAddress = "address"+iStr;
	nDateCreate = "date_create"+iStr;
	nDesc = "description"+iStr;
 
	// send and fetch data to / from server without refreshing page
	p1 = document.forms["myform"][nId].value;
	p2 = document.forms["myform"][nFirstName].value;
	var http = new XMLHttpRequest();
	http.open("POST", "/customers_delete", true);
	http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	var params = "p1=" + p1 +"&p2=" + p2;
	http.send(params);
	http.onload = function() { alert(http.responseText);  }//  http.onload  
 
	// clear cells of removed records
	document.forms["myform"][nId].value = "";
	document.forms["myform"][nTypeName].value = "";
	document.forms["myform"][nFirstName].value = "";
	document.forms["myform"][nMiddleName].value = "";
	document.forms["myform"][nLastName].value = "";

	document.forms["myform"][nTIN].value = "";
	document.forms["myform"][nMobileP].value = "";
	document.forms["myform"][nStationP].value = "";
	document.forms["myform"][nAddress].value = "";
	document.forms["myform"][nDesc].value = "";
	document.forms["myform"][nDateCreate].value = "";
} 

//***********
// delete buttons if users don't have permission for insert/update/delete records
window.onload = function() {
    if ('${globalPermitLevel}' == 'ReadOnly' || '${globalPermitLevel}' == 'ReadAndCustomers')
	{
	document.forms["myform"]["buttonSubmit"].style.visibility="hidden";
	document.forms["myform"]["buttonAdd"].style.visibility="hidden";
	
	var flag=1; // element exists
	i= 0;
	while ((flag == 1) && (i < 1000)) {
		str = "buttonDel" + i;
		if (document.forms["myform"][str]) {
		document.forms["myform"][str].style.visibility="hidden"; }
		else {flag=0;}
	i++;}
	}
}

//*******************************************
</script>

    <div align="center">
		<br>
        <h1>Customers List</h1>
		<br>
        <form name="myform" >
            <table  name="mytable" id="mytable" border="1" width="600px" height="50px"  style="width:1100px">
                <tr>
                    <th style="width:10%">№</th>
                    <th style="width:10%">Type</th>
                    <th style="width:10%">First Name</th>
                    <th style="width:10%">Middle Name</th>
                    <th style="width:10%">Last Name</th>
                    
                    <th style="width:10%">TIN</th>
                    <th style="width:10%">Mobile P</th>
                    <th style="width:10%">Station P</th>
                    <th style="width:10%">Address</th>
                    <th style="width:10%">Description</th>
                    
                    <th style="width:10%">Create Date</th>
                    <th style="width:5%">Details</th>
                    <th style="width:5%">Delete Row</th>
					
					<c:forEach var="sup" items="${listCustomers}" varStatus="i">
                    <tr>
                        <td><input type="text" class="id" name="id${i.index}" value="${sup.id}"
                        	maxlength="7" size="7" disabled></td>
                        <td><select class="types" name="type_name${i.index}" onchange="myChangeFunction('type_name${i.index}')">
                        			<option  selected value="${sup.type_name}">${sup.type_name}</option>
    							<c:forEach items="${listTypes}" var="typ">
        							<option value="${typ.type_name}">${typ.type_name}</option>
    							</c:forEach>
							</select></td>
                        <td><input type="text" class="name" name="first_name${i.index}" value="${sup.first_name}"
                        	onchange="myChangeFunction('first_name${i.index}')"></td>
                        <td><input type="text" class="name" name="middle_name${i.index}" value="${sup.middle_name}"
                        	onchange="myChangeFunction('middle_name${i.index}')"></td>
                        <td><input type="text" class="name" name="last_name${i.index}" value="${sup.last_name}"
                        	onchange="myChangeFunction('last_name${i.index}')"></td>
                        	
                        <td><input type="text" class="tin" name="TIN${i.index}" value="${sup.TIN}"
                        	onchange="myChangeFunction('TIN${i.index}')"></td>
                        <td><input type="text" class="mobile" name="mobile_p${i.index}" value="${sup.mobile_p}"
                        	onchange="myChangeFunction('mobile_p${i.index}')"></td>
                        <td><input type="text" class="station" name="station_p${i.index}" value="${sup.station_p}"
                        	onchange="myChangeFunction('station_p${i.index}')"></td>
                        <td><input type="text" name="address${i.index}" value="${sup.address}"
                        	onchange="myChangeFunction('address${i.index}')"></td>
                        <td><input type="text" name="description${i.index}" value="${sup.description}"
                        	onchange="myChangeFunction('description${i.index}')"></td>
                        	
                        <td><input type="text" class="dt" name="date_create${i.index}" value="${sup.date_create}"
                        	onchange="myChangeFunction('date_create${i.index}')"  disabled></td>
                        <td><input type="button" value="Deals..." 
                        	onclick="window.location.replace('/customers_deals?pId=${sup.id}');"></td>
                        <td><input type="button" value="Delete" name="buttonDel${i.index}"
                        	onclick="delete_row(${i.index})"></td>
                    </tr>
					</c:forEach>
            </table>
		<br>
        <input type="button" name="buttonSubmit" value="Save" onclick="return take_values()">
        <input type="button" name="buttonAdd" value="+ Add" onclick="return new_row()">
		</form>
    </div>

<%@include file="includes/footer.jsp" %>	
	