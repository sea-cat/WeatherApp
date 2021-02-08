# WeatherApp


<img src="https://github.com/sea-cat/WeatherApp/blob/main/screenshot.png" alt="alt text" height="600">



This application uses the **OpenWeatherMap API** to fetch the latest weather based on the device's current location. In order to test this, you will need to create a file called `apikey.properties` and place it in the `src` folder of the project. This file will contain your API key from the OpenWeatherMap as follows:

`OPEN_WEATHER_KEY="yourapikeyhere"`

## Components

The project uses an MVVM architecture with a Repository for clear separation of concerns. It uses **Retrofit** for its network calls, **Room** for data persistance, **DataBinding** for a responsive UI, **LiveData** for lifecycle-aware data updates, **RxJava** for a clean thread work and **Dagger Hilt** for dependency injection.
 
### Location
It will first ask for the **location permission** from the user, having several fallbacks in case the user doesn't understand what the permission is for. Even if the user permanently denies the permission, there will be a Snackbar present with a link to the settings where they can manually enable the permission. The application will not fetch data from the openWeatherMap API without a location. 

*In the future, a screen that prompts the user for his location could be introduced.*

### Network and Storage
After the device acquires the location, using Retrofit, a call will be made to the OpenWeatherMap API. If the call is successfull, the data is deserialised and displayed to the user, but it will also attach the current date and time to it, and saved in the DB.

If the network call was unsuccessfull because of a lack of data connectivity, a Snackbar with a relevant message will appear, otherwise a generic message will apear. After a failed network attempt, as a backup, a Room call is made to the DB to get the stored information and display it. There is a filter on the db call, only to return data that is less than 24 hours old. If there is data, but older than required, then that data is purged form the db.

A call to the database can be made either with a known location or with no location (in case the user turns off their location after data was acquired). Whenever storage data is displayed, there will be a message above it, stating the city, date and time of acquire.

<img src="https://github.com/sea-cat/WeatherApp/blob/main/screenshot_no_network.png" alt="alt text" height="600">

### First time with no connectivity
A message will appear in place of the normal data, to let the user know there's no data to display, together with a Snackbar telling them there's no connectivity.

### Opening the app with no connectivity
If there is no prior data, or the data is not relevant for the user's location or date, a message will appear in place of the normal data, to let the user know there's no data to display, together with a Snackbar telling them there's no connectivity. 
If there is prior relevant data, that data willbe loaded, together with a Snackbar telling them there's no connectivity, and a message to let them know when the data is from.


## Implementation Details
The chained network and storage calls were changed to never throw an error, but instead to wrap the result and/or the error inside an object that would give the ViewModel visibility of what messages it should display. 

## Can be improved upon
- custom location could be required from the user
- location permission logic could be in its own class
- the app could be changed to fetch data for multiple locations and display them in a RecyclerView
- a broadcast receiver could be added to listen for network changes






