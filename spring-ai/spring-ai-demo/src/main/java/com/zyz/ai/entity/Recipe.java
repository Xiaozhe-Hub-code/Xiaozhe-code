package com.zyz.ai.entity;

import lombok.Data;

import java.util.List;

@Data
public class Recipe {
    private String name;
    private List<String> materials;
}
