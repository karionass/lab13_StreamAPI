package task3;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;

public class SalesAnalyzer {

    static class Sale {
        private String product;
        private String category;
        private double price;
        private int quantity;
        private LocalDate date;

        public Sale(String product, String category, double price, int quantity, LocalDate date) {
            this.product = product;
            this.category = category;
            this.price = price;
            this.quantity = quantity;
            this.date = date;
        }

        public String getProduct() { return product; }
        public String getCategory() { return category; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public LocalDate getDate() { return date; }

        public double getTotalAmount() {
            return price * quantity;
        }

        @Override
        public String toString() {
            return String.format("%s (%s) - %.2f₸ x %d = %.2f₸",
                    product, category, price, quantity, getTotalAmount());
        }
    }

    public static void main(String[] args) {
        List<Sale> sales = generateSales();

        System.out.println("=== Анализ данных о продажах ===");
        System.out.println("Всего продаж: " + sales.size());

        // 1. Общая сумма продаж
        double totalRevenue = sales.stream()
                .mapToDouble(Sale::getTotalAmount)
                .sum();
        System.out.printf("\n1. Общая сумма продаж: %.2f₸\n", totalRevenue);

        // 2. Сумма продаж по категориям
        System.out.println("\n2. Продажи по категориям:");
        Map<String, Double> revenueByCategory = sales.stream()
                .collect(Collectors.groupingBy(Sale::getCategory,
                        Collectors.summingDouble(Sale::getTotalAmount)));
        revenueByCategory.forEach((cat, sum) ->
                System.out.printf("%s: %.2f₸\n", cat, sum));

        // 3. Самый продаваемый товар
        Map<String, Integer> productQuantities = sales.stream()
                .collect(Collectors.groupingBy(Sale::getProduct,
                        Collectors.summingInt(Sale::getQuantity)));
        Map.Entry<String, Integer> topProduct =
                productQuantities.entrySet().stream()
                        .max(Map.Entry.comparingByValue()).get();

        System.out.println("\n3. Самый продаваемый товар:");
        String bestProduct = topProduct.getKey();
        int totalQty = topProduct.getValue();

        Sale exampleSale = sales.stream()
                .filter(s -> s.getProduct().equals(bestProduct))
                .findFirst().get();

        System.out.printf("%s (%s) - %.2f₸ x %d = %.2f₸\nОбщее количество: %d единиц\n",
                exampleSale.getProduct(),
                exampleSale.getCategory(),
                exampleSale.getPrice(),
                exampleSale.getQuantity(),
                exampleSale.getTotalAmount(),
                totalQty);

        // 4. Средняя цена по категориям
        System.out.println("\n4. Средняя цена по категориям:");
        Map<String, Double> avgPrice = sales.stream()
                .collect(Collectors.groupingBy(Sale::getCategory,
                        Collectors.averagingDouble(Sale::getPrice)));
        avgPrice.forEach((cat, avg) ->
                System.out.printf("%s: %.2f₸\n", cat, avg));

        // 5. Топ-5 самых дорогих продаж
        System.out.println("\n5. Топ-5 самых дорогих продаж:");
        List<Sale> top5 = sales.stream()
                .sorted(Comparator.comparingDouble(Sale::getTotalAmount).reversed())
                .limit(5)
                .collect(Collectors.toList());
        for (int i = 0; i < top5.size(); i++) {
            System.out.println((i + 1) + ". " + top5.get(i));
        }

        // 6. Количество продаж по категориям
        System.out.println("\n6. Количество продаж по категориям:");
        Map<String, Long> countByCategory = sales.stream()
                .collect(Collectors.groupingBy(Sale::getCategory,
                        Collectors.counting()));
        countByCategory.forEach((cat, cnt) ->
                System.out.println(cat + ": " + cnt + " продаж"));

        // 7. Статистика по ценам
        System.out.println("\n7. Статистика по ценам:");
        DoubleSummaryStatistics stats = sales.stream()
                .mapToDouble(Sale::getPrice)
                .summaryStatistics();
        System.out.printf(
                "Минимальная цена: %.2f₸\nМаксимальная цена: %.2f₸\nСредняя цена: %.2f₸\nОбщее количество товаров: %d\n",
                stats.getMin(), stats.getMax(), stats.getAverage(), sales.size()
        );
    }

    private static List<Sale> generateSales() {
        Random random = new Random();
        List<Sale> sales = new ArrayList<>();

        String[] categories = {"Электроника", "Одежда", "Продукты", "Книги"};
        String[][] products = {
                {"Ноутбук", "Смартфон", "Планшет", "Наушники", "Телевизор"},
                {"Футболка", "Джинсы", "Куртка", "Кроссовки", "Рубашка"},
                {"Хлеб", "Молоко", "Яйца", "Сыр", "Мясо"},
                {"Роман", "Учебник", "Комикс", "Энциклопедия", "Детектив"}
        };

        double[][] priceRanges = {
                {25000, 120000},
                {2000, 15000},
                {350, 5000},
                {1500, 8000}
        };

        for (int i = 0; i < 150; i++) {
            int cat = random.nextInt(categories.length);
            String category = categories[cat];
            String product = products[cat][random.nextInt(products[cat].length)];

            double price = priceRanges[cat][0] +
                    (priceRanges[cat][1] - priceRanges[cat][0]) * random.nextDouble();

            int quantity = random.nextInt(5) + 1;
            LocalDate date = LocalDate.now().minusDays(random.nextInt(30));

            sales.add(new Sale(product, category, price, quantity, date));
        }

        return sales;
    }
}
