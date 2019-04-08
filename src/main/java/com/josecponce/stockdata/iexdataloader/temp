select
	date,
	(12 * sum(position * ifnull(yield, 0))
		- sum(position) * sum(ifnull(yield, 0))) /
	(12 * sum(position * position) - sum(position) * sum(position)) as 'slope'
from treasury.treasury_yield_entity
group by date
order by (12 * sum(position * ifnull(yield, 0))
		- sum(position) * sum(ifnull(yield, 0))) /
	(12 * sum(position * position) - pow(sum(position), 2)) asc;