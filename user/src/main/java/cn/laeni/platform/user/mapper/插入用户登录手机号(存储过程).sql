DELIMITER $$

USE `op_user`$$

DROP PROCEDURE IF EXISTS `in_or_up_login_phone`$$

CREATE DEFINER=`root`@`%` PROCEDURE `in_or_up_login_phone`(IN `loginPhone` CHAR(16), IN `userId` CHAR(16))
BEGIN
	/**
	 * 既可以插入也可以更新
	 * 1.判断`loginPhone`是否存在,如果存在则直接退出
	 * 2.如果`loginPhone`存在则可能为更新操作,可能为插入操作
	 * 		插入: 如果`userId`不存在则插入
	 *		更新:如果`userId`存在则更新(只更新`loginPhone`值)
	 */

	#定义变量
	DECLARE `a` CHAR(16);#用于间接判断通过`loginPhone`查询的记录是否存在
	DECLARE `b` CHAR(16);#用于间接判断通过`userId`查询的记录是否存在
	
	#定义错误处理程序(遇到错误后马上退出)
	#DECLARE EXIT HANDLER FOR SQLWARNING,NOT FOUND,SQLEXCEPTION SET @info='ERROR';
	
	#查询`login_phone`对应的记录是否存在
	SELECT `login_phone` INTO `a` FROM `login_phone` WHERE `login_phone`=`loginPhone`;
	
	IF `a` IS NULL THEN
		#查询`user_id`对应的记录是否存在
		SELECT `user_id` INTO `b` FROM `login_phone` WHERE `user_id`=`userId`;
		IF `b` IS NULL THEN
			#`login_phone`和`user_id`都不存在,插入操作';
			INSERT INTO `login_phone` (`login_phone`, `user_id`) VALUES (`loginPhone`, `userId`);
		ELSE
			#`login_phone`不存在,`user_id`存在,更新操作';
			UPDATE `login_phone` SET `login_phone` = `loginPhone` WHERE `user_id` = `userId`; 
		END IF;
	END IF;
END$$

DELIMITER ;