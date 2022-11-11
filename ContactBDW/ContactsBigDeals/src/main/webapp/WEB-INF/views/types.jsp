<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<%@include file="includes/header.jsp" %>

<script type="text/javascript">
//*********************************************
 
// save records
function take_values(){
	var flag=1; // element exists
	i= - minusI;
	
	// loop by exists element
	while ((flag == 1) && (i < 1000)) {
 
		if (i >= 0) {iStr = ""+i;}// for positive index
			else {iStr = "S"+i*(-1);} // for negative index
 	
		nId = "id"+iStr;
		nTypeName = "type_name"+iStr;
		nDesc = "description"+iStr;
		//alert("|||:"+nId+":");//+document.forms["myform"][nId].value
		if(typeof(document.forms["myform"][nId]) == 'undefined' || document.forms["myform"][nId] == null)
			{flag=0;} // element not exist
 
		if ((i < 0) || // negative index
		((document.forms["myform"][nId]) && // element exists
		(document.forms["myform"][nTypeName].style.color == 'red' ||
		document.forms["myform"][nDesc].style.color == 'red')))
		{//.style.color == 'red' - changed
			p1=document.forms["myform"][nId].value;
			p2=document.forms["myform"][nTypeName].value;
			p3=document.forms["myform"][nDesc].value;
 
			if (p1==null || p1=="") {
				alert("Errors on the table!");
				return false;
				}else{
				// send and fetch data to / from server without refreshing page
				var http = new XMLHttpRequest();
				http.open("POST", "/types_save", true);
				http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				var params = "p1=" + p1 + "&p2=" + p2 + "&p3=" + p3;
				http.send(params);
				http.onload = function() { alert("+++"+http.responseText); }
				} 
		}//.style.color == 'red' - changed
	i++; }//loop while
}//function take_values()

//***********

// change element's color to red
function myChangeFunction(name){
	//var x=document.getElementById('id_1');  
	var x=document.forms["myform"][name];
        x.style.color='red';
} 

//***********

var minusI = 0;// for negative index - new row
var newId = "";

// add new row
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

	// fill new cells by data
	cell1.innerHTML = '<input type="text" class="id" name="idS' + minusI + '" value="' + newId + '" disabled>';
	cell2.innerHTML = '<input type="text" class="name" name="type_nameS' + minusI + '" value="">';
	cell3.innerHTML = '<input type="text" name="descriptionS' + minusI + '" value="">';
	cell4.innerHTML = '<input type="text" class="dt" name="date_createS' + minusI + '" value="TODAY">';

	// send data to MvcController without refreshing page
	var http = new XMLHttpRequest();
	http.open("POST", "/types_insert", true);
	http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	var params='p1='+'Направление';
	http.send(params);

	// fetch data from server
	http.onload = function() { 
		newId=http.responseText;  
		nId = "idS" + minusI;
		document.forms["myform"][nId].value = newId;

		}//  http.onload            
}

//***********

//delete records
function delete_row(i){

	if (i >= 0) {iStr = ""+i;}// for positive index
		else {iStr = "S"+i*(-1);} // for negative index
 	
	nId = "id"+iStr;
	nTypeName = "type_name"+iStr;
	nDateCreate = "date_create"+iStr;
	nDesc = "description"+iStr;
 
	// send and fetch data to / from server without refreshing page
	p1 = document.forms["myform"][nId].value;
	p2 = document.forms["myform"][nTypeName].value;
	var http = new XMLHttpRequest();
	http.open("POST", "/types_delete", true);
	http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	var params = "p1=" + p1 + "&p2=" + p2;
	http.send(params);
	http.onload = function() { alert(http.responseText);  }//  http.onload  
 
	// clear cells of removed records
	document.forms["myform"][nId].value = "";
	document.forms["myform"][nTypeName].value = "";
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
        <h1>Product and service Type List</h1>
		<br>
        <form name="myform" >
            <table  name="mytable" id="mytable" border="1" height="50px"  style="width:400px">
                <tr>
                    <th style="width:10%">№</th>
                    <th style="width:10%">Type Name</th>
                    <th style="width:10%">Description</th>
                    <th style="width:10%">Create Date</th>
                    <th style="width:5%">Delete Row</th>
					
					<c:forEach var="sup" items="${listTypes}" varStatus="i">
                    <tr>
                        <td><input type="text" class="id" name="id${i.index}" value="${sup.id}"
                        	maxlength="7" size="7" disabled></td>

                        <td><input type="text" class="name" name="type_name${i.index}" value="${sup.type_name}"
                        	onchange="myChangeFunction('type_name${i.index}')"></td>
                        
                        <td><input type="text" name="description${i.index}" value="${sup.description}"
                        	onchange="myChangeFunction('description${i.index}')"></td>
                        <td><input type="text" class="dt" name="date_create${i.index}" value="${sup.date_create}"
                        	onchange="myChangeFunction('date_create${i.index}')"  disabled></td>
                        <td><input type="button" value="Delete" name="buttonDel${i.index}"
                        	onclick="delete_row(${i.index})"></td>
                    </tr>
					</c:forEach>
            </table>
		<br>
        <input type="button" name="buttonSubmit" value="Submit" onclick="return take_values()">
        <input type="button" name="buttonAdd" value="+ Add" onclick="return new_row()">
		</form>
    </div>

<%@include file="includes/footer.jsp" %>	
	