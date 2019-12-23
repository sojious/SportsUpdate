package com.example.sportsupdate;

public class NewsEntry {

    private final String itemUrl;
    private String itemtitle;
    private String itemSection;
    private String itemPublishedDate;
    public NewsEntry(String itemtitle, String itemSection, String itemPublishedDate,String itemUrl) {
        this.itemtitle = itemtitle;
        this.itemSection = itemSection;
        this.itemPublishedDate = itemPublishedDate;
        this.itemUrl=itemUrl;
    }

    public void setItemtitle(String itemtitle) {
        this.itemtitle = itemtitle;
    }

    public void setItemSection(String itemSection) {
        this.itemSection = itemSection;
    }

    public void setItemPublishedDate(String itemPublishedDate) {
        this.itemPublishedDate = itemPublishedDate;
    }

    public String getItemtitle() {
        return itemtitle;
    }

    public String getItemSection() {
        return itemSection;
    }

    public String getItemPublishedDate() {
        return itemPublishedDate;
    }
    public String getItemUrl() {
        return itemUrl;
    }
}
