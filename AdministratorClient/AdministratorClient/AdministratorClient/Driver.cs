using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AdministratorClient
{
    [JsonObject(MemberSerialization.OptIn)]
    class Driver
    {
        [JsonIgnore]
        public string Id { get; private set; }

        [JsonProperty(PropertyName = "name", Required = Required.Always)]
        public Name Name { get; set; }

        [JsonProperty(PropertyName = "license_number", Required = Required.Always)]
        public string LicenseNumber { get; set; }

        [JsonProperty(PropertyName = "email", Required = Required.Always)]
        public string Email { get; set; }

        [JsonProperty(PropertyName = "password", Required = Required.Always)]
        public string Password { get; set; }

        [JsonProperty(PropertyName = "phone_number", Required = Required.Always)]
        public string Phone { get; set; }

        [JsonProperty(PropertyName = "average_rating", Required = Required.Always)]
        public double AverageRating { get; set; }

        [JsonProperty(PropertyName = "active", Required = Required.Always)]
        public bool Active { get; set; }
        

        [JsonIgnore]
        public double Payslip { get; set; }

        public Driver(Name name, string license_number, string email, string password, string phone, bool active)
        {
            Name = name;
            LicenseNumber = license_number;
            Email = email;
            Password = password;
            Phone = phone;
            Active = active;
            AverageRating = 5;
        }
        
        [JsonConstructor]
        private Driver(string id, Name name, string license_number, string email, string password, string phone, bool active, double average_rating, double payslip)
        {
            Id = id;
            Name = name;
            LicenseNumber = license_number;
            Email = email;
            Password = password;
            Phone = phone;
            Active = active;
            AverageRating = 5;
            Payslip = payslip;
        }

        public Driver() { }

    }
}
