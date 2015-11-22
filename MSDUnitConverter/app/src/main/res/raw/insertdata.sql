

insert into category (categoryid, CategoryName)
    values
    (1, "Measurements"), (2, "Liquids"), (3, "Currency");

insert into units (unitid, UnitName)
    values
    (1, "Centimeter"), (2, "Meter");

insert into conversion (conversionid, Unit1Id, Unit2Id, toFormula, fromFormula, CategoryId)
    values
    (1, 1, 2, "a*100", "a/100", 1);
