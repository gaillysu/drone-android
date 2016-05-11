package com.dayton.drone.application;

import android.test.ApplicationTestCase;
import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<ApplicationModel> {
    public ApplicationTest() {
        super(ApplicationModel.class);
    }
    public static Test suite(){
        return new TestSuiteBuilder(ApplicationTest.class).includeAllPackagesUnderHere().build();
    }


}