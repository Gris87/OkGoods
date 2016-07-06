package ru.okmarket.okgoods.net;

import java.util.ArrayList;

import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.db.entities.GoodEntity;
import ru.okmarket.okgoods.db.entities.GoodsCategoryEntity;
import ru.okmarket.okgoods.util.AppLog;

public class Web
{
    @SuppressWarnings("unused")
    private static final String TAG = "Web";



    private static final String OK_MARKET_RU_URL     = "http://okmarket.ru";
    private static final String OKEY_DOSTAVKA_RU_URL = "https://www.okeydostavka.ru";

    public static final String[] OKEY_DOSTAVKA_RU_SHOPS = {
              "spb"
            , "spb1"
            , "spb2"
            , "spb3"
            , "msk"
            , "msk1"
            , "msk2"
    };

    public static final int[] OKEY_DOSTAVKA_RU_SHOP_IDS = {
              10653
            , 10654
            , 10655
            , 10656
            , 10151
            , 10651
            , 10652
    };



    public static String getShopUrl(int shopId)
    {
        return OK_MARKET_RU_URL + "/stores/" + String.valueOf(shopId) + "/";
    }

    public static ArrayList<String> getShopPhotosUrlsFromResponse(String response)
    {
        ArrayList<String> res = new ArrayList<>();

        int index = -1;

        do
        {
            index = response.indexOf("<img src=\"", index + 1);

            if (index < 0)
            {
                break;
            }

            int index2 = response.indexOf('>', index + 10);

            if (index2 < 0)
            {
                break;
            }

            String imageTag = response.substring(index, index2 + 1);
            index = index2 + 1;

            if (imageTag.contains("id=\"sd-gallery"))
            {
                index2 = imageTag.indexOf("\"", 10);

                if (index2 >= 0)
                {
                    res.add(OK_MARKET_RU_URL + imageTag.substring(10, index2));
                }
            }
        } while (true);

        return res;
    }

    public static String getFirstShopPhotoUrlFromResponse(String response)
    {
        int index = -1;

        do
        {
            index = response.indexOf("<img src=\"", index + 1);

            if (index < 0)
            {
                break;
            }

            int index2 = response.indexOf('>', index + 10);

            if (index2 < 0)
            {
                break;
            }

            String imageTag = response.substring(index, index2 + 1);
            index = index2 + 1;

            if (imageTag.contains("id=\"sd-gallery"))
            {
                index2 = imageTag.indexOf("\"", 10);

                if (index2 >= 0)
                {
                    return OK_MARKET_RU_URL + imageTag.substring(10, index2);
                }
            }
        } while (true);

        return null;
    }

    public static String getCatalogUrl(String shop, int shopId, int categoryId)
    {
        if (categoryId == MainDatabase.SPECIAL_ID_ROOT)
        {
            return OKEY_DOSTAVKA_RU_URL + "/" + shop + "/catalog";
        }
        else
        {
            return OKEY_DOSTAVKA_RU_URL + "/webapp/wcs/stores/servlet/CategoryDisplay?" +
                    "storeId=" + String.valueOf(shopId)        + "&" +
                    "urlLangId=-20"                            + "&" +
                    "beginIndex=0"                             + "&" +
                    "urlRequestType=Base"                      + "&" +
                    "categoryId=" + String.valueOf(categoryId) + "&" +
                    "pageView=grid"                            + "&" +
                    "langId=-20"                               + "&" +
                    "catalogId=12051";
        }
    }

    public static String getCategoryPhotoUrl(String fileName)
    {
        return OKEY_DOSTAVKA_RU_URL + "/wcsstore/OKMarketCAS/categories/" + fileName;
    }

    public static String getGoodPhotoThumbnailUrl(int imageId)
    {
        return OKEY_DOSTAVKA_RU_URL + "/wcsstore/OKMarketCAS/cat_entries/" + String.valueOf(imageId) + "/" + String.valueOf(imageId) + "_thumbnail.jpg";
    }

    public static String getGoodPhotoFullImageUrl(int imageId)
    {
        return OKEY_DOSTAVKA_RU_URL + "/wcsstore/OKMarketCAS/cat_entries/" + String.valueOf(imageId) + "/" + String.valueOf(imageId) + "_fullimage.jpg" ;
    }

    public static void getCatalogItemsFromResponse(String response, ArrayList<GoodsCategoryEntity> categories, ArrayList<GoodEntity> goods)
    {
        AppLog.e(TAG, String.valueOf(response.length()));
    }
}
