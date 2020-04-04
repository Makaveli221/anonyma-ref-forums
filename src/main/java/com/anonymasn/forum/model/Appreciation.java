package com.anonymasn.forum.model;

import java.util.Objects;

public class Appreciation {

    private int numero;
    private User user;
    private boolean liked;

    public Appreciation() {
    }

    public Appreciation(int numero, User user, boolean liked) {
        this.numero = numero;
        this.user = user;
        this.liked = liked;
    }

    public int hashCode() {
        return Objects.hash(numero, user.getFirstName()+ user.getLastName() + user.getEmail(), 1000);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Appreciation) {
            Appreciation pp = (Appreciation) obj;
            return (pp.numero == this.numero);
        } else {
            return false;
        }
    }

    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLiked() {
        return this.liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}