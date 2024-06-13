package com.example.demo;

import javafx.collections.ObservableList;

import java.util.Comparator;

import static com.example.demo.BookCatalog.*;

public class Sorter {
    public static void sortByName(){
        bubbleSort(books, Comparator.comparing(Book::getName));
        bookList.getColumns().forEach(column -> column.getStyleClass().removeAll("sorted-column"));
        bookList.getColumns().get(0).getStyleClass().add("sorted-column");
        displayBooks();
    }
    public static void sortByPrice(){
        bubbleSort(books, Comparator.comparing(Book::getPrice));
        bookList.getColumns().forEach(column -> column.getStyleClass().removeAll("sorted-column"));
        bookList.getColumns().get(1).getStyleClass().add("sorted-column");
        displayBooks();
    }
    public static void sortByQuantity(){
        bubbleSort(books, Comparator.comparing(Book::getQuantity));
        bookList.getColumns().forEach(column -> column.getStyleClass().removeAll("sorted-column"));
        bookList.getColumns().get(3).getStyleClass().add("sorted-column");
        displayBooks();
    }
    public static <T extends Comparable<T>> void bubbleSort(ObservableList<T> list, Comparator<T> comparator) {
        int n = list.size();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(list.get(j),list.get(j + 1)) > 0 ) {
                    // Меняем элементы местами
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }

            // Если не было обменов во внутреннем цикле, то список уже отсортирован
            if (!swapped) {
                break;
            }
        }
    }
}
