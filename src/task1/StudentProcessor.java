package task1;

import java.util.*;
import java.util.stream.Collectors;

public class StudentProcessor {
    static class Student {
        private String name;
        private int age;
        private int course;
        private double gpa;

        public Student(String name, int age, int course, double gpa) {
            this.name = name;
            this.age = age;
            this.course = course;
            this.gpa = gpa;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
        public int getCourse() { return course; }
        public double getGpa() { return gpa; }

        @Override
        public String toString() {
            return String.format("%s (Курс %d, GPA %.2f)", name, course, gpa);
        }
    }

    public static void main(String[] args) {
        List<Student> students = generateStudents();

        System.out.println("=== Обработка данных студентов ===");

        // 1. Фильтрация по GPA > 3.5
        List<Student> highGpa = students.stream()
                .filter(s -> s.getGpa() > 3.5)
                .collect(Collectors.toList());
        System.out.println("\n1.Студенты с GPA > 3.5:");
        highGpa.forEach(System.out::println);

        // 2. Сортировка по имени
        System.out.println("\n2. Сортировка по имени:");
        students.stream()
                .sorted(Comparator.comparing(Student::getName))
                .forEach(System.out::println);

        // 3. Имена студентов 3 курса
        System.out.println("\n3. Студенты 3-го курса:");
        List<String> names = students.stream()
                .filter(s -> s.getCourse() == 3)
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println(names);

        // 4. Средний GPA
        double avgGpa = students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
        System.out.println("\n4. Средний GPA всех студентов: " + avgGpa);

        // 5. Лучший студент
        Student best = students.stream()
                .max(Comparator.comparingDouble(Student::getGpa))
                .orElse(null);
        System.out.println("\n5. Студент с максимальным GPA:");
        System.out.println(best);

        // 6. Группировка по курсам
        System.out.println("\n6. Студенты по курсам:");
        Map<Integer, List<Student>> byCourse = students.stream()
                .collect(Collectors.groupingBy(Student::getCourse));
        byCourse.forEach((course, list) -> {
            System.out.println("Курс " + course + ": " + list.size() + " студентов");
        });

        // 7. Количество студентов по курсам
        System.out.println("\n7. Количество студентов на каждом курсе:");
        Map<Integer, Long> count = students.stream()
                .collect(Collectors.groupingBy(Student::getCourse, Collectors.counting()));
        System.out.println(count);
    }

    private static List<Student> generateStudents() {
        return Arrays.asList(
                new Student("Алина", 19, 3, 3.6),
                new Student("Илья", 17, 1, 3.1),
                new Student("Снежана", 19, 3, 3.3),
                new Student("Жанна", 18, 2, 3.6),
                new Student("Карина", 19, 3, 3.4),
                new Student("Руслан", 18, 1, 3.0),
                new Student("Алан", 21, 3, 3.3)
        );
    }
}
