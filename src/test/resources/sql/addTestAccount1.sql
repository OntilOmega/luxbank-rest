INSERT INTO accounts (id, number, name, status, created_date, modified_date)
VALUES ('1d84be81-d434-4954-b835-be64500572ff', 'DE89370400440532013000', 'acc1-test1.1', 'ENABLED',
        '2022-10-04 11:20:55.000000', '2022-10-04 11:20:55.000000');

INSERT INTO balances (id, amount, currency, created_date, modified_date, type)
VALUES ('48d32c3a-cdec-4e42-9631-51122265f34c', 100.00000, 'EUR', '2022-10-04 14:39:01.000000',
        '2022-10-04 14:39:36.000000', 'AVAILABLE');

INSERT INTO account_balance (account_id, balance_id)
VALUES ('1d84be81-d434-4954-b835-be64500572ff', '48d32c3a-cdec-4e42-9631-51122265f34c');
