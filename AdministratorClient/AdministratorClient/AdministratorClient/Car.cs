using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AdministratorClient
{
    [JsonObject(MemberSerialization.OptIn)]
    public class Car
    {
        [JsonProperty(PropertyName = "driver_license", Required = Required.Always)]
        public string DriverLicense { get; set; }

        [JsonProperty(PropertyName = "license_plate", Required = Required.Always)]
        public string LicensePlate { get; set; }

        [JsonProperty(PropertyName = "price_km", Required = Required.Always)]
        public double PricePerKm { get; set; }

        [JsonProperty(PropertyName = "car_type", Required = Required.Always)]
        public string CarType { get; set; }

        [JsonProperty(PropertyName = "car_model", Required = Required.Always)]
        public string CarModel { get; set; }

        [JsonProperty(PropertyName = "seats_number", Required = Required.Always)]
        public string NrOfSeats { get; set; }

        [JsonProperty(PropertyName = "trunk_size", Required = Required.Always)]
        public string TrunkSize { get; set; }

        [JsonProperty(PropertyName = "available", Required = Required.Always)]
        public bool Available { get; set; }

        [JsonConstructor]
        public Car(string driver_license, string license_plate, double price_km, string car_type, string car_model, string nr_of_seats, string trunk_size, bool available)
        {
            DriverLicense = driver_license;
            LicensePlate = license_plate;
            PricePerKm = price_km;
            CarType = car_type;
            CarModel = car_model;
            NrOfSeats = nr_of_seats;
            TrunkSize = trunk_size;
            Available = available;
        }

        public Car(string driver_license, string license_plate, double price_km, string car_type, string car_model, string nr_of_seats, string trunk_size)
        {
            DriverLicense = driver_license;
            LicensePlate = license_plate;
            PricePerKm = price_km;
            CarType = car_type;
            CarModel = car_model;
            NrOfSeats = nr_of_seats;
            TrunkSize = trunk_size;
            Available = true;
        }

        public Car() { }
    }
}
