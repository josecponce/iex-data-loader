create table iex.chart_entity (
  symbol varchar(255) not null,
  date varchar(255) not null,
  open decimal(19,2),
  high decimal(19,2),
  low decimal(19,2),
  close decimal(19,2),
  _change decimal(19,2),
  change_percent decimal(19,2),
  change_over_time decimal(19,2),
  label varchar(255),
  volume decimal(19,2),
  unadjusted_volume decimal(19,2),
  vwap decimal(19,2),
  last_updated datetime,
  primary key (symbol, date)
)