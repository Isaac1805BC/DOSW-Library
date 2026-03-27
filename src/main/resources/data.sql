-- Seed initial LIBRARIAN user for first login
-- Password: 'admin123' hashed with BCrypt (strength 10)
INSERT INTO users (id, name, email, password, role)
VALUES (
    'seed-admin-001',
    'Admin Librarian',
    'admin@biblioteca.com',
    '$2a$10$7EqJtq98hPqEX7fNZaFWoOa/SZ9pnRa6fEjT5eEsBhR.2aITh7Bje',
    'LIBRARIAN'
) ON CONFLICT (email) DO NOTHING;
