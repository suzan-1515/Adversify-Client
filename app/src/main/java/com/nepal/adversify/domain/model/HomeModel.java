package com.nepal.adversify.domain.model;

import java.util.ArrayList;
import java.util.List;

public class HomeModel {

    private List<MenuModel> menuModels = new ArrayList<>();

    public List<MenuModel> getMenuModels() {
        return menuModels;
    }

    public void setMenuModels(List<MenuModel> menuModels) {
        this.menuModels = menuModels;
    }
}
