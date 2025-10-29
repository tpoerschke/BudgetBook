-- noinspection SqlWithoutWhereForFile

ALTER TABLE FixedTurnover ADD COLUMN "category_id" INTEGER REFERENCES Category ("id");
ALTER TABLE UniqueTurnoverInformation ADD COLUMN "category_id" INTEGER REFERENCES Category ("id");

UPDATE FixedTurnover SET category_id = (
    SELECT fc.categories_id FROM FixedTurnover_Category fc
    JOIN Category c
        ON c.id = fc.categories_id
    WHERE fc.fixedExpenses_id = FixedTurnover.id LIMIT 1
);

UPDATE UniqueTurnoverInformation SET category_id = (
    SELECT uc.categories_id FROM UniqueTurnoverInformation_Category uc
    JOIN Category c
        ON c.id = uc.categories_id
    WHERE uc.uniqueExpenseInformation_id = UniqueTurnoverInformation.id LIMIT 1
);

DROP TABLE FixedTurnover_Category;
DROP TABLE UniqueTurnoverInformation_Category;

