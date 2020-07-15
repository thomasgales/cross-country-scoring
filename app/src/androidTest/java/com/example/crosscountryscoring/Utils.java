package com.example.crosscountryscoring;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.action.ViewActions.*;
//import androidx.test.espresso.action.ViewActions.replaceText;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class Utils {
    // Credit to riwnodennyk on SO for this code:
    // https://stackoverflow.com/questions/31394569/how-to-assert-inside-a-recyclerview-in-espresso
    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    // Credit to blade on SO for the basis of this code:
    // https://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item
    public static ViewAction replaceTextForId(final int id, final String text) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Replace text on a child view with specified text.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                EditText v = view.findViewById(id);
                v.setText(text);
            }
        };
    }
}
