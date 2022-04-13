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
		<nav class="d-flex justify-content-between align-items-center">
			<h1>Bienvenid@ ${user_session.firstname}</h1>
			<a href="/logout" class="btn btn-danger">Cerrar Sesión</a>
		</nav>
		
		<div class="row">
			<h2>Eventos en tu estado</h2>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Evento</th>
						<th>Fecha</th>
						<th>Locación</th>
						<th>Estado</th>
						<th>Planner</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="i" items="${eventos_enmi_estado}">
						<tr>
							<td><a href="/events/${i.id}">${i.name}</a></td>
							<td>${i.eventDate}</td>
							<td>${i.location}</td>
							<td>${i.state}</td>
							<td>${i.planner.firstname}</td>
							<td>
								<c:if test="${i.planner.id == user.id}">
									<a href="/events/edit/${i.id}" class="btn btn-warning">Editar</a>
									<form action="/events/delete/${i.id}" method="post">
										<input type="hidden" name="_method" value="DELETE">
										<button type="submit" class="btn btn-danger">Elminar</button>
									</form>
								</c:if>
								<c:if test="${i.planner.id != user.id}">
									<c:choose>
										<c:when test="${i.attendees.contains(user)}">
											<span>ASISTIRÉ - 
												<a href="/events/remove/${i.id}" class="btn btn-danger">Ya no asistiré</a>
											</span>
										</c:when>
										<c:otherwise>
											<a href="/events/join/${i.id}" class="btn btn-success">Asistir</a>
										</c:otherwise>
									</c:choose>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="row">
			<h2>Eventos en otros estados</h2>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Evento</th>
						<th>Fecha</th>
						<th>Locación</th>
						<th>Estado</th>
						<th>Planner</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="i" items="${eventos_noenmi_estado}">
						<tr>
							<td><a href="/events/${i.id}">${i.name}</a></td>
							<td>${i.eventDate}</td>
							<td>${i.location}</td>
							<td>${i.state}</td>
							<td>${i.planner.firstname}</td>
							<td>
								<c:if test="${i.planner.id == user.id}">
									<a href="/events/edit/${i.id}" class="btn btn-warning">Editar</a>
									<form action="/events/delete/${i.id}" method="post">
										<input type="hidden" name="_method" value="DELETE">
										<button type="submit" class="btn btn-danger">Eliminar</button>
									</form>
								</c:if>
								<c:if test="${i.planner.id != user.id}">
									<c:choose>
										<c:when test="${i.attendees.contains(user)}">
											<span>ASISTIRÉ - 
												<a href="/events/remove/${i.id}" class="btn btn-danger">Ya no asistiré</a>
											</span>
										</c:when>
										<c:otherwise>
											<a href="/events/join/${i.id}" class="btn btn-success">Asistir</a>
										</c:otherwise>
									</c:choose>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="row">
			<h2>Crear evento</h2>
			<form:form action="/events/create" method="post" modelAttribute="event">
				<form:hidden path="planner" value="${user_session.id}" />
				<div class="form-group">
					<form:label path="name">Nombre:</form:label>
					<form:input path="name" class="form-control"/>
					<form:errors path="name" class="text-danger"/>
				</div>
				<div class="form-group">
					<form:label path="eventDate">Fecha:</form:label>
					<form:input path="eventDate" type="date" class="form-control"/>
					<form:errors path="eventDate" class="text-danger"/>
				</div>
				<div class="form-group">
					<form:label path="location">Locación:</form:label>
					<form:input path="location" class="form-control"/>
					<form:errors path="location" class="text-danger"/>
				</div>
				<div class="form-group">
					<form:label path="state">Estado:</form:label>
					<form:select path="state" class="form-control">
						<c:forEach var="i" items="${states}">
							<option value="${i}">${i}</option>
						</c:forEach>
					</form:select>
					<form:errors path="state" class="text-danger" />
				</div>
				<input type="submit" value="Crear Evento" class="btn btn-success">
			</form:form>
		</div>
		
	</div>
</body>
</html>