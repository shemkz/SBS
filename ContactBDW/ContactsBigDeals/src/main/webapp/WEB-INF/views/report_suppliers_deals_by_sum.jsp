<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<%@include file="includes/header.jsp" %>
<script src="https://d3js.org/d3.v4.min.js"></script>
<br>
<h1>Report: Suppliers by sum of deals</h1>
<br>
<h2>Pie Chart</h2>
<br>
    <svg width="700" height="700"></svg> <!--Step 2-->
<script>
        var svg = d3.select("svg"),
            width = svg.attr("width"),
            height = svg.attr("height"),
            radius = 320;
        var data = [{name: "Alex", share: 20.70}, 
                    <c:forEach items="${listSuppliers_deals_by_sum_10}" var="s">
        			{name: "${s.first_name} ${s.last_name}",  share:${s.sum}},
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
</td>
</tr>
<tr>
<td>
	<form name="myform" >
            <table  name="mytable" id="mytable" border="1" width="600px" height="50px"  style="width:500px">
                <tr>
                    <th style="width:10%">Id</th>
                    <th style="width:20%">Type</th>
                    <th style="width:20%">First Name</th>
                    <th style="width:20%">Middle Name</th>
                    <th style="width:20%">Last Name</th>
                    <th style="width:10%">Sum</th> 
				</tr>
				<c:forEach var="sup" items="${listSuppliers_deals_by_sum}" varStatus="i">
                    <tr>
                        <td><input type="text" class="id" name="id${i.index}" value="${sup.id}" disabled></td>
						<td><input type="text" class="types" name="type_name${i.index}" value="${sup.type_name}" disabled></td>
						<td><input type="text" class="name" name="first_name${i.index}" value="${sup.first_name}" disabled></td>
						<td><input type="text" class="name" name="middle_name${i.index}" value="${sup.middle_name}" disabled></td>
						<td><input type="text" class="name" name="last_name${i.index}" value="${sup.last_name}" disabled></td>
						<td><input type="text" class="digits" name="sum${i.index}" value="${sup.sum}" disabled></td>
                    </tr>
				</c:forEach>
            </table>    
	</form>
<%@include file="includes/footer.jsp" %>