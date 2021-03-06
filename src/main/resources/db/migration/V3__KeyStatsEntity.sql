create table iex.key_stats_entity (
--   id bigint IDENTITY(1,1) not null,
  ebitda decimal(40,20),
  epssurprise_dollar decimal(40,20),
  epssurprise_percent decimal(40,20),
  _float decimal(40,20),
  beta decimal(40,20),
  cash decimal(40,20),
  company_name varchar(255),
  consensuseps decimal(40,20),
  day200moving_avg decimal(40,20),
  day30change_percent decimal(40,20),
  day50moving_avg decimal(40,20),
  day5change_percent decimal(40,20),
  debt decimal(40,20),
  dividend_rate decimal(40,20),
  dividend_yield decimal(40,20),
  ex_dividend_date datetime,
  gross_profit decimal(40,20),
  insider_percent decimal(40,20),
  institution_percent decimal(40,20),
  latesteps decimal(40,20),
  latestepsdate date,
  marketcap decimal(40,20),
  month1change_percent decimal(40,20),
  month3change_percent decimal(40,20),
  month6change_percent decimal(40,20),
  number_of_estimates decimal(40,20),
  pe_ratio_high decimal(40,20),
  pe_ratio_low decimal(40,20),
  price_to_book decimal(40,20),
  price_to_sales decimal(40,20),
  profit_margin decimal(40,20),
  return_on_assets decimal(40,20),
  return_on_capital decimal(40,20),
  return_on_equity decimal(40,20),
  revenue decimal(40,20),
  revenue_per_employee decimal(40,20),
  revenue_per_share decimal(40,20),
  shares_outstanding decimal(40,20),
  short_date date,
  short_interest decimal(40,20),
  short_ratio decimal(40,20),
  symbol varchar(255),
  ttmeps decimal(40,20),
  week52change decimal(40,20),
  week52high decimal(40,20),
  week52low decimal(40,20),
  year1change_percent decimal(40,20),
  year2change_percent decimal(40,20),
  year5change_percent decimal(40,20),
  ytd_change_percent decimal(40,20),
  last_updated datetime,
  primary key (symbol)
)