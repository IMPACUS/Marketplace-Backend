package com.impacus.maketplace.dto.oauth.kakao.userProfile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

@Getter
public class KakaoAccountResponse {
    @JsonProperty("profile_needs_agreement")
    private Boolean profileNeedsAgreement;

    @JsonProperty("profile_nickname_needs_agreement")
    private Boolean profileNicknameNeedsAgreement;

    @JsonProperty("profile_image_needs_agreement")
    private Boolean profileImageNeedsAgreement;

    private LinkedHashMap profile;

    @JsonProperty("name_needs_agreement")
    private Boolean nameNeedsAgreement;

    private String name;

    @JsonProperty("email_needs_agreement")
    private Boolean emailNeedsAgreement;

    @JsonProperty("is_email_valid")
    private Boolean isEmailValid;

    @JsonProperty("is_email_verified")
    private Boolean isEmailVerified;

    private String email;

    @JsonProperty("age_range_needs_agreement")
    private Boolean ageRangeNeedsAgreement;

    @JsonProperty("age_range")
    private String ageRange;

    @JsonProperty("birthyear_needs_agreement")
    private Boolean birthyearNeedsAgreement;

    private String birthyear;

    @JsonProperty("birthday_needs_agreement")
    private Boolean birthdayNeedsAgreement;

    private String birthday;

    @JsonProperty("birthday_type")
    private String birthdayType;

    @JsonProperty("gender_needs_agreement")
    private Boolean genderNeedsAgreement;

    private String gender;

    @JsonProperty("phone_number_needs_agreement")
    private Boolean phoneNumberNeedsAgreement;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("ci_needs_agreement")
    private Boolean ciNeedsAgreement;

    private String ci;

    @JsonProperty("ci_authenticated_at")
    private LocalDateTime ciAuthenticatedAt;
}
