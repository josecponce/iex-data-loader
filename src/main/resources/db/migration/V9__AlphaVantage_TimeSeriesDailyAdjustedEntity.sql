create table iex.time_series_daily_adjusted_entity (
    date date not null,
    symbol varchar(255) not null,
    last_updated datetime,
    adjusted_close decimal(40,20),
    close decimal(40,20),
    dividend_amount decimal(40,20),
    high decimal(40,20),
    low decimal(40,20),
    open decimal(40,20),
    split_coefficient decimal(40,20),
    volume decimal(40,20),
    primary key (date, symbol)
)