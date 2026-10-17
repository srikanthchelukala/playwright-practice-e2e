package com.pw.utils;
import org.testng.annotations.DataProvider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pw.testdata.LoginTestData;

public final class DataProviderUtil {
    private static final String DATA_FILE = "/testdata/login-test-data.json";

    private DataProviderUtil() {
    }

    @DataProvider(name = "loginTestDataExpEr")//All data provider should retrun multi dimensional array of test data so return type is Object[][]. 
    public static Object[][] loginTestDataExpEr() { // Multi dimesional array means an array of arrays, where each inner array represents a set of test data for one test execution.
        return DataProviderUtil.load(DATA_FILE);
    }

    @DataProvider(name = "userlogin")
    public static Object[][] userlogin() {
        return new Object[][] {
                { Map.of("username", "standard_user", "password", "secret_sauce") },
                { Map.of("username", "visual_user", "password", "secret_sauce") }
        };
    }

    @DataProvider(name = "loginTestData")
    public static Object[][] load(String filepath) {
        try (InputStream dataStream = DataProviderUtil.class.getResourceAsStream(filepath)) {
            if (dataStream == null) {
                throw new IllegalStateException("Test data not found: " + filepath);
            }

            LoginTestData[] data = new ObjectMapper().readValue(dataStream, LoginTestData[].class);
            return Arrays.stream(data)
                    .map(testData -> new Object[] { testData })
                    .toArray(Object[][]::new);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read test data: " + DATA_FILE, exception);
        }
    }

    @DataProvider(name = "checkoutData")
    public static Object[][] checkoutData() throws IOException {
        return getjsonDataToMap("/testdata/checkout_testdata.json");
    }

   

    @DataProvider(name = "loginTestDataExpErrMsg")
    public static Object[][] loginTestDataExpErrMsg() throws IOException {
        return getjsonDataToMap("/src/test/resources/testdata/login-test-validlockdata.json");
    }

public static Object[][] getjsonDataToMap(String filepath) throws IOException{
    String jsonContent=new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+filepath)));
    Type type=new TypeToken<List<HashMap<String,String>>>(){}.getType();
    List<HashMap<String,String>> list=new Gson().fromJson(jsonContent,type);
    Object[][] table=new Object[list.size()][1];
    for(int i=0;i<list.size();i++){
        table[i][0]=list.get(i);
    }
    return table;
}


    @DataProvider(name="login-data")
    public static Object[][] loginData(){
        return new Object [][]{{"standard_user", "secret_sauce"},{"invalid_user", "invalid_password"},{"locked_out_user", "secret_sauce"}};
    }
}