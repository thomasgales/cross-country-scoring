package com.example.crosscountryscoring;

import android.content.Context;

// Credit to George Mount on SO for this code: https://stackoverflow.com/a/50193926/5011382
public class NullConverter {
    public static String runnerPlaceToString(Context context, int value) {
        if (value == 0) {
            return context.getResources().getString(R.string.finisher_placeholder);
        }
        return String.valueOf(value);
    }
}
