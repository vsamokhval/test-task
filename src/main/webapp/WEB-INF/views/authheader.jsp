<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="authbar">
	<span>Current user: <strong>${loggedinuser}</strong>.</span>

	<span class="floatRight"><a href="<c:url value="/logout" />">Logout</a></span>

	<sec:authorize access="hasRole('OPERATOR')">
		<c:choose>
			<c:when test="${userlist}">
				<span class="floatRight"><a href="<c:url value="/appslist" />">Go to list of application</a></span>
			</c:when>
			<c:otherwise>
				<span class="floatRight"><a href="<c:url value="/list" />">Go to list of users</a></span>
			</c:otherwise>
		</c:choose>
	</sec:authorize>

</div>
