import cn.laeni.sms.AliSms;
import cn.laeni.sms.SmsAbs;
import com.aliyuncs.exceptions.ClientException;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AliSmsTest {
	SmsAbs SmsAbs = new AliSms();
	@Test
	public void testSendSms() {
		try {
			System.out.println(SmsAbs.sendSms("17608776656", "LAENI网", "SMS_138062930",
					"{\"code\":\"" + 85200 + "\"}"));
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void pat() {

	}

}
