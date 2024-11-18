<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="head.jsp" %>

<!--CONTENT START-->

<img src="${pageContext.request.contextPath}/images/logo_sjsu.jpeg" alt="Library Logo" class="mx-auto mb-4 w-32 h-auto">
<h1 class="text-5xl font-bold text-blue-600 mb-6">Welcome to ${libraryName}</h1>
<p class="text-xl font-semibold text-gray-700 mb-6">Scan ID to continue</p>

<div class="mx-auto">
    <button class="bg-teal-500 text-white text-xl font-semibold py-3 px-6 rounded-lg shadow-lg hover:bg-teal-600 transform hover:scale-105 transition">
        Simulate user scan
    </button>
    <button class="bg-red-500 text-white text-xl font-semibold py-3 px-6 rounded-lg shadow-lg hover:bg-red-600 transform hover:scale-105 transition">
        Simulate admin scan
    </button>
</div>

<!--CONTENT STOP-->


<%@ include file="foot.jsp" %>
