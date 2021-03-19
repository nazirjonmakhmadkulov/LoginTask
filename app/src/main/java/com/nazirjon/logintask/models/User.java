package com.nazirjon.logintask.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "user")
public class User {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private String _id;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "isEmailConfirmed")
    private Boolean isEmailConfirmed;
    @ColumnInfo(name = "createdAt")
    private String createdAt;
    @ColumnInfo(name = "profiles")
    private Integer profiles;
//    @Ignore
//    private Subscription subscription;
//    @Ignore
//    private Payment_ payment;
//    @Ignore
//    private Plan plan;
    @ColumnInfo(name = "targetPageShown")
    private Boolean targetPageShown;
    @ColumnInfo(name = "googleClientId")
    private String googleClientId;
    @ColumnInfo(name = "hasSuccessPayment")
    private Boolean hasSuccessPayment;
    @ColumnInfo(name = "paypalSubsCount")
    private Integer paypalSubsCount;
    @ColumnInfo(name = "paypalSubscriptionId")
    private String paypalSubscriptionId;
    @ColumnInfo(name = "planExpireDate")
    private String planExpireDate;

    public User(@NonNull String _id, String email, Boolean isEmailConfirmed, String createdAt,
                Integer profiles, Boolean targetPageShown, String googleClientId, Boolean hasSuccessPayment,
                Integer paypalSubsCount, String paypalSubscriptionId, String planExpireDate) {
        this._id = _id;
        this.email = email;
        this.isEmailConfirmed = isEmailConfirmed;
        this.createdAt = createdAt;
        this.profiles = profiles;
        this.targetPageShown = targetPageShown;
        this.googleClientId = googleClientId;
        this.hasSuccessPayment = hasSuccessPayment;
        this.paypalSubsCount = paypalSubsCount;
        this.paypalSubscriptionId = paypalSubscriptionId;
        this.planExpireDate = planExpireDate;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsEmailConfirmed() {
        return isEmailConfirmed;
    }

    public void setIsEmailConfirmed(Boolean isEmailConfirmed) {
        this.isEmailConfirmed = isEmailConfirmed;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getProfiles() {
        return profiles;
    }

    public void setProfiles(Integer profiles) {
        this.profiles = profiles;
    }

    public Boolean getTargetPageShown() {
        return targetPageShown;
    }

    public void setTargetPageShown(Boolean targetPageShown) {
        this.targetPageShown = targetPageShown;
    }

    public String getGoogleClientId() {
        return googleClientId;
    }

    public void setGoogleClientId(String googleClientId) {
        this.googleClientId = googleClientId;
    }

    public Boolean getHasSuccessPayment() {
        return hasSuccessPayment;
    }

    public void setHasSuccessPayment(Boolean hasSuccessPayment) {
        this.hasSuccessPayment = hasSuccessPayment;
    }

    public Integer getPaypalSubsCount() {
        return paypalSubsCount;
    }

    public void setPaypalSubsCount(Integer paypalSubsCount) {
        this.paypalSubsCount = paypalSubsCount;
    }

    public String getPaypalSubscriptionId() {
        return paypalSubscriptionId;
    }

    public void setPaypalSubscriptionId(String paypalSubscriptionId) {
        this.paypalSubscriptionId = paypalSubscriptionId;
    }

    public String getPlanExpireDate() {
        return planExpireDate;
    }

    public void setPlanExpireDate(String planExpireDate) {
        this.planExpireDate = planExpireDate;
    }

}