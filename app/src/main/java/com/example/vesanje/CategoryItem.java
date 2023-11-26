package com.example.vesanje;

public class CategoryItem {
    public String categoryName;
    public int imageResourceId;
    public int itemCount;
    public int itemRecord;

    public CategoryItem(String categoryName, int imageResourceId, int itemCount, int itemRecord) {
        this.categoryName = categoryName;
        this.imageResourceId = imageResourceId;
        this.itemCount = itemCount;
        this.itemRecord = itemRecord;
    }

    public String getCategoryName(){
        return categoryName;
    }
    public int getImageResourceId() {
        return imageResourceId;
    }
    public int getItemCount() {
        return itemCount;
    }
    public int getItemRecord() {
        return itemRecord;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    public void setItemRecord(int itemRecord) {
        this.itemRecord = itemRecord;
    }


}