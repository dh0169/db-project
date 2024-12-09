<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>Oasis LMS🌴 - Kiosk</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        /* Prevent text selection */
        * {
            -webkit-touch-callout: none;
            -webkit-user-select: none;
            user-select: none;
        }
        /* Allow text selection in input fields */
        input[type="text"] {
            -webkit-user-select: text;
            user-select: text;
        }
        /* Improve touch scrolling */
        body {
            -webkit-overflow-scrolling: touch;
        }
    </style>
</head>
<body class="bg-blue-50 min-h-screen text-lg">
    <!-- Top Navigation Bar - Simplified for kiosk -->
    <nav class="bg-white shadow-lg p-6">
        <div class="max-w-5xl mx-auto flex justify-between items-center">
            <div class="flex items-center space-x-4">
                <img src="${pageContext.request.contextPath}/static/images/logo_sjsu.svg" alt="Library Logo" class="w-16 h-16">
                <h1 class="text-3xl font-bold text-blue-600">${libraryName}</h1>
            </div>
            <div class="flex items-center">
                <form action="logout" method="POST" class="inline-block">
                    <button class="bg-red-500 text-white text-xl px-6 py-3 rounded-xl hover:bg-red-600" type="submit">
                        <i class="fas fa-sign-out-alt mr-2"></i>End Session
                    </button>
                </form>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="max-w-5xl mx-auto p-4 sm:p-6">
        <div class="bg-white rounded-2xl shadow-xl p-6">
            <!-- Search Bar - Larger for touch -->
            <div class="mb-8">
                <div class="flex gap-4">
                    <input type="text" 
                           placeholder="Search by title, author, or ISBN..." 
                           class="flex-1 px-6 py-4 text-xl rounded-xl border-2 border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <button class="bg-blue-600 text-white px-8 py-4 text-xl rounded-xl hover:bg-blue-700">
                        <i class="fas fa-search mr-2"></i>Search
                    </button>
                </div>
            </div>

            <!-- Quick Stats - Larger, more touch-friendly -->
            <h2 class="text-3xl font-bold text-gray-800 mb-6">Hello ${sessionScope.user.name}!</h2>
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-6 mb-8">
                <div class="bg-blue-50 rounded-xl p-8 text-center touch-manipulation">
                    <i class="fas fa-book text-5xl text-blue-600 mb-4"></i>
                    <h3 class="text-2xl font-semibold text-gray-800">Books Checked Out</h3>
                    <p class="text-3xl font-bold text-blue-600 mt-2">${checked_count}</p>
                </div>
                <div class="bg-green-50 rounded-xl p-8 text-center touch-manipulation">
                    <i class="fas fa-clock text-5xl text-green-600 mb-4"></i>
                    <h3 class="text-2xl font-semibold text-gray-800">Due Soon</h3>
                    <p class="text-3xl font-bold text-green-600 mt-2">${due_count}</p>
                </div>
                <div class="bg-purple-50 rounded-xl p-8 text-center touch-manipulation">
                    <i class="fas fa-history text-5xl text-purple-600 mb-4"></i>
                    <h3 class="text-2xl font-semibold text-gray-800">Total Borrowed</h3>
                    <p class="text-3xl font-bold text-purple-600 mt-2">${total_count}</p>
                </div>
            </div>

            <!-- Admin Tools - Larger buttons for touch -->
            <c:if test="${user.isAdmin()}">
                <div class="mb-8 p-6 bg-yellow-50 rounded-xl">
                    <h2 class="text-3xl font-bold text-gray-800 mb-6">Administrative Tools</h2>
                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
                        <div>
                            <h3 class="text-2xl font-semibold text-gray-800 mb-4">Quick Actions</h3>
                            <div class="space-y-4">
                                <button class="w-full bg-blue-600 text-white text-xl px-6 py-4 rounded-xl hover:bg-blue-700">
                                    <i class="fas fa-plus-circle mr-2"></i>Add New Book
                                </button>
                                <button class="w-full bg-green-600 text-white text-xl px-6 py-4 rounded-xl hover:bg-green-700">
                                    <i class="fas fa-users mr-2"></i>Manage Users
                                </button>
                                <button class="w-full bg-purple-600 text-white text-xl px-6 py-4 rounded-xl hover:bg-purple-700">
                                    <i class="fas fa-chart-bar mr-2"></i>View Reports
                                </button>
                            </div>
                        </div>
                        <div class="bg-white rounded-xl p-6 shadow">
                            <h3 class="text-2xl font-semibold text-gray-800 mb-4">System Status</h3>
                            <div class="space-y-4 text-xl">
                                <div class="flex justify-between p-2">
                                    <span>Total Books:</span>
                                    <span class="font-semibold">${adminStats.totalBooks}</span>
                                </div>
                                <div class="flex justify-between p-2">
                                    <span>Books Out:</span>
                                    <span class="font-semibold">${adminStats.booksOut}</span>
                                </div>
                                <div class="flex justify-between p-2">
                                    <span>Active Users:</span>
                                    <span class="font-semibold">${adminStats.activeUsers}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Current Checkouts - Optimized for touch -->
            <div class="mb-8">
                <h2 class="text-3xl font-bold text-gray-800 mb-6">Current Checkouts</h2>
                <div class="overflow-x-auto">
                    <table class="w-full">
                        <thead class="bg-gray-50 rounded-t-xl">
                            <tr>
                                <th class="px-6 py-4 text-left text-xl text-gray-600">Book Title</th>
                                <th class="px-6 py-4 text-left text-xl text-gray-600">Due Date</th>
                                <th class="px-6 py-4 text-left text-xl text-gray-600">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-100">
                            <c:forEach items="${transactions}" var="transaction">
                                <tr class="hover:bg-gray-50">
                                    <td class="px-6 py-6 text-xl">${transaction.book.title}</td>
                                    <td class="px-6 py-6 text-xl">${transaction.returnDate}</td>
                                    <td class="px-6 py-6">
                                        <button class="bg-teal-500 text-white text-xl px-6 py-3 rounded-xl hover:bg-teal-600">
                                            <i class="fas fa-undo mr-2"></i>Return
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

        <footer class="text-center mt-10 text-gray-500">
            &copy; 2024 Oasis Library Management System. All rights reserved.
        </footer>
</body>
</html>
