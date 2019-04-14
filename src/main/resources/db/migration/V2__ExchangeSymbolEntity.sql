create table iex.exchange_symbol_entity (
--   id      bigint IDENTITY(1,1) not null,
  enabled bit,
  date    date,
  iex_id  bigint,
  name    varchar(255),
  symbol  varchar(255),
  type    varchar(255),
  last_updated datetime,
  primary key (symbol)
)