INSERT INTO public.merchants (
    acquirer_name, category, city, country, created_at, domain,
    is_high_risk, merchant_id, name, updated_at
) VALUES
('Visa', 'Electronics', 'New York', 'USA', '2025-11-01 09:30:00', 'www.techhub.com', FALSE, 'M001', 'Tech Hub', '2025-11-01 09:30:00'),
('Mastercard', 'Groceries', 'Toronto', 'Canada', '2025-11-02 10:00:00', 'www.greensupermarket.ca', FALSE, 'M002', 'Green Supermarket', '2025-11-02 10:00:00'),
('Amex', 'Travel', 'London', 'UK', '2025-11-03 14:45:00', 'www.flyaway.co.uk', TRUE, 'M003', 'FlyAway Travel', '2025-11-03 14:45:00'),
('Discover', 'Fashion', 'Paris', 'France', '2025-11-04 11:20:00', 'www.styleparis.fr', FALSE, 'M004', 'Style Paris', '2025-11-04 11:20:00'),
('PayPal', 'Online Services', 'Berlin', 'Germany', '2025-11-04 13:10:00', 'www.webmart.de', TRUE, 'M005', 'WebMart', '2025-11-04 13:10:00');

INSERT INTO public.transactions (
    amount, card_number_masked, card_type, channel, created_at, currency,
    device_id, flag_reason, ip_address, is_flagged, location,
    merchant_id, response_code, review_status, risk_score,
    transaction_id, transaction_time, transaction_type, flagged_at,
    flagged_by, updated_at
) VALUES
(129.99, '4111-****-****-1111', 'VISA', 'Online', '2025-11-01 09:32:00', 'USD',
 'DEV-001', 'None', '192.168.0.5', FALSE, 'New York, USA',
 'M001', '00', 'Approved', 12, 'T001', '2025-11-01 09:32:00', 'Purchase', '2025-11-01 09:32:00', 'System', '2025-11-01 09:32:00'),

(56.75, '5500-****-****-2222', 'Mastercard', 'POS', '2025-11-02 10:10:00', 'CAD',
 'DEV-002', 'None', '10.0.0.10', FALSE, 'Toronto, Canada',
 'M002', '00', 'Approved', 8, 'T002', '2025-11-02 10:10:00', 'Purchase', '2025-11-02 10:10:00', 'System', '2025-11-02 10:10:00'),

(899.00, '3400-****-****-3333', 'Amex', 'Mobile', '2025-11-03 15:00:00', 'GBP',
 'DEV-003', 'High amount', '172.16.5.25', TRUE, 'London, UK',
 'M003', '05', 'Under Review', 85, 'T003', '2025-11-03 15:00:00', 'Purchase', '2025-11-03 15:05:00', 'FraudSystem', '2025-11-03 15:05:00'),

(240.50, '6011-****-****-4444', 'Discover', 'POS', '2025-11-04 11:25:00', 'EUR',
 'DEV-004', 'Suspicious location', '203.0.113.10', TRUE, 'Paris, France',
 'M004', '10', 'Flagged', 70, 'T004', '2025-11-04 11:25:00', 'Purchase', '2025-11-04 11:30:00', 'FraudTeam', '2025-11-04 11:30:00'),

(34.99, '3782-****-****-5555', 'Amex', 'Online', '2025-11-04 13:15:00', 'EUR',
 'DEV-005', 'Repeated IP', '198.51.100.15', TRUE, 'Berlin, Germany',
 'M005', '91', 'Pending Review', 62, 'T005', '2025-11-04 13:15:00', 'Purchase', '2025-11-04 13:17:00', 'FraudSystem', '2025-11-04 13:17:00');
