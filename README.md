## **Job Board Platform Backend**

#### **Overview:**
This project is a backend implementation of a Job Board Platform built using Spring Boot with Gradle and MySQL. The main focus of this project is to provide advanced search and filtering capabilities for job listings. Users can register, log in, and search for jobs based on various criteria.

#### **Technologies Used:**
1. **Java:** Programming language used for backend development.
2. **Spring Boot:** Framework for building robust and scalable applications.
3. **Gradle:** Build automation tool used for managing dependencies and building the project.
4. **MySQL:**  Relational database management system used for storing job listings and user information.

#### **TF-IDF Introduction:**
TF-IDF (Term Frequency-Inverse Document Frequency) is a widely used technique in information retrieval and text mining to evaluate the importance of a word in a document relative to a collection of documents. It helps in ranking the relevance of documents based on a query. In this project, TF-IDF is used to implement advanced search functionality, providing users with more accurate and relevant search results.

#### **Features:**
1. **User Registration and Authentication:** Users can register an account and log in securely.
2. **Advanced Search:** Implemented advanced search functionality using the TF-IDF algorithm to rank search results based on relevance.
3. **Filtering:** Users can filter job listings based on criteria such as location, category, and keywords.

#### **How to Use:**
1. **Clone the Repository:**
   `git clone https://github.com/nishitakoshta/jobportalproject.git`
2. **Configure MySQL Database:**
   - Create a MySQL database
   - Update the database configuration in application.properties file with your MySQL credentials.
3. **Build and Run the Application:** Go to `JobportalprojectApplication` file and run main method.
4. **Access the Application:** Once the application is running, you can access it at `http://localhost:8080`.
