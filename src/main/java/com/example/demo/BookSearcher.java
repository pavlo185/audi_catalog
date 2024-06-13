package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.example.demo.BookCatalog.*;
import static com.example.demo.Sorter.bubbleSort;

public class BookSearcher implements Searcher{
    @Override
    public void searchByPrice(){
        String searchText = searchField.getText();
        //bubble сортировка 3 алгоритм
        bubbleSort(books, Comparator.comparing(Book::getPrice));
        Optional<ObservableList<Book>> filteredBooks = binarySearch(
                books,
                0,
                books.size() - 1,
                (book -> {
                    Double price = (book).getPrice();
                    return price.compareTo(Double.valueOf(searchText));
                })
        );
        bookList.setItems(filteredBooks.orElse(FXCollections.observableArrayList()));
    }

    @Override
    public void searchByName() {
        String searchText = searchField.getText();
        bookList.setItems(linerSearch(searchText));
    }
    @Override
    public void searchByQuantity(){
        String searchText = searchField.getText();
        bubbleSort(books, Comparator.comparing(Book::getQuantity));
        Optional<ObservableList<Book>> filteredBooks = binarySearch(
                books,
                0,
                books.size() - 1,
                (book -> {
                    Integer price = (book).getQuantity();
                    return price.compareTo(Integer.valueOf(searchText));
                })
        );
        bookList.setItems(filteredBooks.orElse(FXCollections.observableArrayList()));
    }
    private static <T> Optional<ObservableList<Book>> binarySearch(List<T> list, int low, int high, Function<T, Integer> function) {
        if (low <= high) {
            int mid = low + (high - low) / 2;
            T midValue = list.get(mid);

            int comparisonResult = function.apply(midValue);

            if (Math.abs(comparisonResult) == 0) { // Используйте допустимую погрешность
                // Найден элемент, создаем список и добавляем его в результат
                ObservableList<Book> result = FXCollections.observableArrayList();
                result.add((Book) midValue);

                // Ищем дополнительные совпадающие элементы слева
                int left = mid - 1;
                while (left >= 0 && Math.abs(function.apply(list.get(left))) == 0) {
                    result.add((Book) (list.get(left)));
                    left--;
                }

                // Ищем дополнительные совпадающие элементы справа
                int right = mid + 1;
                while (right < list.size() && Math.abs(function.apply(list.get(right))) == 0) {
                    result.add((Book) list.get(right));
                    right++;
                }

                return Optional.of(result);
            } else if (comparisonResult < 0) {
                return binarySearch(list, mid + 1, high, function);
            } else {
                return binarySearch(list, low, mid - 1, function);
            }
        }

        return Optional.empty(); // Элемент не найден
    }

    private static ObservableList<Book> linerSearch( String searchText) {
        ObservableList<Book> result = FXCollections.observableArrayList();
        for (Book book : BookCatalog.books) {
            String bookName = book.getName().toLowerCase();
            if (bookName.contains(searchText.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }
}
