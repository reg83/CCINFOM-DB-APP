DROP SCHEMA IF EXISTS country_club;
CREATE SCHEMA country_club;
USE country_club;

-- MEMBERSHIP TYPES
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

-- MEMBERS LIST
CREATE TABLE members_list (
  member_id INT NOT NULL AUTO_INCREMENT,
  membership_type_id INT NOT NULL,
  contact_no VARCHAR(50) NOT NULL,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  birth_date DATE NOT NULL,
  join_date DATE NOT NULL,
  expiry_date DATE NOT NULL,
  email VARCHAR(50),
  PRIMARY KEY (member_id),
  FOREIGN KEY (membership_type_id) REFERENCES membership_types(type_id)
) ENGINE=InnoDB;

INSERT INTO members_list (membership_type_id, contact_no, first_name, last_name, birth_date, join_date, expiry_date, email)
VALUES
(1, '09171234567', 'Juan', 'Dela Cruz', '1990-05-12', '2025-01-01', '2026-01-01', 'juan@example.com'),
(2, '09281234567', 'Maria', 'Santos', '1985-08-20', '2025-02-15', '2026-02-15', 'maria@example.com'),
(3, '09391234567', 'Carlos', 'Reyes', '1978-03-10', '2025-03-01', '2026-03-01', 'carlos@example.com');

-- SUPPLIER
CREATE TABLE supplier (
  supplier_id INT NOT NULL AUTO_INCREMENT,
  supplier_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (supplier_id)
) ENGINE=InnoDB;

INSERT INTO supplier (supplier_name)
VALUES
('FreshFoods Inc.'),
('SportsGear Co.'),
('EventDecor Ltd.');

-- INVENTORY
CREATE TABLE inventory (
  item_id INT NOT NULL AUTO_INCREMENT,
  item_name VARCHAR(50) NOT NULL,
  item_category VARCHAR(30) NOT NULL,
  item_quantity INT NOT NULL,
  item_unit_price DECIMAL(10,2) NOT NULL,
  supplier_id INT NOT NULL,
  PRIMARY KEY (item_id),
  FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id)
) ENGINE=InnoDB;

INSERT INTO inventory (item_name, item_category, item_quantity, item_unit_price, supplier_id)
VALUES
('Tennis Balls', 'Sports', 100, 50.00, 2),
('Wine Bottle', 'Beverages', 50, 800.00, 1),
('Stage Lights', 'Equipment', 10, 5000.00, 3);

-- STAFF ROLES
CREATE TABLE staff_role (
  role_id INT NOT NULL AUTO_INCREMENT,
  role_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (role_id)
) ENGINE=InnoDB;

INSERT INTO staff_role (role_name)
VALUES
('Manager'),
('Coach'),
('Waiter');

-- STAFF MEMBERS
CREATE TABLE staff_member (
  staff_id INT NOT NULL AUTO_INCREMENT,
  contact_no VARCHAR(50) NOT NULL,
  email VARCHAR(50) NOT NULL,
  staff_name VARCHAR(50) NOT NULL,
  birth_date DATE NOT NULL,
  role_name VARCHAR(50) NOT NULL,
  availability_status VARCHAR(20) NOT NULL,
  PRIMARY KEY (staff_id)
) ENGINE=InnoDB;

INSERT INTO staff_member (contact_no, email, staff_name, birth_date, role_name, availability_status)
VALUES
('09123456789', 'ana.lopez@example.com', 'Ana Lopez', '1992-07-15', 'Manager', 'Available'),
('09234567890', 'mark.cruz@example.com', 'Mark Cruz', '1988-11-20', 'Coach', 'Available'),
('09345678901', 'sofia.ramos@example.com', 'Sofia Ramos', '1995-01-05', 'Waiter', 'On Leave');

-- VENUES
CREATE TABLE venue (
  venue_id INT NOT NULL AUTO_INCREMENT,
  venue_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (venue_id)
) ENGINE=InnoDB;

INSERT INTO venue (venue_name)
VALUES
('Main Hall'),
('Swimming Pool'),
('Tennis Court');

-- EVENTS
CREATE TABLE events (
  event_id INT NOT NULL AUTO_INCREMENT,
  event_name VARCHAR(50) NOT NULL,
  from_date DATE NOT NULL,
  to_date DATE NOT NULL,
  from_time TIME NOT NULL,
  to_time TIME NOT NULL,
  item_id INT NOT NULL,
  member_id INT NOT NULL,
  staff_incharge_id INT NOT NULL,
  venue_id INT NOT NULL,
  PRIMARY KEY (event_id),
  FOREIGN KEY (member_id) REFERENCES members_list(member_id),
  FOREIGN KEY (item_id) REFERENCES inventory(item_id),
  FOREIGN KEY (staff_incharge_id) REFERENCES staff_member(staff_id),
  FOREIGN KEY (venue_id) REFERENCES venue(venue_id)
) ENGINE=InnoDB;

INSERT INTO events (event_name, from_date, to_date, from_time, to_time, item_id, member_id, staff_incharge_id, venue_id)
VALUES
('Annual Gala', '2025-05-01', '2025-05-01', '18:00:00', '23:00:00', 3, 1, 1, 1),
('Swimming Competition', '2025-06-10', '2025-06-10', '09:00:00', '12:00:00', 2, 2, 2, 2),
('Tennis Tournament', '2025-07-15', '2025-07-16', '08:00:00', '17:00:00', 1, 3, 2, 3);

-- EVENT ITEMS
CREATE TABLE event_items (
  event_id INT NOT NULL,
  item_id INT NOT NULL,
  quantity_used INT,
  PRIMARY KEY (event_id, item_id),
  FOREIGN KEY (event_id) REFERENCES events(event_id),
  FOREIGN KEY (item_id) REFERENCES inventory(item_id)
) ENGINE=InnoDB;

INSERT INTO event_items (event_id, item_id, quantity_used)
VALUES
(1, 3, 5),
(2, 2, 10),
(3, 1, 20);

-- EVENT PARTICIPANTS
CREATE TABLE event_participants (
  event_id INT NOT NULL,
  member_id INT NOT NULL,
  attendance_status VARCHAR(20),
  PRIMARY KEY (event_id, member_id),
  FOREIGN KEY (event_id) REFERENCES events(event_id),
  FOREIGN KEY (member_id) REFERENCES members_list(member_id)
) ENGINE=InnoDB;

INSERT INTO event_participants (event_id, member_id, attendance_status)
VALUES
(1, 1, 'Attended'),
(1, 2, 'Attended'),
(2, 3, 'Absent'),
(3, 1, 'Attended');

-- TRANSACTIONS
CREATE TABLE transactions (
  transaction_id INT NOT NULL AUTO_INCREMENT,
  member_id INT,
  transaction_date DATE,
  amount DECIMAL(10,2),
  description VARCHAR(100),
  PRIMARY KEY (transaction_id),
  FOREIGN KEY (member_id) REFERENCES members_list(member_id)
) ENGINE=InnoDB;

INSERT INTO transactions (member_id, transaction_date, amount, description)
VALUES
(1, '2025-01-01', 1000.00, 'Initial membership payment'),
(2, '2025-02-15', 2500.00, 'Premium membership payment'),
(3, '2025-03-01', 5000.00, 'VIP membership payment');
