package cn.laeni;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MyTest {

    @Test
    public void test1() throws IOException {
        long l1 = System.currentTimeMillis();
        File file = new File("D:\\课堂备份.vmdk");
        System.out.println("文件长度:" + file.length() + "字节");
        System.out.println(System.currentTimeMillis() - l1);
    }
}
