# BudgetTracker
This is a 100% offline android application to track the user's budget. You can sign up and then login to see the past records of the expenses entered by you. The user can add, edit and delete the records. The data is stored in SQLite Database which contains 4 tables:-
Users table stores the User authentication information such as username, password and recovery code;
UserBudgetInfo table stores the budget and moneyspent by all the users(user_id);
Expenses table stores unique(not redundant) names of the expenses;
UserExpenses table stores the expenses(item_id) and its corresponding user_id and price of the expense/item.

## Glimpses of the app:-
### Signup Screen
![image](https://user-images.githubusercontent.com/55046164/210106460-b1203d0a-767b-46c5-bdba-96adbae13699.png)
![image](https://user-images.githubusercontent.com/55046164/210106547-93b3bc2e-7029-40a5-9052-dc397f3e4260.png)
![image](https://user-images.githubusercontent.com/55046164/210106786-f57f32e3-1b49-4c93-b0e2-3107d9e2a531.png)
The Recovery Code is really important as it enables a user to change his/her account's password.

### Login Screen
![image](https://user-images.githubusercontent.com/55046164/210106360-77086d78-f5f0-45b3-ba71-d25f120495b0.png)

### Forgot Password
![image](https://user-images.githubusercontent.com/55046164/210106687-ef83c1cc-4cbb-49c4-8d41-81617d92ce2c.png)
![image](https://user-images.githubusercontent.com/55046164/210106733-ff134eea-d8ff-4560-85bc-91dcaed2c616.png)

### List of Expenses Screen
![image](https://user-images.githubusercontent.com/55046164/210109165-e5686136-ea7c-4788-84f3-9d3c28a30c23.png)
![image](https://user-images.githubusercontent.com/55046164/210109122-914b443d-9260-4316-8514-e8b07f7e82d1.png)

### Adding an expense
![image](https://user-images.githubusercontent.com/55046164/210109226-e253c6d6-c09d-4a1e-b2c6-0e586ecf6845.png)

### Details of an expense
![image](https://user-images.githubusercontent.com/55046164/210109776-a65bf39c-d87c-4a51-8cba-49c8e2accc60.png)
