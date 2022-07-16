package com.example.recipe_project.models.dto.categories_dto;

import com.example.recipe_project.models.entity.categories.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityType_DTO {
    private int id;
    private String name;
    private String about;

    public ActivityType_DTO(ActivityType activityType) {
        this.id = activityType.getId();
        this.name = activityType.getName();
        this.about = activityType.getAbout();
    }
}
