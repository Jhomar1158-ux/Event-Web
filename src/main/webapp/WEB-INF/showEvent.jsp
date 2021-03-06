<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
		<div class="row">
			<div class="col-6">
				<h1>${evento.name}</h1>
				<p><b>Planner:</b> ${evento.planner.firstname}</p>
				<p><b>Fecha:</b> ${evento.eventDate}</p>
				<p><b>Locaci?n:</b> ${evento.location}</p>
				<p><b>Estado:</b> ${evento.state}</p>
				<p><b>Cantidad de personas:</b> ${evento.attendees.size()}</p>
				<table class="table table-hover">
					<thead>
						<tr>
							<th>Nombre</th>
							<th>Locaci?n</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="user" items="${evento.attendees}">
							<tr>
								<td>${user.firstname}</td>
								<td>${user.location}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="col-6">
				<h2>Muro de Mensajes</h2>
				<div><!-- Espacio para muro -->
					<c:forEach var="m" items="${evento.messages}">
						<p>${m.author.firstname} dice: ${m.content}</p>
					</c:forEach>
				</div>
				<form:form action="/events/message" method="post" modelAttribute="message">
					<div class="form-group">
						<form:label path="content">Agregar Comentario</form:label>
						<form:textarea path="content" class="form-control"/>
						<form:errors path="content" class="text-danger"/>
						<form:hidden path="author" value="${user_session.id}"/>
						<form:hidden path="event" value="${evento.id}"/>
						<input type="submit" class="btn btn-primary" value="Enviar">
					</div>
				</form:form>
			</div>
			<a href="/dashboard" class="btn btn-warning">Regresar</a>
			
		</div>
	</div>
</body>
</html>