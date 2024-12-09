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
        input[type="text"], input[type="email"], input[type="password"] {
            -webkit-user-select: text;
            user-select: text;
        }
        body {
            -webkit-overflow-scrolling: touch;
        }
        /* Modal styles */
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 1000;
            justify-content: center;
            align-items: center;
        }
        .modal-content {
            background-color: white;
            padding: 2rem;
            border-radius: 1rem;
            text-align: center;
            width: 400px;
        }
        #returnModal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 1000;
            justify-content: center;
            align-items: center;
        }
        #returnModalContent {
            background-color: white;
            padding: 2rem;
            border-radius: 1rem;
            text-align: center;
            width: 300px;
        }
        #spinner {
            border: 4px solid #f3f3f3;
            border-top: 4px solid #3498db;
            border-radius: 50%;
            width: 40px;
            height: 40px;
            animation: spin 1s linear infinite;
            margin: 1rem auto;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>
    <script>
        function openReturnModal() {
            document.getElementById("returnModal").style.display = "flex";
        }

        function closeReturnModal() {
            document.getElementById("returnModal").style.display = "none";
            sendPostRequestToDashboard();
        }

        function sendPostRequestToDashboard() {
            fetch('/dashboard', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'same-origin', // Ensures cookies and session context are included
                body: JSON.stringify({ action: 'return' }) // Include any relevant data
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                console.log('Post request successful!');
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });
        }

        function openModal(modalId) {
            document.getElementById(modalId).style.display = "flex";
        }

        function closeModal(modalId) {
            document.getElementById(modalId).style.display = "none";
        }

        function issueDatabaseDump() {
            fetch('/databaseDump', {
                method: 'POST',
                credentials: 'same-origin'
            })
            .then(response => {
                if (response.ok) {
                    alert('Database dump completed successfully!');
                } else {
                    alert('Failed to complete database dump.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
        }
    </script>
</head>
<body class="bg-blue-50 min-h-screen text-lg">
<!-- Top Navigation Bar -->
<nav class="bg-white shadow-lg p-4">
    <div class="max-w-6xl mx-auto flex items-center justify-between space-x-6">
        <div class="flex items-center space-x-4">
            <h1 class="text-2xl font-bold text-blue-600">${libraryName}</h1>
            <img src="${pageContext.request.contextPath}/static/images/logo_sjsu.svg" alt="Library Logo" class="w-14 h-14">
        </div>
        <div class="flex-1 max-w-lg">
            <input type="text" 
                   placeholder="Search by title, author, or ISBN..." 
                   class="w-full px-4 py-3 text-base rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500">
        </div>
        <div>
            <form action="logout" method="POST" class="flex items-center">
                <button class="bg-red-500 text-white text-base px-4 py-2 rounded-lg hover:bg-red-600 flex items-center" type="submit">
                    <i class="fas fa-sign-out-alt mr-2"></i>Logout
                </button>
            </form>
        </div>
    </div>
</nav>

<!-- Main Content -->
<div class="max-w-5xl mx-auto p-4 sm:p-6">
    <div class="bg-white rounded-2xl shadow-xl p-6">
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

        <!-- Admin Tools -->
        <c:if test="${user.isAdmin()}">
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
                <div>
                    <h3 class="text-2xl font-semibold text-gray-800 mb-4">Quick Actions</h3>
                    <div class="space-y-4">
                        <button class="w-full bg-blue-600 text-white text-xl px-6 py-4 rounded-xl hover:bg-blue-700" 
                        onclick="openModal('addBookModal')">
                            <i class="fas fa-plus-circle mr-2"></i>Add New Book
                        </button>
                        <button class="w-full bg-green-600 text-white text-xl px-6 py-4 rounded-xl hover:bg-green-700" 
                                onclick="openModal('addUserModal')">
                            <i class="fas fa-users mr-2"></i>Manage Users
                        </button>
                        <button class="w-full bg-purple-600 text-white text-xl px-6 py-4 rounded-xl hover:bg-purple-700"
                                onclick="issueDatabaseDump()">
                            <i class="fas fa-database mr-2"></i>Database Dump
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
        </c:if>

        <!-- Current Checkouts -->
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
                                    <button class="bg-teal-500 text-white text-xl px-6 py-3 rounded-xl hover:bg-teal-600" 
                                            onclick="openReturnModal()">
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

<!-- Return Modal -->
<div id="returnModal">
    <div id="returnModalContent">
        <div id="spinner"></div>
        <p class="text-xl font-semibold text-gray-700">Please scan book to return</p>
        <button class="mt-4 bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600" onclick="closeReturnModal()">
            Simulate Scan
        </button>
    </div>
</div>

<!-- Add Book Modal -->
<div id="addBookModal" class="modal">
    <div class="modal-content">
        <h3 class="text-2xl font-bold mb-4">Add New Book</h3>
        <form action="/addBook" method="POST">
            <input type="text" name="title" placeholder="Book Title" class="w-full mb-4 px-3 py-2 border rounded-lg">
            <input type="text" name="author" placeholder="Author" class="w-full mb-4 px-3 py-2 border rounded-lg">
            <input type="text" name="isbn" placeholder="ISBN" class="w-full mb-4 px-3 py-2 border rounded-lg">
            <div class="flex justify-between">
                <button type="button" class="bg-gray-500 text-white px-4 py-2 rounded-lg" onclick="closeModal('addBookModal')">Cancel</button>
                <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded-lg">Add</button>
            </div>
        </form>
    </div>
</div>

<!-- Add User Modal -->
<div id="addUserModal" class="modal">
    <div class="modal-content">
        <h3 class="text-2xl font-bold mb-4">Add New User</h3>
        <form action="/addUser" method="POST">
            <input type="text" name="name" placeholder="Name" class="w-full mb-4 px-3 py-2 border rounded-lg">
            <input type="email" name="email" placeholder="Email" class="w-full mb-4 px-3 py-2 border rounded-lg">
            <input type="password" name="password" placeholder="Password" class="w-full mb-4 px-3 py-2 border rounded-lg">
            <div class="flex justify-between">
                <button type="button" class="bg-gray-500 text-white px-4 py-2 rounded-lg" onclick="closeModal('addUserModal')">Cancel</button>
                <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded-lg">Add</button>
            </div>
        </form>
    </div>
</div>

<footer class="text-center mt-10 text-gray-500">
    &copy; 2024 Oasis Library Management System. All rights reserved.
</footer>
</body>
</html>




                