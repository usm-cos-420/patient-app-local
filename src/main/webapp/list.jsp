<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="container">
	<h3>Patients</h3>
	<a href="${pageContext.request.contextPath}/create" class="btn btn-success btn-sm"> <i
		class="glyphicon glyphicon-plus"></i> Add Patient
	</a>
	<c:choose>
		<c:when test="${empty patients}">
			<p>No patients found</p>
		</c:when>
		<c:otherwise>
			<c:forEach items="${patients}" var="patient">
				<div class="media">
					<a href="${pageContext.request.contextPath}/read?id=${patient.id}">
						<div class="media-body">
							<h4>${patient.firstName}
								${patient.lastName}</h4>
							<p>${patient.birthDate}</p>
						</div>
					</a>
				</div>
			</c:forEach>
			<c:if test="${not empty cursor}">
				<nav>
					<ul class="pager">
						<li><a href="?cursor=${cursor}">More</a></li>
					</ul>
				</nav>
			</c:if>
		</c:otherwise>
	</c:choose>
</div>

