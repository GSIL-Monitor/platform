package cn.laeni.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import cn.laeni.user.dao.entity.AppDomain;
import cn.laeni.user.dao.mapper.dynaSqlProvider.AppDomainDynamic;

/**
 * 查询访问者域名与应用的对应关系
 * @author laeni.cn
 *
 */
public interface AppDomainMapper {

	/**
	 * 根据域名返回最多一条记录
	 * @param domain
	 * @return
	 */
	@Select("SELECT * FROM `app_domain` WHERE `domain`=#{domain} LIMIT 1")
	AppDomain findByDomain(String domain);
	
	/**
	 * 获得访问者属于哪个应用
	 * @param domian 访问者域名
	 * @return
	 */
	@SelectProvider(type=AppDomainDynamic.class, method = "findByDomainsAll")
	List<AppDomain> findByDomainsAll(@Param("array") String... domian);
}
