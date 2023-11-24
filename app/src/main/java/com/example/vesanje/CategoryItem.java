package com.example.vesanje;

public class CategoryItem {
    public String categoryName;
    public int imageResourceId;
    public int itemCount;

    public CategoryItem(String categoryName, int imageResourceId, int itemCount) {
        this.categoryName = categoryName;
        this.imageResourceId = imageResourceId;
        this.itemCount = itemCount;
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

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }


}