<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<%@include file="includes/header.jsp" %>

<script type="text/javascript">
//**********************
// save records
function save_values(){
	var flag=1; // element exists
	i= - minusI;
	
	// loop by exists element
	while ((flag == 1) && (i < 1000)) {
	
		if (i >= 0) {iStr = ""+i;}// for positive index
			else {iStr = "S"+i*(-1);} // for negative index
 	
		nId = "id"+iStr;
		nIdSupplier = "id_supplier"+iStr;
		nNameGood = "name_good"+iStr;
		nPrice = "price"+iStr;
		nQuantity = "quantity"+iStr;
 
		nSum = "sum"+iStr;
		nDetails = "details"+iStr;
		nDateDeal = "date_deal"+iStr;

		if(typeof(document.forms["myform"][nId]) == 'undefined' || document.forms["myform"][nId] == null)
			{flag=0;} // element not exist

		if ((i < 0) || // negative index
		((document.forms["myform"][nId]) && // element exists
		(document.forms["myform"][nIdSupplier].style.color == 'red' ||
		document.forms["myform"][nNameGood].style.color == 'red' ||
		document.forms["myform"][nPrice].style.color == 'red' ||
		document.forms["myform"][nQuantity].style.color == 'red' ||
 
		document.forms["myform"][nSum].style.color == 'red' ||
		document.forms["myform"][nDetails].style.color == 'red')))
			{//.style.color == 'red' - changed
			p1=document.forms["myform"][nId].value;
			p2=document.forms["myform"][nIdSupplier].value;
			p3=document.forms["myform"][nNameGood].value;
			p4=document.forms["myform"][nPrice].value;
			p5=document.forms["myform"][nQuantity].value;

			p6=p4*p5;
			p7=document.forms["myform"][nDetails].value;
			p8=document.forms["myform"][nDateDeal].value;
			//alert("|767|:"+nId+":");
			if (p1==null || p1=="") {
				alert("Errors on the table!");
				return false;
				}else{
				// send and fetch data to / from server without refreshing page
				var http = new XMLHttpRequest();
				http.open("POST", "/suppliers_deals_save", true);
				http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				// probably use document.getElementById(...).value
				var params = "p1=" + p1 + "&p2=" + p2 + "&p3=" + p3 + "&p4=" + p4 + "&p5=" + p5
 				 + "&p6=" + p6 + "&p7=" + p7 + "&p8=" + p8;
				http.send(params);
				http.onload = function() {  }//alert("+++"+http.responseText);
				} 
			}//.style.color == 'red' - changed
	i++; }//loop for
}//function take_values()
 
//**************************
// change element's color to red
function myChangeFunction(name){
	//var x=document.getElementById('id_1');  
	var x=document.forms["myform"][name];
        x.style.color='red';
} 

//************************

var minusI = 0;// global var - for negative index - new row
var newId = "";// global var - for id

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
	var cell6 = row.insertCell(5);
	var cell7 = row.insertCell(6);
	var cell8 = row.insertCell(7);
	var cell9 = row.insertCell(8);

	// fill new cells by data
	cell1.innerHTML = '<input type="text" class="id" name="idS' + minusI + '" value="' + newId + '" disabled>';
	cell2.innerHTML = '<input type="text" class="id_customer" name="id_supplierS' + minusI + '" value="${pId}" disabled>';
	cell3.innerHTML = '<input type="text" name="name_goodS' + minusI + '" value="">';
	cell4.innerHTML = '<input type="number" class="digits" name="priceS' + minusI + '" value="0"  >';
	cell5.innerHTML = '<input type="number" class="digits" name="quantityS' + minusI + '" value="0"   >';
  
	cell6.innerHTML = '<input type="number" class="digits" name="sumS' + minusI + '" value="0"  >';
	cell7.innerHTML = '<input type="text" name="detailsS' + minusI + '" value="">';
	cell8.innerHTML = '<input type="text" class="dt" name="date_dealS' + minusI + '" value="TODAY" disabled>';

	// send data to MvcController without refreshing page
	var http = new XMLHttpRequest();
	http.open("POST", "/suppliers_deals_insert", true);
	http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	var params='p1='+'${pId}';
	http.send(params);

	// fetch data from server
	http.onload = function() { 
		newId=http.responseText;  
		nId = "idS" + minusI;
		document.forms["myform"][nId].value = newId;
	}//  http.onload            
}

//*****************************

//delete records
function delete_row(i){

	if (i >= 0) {iStr = ""+i;}// for positive index
		else {iStr = "S"+i*(-1);} // for negative index
 	
	nId = "id"+iStr;
	nIdSupplier = "id_supplier"+iStr;
	nNameGood = "name_good"+iStr;
	nPrice = "price"+iStr;
	nQuantity = "quantity"+iStr;
 
	nSum = "sum"+iStr;
	nDetails = "details"+iStr;
	nDateDeal = "date_deal"+iStr;
 
	// send and fetch data to / from server without refreshing page
	p1 = document.forms["myform"][nId].value;
	p2 = document.forms["myform"][nNameGood].value;
	var http = new XMLHttpRequest();
	http.open("POST", "/suppliers_deals_delete", true);
	http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	var params = "p1=" + p1 + "&p2=" + p2;
	http.send(params);
	http.onload = function() { alert(http.responseText);  }//  http.onload  
 
	// clear cells of removed records
	document.forms["myform"][nId].value = "";
	document.forms["myform"][nIdSupplier].value = "";
	document.forms["myform"][nNameGood].value = "";
	document.forms["myform"][nPrice].value = "";
	document.forms["myform"][nQuantity].value = "";

	document.forms["myform"][nSum].value = "";
	document.forms["myform"][nDetails].value = "";
	document.forms["myform"][nDateDeal].value = "";

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

//****************************************************************************
</script>

    <div align="center">
		<br>
        <h1>Supplier Deal List</h1>
		<br>
		<h2>${nameSupplier} - ${typeSupplier}</h2>
		<br>
        <form name="myform" >
            <table  name="mytable" id="mytable" border="1" width="600px" height="50px"  style="width:800px">
                <tr>
                    <th style="width:10%">№</th>
                    <th style="width:10%">Id Supplier</th>
                    <th style="width:10%">Name Good</th>
                    <th style="width:10%">Price</th>
                    <th style="width:10%">Quantity</th>
                    
                    <th style="width:10%">Sum</th>
                    <th style="width:10%">Details</th>
                    <th style="width:10%">Deal Date</th>
                    <th style="width:5%">Delete Row</th>
				</tr>
				<c:forEach var="sup" items="${listSuppliers_deals}" varStatus="i">
                    <tr>

                        <td><input type="text" class="id" name="id${i.index}" value="${sup.id}"
                        	maxlength="7" size="7" disabled></td>
                        <td><input type="text" class="id_customer" name="id_supplier${i.index}" value="${sup.id_supplier}"
                        	onchange="myChangeFunction('id_supplier${i.index}')" disabled></td>
                        <td><input type="text" name="name_good${i.index}" value="${sup.name_good}"
                        	onchange="myChangeFunction('name_good${i.index}')"></td>
                        <td><input type="number" class="digits" name="price${i.index}" value="${sup.price}"
                        	onchange="myChangeFunction('price${i.index}')"></td>
                        <td><input type="number" class="digits" name="quantity${i.index}" value="${sup.quantity}"
                        	onchange="myChangeFunction('quantity${i.index}')"></td>
                        	
                        <td><input type="number" class="digits" name="sum${i.index}" value="${sup.sum}"
                        	onchange="myChangeFunction('sum${i.index}')"></td>
                        <td><input type="text" name="details${i.index}" value="${sup.details}"
                        	onchange="myChangeFunction('details${i.index}')"></td>
                        <td><input type="text" class="dt" name="date_deal${i.index}" value="${sup.date_deal}" disabled></td>
                        <td><input type="button" value="Delete" name="buttonDel${i.index}"
                        	onclick="delete_row(${i.index})"></td>
                    </tr>
				</c:forEach>
            </table>
		<br>
        <input type="button" name="buttonSubmit" value="Submit" onclick="return save_values()">
        <input type="button" name="buttonAdd" value="+ Add" onclick="return new_row()">
		</form>
    </div>
	<br>
<%@include file="includes/footer.jsp" %>
	