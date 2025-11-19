DROP SCHEMA IF EXISTS country_club;
CREATE SCHEMA country_club;
USE country_club;

CREATE TABLE membership_types (
  type_id INT NOT NULL AUTO_INCREMENT,
  type_name VARCHAR(30) NOT NULL UNIQUE,
  monthly_fee DECIMAL(10,2),
  PRIMARY KEY (type_id)
) ENGINE=InnoDB;

INSERT INTO membership_types (type_name, monthly_fee)
VALUES 
('Regular', 1000.00),
('Premium', 2500.00),
('VIP', 5000.00);

CREATE TABLE supplier (
  supplier_id INT NOT NULL AUTO_INCREMENT,
  supplier_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (supplier_id)
) ENGINE=InnoDB;

INSERT INTO supplier (supplier_name)
VALUES
('FreshFoods Inc.'),
('SportsGear Co.'),
('EventDecor Ltd.'),
('TechSolutions IT'),
('CleanPro Services');

CREATE TABLE staff_role (
  role_id INT NOT NULL AUTO_INCREMENT,
  role_name VARCHAR(50) NOT NULL UNIQUE,
  PRIMARY KEY (role_id)
) ENGINE=InnoDB;

INSERT INTO staff_role (role_name)
VALUES
('Manager'),
('Coach'),
('Waiter'),
('Receptionist'),
('Security'),
('Janitorial');

CREATE TABLE venue (
  venue_id INT NOT NULL AUTO_INCREMENT,
  venue_name VARCHAR(50) NOT NULL,
  venue_type VARCHAR(30) NOT NULL DEFAULT 'General',
  capacity INT NOT NULL DEFAULT 0,
  availability_status VARCHAR(20) DEFAULT 'Available',
  PRIMARY KEY (venue_id)
) ENGINE=InnoDB;

INSERT INTO venue (venue_name, venue_type, capacity, availability_status)
VALUES
('Main Hall', 'Event Space', 500, 'Available'),
('Tennis Court 1', 'Sports', 4, 'Available'),
('Tennis Court 2', 'Sports', 4, 'Maintenance'),
('Swimming Pool (Olympic)', 'Sports', 50, 'Available'),
('Golf Course (18 Hole)', 'Sports', 100, 'Available'),
('Restaurant', 'Dining', 80, 'Available'),
('Meeting Room A', 'Meeting', 20, 'Occupied'),
('Meeting Room B', 'Meeting', 20, 'Available'),
('Gymnasium', 'Sports', 60, 'Available');

CREATE TABLE members_list (
  member_id INT NOT NULL AUTO_INCREMENT,
  membership_type_id INT NOT NULL,
  contact_no VARCHAR(15),
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  birth_date DATE,
  join_date DATE NOT NULL,
  expiry_date DATE NOT NULL,
  email VARCHAR(100),
  balance DECIMAL(10,2) DEFAULT 0.00, 
  PRIMARY KEY (member_id),
  FOREIGN KEY (membership_type_id) REFERENCES membership_types(type_id)
) ENGINE=InnoDB;

INSERT INTO members_list (membership_type_id, contact_no, first_name, last_name, birth_date, join_date, expiry_date, email, balance)
VALUES
(1, '0917-123-4567', 'Juan', 'Dela Cruz', '1990-05-15', '2020-01-10', '2025-01-10', 'juan.delacruz@email.com', 0.00),
(2, '0918-234-5678', 'Maria', 'Clara', '1985-11-20', '2019-03-22', '2025-03-22', 'maria.clara@email.com', 0.00),
(3, '0919-345-6789', 'Antonio', 'Luna', '1995-02-28', '2021-07-01', '2025-07-01', 'antonio.luna@email.com', 0.00),
(1, '0920-456-7890', 'Gabriela', 'Silang', '1992-08-10', '2022-11-05', '2024-11-05', 'gabriela.silang@email.com', 0.00),
(2, '0921-567-8901', 'Jose', 'Rizal', '1988-06-19', '2018-05-15', '2025-05-15', 'jose.rizal@email.com', 500.00),
(1, '0917-678-9012', 'Andres', 'Bonifacio', '1993-12-30', '2021-02-20', '2025-02-20', 'andres.bonifacio@email.com', 0.00),
(3, '0918-789-0123', 'Melchora', 'Aquino', '1975-01-06', '2017-09-10', '2025-09-10', 'melchora.aquino@email.com', 0.00),
(1, '0919-890-1234', 'Emilio', 'Aguinaldo', '1998-03-22', '2023-01-15', '2025-01-15', 'emilio.aguinaldo@email.com', 0.00),
(2, '0920-901-2345', 'Teresa', 'Magbanua', '1991-10-13', '2022-04-30', '2025-04-30', 'teresa.magbanua@email.com', 0.00),
(1, '0921-012-3456', 'Apolinario', 'Mabini', '1982-07-23', '2019-11-11', '2024-11-11', 'apolinario.mabini@email.com', 0.00),
(3, '0917-111-2222', 'Gregorio', 'del Pilar', '1999-11-14', '2023-06-01', '2025-06-01', 'goyo.pilar@email.com', 0.00),
(2, '0918-222-3333', 'Marcela', 'Agoncillo', '1980-06-24', '2018-08-20', '2025-08-20', 'marcela.agoncillo@email.com', 0.00);

CREATE TABLE inventory (
  item_id INT NOT NULL AUTO_INCREMENT,
  item_name VARCHAR(100) NOT NULL,
  item_category VARCHAR(50),
  item_quantity INT NOT NULL DEFAULT 0,
  item_unit_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  supplier_id INT,
  availability_start TIME DEFAULT '00:00:00', 
  availability_end TIME DEFAULT '23:59:59',   
  PRIMARY KEY (item_id),
  FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id)
) ENGINE=InnoDB;


INSERT INTO inventory (item_name, item_category, item_quantity, item_unit_price, supplier_id, availability_start, availability_end)
VALUES
('Premium Beef', 'Food', 100, 500.00, 1, '10:00:00', '22:00:00'),
('Fresh Salmon', 'Food', 50, 800.00, 1, '10:00:00', '22:00:00'),
('Organic Vegetables', 'Food', 200, 150.00, 1, '00:00:00', '23:59:59'),
('Tennis Balls (Can)', 'Sports', 150, 250.00, 2, '06:00:00', '20:00:00'),
('Golf Balls (Dozen)', 'Sports', 100, 600.00, 2, '06:00:00', '18:00:00'),
('Chlorine (Gallon)', 'Supplies', 50, 1200.00, 5, '00:00:00', '23:59:59'),
('Towels (Large)', 'Supplies', 300, 300.00, 3, '00:00:00', '23:59:59'),
('Table Linens (White)', 'Decor', 100, 400.00, 3, '00:00:00', '23:59:59'),
('Projector', 'Tech', 5, 25000.00, 4, '00:00:00', '23:59:59'),
('Whiteboard', 'Tech', 10, 3000.00, 4, '00:00:00', '23:59:59'),
('Soda (Case)', 'Beverage', 100, 300.00, 1, '00:00:00', '23:59:59'),
('Wine (Bottle)', 'Beverage', 80, 750.00, 1, '18:00:00', '23:59:59'),
('Dumbbells (Set)', 'Sports', 20, 1500.00, 2, '06:00:00', '22:00:00'),
('Yoga Mats', 'Sports', 50, 500.00, 2, '06:00:00', '22:00:00'),
('Microphone (Wireless)', 'Tech', 10, 3500.00, 4, '00:00:00', '23:59:59'),
('Chocolate Eggs (Bag)', 'Food', 50, 100.00, 1, '00:00:00', '23:59:59');

CREATE TABLE staff_member (
  staff_id INT NOT NULL AUTO_INCREMENT,
  contact_no VARCHAR(15),
  email VARCHAR(100),
  staff_name VARCHAR(100) NOT NULL,
  birth_date DATE,
  role_name VARCHAR(50) NOT NULL,
  availability_status VARCHAR(20) DEFAULT 'Available',
  salary DECIMAL(10,2) DEFAULT 15000.00, 
  PRIMARY KEY (staff_id),
  FOREIGN KEY (role_name) REFERENCES staff_role(role_name)
) ENGINE=InnoDB;

INSERT INTO staff_member (contact_no, email, staff_name, birth_date, role_name, availability_status, salary)
VALUES
('0917-987-6543', 'manager.main@cc.com', 'Carlos Mendoza', '1980-01-20', 'Manager', 'Available', 45000.00),
('0918-876-5432', 'coach.ana@cc.com', 'Ana Reyes', '1992-07-14', 'Coach', 'Available', 25000.00),
('0919-765-4321', 'waiter.leo@cc.com', 'Leo Santos', '1995-09-02', 'Waiter', 'Available', 18000.00),
('0920-654-3210', 'reception.mia@cc.com', 'Mia Gonzales', '1998-04-12', 'Receptionist', 'Available', 20000.00),
('0921-543-2109', 'security.ben@cc.com', 'Ben Torres', '1988-12-05', 'Security', 'On-Duty', 22000.00),
('0917-432-1098', 'janitor.rio@cc.com', 'Rio David', '1990-03-30', 'Janitorial', 'Available', 16000.00),
('0918-321-0987', 'coach.mark@cc.com', 'Mark Bautista', '1989-11-25', 'Coach', 'Available', 25000.00),
('0919-210-9876', 'manager.events@cc.com', 'Eliza Domingo', '1985-08-18', 'Manager', 'Available', 45000.00);

CREATE TABLE events (
  event_id INT NOT NULL AUTO_INCREMENT,
  event_name VARCHAR(100),
  from_date DATE,
  to_date DATE,
  from_time TIME,
  to_time TIME,
  item_id INT NOT NULL,
  member_id INT NOT NULL,
  staff_incharge_id INT NOT NULL,
  venue_id INT NOT NULL,
  PRIMARY KEY (event_id),
  FOREIGN KEY (item_id) REFERENCES inventory(item_id) ON DELETE CASCADE,
  FOREIGN KEY (member_id) REFERENCES members_list(member_id) ON DELETE CASCADE,
  FOREIGN KEY (staff_incharge_id) REFERENCES staff_member(staff_id) ON DELETE CASCADE,
  FOREIGN KEY (venue_id) REFERENCES venue(venue_id) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO events (event_name, from_date, to_date, from_time, to_time, item_id, member_id, staff_incharge_id, venue_id)
VALUES
('Annual Gala Night', '2024-12-20', '2024-12-20', '19:00:00', '23:00:00', 1, 1, 1, 1),
('Wine Tasting Event', '2024-11-15', '2024-11-15', '18:00:00', '21:00:00', 12, 2, 3, 6),
('Tennis Tournament', '2024-10-01', '2024-10-03', '08:00:00', '17:00:00', 4, 3, 2, 2),
('Golf Classic', '2024-09-10', '2024-09-12', '07:00:00', '16:00:00', 5, 5, 7, 5),
('Summer Pool Party', '2024-08-15', '2024-08-15', '14:00:00', '20:00:00', 11, 4, 3, 4),
('Family Day', '2024-07-20', '2024-07-20', '09:00:00', '17:00:00', 1, 6, 1, 1),
('Fitness Marathon', '2024-06-05', '2024-06-05', '06:00:00', '12:00:00', 14, 7, 2, 9),
('New Year Countdown', '2024-12-31', '2025-01-01', '20:00:00', '01:00:00', 12, 8, 8, 1),
('Corporate Seminar', '2024-11-25', '2024-11-25', '09:00:00', '17:00:00', 9, 9, 8, 7),
('Easter Egg Hunt', '2025-04-20', '2025-04-20', '10:00:00', '12:00:00', 16, 10, 4, 1);

CREATE TABLE transactions (
  transaction_id INT NOT NULL AUTO_INCREMENT,
  member_id INT, 
  event_id INT,  
  transaction_date DATE NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  description VARCHAR(255),
  PRIMARY KEY (transaction_id),
  FOREIGN KEY (member_id) REFERENCES members_list(member_id) ON DELETE CASCADE,
  FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE SET NULL
) ENGINE=InnoDB;


INSERT INTO transactions (member_id, event_id, transaction_date, amount, description)
VALUES
(1, NULL, '2024-01-10', 5000.00, 'Membership - Annual Dues'),
(2, NULL, '2024-03-22', 2500.00, 'Membership - Monthly Fee'),
(3, NULL, '2024-07-01', 1000.00, 'Membership - Monthly Fee'),
(4, NULL, '2024-11-05', 1000.00, 'Membership - Monthly Fee'),
(5, NULL, '2024-05-15', 2500.00, 'Membership - Monthly Fee'),
(1, 3, '2024-10-02', 800.00, 'Event - Tennis Tournament Fee'), 
(6, 4, '2024-09-10', 1200.00, 'Event - Golf Classic Entry'), 
(NULL, NULL, '2024-09-15', 1500.00, 'Guest - Guest Pass (Juan Dela Cruz)'),
(2, 2, '2024-11-15', 2000.00, 'Event - Wine Tasting Ticket'), 
(7, 7, '2024-06-05', 500.00, 'Event - Fitness Marathon'), 
(8, NULL, '2024-01-15', 1000.00, 'Membership - Monthly Fee'),
(9, NULL, '2024-04-30', 2500.00, 'Membership - Monthly Fee'),
(10, NULL, '2024-11-11', 1000.00, 'Membership - Monthly Fee'),
(11, NULL, '2024-06-01', 5000.00, 'Membership - Monthly Fee'),
(12, NULL, '2024-08-20', 2500.00, 'Membership - Monthly Fee'),
(1, NULL, '2024-11-10', 1000.00, 'Membership - Monthly Fee'),
(2, NULL, '2024-11-22', 2500.00, 'Membership - Monthly Fee'),
(3, NULL, '2024-11-01', 1000.00, 'Membership - Monthly Fee');

CREATE TABLE event_items (
  event_id INT NOT NULL,
  item_id INT NOT NULL,
  quantity_used INT NOT NULL,
  PRIMARY KEY (event_id, item_id),
  FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
  FOREIGN KEY (item_id) REFERENCES inventory(item_id) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO event_items (event_id, item_id, quantity_used)
VALUES
(1, 1, 50), (1, 2, 30), (1, 8, 50), (1, 12, 40),
(2, 12, 60), (2, 3, 10),
(3, 4, 40), (3, 7, 60), (3, 11, 20),
(4, 5, 30), (4, 7, 50), (4, 11, 25),
(5, 7, 80), (5, 11, 40), (5, 6, 2),
(7, 14, 30), (7, 13, 10), (7, 7, 40),
(8, 12, 70), (8, 1, 30), (8, 11, 50),
(9, 15, 2), (9, 8, 30),
(10, 11, 20), (10, 16, 50);

CREATE TABLE event_participants (
  event_id INT NOT NULL,
  member_id INT NOT NULL,
  attendance_status VARCHAR(20),
  PRIMARY KEY (event_id, member_id),
  FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
  FOREIGN KEY (member_id) REFERENCES members_list(member_id) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO event_participants (event_id, member_id, attendance_status)
VALUES
(1, 1, 'Attended'), (1, 2, 'Attended'), (1, 3, 'Attended'), (1, 5, 'Attended'), (1, 6, 'Attended'), (1, 11, 'Attended'), (1, 12, 'Absent'),
(3, 1, 'Attended'), (3, 3, 'Attended'), (3, 4, 'Attended'),
(4, 6, 'Attended'), (4, 8, 'Attended'), (4, 11, 'Absent'), (4, 12, 'Attended'),
(5, 4, 'Attended'), (5, 5, 'Attended'), (5, 9, 'Attended'),
(7, 7, 'Attended'), (7, 1, 'Attended'), (7, 10, 'Attended'),
(2, 2, 'Attended'), (2, 5, 'Attended'), (2, 7, 'Attended'), (2, 12, 'Attended');