-- SQLite erfordert ein erneutes Aufbauen der Tabelle,
-- das ALTER TABLE ... MODIFY COLUMN ... nicht unterstützt wird

-- In diesem Zuge wird außerdem:
-- - Die Benennung der Referenz auf die Umsätze korrigiert
-- - Die Nullable-Constraints einiger Spalten angepasst
-- - Fehlende Fremdschlüssel-Constraints eingebaut

CREATE TABLE IF NOT EXISTS AccountTurnover_tmp
(
    "id"                 integer NOT NULL,
    "amount"             integer NOT NULL,
    "date"               date    NOT NULL,
    "postingText"        VARCHAR2(255),
    "receiver"           VARCHAR2(255),
    "reference"          VARCHAR2(255),
    "unique_turnover_id" integer NOT NULL REFERENCES "UniqueTurnover" ("id"),
    PRIMARY KEY ("id")
);

INSERT INTO AccountTurnover_tmp (id, amount, date, postingText, receiver, reference, unique_turnover_id)
SELECT at.id, CAST(at.amount * 100 AS INTEGER), at.date, at.postingText, at.receiver, at.reference, at.unique_expense_id
FROM AccountTurnover at
INNER JOIN UniqueTurnover ut
    ON ut.id = at.unique_expense_id;

DROP TABLE AccountTurnover;
ALTER TABLE AccountTurnover_tmp RENAME TO AccountTurnover;

-- ### -- ### --

CREATE TABLE IF NOT EXISTS UniqueTurnoverInformation_tmp
(
    "id"          integer       NOT NULL,
    "direction"   VARCHAR2(255) NOT NULL,
    "label"       VARCHAR2(255) NOT NULL,
    "value"       integer       NOT NULL,
    "turnover_id" integer       NOT NULL REFERENCES UniqueTurnover ("id"),
    PRIMARY KEY ("id")
);

INSERT INTO UniqueTurnoverInformation_tmp (id, direction, label, value, turnover_id)
SELECT id, direction, label, CAST(value * 100 AS INTEGER), expense_id
FROM UniqueTurnoverInformation;

DROP TABLE UniqueTurnoverInformation;
ALTER TABLE UniqueTurnoverInformation_tmp RENAME TO UniqueTurnoverInformation;

-- ### -- ### --

CREATE TABLE PaymentInformation_tmp
(
    "id"              integer NOT NULL,
    "end_month"       integer,
    "end_year"        integer,
    "start_month"     integer,
    "start_year"      integer,
    "type"            VARCHAR2(255),
    "monthsOfPayment" blob,
    "value"           integer NOT NULL,
    "turnover_id"     integer NOT NULL REFERENCES FixedTurnover ("id"),
    PRIMARY KEY ("id")
);

INSERT INTO PaymentInformation_tmp (id, end_month, end_year, start_month, start_year, type, monthsOfPayment, value, turnover_id)
SELECT id, end_month, end_year, start_month, start_year, type, monthsOfPayment, CAST(value * 100 AS INTEGER), expense_id
FROM PaymentInformation;

DROP TABLE PaymentInformation;
ALTER TABLE PaymentInformation_tmp RENAME TO PaymentInformation;

-- ### -- ### --

CREATE TABLE Category_tmp
(
    "id"           integer NOT NULL,
    "name"         VARCHAR2(255) NOT NULL,
    "description"  VARCHAR2(255),
    "group_id"     INTEGER REFERENCES CategoryGroup ("id"),
    "budgetValue"  INTEGER NULL,
    "budgetActive" BOOLEAN NOT NULL DEFAULT false,
    "budgetType"   VARCHAR2(255) NULL,
    PRIMARY KEY ("id")
);

INSERT INTO Category_tmp (id, name, description, group_id, budgetValue, budgetActive, budgetType)
SELECT id,
       name,
       description,
       group_id,
       CASE WHEN budgetValue IS NOT NULL THEN CAST(budgetValue * 100 AS INTEGER) ELSE NULL END,
       budgetActive,
       budgetType
FROM Category;

DROP TABLE Category;
ALTER TABLE Category_tmp RENAME TO Category;