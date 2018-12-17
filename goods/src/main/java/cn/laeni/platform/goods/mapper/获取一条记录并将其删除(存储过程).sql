DROP FUNCTION IF EXISTS `get_userid_and_del_this`;
DELIMITER $$

CREATE DEFINER=`root`@`%` PROCEDURE `get_userid_and_del_this`(OUT userId CHAR(16))
COMMENT '从userid库中随机取一条返回,同时将其删除'
BEGIN
	#(方法1)将查询到的 user_id 字段对应的值赋值给 userId 变量
	SELECT `user_id` INTO `userId` FROM `user_id_warehouse` ORDER BY RAND() LIMIT 1;
	#(方法2)将查询的值赋值给userId
	#SET userId = (SELECT `user_id` FROM `user_id_warehouse` ORDER BY RAND() LIMIT 1);
	#返回该记录后将该条记录删除
	DELETE FROM `user_id_warehouse` WHERE `user_id` = `userId`;
END $$

DELIMITER ;