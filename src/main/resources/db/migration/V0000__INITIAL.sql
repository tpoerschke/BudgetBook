CREATE TABLE IF NOT EXISTS "Category"
(
    "id"          integer NOT NULL,
    "description" varchar(255),
    "name"        varchar(255),
    "parent_id"   integer,
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "PaymentInformation"
(
    "id"              integer NOT NULL,
    "end_month"       integer,
    "end_year"        integer,
    "monthsOfPayment" blob,
    "start_month"     integer,
    "start_year"      integer,
    "type"            varchar(255),
    "value"           float   NOT NULL,
    "expense_id"      integer NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "AccountTurnover"
(
    "id"                integer NOT NULL,
    "amount"            float   NOT NULL,
    "date"              date,
    "postingText"       varchar(255),
    "receiver"          varchar(255),
    "reference"         varchar(255),
    "fixed_expense_id"  integer,
    "unique_expense_id" integer,
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "FixedTurnover"
(
    "id"        integer      NOT NULL,
    "direction" varchar(255) NOT NULL,
    "note"      varchar(255),
    "position"  varchar(255),
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "FixedTurnover_Category"
(
    "fixedExpenses_id" integer NOT NULL,
    "categories_id"    integer NOT NULL
);

CREATE TABLE IF NOT EXISTS "ImportRule"
(
    "id"                    integer NOT NULL,
    "isActive"              boolean NOT NULL,
    "receiverContains"      varchar(255),
    "referenceContains"     varchar(255),
    "linkedFixedExpense_id" integer,
    PRIMARY KEY ("id")
);


CREATE TABLE IF NOT EXISTS "UniqueTurnoverInformation"
(
    "id"         integer      NOT NULL,
    "direction"  varchar(255) NOT NULL,
    "label"      varchar(255),
    "value"      float        NOT NULL,
    "expense_id" integer      NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "UniqueTurnoverInformation_Category"
(
    "uniqueExpenseInformation_id" integer NOT NULL,
    "categories_id"               integer NOT NULL
);

CREATE TABLE IF NOT EXISTS "UniqueTurnover"
(
    "id"               integer NOT NULL,
    "biller"           varchar(255),
    "date"             date    NOT NULL,
    "note"             varchar(255),
    "receiptImagePath" varchar(255),
    PRIMARY KEY ("id")
);
