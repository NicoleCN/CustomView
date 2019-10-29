package com.example.customview.autotest;

import android.app.Activity;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.customview.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

/***
 * @date 2019-10-28 16:59
 * @author BoXun.Zhao
 * @description
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AutoTestActivityTest {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(AutoTestActivity.class);

    public Activity autoTestActivity;

    @Before
    public void setup() {
//        Intent intent = new Intent();
//        autoTestActivity = mActivityRule.launchActivity(intent);
    }

    @Test
    public void buttonClick() {
//        assertEquals("com.example.customview", "com.example.customview");

        //检验：一开始，textView不显示
        onView(withId(R.id.auto_test_TextView)).check(matches(not(isDisplayed())));

        //检验：button的文字内容
        onView(withId(R.id.auto_test_button))
                .check(matches(withText("自动化测试的Button")))
                .perform(click());  //操作：点击按钮

        //检验：textView内容是否修改，并且变为可见
        onView(withId(R.id.auto_test_TextView))
                .check(matches(withText("自动化测试完成")))
                .check(matches(isDisplayed()));
    }
}
