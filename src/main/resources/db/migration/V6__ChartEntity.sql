create table iex.chart_entity (
  symbol varchar(255) not null,
  date varchar(255) not null,
  date_date date,
  open decimal(19,5),
  high decimal(19,5),
  low decimal(19,5),
  close decimal(19,5),
  _change decimal(19,5),
  change_percent decimal(19,5),
  change_over_time decimal(19,5),
  label varchar(255),
  volume decimal(19,2),
  unadjusted_volume decimal(19,5),
  vwap decimal(19,5),
  last_updated datetime,
  created           datetime,
  primary key (symbol, date)
)