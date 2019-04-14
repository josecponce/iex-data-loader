create table iex.chart_entity (
  symbol varchar(255) not null,
  date varchar(255) not null,
  date_date date,
  open decimal(40,20),
  high decimal(40,20),
  low decimal(40,20),
  close decimal(40,20),
  _change decimal(40,20),
  change_percent decimal(40,20),
  change_over_time decimal(40,20),
  label varchar(255),
  volume decimal(40,20),
  unadjusted_volume decimal(40,20),
  vwap decimal(40,20),
  last_updated datetime,
  primary key (symbol, date)
)