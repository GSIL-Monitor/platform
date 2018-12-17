DELIMITER $$

USE `op_user`$$

DROP PROCEDURE IF EXISTS `in_or_up_login_email`$$

CREATE DEFINER=`root`@`%` PROCEDURE `in_or_up_login_email`(IN `loginEmail` CHAR(32), IN `userId` CHAR(16))
BEGIN
	/**
	 * 既可以插入也可以更新
	 * 1.判断`loginEmail`是否存在,如果存在则直接退出
	 * 2.如果`loginEmail`存在则可能为更新操作,可能为插入操作
	 * 		插入: 如果`userId`不存在则插入
	 *		更新:如果`userId`存在则更新(只更新`loginEmail`值)
	 */

	#定义变量
	DECLARE `a` CHAR(32);#用于间接判断通过`loginEmail`查询的记录是否存在
	DECLARE `b` CHAR(16);#用于间接判断通过`userId`查询的记录是否存在
	
	#定义错误处理程序(遇到错误后马上退出)
	#DECLARE EXIT HANDLER FOR SQLWARNING,NOT FOUND,SQLEXCEPTION SET @info='ERROR';
	
	#查询`login_email`对应的记录是否存在
	SELECT `login_email` INTO `a` FROM `login_email` WHERE `login_email`=`loginEmail`;
	
	IF `a` IS NULL THEN
		#查询`user_id`对应的记录是否存在
		SELECT `user_id` INTO `b` FROM `login_email` WHERE `user_id`=`userId`;
		SELECT "852";
	END IF;
END$$

DELIMITER ;