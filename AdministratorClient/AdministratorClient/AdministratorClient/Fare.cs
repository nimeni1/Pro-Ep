using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AdministratorClient
{
    [JsonObject(MemberSerialization.OptIn)]
    class Fare
    {
        [JsonProperty(PropertyName = "car_id", Required = Required.Always)]
        public int CarId { get; set; }
        [JsonProperty(PropertyName = "client_id", Required = Required.Always)]
        public int ClientId { get; set; }
        [JsonProperty(PropertyName = "distance_km", Required = Required.Always)]
        public double Distance { get; set; }
        [JsonProperty(PropertyName = "total_price", Required = Required.Always)]
        public double TotalPrice { get; set; }
        [JsonProperty(PropertyName = "start_address", Required = Required.Always)]
        public StartAddress StartAddress {get; set;}
        [JsonProperty(PropertyName = "destination_address", Required = Required.Always)]
        public DestinationAddress DestinationAddress { get; set; }
        [JsonProperty(PropertyName = "license_number", Required = Required.Always)]
        public string LicenseNumber { get; set; }
        [JsonProperty(PropertyName = "date", Required = Required.Always)]
        public DateTime Date { get; set; }

        [JsonConstructor]
        public Fare(int carId, string licenseNumber, int clientId, double distance, double totalPrice,
            StartAddress startAddress, DestinationAddress destinationAddress, DateTime date)
        {
            this.CarId = carId;
            this.LicenseNumber = licenseNumber;
            this.ClientId = clientId;
            this.Distance = distance;
            this.TotalPrice = totalPrice;
            this.StartAddress = startAddress;
            this.DestinationAddress = destinationAddress;
            this.Date = date;
        }
    }
}
