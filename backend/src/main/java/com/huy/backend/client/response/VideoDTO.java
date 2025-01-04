package com.huy.backend.client.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VideoDTO {
    private String id;
    private String key;
    private String name;
    private String site;
    private String type;
    private Boolean official;
    private String published_at;
}