package com.softtechglobal.androidcarmanager.Database;

public class VehicleDB {
    String mvehicleName, modometerType, mmanufacturer, mvehicleModel;
    Double mmileageRange, mfuelLimit;
    String mplateNumber;
    Long mpurchaseDate;

    public VehicleDB() {
    }

    public VehicleDB(String vehicleName, String odometerType, String manufacturer, String vehicleModel, Double mileageRange,
                     Double fuelLimit, String plateNumber, Long purchaseDate) {
        this.mvehicleName = vehicleName;
        this.modometerType = odometerType;
        this.mmanufacturer = manufacturer;
        this.mvehicleModel = vehicleModel;
        this.mmileageRange = mileageRange;
        this.mfuelLimit = fuelLimit;
        this.mplateNumber = plateNumber;
        this.mpurchaseDate = purchaseDate;
    }

    public Long getPurchaseDate() {
        return mpurchaseDate;
    }

    public void setPurchaseDate(Long purchaseDate) {
        this.mpurchaseDate = purchaseDate;
    }

    public String getVehicleName() {
        return mvehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.mvehicleName = vehicleName;
    }

    public String getOdometerType() {
        return modometerType;
    }

    public void setOdometerType(String odometerType) {
        this.modometerType = odometerType;
    }

    public String getManufacturer() {
        return mmanufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.mmanufacturer = manufacturer;
    }

    public String getVehicleModel() {
        return mvehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.mvehicleModel = vehicleModel;
    }

    public Double getMileageRange() {
        return mmileageRange;
    }

    public void setMileageRange(Double mileageRange) {
        this.mmileageRange = mileageRange;
    }

    public Double getFuelLimit() {
        return mfuelLimit;
    }

    public void setFuelLimit(Double fuelLimit) {
        this.mfuelLimit = fuelLimit;
    }

    public String getPlateNumber() {
        return mplateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.mplateNumber = plateNumber;
    }
}
