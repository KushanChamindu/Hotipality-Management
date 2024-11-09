package com.megabliss.mbpackageservice.model.activitypackage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_package_video")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPackageVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String videoUrl;
}
