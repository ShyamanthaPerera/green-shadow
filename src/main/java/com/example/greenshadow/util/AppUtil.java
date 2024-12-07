package com.example.greenshadow.util;

import java.util.Base64;
import java.util.UUID;

public class AppUtil {

    public static String generateCropId(){
        return "CROP-" + UUID.randomUUID();
    }

    public static String generateEquipmentId(){

        return "EQUIP-" + UUID.randomUUID();
    }

    public static String generateFieldId(){
        return "FIELD-" + UUID.randomUUID();
    }

    public static String generateLogId(){
        return "LOG-" + UUID.randomUUID();
    }

    public static String generateStaffId(){
        return "STAFF-" + UUID.randomUUID();
    }

    public static String generateUserId(){
        return "USER-" + UUID.randomUUID();
    }

    public static String generateVehicleId(){
        return "VEHICLE-" + UUID.randomUUID();
    }

    public static String cropImageToBase64(byte [] cropImage){
        return Base64.getEncoder().encodeToString(cropImage);
    }

    public static String fieldImageOneToBase64(byte [] fieldImage1){
        return Base64.getEncoder().encodeToString(fieldImage1);
    }

    public static String fieldImageTwoToBase64(byte [] fieldImage2){
        return Base64.getEncoder().encodeToString(fieldImage2);
    }

    public static String observedImageOneToBase64(byte [] observedImage){
        return Base64.getEncoder().encodeToString(observedImage);
    }
}
