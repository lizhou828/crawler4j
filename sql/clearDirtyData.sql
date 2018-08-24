-- 清除含有乱码的数据
delete from remise_notice_detail where notice_id in (
	select id from remise_notice
	where title LIKE '%�%'
);
delete from remise_notice
where title LIKE '%�%' ;


delete from remise_notice_detail
where land_location like '%�%' or remark like '%�%';
delete from remise_notice where id in (
	select notice_id from remise_notice_detail
	where land_location like '%�%' or remark like '%�%'
);

