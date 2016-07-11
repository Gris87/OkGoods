package ru.okmarket.okgoods.net;

import android.text.TextUtils;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

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



    private static int PAGE_START_POINT = 292927;



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
                index2 = imageTag.indexOf('\"', 10);

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
                index2 = imageTag.indexOf('\"', 10);

                if (index2 >= 0)
                {
                    return OK_MARKET_RU_URL + imageTag.substring(10, index2);
                }
            }
        } while (true);

        return null;
    }

    public static String getCatalogUrl(String shop, int shopId, int categoryId, int pageIndex)
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
        return OKEY_DOSTAVKA_RU_URL + "/wcsstore/OKMarketCAS/" + fileName;
    }

    public static String getGoodPhotoThumbnailUrl(int imageId)
    {
        return OKEY_DOSTAVKA_RU_URL + "/wcsstore/OKMarketCAS/cat_entries/" + String.valueOf(imageId) + "/" + String.valueOf(imageId) + "_thumbnail.jpg";
    }

    public static String getGoodPhotoFullImageUrl(int imageId)
    {
        return OKEY_DOSTAVKA_RU_URL + "/wcsstore/OKMarketCAS/cat_entries/" + String.valueOf(imageId) + "/" + String.valueOf(imageId) + "_fullimage.jpg" ;
    }

    public static int getCatalogItemsFromResponse(String response, ArrayList<GoodsCategoryEntity> categories, ArrayList<GoodEntity> goods, String shop, int shopId, int parentCategoryId, int pageIndex)
    {
        int startPoint = response.indexOf("<div class=\"rowContainer\"", PAGE_START_POINT);

        if (startPoint < 0)
        {
            startPoint = response.lastIndexOf("<div class=\"rowContainer\"", PAGE_START_POINT);

            if (startPoint >= 0)
            {
                AppLog.wtf(TAG, "Please move page start point to " + String.valueOf(startPoint) + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                PAGE_START_POINT = startPoint;
            }
            else
            {
                AppLog.wtf(TAG, "Failed to find start point for parsing categories and goods | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                return -1;
            }
        }

        try
        {
            int index = startPoint;

            do
            {
                index = response.indexOf("<div class=\"col-xs-3 col-lg-2 col-xl-special\">", index + 1);

                if (index < 0)
                {
                    break;
                }

                int    categoryId   = MainDatabase.SPECIAL_ID_NONE;
                String categoryName = null;
                String imageName    = null;

                int i = index + 46;

                while (i < response.length())
                {
                    if (response.startsWith("<img src=\"", i))
                    {
                        int index2 = response.indexOf('\"', i + 10);

                        if (index2 < 0)
                        {
                            AppLog.wtf(TAG, "Failed to get category image name from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            return -1;
                        }

                        imageName = response.substring(i + 10, index2);

                        if (imageName.regionMatches(true, 0, "/wcsstore/OKMarketCAS/", 0, 22))
                        {
                            imageName = URLEncoder.encode(imageName.substring(22), "UTF-8").replaceAll("%2F", "/").replaceAll("\\+", "%20");
                        }
                        else
                        {
                            AppLog.wtf(TAG, "Invalid image url: " + imageName + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            imageName = "";
                        }

                        i = index2 + 1;
                        break;
                    }
                    else
                    if (response.startsWith("</div>", i))
                    {
                        if (!response.startsWith("</a>", i - 4))
                        {
                            AppLog.wtf(TAG, "Unexpected closure for tag div | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            return -1;
                        }
                    }

                    ++i;
                }

                while (i < response.length())
                {
                    if (response.startsWith("href=\"", i))
                    {
                        int index2 = response.indexOf('\"', i + 6);

                        if (index2 < 0)
                        {
                            AppLog.wtf(TAG, "Failed to get category link from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            return -1;
                        }

                        String categoryLink  = response.substring(i + 6, index2);
                        String categoryIdStr = null;

                        if (categoryLink.endsWith("-20"))
                        {
                            int index3 = categoryLink.lastIndexOf('-', categoryLink.length() - 4);

                            if (index3 >= 0)
                            {
                                categoryIdStr = categoryLink.substring(index3 + 1, categoryLink.length() - 3);
                            }
                        }
                        else
                        {
                            int index3 = categoryLink.indexOf("categoryId=");

                            if (index3 >= 0)
                            {
                                int index4 = categoryLink.indexOf("&amp;", index3 + 11);

                                if (index4 < 0)
                                {
                                    AppLog.wtf(TAG, "Failed to get category id from category link: " + categoryLink + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                                    return -1;
                                }

                                categoryIdStr = categoryLink.substring(index3 + 11, index4);
                            }
                        }

                        try
                        {
                            categoryId = Integer.parseInt(categoryIdStr);
                        }
                        catch (Exception e)
                        {
                            // Nothing
                        }

                        i = index2 + 1;
                        break;
                    }
                    else
                    if (response.startsWith("</div>", i))
                    {
                        if (!response.startsWith("</a>", i - 4))
                        {
                            AppLog.wtf(TAG, "Unexpected closure for tag div | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            return -1;
                        }
                    }

                    ++i;
                }

                while (i < response.length())
                {
                    if (response.charAt(i) == '>')
                    {
                        int index2 = response.indexOf('<', i + 1);

                        if (index2 < 0)
                        {
                            AppLog.wtf(TAG, "Failed to get category name from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            return -1;
                        }

                        categoryName = response.substring(i + 1, index2);

                        i = index2 + 1;
                        break;
                    }
                    else
                    if (response.startsWith("</div>", i))
                    {
                        if (!response.startsWith("</a>", i - 4))
                        {
                            AppLog.wtf(TAG, "Unexpected closure for tag div | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            return -1;
                        }
                    }

                    ++i;
                }

                index = i;

                if (categoryId != MainDatabase.SPECIAL_ID_NONE)
                {
                    boolean found = false;

                    for (int j = 0; j < categories.size(); ++j)
                    {
                        if (categories.get(j).getId() == categoryId)
                        {
                            found = true;
                            break;
                        }
                    }

                    if (!found)
                    {
                        GoodsCategoryEntity category = new GoodsCategoryEntity();

                        category.setId(categoryId);
                        category.setParentId(parentCategoryId);
                        category.setName(categoryName);
                        category.setImageName(imageName);
                        category.setUpdateTime(0);
                        category.setExpanded(false);
                        category.setEnabled(MainDatabase.ENABLED);

                        categories.add(category);
                    }
                }
            } while (true);
        }
        catch (Exception e)
        {
            AppLog.e(TAG, "Failed to parse categories | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex), e);
        }



        try
        {
            int index = startPoint;

            do
            {
                index = response.indexOf("<div class=\"product ok-theme\">", index + 1);

                if (index < 0)
                {
                    break;
                }

                int divLevel = 1;

                int    goodId             = MainDatabase.SPECIAL_ID_NONE;
                String goodName           = null;
                int    goodImageId        = 0;
                double goodCost           = 0;
                double goodUnit           = 0;
                int    goodUnitType       = MainDatabase.UNIT_TYPE_NOTHING;
                double goodCountIncrement = 0;
                int    goodCountType      = MainDatabase.UNIT_TYPE_NOTHING;
                String goodBrand          = null;
                int    goodEnabled        = MainDatabase.DISABLED;

                int i = index + 46;

                while (i < response.length())
                {
                    if (response.startsWith("<div class=\"product_name\">", i))
                    {
                        ++divLevel;

                        int index2 = response.indexOf("title=\"", i + 26);

                        if (index2 < 0)
                        {
                            AppLog.wtf(TAG, "Failed to get good name from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            return -1;
                        }

                        int index3 = response.indexOf('\"', index2 + 7);

                        if (index3 < 0)
                        {
                            AppLog.wtf(TAG, "Failed to get good name from line: " + response.substring(index2, index2 + 30) + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            return -1;
                        }

                        goodName = response.substring(index2 + 7, index3);

                        i = index3 + 1;
                        break;
                    }
                    else
                    if (response.startsWith("<div", i))
                    {
                        ++divLevel;
                    }
                    else
                    if (response.startsWith("</div>", i))
                    {
                        --divLevel;

                        if (divLevel == 0)
                        {
                            AppLog.wtf(TAG, "Unexpected closure for tag div | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            return -1;
                        }
                    }

                    ++i;
                }

                while (i < response.length())
                {
                    if (response.startsWith("var product = {", i))
                    {
                        int index2 = response.indexOf('}', i + 15);

                        if (index2 < 0)
                        {
                            AppLog.wtf(TAG, "Failed to get good metadata from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            return -1;
                        }

                        String goodMetaData = response.substring(i + 14, index2 + 1);
                        JSONObject jsonObject = new JSONObject(goodMetaData);

                        goodId      = jsonObject.getInt(   "productId");
                        goodImageId = jsonObject.getInt(   "id");
                        goodCost    = jsonObject.getDouble("price");
                        goodBrand   = jsonObject.getString("brand");

                        i = index2 + 1;
                        break;
                    }
                    else
                    if (response.startsWith("<div", i))
                    {
                        ++divLevel;
                    }
                    else
                    if (response.startsWith("</div>", i))
                    {
                        --divLevel;

                        if (divLevel == 0)
                        {
                            AppLog.wtf(TAG, "Unexpected closure for tag div | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            return -1;
                        }
                    }

                    ++i;
                }

                while (i < response.length())
                {
                    if (response.startsWith("<div class=\"product_weight\">", i))
                    {
                        int index2 = response.indexOf("</div>", i + 28);

                        if (index2 < 0)
                        {
                            AppLog.wtf(TAG, "Failed to get good weight from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                            return -1;
                        }

                        String weight = response.substring(i + 28, index2).replace("<span>", "").replace("</span>", "").replace("кг", "").trim().replace(',', '.');

                        if (goodUnit == 0 || goodUnitType == MainDatabase.UNIT_TYPE_NOTHING)
                        {
                            goodUnit = Double.parseDouble(weight);
                            goodUnitType = MainDatabase.UNIT_TYPE_KILOGRAM;
                        }

                        i = index2 + 27;
                    }
                    else
                    if (response.startsWith("<div class=\"quantity_section\">", i))
                    {
                        ++divLevel;

                        while (i < response.length())
                        {
                            if (response.startsWith("class=\"header\">", i))
                            {
                                int index2 = response.indexOf('<', i + 15);

                                if (index2 < 0)
                                {
                                    AppLog.wtf(TAG, "Failed to get good count increment from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                                    return -1;
                                }

                                String header = response.substring(i + 15, index2);

                                switch (header)
                                {
                                    case "Количество":
                                        goodCountType = MainDatabase.UNIT_TYPE_ITEMS;

                                        if (goodUnit == 0 || goodUnitType == MainDatabase.UNIT_TYPE_NOTHING)
                                        {
                                            goodUnit     = 1;
                                            goodUnitType = MainDatabase.UNIT_TYPE_ITEMS;
                                        }
                                    break;

                                    case "Вес":
                                        goodCountType = MainDatabase.UNIT_TYPE_KILOGRAM;

                                        goodUnit     = 1;
                                        goodUnitType = MainDatabase.UNIT_TYPE_KILOGRAM;
                                    break;

                                    default:
                                        AppLog.wtf(TAG, "Unknown count type: " + header + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));
                                    break;
                                }

                                int index3 = response.indexOf("notifyAndUpdateQuantityChange(this, this.value, ", index2 + 1);

                                if (index3 < 0)
                                {
                                    AppLog.wtf(TAG, "Failed to get good count increment from line: " + response.substring(index2, index2 + 30) + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                                    return -1;
                                }

                                int index4 = response.indexOf(',', index3 + 48);

                                if (index4 < 0)
                                {
                                    AppLog.wtf(TAG, "Failed to get good count increment from line: " + response.substring(index3, index3 + 30) + " | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                                    return -1;
                                }

                                String increment = response.substring(index3 + 48, index4);
                                goodCountIncrement = Double.parseDouble(increment);

                                if (goodCountIncrement > 1)
                                {
                                    goodCountIncrement = 1;
                                }

                                goodEnabled = MainDatabase.ENABLED;

                                i = index4;
                                break;
                            }
                            else
                            if (response.startsWith("<div class=\"product-unavailable-text\">", i))
                            {
                                goodCountType      = MainDatabase.UNIT_TYPE_ITEMS;
                                goodCountIncrement = 1;
                                goodEnabled        = MainDatabase.DISABLED;

                                if (goodUnit == 0 || goodUnitType == MainDatabase.UNIT_TYPE_NOTHING)
                                {
                                    goodUnit     = 1;
                                    goodUnitType = MainDatabase.UNIT_TYPE_ITEMS;
                                }

                                break;
                            }
                            else
                            if (response.startsWith("<div", i))
                            {
                                ++divLevel;
                            }
                            else
                            if (response.startsWith("</div>", i))
                            {
                                --divLevel;

                                if (divLevel == 0)
                                {
                                    AppLog.wtf(TAG, "Unexpected closure for tag div | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex));

                                    return -1;
                                }
                            }

                            ++i;
                        }
                    }
                    else
                    if (response.startsWith("<div", i))
                    {
                        ++divLevel;
                    }
                    else
                    if (response.startsWith("</div>", i))
                    {
                        --divLevel;

                        if (divLevel == 0)
                        {
                            break;
                        }
                    }

                    ++i;
                }

                index = i;

                if (goodId != MainDatabase.SPECIAL_ID_NONE)
                {
                    if (
                        TextUtils.isEmpty(goodName)
                        ||
                        goodImageId        == 0
                        ||
                        goodCost           == 0
                        ||
                        goodUnit           == 0
                        ||
                        goodUnitType       == MainDatabase.UNIT_TYPE_NOTHING
                        ||
                        goodCountIncrement == 0
                        ||
                        goodCountType      == MainDatabase.UNIT_TYPE_NOTHING
                        ||
                        TextUtils.isEmpty(goodBrand)
                       )
                    {
                        AppLog.e(TAG, String.format(Locale.US, "Parsed good with invalid data: goodId = %1$d, goodName = %2$s, goodImageId = %3$d, goodCost = %4$.2f, goodUnit = %5$.2f, goodUnitType = %6$d, goodCountIncrement = %7$.2f, goodCountType = %8$d, goodBrand = %9$s | URL: %10$s"
                                , goodId
                                , String.valueOf(goodName)
                                , goodImageId
                                , goodCost
                                , goodUnit
                                , goodUnitType
                                , goodCountIncrement
                                , goodCountType
                                , String.valueOf(goodBrand)
                                , getCatalogUrl(shop, shopId, parentCategoryId, pageIndex)
                        ));
                    }



                    boolean found = false;

                    for (int j = 0; j < goods.size(); ++j)
                    {
                        if (goods.get(j).getId() == goodId)
                        {
                            found = true;
                            break;
                        }
                    }

                    if (!found)
                    {
                        GoodEntity good = new GoodEntity();

                        good.setId(goodId);
                        good.setCategoryId(parentCategoryId);
                        good.setName(goodName);
                        good.setImageId(goodImageId);
                        good.setCost(goodCost);
                        good.setUnit(goodUnit);
                        good.setUnitType(goodUnitType);
                        good.setUpdateTime(0);
                        good.setEnabled(goodEnabled);

                        goods.add(good);
                    }
                }
                else
                {
                    AppLog.e(TAG, String.format(Locale.US, "Failed to get ID for good: goodId = %1$d, goodName = %2$s, goodImageId = %3$d, goodCost = %4$.2f, goodUnit = %5$.2f, goodUnitType = %6$d, goodCountIncrement = %7$.2f, goodCountType = %8$d, goodBrand = %9$s | URL: %10$s"
                            , goodId
                            , String.valueOf(goodName)
                            , goodImageId
                            , goodCost
                            , goodUnit
                            , goodUnitType
                            , goodCountIncrement
                            , goodCountType
                            , String.valueOf(goodBrand)
                            , getCatalogUrl(shop, shopId, parentCategoryId, pageIndex)
                    ));
                }
            } while (true);
        }
        catch (Exception e)
        {
            AppLog.e(TAG, "Failed to parse goods | URL: " + getCatalogUrl(shop, shopId, parentCategoryId, pageIndex), e);
        }

        if (pageIndex == 0)
        {
            return 1;
        }

        return 0;
    }
}
