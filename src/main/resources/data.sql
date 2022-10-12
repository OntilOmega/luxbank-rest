truncate table users cascade;
truncate table accounts cascade;
truncate table payments cascade;
truncate table balances;
truncate table account_balance;
truncate table fraud_attempts;

-- add test users
INSERT INTO public.users (id, name, created_date, modified_date, password, address)
VALUES ('7f395f1c-de79-4254-aee6-d226c70c8527', 'test1', '2022-10-10 21:49:54.075009', '2022-10-10 21:49:54.075009',
        '$2a$10$Npaa90Y0CjqgG.Efa96pQO0pQR8g9kMR9iVuad.3fMXX8ZAS9uHiC', null)
on conflict DO NOTHING;
INSERT INTO public.users (id, name, created_date, modified_date, password, address)
VALUES ('18a27390-37b6-4a6a-ae22-162d2b794295', 'test2', '2022-10-10 23:48:45.262407', '2022-10-10 23:48:45.262407',
        '$2a$10$Z6v.mgjp1IRg2.LMCtXg0.Tua6xQdm2k2dpnqYoz1yaT9YlsWDeoG', null)
on conflict DO NOTHING;

-- add forbidden accounts
INSERT INTO public.forbidden_accounts (id, created_date, modified_date, number)
VALUES ('3ed66920-468b-11ed-b878-0242ac120003', '2022-10-08 00:58:26.000000', '2022-10-08 00:58:27.000000',
        'LU280019400644750000')
on conflict DO NOTHING;
INSERT INTO public.forbidden_accounts (id, created_date, modified_date, number)
VALUES ('548c74d8-4895-11ed-b878-0242ac120002', '2022-10-08 00:58:26.000000', '2022-10-08 00:58:27.000000',
        'LU120010001234567891')
on conflict DO NOTHING;

-- add test account1.1 with balance 100 EUR
INSERT INTO public.accounts (id, number, name, status, created_date, modified_date)
VALUES ('1d84be81-d434-4954-b835-be64500572ff', 'DE89370400440532013000', 'acc1-test1.1', 'ENABLED',
        '2022-10-04 11:20:55.000000', '2022-10-04 11:20:55.000000')
on conflict DO NOTHING;
INSERT INTO public.balances (id, amount, currency, created_date, modified_date, type)
VALUES ('48d32c3a-cdec-4e42-9631-51122265f34c', 100.00000, 'EUR', '2022-10-04 14:39:01.000000',
        '2022-10-04 14:39:36.000000', 'AVAILABLE')
on conflict DO NOTHING;
INSERT INTO public.account_balance (account_id, balance_id)
VALUES ('1d84be81-d434-4954-b835-be64500572ff', '48d32c3a-cdec-4e42-9631-51122265f34c')
on conflict DO NOTHING;


-- add test account1.2 with balance 0 EUR
INSERT INTO public.accounts (id, number, name, status, created_date, modified_date)
VALUES ('bf7431aa-cba5-4f12-bd2f-01bb7ad872dc', 'US64SVBKUS6S3300958879', 'acc1-test1.2', 'ENABLED',
        '2022-10-04 11:20:55.000000', '2022-10-04 11:20:55.000000')
on conflict DO NOTHING;
INSERT INTO public.balances (id, amount, currency, created_date, modified_date, type)
VALUES ('f7858377-0121-4224-a64e-21ddf18d6362', 0.00000, 'EUR', '2022-10-04 14:39:01.000000',
        '2022-10-04 14:39:36.000000', 'AVAILABLE')
on conflict DO NOTHING;
INSERT INTO public.account_balance (account_id, balance_id)
VALUES ('bf7431aa-cba5-4f12-bd2f-01bb7ad872dc', 'f7858377-0121-4224-a64e-21ddf18d6362')
on conflict DO NOTHING;


-- add test account2.1 with balance 100 EUR
INSERT INTO public.accounts (id, number, name, status, created_date, modified_date)
VALUES ('606ef608-4896-11ed-b878-0242ac120002', 'AL35202111090000000001234567', 'acc1-test2.1', 'ENABLED',
        '2022-10-04 11:20:55.000000', '2022-10-04 11:20:55.000000')
on conflict DO NOTHING;
INSERT INTO public.balances (id, amount, currency, created_date, modified_date, type)
VALUES ('4057dca6-3b02-4f7a-a785-d33bcb4291eb', 100.00000, 'EUR', '2022-10-04 14:39:01.000000',
        '2022-10-04 14:39:36.000000', 'AVAILABLE')
on conflict DO NOTHING;
INSERT INTO public.account_balance (account_id, balance_id)
VALUES ('606ef608-4896-11ed-b878-0242ac120002', '4057dca6-3b02-4f7a-a785-d33bcb4291eb')
on conflict DO NOTHING;

-- add test account2.2 with balance 0 EUR
INSERT INTO public.accounts (id, number, name, status, created_date, modified_date)
VALUES ('24007e28-c078-48fe-83e4-c848b3d30f17', 'AD1400080001001234567890', 'acc1-test2.2', 'ENABLED',
        '2022-10-04 11:20:55.000000', '2022-10-04 11:20:55.000000')
on conflict DO NOTHING;
INSERT INTO public.balances (id, amount, currency, created_date, modified_date, type)
VALUES ('1b751b3c-0272-4235-b838-c3f39da4334d', 0.00000, 'EUR', '2022-10-04 14:39:01.000000',
        '2022-10-04 14:39:36.000000', 'AVAILABLE')
on conflict DO NOTHING;
INSERT INTO public.account_balance (account_id, balance_id)
VALUES ('24007e28-c078-48fe-83e4-c848b3d30f17', '1b751b3c-0272-4235-b838-c3f39da4334d')
on conflict DO NOTHING;

-- assign accoun1.1 to user1
INSERT INTO public.account_user (account_id, user_id)
VALUES ('1d84be81-d434-4954-b835-be64500572ff', '7f395f1c-de79-4254-aee6-d226c70c8527')
on conflict DO NOTHING;

-- assign accoun1.2 to user1
INSERT INTO public.account_user (account_id, user_id)
VALUES ('bf7431aa-cba5-4f12-bd2f-01bb7ad872dc', '7f395f1c-de79-4254-aee6-d226c70c8527')
on conflict DO NOTHING;


-- assign accoun2.1 to user2
INSERT INTO public.account_user (account_id, user_id)
VALUES ('606ef608-4896-11ed-b878-0242ac120002', '18a27390-37b6-4a6a-ae22-162d2b794295')
on conflict DO NOTHING;

-- assign accoun2.2 to user2
INSERT INTO public.account_user (account_id, user_id)
VALUES ('24007e28-c078-48fe-83e4-c848b3d30f17', '18a27390-37b6-4a6a-ae22-162d2b794295')
on conflict DO NOTHING;