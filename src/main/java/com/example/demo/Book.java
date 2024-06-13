package com.example.demo;

public class Book implements Comparable<Book> {
    private String name;
    private double price;
    private String location;
    private int quantity;
    private boolean reserved;
    private String userName;
    public Book(String name, double price, String location, int quantity) {
        this.name = name;
        this.price = price;
        this.location = location;
        this.quantity = quantity;
        this.reserved = false;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public boolean isReserved() {
        return reserved;
    }

    public Book() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUserName() {
        return userName;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String toString() {
        String reservedStatus = reserved ? "(Reserved)" : "(Not reserved)";
        String user = (userName != null && !userName.isEmpty()) ? " [User: " + userName + "]" : "";

        return name + " - $" + price + " - " + location + " - " + quantity + " " + reservedStatus + user;
    }


    @Override
    public int compareTo(Book o) {
        Integer a = getQuantity();
        Integer b = o.getQuantity();

        return a.compareTo(b);
    }
}
