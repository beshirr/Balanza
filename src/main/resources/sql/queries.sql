-- @create_user_table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    phoneNumber TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    otp TEXT,
    verified BOOLEAN DEFAULT 0
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

-- @get_user_email_by_id
SELECT email FROM users WHERE id=?;

-- @get_user_otp_by_id
SELECT otp FROM users WHERE id=?;

-- @get_user_verified_by_id
SELECT verified FROM users WHERE id=?;

-- @set_otp
UPDATE users
SET otp=?
WHERE email=?;

-- @set_verified
UPDATE users
SET verified=true
WHERE id=?;

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

-- @create_reminder_table
CREATE TABLE IF NOT EXISTS reminders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    time TIMESTAMP NOT NULL,
    task_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (task_id) REFERENCES financial_tasks(id)
);

-- @insert_reminder
INSERT INTO reminders(user_id, title, description, time, task_id)
VALUES(?, ?, ?, ?, ?);

-- @delete_reminder
DELETE FROM reminders WHERE id = ?;

-- @get_user_reminders
SELECT * FROM reminders WHERE user_id = ? ORDER BY time ASC;

-- @update_reminder
UPDATE reminders
SET title = ?, description = ?, time = ?, task_id = ?
WHERE id = ?;

-- @create_financial_task_table
CREATE TABLE IF NOT EXISTS financial_tasks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    due_date DATE NOT NULL,
    amount REAL NOT NULL,
    category TEXT NOT NULL,
    status TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- @insert_financial_task
INSERT INTO financial_tasks(user_id, title, description, due_date, amount, category, status)
VALUES(?, ?, ?, ?, ?, ?, ?);

-- @delete_financial_task
DELETE FROM financial_tasks WHERE id = ?;

-- @get_user_financial_tasks
SELECT * FROM financial_tasks WHERE user_id = ? ORDER BY due_date ASC;

-- @get_financial_task_by_id
SELECT * FROM financial_tasks WHERE id = ?;

-- @update_financial_task_status
UPDATE financial_tasks
SET status = ?
WHERE id = ?;

-- @update_financial_task
UPDATE financial_tasks
SET title = ?, description = ?, due_date = ?, amount = ?, category = ?, status = ?
WHERE id = ?;

-- @get_upcoming_financial_tasks
SELECT * FROM financial_tasks 
WHERE user_id = ? AND due_date >= ? AND status = 'PENDING'
ORDER BY due_date ASC;