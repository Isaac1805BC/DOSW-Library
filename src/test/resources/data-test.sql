-- Seed user for testing
-- Password: 'admin123' hashed with BCrypt
INSERT INTO users (id, name, email, password, role)
VALUES (
    'seed-admin-001',
    'Admin Librarian',
    'admin@biblioteca.com',
    '$2a$10$7EqJtq98hPqEX7fNZaFWoOa/SZ9pnRa6fEjT5eEsBhR.2aITh7Bje',
    'LIBRARIAN'
);
