package com.mybook.bookstore.dto;

import java.util.List;

public class CheckoutRequestDto {
    private List<String> isbnlist;
    private String promocode;

    public List<String> getIsbnlist() {
        return isbnlist;
    }

    public void setIsbnlist(List<String> isbnlist) {
        this.isbnlist = isbnlist;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }
}
