package ru.okmarket.okgoods.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.util.AppLog;

public class MainDatabase extends SQLiteOpenHelper
{
    private static final String TAG = "MainDatabase";



    private static final String DB_NAME    = "main.db";
    private static final int    DB_VERSION = 1;



    public static final String COLUMN_ID                  = "_id";
    public static final String COLUMN_NAME                = "_name";
    public static final String COLUMN_CITY_ID             = "_city_id";
    public static final String COLUMN_LATITUDE            = "_latitude";
    public static final String COLUMN_LONGITUDE           = "_longitude";
    public static final String COLUMN_PHONE               = "_phone";
    public static final String COLUMN_WORK_HOURS          = "_work_hours";
    public static final String COLUMN_SQUARE              = "_square";
    public static final String COLUMN_OPENING_DATE        = "_opening_date";
    public static final String COLUMN_PARKING_PLACES      = "_parking_places";
    public static final String COLUMN_NUMBER_OF_CASHBOXES = "_number_of_cashboxes";
    public static final String COLUMN_SERVICES_SET        = "_services_set";



    public static final String[] CITIES_COLUMNS =   {
                                                        COLUMN_ID,
                                                        COLUMN_NAME
                                                    };

    public static final String[] SERVICES_COLUMNS = {
                                                        COLUMN_ID,
                                                        COLUMN_NAME
                                                    };

    public static final String[] SHOPS_COLUMNS =    {
                                                        COLUMN_ID,
                                                        COLUMN_CITY_ID,
                                                        COLUMN_NAME,
                                                        COLUMN_LATITUDE,
                                                        COLUMN_LONGITUDE,
                                                        COLUMN_PHONE,
                                                        COLUMN_WORK_HOURS,
                                                        COLUMN_SQUARE,
                                                        COLUMN_OPENING_DATE,
                                                        COLUMN_PARKING_PLACES,
                                                        COLUMN_NUMBER_OF_CASHBOXES,
                                                        COLUMN_SERVICES_SET
                                                    };



    public static final String CITIES_TABLE_NAME   = "cities";
    public static final String SERVICES_TABLE_NAME = "services";
    public static final String SHOPS_TABLE_NAME    = "shops";



    private static final String CITIES_TABLE_CREATE = "CREATE TABLE " + CITIES_TABLE_NAME + " " +
                                                      "(" +
                                                           COLUMN_ID   + " INTEGER PRIMARY KEY, " +
                                                           COLUMN_NAME + " TEXT NOT NULL "        +
                                                      ");";

    private static final String SERVICES_TABLE_CREATE = "CREATE TABLE " + SERVICES_TABLE_NAME + " " +
                                                        "(" +
                                                             COLUMN_ID   + " INTEGER PRIMARY KEY, " +
                                                             COLUMN_NAME + " TEXT NOT NULL "        +
                                                        ");";

    private static final String SHOPS_TABLE_CREATE = "CREATE TABLE " + SHOPS_TABLE_NAME + " " +
                                                     "(" +
                                                          COLUMN_ID                  + " INTEGER PRIMARY KEY, "                                                                            +
                                                          COLUMN_CITY_ID             + " INTEGER NOT NULL REFERENCES " + CITIES_TABLE_NAME + "(" + COLUMN_ID + "), "                       +
                                                          COLUMN_NAME                + " TEXT NOT NULL, "                                                                                  +
                                                          COLUMN_LATITUDE            + " REAL NOT NULL CHECK (" + COLUMN_LATITUDE  + " >= -90)  CHECK (" + COLUMN_LATITUDE  + " <= 90), "  +
                                                          COLUMN_LONGITUDE           + " REAL NOT NULL CHECK (" + COLUMN_LONGITUDE + " >= -180) CHECK (" + COLUMN_LONGITUDE + " <= 180), " +
                                                          COLUMN_PHONE               + " TEXT NOT NULL, "                                                                                  +
                                                          COLUMN_WORK_HOURS          + " TEXT NOT NULL, "                                                                                  +
                                                          COLUMN_SQUARE              + " INTEGER NOT NULL, "                                                                               +
                                                          COLUMN_OPENING_DATE        + " TEXT NOT NULL, "                                                                                  +
                                                          COLUMN_PARKING_PLACES      + " INTEGER NOT NULL, "                                                                               +
                                                          COLUMN_NUMBER_OF_CASHBOXES + " INTEGER NOT NULL, "                                                                               +
                                                          COLUMN_SERVICES_SET        + " INTEGER NOT NULL "                                                                                +
                                                     ");";



    public static final int CITY_ID_MOSCOW           = 1;
    public static final int CITY_ID_SAINT_PETERSBURG = 2;
    public static final int CITY_ID_ASTRAKHAN        = 3;
    public static final int CITY_ID_VOLGOGRAD        = 4;
    public static final int CITY_ID_VORONEZH         = 5;
    public static final int CITY_ID_EKATERINBURG     = 6;
    public static final int CITY_ID_IVANOVO          = 7;
    public static final int CITY_ID_IRKUTSK          = 8;
    public static final int CITY_ID_KRASNODAR        = 9;
    public static final int CITY_ID_KRASNOYARSK      = 10;
    public static final int CITY_ID_LIPETSK          = 11;
    public static final int CITY_ID_MURMANSK         = 12;
    public static final int CITY_ID_NIZHNY_NOVGOROD  = 13;
    public static final int CITY_ID_NOVOSIBIRSK      = 14;
    public static final int CITY_ID_NOVOCHERKASSK    = 15;
    public static final int CITY_ID_OMSK             = 16;
    public static final int CITY_ID_ORENBURG         = 17;
    public static final int CITY_ID_ROSTOV_ON_DON    = 18;
    public static final int CITY_ID_SARATOV          = 19;
    public static final int CITY_ID_SOCHI            = 20;
    public static final int CITY_ID_STAVROPOL        = 21;
    public static final int CITY_ID_STERLITAMAK      = 22;
    public static final int CITY_ID_SURGUT           = 23;
    public static final int CITY_ID_SYKTYVKAR        = 24;
    public static final int CITY_ID_TOLYATTI         = 25;
    public static final int CITY_ID_TYUMEN           = 26;
    public static final int CITY_ID_UFA              = 27;
    public static final int CITY_ID_CHEREPOVETS      = 28;

    public static final int SERVICE_ID_CLEARING_SETTLEMENT     = 1;
    public static final int SERVICE_ID_COSMETICS               = 2;
    public static final int SERVICE_ID_PLAYGROUND              = 3;
    public static final int SERVICE_ID_FISH_ISLAND             = 4;
    public static final int SERVICE_ID_BAKERY                  = 5;
    public static final int SERVICE_ID_COOKERY                 = 6;
    public static final int SERVICE_ID_TAXI_ORDERING           = 7;
    public static final int SERVICE_ID_PHARMACY                = 8;
    public static final int SERVICE_ID_ORDERING_FOOD           = 9;
    public static final int SERVICE_ID_DEGUSTATION             = 10;
    public static final int SERVICE_ID_CAFE                    = 11;
    public static final int SERVICE_ID_GIFT_CARDS              = 12;
    public static final int SERVICE_ID_PARKING                 = 13;
    public static final int SERVICE_ID_POINT_OF_ISSUING_ORDERS = 14;

    public static final int SERVICE_CLEARING_SETTLEMENT_MASK     = 1 << (SERVICE_ID_CLEARING_SETTLEMENT     - 1);
    public static final int SERVICE_COSMETICS_MASK               = 1 << (SERVICE_ID_COSMETICS               - 1);
    public static final int SERVICE_PLAYGROUND_MASK              = 1 << (SERVICE_ID_PLAYGROUND              - 1);
    public static final int SERVICE_FISH_ISLAND_MASK             = 1 << (SERVICE_ID_FISH_ISLAND             - 1);
    public static final int SERVICE_BAKERY_MASK                  = 1 << (SERVICE_ID_BAKERY                  - 1);
    public static final int SERVICE_COOKERY_MASK                 = 1 << (SERVICE_ID_COOKERY                 - 1);
    public static final int SERVICE_TAXI_ORDERING_MASK           = 1 << (SERVICE_ID_TAXI_ORDERING           - 1);
    public static final int SERVICE_PHARMACY_MASK                = 1 << (SERVICE_ID_PHARMACY                - 1);
    public static final int SERVICE_ORDERING_FOOD_MASK           = 1 << (SERVICE_ID_ORDERING_FOOD           - 1);
    public static final int SERVICE_DEGUSTATION_MASK             = 1 << (SERVICE_ID_DEGUSTATION             - 1);
    public static final int SERVICE_CAFE_MASK                    = 1 << (SERVICE_ID_CAFE                    - 1);
    public static final int SERVICE_GIFT_CARDS_MASK              = 1 << (SERVICE_ID_GIFT_CARDS              - 1);
    public static final int SERVICE_PARKING_MASK                 = 1 << (SERVICE_ID_PARKING                 - 1);
    public static final int SERVICE_POINT_OF_ISSUING_ORDERS_MASK = 1 << (SERVICE_ID_POINT_OF_ISSUING_ORDERS - 1);



    private Context mContext = null;



    public MainDatabase(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);

        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createTables(db);
        fillStaticTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion <= 0)
        {
            dropStaticTables(db);
            createStaticTables(db);
            fillStaticTables(db);
        }
    }

    private void createTables(SQLiteDatabase db)
    {
        createStaticTables(db);
        createDynamicTables(db);
    }

    private void createStaticTables(SQLiteDatabase db)
    {
        db.execSQL(CITIES_TABLE_CREATE);
        db.execSQL(SERVICES_TABLE_CREATE);
        db.execSQL(SHOPS_TABLE_CREATE);
    }

    private void dropStaticTables(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE " + SHOPS_TABLE_NAME      + ";");
        db.execSQL("DROP TABLE " + SERVICES_TABLE_CREATE + ";");
        db.execSQL("DROP TABLE " + CITIES_TABLE_CREATE   + ";");
    }

    private void createDynamicTables(SQLiteDatabase db)
    {
    }

    private void fillStaticTables(SQLiteDatabase db)
    {
        fillCitiesTable(db);
        fillServicesTable(db);
        fillShopsTable(db);
    }

    private void fillCitiesTable(SQLiteDatabase db)
    {
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_MOSCOW,           mContext.getResources().getString(R.string.city_moscow));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_SAINT_PETERSBURG, mContext.getResources().getString(R.string.city_st_petersburg));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_ASTRAKHAN,        mContext.getResources().getString(R.string.city_astrakhan));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_VOLGOGRAD,        mContext.getResources().getString(R.string.city_volgograd));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_VORONEZH,         mContext.getResources().getString(R.string.city_voronezh));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_EKATERINBURG,     mContext.getResources().getString(R.string.city_ekaterinburg));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_IVANOVO,          mContext.getResources().getString(R.string.city_ivanovo));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_IRKUTSK,          mContext.getResources().getString(R.string.city_irkutsk));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_KRASNODAR,        mContext.getResources().getString(R.string.city_krasnodar));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_KRASNOYARSK,      mContext.getResources().getString(R.string.city_krasnoyarsk));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_LIPETSK,          mContext.getResources().getString(R.string.city_lipetsk));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_MURMANSK,         mContext.getResources().getString(R.string.city_murmansk));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_NIZHNY_NOVGOROD,  mContext.getResources().getString(R.string.city_nizhny_novgorod));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_NOVOSIBIRSK,      mContext.getResources().getString(R.string.city_novosibirsk));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_NOVOCHERKASSK,    mContext.getResources().getString(R.string.city_novocherkassk));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_OMSK,             mContext.getResources().getString(R.string.city_omsk));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_ORENBURG,         mContext.getResources().getString(R.string.city_orenburg));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_ROSTOV_ON_DON,    mContext.getResources().getString(R.string.city_rostov_on_don));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_SARATOV,          mContext.getResources().getString(R.string.city_saratov));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_SOCHI,            mContext.getResources().getString(R.string.city_sochi));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_STAVROPOL,        mContext.getResources().getString(R.string.city_stavropol));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_STERLITAMAK,      mContext.getResources().getString(R.string.city_sterlitamak));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_SURGUT,           mContext.getResources().getString(R.string.city_surgut));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_SYKTYVKAR,        mContext.getResources().getString(R.string.city_syktyvkar));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_TOLYATTI,         mContext.getResources().getString(R.string.city_tolyatti));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_TYUMEN,           mContext.getResources().getString(R.string.city_tyumen));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_UFA,              mContext.getResources().getString(R.string.city_ufa));
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_CHEREPOVETS,      mContext.getResources().getString(R.string.city_cherepovets));
    }

    private void fillServicesTable(SQLiteDatabase db)
    {
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_CLEARING_SETTLEMENT,     mContext.getResources().getString(R.string.service_clearing_settlement));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_COSMETICS,               mContext.getResources().getString(R.string.service_cosmetics));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_PLAYGROUND,              mContext.getResources().getString(R.string.service_playground));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_FISH_ISLAND,             mContext.getResources().getString(R.string.service_fish_island));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_BAKERY,                  mContext.getResources().getString(R.string.service_bakery));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_COOKERY,                 mContext.getResources().getString(R.string.service_cookery));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_TAXI_ORDERING,           mContext.getResources().getString(R.string.service_taxi_ordering));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_PHARMACY,                mContext.getResources().getString(R.string.service_pharmacy));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_ORDERING_FOOD,           mContext.getResources().getString(R.string.service_ordering_food));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_DEGUSTATION,             mContext.getResources().getString(R.string.service_degustation));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_CAFE,                    mContext.getResources().getString(R.string.service_cafe));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_GIFT_CARDS,              mContext.getResources().getString(R.string.service_gift_cards));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_PARKING,                 mContext.getResources().getString(R.string.service_parking));
        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_POINT_OF_ISSUING_ORDERS, mContext.getResources().getString(R.string.service_point_of_issuing_orders));
    }

    private void fillShopsTable(SQLiteDatabase db)
    {
        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                1,                                      // COLUMN_ID
                CITY_ID_SAINT_PETERSBURG,               // COLUMN_CITY_ID
                "Гипермаркет О’КЕЙ «Балканская»",       // COLUMN_NAME
                59.829385934709,                        // COLUMN_LATITUDE
                30.38297256721,                         // COLUMN_LONGITUDE
                "+7 (812) 703-70-12",                   // COLUMN_PHONE
                "8:00 - 24:00",                         // COLUMN_WORK_HOURS
                6484,                                   // COLUMN_SQUARE
                "20.12.2007",                           // COLUMN_OPENING_DATE
                561,                                    // COLUMN_PARKING_PLACES
                37,                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );
    }

    public void insertToTable(SQLiteDatabase db, String tableName, String[] columns, Object... values)
    {
        if (columns.length != values.length)
        {
            AppLog.wtf(TAG, "Incorrect amount of columns and values: " + String.valueOf(columns.length) + " != " + String.valueOf(values.length));

            return;
        }



        StringBuilder builder = new StringBuilder();

        builder.append("INSERT INTO ");
        builder.append(tableName);
        builder.append(" (");
        builder.append(TextUtils.join(", ", columns));
        builder.append(") VALUES (");

        for (int i = 0; i < values.length; ++i)
        {
            if (i > 0)
            {
                builder.append(", ");
            }

            builder.append("\'");
            builder.append(String.valueOf(values[i]).replace("\'", "\\\'"));
            builder.append("\'");
        }

        builder.append(");");

        db.execSQL(builder.toString());
    }
}
