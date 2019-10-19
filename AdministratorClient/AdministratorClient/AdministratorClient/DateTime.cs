using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AdministratorClient
{
    [JsonObject(MemberSerialization.OptIn)]
    class DateTime
    {
        [JsonProperty(PropertyName = "day", Required = Required.Always)]
        public string Day { get; set; }
        [JsonProperty(PropertyName = "month", Required = Required.Always)]
        public string Month { get; set; }
        [JsonProperty(PropertyName = "year", Required = Required.Always)]
        public string Year { get; set; }

        [JsonConstructor]
        public DateTime(string day, string month, string year)
        {
            this.Day = day;
            this.Month = month;
            this.Year = year;
        }

        public override string ToString()
        {
            string date;
            date = this.Day + '.' + this.Month + '.' + this.Year;
            return date;
        }
    }
}
