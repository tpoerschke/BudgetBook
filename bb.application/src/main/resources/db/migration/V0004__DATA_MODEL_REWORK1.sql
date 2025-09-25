ALTER TABLE "UniqueTurnover" ADD COLUMN "fixed_turnover_id" INTEGER REFERENCES "FixedTurnover" ("id");

INSERT INTO UniqueTurnover (biller, date, note, fixed_turnover_id)
SELECT a.receiver, a.date, a.reference, a.fixed_expense_id FROM AccountTurnover a
WHERE a.fixed_expense_id IS NOT NULL;

UPDATE AccountTurnover
SET unique_expense_id = (
    SELECT uq.id
    FROM UniqueTurnover uq
    WHERE uq.fixed_turnover_id = AccountTurnover.fixed_expense_id
      AND uq.date = AccountTurnover.date
      AND uq.note = AccountTurnover.reference
)
WHERE AccountTurnover.fixed_expense_id IS NOT NULL;

INSERT INTO UniqueTurnoverInformation (label, value, direction, expense_id)
SELECT 'Gesamt', ABS(a.amount), iif(a.amount > 0, 'IN', 'OUT'), uq.id
FROM UniqueTurnover uq
JOIN AccountTurnover a
    ON a.unique_expense_id = uq.id
WHERE uq.fixed_turnover_id IS NOT NULL;

ALTER TABLE AccountTurnover DROP COLUMN "fixed_expense_id";