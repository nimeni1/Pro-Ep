using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Net;
using System.IO;
using Newtonsoft.Json;

namespace AdministratorClient
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private static HttpClient client = new HttpClient();
        Car car;
        Driver driver;

        public MainWindow()
        {
            InitializeComponent();
            client.BaseAddress = new Uri("http://localhost:8090/");
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(
                new MediaTypeWithQualityHeaderValue("application/json"));
        }

        private void CreateDriverBtn_Click(object sender, RoutedEventArgs e)
        {
            CreateDriverAsync();
        }

        private void CreateCarBtn_Click(object sender, RoutedEventArgs e)
        {
            CreateCarAsync();
        }

        private void TabControl_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (this.UpdateDriverTab.IsSelected)
            {
                this.UpdateDriverBtn.IsEnabled = false;
            }
            if (this.UpdateCarTab.IsSelected)
            {
                this.UpdateCarBtn.IsEnabled = false;
            }
        }

        async void GetDriver(string driverId)
        {
            try
            {
                HttpResponseMessage response = await client.GetAsync("admin/rest/admin/getDriver/" + driverId);
                if (response.StatusCode != HttpStatusCode.OK)
                {
                    MessageBox.Show("No driver with this license number has been found!");
                }
                else
                {
                    driver = await response.Content.ReadAsAsync<Driver>();
                    this.UpdateDriverFirstName.Text = driver.Name.FirstName;
                    this.UpdateDriverLastName.Text = driver.Name.LastName;
                    this.UpdateDriverPassword.Text = driver.Password;
                    this.UpdateDriverPhone.Text = driver.Phone;
                    this.UpdateDriverEmail.Text = driver.Email;
                    this.UpdateDriverBtn.IsEnabled = true;
                }
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("Internal server error!","Error",MessageBoxButton.OK,MessageBoxImage.Error);
            }
            catch (ArgumentNullException)
            {
                MessageBox.Show("JSON Parse Error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        async void GetDriverOverview(string licenseId)
        {
            try
            {
                HttpResponseMessage response = await client.GetAsync("admin/rest/admin/payments/" + licenseId);
                if (response.StatusCode != HttpStatusCode.OK)
                {
                    MessageBox.Show("No driver with this license number has been found!");
                }
                else
                {
                    List<Fare> faresList = await response.Content.ReadAsAsync<List<Fare>>();
                    this.GenerateStats(faresList);
                    this.PopulateFaresGrid(faresList);
                    
                }
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("Internal server error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            catch (ArgumentNullException)
            {
                MessageBox.Show("JSON Parse Error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        void GenerateStats(List<Fare> faresList)
        {
            int nrOfFares = faresList.Count;
            double revenue = 0;
            double kmDriven = 0;
            for (int i = 0; i < faresList.Count; i++)
            {
                revenue += faresList[i].TotalPrice;
                kmDriven = faresList[i].Distance;
            }
            this.DisplayStats(revenue, kmDriven, nrOfFares);
        }

        void DisplayStats(double revenue, double kmDriven, int nrOfFares)
        {
            this.KmDrivenLabel.Content = "Km driven - " + kmDriven.ToString();
            this.NrOfFaresLabel.Content = "Nr of fares - " + nrOfFares.ToString();
            this.TotalRevenueLabel.Content = "Total Revenue - " + revenue.ToString();
        }

        async void GetCar(string carId)
        {
            try
            {
                HttpResponseMessage response = await client.GetAsync("admin/rest/admin/getCar/" + carId);
                if (response.StatusCode != HttpStatusCode.OK)
                {
                    MessageBox.Show("No car with this license plate has been found!");
                }
                else
                {
                    car = await response.Content.ReadAsAsync<Car>();
                    this.UpdateCarLicenseIdTextBox.Text = car.DriverLicense;
                    this.UpdateCarModelTextBox.Text = car.CarModel;
                    this.UpdateCarNrOfSeatsTextBox.Text = car.NrOfSeats.ToString();
                    this.UpdateCarPricePerKmTextBox.Text = car.PricePerKm.ToString();
                    this.UpdateCarTrunkSizeTextBox.Text = car.TrunkSize.ToString();
                    this.UpdateCarTypeTextBox.Text = car.CarType;
                    this.UpdateCarBtn.IsEnabled = true;
                }
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("Internal server error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            catch (ArgumentNullException)
            {
                MessageBox.Show("JSON Parse Error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        async void CreateDriverAsync()
        {
            try
            {
                Name name = new Name(DriverFirstName.Text, DriverLastName.Text);
                string driverEmail = DriverEmail.Text;
                string driverPassword = DriverPassword.Text;
                string driverPhone = DriverPhone.Text;
                string driverLicense = DriverLicense.Text;
                Driver driver = new Driver(name, driverLicense, driverEmail, driverPassword, driverPhone, true);
                HttpResponseMessage response = await client.PostAsJsonAsync("/admin/rest/admin/createDriver", driver);
                response.EnsureSuccessStatusCode();
                Console.WriteLine(response.StatusCode);
            }
            catch (FormatException)
            {
                MessageBox.Show("Please enter the values in the correct format", "Invalid Input", MessageBoxButton.OK, MessageBoxImage.Warning);
            } finally { MessageBox.Show("Driver created and added to the database!"); }
        }

        async void CreateCarAsync()
        {
            try
            {
                string driverId = DriverId.Text;
                string carLicensePlate = CarLicensePlate.Text;
                double carPricePerKm = Convert.ToDouble(CarPricePerKm.Text);
                string carType = CarType.Text;
                string carModel = CarModel.Text;
                string carTrunkSize = CarTrunkSize.Text;
                string carNrOfSeats = CarNrOfSeats.Text;
                car = new Car(driverId, carLicensePlate, carPricePerKm, carType, carModel, carNrOfSeats, carTrunkSize);
                HttpResponseMessage response = await client.PostAsJsonAsync("/admin/rest/admin/createCar", car);
                response.EnsureSuccessStatusCode();
                Console.WriteLine(response.StatusCode);
                MessageBox.Show("Car created and added to the database!");
            }
            catch (FormatException)
            {
                MessageBox.Show("Please enter the values in the correct format", "Invalid Input", MessageBoxButton.OK,MessageBoxImage.Warning);
            }
        }

        private void UpdateDriverBtn_Click(object sender, RoutedEventArgs e)
        {
            UpdateDriver();
        }

        async void UpdateDriver()
        {
            try { 
                driver.Name.FirstName = this.UpdateDriverFirstName.Text;
                driver.Name.LastName = this.UpdateDriverLastName.Text;
                driver.Password = this.UpdateDriverPassword.Text;
                driver.Phone = this.UpdateDriverPhone.Text;
                driver.Email = this.UpdateDriverEmail.Text;
                HttpResponseMessage response = await client.PutAsJsonAsync("admin/rest/admin/updateDriver", driver);
                if (response.StatusCode == HttpStatusCode.Accepted)
                {
                    MessageBox.Show("The driver has been updated!");
                }
                else
                {
                    MessageBox.Show("Server failure");
                }
            }
            catch (FormatException)
            {
                MessageBox.Show("Please enter the values in the correct format", "Invalid Input", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
        }

        private void RemoveDriverBtn_Click(object sender, RoutedEventArgs e)
        {
            RemoveDriver(this.RemoveDriverTextBox.Text);
        }

        async void RemoveDriver(string driverId)
        {
            try {
                HttpResponseMessage response = await client.DeleteAsync("admin/rest/admin/deletion/driver/" + driverId);
                if (response.StatusCode == HttpStatusCode.OK)
                {
                    MessageBox.Show("The driver has been removed from the database!");
                }
                else
                {
                    MessageBox.Show("Driver has not been found in the database!");
                }
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("Internal server error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            catch (ArgumentNullException)
            {
                MessageBox.Show("JSON Parse Error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            catch (InvalidOperationException)
            {
                MessageBox.Show("Internal server error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private void UpdateCarBtn_Click(object sender, RoutedEventArgs e)
        {
            UpdateCar();
        }

        async void UpdateCar()
        {
            try
            {
                car.DriverLicense = this.UpdateCarLicenseIdTextBox.Text;
                car.CarModel = this.UpdateCarModelTextBox.Text;
                car.NrOfSeats = this.UpdateCarNrOfSeatsTextBox.Text;
                car.PricePerKm = Convert.ToDouble(this.UpdateCarPricePerKmTextBox.Text);
                car.TrunkSize = this.UpdateCarTrunkSizeTextBox.Text;
                car.CarType = this.UpdateCarTypeTextBox.Text;
                HttpResponseMessage response = await client.PutAsJsonAsync("admin/rest/admin/updateCar", car);
                if (response.StatusCode == HttpStatusCode.Accepted)
                {
                    MessageBox.Show("The car details have been updated!");
                }
                else
                {
                    MessageBox.Show("Server failure");
                }
            }
            catch (FormatException)
            {
                MessageBox.Show("Please enter the values in the correct format", "Invalid Input", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
        }

        private void RemoveCarBtn_Click(object sender, RoutedEventArgs e)
        {
            RemoveCar(this.RemoveCarTextBox.Text);
        }

        async void RemoveCar(string licensePlate)
        {
            try
            {
                HttpResponseMessage response = await client.DeleteAsync("admin/rest/admin/deletion/car/" + licensePlate);
                if (response.StatusCode == HttpStatusCode.OK)
                {
                    MessageBox.Show("The car has been removed from the database!");
                }
                else
                {
                    MessageBox.Show("No car with this licence plate!");
                }
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("Internal server error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            catch (ArgumentNullException)
            {
                MessageBox.Show("JSON Parse Error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            catch (InvalidOperationException)
            {
                MessageBox.Show("Internal server error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private void SearchCarBtn_Click(object sender, RoutedEventArgs e)
        {
            GetCar(this.UpdateCarTextBox.Text);
        }

        private void SearchDriverBtn_Click(object sender, RoutedEventArgs e)
        {
            GetDriver(this.UpdateDriverTextBox.Text);
        }

        private void OverviewSearch_Click(object sender, RoutedEventArgs e)
        {
            GetDriverOverview(this.DriverOverviewTextBox.Text);
        }

        //async void GetPriceBasedFareSearch(double pricePerKm)
        //{
        //    try {
        //        HttpResponseMessage response = await client.GetAsync("admin/rest/admin/payments/price_km/" + pricePerKm);
        //        if (response.StatusCode != HttpStatusCode.OK)
        //        {
        //            MessageBox.Show("Server error");
        //        }
        //        else
        //        {
        //            List<Fare> faresList = await response.Content.ReadAsAsync<List<Fare>>();
        //            this.PopulateFaresGrid(faresList);
        //        }
        //    }
        //    catch (HttpRequestException)
        //    {
        //        MessageBox.Show("Internal server error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
        //    }
        //    catch (ArgumentNullException)
        //    {
        //        MessageBox.Show("JSON Parse Error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
        //    }
        //}

        void PopulateFaresGrid(List<Fare> faresList)
        {
            this.FaresListGrid.Children.Clear();
            RowDefinition headerdef = new RowDefinition
            {
                Height = GridLength.Auto
            };
            this.FaresListGrid.RowDefinitions.Add(headerdef);
            Label fLabel = new Label();
            Label pLabel = new Label();
            fLabel.Content = "Fare";
            pLabel.Content = "Payment";
            Grid.SetRow(fLabel, 0);
            Grid.SetColumn(fLabel, 0);
            Grid.SetRow(pLabel, 0);
            Grid.SetColumn(pLabel, 1);
            this.FaresListGrid.Children.Add(fLabel);
            this.FaresListGrid.Children.Add(pLabel);

            for (int i = 0; i < faresList.Count; i++)
            {
                RowDefinition rowdef = new RowDefinition
                {
                    Height = GridLength.Auto
                };
                this.FaresListGrid.RowDefinitions.Add(rowdef);

                string fare, payment;
                fare = "From " + faresList[i].StartAddress.ToString() + " to " + faresList[i].DestinationAddress.ToString();
                payment = faresList[i].TotalPrice.ToString();

                Label fareLabel = new Label();
                Label paymentLabel = new Label();
                fareLabel.Content = fare;
                paymentLabel.Content = payment;
                Grid.SetRow(fareLabel, i + 1);
                Grid.SetColumn(fareLabel, 0);
                Grid.SetRow(paymentLabel, i + 1);
                Grid.SetColumn(paymentLabel, 1);
                this.FaresListGrid.Children.Add(fareLabel);
                this.FaresListGrid.Children.Add(paymentLabel);
            }
            MessageBox.Show("Data retrieved successfully!");
        }

        //private void PriceBasedFareSearch_Click(object sender, RoutedEventArgs e)
        //{
        //    GetPriceBasedFareSearch(Convert.ToDouble(0.0));

        //}

        async void GetDateBasedFareSearch(DateTime date)
        {
            try
            {
                string dateString = date.ToString();
                HttpResponseMessage response = await client.GetAsync("admin/rest/admin/payments/date/" + dateString);
                if (response.StatusCode != HttpStatusCode.OK)
                {
                    MessageBox.Show("Server error");
                }
                else
                {
                    List<Fare> faresList = await response.Content.ReadAsAsync<List<Fare>>();
                    this.GenerateStats(faresList);
                    this.PopulateFaresGrid(faresList);
                }
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("Internal server error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            catch (ArgumentNullException)
            {
                MessageBox.Show("JSON Parse Error!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private void DateBasedFareSearch_Click(object sender, RoutedEventArgs e)
        {
            if (this.CarDate.Text.Contains("."))
            { 
                string[] array = this.CarDate.Text.Split('.');
                if (array[0] != null && array[1] != null && array[2] != null)
                {
                    DateTime date = new DateTime(array[0], array[1], array[2]);
                    GetDateBasedFareSearch(date);
                }
            }
        }
    }
}
