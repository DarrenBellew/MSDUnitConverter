

insert into category (categoryid, CategoryName)
    values
    (1, "Measurements"), (2, "Liquids"), (3, "Currency");

insert into units (unitid, UnitName, UnitType, CategoryId)
    values
    (1, "Centimeter", "int", 1), (2, "Meter", "int", 1);

insert into conversion (conversionid, Unit1Id, Unit2Id, UnitType, toFormula, fromFormula)
    values
    (1, 1, 2, "int", "ax100", "ad100");