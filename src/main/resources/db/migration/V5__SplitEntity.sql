create table iex.split_entity (
  symbol           varchar(255),
  ex_date          date,
  declared_date    date,
  record_date      date,
  payment_date     date,
  ratio           decimal(19,2),
  to_factor        decimal(19,2),
  for_factor      decimal(19,2),
  last_updated     datetime,
  primary key (symbol, ex_date)
)