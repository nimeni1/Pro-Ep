using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AdministratorClient
{
    class StartAddress
    {
        [JsonProperty(PropertyName = "street", Required = Required.Always)]
        public string Street { get; set; }
        [JsonProperty(PropertyName = "number", Required = Required.Always)]
        public int Number { get; set; }
        [JsonProperty(PropertyName = "city", Required = Required.Always)]
        public string City { get; set; }
        [JsonProperty(PropertyName = "postal_code", Required = Required.Always)]
        public string PostalCode { get; set; }

        [JsonConstructor]
        public StartAddress(string street, int number, string postalCode, string city)
        {
            this.Street = street;
            this.Number = number;
            this.PostalCode = postalCode;
            this.City = city;
        }

        public override string ToString()
        {
            string address;
            address = Street + " " + Number + " " + PostalCode + " " + City;
            return address;
        }
    }
}
