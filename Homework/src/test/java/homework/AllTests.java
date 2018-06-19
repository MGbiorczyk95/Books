package homework;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import homework.services.HomeworkServiceTest;
import homework.services.rests.HomeworkRestServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ HomeworkServiceTest.class, HomeworkRestServiceTest.class })
public class AllTests {
}
