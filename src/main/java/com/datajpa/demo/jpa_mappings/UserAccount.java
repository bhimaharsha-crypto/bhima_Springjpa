package com.datajpa.demo.jpa_mappings;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
//@Table(name = "hello")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;

    public UserAccount() {
    }

    public UserAccount(String email, Integer id, String password, Address address) {
        this.email = email;
        this.id = id;
        this.password = password;
        this.address = address;
    }

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;  // Has a relationship or Has - A dependency

    @OneToMany(cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();


    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Integer getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
