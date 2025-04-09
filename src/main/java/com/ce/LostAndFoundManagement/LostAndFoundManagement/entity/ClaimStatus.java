package com.ce.LostAndFoundManagement.LostAndFoundManagement.entity;

public enum ClaimStatus {
    PENDING,
    APPROVED,
    REJECTED;

    public static ClaimStatus fromString(String status) {
        for (ClaimStatus claimStatus : ClaimStatus.values()) {
            if (claimStatus.name().equalsIgnoreCase(status)) {
                return claimStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
    