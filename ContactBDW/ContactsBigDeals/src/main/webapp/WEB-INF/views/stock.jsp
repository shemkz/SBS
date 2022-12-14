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
		nProductName = "product_name"+iStr;
		nMeasure = "measure"+iStr;
		nPrice = "price"+iStr;
		nQuantity = "quantity"+iStr;
 
		nSmount = "amount"+iStr;
		nDateCreate = "date_create"+iStr;
		nDesc = "description"+iStr;
		//alert("|||:"+nId+":");//+document.forms["myform"][nId].value
		if(typeof(document.forms["myform"][nId]) == 'undefined' || document.forms["myform"][nId] == null)
			{flag=0;} // element not exist
 
		if ((i < 0) || // negative index
		((document.forms["myform"][nId]) && // element exists
		(document.forms["myform"][nProductName].style.color == 'red' ||
		document.forms["myform"][nMeasure].style.color == 'red' ||
		document.forms["myform"][nPrice].style.color == 'red' ||
		document.forms["myform"][nQuantity].style.color == 'red' ||
		
		document.forms["myform"][nSmount].style.color == 'red' ||
		document.forms["myform"][nDesc].style.color == 'red')))
		{//.style.color == 'red' - changed
			p1=document.forms["myform"][nId].value;
			p2=document.forms["myform"][nProductName].value;
			p3=document.forms["myform"][nMeasure].value;
			p4=document.forms["myform"][nPrice].value;
			p5=document.forms["myform"][nQuantity].value;

			p6=p4*p5;
			p7=document.forms["myform"][nDesc].value;
 
			if (p1==null || p1=="") {
				alert("Errors on the table!");
				return false;
				}else{
				var http = new XMLHttpRequest();
				http.open("POST", "/stock_save", true);
				http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				var params = "p1=" + p1 + "&p2=" + p2 + "&p3=" + p3 + "&p4=" + p4 + "&p5=" + p5
 				 + "&p6=" + p6 + "&p7=" + p7 ;
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


	// fill new cells
	cell1.innerHTML = '<input type="text" class="id" name="idS' + minusI + '" value="' + newId + '" disabled>';
	cell2.innerHTML = `<select class="product_name" name="product_nameS` + minusI + `" ><option  selected value="Goods">Goods</option><c:forEach items="${listProducts}" var="typ"><option value="${typ.product_name}">${typ.product_name}</option></c:forEach></select>`;
	
	cell3.innerHTML = `<select class="types" name="measureS` + minusI + `"><option  selected value="????????????">????????????</option><c:forEach items="${listMeasure}" var="m"><option value="${m}">${m}</option></c:forEach></select>`;
	
	cell4.innerHTML = '<input type="number" class="digits" name="priceS' + minusI + '" value="0">';
	cell5.innerHTML = '<input type="number" class="digits" name="quantityS' + minusI + '" value="0">';
	cell6.innerHTML = '<input type="number" class="digits" name="amountS' + minusI + '" value="0">';
	cell7.innerHTML = '<input type="text" name="descriptionS' + minusI + '" value="">';
	cell8.innerHTML = '<input type="text" class="dt" name="date_createS' + minusI + '" value="TODAY">';

	// send data to MvcController without refreshing page
	var http = new XMLHttpRequest();
	http.open("POST", "/stock_insert", true);
	http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	var params="p1=Goods&p2=????????????&p3=0&p4=0&p5=0";
	http.send(params);

	// fetch data from server
	http.onload = function() { 
		newId=http.responseText;  
		nId = "idS" + minusI;
		document.forms["myform"][nId].value = newId;

		}//  http.onload            
}

//***********

function delete_row(i){

	if (i >= 0) {iStr = ""+i;}// for positive index
		else {iStr = "S"+i*(-1);} // for negative index
 	
	nId = "id"+iStr;
	nProductName = "product_name"+iStr;
	nMeasure = "measure"+iStr;
	nPrice = "price"+iStr;
	nQuantity = "quantity"+iStr;
 
	nAmount = "amount"+iStr;
	nDesc = "description"+iStr;
	nDateCreate = "date_create"+iStr;
	
	// send and fetch data to / from server without refreshing page
	p1 = document.forms["myform"][nId].value;
	var http = new XMLHttpRequest();
	http.open("POST", "/stock_delete", true);
	http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	var params = "p1=" + p1;
	http.send(params);
	http.onload = function() { alert(http.responseText);  }//  http.onload  
 
	// clear cells of removed records
	document.forms["myform"][nId].value = "";
	document.forms["myform"][nProductName].value = "";
	document.forms["myform"][nMeasure].value = "";
	document.forms["myform"][nPrice].value = "";
	document.forms["myform"][nQuantity].value = "";

	document.forms["myform"][nAmount].value = "";
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
        <h1>Stock List</h1>
		<br>
        <form name="myform" >
            <table  name="mytable" id="mytable" border="1" width="600px" height="50px"  style="width:800px">
                <tr>
                    <th style="width:10%">???</th>
                    <th style="width:10%">Product Name</th>
                    <th style="width:10%">Measure</th>
                    <th style="width:10%">Price</th>
                    <th style="width:10%">Quantity</th>
                    
                    <th style="width:10%">Amount</th>
                    <th style="width:10%">Description</th>
                    <th style="width:10%">Create Date</th>
                    <th style="width:5%">Delete Row</th>
					
					<c:forEach var="sup" items="${listStock}" varStatus="i">
                    <tr>
                        <td><input type="text" class="id" name="id${i.index}" value="${sup.id}"
                        	maxlength="7" size="7" disabled></td>
                        <td><select class="product_name" name="product_name${i.index}" onchange="myChangeFunction('product_name${i.index}')">
                        			<option  selected value="${sup.product_name}">${sup.product_name}</option>
    							<c:forEach items="${listProducts}" var="typ">
        							<option value="${typ.product_name}">${typ.product_name}</option>
    							</c:forEach>
							</select></td>
                        <td><select class="types" name="measure${i.index}" onchange="myChangeFunction('measure${i.index}')">
                        			<option  selected value="${sup.measure}">${sup.measure}</option>
    							    <c:forEach items="${listMeasure}" var="m">
        							<option value="${m}">${m}</option>
									</c:forEach>
							</select></td>
                        <td><input type="number" class="digits" name="price${i.index}" value="${sup.price}"
                        	onchange="myChangeFunction('price${i.index}')"></td>
                        <td><input type="number" class="digits" name="quantity${i.index}" value="${sup.quantity}"
                        	onchange="myChangeFunction('quantity${i.index}')"></td>
                        	
							
                        <td><input type="number" class="digits" name="amount${i.index}" value="${sup.amount}"
                        	 disabled></td>

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
        <input type="button" name="buttonSubmit" value="Save" onclick="return take_values()">
        <input type="button" name="buttonAdd" value="+ Add" onclick="return new_row()">
		</form>
    </div>

<%@include file="includes/footer.jsp" %>	
	