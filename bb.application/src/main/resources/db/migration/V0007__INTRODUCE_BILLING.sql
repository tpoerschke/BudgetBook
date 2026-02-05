-- Create Billing table
CREATE TABLE IF NOT EXISTS "Billing"
(
    "id"          integer      NOT NULL,
    "title"       varchar(255) NOT NULL,
    "description" varchar(1000),
    PRIMARY KEY ("id")
);

-- Create join table for Billing and UniqueTurnover relationship
CREATE TABLE IF NOT EXISTS "billing_unique_turnover"
(
    "billing_id"           integer NOT NULL,
    "unique_turnover_id"   integer NOT NULL,
    PRIMARY KEY ("billing_id", "unique_turnover_id"),
    FOREIGN KEY ("billing_id") REFERENCES "Billing" ("id"),
    FOREIGN KEY ("unique_turnover_id") REFERENCES "UniqueTurnover" ("id")
);
