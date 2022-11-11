<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<%@include file="includes/header.jsp" %>
<script src="https://d3js.org/d3.v4.min.js"></script>

<br>
<h1>Report: Stock Current Status</h1>
<br>

<h2>Pie Chart</h2>
    <svg width="700" height="700"></svg> <!--Step 2-->
<script>
        var svg = d3.select("svg"),
            width = svg.attr("width"),
            height = svg.attr("height"),
            radius = 320;
        var data = [//{name: "Alex", share: 20.70}, 
                    <c:forEach items="${listStock10}" var="s">
        			{name: "${s.product_name}",  share:${s.amount}},
    				</c:forEach>
								];
        var g = svg.append("g")
                   .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
        var ordScale = d3.scaleOrdinal()
                        	.domain(data)
                        	.range(['#ffd384','#94ebcd','#fbaccc','#d3e0ea','#fa7f72']);
        var pie = d3.pie().value(function(d) { 
                return d.share; 
            });
        var arc = g.selectAll("arc")
                   .data(pie(data))
                   .enter();
        var path = d3.arc()
                     .outerRadius(radius)
                     .innerRadius(0);
        arc.append("path")
           .attr("d", path)
           .attr("fill", function(d) { return ordScale(d.data.name); });
        var label = d3.arc()
                      .outerRadius(radius)
                      .innerRadius(0);
        arc.append("text")
           .attr("transform", function(d) { 
                    return "translate(" + label.centroid(d) + ")"; 
            })
           .text(function(d) { return d.data.name; })
           .style("font-family", "arial")
           .style("font-size", 15);
</script>

    <div align="center">
		<br>
        <h3>Current common cost of stock: ${sum_stock} tenge</h3>
		<br>
        <form name="myform" >
            <table  name="mytable" id="mytable" border="1" width="600px" height="50px"  style="width:700px">
                <tr>
                    <th style="width:10%">№</th>
                    <th style="width:10%">Product Name</th>
                    <th style="width:10%">Measure</th>
                    <th style="width:10%">Price</th>
                    <th style="width:10%">Quantity</th>
                    
                    <th style="width:10%">Amount</th>
                    <th style="width:10%">Description</th>
                    <th style="width:10%">Create Date</th>
					
					<c:forEach var="sup" items="${listStock}" varStatus="i">
                    <tr>
                        <td><input type="text" class="id" name="id${i.index}" value="${sup.id}"
                        	maxlength="7" size="7" disabled></td>
                        <td><input type="text" class="product_name" name="product_name${i.index}" value="${sup.product_name}" disabled></td>
                        <td><input type="text" class="types" name="measure${i.index}" value="${sup.measure}" disabled> </td>
                        <td><input type="number" class="digits" name="price${i.index}" value="${sup.price}" disabled></td>
                        <td><input type="number" class="digits" name="quantity${i.index}" value="${sup.quantity}" disabled></td>
                        	
							
                        <td><input type="number" class="digits" name="amount${i.index}" value="${sup.amount}" disabled></td>

                        <td><input type="text" name="description${i.index}" value="${sup.description}"
                        	disabled></td>
                        	
                        <td><input type="text" class="dt" name="date_create${i.index}" value="${sup.date_create}" disabled></td>

                    </tr>
					</c:forEach>
            </table>
		<br>

		</form>
    </div>

<%@include file="includes/footer.jsp" %>	
	