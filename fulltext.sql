-- alter table question add fulltext index fulltext_title(title) with parser ngram; 
select * from question where match(title) AGAINST('t1');

-- alter table answer add fulltext index fulltext_contetn(content) with parser ngram;
select * from answer 
where match(content) AGAINST('answer');