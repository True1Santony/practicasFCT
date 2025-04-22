package com.practica1.model;

public class Concessionaire {

    private int id;
    private String name;

    public Concessionaire() {
    }
    public Concessionaire(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void displayInformation() {
        System.out.println("-------------------------------");
        System.out.println("Concessionaire Information: ");
        System.out.println("ID: " + getId());
        System.out.println("Name: " + getName());
        System.out.println("-------------------------------");
    }
}
