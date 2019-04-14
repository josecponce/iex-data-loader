create table iex.split_entity (
  symbol            varchar(255),
  ex_date           date,
  declared_date     date,
  record_date       date,
  payment_date      date,
  ratio             decimal(40,20),
  to_factor         decimal(40,20),
  for_factor        decimal(40,20),
  last_updated      datetime,
  primary key (symbol, ex_date)
)