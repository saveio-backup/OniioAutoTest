package com.ontfs.dataProvider;

import org.testng.annotations.DataProvider;

public class SectorDataProvider {

    @DataProvider(name = "invalidSectorId")
    public Object[][] invalidSectorId() {

        return new Object[][] { { "-1" }, { "0" },{ "" }, { "wallet" }, {" "},{"1.1"},{"3.1"} ,{"\\/*%"},{1}};
    }

    @DataProvider(name="invalidProveLevel")
    public Object[][] invalidProveLevel(){
        return  new Object[][]{{-1},{0},{4},{12},{" "},{""},{"provelevel"}};
    }

    @DataProvider(name="invalidSectorSize")
    public Object[][] invalidSectorSize(){
        return  new Object[][]{{""},{" "},{0},{"0"},{-1},{"-1"},{"1023M"},{"1.2G"},{"100000000G"}};
    }
}
