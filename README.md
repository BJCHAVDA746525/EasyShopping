EasyShopping
The EasyShopping is an android-based ecommerce app which makes the shopping very smooth and efficient for its users. Users can search for products easily, add them to a shopping cart, and even order them safely using Firebase for authentication, database, and storage back-end services.

Features
Product Browsing: View different products along with their details like name, price, description, etc.
Use Firebase Authentication for secure user sign-up and login functionality
Users are able to add items into shopping cart, change quantities, remove items
Order Placement 
Place order securely using Firebase Firestore for storing information related to the order
Simpler search for products using an intuitive search bar
Firebase Integration : Real-time update using Firestore along with user authentication using Firebase
Technologies Used
Frontend (Android App): 
Java(Android SDK)
XML for UI layout
Backend:

Firebase Authentication (for sign-up, login, and authentication)
Firebase Firestore (for storing data of products, orders, and cart)
Firebase Storage (for product images)
Other Tools:

Android Studio (development environment)
Firebase Console (to manage backend services)
Glide (to load images from Firebase Storage)
Installation
Prerequisites
Android Studio must be installed on your computer.
You have a Firebase project set up. If you haven't, follow the Firebase Setup Instructions
Steps
Clone the repository:

 Open the project in Android Studio:

Launch Android Studio.
Select "Open an existing Android project" and navigate to the cloned directory.
Firebase Configuration:
   In Firebase Console, create a Firebase project.
Get google-services.json file from your Firebase project and include it in your Android project in app/ directory.
Building Project
   Open Android Studio, let's allow it to sync all the necessary Gradle dependencies
after a successful synchronization the project should be built
Run App

Android Emulator or Connected Device
Use the app by running it from Android Studio.
Firebase Configuration
Firebase Authentication:
Enable Email/Password sign-in method - Firebase Console (Authentication > Sign-In Method).
Firebase Realtime Database:
Utilize Firestore to store details of products, information on cart for the user, and orders.
Firebase Storage:
Use Firebase Storage for uploading and management of product images.
Usage
User Authentication:
Register: Using the email and password
Login: Using email and password.
Browse Products:
Firebase Cloud Messaging : Users receive real-time notifications for special offers, order updates, and more using Firebase Cloud Messaging (FCM). 

Login
Users shall login in order to brows list products.
Shopping Cart:
Add the products in cart and modify the quantity according to needs
Place Orders:
Users shall safely place orders. Orders will be uploaded to Firebase Firestore
Search Products:
Users can search the product by search functionality
Project Structure
app/src/main/java/: all the Java classes that contain activities as well as Firebase interaction logic
app/src/main/res/layout/: XML files of defining UI layouts
app/google-services.json: Firebase configuration
Contributing
Contributions are more than welcome! To contribute:

Fork the repository.
Create a new branch (git checkout -b feature/your-feature).
Commit your changes (git commit -am 'Add new feature').
Push to the branch (git push origin feature/your-feature).
Open a Pull Request.
License
The project is licensed under the MIT License. See the LICENSE file for details.

