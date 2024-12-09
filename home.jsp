<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Oasis LMS🌴</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-blue-50 h-screen flex items-center justify-center">
    <div class="text-center">
        <!-- CONTENT START -->


        <img src="${pageContext.request.contextPath}/static/images/logo_sjsu.svg" alt="Library Logo" class="mx-auto mb-4 w-32 h-auto">
        <h1 class="text-5xl font-bold text-blue-600 mb-6">Welcome to ${libraryName}</h1>
        <p class="text-xl font-semibold text-gray-700 mb-6">Enter Password and Scan ID to login</p>

    <form action="login" method="POST" class="inline-block">
        <div class="mx-auto mb-6">
            <!-- Password Field -->
            <input type="password"
                   name="password" 
                   placeholder="Enter Password" 
                   class="block w-2/3 mx-auto p-3 border rounded-lg shadow-sm text-lg focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent mb-4">
        </div>

        <div class="mx-auto">
            <!-- User Scan Form -->
                <button type="submit"
                        name="user"
                        value="user@gmail.com" 
                        class="bg-teal-500 text-white text-xl font-semibold py-3 px-6 rounded-lg shadow-lg hover:bg-teal-600 transform hover:scale-105 transition">
                    Simulate user scan
                </button>
            
            <!-- Admin Scan Form -->
                <button type="submit" 
                        name="user"
                        value="admin@oasisLMS.io"
                        class="bg-red-500 text-white text-xl font-semibold py-3 px-6 rounded-lg shadow-lg hover:bg-red-600 transform hover:scale-105 transition">
                    Simulate admin scan
                </button>
        </div>
    </form>
        <!-- CONTENT STOP -->

        <div class="mx-auto">
            <footer class="mt-10 text-gray-500">
                &copy; 2024 Oasis Library Management System. All rights reserved.
            </footer>
        </div>
    </div>   
</body>
</html>
