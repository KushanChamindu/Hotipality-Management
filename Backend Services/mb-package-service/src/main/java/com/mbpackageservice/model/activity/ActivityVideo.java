package com.mbpackageservice.model.activity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_video")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String videoURL;
}
