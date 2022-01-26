package com.florczakdavid.maru.utils;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

public class Utils {
    public static String getText(ViewInteraction matcher) {
        final String[] text = new String[1];

        matcher.perform(new ViewAction()  {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom( TextView.class );
            }

            @Override
            public String getDescription() {
                return "Text of the view";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView) view;
                text[0] = tv.getText().toString();
            }
        });

        return text[0];
    }
}
