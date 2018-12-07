# >>>op-user

## 知识点代码示例

> ### 获取配置文件的值并设置默认值
> 	package cn.laeni.user.config
> 	@Value("${xx.xx:默认值}") String str;	#建议都要有默认值,否则找不到相关配置会报错

> ### 动态SQL语句
> #### 自定义动态SQL语句
> 	class: cn.laeni.user.dao.mapper.UserIdWarehouseMapper
> 	function: int insertUserIdWarehouse()
> #### 标准动态SQL语句
> 	class: cn.laeni.user.dao.mapper.AppDomainMapper
> 	function: List<AppDomain> findByDomainsAll()
> #### 注意
> * 动态SQL方法的形参只能是Map类型
> * 当接口方法的参数类型不是Map类型时,MyBatis会自动将其转化为Map&lt;String key,Object value&gt;类型
>   * key为@Param("list")注解指定的值(该注解一般是必须的,否则可能报错,
>   比如只有一个String类型的参数时会报错,但如果为String[]时,则key默认为"array"
>   * value为接口方法指定的类型

> ### 关联查询
> #### 一对一关联查询
> 	class: cn.laeni.user.dao.mapper.DePasswordMapper
> 	function: DeLoginName findDeLoginNameByLoginName()
> 	
> 	class: cn.laeni.user.dao.mapper.DeLoginNameMapper
> 	function: DeLoginName findDePasswordByUserId()
> 	
> ### 状态码管理和枚举类型
> 	enum cn.laeni.user.dao.entity.code.RespCode
> 	/**
> 	 * 状态码
> 	 * 		环境		  帐号			  密码
> 	 *	0(环境正常)		0(存在)		0(密码正确)
> 	 *	1(请求次数过多)	1(不存在)	   1(密码错误)
> 	 *	2(不在常用地)	  2(禁用)
> 	 *	4(黑名单环境)
> 	 */
