create table iex.treasury_yield_entity (
    date varchar(255) not null,
    maturity varchar(255) not null,
    yield decimal(40,20),
    position int,
    last_updated     datetime,
    primary key (date, maturity)
)