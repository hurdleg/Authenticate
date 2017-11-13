package mad9132.doo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO class for a Building JSON object.
 *
 * Before You Begin - install the Parceable plugin for Android Studio (1)
 *
 * Copy 'n paste the JSON structure for a Building: https://doors-open-ottawa.mybluemix.net/buildings/2
 *
 * Next, use this online tool to generate this POJO class: http://www.jsonschema2pojo.org/
 *
 * Finally, implement the Parceable interface (click 'PlanetPOJO' > Code > Generate... > Parceable
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 *
 * Reference
 * 1) "Model response data with POJO classes", Chapter 3. Requesting Data over the Web, Android
 *     App Development: RESTful Web Services by David Gassner
 */
public class BuildingPOJO implements Parcelable {

    private String nameEN;

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nameEN);
    }

    public BuildingPOJO() {
    }

    protected BuildingPOJO(Parcel in) {
        this.nameEN = in.readString();
    }

    public static final Creator<BuildingPOJO> CREATOR = new Creator<BuildingPOJO>() {
        @Override
        public BuildingPOJO createFromParcel(Parcel source) {
            return new BuildingPOJO(source);
        }

        @Override
        public BuildingPOJO[] newArray(int size) {
            return new BuildingPOJO[size];
        }
    };
}