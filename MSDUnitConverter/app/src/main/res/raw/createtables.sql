create table Category(
    CategoryId integer(5) primary key,
    CategoryName varchar(50)
);

create table Units  (
    UnitId integer(5) primary key,
    UnitName varchar(50),
    UnitType varchar(30),
    CategoryId integer(5),
    foreign key (CategoryId) references Category (CategoryId)
);

create table Conversion  (
    ConversionId integer(5) primary key,
    Unit1Id integer(5),
    Unit2Id integer(5),
    UnitType varchar(10),
    toFormula varchar(50),
    fromFormula varchar(50),
    Foreign key (Unit1Id) references Units (UnitId),
    Foreign key (Unit2Id) references Units (UnitId)
);