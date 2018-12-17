package cn.laeni.platform.user.mapper.dynaSqlProvider;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

public class AppDomainDynamic {

	public String findByDomainsAll(Map<String, String[]> strings) {
		String[] pres = strings.get("array");

		String sql = new SQL() {
			{
				SELECT("*");
				FROM("app_domain");
				for (int i = 0; i < pres.length; i++) {
					if (pres[i] == null) {
						pres[i] = "";
					}

					if (i != 0) {
						OR();
					}

					WHERE("`domain` = '" + pres[i] + "'");
				}
			}
		}.toString();
		//System.out.println(sql);
		return sql;
	}
}
