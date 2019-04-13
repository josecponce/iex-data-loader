create table iex.dividends_entity (
--   id            bigint IDENTITY(1,1) not null,
  amount        decimal(19, 3),
  symbol varchar(255),
  declared_date date,
  ex_date       date,
  flag          varchar(255),
  indicated     varchar(255),
  payment_date  date,
  qualified     varchar(255),
  record_date   date,
  type          varchar(255),
  last_updated datetime,
  created           datetime,
  primary key (symbol, ex_date)
)