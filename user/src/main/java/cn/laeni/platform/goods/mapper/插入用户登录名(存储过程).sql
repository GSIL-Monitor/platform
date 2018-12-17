DELIMITER $$

USE `op_user`$$

DROP PROCEDURE IF EXISTS `in_or_up_login_name`$$

CREATE DEFINER=`root`@`%` PROCEDURE `in_or_up_login_name`(IN `loginName` CHAR(16), IN `userId` CHAR(16))
BEGIN
	/**
	 * 既可以插入也可以更新
	 * 1.判断`loginName`是否存在,如果存在则直接退出
	 * 2.如果`loginName`存在则可能为更新操作,可能为插入操作
	 * 		插入: 如果`userId`不存在则插入
	 *		更新:如果`userId`存在则更新(只更新`loginName`值)
	 */

	#定义变量
	DECLARE `a` CHAR(16);#用于间接判断通过`loginName`查询的记录是否存在
	DECLARE `b` CHAR(16);#用于间接判断通过`userId`查询的记录是否存在
	
	#定义错误处理程序(遇到错误后马上退出)
	#DECLARE EXIT HANDLER FOR SQLWARNING,NOT FOUND,SQLEXCEPTION SET @info='ERROR';
	
	#查询`login_name`对应的记录是否存在
	SELECT `login_name` INTO `a` FROM `login_name` WHERE `login_name`=`loginName`;
	
	IF `a` IS NULL THEN
		#查询`user_id`对应的记录是否存在
		SELECT `user_id` INTO `b` FROM `login_name` WHERE `user_id`=`userId`;
		IF `b` IS NULL THEN
			#`login_name`和`user_id`都不存在,插入操作';
			INSERT INTO `login_name` (`login_name`, `user_id`) VALUES (`loginName`, `userId`);
		ELSE
			#`login_name`不存在,`user_id`存在,更新操作';
			UPDATE `login_name` SET `login_name` = `loginName` WHERE `user_id` = `userId`; 
		END IF;
	END IF;
END$$

DELIMITER ;