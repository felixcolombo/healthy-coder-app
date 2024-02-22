package com.healthycoderapp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BMICalculatorTest {

    private String environment = "dev";

    @BeforeAll
    static void beforeAll() {
        System.out.println("Utilizado para iniciar uma conexão com o banco de dados antes de todos os testes, por exemplo.");
    }

    @BeforeAll
    static void afterAll() {
        System.out.println("Utilizado para fechar a conexão com o banco de dados depois de todos os testes, por exemplo.");
    }

    @Nested
    class IsDietRecommendedTests {
        @Test
        @DisplayName("Teste Desativado")
        @Disabled
        void should_ReturnTrue_When_DietRecommend() {
            //given
            double weight = 89.0;
            double height = 1.72;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);
        }

        //Repetindo o mesmo teste 10x
        //Método com números aleatórios, métodos que tenham alteração de estado ou é executado em vários threads
        @RepeatedTest(value = 10, name = RepeatedTest.LONG_DISPLAY_NAME)
        void should_ReturnTrue_When_DietRecommend_RepeatedTest() {
            //given
            double weight = 89.0;
            double height = 1.72;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);
        }

        //Teste com apenas um valor de altura e vários pesos
        @ParameterizedTest
        @ValueSource(doubles = {89.0, 95.0, 110.0})
        void should_ReturnTrue_When_DietRecommend_ValueSource(Double coderWeight) {
            //given
            double weight = coderWeight;
            double height = 1.72;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);
        }

        //Teste com valores de peso e altura parametrizado
        @ParameterizedTest(name = "weight={0}, height={1}")
        @CsvSource(value = {"89.0, 1.72", "95.0, 1.75", "110.0, 1.78"})
        void should_ReturnTrue_When_DietRecommend_CsvSource(Double coderWeight, Double codeHeight) {
            //given
            double weight = coderWeight;
            double height = codeHeight;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);
        }

        //Teste com valores importados de um arquivo CSV
        @ParameterizedTest(name = "weight={0}, height={1}")
        @CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
        void should_ReturnTrue_When_DietRecommend_CsvFileSource(Double coderWeight, Double codeHeight) {
            //given
            double weight = coderWeight;
            double height = codeHeight;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);
        }

        //Desativiar o teste de acordo com o Sistema Operacional
        @Test
        @DisabledOnOs(OS.LINUX)
        void should_ReturnFalse_When_DietRecommend() {
            //given
            double weight = 50.0;
            double height = 1.92;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertFalse(recommended);
        }

        @Test
        void should_ThrowArithmeticException_When_HeightZero() {
            //given
            double weight = 50.0;
            double height = 0.0;

            //when
            Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

            //then
            assertThrows(ArithmeticException.class, executable);
        }
    }

    @Nested
    class FindCoderWithWorstBMITests {
        @Test
        void should_ReturnCoderWithWorstBMI_When_CoderListNotEmpty() {
            //given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.82, 64.7));

            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertAll(
                    () -> assertEquals(1.82, coderWorstBMI.getHeight()),
                    () -> assertEquals(98.0, coderWorstBMI.getWeight())
            );
        }

        @Test
        void should_ReturnNullWorstBMICoder_When_CoderListEmpty() {
            //given
            List<Coder> coders = new ArrayList<>();

            //when
            Coder codersWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertNull(codersWorstBMI);
        }

        @Test
        void should_ReturnCoderWithWorstBMIIn100Ms_When_CoderListHas10000Elements() {
            //given
            List<Coder> coders = new ArrayList<>();

            for(int i = 0; i < 10000; i++) {
                coders.add(new Coder(1.0 + i, 10.0 + i));
            }

            //when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertTimeout(Duration.ofMillis(100), executable);
        }

        @Test
        void should_ReturnCoderWithWorstBMIIn100Ms_When_CoderListHas10000Elements_InPROD() {
            //given
            assumeTrue(BMICalculatorTest.this.environment.equals("prod"));
            List<Coder> coders = new ArrayList<>();

            for(int i = 0; i < 10000; i++) {
                coders.add(new Coder(1.0 + i, 10.0 + i));
            }

            //when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertTimeout(Duration.ofMillis(100), executable);
        }
    }

    @Nested
    class GetBMIScoresTests {
        @Test
        void should_ReturnCorrectBMIScoreArray_When_CoderListNotEmpty() {
            //given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.82, 64.7));
            double[] expected = {18.52, 29.59, 19.53};

            //when
            double[] bmiScores = BMICalculator.getBMIScores(coders);

            //then
            assertArrayEquals(expected, bmiScores);
        }
    }
}