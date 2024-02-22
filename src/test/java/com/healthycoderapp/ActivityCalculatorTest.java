package com.healthycoderapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class ActivityCalculatorTest {

    @Test
    void should_ReturnBad_When_AvgBelow20() {
        //given
        int weeklyCardioMin = 40;
        int weeklyWorkouts = 1;

        //when
        String actual = ActivityCalculator.rateActivityLevel(weeklyCardioMin, weeklyWorkouts);

        //then
        assertEquals("bad", actual);
    }

    @Test
    void should_ReturnAverage_When_AvgBetween20And40() {
        //given
        int weeklyCardioMin = 40;
        int weeklyWorkouts = 3;

        //when
        String actual = ActivityCalculator.rateActivityLevel(weeklyCardioMin, weeklyWorkouts);

        //then
        assertEquals("average", actual);
    }

    @Test
    void should_ReturnGood_When_AvgAbove40() {
        //given
        int weeklyCardioMin = 40;
        int weeklyWorkouts = 7;

        //when
        String actual = ActivityCalculator.rateActivityLevel(weeklyCardioMin, weeklyWorkouts);

        //then
        assertEquals("good", actual);
    }

    @Test
    void should_TrowException_When_InputBelowZero() {
        //given
        int weeklyCardioMin = -40;
        int weeklyWorkouts = 1;

        //when
        Executable executable = () -> ActivityCalculator.rateActivityLevel(weeklyCardioMin, weeklyWorkouts);

        //then
        assertThrows(RuntimeException.class, executable);
    }
}