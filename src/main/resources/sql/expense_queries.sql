-- @create_table
CREATE TABLE expenses (
      id INT PRIMARY KEY IDENTITY(1,1),
      category NVARCHAR(100) NOT NULL,
      amount FLOAT NOT NULL CHECK(amount > 0),
      date DATE NOT NULL,
      payment_method NVARCHAR(50)
);

-- @insert_expense
INSERT INTO expenses(category, amount, date, payment_method)
VALUES(@category, @amount, @date, @payment_method);

-- @select_all_expenses
SELECT * FROM expenses;

-- @delete_expense_by_id
DELETE FROM expenses WHERE id = @id;
