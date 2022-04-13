<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isErrorPage="true" %>   
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>
	<div class="container">
		<h2>Editar Evento</h2>
		<form:form action="/events/update" method="post" modelAttribute="evento">
			<input type="hidden" name="_method" value="put">
			<form:hidden path="planner" value="${user_session.id}"/>
			<form:hidden path="id" value="${evento.id}"/>
			<div class="form-group">
				<form:label path="name">Nombre:</form:label>
				<form:input path="name" class="form-control"/>
				<form:errors path="name" class="text-danger" />
			</div>
			<div class="form-group">
				<form:label path="eventDate">Fecha:</form:label>
				<form:input path="eventDate" type="date" class="form-control"/>
				<form:errors path="eventDate" class="text-danger" />
			</div>
			<div class="form-group">
				<form:label path="location">Locación:</form:label>
				<form:input path="location" class="form-control"/>
				<form:errors path="location" class="text-danger" />
			</div>
			<div class="form-group">
				<form:label path="state">Estado:</form:label>
				<form:select path="state" class="form-control">
					<c:forEach var="i" items="${states}">
						<c:choose>
							<c:when test="${i.equals(evento.state)}">
								<option selected value="${i}">${i}</option>
							</c:when>
							<c:otherwise>
								<option value="${i}">${i}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</form:select>
				<form:errors path="state" class="text-danger" />
			</div>
			<input type="submit" class="btn btn-success" value="Actualizar">
		</form:form>
		<a href="/dashboard" class="btn btn-warning">Regresar</a>
	</div>
</body>
</html>