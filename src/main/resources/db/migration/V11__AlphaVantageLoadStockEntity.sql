create table management.alpha_vantage_load_stock_entity (
    symbol varchar(255) not null,
    last_updated datetime,
    primary key (symbol)
)