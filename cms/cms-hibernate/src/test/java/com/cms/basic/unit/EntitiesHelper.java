package com.cms.basic.unit;

import com.cms.basic.model.User;
import org.junit.Assert;

public class EntitiesHelper {

    private static User baseUser = new User(1,"张三");

    public static void assertUser(User expected,User actual){
        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getId(),actual.getId());
        Assert.assertEquals(expected.getUsername(),actual.getUsername());
    }

    public static void assertUser(User expected){
        assertUser(expected,baseUser);
    }
}
