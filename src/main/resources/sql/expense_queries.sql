-- @create_table
CREATE TABLE IF NOT EXISTS expenses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    category TEXT NOT NULL,
    amount REAL NOT NULL CHECK(amount > 0),
    date TEXT NOT NULL,
    payment_method TEXT
);

-- @insert_expense
INSERT INTO expenses(category, amount, date, payment_method)
VALUES(?, ?, ?, ?);

-- @select_all_expenses
SELECT * FROM expenses;

-- @delete_expense_by_id
DELETE FROM expenses WHERE id=?;