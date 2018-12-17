var laeni = {
	/*
	 * 去除字符串中的字母(只保留最后一个字母前的内容),并转换为Int类型
	 * 比如去除"px"
	 */
	toInt: function($str) {
		switch (typeof($str)){
			case "number":
				return $str
			case "string":
				return parseInt($str.replace(/[^-{0,1}\d\.]/g, ""));
			default:
				return null;
		}
	},
	
}
