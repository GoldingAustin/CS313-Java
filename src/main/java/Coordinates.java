/**
 * Created by austingolding on 3/15/17.
 */
public class Coordinates {
        public double latitude;
        public double longitude;

        public Coordinates(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

    public String getLatitude() {
        return String.valueOf(latitude);
    }

    public String getLongitude() {
        return String.valueOf(longitude);
    }
}
