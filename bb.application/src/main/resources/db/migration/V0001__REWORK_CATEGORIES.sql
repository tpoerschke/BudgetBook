CREATE TABLE IF NOT EXISTS "CategoryGroup"
(
    "id"   INTEGER NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    PRIMARY KEY ("id")
);

ALTER TABLE "Category" ADD COLUMN "group_id" INTEGER REFERENCES "CategoryGroup" ("id");
ALTER TABLE "Category" DROP COLUMN "parent_id";