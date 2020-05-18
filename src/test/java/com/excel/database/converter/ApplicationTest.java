package com.excel.database.converter;

import com.excel.database.converter.util.HttpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p>
 *     服务测试
 * </p>
 * @author zhangbin
 * @date 2020-05-13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {

    @Test
    public void excel() throws IOException {
        System.out.println(HttpUtil.post("http://localhost:2551/convert/excel_to_sqlite",
                new File(ApplicationTest.class.getResource("/").getPath() + "demo.xlsx"),
                new HashMap<>(0)));
    }
}
