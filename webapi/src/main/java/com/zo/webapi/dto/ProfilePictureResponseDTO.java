package com.zo.webapi.dto;

public class ProfilePictureResponseDTO {

    private Long userId;
    private String profilePicture;

    public ProfilePictureResponseDTO() {}

    public ProfilePictureResponseDTO(Long userId, String profilePicture){
        this.userId = userId;
        this.profilePicture = profilePicture;
    }

    public Long getUserId(){
        return userId;
    }

    public String getProfilePicture(){
        return profilePicture;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public void setProfilePicture(String profilePicture){
        this.profilePicture = profilePicture;
    }
}
