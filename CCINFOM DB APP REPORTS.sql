-- Event Report 
-- (total income and maintenance per event) for a specific month and year 
SELECT e.event_id as 'Event ID' , e.event_name as 'Event Name', SUM(i.item_unit_price * ei.quantity_used) AS 'Total Cost'
FROM events e
JOIN event_items ei ON e.event_id = ei.event_id
JOIN inventory i ON ei.item_id = i.item_id
WHERE MONTH(e.from_date) = 4 AND YEAR(e.from_date) = 2025
GROUP BY e.event_id, e.event_name;

-- Sales Report
-- (total and average sales amount) per day for a specific year and month)
SELECT DATE(transaction_date) AS 'Sale Date', SUM(amount) AS 'Total Sales', AVG(amount) AS 'Average Sales'
FROM transactions
WHERE MONTH(transaction_date) = 4 AND YEAR(transaction_date) = 2025
GROUP BY DATE(transaction_date)
ORDER BY DATE(transaction_date);

-- Membership Report
-- (total number of members) in a certain month and year
SELECT COUNT(member_id) AS 'New Member Count', MONTH(join_date) AS 'Join Month', YEAR(join_date) as 'Join Year'
FROM members_list
WHERE MONTH(join_date) = 1 AND YEAR(join_date) = 2025
GROUP BY MONTH(join_date), YEAR(join_date);

SELECT COUNT(member_id) AS 'New Member Count', MONTH(join_date) AS 'Join Month', YEAR(join_date) as 'Join Year'
FROM members_list
WHERE MONTH(join_date) = 2 AND YEAR(join_date) = 2025
GROUP BY MONTH(join_date), YEAR(join_date);

-- (total number of members) in a certain year
SELECT COUNT(member_id) AS 'New Member Count', YEAR(join_date) as 'Join Year'
FROM members_list
WHERE YEAR(join_date) = 2025
GROUP BY YEAR(join_date);

-- Staff Report
-- (total number of staff and their schedules) in a specific month
SELECT s.staff_id as 'Staff ID', s.staff_name as 'Staff Name', e.event_name as 'Event Name', 
e.from_date as 'Start Date', e.to_date as 'End Date', e.from_time as 'Start Time', e.to_time as 'End Time'
FROM staff_member s LEFT JOIN events e ON s.staff_id = e.staff_incharge_id
WHERE MONTH(e.from_date) = 4 AND YEAR(e.from_date) = 2025
ORDER BY s.staff_id, e.from_date;