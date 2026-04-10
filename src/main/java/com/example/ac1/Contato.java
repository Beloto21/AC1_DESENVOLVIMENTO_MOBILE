package com.example.ac1;

public class Contato {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String category;
    private String city;
    private boolean favorite;

    public Contato() {}

    public Contato(int id, String name, String phone, String email, String category, String city, boolean favorite) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.category = category;
        this.city = city;
        this.favorite = favorite;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    @Override
    public String toString() {
        return name + " (" + category + ")";
    }
}
