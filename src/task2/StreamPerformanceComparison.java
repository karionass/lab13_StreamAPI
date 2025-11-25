package task2;

import java.util.*;
import java.util.stream.*;

public class StreamPerformanceComparison {
    private static final int DATA_SIZE = 10_000_000;
    public static void main(String[] args) {
        System.out.println("=== Сравнение производительности потоков === ");
        System.out.println("Размер данных: " + DATA_SIZE + " элементов");
        List<Integer> numbers = IntStream.rangeClosed(1, DATA_SIZE)
                .boxed().collect(Collectors.toList());
        System.out.println("Количество процессоров: "
                + Runtime.getRuntime().availableProcessors());

        testFiltering(numbers);
        testSquareSum(numbers);
        testPrimes();
    }

    private static void testFiltering(List<Integer> numbers) {
        System.out.println("\n--- Операция 1: Фильтрация чётных ---");

        long startSeq = System.currentTimeMillis();
        long seqCount = numbers.stream().filter(n -> n % 2 == 0).count();
        long seqTime = System.currentTimeMillis() - startSeq;

        long startPar = System.currentTimeMillis();
        long parCount = numbers.parallelStream().filter(n -> n % 2 == 0).count();
        long parTime = System.currentTimeMillis() - startPar;

        print("Последовательный", seqCount, seqTime);
        print("Параллельный", parCount, parTime);
        printSpeed(seqTime, parTime);
    }

    //При возведении в квадрат и суммирование
    private static void testSquareSum(List<Integer> numbers) {
        System.out.println("\n--- Операция 2: Квадрат и сумма ---");

        long startSeq = System.currentTimeMillis();
        long sumSeq = numbers.stream().mapToLong(n -> (long) n * n).sum();
        long seqTime = System.currentTimeMillis() - startSeq;

        long startPar = System.currentTimeMillis();
        long sumPar = numbers.parallelStream().mapToLong(n -> (long) n * n).sum();
        long parTime = System.currentTimeMillis() - startPar;

        print("Последовательный", sumSeq, seqTime);
        print("Параллельный", sumPar, parTime);
        printSpeed(seqTime, parTime);
    }


    private static void testPrimes() {
        System.out.println("\n--- Операция 3: Простые числа (1–100 000) ---");

        int limit = 100_000;
        List<Integer> nums = IntStream.rangeClosed(1, limit).boxed().toList();

        long startSeq = System.currentTimeMillis();
        long countSeq = nums.stream().filter(StreamPerformanceComparison::isPrime).count();
        long seqTime = System.currentTimeMillis() - startSeq;

        long startPar = System.currentTimeMillis();
        long countPar = nums.parallelStream().filter(StreamPerformanceComparison::isPrime).count();
        long parTime = System.currentTimeMillis() - startPar;

        print("Последовательный", countSeq, seqTime);
        print("Параллельный", countPar, parTime);
        printSpeed(seqTime, parTime);
    }

    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        return IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(x -> n % x == 0);
    }

    private static void print(String type, long result, long time) {
        System.out.println(type + " поток:");
        System.out.println("Результат: " + result);
        System.out.println("Время: " + time + " мс\n");
    }

    private static void printSpeed(long seq, long par) {
        double speedup = (double) seq / par;
        System.out.printf("Ускорение: %.2fx\n", speedup);
    }
}
