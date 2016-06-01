package ru.okmarket.okgoods.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.other.ShopInfo;
import ru.okmarket.okgoods.util.AppLog;

public class MainDatabase extends SQLiteOpenHelper
{
    @SuppressWarnings("unused")
    private static final String TAG = "MainDatabase";



    private static final String DB_NAME    = "main.db";
    private static final int    DB_VERSION = 1;



    public static final String COLUMN_ID                  = "_id";
    public static final String COLUMN_NAME                = "_name";
    public static final String COLUMN_CITY_ID             = "_city_id";
    public static final String COLUMN_IS_HYPERMARKET      = "_is_hypermarket";
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
                                                        COLUMN_IS_HYPERMARKET,
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
                                                          COLUMN_IS_HYPERMARKET      + " INTEGER NOT NULL, "                                                                               +
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
    public static final int CITY_ID_ST_PETERSBURG    = 2;
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
    public static final int CITY_ID_NIZHNIY_NOVGOROD = 13;
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

    public static final String[] CITIES = {
              "MOSCOW"
            , "ST_PETERSBURG"
            , "ASTRAKHAN"
            , "VOLGOGRAD"
            , "VORONEZH"
            , "EKATERINBURG"
            , "IVANOVO"
            , "IRKUTSK"
            , "KRASNODAR"
            , "KRASNOYARSK"
            , "LIPETSK"
            , "MURMANSK"
            , "NIZHNIY_NOVGOROD"
            , "NOVOSIBIRSK"
            , "NOVOCHERKASSK"
            , "OMSK"
            , "ORENBURG"
            , "ROSTOV_ON_DON"
            , "SARATOV"
            , "SOCHI"
            , "STAVROPOL"
            , "STERLITAMAK"
            , "SURGUT"
            , "SYKTYVKAR"
            , "TOLYATTI"
            , "TYUMEN"
            , "UFA"
            , "CHEREPOVETS"
    };

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

    public static final int SERVICE_CLEARING_SETTLEMENT_MASK     = 0x00000001;
    public static final int SERVICE_COSMETICS_MASK               = 0x00000002;
    public static final int SERVICE_PLAYGROUND_MASK              = 0x00000004;
    public static final int SERVICE_FISH_ISLAND_MASK             = 0x00000008;
    public static final int SERVICE_BAKERY_MASK                  = 0x00000010;
    public static final int SERVICE_COOKERY_MASK                 = 0x00000020;
    public static final int SERVICE_TAXI_ORDERING_MASK           = 0x00000040;
    public static final int SERVICE_PHARMACY_MASK                = 0x00000080;
    public static final int SERVICE_ORDERING_FOOD_MASK           = 0x00000100;
    public static final int SERVICE_DEGUSTATION_MASK             = 0x00000200;
    public static final int SERVICE_CAFE_MASK                    = 0x00000400;
    public static final int SERVICE_GIFT_CARDS_MASK              = 0x00000800;
    public static final int SERVICE_PARKING_MASK                 = 0x00001000;
    public static final int SERVICE_POINT_OF_ISSUING_ORDERS_MASK = 0x00002000;

    public static final int SHOP_SUPERMARKET = 0;
    public static final int SHOP_HYPERMARKET = 1;

    public static final int SHOP_ID_MOSCOW_HYPERMARKET_OK_ROSTOKINO                                     = 284;
    public static final int SHOP_ID_MOSCOW_SUPERMARKET_OK_MOSCOW_LENINSKIY                              = 532;
    public static final int SHOP_ID_MOSCOW_HYPERMARKET_OK_PYATNITSKOE_7KM                               = 534;
    public static final int SHOP_ID_MOSCOW_SUPERMARKET_OK_VENEVSKAYA                                    = 536;
    public static final int SHOP_ID_MOSCOW_SUPERMARKET_OK_LOBNYA_KRASNOPOLYANSKIY                       = 538;
    public static final int SHOP_ID_MOSCOW_SUPERMARKET_OK_LYUBERTSY_GRENADA                             = 540;
    public static final int SHOP_ID_MOSCOW_HYPERMARKET_OK_PUTILKOVO                                     = 542;
    public static final int SHOP_ID_MOSCOW_SUPERMARKET_OK_VARSHAVSKOE_SOMBRERO                          = 546;
    public static final int SHOP_ID_MOSCOW_HYPERMARKET_OK_IYUN_MYTISHCHI                                = 548;
    public static final int SHOP_ID_MOSCOW_HYPERMARKET_OK_NOGINSK_BORILOVO                              = 550;
    public static final int SHOP_ID_MOSCOW_SUPERMARKET_OK_ZELENOGRAD_PANFILOVSKIY                       = 552;
    public static final int SHOP_ID_MOSCOW_HYPERMARKET_OK_GUD_ZON                                       = 554;
    public static final int SHOP_ID_MOSCOW_HYPERMARKET_OK_MOSCOW_RIO                                    = 556;
    public static final int SHOP_ID_MOSCOW_HYPERMARKET_OK_VESNA_ALTUFEVO                                = 558;
    public static final int SHOP_ID_MOSCOW_HYPERMARKET_OK_VODNYY                                        = 560;
    public static final int SHOP_ID_MOSCOW_HYPERMARKET_OK_KIROVOGRADSKAYA_KOLUMBUS                      = 564;
    public static final int SHOP_ID_MOSCOW_SUPERMARKET_OK_MOSCOW_BALAKLAVSKIY                           = 22116;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_LADOZHSKAYA                            = 69;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_BOGATYRSKIY                            = 71;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_VYBORGSKOE                             = 73;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_ZELENOGORSK_VOKZALNAYA                 = 458;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_KOLPINO                                = 460;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_RYBATSKOE                              = 461;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_GRAND_KANON                            = 464;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_INDUSTRIALNYY                          = 466;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_BALKANSKAYA                            = 468;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_LENEHKSPO                              = 470;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_OZERKI                                 = 472;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_PULKOVSKOE                             = 474;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_EHLEKTROSILA                           = 476;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_PARASHYUTNAYA                          = 478;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_BOGATYRSKIY_YAKHTENNAYA                = 481;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_GATCHINA_LENINGRADSKOE                 = 484;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_TALLINSKOE_SHOSSE                      = 486;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_LENINSKIY                              = 490;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_GALEREYA                               = 492;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_SAVUSHKINA                             = 494;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_KINGISEPP_OKTYABRSKAYA                 = 496;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_SHCHERBAKOVA                           = 498;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_NASTAVNIKOV                            = 500;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_KRYLENKO                               = 502;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_ISKROVSKIY                             = 504;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_SOLIDARNOSTI                           = 506;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_SIZOVA                                 = 508;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_SESTRORETSK_VOLODARSKOGO               = 510;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_KRASNOE_SELO_STRELNINSKOE              = 512;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_RIO                                    = 513;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_AKADEMICHESKAYA                        = 515;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_SAVUSHKINA                             = 517;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_PLANERNAYA                             = 519;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_LENSKAYA                               = 521;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_BOLSHEVIKOV                            = 523;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_TIPANOVA                               = 525;
    public static final int SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_ZHUKOVA                                = 527;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_KOLPINO_TRUDYASHCHIKHSYA               = 529;
    public static final int SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_KOLPINO_TVERSKAYA                      = 531;
    public static final int SHOP_ID_ASTRAKHAN_HYPERMARKET_OK_ASTRAKHAN_ALIMPIK                          = 680;
    public static final int SHOP_ID_ASTRAKHAN_SUPERMARKET_OK_ASTRAKHAN_TRI_KOTA                         = 682;
    public static final int SHOP_ID_ASTRAKHAN_HYPERMARKET_OK_ASTRAKHAN_VOKZALNAYA                       = 684;
    public static final int SHOP_ID_VOLGOGRAD_HYPERMARKET_OK_VOLGOGRAD_DIAMANT                          = 674;
    public static final int SHOP_ID_VOLGOGRAD_SUPERMARKET_OK_VOLGOGRAD_PIRAMIDA                         = 676;
    public static final int SHOP_ID_VOLGOGRAD_SUPERMARKET_OK_VOLGOGRAD_VOROSHILOVSKIY                   = 678;
    public static final int SHOP_ID_VORONEZH_HYPERMARKET_OK_VORONEZH_GALEREYA_CHIZHOVA                  = 666;
    public static final int SHOP_ID_VORONEZH_HYPERMARKET_OK_VORONEZH_SHISHKOVA                          = 668;
    public static final int SHOP_ID_VORONEZH_SUPERMARKET_OK_VORONEZH_ZHUKOVA                            = 670;
    public static final int SHOP_ID_EKATERINBURG_HYPERMARKET_OK_EKATERINBURG_BABUSHKINA                 = 662;
    public static final int SHOP_ID_EKATERINBURG_HYPERMARKET_OK_EKATERINBURG_RADUGA                     = 664;
    public static final int SHOP_ID_IVANOVO_HYPERMARKET_OK_IVANOVO_LEZHNEVSKAYA_TOPOL                   = 658;
    public static final int SHOP_ID_IRKUTSK_HYPERMARKET_OK_IRKUTSK_KOMSOMOLL                            = 656;
    public static final int SHOP_ID_KRASNODAR_HYPERMARKET_OK_KRASNODAR_MINSKAYA                         = 646;
    public static final int SHOP_ID_KRASNODAR_HYPERMARKET_OK_KRASNODAR_MACHUGI                          = 648;
    public static final int SHOP_ID_KRASNODAR_HYPERMARKET_OK_KRASNODAR_GALAKTIKA                        = 650;
    public static final int SHOP_ID_KRASNODAR_SUPERMARKET_OK_KRASNODAR_BOSS_KHAUZ                       = 652;
    public static final int SHOP_ID_KRASNODAR_HYPERMARKET_OK_KRASNODAR_OZ                               = 654;
    public static final int SHOP_ID_KRASNOYARSK_HYPERMARKET_OK_KRASNOYARSK_PLANETA                      = 640;
    public static final int SHOP_ID_KRASNOYARSK_SUPERMARKET_OK_KRASNOYARSK_KRASNODARSKAYA               = 642;
    public static final int SHOP_ID_KRASNOYARSK_HYPERMARKET_OK_KRASNOYARSK_SIBIRSKIY                    = 644;
    public static final int SHOP_ID_LIPETSK_HYPERMARKET_OK_LIPETSK_EVROPA                               = 636;
    public static final int SHOP_ID_LIPETSK_SUPERMARKET_OK_LIPETSK_PETRA_SMORODINA                      = 638;
    public static final int SHOP_ID_MURMANSK_HYPERMARKET_OK_MURMANSK_SHMIDTA                            = 632;
    public static final int SHOP_ID_NIZHNIY_NOVGOROD_HYPERMARKET_OK_NIZHNIY_NOVGOROD_TSEKH_TARY         = 626;
    public static final int SHOP_ID_NIZHNIY_NOVGOROD_HYPERMARKET_OK_NIZHNIY_NOVGOROD_DEREVOOBDELOCHNAYA = 628;
    public static final int SHOP_ID_NIZHNIY_NOVGOROD_HYPERMARKET_OK_NIZHNIY_NOVGOROD_SOVETSKAYA         = 630;
    public static final int SHOP_ID_NOVOSIBIRSK_HYPERMARKET_OK_NOVOSIBIRSK_AURA                         = 609;
    public static final int SHOP_ID_NOVOSIBIRSK_HYPERMARKET_OK_NOVOSIBIRSK_MALINKA                      = 624;
    public static final int SHOP_ID_NOVOCHERKASSK_SUPERMARKET_OK_NOVOCHERKASSK_MAGNITNYY                = 607;
    public static final int SHOP_ID_OMSK_HYPERMARKET_OK_OMSK_EHNTUZIASTOV                               = 603;
    public static final int SHOP_ID_OMSK_SUPERMARKET_OK_OMSK_70_LET_OKTYABRYA                           = 605;
    public static final int SHOP_ID_ORENBURG_HYPERMARKET_OK_ORENBURG_SALMYSHSKAYA                       = 601;
    public static final int SHOP_ID_ROSTOV_ON_DON_HYPERMARKET_OK_ROSTOV_ON_DON_MALINOVSKOGO             = 597;
    public static final int SHOP_ID_ROSTOV_ON_DON_HYPERMARKET_OK_ROSTOV_ON_DON_KOMAROVA                 = 599;
    public static final int SHOP_ID_SARATOV_HYPERMARKET_OK_SARATOV_KHEHPPI_MOLL                         = 593;
    public static final int SHOP_ID_SARATOV_SUPERMARKET_OK_SARATOV_TANKISTOV                            = 595;
    public static final int SHOP_ID_SOCHI_HYPERMARKET_OK_SOCHI_MOREMOLL                                 = 591;
    public static final int SHOP_ID_STAVROPOL_HYPERMARKET_OK_STAVROPOL_DOVATORTSEV                      = 589;
    public static final int SHOP_ID_STERLITAMAK_HYPERMARKET_OK_STERLITAMAK_KHUDAYBERDINA                = 587;
    public static final int SHOP_ID_SURGUT_HYPERMARKET_OK_SURGUT_SITI_MOLL                              = 583;
    public static final int SHOP_ID_SURGUT_HYPERMARKET_OK_SURGUT_NEFTEYUGANSKOE_AURA                    = 585;
    public static final int SHOP_ID_SYKTYVKAR_HYPERMARKET_OK_SYKTYVKAR_OKTYABRSKIY_IYUN                 = 581;
    public static final int SHOP_ID_TOLYATTI_HYPERMARKET_OK_TOLYATTI_BORKOVSKAYA                        = 577;
    public static final int SHOP_ID_TOLYATTI_SUPERMARKET_OK_TOLYATTI_SPORTIVNAYA_MALINA                 = 579;
    public static final int SHOP_ID_TYUMEN_HYPERMARKET_OK_TYUMEN_SHIROTNAYA                             = 573;
    public static final int SHOP_ID_TYUMEN_HYPERMARKET_OK_TYUMEN_FEDYUNINSKOGO_OSTROV                   = 575;
    public static final int SHOP_ID_UFA_HYPERMARKET_OK_UFA_ZHUKOVA                                      = 567;
    public static final int SHOP_ID_UFA_HYPERMARKET_OK_UFA_IYUN                                         = 569;
    public static final int SHOP_ID_UFA_HYPERMARKET_OK_UFA_PLANETA                                      = 571;
    public static final int SHOP_ID_CHEREPOVETS_HYPERMARKET_OK_CHEREPOVETS_RAAKHE                       = 565;



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
            recreateStaticTables(db);
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
        db.execSQL("DROP TABLE IF EXISTS " + SHOPS_TABLE_NAME    + ";");
        db.execSQL("DROP TABLE IF EXISTS " + SERVICES_TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + CITIES_TABLE_NAME   + ";");
    }

    public void recreateStaticTables(SQLiteDatabase db)
    {
        dropStaticTables(db);
        createStaticTables(db);
        fillStaticTables(db);
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
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_ST_PETERSBURG,    mContext.getResources().getString(R.string.city_st_petersburg));
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
        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_NIZHNIY_NOVGOROD, mContext.getResources().getString(R.string.city_nizhniy_novgorod));
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
                SHOP_ID_MOSCOW_HYPERMARKET_OK_ROSTOKINO,                                            // COLUMN_ID
                CITY_ID_MOSCOW,                                                                     // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_hypermarket_ok_rostokino),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                   // COLUMN_IS_HYPERMARKET
                55.844309395064,                                                                    // COLUMN_LATITUDE
                37.659546380951,                                                                    // COLUMN_LONGITUDE
                "+7 (495) 213-32-61, +7 (495) 213-32-62",                                           // COLUMN_PHONE
                "8:00 - 23:00",                                                                     // COLUMN_WORK_HOURS
                11300,                                                                              // COLUMN_SQUARE
                "18.11.2009",                                                                       // COLUMN_OPENING_DATE
                7500,                                                                               // COLUMN_PARKING_PLACES
                64,                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK             |                                      // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK               |
                        SERVICE_PLAYGROUND_MASK              |
                        SERVICE_FISH_ISLAND_MASK             |
                        SERVICE_BAKERY_MASK                  |
                        SERVICE_COOKERY_MASK                 |
                        SERVICE_TAXI_ORDERING_MASK           |
                        SERVICE_ORDERING_FOOD_MASK           |
                        SERVICE_DEGUSTATION_MASK             |
                        SERVICE_GIFT_CARDS_MASK              |
                        SERVICE_POINT_OF_ISSUING_ORDERS_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_SUPERMARKET_OK_MOSCOW_LENINSKIY,                                             // COLUMN_ID
                CITY_ID_MOSCOW,                                                                             // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_supermarket_ok_moscow_leninskiy),    // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                55.666690982744,                                                                            // COLUMN_LATITUDE
                37.515515923279,                                                                            // COLUMN_LONGITUDE
                "+7 (495) 258-60-14",                                                                       // COLUMN_PHONE
                "8:00 - 23:00",                                                                             // COLUMN_WORK_HOURS
                2827,                                                                                       // COLUMN_SQUARE
                "09.12.2011",                                                                               // COLUMN_OPENING_DATE
                155,                                                                                        // COLUMN_PARKING_PLACES
                0,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PHARMACY_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_HYPERMARKET_OK_PYATNITSKOE_7KM,                                          // COLUMN_ID
                CITY_ID_MOSCOW,                                                                         // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_hypermarket_ok_pyatnitskoe_7km), // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                55.874927139863,                                                                        // COLUMN_LATITUDE
                37.333757383636,                                                                        // COLUMN_LONGITUDE
                "+7 (495) 213-17-82",                                                                   // COLUMN_PHONE
                "9:00 - 24:00",                                                                         // COLUMN_WORK_HOURS
                12651,                                                                                  // COLUMN_SQUARE
                "22.12.2011",                                                                           // COLUMN_OPENING_DATE
                1200,                                                                                   // COLUMN_PARKING_PLACES
                65,                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_SUPERMARKET_OK_VENEVSKAYA,                                           // COLUMN_ID
                CITY_ID_MOSCOW,                                                                     // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_supermarket_ok_venevskaya),  // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                   // COLUMN_IS_HYPERMARKET
                55.548003424894,                                                                    // COLUMN_LATITUDE
                37.543148940475,                                                                    // COLUMN_LONGITUDE
                "+7 (495) 411-78-48",                                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                     // COLUMN_WORK_HOURS
                3300,                                                                               // COLUMN_SQUARE
                "26.12.2011",                                                                       // COLUMN_OPENING_DATE
                350,                                                                                // COLUMN_PARKING_PLACES
                17,                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                  // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_SUPERMARKET_OK_LOBNYA_KRASNOPOLYANSKIY,                                          // COLUMN_ID
                CITY_ID_MOSCOW,                                                                                 // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_supermarket_ok_lobnya_krasnopolyanskiy), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                56.008445449313,                                                                                // COLUMN_LATITUDE
                37.440043992065,                                                                                // COLUMN_LONGITUDE
                "+7 (495) 662-14-70",                                                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                1260,                                                                                           // COLUMN_SQUARE
                "18.12.2009",                                                                                   // COLUMN_OPENING_DATE
                320,                                                                                            // COLUMN_PARKING_PLACES
                0,                                                                                              // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                              // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_SUPERMARKET_OK_LYUBERTSY_GRENADA,                                            // COLUMN_ID
                CITY_ID_MOSCOW,                                                                             // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_supermarket_ok_lyubertsy_grenada),   // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                55.669981699694,                                                                            // COLUMN_LATITUDE
                37.871325220238,                                                                            // COLUMN_LONGITUDE
                "+7 (495) 660-68-50",                                                                       // COLUMN_PHONE
                "9:00 - 23:00",                                                                             // COLUMN_WORK_HOURS
                1015,                                                                                       // COLUMN_SQUARE
                "15.08.2011",                                                                               // COLUMN_OPENING_DATE
                190,                                                                                        // COLUMN_PARKING_PLACES
                9,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_HYPERMARKET_OK_PUTILKOVO,                                            // COLUMN_ID
                CITY_ID_MOSCOW,                                                                     // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_hypermarket_ok_putilkovo),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                   // COLUMN_IS_HYPERMARKET
                55.864167062595,                                                                    // COLUMN_LATITUDE
                37.397173695801,                                                                    // COLUMN_LONGITUDE
                "+7 (499) 272-54-44",                                                               // COLUMN_PHONE
                "0:00 - 24:00",                                                                     // COLUMN_WORK_HOURS
                10700,                                                                              // COLUMN_SQUARE
                "11.12.2012",                                                                       // COLUMN_OPENING_DATE
                1000,                                                                               // COLUMN_PARKING_PLACES
                57,                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK             |                                      // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK               |
                        SERVICE_PLAYGROUND_MASK              |
                        SERVICE_FISH_ISLAND_MASK             |
                        SERVICE_BAKERY_MASK                  |
                        SERVICE_COOKERY_MASK                 |
                        SERVICE_TAXI_ORDERING_MASK           |
                        SERVICE_PHARMACY_MASK                |
                        SERVICE_ORDERING_FOOD_MASK           |
                        SERVICE_DEGUSTATION_MASK             |
                        SERVICE_CAFE_MASK                    |
                        SERVICE_GIFT_CARDS_MASK              |
                        SERVICE_PARKING_MASK                 |
                        SERVICE_POINT_OF_ISSUING_ORDERS_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_SUPERMARKET_OK_VARSHAVSKOE_SOMBRERO,                                             // COLUMN_ID
                CITY_ID_MOSCOW,                                                                                 // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_supermarket_ok_varshavskoe_sombrero),    // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                55.594641154391,                                                                                // COLUMN_LATITUDE
                37.599226559525,                                                                                // COLUMN_LONGITUDE
                "+7 (499) 272-77-92",                                                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                1108,                                                                                           // COLUMN_SQUARE
                "15.06.2012",                                                                                   // COLUMN_OPENING_DATE
                100,                                                                                            // COLUMN_PARKING_PLACES
                10,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                              // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_HYPERMARKET_OK_IYUN_MYTISHCHI,                                           // COLUMN_ID
                CITY_ID_MOSCOW,                                                                         // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_hypermarket_ok_iyun_mytishchi),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                55.919899980976,                                                                        // COLUMN_LATITUDE
                37.708311923279,                                                                        // COLUMN_LONGITUDE
                "+7 (495) 232-49-70",                                                                   // COLUMN_PHONE
                "9:00 - 23:00",                                                                         // COLUMN_WORK_HOURS
                10600,                                                                                  // COLUMN_SQUARE
                "02.03.2013",                                                                           // COLUMN_OPENING_DATE
                3500,                                                                                   // COLUMN_PARKING_PLACES
                38,                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_HYPERMARKET_OK_NOGINSK_BORILOVO,                                             // COLUMN_ID
                CITY_ID_MOSCOW,                                                                             // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_hypermarket_ok_noginsk_borilovo),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                55.831412545996,                                                                            // COLUMN_LATITUDE
                38.393524753967,                                                                            // COLUMN_LONGITUDE
                "+7 (495) 287-96-69",                                                                       // COLUMN_PHONE
                "0:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                9322,                                                                                       // COLUMN_SQUARE
                "20.05.2009",                                                                               // COLUMN_OPENING_DATE
                991,                                                                                        // COLUMN_PARKING_PLACES
                54,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                        // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_SUPERMARKET_OK_ZELENOGRAD_PANFILOVSKIY,                                          // COLUMN_ID
                CITY_ID_MOSCOW,                                                                                 // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_supermarket_ok_zelenograd_panfilovskiy), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                56.008797276189,                                                                                // COLUMN_LATITUDE
                37.200001584656,                                                                                // COLUMN_LONGITUDE
                "+7 (495) 212-00-76",                                                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                2442,                                                                                           // COLUMN_SQUARE
                "20.09.2012",                                                                                   // COLUMN_OPENING_DATE
                350,                                                                                            // COLUMN_PARKING_PLACES
                13,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                              // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_HYPERMARKET_OK_GUD_ZON,                                          // COLUMN_ID
                CITY_ID_MOSCOW,                                                                 // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_hypermarket_ok_gud_zon), // COLUMN_NAME
                SHOP_HYPERMARKET,                                                               // COLUMN_IS_HYPERMARKET
                55.665142624692,                                                                // COLUMN_LATITUDE
                37.625617616394,                                                                // COLUMN_LONGITUDE
                "+7 (499) 951-05-36",                                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                 // COLUMN_WORK_HOURS
                11220,                                                                          // COLUMN_SQUARE
                "31.12.2013",                                                                   // COLUMN_OPENING_DATE
                1600,                                                                           // COLUMN_PARKING_PLACES
                49,                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_HYPERMARKET_OK_MOSCOW_RIO,                                           // COLUMN_ID
                CITY_ID_MOSCOW,                                                                     // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_hypermarket_ok_moscow_rio),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                   // COLUMN_IS_HYPERMARKET
                55.908559061474,                                                                    // COLUMN_LATITUDE
                37.539291415344,                                                                    // COLUMN_LONGITUDE
                "+7 (499) 951-17-59",                                                               // COLUMN_PHONE
                "09:00 - 23:00",                                                                    // COLUMN_WORK_HOURS
                6500,                                                                               // COLUMN_SQUARE
                "24.07.2013",                                                                       // COLUMN_OPENING_DATE
                4000,                                                                               // COLUMN_PARKING_PLACES
                30,                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_HYPERMARKET_OK_VESNA_ALTUFEVO,                                           // COLUMN_ID
                CITY_ID_MOSCOW,                                                                         // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_hypermarket_ok_vesna_altufevo),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                55.913034926402,                                                                        // COLUMN_LATITUDE
                37.584363894135,                                                                        // COLUMN_LONGITUDE
                "+7 (499) 272-31-74",                                                                   // COLUMN_PHONE
                "9:00 - 24:00",                                                                         // COLUMN_WORK_HOURS
                9800,                                                                                   // COLUMN_SQUARE
                "18.04.2014",                                                                           // COLUMN_OPENING_DATE
                3100,                                                                                   // COLUMN_PARKING_PLACES
                54,                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_HYPERMARKET_OK_VODNYY,                                           // COLUMN_ID
                CITY_ID_MOSCOW,                                                                 // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_hypermarket_ok_vodnyy),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                               // COLUMN_IS_HYPERMARKET
                55.840302480470,                                                                // COLUMN_LATITUDE
                37.491758313492,                                                                // COLUMN_LONGITUDE
                "+7 (495) 139-20-85",                                                           // COLUMN_PHONE
                "8:00 - 24:00",                                                                 // COLUMN_WORK_HOURS
                3150,                                                                           // COLUMN_SQUARE
                "18.09.2014",                                                                   // COLUMN_OPENING_DATE
                805,                                                                            // COLUMN_PARKING_PLACES
                19,                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                            // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_HYPERMARKET_OK_KIROVOGRADSKAYA_KOLUMBUS,                                             // COLUMN_ID
                CITY_ID_MOSCOW,                                                                                     // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_hypermarket_ok_kirovogradskaya_kolumbus),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                55.612265447129,                                                                                    // COLUMN_LATITUDE
                37.606913669311,                                                                                    // COLUMN_LONGITUDE
                "+7 (495) 734-78-46",                                                                               // COLUMN_PHONE
                "8:00 - 24:00",                                                                                     // COLUMN_WORK_HOURS
                8500,                                                                                               // COLUMN_SQUARE
                "01.03.2015",                                                                                       // COLUMN_OPENING_DATE
                2600,                                                                                               // COLUMN_PARKING_PLACES
                36,                                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK             |                                                      // COLUMN_SERVICES_SET
                        SERVICE_PLAYGROUND_MASK              |
                        SERVICE_FISH_ISLAND_MASK             |
                        SERVICE_BAKERY_MASK                  |
                        SERVICE_COOKERY_MASK                 |
                        SERVICE_TAXI_ORDERING_MASK           |
                        SERVICE_ORDERING_FOOD_MASK           |
                        SERVICE_DEGUSTATION_MASK             |
                        SERVICE_GIFT_CARDS_MASK              |
                        SERVICE_PARKING_MASK                 |
                        SERVICE_POINT_OF_ISSUING_ORDERS_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MOSCOW_SUPERMARKET_OK_MOSCOW_BALAKLAVSKIY,                                          // COLUMN_ID
                CITY_ID_MOSCOW,                                                                             // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_moscow_supermarket_ok_moscow_balaklavskiy), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                55.642269909156,                                                                            // COLUMN_LATITUDE
                37.603531219857,                                                                            // COLUMN_LONGITUDE
                "+7 (495) 139-28-35  доб. 126",                                                             // COLUMN_PHONE
                "9:00 - 23:00",                                                                             // COLUMN_WORK_HOURS
                0,                                                                                          // COLUMN_SQUARE
                "01.03.2016",                                                                               // COLUMN_OPENING_DATE
                0,                                                                                          // COLUMN_PARKING_PLACES
                8,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_GIFT_CARDS_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_LADOZHSKAYA,                                           // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_ladozhskaya),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                59.929978494951,                                                                            // COLUMN_LATITUDE
                30.433080023804,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 703-70-05",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                11884,                                                                                      // COLUMN_SQUARE
                "24.03.2006",                                                                               // COLUMN_OPENING_DATE
                1359,                                                                                       // COLUMN_PARKING_PLACES
                60,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK             |                                              // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK               |
                        SERVICE_PLAYGROUND_MASK              |
                        SERVICE_FISH_ISLAND_MASK             |
                        SERVICE_BAKERY_MASK                  |
                        SERVICE_COOKERY_MASK                 |
                        SERVICE_TAXI_ORDERING_MASK           |
                        SERVICE_PHARMACY_MASK                |
                        SERVICE_ORDERING_FOOD_MASK           |
                        SERVICE_DEGUSTATION_MASK             |
                        SERVICE_CAFE_MASK                    |
                        SERVICE_GIFT_CARDS_MASK              |
                        SERVICE_PARKING_MASK                 |
                        SERVICE_POINT_OF_ISSUING_ORDERS_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_BOGATYRSKIY,                                           // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_bogatyrskiy),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                60.000131726412,                                                                            // COLUMN_LATITUDE
                30.272287944458,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 703-70-06",                                                                       // COLUMN_PHONE
                "0:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                8651,                                                                                       // COLUMN_SQUARE
                "24.03.2006",                                                                               // COLUMN_OPENING_DATE
                722,                                                                                        // COLUMN_PARKING_PLACES
                0,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                        // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_VYBORGSKOE,                                            // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_vyborgskoe),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                60.056460343261,                                                                            // COLUMN_LATITUDE
                30.309786855820,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 703-70-10",                                                                       // COLUMN_PHONE
                "0:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                8331,                                                                                       // COLUMN_SQUARE
                "12.01.2007",                                                                               // COLUMN_OPENING_DATE
                734,                                                                                        // COLUMN_PARKING_PLACES
                42,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK             |                                              // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK               |
                        SERVICE_PLAYGROUND_MASK              |
                        SERVICE_FISH_ISLAND_MASK             |
                        SERVICE_BAKERY_MASK                  |
                        SERVICE_TAXI_ORDERING_MASK           |
                        SERVICE_PHARMACY_MASK                |
                        SERVICE_ORDERING_FOOD_MASK           |
                        SERVICE_DEGUSTATION_MASK             |
                        SERVICE_GIFT_CARDS_MASK              |
                        SERVICE_PARKING_MASK                 |
                        SERVICE_POINT_OF_ISSUING_ORDERS_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_ZELENOGORSK_VOKZALNAYA,                                            // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_zelenogorsk_vokzalnaya),   // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                60.205953387285,                                                                                        // COLUMN_LATITUDE
                29.703745542992,                                                                                        // COLUMN_LONGITUDE
                "+7 (812) 449-31-42, +7 (812) 449-31-43",                                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                                         // COLUMN_WORK_HOURS
                1100,                                                                                                   // COLUMN_SQUARE
                "31.10.2011",                                                                                           // COLUMN_OPENING_DATE
                50,                                                                                                     // COLUMN_PARKING_PLACES
                0,                                                                                                      // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                                      // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_KOLPINO,                                           // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_kolpino),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                59.738853276050,                                                                        // COLUMN_LATITUDE
                30.622989330688,                                                                        // COLUMN_LONGITUDE
                "+7 (812) 648-06-70",                                                                   // COLUMN_PHONE
                "9:00 - 23:00",                                                                         // COLUMN_WORK_HOURS
                1788,                                                                                   // COLUMN_SQUARE
                "03.09.2010",                                                                           // COLUMN_OPENING_DATE
                182,                                                                                    // COLUMN_PARKING_PLACES
                47,                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_RYBATSKOE,                                             // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_rybatskoe),    // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                59.834180258448,                                                                            // COLUMN_LATITUDE
                30.504292957672,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 707-59-92, +7 (812) 707-58-83",                                                   // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                809,                                                                                        // COLUMN_SQUARE
                "25.08.2006",                                                                               // COLUMN_OPENING_DATE
                36,                                                                                         // COLUMN_PARKING_PLACES
                0,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_GRAND_KANON,                                           // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_grand_kanon),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                60.058714946298,                                                                            // COLUMN_LATITUDE
                30.335752568787,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 332-50-50, +7 (812) 332-48-70",                                                   // COLUMN_PHONE
                "10:00 - 23:00",                                                                            // COLUMN_WORK_HOURS
                5000,                                                                                       // COLUMN_SQUARE
                "25.07.2009",                                                                               // COLUMN_OPENING_DATE
                1500,                                                                                       // COLUMN_PARKING_PLACES
                34,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                        // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_INDUSTRIALNYY,                                             // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                          // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_industrialnyy),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                59.946871559036,                                                                                // COLUMN_LATITUDE
                30.477594746033,                                                                                // COLUMN_LONGITUDE
                "+7 (812) 703-70-13",                                                                           // COLUMN_PHONE
                "8:00 - 24:00",                                                                                 // COLUMN_WORK_HOURS
                3716,                                                                                           // COLUMN_SQUARE
                "11.06.2008",                                                                                   // COLUMN_OPENING_DATE
                249,                                                                                            // COLUMN_PARKING_PLACES
                28,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_BALKANSKAYA,                                           // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_balkanskaya),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                59.829385934709,                                                                            // COLUMN_LATITUDE
                30.382972567217,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 703-70-12",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                6484,                                                                                       // COLUMN_SQUARE
                "20.12.2007",                                                                               // COLUMN_OPENING_DATE
                561,                                                                                        // COLUMN_PARKING_PLACES
                37,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                        // COLUMN_SERVICES_SET
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

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_LENEHKSPO,                                             // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_lenehkspo),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                60.039012232311,                                                                            // COLUMN_LATITUDE
                30.409833251343,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 703-70-11",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                7865,                                                                                       // COLUMN_SQUARE
                "21.02.2007",                                                                               // COLUMN_OPENING_DATE
                572,                                                                                        // COLUMN_PARKING_PLACES
                42,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                        // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_OZERKI,                                            // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_ozerki),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                60.038165943990,                                                                        // COLUMN_LATITUDE
                30.320856746033,                                                                        // COLUMN_LONGITUDE
                "+7 (812) 703-70-01",                                                                   // COLUMN_PHONE
                "8:00 - 24:00",                                                                         // COLUMN_WORK_HOURS
                5535,                                                                                   // COLUMN_SQUARE
                "27.05.2002",                                                                           // COLUMN_OPENING_DATE
                431,                                                                                    // COLUMN_PARKING_PLACES
                44,                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_PULKOVSKOE,                                            // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_pulkovskoe),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                59.827402167838,                                                                            // COLUMN_LATITUDE
                30.316197042328,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 703-70-09",                                                                       // COLUMN_PHONE
                "0:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                10776,                                                                                      // COLUMN_SQUARE
                "25.12.2006",                                                                               // COLUMN_OPENING_DATE
                1024,                                                                                       // COLUMN_PARKING_PLACES
                57,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK             |                                              // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK               |
                        SERVICE_PLAYGROUND_MASK              |
                        SERVICE_FISH_ISLAND_MASK             |
                        SERVICE_COOKERY_MASK                 |
                        SERVICE_TAXI_ORDERING_MASK           |
                        SERVICE_PHARMACY_MASK                |
                        SERVICE_ORDERING_FOOD_MASK           |
                        SERVICE_DEGUSTATION_MASK             |
                        SERVICE_GIFT_CARDS_MASK              |
                        SERVICE_PARKING_MASK                 |
                        SERVICE_POINT_OF_ISSUING_ORDERS_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_EHLEKTROSILA,                                          // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_ehlektrosila), // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                59.881624397353,                                                                            // COLUMN_LATITUDE
                30.314871685181,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 703-70-08",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                8396,                                                                                       // COLUMN_SQUARE
                "25.12.2006",                                                                               // COLUMN_OPENING_DATE
                950,                                                                                        // COLUMN_PARKING_PLACES
                49,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                        // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_PARASHYUTNAYA,                                             // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                          // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_parashyutnaya),    // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                60.032335784638,                                                                                // COLUMN_LATITUDE
                30.248194542328,                                                                                // COLUMN_LONGITUDE
                "+7 (812) 244-01-47",                                                                           // COLUMN_PHONE
                "8:00 - 24:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                265,                                                                                            // COLUMN_PARKING_PLACES
                12,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                              // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_BOGATYRSKIY_YAKHTENNAYA,                                           // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_bogatyrskiy_yakhtennaya),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                60.002845871298,                                                                                        // COLUMN_LATITUDE
                30.228589822754,                                                                                        // COLUMN_LONGITUDE
                "+7 (812)332-90-50, +7 (812) 332-90-70",                                                                // COLUMN_PHONE
                "8:00 - 24:00",                                                                                         // COLUMN_WORK_HOURS
                23000,                                                                                                  // COLUMN_SQUARE
                "20.12.2013",                                                                                           // COLUMN_OPENING_DATE
                1190,                                                                                                   // COLUMN_PARKING_PLACES
                54,                                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_GATCHINA_LENINGRADSKOE,                                            // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_gatchina_leningradskoe),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                59.583557995025,                                                                                        // COLUMN_LATITUDE
                30.138592415344,                                                                                        // COLUMN_LONGITUDE
                "+7 (812) 648-10-30",                                                                                   // COLUMN_PHONE
                "9:00-24:00",                                                                                           // COLUMN_WORK_HOURS
                5200,                                                                                                   // COLUMN_SQUARE
                "14.02.2014",                                                                                           // COLUMN_OPENING_DATE
                342,                                                                                                    // COLUMN_PARKING_PLACES
                28,                                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                    // COLUMN_SERVICES_SET
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_TALLINSKOE_SHOSSE,                                             // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                              // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_tallinskoe_shosse),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                59.796010038670,                                                                                    // COLUMN_LATITUDE
                30.147072059524,                                                                                    // COLUMN_LONGITUDE
                "+7 (812) 718-50-44, +7 (812) 718-50-45",                                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                                     // COLUMN_WORK_HOURS
                9500,                                                                                               // COLUMN_SQUARE
                "27.12.2012",                                                                                       // COLUMN_OPENING_DATE
                680,                                                                                                // COLUMN_PARKING_PLACES
                51,                                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK             |                                                      // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK               |
                        SERVICE_PLAYGROUND_MASK              |
                        SERVICE_FISH_ISLAND_MASK             |
                        SERVICE_BAKERY_MASK                  |
                        SERVICE_COOKERY_MASK                 |
                        SERVICE_TAXI_ORDERING_MASK           |
                        SERVICE_PHARMACY_MASK                |
                        SERVICE_ORDERING_FOOD_MASK           |
                        SERVICE_DEGUSTATION_MASK             |
                        SERVICE_GIFT_CARDS_MASK              |
                        SERVICE_PARKING_MASK                 |
                        SERVICE_POINT_OF_ISSUING_ORDERS_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_LENINSKIY,                                             // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_leninskiy),    // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                59.854326784131,                                                                            // COLUMN_LATITUDE
                30.214835990738,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 448-20-89",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                1519,                                                                                       // COLUMN_SQUARE
                "22.02.2011",                                                                               // COLUMN_OPENING_DATE
                100,                                                                                        // COLUMN_PARKING_PLACES
                14,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PHARMACY_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_GALEREYA,                                          // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_galereya), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                59.927280510921,                                                                        // COLUMN_LATITUDE
                30.360726330688,                                                                        // COLUMN_LONGITUDE
                "+7 (812) 363-23-03",                                                                   // COLUMN_PHONE
                "10:00 - 23:00",                                                                        // COLUMN_WORK_HOURS
                1500,                                                                                   // COLUMN_SQUARE
                "25.11.2010",                                                                           // COLUMN_OPENING_DATE
                1300,                                                                                   // COLUMN_PARKING_PLACES
                15,                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                      // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_SAVUSHKINA,                                            // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_savushkina),   // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                59.988891386711,                                                                            // COLUMN_LATITUDE
                30.198248042328,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 448-81-73",                                                                       // COLUMN_PHONE
                "9:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                704,                                                                                        // COLUMN_SQUARE
                "21.12.2007",                                                                               // COLUMN_OPENING_DATE
                6,                                                                                          // COLUMN_PARKING_PLACES
                0,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PHARMACY_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_KINGISEPP_OKTYABRSKAYA,                                            // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_kingisepp_oktyabrskaya),   // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                59.376372366117,                                                                                        // COLUMN_LATITUDE
                28.608008738098,                                                                                        // COLUMN_LONGITUDE
                "+7 (812) 313-67-81",                                                                                   // COLUMN_PHONE
                "8:00 - 24:00",                                                                                         // COLUMN_WORK_HOURS
                1431,                                                                                                   // COLUMN_SQUARE
                "12.12.2008",                                                                                           // COLUMN_OPENING_DATE
                190,                                                                                                    // COLUMN_PARKING_PLACES
                0,                                                                                                      // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                                      // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_SHCHERBAKOVA,                                          // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_shcherbakova), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                0.000000000000,                                                                             // COLUMN_LATITUDE
                0.000000000000,                                                                             // COLUMN_LONGITUDE
                "+7 (812) 304-95-67",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                793,                                                                                        // COLUMN_SQUARE
                "21.12.2007",                                                                               // COLUMN_OPENING_DATE
                30,                                                                                         // COLUMN_PARKING_PLACES
                0,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_NASTAVNIKOV,                                           // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_nastavnikov),  // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                59.949111448699,                                                                            // COLUMN_LATITUDE
                30.489320220238,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 448-81-61",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                739,                                                                                        // COLUMN_SQUARE
                "29.09.2007",                                                                               // COLUMN_OPENING_DATE
                20,                                                                                         // COLUMN_PARKING_PLACES
                0,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_KRYLENKO,                                          // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_krylenko), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                59.900507000000,                                                                        // COLUMN_LATITUDE
                30.485892000000,                                                                        // COLUMN_LONGITUDE
                "+7 (812) 449-48-45",                                                                   // COLUMN_PHONE
                "9:00 - 23:00",                                                                         // COLUMN_WORK_HOURS
                1300,                                                                                   // COLUMN_SQUARE
                "16.04.2010",                                                                           // COLUMN_OPENING_DATE
                139,                                                                                    // COLUMN_PARKING_PLACES
                0,                                                                                      // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                      // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_ISKROVSKIY,                                            // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_iskrovskiy),   // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                59.914461195001,                                                                            // COLUMN_LATITUDE
                30.459574957672,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 313-63-06",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                542,                                                                                        // COLUMN_SQUARE
                "26.03.2009",                                                                               // COLUMN_OPENING_DATE
                13,                                                                                         // COLUMN_PARKING_PLACES
                0,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_SOLIDARNOSTI,                                          // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_solidarnosti), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                59.917566531773,                                                                            // COLUMN_LATITUDE
                30.496978211639,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 313-45-38",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                685,                                                                                        // COLUMN_SQUARE
                "13.07.2008",                                                                               // COLUMN_OPENING_DATE
                13,                                                                                         // COLUMN_PARKING_PLACES
                0,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_SIZOVA,                                            // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_sizova),   // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                60.006931268248,                                                                        // COLUMN_LATITUDE
                30.274813415344,                                                                        // COLUMN_LONGITUDE
                "+7 (812) 448-81-06",                                                                   // COLUMN_PHONE
                "8:00 - 24:00",                                                                         // COLUMN_WORK_HOURS
                901,                                                                                    // COLUMN_SQUARE
                "27.06.2008",                                                                           // COLUMN_OPENING_DATE
                20,                                                                                     // COLUMN_PARKING_PLACES
                0,                                                                                      // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                      // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_SESTRORETSK_VOLODARSKOGO,                                          // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_sestroretsk_volodarskogo), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                60.095835683606,                                                                                        // COLUMN_LATITUDE
                29.971504186508,                                                                                        // COLUMN_LONGITUDE
                "+7 (812) 363-37-30",                                                                                   // COLUMN_PHONE
                "0:00 - 24:00",                                                                                         // COLUMN_WORK_HOURS
                1300,                                                                                                   // COLUMN_SQUARE
                "11.06.2008",                                                                                           // COLUMN_OPENING_DATE
                17,                                                                                                     // COLUMN_PARKING_PLACES
                0,                                                                                                      // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                                      // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_KRASNOE_SELO_STRELNINSKOE,                                             // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_krasnoe_selo_strelninskoe),    // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                                           // COLUMN_IS_HYPERMARKET
                59.742376385260,                                                                                            // COLUMN_LATITUDE
                30.093284305557,                                                                                            // COLUMN_LONGITUDE
                "+7 (812) 363-37-28",                                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                                             // COLUMN_WORK_HOURS
                860,                                                                                                        // COLUMN_SQUARE
                "30.01.2008",                                                                                               // COLUMN_OPENING_DATE
                26,                                                                                                         // COLUMN_PARKING_PLACES
                0,                                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_RIO,                                           // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                              // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_rio),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                   // COLUMN_IS_HYPERMARKET
                59.877046054062,                                                                    // COLUMN_LATITUDE
                30.358976177246,                                                                    // COLUMN_LONGITUDE
                "+7 (812) 385-41-95",                                                               // COLUMN_PHONE
                "10:00 - 23:00",                                                                    // COLUMN_WORK_HOURS
                10450,                                                                              // COLUMN_SQUARE
                "20.12.2011",                                                                       // COLUMN_OPENING_DATE
                1800,                                                                               // COLUMN_PARKING_PLACES
                33,                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_AKADEMICHESKAYA,                                           // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                          // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_akademicheskaya),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                60.014403507064,                                                                                // COLUMN_LATITUDE
                30.388424884709,                                                                                // COLUMN_LONGITUDE
                "+7 (812) 385-92-34",                                                                           // COLUMN_PHONE
                "8:00 - 24:00",                                                                                 // COLUMN_WORK_HOURS
                11686,                                                                                          // COLUMN_SQUARE
                "09.09.2011",                                                                                   // COLUMN_OPENING_DATE
                350,                                                                                            // COLUMN_PARKING_PLACES
                36,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_SAVUSHKINA,                                            // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_savushkina),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                59.989058782082,                                                                            // COLUMN_LATITUDE
                30.225462661377,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 703-70-04",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                10782,                                                                                      // COLUMN_SQUARE
                "04.12.2013",                                                                               // COLUMN_OPENING_DATE
                904,                                                                                        // COLUMN_PARKING_PLACES
                44,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                        // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_PLANERNAYA,                                            // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_planernaya),   // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                60.005651797084,                                                                            // COLUMN_LATITUDE
                30.236749330688,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 342-58-66",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                792,                                                                                        // COLUMN_SQUARE
                "21.12.2007",                                                                               // COLUMN_OPENING_DATE
                20,                                                                                         // COLUMN_PARKING_PLACES
                0,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_LENSKAYA,                                          // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_lenskaya), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                59.940509416905,                                                                        // COLUMN_LATITUDE
                30.494231957672,                                                                        // COLUMN_LONGITUDE
                "+7 (812) 448-81-65",                                                                   // COLUMN_PHONE
                "8:00 - 24:00",                                                                         // COLUMN_WORK_HOURS
                0,                                                                                      // COLUMN_SQUARE
                "",                                                                                     // COLUMN_OPENING_DATE
                23,                                                                                     // COLUMN_PARKING_PLACES
                0,                                                                                      // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                      // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_BOLSHEVIKOV,                                           // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_bolshevikov),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                59.912023299337,                                                                            // COLUMN_LATITUDE
                30.480107355820,                                                                            // COLUMN_LONGITUDE
                "+7 (812) 703-70-07",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                9351,                                                                                       // COLUMN_SQUARE
                "25.12.2006",                                                                               // COLUMN_OPENING_DATE
                546,                                                                                        // COLUMN_PARKING_PLACES
                0,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                        // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_TIPANOVA,                                          // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_tipanova), // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                59.851192167550,                                                                        // COLUMN_LATITUDE
                30.350267246033,                                                                        // COLUMN_LONGITUDE
                "+7 (812) 703-70-03",                                                                   // COLUMN_PHONE
                "8:00 - 24:00",                                                                         // COLUMN_WORK_HOURS
                5784,                                                                                   // COLUMN_SQUARE
                "17.10.2003",                                                                           // COLUMN_OPENING_DATE
                580,                                                                                    // COLUMN_PARKING_PLACES
                35,                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_HYPERMARKET_OK_ZHUKOVA,                                           // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_hypermarket_ok_zhukova),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                59.858404672257,                                                                        // COLUMN_LATITUDE
                30.228498000000,                                                                        // COLUMN_LONGITUDE
                "+7 (812) 703-79-10",                                                                   // COLUMN_PHONE
                "8:00 - 24:00",                                                                         // COLUMN_WORK_HOURS
                6138,                                                                                   // COLUMN_SQUARE
                "21.05.2003",                                                                           // COLUMN_OPENING_DATE
                538,                                                                                    // COLUMN_PARKING_PLACES
                32,                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                    // COLUMN_SERVICES_SET
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_KOLPINO_TRUDYASHCHIKHSYA,                                          // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_kolpino_trudyashchikhsya), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                59.737312248374,                                                                                        // COLUMN_LATITUDE
                30.571929398148,                                                                                        // COLUMN_LONGITUDE
                "+7 (812) 385-22-04",                                                                                   // COLUMN_PHONE
                "9:00 - 23:00",                                                                                         // COLUMN_WORK_HOURS
                2586,                                                                                                   // COLUMN_SQUARE
                "31.10.2011",                                                                                           // COLUMN_OPENING_DATE
                200,                                                                                                    // COLUMN_PARKING_PLACES
                15,                                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                                      // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ST_PETERSBURG_SUPERMARKET_OK_KOLPINO_TVERSKAYA,                                             // COLUMN_ID
                CITY_ID_ST_PETERSBURG,                                                                              // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_st_petersburg_supermarket_ok_kolpino_tverskaya),    // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                59.740489496449,                                                                                    // COLUMN_LATITUDE
                30.611235634918,                                                                                    // COLUMN_LONGITUDE
                "+7 (812) 463-69-81",                                                                               // COLUMN_PHONE
                "8:00 - 23:00",                                                                                     // COLUMN_WORK_HOURS
                1788,                                                                                               // COLUMN_SQUARE
                "20.05.2006",                                                                                       // COLUMN_OPENING_DATE
                70,                                                                                                 // COLUMN_PARKING_PLACES
                14,                                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                                  // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ASTRAKHAN_HYPERMARKET_OK_ASTRAKHAN_ALIMPIK,                                             // COLUMN_ID
                CITY_ID_ASTRAKHAN,                                                                              // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_astrakhan_hypermarket_ok_astrakhan_alimpik),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                46.338610536925,                                                                                // COLUMN_LATITUDE
                48.023128584656,                                                                                // COLUMN_LONGITUDE
                "+7 (8512) 46-43-01",                                                                           // COLUMN_PHONE
                "8:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                1600,                                                                                           // COLUMN_PARKING_PLACES
                0,                                                                                              // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ASTRAKHAN_SUPERMARKET_OK_ASTRAKHAN_TRI_KOTA,                                            // COLUMN_ID
                CITY_ID_ASTRAKHAN,                                                                              // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_astrakhan_supermarket_ok_astrakhan_tri_kota),   // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                46.359044574446,                                                                                // COLUMN_LATITUDE
                48.069706584656,                                                                                // COLUMN_LONGITUDE
                "+7 (8512) 61-07-84, +7 (8512)61-07-85",                                                        // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                600,                                                                                            // COLUMN_PARKING_PLACES
                17,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                              // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ASTRAKHAN_HYPERMARKET_OK_ASTRAKHAN_VOKZALNAYA,                                          // COLUMN_ID
                CITY_ID_ASTRAKHAN,                                                                              // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_astrakhan_hypermarket_ok_astrakhan_vokzalnaya), // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                46.359854160772,                                                                                // COLUMN_LATITUDE
                48.055369228836,                                                                                // COLUMN_LONGITUDE
                "+7 (8512) 46-42-70, +7 (8512) 46-42-71",                                                       // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                700,                                                                                            // COLUMN_PARKING_PLACES
                20,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_VOLGOGRAD_HYPERMARKET_OK_VOLGOGRAD_DIAMANT,                                             // COLUMN_ID
                CITY_ID_VOLGOGRAD,                                                                              // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_volgograd_hypermarket_ok_volgograd_diamant),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                48.799876441969,                                                                                // COLUMN_LATITUDE
                44.604719934008,                                                                                // COLUMN_LONGITUDE
                "+7 (8442) 968-150",                                                                            // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                780,                                                                                            // COLUMN_PARKING_PLACES
                0,                                                                                              // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_VOLGOGRAD_SUPERMARKET_OK_VOLGOGRAD_PIRAMIDA,                                            // COLUMN_ID
                CITY_ID_VOLGOGRAD,                                                                              // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_volgograd_supermarket_ok_volgograd_piramida),   // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                48.704442124489,                                                                                // COLUMN_LATITUDE
                44.509524508598,                                                                                // COLUMN_LONGITUDE
                "+7 (8442) 968-136",                                                                            // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                337,                                                                                            // COLUMN_PARKING_PLACES
                17,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                              // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_VOLGOGRAD_SUPERMARKET_OK_VOLGOGRAD_VOROSHILOVSKIY,                                          // COLUMN_ID
                CITY_ID_VOLGOGRAD,                                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_volgograd_supermarket_ok_volgograd_voroshilovskiy), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                48.697141727939,                                                                                    // COLUMN_LATITUDE
                44.501582075394,                                                                                    // COLUMN_LONGITUDE
                "+7 (8442) 96-81-76",                                                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                                     // COLUMN_WORK_HOURS
                0,                                                                                                  // COLUMN_SQUARE
                "",                                                                                                 // COLUMN_OPENING_DATE
                750,                                                                                                // COLUMN_PARKING_PLACES
                0,                                                                                                  // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                                  // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_VORONEZH_HYPERMARKET_OK_VORONEZH_GALEREYA_CHIZHOVA,                                             // COLUMN_ID
                CITY_ID_VORONEZH,                                                                                       // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_voronezh_hypermarket_ok_voronezh_galereya_chizhova),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                51.666400655191,                                                                                        // COLUMN_LATITUDE
                39.191978399475,                                                                                        // COLUMN_LONGITUDE
                "+7 (473) 262-93-70",                                                                                   // COLUMN_PHONE
                "9:00 - 23:00",                                                                                         // COLUMN_WORK_HOURS
                0,                                                                                                      // COLUMN_SQUARE
                "",                                                                                                     // COLUMN_OPENING_DATE
                740,                                                                                                    // COLUMN_PARKING_PLACES
                31,                                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_VORONEZH_HYPERMARKET_OK_VORONEZH_SHISHKOVA,                                             // COLUMN_ID
                CITY_ID_VORONEZH,                                                                               // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_voronezh_hypermarket_ok_voronezh_shishkova),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                51.702483128796,                                                                                // COLUMN_LATITUDE
                39.197782822754,                                                                                // COLUMN_LONGITUDE
                "+7 (473) 269-73-29",                                                                           // COLUMN_PHONE
                "8:00 - 24:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                830,                                                                                            // COLUMN_PARKING_PLACES
                52,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_VORONEZH_SUPERMARKET_OK_VORONEZH_ZHUKOVA,                                           // COLUMN_ID
                CITY_ID_VORONEZH,                                                                           // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_voronezh_supermarket_ok_voronezh_zhukova),  // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                51.715667699425,                                                                            // COLUMN_LATITUDE
                39.171163194443,                                                                            // COLUMN_LONGITUDE
                "+7 (473) 206-58-35",                                                                       // COLUMN_PHONE
                "9:00 - 23:00",                                                                             // COLUMN_WORK_HOURS
                0,                                                                                          // COLUMN_SQUARE
                "",                                                                                         // COLUMN_OPENING_DATE
                250,                                                                                        // COLUMN_PARKING_PLACES
                12,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PHARMACY_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_EKATERINBURG_HYPERMARKET_OK_EKATERINBURG_BABUSHKINA,                                            // COLUMN_ID
                CITY_ID_EKATERINBURG,                                                                                   // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_ekaterinburg_hypermarket_ok_ekaterinburg_babushkina),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                56.882254772513,                                                                                        // COLUMN_LATITUDE
                60.631223076721,                                                                                        // COLUMN_LONGITUDE
                "+7 (343) 351-10-20",                                                                                   // COLUMN_PHONE
                "8:00 - 24:00",                                                                                         // COLUMN_WORK_HOURS
                0,                                                                                                      // COLUMN_SQUARE
                "",                                                                                                     // COLUMN_OPENING_DATE
                640,                                                                                                    // COLUMN_PARKING_PLACES
                45,                                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_EKATERINBURG_HYPERMARKET_OK_EKATERINBURG_RADUGA,                                            // COLUMN_ID
                CITY_ID_EKATERINBURG,                                                                               // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_ekaterinburg_hypermarket_ok_ekaterinburg_raduga),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                56.816698022444,                                                                                    // COLUMN_LATITUDE
                60.537099023804,                                                                                    // COLUMN_LONGITUDE
                "+7 (343) 311-41-70",                                                                               // COLUMN_PHONE
                "9:00 - 24:00",                                                                                     // COLUMN_WORK_HOURS
                0,                                                                                                  // COLUMN_SQUARE
                "",                                                                                                 // COLUMN_OPENING_DATE
                1450,                                                                                               // COLUMN_PARKING_PLACES
                50,                                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_IVANOVO_HYPERMARKET_OK_IVANOVO_LEZHNEVSKAYA_TOPOL,                                          // COLUMN_ID
                CITY_ID_IVANOVO,                                                                                    // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_ivanovo_hypermarket_ok_ivanovo_lezhnevskaya_topol), // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                56.983372283481,                                                                                    // COLUMN_LATITUDE
                40.982307068787,                                                                                    // COLUMN_LONGITUDE
                "+7 (4932) 92-07-50",                                                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                                     // COLUMN_WORK_HOURS
                0,                                                                                                  // COLUMN_SQUARE
                "",                                                                                                 // COLUMN_OPENING_DATE
                1000,                                                                                               // COLUMN_PARKING_PLACES
                25,                                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_IRKUTSK_HYPERMARKET_OK_IRKUTSK_KOMSOMOLL,                                           // COLUMN_ID
                CITY_ID_IRKUTSK,                                                                            // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_irkutsk_hypermarket_ok_irkutsk_komsomoll),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                52.268205701729,                                                                            // COLUMN_LATITUDE
                104.289226330690,                                                                           // COLUMN_LONGITUDE
                "+7 (3952) 784-400",                                                                        // COLUMN_PHONE
                "9:00 - 23:00",                                                                             // COLUMN_WORK_HOURS
                0,                                                                                          // COLUMN_SQUARE
                "",                                                                                         // COLUMN_OPENING_DATE
                1200,                                                                                       // COLUMN_PARKING_PLACES
                36,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                        // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_KRASNODAR_HYPERMARKET_OK_KRASNODAR_MINSKAYA,                                            // COLUMN_ID
                CITY_ID_KRASNODAR,                                                                              // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_krasnodar_hypermarket_ok_krasnodar_minskaya),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                45.036868802080,                                                                                // COLUMN_LATITUDE
                38.932500161377,                                                                                // COLUMN_LONGITUDE
                "+7 (861) 273-97-01",                                                                           // COLUMN_PHONE
                "8:00 - 24:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                467,                                                                                            // COLUMN_PARKING_PLACES
                36,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_KRASNODAR_HYPERMARKET_OK_KRASNODAR_MACHUGI,                                             // COLUMN_ID
                CITY_ID_KRASNODAR,                                                                              // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_krasnodar_hypermarket_ok_krasnodar_machugi),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                45.010596184138,                                                                                // COLUMN_LATITUDE
                39.067848500000,                                                                                // COLUMN_LONGITUDE
                "+7 (861) 273-97-50",                                                                           // COLUMN_PHONE
                "8:00 - 24:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                625,                                                                                            // COLUMN_PARKING_PLACES
                39,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_KRASNODAR_HYPERMARKET_OK_KRASNODAR_GALAKTIKA,                                           // COLUMN_ID
                CITY_ID_KRASNODAR,                                                                              // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_krasnodar_hypermarket_ok_krasnodar_galaktika),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                45.029675564542,                                                                                // COLUMN_LATITUDE
                39.046138584656,                                                                                // COLUMN_LONGITUDE
                "+7 (861) 210-24-60",                                                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                1750,                                                                                           // COLUMN_PARKING_PLACES
                0,                                                                                              // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_KRASNODAR_SUPERMARKET_OK_KRASNODAR_BOSS_KHAUZ,                                          // COLUMN_ID
                CITY_ID_KRASNODAR,                                                                              // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_krasnodar_supermarket_ok_krasnodar_boss_khauz), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                45.060221734957,                                                                                // COLUMN_LATITUDE
                38.942461500000,                                                                                // COLUMN_LONGITUDE
                "+7 (861) 210-59-20",                                                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                272,                                                                                            // COLUMN_PARKING_PLACES
                13,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                              // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_KRASNODAR_HYPERMARKET_OK_KRASNODAR_OZ,                                          // COLUMN_ID
                CITY_ID_KRASNODAR,                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_krasnodar_hypermarket_ok_krasnodar_oz), // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                45.011568066803,                                                                        // COLUMN_LATITUDE
                39.121756523804,                                                                        // COLUMN_LONGITUDE
                "+7 (861) 210-50-60",                                                                   // COLUMN_PHONE
                "9:00 - 23:00",                                                                         // COLUMN_WORK_HOURS
                0,                                                                                      // COLUMN_SQUARE
                "",                                                                                     // COLUMN_OPENING_DATE
                2167,                                                                                   // COLUMN_PARKING_PLACES
                0,                                                                                      // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_KRASNOYARSK_HYPERMARKET_OK_KRASNOYARSK_PLANETA,                                             // COLUMN_ID
                CITY_ID_KRASNOYARSK,                                                                                // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_krasnoyarsk_hypermarket_ok_krasnoyarsk_planeta),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                56.050591057505,                                                                                    // COLUMN_LATITUDE
                92.903605023804,                                                                                    // COLUMN_LONGITUDE
                "+7 (391) 274-91-44",                                                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                                     // COLUMN_WORK_HOURS
                0,                                                                                                  // COLUMN_SQUARE
                "",                                                                                                 // COLUMN_OPENING_DATE
                1340,                                                                                               // COLUMN_PARKING_PLACES
                41,                                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_KRASNOYARSK_SUPERMARKET_OK_KRASNOYARSK_KRASNODARSKAYA,                                          // COLUMN_ID
                CITY_ID_KRASNOYARSK,                                                                                    // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_krasnoyarsk_supermarket_ok_krasnoyarsk_krasnodarskaya), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                56.054497600020,                                                                                        // COLUMN_LATITUDE
                92.940323179834,                                                                                        // COLUMN_LONGITUDE
                "+7 (391) 252-76-45",                                                                                   // COLUMN_PHONE
                "9:00 - 23:00",                                                                                         // COLUMN_WORK_HOURS
                0,                                                                                                      // COLUMN_SQUARE
                "",                                                                                                     // COLUMN_OPENING_DATE
                100,                                                                                                    // COLUMN_PARKING_PLACES
                0,                                                                                                      // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                                      // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PHARMACY_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_KRASNOYARSK_HYPERMARKET_OK_KRASNOYARSK_SIBIRSKIY,                                           // COLUMN_ID
                CITY_ID_KRASNOYARSK,                                                                                // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_krasnoyarsk_hypermarket_ok_krasnoyarsk_sibirskiy),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                56.015553659839,                                                                                    // COLUMN_LATITUDE
                92.953806246033,                                                                                    // COLUMN_LONGITUDE
                "+7 (391) 249-63-63",                                                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                                     // COLUMN_WORK_HOURS
                0,                                                                                                  // COLUMN_SQUARE
                "",                                                                                                 // COLUMN_OPENING_DATE
                750,                                                                                                // COLUMN_PARKING_PLACES
                41,                                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_LIPETSK_HYPERMARKET_OK_LIPETSK_EVROPA,                                          // COLUMN_ID
                CITY_ID_LIPETSK,                                                                        // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_lipetsk_hypermarket_ok_lipetsk_evropa), // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                52.605994145220,                                                                        // COLUMN_LATITUDE
                39.577886584656,                                                                        // COLUMN_LONGITUDE
                "+7 (4742) 51-61-52, +7 (4742) 51-61-53",                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                         // COLUMN_WORK_HOURS
                0,                                                                                      // COLUMN_SQUARE
                "",                                                                                     // COLUMN_OPENING_DATE
                600,                                                                                    // COLUMN_PARKING_PLACES
                38,                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_LIPETSK_SUPERMARKET_OK_LIPETSK_PETRA_SMORODINA,                                             // COLUMN_ID
                CITY_ID_LIPETSK,                                                                                    // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_lipetsk_supermarket_ok_lipetsk_petra_smorodina),    // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                52.588580375854,                                                                                    // COLUMN_LATITUDE
                39.535636076721,                                                                                    // COLUMN_LONGITUDE
                "+7 (4742) 90-84-02, +7 (4742) 90-84-04",                                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                                     // COLUMN_WORK_HOURS
                0,                                                                                                  // COLUMN_SQUARE
                "",                                                                                                 // COLUMN_OPENING_DATE
                250,                                                                                                // COLUMN_PARKING_PLACES
                10,                                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                                  // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_MURMANSK_HYPERMARKET_OK_MURMANSK_SHMIDTA,                                           // COLUMN_ID
                CITY_ID_MURMANSK,                                                                           // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_murmansk_hypermarket_ok_murmansk_shmidta),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                68.956825929537,                                                                            // COLUMN_LATITUDE
                33.067488500000,                                                                            // COLUMN_LONGITUDE
                "+7 (8152) 59-63-33",                                                                       // COLUMN_PHONE
                "9:00 - 23:00",                                                                             // COLUMN_WORK_HOURS
                9544,                                                                                       // COLUMN_SQUARE
                "16.07.2008",                                                                               // COLUMN_OPENING_DATE
                630,                                                                                        // COLUMN_PARKING_PLACES
                0,                                                                                          // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                        // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_NIZHNIY_NOVGOROD_HYPERMARKET_OK_NIZHNIY_NOVGOROD_TSEKH_TARY,                                            // COLUMN_ID
                CITY_ID_NIZHNIY_NOVGOROD,                                                                                       // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_nizhniy_novgorod_hypermarket_ok_nizhniy_novgorod_tsekh_tary),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                               // COLUMN_IS_HYPERMARKET
                56.249805167415,                                                                                                // COLUMN_LATITUDE
                43.876383753967,                                                                                                // COLUMN_LONGITUDE
                "+7 (831) 275-85-75",                                                                                           // COLUMN_PHONE
                "8:00 - 24:00",                                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                                              // COLUMN_SQUARE
                "",                                                                                                             // COLUMN_OPENING_DATE
                744,                                                                                                            // COLUMN_PARKING_PLACES
                40,                                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_CAFE_MASK          |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_NIZHNIY_NOVGOROD_HYPERMARKET_OK_NIZHNIY_NOVGOROD_DEREVOOBDELOCHNAYA,                                            // COLUMN_ID
                CITY_ID_NIZHNIY_NOVGOROD,                                                                                               // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_nizhniy_novgorod_hypermarket_ok_nizhniy_novgorod_derevoobdelochnaya),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                                       // COLUMN_IS_HYPERMARKET
                56.301417634814,                                                                                                        // COLUMN_LATITUDE
                43.947733957672,                                                                                                        // COLUMN_LONGITUDE
                "+7 (831) 202-25-00",                                                                                                   // COLUMN_PHONE
                "8:00 - 24:00",                                                                                                         // COLUMN_WORK_HOURS
                0,                                                                                                                      // COLUMN_SQUARE
                "",                                                                                                                     // COLUMN_OPENING_DATE
                750,                                                                                                                    // COLUMN_PARKING_PLACES
                36,                                                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_CAFE_MASK          |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_NIZHNIY_NOVGOROD_HYPERMARKET_OK_NIZHNIY_NOVGOROD_SOVETSKAYA,                                            // COLUMN_ID
                CITY_ID_NIZHNIY_NOVGOROD,                                                                                       // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_nizhniy_novgorod_hypermarket_ok_nizhniy_novgorod_sovetskaya),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                               // COLUMN_IS_HYPERMARKET
                56.296356816575,                                                                                                // COLUMN_LATITUDE
                44.041981880981,                                                                                                // COLUMN_LONGITUDE
                "+7 (831) 202-26-46",                                                                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                                              // COLUMN_SQUARE
                "",                                                                                                             // COLUMN_OPENING_DATE
                1200,                                                                                                           // COLUMN_PARKING_PLACES
                32,                                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_CAFE_MASK          |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_NOVOSIBIRSK_HYPERMARKET_OK_NOVOSIBIRSK_AURA,                                            // COLUMN_ID
                CITY_ID_NOVOSIBIRSK,                                                                            // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_novosibirsk_hypermarket_ok_novosibirsk_aura),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                55.028746344659,                                                                                // COLUMN_LATITUDE
                82.936600161377,                                                                                // COLUMN_LONGITUDE
                "+7 (383) 298-90-72",                                                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                1300,                                                                                           // COLUMN_PARKING_PLACES
                23,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_NOVOSIBIRSK_HYPERMARKET_OK_NOVOSIBIRSK_MALINKA,                                             // COLUMN_ID
                CITY_ID_NOVOSIBIRSK,                                                                                // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_novosibirsk_hypermarket_ok_novosibirsk_malinka),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                55.049301732278,                                                                                    // COLUMN_LATITUDE
                82.882172769897,                                                                                    // COLUMN_LONGITUDE
                "+7 (383) 210-65-25",                                                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                                     // COLUMN_WORK_HOURS
                0,                                                                                                  // COLUMN_SQUARE
                "",                                                                                                 // COLUMN_OPENING_DATE
                400,                                                                                                // COLUMN_PARKING_PLACES
                22,                                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_NOVOCHERKASSK_SUPERMARKET_OK_NOVOCHERKASSK_MAGNITNYY,                                           // COLUMN_ID
                CITY_ID_NOVOCHERKASSK,                                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_novocherkassk_supermarket_ok_novocherkassk_magnitnyy),  // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                47.425313798076,                                                                                        // COLUMN_LATITUDE
                40.054446330688,                                                                                        // COLUMN_LONGITUDE
                "+7 (8635) 21-01-58, +7 (8635) 21-01-59",                                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                                         // COLUMN_WORK_HOURS
                0,                                                                                                      // COLUMN_SQUARE
                "",                                                                                                     // COLUMN_OPENING_DATE
                100,                                                                                                    // COLUMN_PARKING_PLACES
                0,                                                                                                      // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                                      // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_OMSK_HYPERMARKET_OK_OMSK_EHNTUZIASTOV,                                          // COLUMN_ID
                CITY_ID_OMSK,                                                                           // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_omsk_hypermarket_ok_omsk_ehntuziastov), // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                55.048134914124,                                                                        // COLUMN_LATITUDE
                73.291397391693,                                                                        // COLUMN_LONGITUDE
                "+7 (3812) 29-28-10",                                                                   // COLUMN_PHONE
                "9:00 - 23:00",                                                                         // COLUMN_WORK_HOURS
                0,                                                                                      // COLUMN_SQUARE
                "",                                                                                     // COLUMN_OPENING_DATE
                800,                                                                                    // COLUMN_PARKING_PLACES
                45,                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_OMSK_SUPERMARKET_OK_OMSK_70_LET_OKTYABRYA,                                          // COLUMN_ID
                CITY_ID_OMSK,                                                                               // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_omsk_supermarket_ok_omsk_70_let_oktyabrya), // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                54.985295243542,                                                                            // COLUMN_LATITUDE
                73.310843415344,                                                                            // COLUMN_LONGITUDE
                "+7 (3812) 35-62-16",                                                                       // COLUMN_PHONE
                "9:00 - 23.00",                                                                             // COLUMN_WORK_HOURS
                0,                                                                                          // COLUMN_SQUARE
                "",                                                                                         // COLUMN_OPENING_DATE
                300,                                                                                        // COLUMN_PARKING_PLACES
                10,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ORENBURG_HYPERMARKET_OK_ORENBURG_SALMYSHSKAYA,                                          // COLUMN_ID
                CITY_ID_ORENBURG,                                                                               // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_orenburg_hypermarket_ok_orenburg_salmyshskaya), // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                51.812585190553,                                                                                // COLUMN_LATITUDE
                55.181281208984,                                                                                // COLUMN_LONGITUDE
                "+7 (3532) 54-12-99",                                                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                1900,                                                                                           // COLUMN_PARKING_PLACES
                35,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ROSTOV_ON_DON_HYPERMARKET_OK_ROSTOV_ON_DON_MALINOVSKOGO,                                            // COLUMN_ID
                CITY_ID_ROSTOV_ON_DON,                                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_rostov_on_don_hypermarket_ok_rostov_on_don_malinovskogo),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                           // COLUMN_IS_HYPERMARKET
                47.228883298224,                                                                                            // COLUMN_LATITUDE
                39.610436042328,                                                                                            // COLUMN_LONGITUDE
                "+7 (863) 204-03-00",                                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                                             // COLUMN_WORK_HOURS
                0,                                                                                                          // COLUMN_SQUARE
                "",                                                                                                         // COLUMN_OPENING_DATE
                826,                                                                                                        // COLUMN_PARKING_PLACES
                50,                                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                        // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_ROSTOV_ON_DON_HYPERMARKET_OK_ROSTOV_ON_DON_KOMAROVA,                                            // COLUMN_ID
                CITY_ID_ROSTOV_ON_DON,                                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_rostov_on_don_hypermarket_ok_rostov_on_don_komarova),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                47.288683598212,                                                                                        // COLUMN_LATITUDE
                39.707071738098,                                                                                        // COLUMN_LONGITUDE
                "+7 (863) 237-01-30",                                                                                   // COLUMN_PHONE
                "8:00 - 24:00",                                                                                         // COLUMN_WORK_HOURS
                0,                                                                                                      // COLUMN_SQUARE
                "",                                                                                                     // COLUMN_OPENING_DATE
                500,                                                                                                    // COLUMN_PARKING_PLACES
                42,                                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_SARATOV_HYPERMARKET_OK_SARATOV_KHEHPPI_MOLL,                                            // COLUMN_ID
                CITY_ID_SARATOV,                                                                                // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_saratov_hypermarket_ok_saratov_khehppi_moll),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                51.621368150319,                                                                                // COLUMN_LATITUDE
                45.972443500000,                                                                                // COLUMN_LONGITUDE
                "+7 (8452) 669-367, +7 (8452) 669-368",                                                         // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                1500,                                                                                           // COLUMN_PARKING_PLACES
                43,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_SARATOV_SUPERMARKET_OK_SARATOV_TANKISTOV,                                           // COLUMN_ID
                CITY_ID_SARATOV,                                                                            // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_saratov_supermarket_ok_saratov_tankistov),  // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                51.547004916837,                                                                            // COLUMN_LATITUDE
                46.014904653442,                                                                            // COLUMN_LONGITUDE
                "+7 (8452) 66-93-56",                                                                       // COLUMN_PHONE
                "9:00 - 23:00",                                                                             // COLUMN_WORK_HOURS
                0,                                                                                          // COLUMN_SQUARE
                "",                                                                                         // COLUMN_OPENING_DATE
                60,                                                                                         // COLUMN_PARKING_PLACES
                12,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                          // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK |
                        SERVICE_BAKERY_MASK      |
                        SERVICE_COOKERY_MASK     |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_SOCHI_HYPERMARKET_OK_SOCHI_MOREMOLL,                                            // COLUMN_ID
                CITY_ID_SOCHI,                                                                          // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_sochi_hypermarket_ok_sochi_moremoll),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                       // COLUMN_IS_HYPERMARKET
                43.606340353254,                                                                        // COLUMN_LATITUDE
                39.732571992065,                                                                        // COLUMN_LONGITUDE
                "+7 (862) 269-59-25",                                                                   // COLUMN_PHONE
                "9:00 - 23:00",                                                                         // COLUMN_WORK_HOURS
                0,                                                                                      // COLUMN_SQUARE
                "",                                                                                     // COLUMN_OPENING_DATE
                2100,                                                                                   // COLUMN_PARKING_PLACES
                35,                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_STAVROPOL_HYPERMARKET_OK_STAVROPOL_DOVATORTSEV,                                             // COLUMN_ID
                CITY_ID_STAVROPOL,                                                                                  // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_stavropol_hypermarket_ok_stavropol_dovatortsev),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                45.005013690105,                                                                                    // COLUMN_LATITUDE
                41.926005330688,                                                                                    // COLUMN_LONGITUDE
                "+7 (8652) 95-15-56",                                                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                                     // COLUMN_WORK_HOURS
                0,                                                                                                  // COLUMN_SQUARE
                "",                                                                                                 // COLUMN_OPENING_DATE
                200,                                                                                                // COLUMN_PARKING_PLACES
                15,                                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                // COLUMN_SERVICES_SET
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_STERLITAMAK_HYPERMARKET_OK_STERLITAMAK_KHUDAYBERDINA,                                           // COLUMN_ID
                CITY_ID_STERLITAMAK,                                                                                    // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_sterlitamak_hypermarket_ok_sterlitamak_khudayberdina),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                53.628641454057,                                                                                        // COLUMN_LATITUDE
                55.940569653442,                                                                                        // COLUMN_LONGITUDE
                "+7 (3472) 26-07-50",                                                                                   // COLUMN_PHONE
                "9:00 - 23:00",                                                                                         // COLUMN_WORK_HOURS
                0,                                                                                                      // COLUMN_SQUARE
                "",                                                                                                     // COLUMN_OPENING_DATE
                1400,                                                                                                   // COLUMN_PARKING_PLACES
                39,                                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                    // COLUMN_SERVICES_SET
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_SURGUT_HYPERMARKET_OK_SURGUT_SITI_MOLL,                                             // COLUMN_ID
                CITY_ID_SURGUT,                                                                             // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_surgut_hypermarket_ok_surgut_siti_moll),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                61.239359441378,                                                                            // COLUMN_LATITUDE
                73.373528177246,                                                                            // COLUMN_LONGITUDE
                "+7 (3462) 27-03-50",                                                                       // COLUMN_PHONE
                "9:00 - 23:00",                                                                             // COLUMN_WORK_HOURS
                0,                                                                                          // COLUMN_SQUARE
                "",                                                                                         // COLUMN_OPENING_DATE
                5000,                                                                                       // COLUMN_PARKING_PLACES
                43,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                        // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_SURGUT_HYPERMARKET_OK_SURGUT_NEFTEYUGANSKOE_AURA,                                           // COLUMN_ID
                CITY_ID_SURGUT,                                                                                     // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_surgut_hypermarket_ok_surgut_nefteyuganskoe_aura),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                61.277458438844,                                                                                    // COLUMN_LATITUDE
                73.365989992065,                                                                                    // COLUMN_LONGITUDE
                "+7 (3462) 27-04-00",                                                                               // COLUMN_PHONE
                "9:00 - 24:00",                                                                                     // COLUMN_WORK_HOURS
                0,                                                                                                  // COLUMN_SQUARE
                "",                                                                                                 // COLUMN_OPENING_DATE
                2500,                                                                                               // COLUMN_PARKING_PLACES
                34,                                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_SYKTYVKAR_HYPERMARKET_OK_SYKTYVKAR_OKTYABRSKIY_IYUN,                                            // COLUMN_ID
                CITY_ID_SYKTYVKAR,                                                                                      // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_syktyvkar_hypermarket_ok_syktyvkar_oktyabrskiy_iyun),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                61.690279365307,                                                                                        // COLUMN_LATITUDE
                50.803261822754,                                                                                        // COLUMN_LONGITUDE
                "+7 (8212) 250-855, +7 (8212) 250-825 - блюда на заказ",                                                // COLUMN_PHONE
                "9:00 - 23:00",                                                                                         // COLUMN_WORK_HOURS
                0,                                                                                                      // COLUMN_SQUARE
                "",                                                                                                     // COLUMN_OPENING_DATE
                1500,                                                                                                   // COLUMN_PARKING_PLACES
                33,                                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                    // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_TOLYATTI_HYPERMARKET_OK_TOLYATTI_BORKOVSKAYA,                                           // COLUMN_ID
                CITY_ID_TOLYATTI,                                                                               // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_tolyatti_hypermarket_ok_tolyatti_borkovskaya),  // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                53.543240285064,                                                                                // COLUMN_LATITUDE
                49.287868669311,                                                                                // COLUMN_LONGITUDE
                "+7 (8482) 54-00-01, +7 (8482) 54-00-02, +7 (8482) 53-59-99",                                   // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                0,                                                                                              // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                650,                                                                                            // COLUMN_PARKING_PLACES
                49,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_CAFE_MASK          |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_TOLYATTI_SUPERMARKET_OK_TOLYATTI_SPORTIVNAYA_MALINA,                                            // COLUMN_ID
                CITY_ID_TOLYATTI,                                                                                       // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_tolyatti_supermarket_ok_tolyatti_sportivnaya_malina),   // COLUMN_NAME
                SHOP_SUPERMARKET,                                                                                       // COLUMN_IS_HYPERMARKET
                53.499137931645,                                                                                        // COLUMN_LATITUDE
                49.288065246033,                                                                                        // COLUMN_LONGITUDE
                "+7 (8482) 94-01-10, +7 (8482) 94-01-13",                                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                                         // COLUMN_WORK_HOURS
                0,                                                                                                      // COLUMN_SQUARE
                "",                                                                                                     // COLUMN_OPENING_DATE
                300,                                                                                                    // COLUMN_PARKING_PLACES
                14,                                                                                                     // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK |                                                                      // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_TYUMEN_HYPERMARKET_OK_TYUMEN_SHIROTNAYA,                                            // COLUMN_ID
                CITY_ID_TYUMEN,                                                                             // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_tyumen_hypermarket_ok_tyumen_shirotnaya),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                           // COLUMN_IS_HYPERMARKET
                57.096376130745,                                                                            // COLUMN_LATITUDE
                65.624680500000,                                                                            // COLUMN_LONGITUDE
                "+7 (3452) 55-07-10",                                                                       // COLUMN_PHONE
                "8:00 - 24:00",                                                                             // COLUMN_WORK_HOURS
                0,                                                                                          // COLUMN_SQUARE
                "",                                                                                         // COLUMN_OPENING_DATE
                700,                                                                                        // COLUMN_PARKING_PLACES
                56,                                                                                         // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                        // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_TYUMEN_HYPERMARKET_OK_TYUMEN_FEDYUNINSKOGO_OSTROV,                                          // COLUMN_ID
                CITY_ID_TYUMEN,                                                                                     // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_tyumen_hypermarket_ok_tyumen_fedyuninskogo_ostrov), // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                                   // COLUMN_IS_HYPERMARKET
                57.102919916569,                                                                                    // COLUMN_LATITUDE
                65.570567161377,                                                                                    // COLUMN_LONGITUDE
                "+7 (3452) 79-04-20",                                                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                                     // COLUMN_WORK_HOURS
                0,                                                                                                  // COLUMN_SQUARE
                "",                                                                                                 // COLUMN_OPENING_DATE
                900,                                                                                                // COLUMN_PARKING_PLACES
                22,                                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                                // COLUMN_SERVICES_SET
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_UFA_HYPERMARKET_OK_UFA_ZHUKOVA,                                             // COLUMN_ID
                CITY_ID_UFA,                                                                        // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_ufa_hypermarket_ok_ufa_zhukova),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                   // COLUMN_IS_HYPERMARKET
                54.777880008277,                                                                    // COLUMN_LATITUDE
                56.067239330688,                                                                    // COLUMN_LONGITUDE
                "+7 (347) 292-65-65, +7 (347) 292-65-56",                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                     // COLUMN_WORK_HOURS
                0,                                                                                  // COLUMN_SQUARE
                "",                                                                                 // COLUMN_OPENING_DATE
                683,                                                                                // COLUMN_PARKING_PLACES
                39,                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                // COLUMN_SERVICES_SET
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

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_UFA_HYPERMARKET_OK_UFA_IYUN,                                            // COLUMN_ID
                CITY_ID_UFA,                                                                    // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_ufa_hypermarket_ok_ufa_iyun),   // COLUMN_NAME
                SHOP_HYPERMARKET,                                                               // COLUMN_IS_HYPERMARKET
                54.752605248202,                                                                // COLUMN_LATITUDE
                56.008103568787,                                                                // COLUMN_LONGITUDE
                "+7 (347) 226-07-27, +7 (347) 226-07-37",                                       // COLUMN_PHONE
                "9:00 - 23:00",                                                                 // COLUMN_WORK_HOURS
                0,                                                                              // COLUMN_SQUARE
                "",                                                                             // COLUMN_OPENING_DATE
                425,                                                                            // COLUMN_PARKING_PLACES
                22,                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                            // COLUMN_SERVICES_SET
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_UFA_HYPERMARKET_OK_UFA_PLANETA,                                             // COLUMN_ID
                CITY_ID_UFA,                                                                        // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_ufa_hypermarket_ok_ufa_planeta),    // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                   // COLUMN_IS_HYPERMARKET
                54.757090266072,                                                                    // COLUMN_LATITUDE
                56.035488261902,                                                                    // COLUMN_LONGITUDE
                "+7 (347) 226-95-50",                                                               // COLUMN_PHONE
                "9:00 - 23:00",                                                                     // COLUMN_WORK_HOURS
                0,                                                                                  // COLUMN_SQUARE
                "",                                                                                 // COLUMN_OPENING_DATE
                3200,                                                                               // COLUMN_PARKING_PLACES
                36,                                                                                 // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
                        SERVICE_COOKERY_MASK       |
                        SERVICE_TAXI_ORDERING_MASK |
                        SERVICE_PHARMACY_MASK      |
                        SERVICE_ORDERING_FOOD_MASK |
                        SERVICE_DEGUSTATION_MASK   |
                        SERVICE_GIFT_CARDS_MASK    |
                        SERVICE_PARKING_MASK
        );

        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,
                SHOP_ID_CHEREPOVETS_HYPERMARKET_OK_CHEREPOVETS_RAAKHE,                                          // COLUMN_ID
                CITY_ID_CHEREPOVETS,                                                                            // COLUMN_CITY_ID
                mContext.getResources().getString(R.string.shop_cherepovets_hypermarket_ok_cherepovets_raakhe), // COLUMN_NAME
                SHOP_HYPERMARKET,                                                                               // COLUMN_IS_HYPERMARKET
                59.103902762157,                                                                                // COLUMN_LATITUDE
                37.910383992065,                                                                                // COLUMN_LONGITUDE
                "+7 (8202) 20-19-60",                                                                           // COLUMN_PHONE
                "9:00 - 23:00",                                                                                 // COLUMN_WORK_HOURS
                13927,                                                                                          // COLUMN_SQUARE
                "",                                                                                             // COLUMN_OPENING_DATE
                600,                                                                                            // COLUMN_PARKING_PLACES
                44,                                                                                             // COLUMN_NUMBER_OF_CASHBOXES
                SERVICE_CLEARING_SETTLEMENT_MASK   |                                                            // COLUMN_SERVICES_SET
                        SERVICE_COSMETICS_MASK     |
                        SERVICE_PLAYGROUND_MASK    |
                        SERVICE_FISH_ISLAND_MASK   |
                        SERVICE_BAKERY_MASK        |
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
            builder.append(String.valueOf(values[i]).replace("\'", "\'\'").replace("\"", "\"\""));
            builder.append("\'");
        }

        builder.append(");");

        db.execSQL(builder.toString());
    }

    public String[] getCities(SQLiteDatabase db)
    {
        Cursor cursor = db.query(CITIES_TABLE_NAME, new String[] { COLUMN_NAME }, null, null, null, null, null);
        int nameColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME);

        String[] res = new String[cursor.getCount()];

        int i = 0;
        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            res[i] = cursor.getString(nameColumnIndex);

            ++i;
            cursor.moveToNext();
        }

        cursor.close();

        return res;
    }

    public int getCityId(String city)
    {
        for (int i = 0; i < CITIES.length; ++i)
        {
            if (city.equals(CITIES[i]))
            {
                return i + 1;
            }
        }

        return -1;
    }

    public ArrayList<ShopInfo> getShops(SQLiteDatabase db, int cityId)
    {
        ArrayList<ShopInfo> res = new ArrayList<>();



        Cursor cursor;

        if (cityId > 0)
        {
            cursor = db.query(SHOPS_TABLE_NAME, SHOPS_COLUMNS, COLUMN_CITY_ID + " = ?", new String[] { String.valueOf(cityId) }, null, null, null);
        }
        else
        {
            cursor = db.query(SHOPS_TABLE_NAME, SHOPS_COLUMNS, null, null, null, null, null);
        }



        int idColumnIndex                = cursor.getColumnIndexOrThrow(COLUMN_ID);
        int cityIdColumnIndex            = cursor.getColumnIndexOrThrow(COLUMN_CITY_ID);
        int nameColumnIndex              = cursor.getColumnIndexOrThrow(COLUMN_NAME);
        int isHypermarketColumnIndex     = cursor.getColumnIndexOrThrow(COLUMN_IS_HYPERMARKET);
        int latitudeColumnIndex          = cursor.getColumnIndexOrThrow(COLUMN_LATITUDE);
        int longitudeColumnIndex         = cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE);
        int phoneColumnIndex             = cursor.getColumnIndexOrThrow(COLUMN_PHONE);
        int workHoursColumnIndex         = cursor.getColumnIndexOrThrow(COLUMN_WORK_HOURS);
        int squareColumnIndex            = cursor.getColumnIndexOrThrow(COLUMN_SQUARE);
        int openingDateColumnIndex       = cursor.getColumnIndexOrThrow(COLUMN_OPENING_DATE);
        int parkingPlacesColumnIndex     = cursor.getColumnIndexOrThrow(COLUMN_PARKING_PLACES);
        int numberOfCashboxesColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_NUMBER_OF_CASHBOXES);
        int servicesSetColumnIndex       = cursor.getColumnIndexOrThrow(COLUMN_SERVICES_SET);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);



        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            ShopInfo shop = new ShopInfo();

            shop.setId(cursor.getInt(idColumnIndex));
            shop.setCityId(cursor.getInt(cityIdColumnIndex));
            shop.setName(cursor.getString(nameColumnIndex));
            shop.setIsHypermarket(cursor.getInt(isHypermarketColumnIndex) == SHOP_HYPERMARKET);
            shop.setLatitude(cursor.getDouble(latitudeColumnIndex));
            shop.setLongitude(cursor.getDouble(longitudeColumnIndex));
            shop.setPhone(cursor.getString(phoneColumnIndex));
            shop.setWorkHours(cursor.getString(workHoursColumnIndex));
            shop.setSquare(cursor.getInt(squareColumnIndex));

            try
            {
                shop.setOpeningDate(dateFormat.parse(cursor.getString(openingDateColumnIndex)));
            }
            catch (ParseException e)
            {
                shop.setOpeningDate(null);
            }

            shop.setParkingPlaces(cursor.getInt(parkingPlacesColumnIndex));
            shop.setNumberOfCashboxes(cursor.getInt(numberOfCashboxesColumnIndex));
            shop.setServicesSet(cursor.getInt(servicesSetColumnIndex));

            res.add(shop);

            cursor.moveToNext();
        }

        cursor.close();



        return res;
    }
}
