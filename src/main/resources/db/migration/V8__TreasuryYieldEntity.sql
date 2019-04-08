create table treasury.treasury_yield_entity (
    date varchar(255) not null,
    maturity varchar(255) not null,
    yield double precision,
    position int,
    last_updated     datetime,
    primary key (date, maturity)
)