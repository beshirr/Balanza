-- @create_user_table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    phoneNumber TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    otp TEXT"
);

-- @insert_user
INSERT INTO users(username, email, phoneNumber, password, otp)
VALUES(?, ?, ?, ?, ?);

-- @remove_user
DELETE FROM users WHERE email = ?;

-- @get_user_info_by_username
SELECT * FROM users WHERE username = ?;

-- @get_user_info_by_email
SELECT * FROM users WHERE email = ?;

-- @set_otp
UPDATE users
SET otp=?
WHERE email=?;

-- @create_expenses_table
CREATE TABLE IF NOT EXISTS expenses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INT NOT NULL,
    category NVARCHAR(100) NOT NULL,
    amount FLOAT NOT NULL CHECK(amount > 0),
    date DATE NOT NULL,
    payment_method NVARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- @insert_expense
INSERT INTO expenses(user_id, category, amount, date, payment_method)
VALUES(?, ?, ?, ?, ?);

-- @select_all_expenses
SELECT * FROM expenses WHERE user_id=?;

-- @delete_expense_by_id
DELETE FROM expenses WHERE id = ?;


-- @create_income_table
CREATE TABLE IF NOT EXISTS income (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    source TEXT NOT NULL,
    amount REAL NOT NULL,
    date TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


-- @create_budget_table
CREATE TABLE IF NOT EXISTS budgets (
     id INTEGER PRIMARY KEY AUTOINCREMENT,
     user_id INTEGER NOT NULL,
     category TEXT,
     max_limit REAL,
     current_spent REAL,
     FOREIGN KEY (user_id) REFERENCES users(id)
);