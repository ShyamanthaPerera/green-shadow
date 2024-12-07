package com.example.greenshadow.util;

import java.util.regex.Pattern;

public class Regex {

    public static boolean cropCodeMatcher(String cropCode) {
        String regexForCropCode = "^CROP-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForCropCode);
        return regexPattern.matcher(cropCode).matches();
    }

    public static boolean equipIdMatcher(String equipId) {
        String regexForEquipId = "^EQUIP-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForEquipId);
        return regexPattern.matcher(equipId).matches();
    }

    public static boolean fieldCodeMatcher(String fieldCode) {
        String regexForFieldCode = "^FIELD-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForFieldCode);
        return regexPattern.matcher(fieldCode).matches();
    }

    public static boolean logCodeMatcher(String logCode) {
        String regexForLogCode = "^LOG-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForLogCode);
        return regexPattern.matcher(logCode).matches();
    }

    public static boolean staffIdMatcher(String id) {
        String regexForStaffId = "^STAFF-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForStaffId);
        return regexPattern.matcher(id).matches();
    }

    public static boolean userIdMatcher(String userId) {
        String regexForUserId = "^USER-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForUserId);
        return regexPattern.matcher(userId).matches();
    }

    public static boolean vehicleCodeMatcher(String vehicleCode) {
        String regexForVehicleCode = "^VEHICLE-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForVehicleCode);
        return regexPattern.matcher(vehicleCode).matches();
    }

    public static boolean vehicleLicenseMatcher(String licenseNumber) {
        String regexForVehicleNumber = "^[xXyYzZ]-?\\d{4}$";
        Pattern regexPattern = Pattern.compile(regexForVehicleNumber);
        return regexPattern.matcher(licenseNumber).matches();
    }
}
