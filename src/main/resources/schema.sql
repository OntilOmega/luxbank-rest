create table if not exists users
(
    id            uuid        not null
        constraint users_pk
            primary key,
    name          varchar(50) not null,
    created_date  timestamp   not null,
    modified_date timestamp   not null,
    password      varchar(64) not null,
    address       varchar(100)
);

create unique index if not exists users_name_uindex
    on users (name);

create table if not exists accounts
(
    id            uuid         not null
        constraint accounts_pk
            primary key,
    number        varchar(100) not null,
    name          varchar(100) not null,
    status        varchar(7)   not null,
    created_date  timestamp    not null,
    modified_date timestamp    not null
);

create unique index if not exists accounts_number_uindex
    on accounts (number);

create table if not exists account_user
(
    account_id uuid not null
        constraint accounts_users_accounts_null_fk
            references accounts,
    user_id    uuid not null
        constraint accounts_users_users_null_fk
            references users
);

create index if not exists account_user_account_id_index
    on account_user (account_id);

create index if not exists account_user_user_id_index
    on account_user (user_id);

create unique index if not exists account_user_account_id_user_id_uindex
    on account_user (account_id, user_id);

create table if not exists balances
(
    id            uuid           not null
        constraint balances_pk
            primary key,
    amount        numeric(15, 5) not null,
    currency      varchar(3)     not null,
    created_date  timestamp      not null,
    modified_date timestamp      not null,
    type          varchar(10)    not null
);

create table if not exists account_balance
(
    account_id uuid not null,
    balance_id uuid not null
);

create unique index if not exists account_balance_account_id_balance_id_uindex
    on account_balance (account_id, balance_id);

create table if not exists forbidden_accounts
(
    id            uuid        not null
        constraint forbidden_accounts_pk
            primary key,
    created_date  timestamp   not null,
    modified_date timestamp   not null,
    number        varchar(20) not null
);

create unique index if not exists forbidden_accounts_number_uindex
    on forbidden_accounts (number);

create table if not exists payments
(
    id                         uuid           not null
        constraint payments_pk
            primary key,
    amount                     numeric(15, 5) not null,
    currency                   varchar(3)     not null,
    communication              varchar(100),
    created_date               timestamp      not null,
    modified_date              timestamp      not null,
    beneficiary_account_number varchar(100)   not null,
    beneficiary_name           varchar(100)   not null,
    status                     varchar(8)     not null,
    giver_account_id           uuid
        constraint payments_accounts_null_fk
            references accounts
);

create table if not exists fraud_attempts
(
    id                  uuid         not null
        constraint fraud_attempts_pk
            primary key,
    user_id             uuid         not null
        constraint fraud_attempts_users_id_fk
            references users,
    giver_account       varchar(100) not null,
    beneficiary_account varchar(100) not null,
    created_date        timestamp    not null,
    modified_date       timestamp    not null
);

create unique index if not exists fraud_attempts_id_uindex
    on fraud_attempts (id);

create index if not exists fraud_attempts_user_id_index
    on fraud_attempts (user_id);

