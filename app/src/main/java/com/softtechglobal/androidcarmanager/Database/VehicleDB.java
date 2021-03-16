package com.softtechglobal.androidcarmanager.Database;

public class VehicleDB {
    String mvehicleName, modometerReading, mmanufacturer, mvehicleModel;
    Double mmileageRange, mfuelLimit;
    String mplateNumber;
    Long mpurchaseDate;

    public VehicleDB() {
    }

    public VehicleDB(String mvehicleName, String modometerReading, String mmanufacturer, String mvehicleModel,
                     Double mmileageRange, Double mfuelLimit, String mplateNumber, Long mpurchaseDate) {
        this.mvehicleName = mvehicleName;
        this.modometerReading = modometerReading;
        this.mmanufacturer = mmanufacturer;
        this.mvehicleModel = mvehicleModel;
        this.mmileageRange = mmileageRange;
        this.mfuelLimit = mfuelLimit;
        this.mplateNumber = mplateNumber;
        this.mpurchaseDate = mpurchaseDate;
    }

    public String getModometerReading() {
        return modometerReading;
    }

    public void setModometerReading(String modometerReading) {
        this.modometerReading = modometerReading;
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
