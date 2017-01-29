<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Users List</title>
	<link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
	<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>

<body>
	<div class="generic-container">
		<%@include file="authheader.jsp" %>	
		<div class="panel panel-default">
			  <!-- Default panel contents -->
		  	<div class="panel-heading"><span class="lead">List of Applications </span></div>
			<table class="table table-hover">
	    		<thead>
		      		<tr>
				        <th>Name</th>
				        <th>URL</th>
				        <th>Description</th>
						<th>Added By</th>
				        <sec:authorize access="hasRole('PUBLISHER') or hasRole('OPERATOR')">
				        	<th width="100"></th>
				        </sec:authorize>
				        <sec:authorize access="hasRole('PUBLISHER') or hasRole('OPERATOR')">
				        	<th width="100"></th>
				        </sec:authorize>
				        
					</tr>
		    	</thead>
	    		<tbody>
				<c:forEach items="${registeredApps}" var="app">
					<tr>
						<td>${app.name}</td>
						<td>${app.url}</td>
						<td>${app.description}</td>
						<td>${app.addedBy}</td>
					    <%--<sec:authorize access="hasRole('ADMIN') or hasRole('OPERATOR')">--%>
							<%--<td><a href="<c:url value='/edit-user-${user.ssoId}' />" class="btn btn-success custom-width">edit</a></td>--%>
				        <%--</sec:authorize>--%>
				        <%--<sec:authorize access="hasRole('ADMIN')">--%>
							<%--<td><a href="<c:url value='/delete-user-${user.ssoId}' />" class="btn btn-danger custom-width">delete</a></td>--%>
        				<%--</sec:authorize>--%>
					</tr>
				</c:forEach>
	    		</tbody>
	    	</table>
		</div>
		<sec:authorize access="hasRole('PUBLISHER') or hasRole('OPERATOR')">
		 	<div class="well">
		 		<%--<a href="<c:url value='/newuser' />">Add New Application</a>--%>
		 		<a href="javascript;">Add New Application</a>
		 	</div>
	 	</sec:authorize>
   	</div>
</body>
</html>