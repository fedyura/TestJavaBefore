package ru.mail.projects.utils_test;

import java.util.logging.Logger;

import org.testng.annotations.Test;

import ru.mail.projects.utils.ThreadHelper;

public class ThreadHelperTest {
  
	@Test(timeOut = 1050)
    public void SleepTest() {
	  
		Logger log = Logger.getLogger ("Log");
		ThreadHelper.Sleep(1000, log);
	}
}
