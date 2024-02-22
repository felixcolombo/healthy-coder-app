package com.healthycoderapp;

public class ActivityCalculator {

    private static final int WORKOUT_DURATION_MIN = 45;

    public static String rateActivityLevel(
            int weeklyCardioMin, int weeklyWorkoutSessions) {

        if(weeklyCardioMin < 0 || weeklyWorkoutSessions < 0) {
            throw new RuntimeException("Input Below 0");
        }

        int totalMinutes = weeklyCardioMin + weeklyWorkoutSessions * WORKOUT_DURATION_MIN;
        double avgDailyActivityMin = totalMinutes / 7.0;

        if(avgDailyActivityMin < 20) {
            return "bad";
        } else if (avgDailyActivityMin < 40) {
            return "average";
        }
        return "good";
    }
}
