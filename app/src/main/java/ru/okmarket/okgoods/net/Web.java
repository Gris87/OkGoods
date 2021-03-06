package ru.okmarket.okgoods.net;

import android.text.TextUtils;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.db.entities.GoodEntity;
import ru.okmarket.okgoods.db.entities.GoodsCategoryEntity;
import ru.okmarket.okgoods.util.AppLog;

public final class Web
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "Web";
    // endregion



    // region URL parameters
    private static final String OK_MARKET_RU_URL     = "http://okmarket.ru";
    private static final String OKEY_DOSTAVKA_RU_URL = "https://www.okeydostavka.ru";

    @SuppressWarnings("PublicStaticArrayField")
    public static final int[] OKEY_DOSTAVKA_RU_SHOP_IDS = {
              10653
            , 10653
            , 10653
            , 10653
            , 10653
            , 10653
            , 10151
            , 10151
            , 10151
            , 10151
            , 10151
    };

    @SuppressWarnings("PublicStaticArrayField")
    public static final int[] OKEY_DOSTAVKA_RU_SHOP_FFC_IDS = {
              13156
            , 13651
            , 13157
            , 13158
            , 13159
            , 13160
            , 13151
            , 13152
            , 13153
            , 13154
            , 13155
    };

    @SuppressWarnings("PublicStaticArrayField")
    public static final String[] OKEY_DOSTAVKA_RU_SHOP_GROUPS = {
              "spb1"
            , "spb1"
            , "spb1"
            , "spb1"
            , "spb1"
            , "spb1"
            , "msk1"
            , "msk2"
            , "msk3"
            , "msk4"
            , "msk5"
    };
    // endregion



    // region Parser constants
    private static int sPageStartPoint = 292316;
    // endregion



    // region firstPage values
    public static final int FIRST_PAGE = 0;
    public static final int HUGE_PAGE  = 1;
    // endregion
    // endregion



    private Web()
    {
        // Nothing
    }

    public static String getShopUrl(int shopId)
    {
        return OK_MARKET_RU_URL + "/stores/" + shopId + '/';
    }

    public static ArrayList<String> getShopPhotosUrlsFromResponse(String response)
    {
        ArrayList<String> res = new ArrayList<>(0);

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

    public static String getCatalogUrl(String shop, int shopId, int ffcId, int categoryId, int firstPage)
    {
        if (categoryId == MainDatabase.SPECIAL_ID_ROOT)
        {
            return OKEY_DOSTAVKA_RU_URL + '/' + shop.substring(0, 3) + "/catalog";
        }
        else
        {
            if (firstPage == FIRST_PAGE)
            {
                return OKEY_DOSTAVKA_RU_URL + "/webapp/wcs/stores/servlet/CategoryDisplay?" +
                        "storeId="    + shopId     + '&' +
                        "ffcId="      + ffcId      + '&' +
                        "categoryId=" + categoryId + '&' +
                        "pageView=grid";
            }
            else
            {
                return OKEY_DOSTAVKA_RU_URL + "/webapp/wcs/stores/servlet/ProductListingView?" +
                        "storeId="    + shopId     + '&' +
                        "ffcId="      + ffcId      + '&' +
                        "categoryId=" + categoryId + '&' +
                        "resultsPerPage=1000000";
            }
        }
    }

    public static String getCategoryPhotoUrl(String fileName)
    {
        return OKEY_DOSTAVKA_RU_URL + "/wcsstore/OKMarketCAS/" + fileName;
    }

    public static String getGoodPhotoThumbnailUrl(int imageId)
    {
        return OKEY_DOSTAVKA_RU_URL + "/wcsstore/OKMarketCAS/cat_entries/" + imageId + '/' + imageId + "_thumbnail.jpg";
    }

    public static String getGoodPhotoFullImageUrl(int imageId)
    {
        return OKEY_DOSTAVKA_RU_URL + "/wcsstore/OKMarketCAS/cat_entries/" + imageId + '/' + imageId + "_fullimage.jpg" ;
    }

    public static boolean getCatalogItemsFromResponse(String response, ArrayList<GoodsCategoryEntity> categories, ArrayList<GoodEntity> goods, String shop, int shopId, int ffcId, int parentCategoryId, int firstPage)
    {
        int startPoint = 0;

        // region Parse categories
        if (firstPage == FIRST_PAGE)
        {
            //noinspection ConstantValueVariableUse
            startPoint = findStartPoint(response, shop, shopId, ffcId, parentCategoryId, firstPage);

            if (startPoint < 0)
            {
                return false;
            }

            if (!parseCategories(response, categories, startPoint, shop, shopId, ffcId, parentCategoryId, firstPage))
            {
                return false;
            }
        }
        // endregion



        // region Parse goods
        if (!parseGoods(response, goods, startPoint, shop, shopId, ffcId, parentCategoryId, firstPage))
        {
            return false;
        }
        // endregion



        // region Check for pages
        if (firstPage == FIRST_PAGE)
        {
            try
            {
                int index = response.length() - 13;

                while (index > startPoint)
                {
                    if (response.startsWith("{pageNumber:\"", index))
                    {
                        break;
                    }

                    --index;
                }

                return index > startPoint;
            }
            catch (Exception e)
            {
                //noinspection ConstantValueVariableUse
                AppLog.e(TAG, "Failed to get pages count | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage), e);
            }
        }
        // endregion



        return false;
    }

    private static int findStartPoint(String response, String shop, int shopId, int ffcId, int parentCategoryId, int firstPage)
    {
        int res = response.indexOf("<div class=\"rowContainer\"", sPageStartPoint);

        if (res < 0)
        {
            res = response.lastIndexOf("<div class=\"rowContainer\"", sPageStartPoint);

            if (res >= 0)
            {
                AppLog.wtf(TAG, "Please move page start point to " + res + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                sPageStartPoint = res;
            }
            else
            {
                AppLog.wtf(TAG, "Failed to find start point for parsing categories and goods | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                return -1;
            }
        }

        return res + 25;
    }

    private static boolean parseCategories(String response, ArrayList<GoodsCategoryEntity> categories, int startPoint, String shop, int shopId, int ffcId, int parentCategoryId, int firstPage)
    {
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



                int i = index + 46;

                int    categoryId;
                String categoryName;
                String imageName;



                Pair<Integer, String> pair = parseCategoryImageName(response, i, shop, shopId, ffcId, parentCategoryId, firstPage);

                if (pair == null)
                {
                    return false;
                }

                i         = pair.first;
                imageName = pair.second;



                Pair<Integer, Integer> pair2 = parseCategoryId(response, i, shop, shopId, ffcId, parentCategoryId, firstPage);

                if (pair2 == null)
                {
                    return false;
                }

                i          = pair2.first;
                categoryId = pair2.second;



                Pair<Integer, String> pair3 = parseCategoryName(response, i, shop, shopId, ffcId, parentCategoryId, firstPage);

                if (pair3 == null)
                {
                    return false;
                }

                i            = pair3.first;
                categoryName = pair3.second;



                index = i;

                if (categoryId != MainDatabase.SPECIAL_ID_NONE)
                {
                    boolean absent = true;

                    for (int j = 0; j < categories.size(); ++j)
                    {
                        if (categories.get(j).getId() == categoryId)
                        {
                            absent = false;

                            break;
                        }
                    }

                    if (absent)
                    {
                        GoodsCategoryEntity category = GoodsCategoryEntity.newInstance();

                        category.setId(categoryId);
                        category.setParentId(parentCategoryId);
                        category.setName(categoryName);
                        category.setImageName(imageName);
                        category.setPriority(categories.size() + 1);
                        category.setUpdateTime(0);
                        category.setEnabled(MainDatabase.ENABLED);
                        category.setExpanded(false);

                        categories.add(category);
                    }
                }
            } while (true);
        }
        catch (Exception e)
        {
            AppLog.e(TAG, "Failed to parse categories | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage), e);

            return false;
        }



        return true;
    }

    private static Pair<Integer, String> parseCategoryImageName(String response, int pos, String shop, int shopId, int ffcId, int parentCategoryId, int firstPage) throws UnsupportedEncodingException
    {
        String res = "";



        int i = pos;

        while (i < response.length())
        {
            if (response.startsWith("<img src=\"", i))
            {
                int index = response.indexOf('\"', i + 10);

                if (index < 0)
                {
                    AppLog.wtf(TAG, "Failed to get category image name from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }

                res = response.substring(i + 10, index);

                if (res.regionMatches(true, 0, "/wcsstore/OKMarketCAS/", 0, 22))
                {
                    res = URLEncoder.encode(res.substring(22), "UTF-8").replaceAll("%2F", "/").replaceAll("\\+", "%20");
                }
                else
                {
                    AppLog.wtf(TAG, "Invalid image url: " + res + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    res = "";
                }

                i = index + 1;

                break;
            }
            else
            if (response.startsWith("</div>", i))
            {
                if (!response.startsWith("</a>", i - 4))
                {
                    AppLog.wtf(TAG, "Unexpected closure for tag div | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }
            }

            ++i;
        }



        return new Pair<>(i, res);
    }

    private static Pair<Integer, Integer> parseCategoryId(String response, int pos, String shop, int shopId, int ffcId, int parentCategoryId, int firstPage)
    {
        int res = MainDatabase.SPECIAL_ID_NONE;



        int i = pos;

        while (i < response.length())
        {
            if (response.startsWith("href=\"", i))
            {
                int index = response.indexOf('\"', i + 6);

                if (index < 0)
                {
                    AppLog.wtf(TAG, "Failed to get category link from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }

                String categoryLink  = response.substring(i + 6, index);
                String categoryIdStr = null;

                if (categoryLink.endsWith("-20"))
                {
                    int index2 = categoryLink.lastIndexOf('-', categoryLink.length() - 4);

                    if (index2 >= 0)
                    {
                        categoryIdStr = categoryLink.substring(index2 + 1, categoryLink.length() - 3);
                    }
                }
                else
                {
                    int index2 = categoryLink.indexOf("categoryId=");

                    if (index2 >= 0)
                    {
                        int index3 = categoryLink.indexOf("&amp;", index2 + 11);

                        if (index3 < 0)
                        {
                            AppLog.wtf(TAG, "Failed to get category id from category link: " + categoryLink + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                            return null;
                        }

                        categoryIdStr = categoryLink.substring(index2 + 11, index3);
                    }
                }

                try
                {
                    res = Integer.parseInt(categoryIdStr);
                }
                catch (Exception ignored)
                {
                    // Nothing
                }

                i = index + 1;

                break;
            }
            else
            if (response.startsWith("</div>", i))
            {
                if (!response.startsWith("</a>", i - 4))
                {
                    AppLog.wtf(TAG, "Unexpected closure for tag div | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }
            }

            ++i;
        }



        return new Pair<>(i, res);
    }

    private static Pair<Integer, String> parseCategoryName(String response, int pos, String shop, int shopId, int ffcId, int parentCategoryId, int firstPage)
    {
        String res = "";



        int i = pos;

        while (i < response.length())
        {
            if (response.charAt(i) == '>')
            {
                int index = response.indexOf('<', i + 1);

                if (index < 0)
                {
                    AppLog.wtf(TAG, "Failed to get category name from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }

                res = response.substring(i + 1, index);

                i = index + 1;

                break;
            }
            else
            if (response.startsWith("</div>", i))
            {
                if (!response.startsWith("</a>", i - 4))
                {
                    AppLog.wtf(TAG, "Unexpected closure for tag div | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }
            }

            ++i;
        }



        return new Pair<>(i, res);
    }

    private static boolean parseGoods(String response, ArrayList<GoodEntity> goods, int startPoint, String shop, int shopId, int ffcId, int parentCategoryId, int firstPage)
    {
        try
        {
            int index = startPoint;   // TODO: Start point for goods?

            do
            {
                index = response.indexOf("<div class=\"product ok-theme\">", index + 1);

                if (index < 0)
                {
                    break;
                }



                int i = index + 30;
                int divLevel = 1;

                int    goodId;
                String goodName;
                int    goodImageId;
                double goodCost;
                double goodUnit;
                int    goodUnitType;
                double goodCountIncrement;
                int    goodCountType;
                String goodBrand;
                int    goodEnabled;



                Pair<Integer, Pair<Integer, String>> triple = parseGoodName(response, i, divLevel, shop, shopId, ffcId, parentCategoryId, firstPage);

                if (triple == null)
                {
                    return false;
                }

                i        = triple.first;
                divLevel = triple.second.first;
                goodName = triple.second.second;



                Pair<Integer, Pair<Integer, Pair<Integer, Pair<Integer, Pair<Double, String>>>>> dataSet = parseGoodIdImageIdCostBrand(response, i, divLevel, shop, shopId, ffcId, parentCategoryId, firstPage);

                if (dataSet == null)
                {
                    return false;
                }

                i           = dataSet.first;
                divLevel    = dataSet.second.first;
                goodId      = dataSet.second.second.first;
                goodImageId = dataSet.second.second.second.first;
                goodCost    = dataSet.second.second.second.second.first;
                goodBrand   = dataSet.second.second.second.second.second;



                Pair<Integer, Pair<Integer, Pair<Double, Pair<Integer, Pair<Double, Pair<Integer, Integer>>>>>> dataSet2 = parseGoodUnitUnitTypeCountIncrementCountTypeEnabled(response, i, divLevel, shop, shopId, ffcId, parentCategoryId, firstPage);

                if (dataSet2 == null)
                {
                    return false;
                }

                i                  = dataSet2.first;
                //noinspection UnusedAssignment
                divLevel           = dataSet2.second.first;
                goodUnit           = dataSet2.second.second.first;
                goodUnitType       = dataSet2.second.second.second.first;
                goodCountIncrement = dataSet2.second.second.second.second.first;
                goodCountType      = dataSet2.second.second.second.second.second.first;
                goodEnabled        = dataSet2.second.second.second.second.second.second;



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
                                , getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage)
                        ));
                    }



                    boolean absent = true;

                    for (int j = 0; j < goods.size(); ++j)
                    {
                        if (goods.get(j).getId() == goodId)
                        {
                            absent = false;

                            break;
                        }
                    }

                    if (absent)
                    {
                        JSONObject goodAttrs = new JSONObject();
                        goodAttrs.put(GoodEntity.ATTRIBUTE_BRAND, goodBrand);



                        GoodEntity good = GoodEntity.newInstance();

                        good.setId(goodId);
                        good.setCategoryId(parentCategoryId);
                        good.setName(goodName);
                        good.setImageId(goodImageId);
                        good.setCost(goodCost);
                        good.setUnit(goodUnit);
                        good.setUnitType(goodUnitType);
                        good.setCountIncrement(goodCountIncrement);
                        good.setCountType(goodCountType);
                        good.setAttrs(goodAttrs);
                        good.setAttrsDetails(null);
                        good.setPriority(goods.size() + 1);
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
                            , getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage)
                    ));
                }
            } while (true);
        }
        catch (Exception e)
        {
            AppLog.e(TAG, "Failed to parse goods | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage), e);

            return false;
        }



        return true;
    }

    private static Pair<Integer, Pair<Integer, String>> parseGoodName(String response, int pos, int level, String shop, int shopId, int ffcId, int parentCategoryId, int firstPage)
    {
        String res = "";



        int i        = pos;
        int divLevel = level;

        while (i < response.length())
        {
            if (response.startsWith("<div class=\"product_name\">", i))
            {
                ++divLevel;

                int index = response.indexOf("title=\"", i + 26);

                if (index < 0)
                {
                    AppLog.wtf(TAG, "Failed to get good name from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }

                int index2 = response.indexOf("onclick=\"", index + 7);

                if (index2 < 0)
                {
                    AppLog.wtf(TAG, "Failed to get good name from line: " + response.substring(index, index + 30) + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }

                res = response.substring(index + 7, index2).trim();

                if (!res.isEmpty() && res.charAt(res.length() - 1) == '\"')
                {
                    res = res.substring(0, res.length() - 1);
                }
                else
                {
                    AppLog.wtf(TAG, "Failed to get good name from line: " + response.substring(index, index + 30) + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }

                i = index2 + 9;

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
                    AppLog.wtf(TAG, "Unexpected closure for tag div | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }
            }

            ++i;
        }



        return new Pair<>(i, new Pair<>(divLevel, res));
    }

    private static Pair<Integer, Pair<Integer, Pair<Integer, Pair<Integer, Pair<Double, String>>>>> parseGoodIdImageIdCostBrand(String response, int pos, int level, String shop, int shopId, int ffcId, int parentCategoryId, int firstPage) throws JSONException
    {
        int    goodId      = MainDatabase.SPECIAL_ID_NONE;
        int    goodImageId = 0;
        double goodCost    = 0;
        String goodBrand   = null;



        int i        = pos;
        int divLevel = level;

        while (i < response.length())
        {
            if (response.startsWith("var product = {", i))
            {
                int index = response.indexOf('}', i + 15);

                if (index < 0)
                {
                    AppLog.wtf(TAG, "Failed to get good metadata from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }

                String goodMetaData = response.substring(i + 14, index + 1);
                JSONObject jsonObject = new JSONObject(goodMetaData);

                goodId      = jsonObject.getInt(   "productId");
                goodImageId = jsonObject.getInt(   "id");
                goodCost    = jsonObject.getDouble("price");
                goodBrand   = jsonObject.getString("brand");

                i = index + 1;

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
                    AppLog.wtf(TAG, "Unexpected closure for tag div | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }
            }

            ++i;
        }



        return new Pair<>(i, new Pair<>(divLevel, new Pair<>(goodId, new Pair<>(goodImageId, new Pair<>(goodCost, goodBrand)))));
    }

    private static Pair<Integer, Pair<Integer, Pair<Double, Pair<Integer, Pair<Double, Pair<Integer, Integer>>>>>> parseGoodUnitUnitTypeCountIncrementCountTypeEnabled(String response, int pos, int level, String shop, int shopId, int ffcId, int parentCategoryId, int firstPage)
    {
        double goodUnit           = 0;
        int    goodUnitType       = MainDatabase.UNIT_TYPE_NOTHING;
        double goodCountIncrement = 0;
        int    goodCountType      = MainDatabase.UNIT_TYPE_NOTHING;
        int    goodEnabled        = MainDatabase.DISABLED;



        int i        = pos;
        int divLevel = level;

        while (i < response.length())
        {
            if (response.startsWith("<div class=\"product_weight\">", i))
            {
                int index = response.indexOf("</div>", i + 28);

                if (index < 0)
                {
                    AppLog.wtf(TAG, "Failed to get good weight from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }

                String weight = response.substring(i + 28, index).replace("<span>", "").replace("</span>", "").replace("кг", "").trim().replace(',', '.');

                //noinspection ConstantConditions
                if (goodUnit == 0 || goodUnitType == MainDatabase.UNIT_TYPE_NOTHING)
                {
                    goodUnit = Double.parseDouble(weight);
                    goodUnitType = MainDatabase.UNIT_TYPE_KILOGRAM;
                }

                i = index + 6;
            }
            else
            if (response.startsWith("<div class=\"quantity_section\">", i))
            {
                ++divLevel;
                i += 30;



                Pair<Integer, Pair<Integer, Pair<Double, Pair<Integer, Pair<Double, Pair<Integer, Integer>>>>>> dataSet = parseGoodQuantitySection(response, i, divLevel, goodUnit, goodUnitType, shop, shopId, ffcId, parentCategoryId, firstPage);

                if (dataSet == null)
                {
                    return null;
                }

                i                  = dataSet.first;
                divLevel           = dataSet.second.first;
                goodUnit           = dataSet.second.second.first;
                goodUnitType       = dataSet.second.second.second.first;
                goodCountIncrement = dataSet.second.second.second.second.first;
                goodCountType      = dataSet.second.second.second.second.second.first;
                goodEnabled        = dataSet.second.second.second.second.second.second;
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



        return new Pair<>(i, new Pair<>(divLevel, new Pair<>(goodUnit, new Pair<>(goodUnitType, new Pair<>(goodCountIncrement, new Pair<>(goodCountType, goodEnabled))))));
    }

    @SuppressWarnings("AssignmentToMethodParameter")
    private static Pair<Integer, Pair<Integer, Pair<Double, Pair<Integer, Pair<Double, Pair<Integer, Integer>>>>>> parseGoodQuantitySection(String response, int pos, int level, double goodUnit, int goodUnitType, String shop, int shopId, int ffcId, int parentCategoryId, int firstPage)
    {
        double goodCountIncrement = 0;
        int    goodCountType      = MainDatabase.UNIT_TYPE_NOTHING;
        int    goodEnabled        = MainDatabase.DISABLED;



        int i        = pos;
        int divLevel = level;

        while (i < response.length())
        {
            if (response.startsWith("class=\"header\">", i))
            {
                int index = response.indexOf('<', i + 15);

                if (index < 0)
                {
                    AppLog.wtf(TAG, "Failed to get good count increment from line: " + response.substring(i, i + 30) + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }

                String header = response.substring(i + 15, index);

                switch (header)
                {
                    case "Количество":
                    {
                        goodCountType = MainDatabase.UNIT_TYPE_ITEMS;

                        if (goodUnit == 0 || goodUnitType == MainDatabase.UNIT_TYPE_NOTHING)
                        {
                            goodUnit = 1;
                            goodUnitType = MainDatabase.UNIT_TYPE_ITEMS;
                        }
                    }
                    break;

                    case "Вес":
                    {
                        goodCountType = MainDatabase.UNIT_TYPE_KILOGRAM;

                        goodUnit = 1;
                        goodUnitType = MainDatabase.UNIT_TYPE_KILOGRAM;
                    }
                    break;

                    default:
                    {
                        AppLog.wtf(TAG, "Unknown count type: " + header + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));
                    }
                    break;
                }

                int index2 = response.indexOf("notifyAndUpdateQuantityChange(this, this.value, ", index + 1);

                if (index2 < 0)
                {
                    AppLog.wtf(TAG, "Failed to get good count increment from line: " + response.substring(index, index + 30) + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }

                int index3 = response.indexOf(',', index2 + 48);

                if (index3 < 0)
                {
                    AppLog.wtf(TAG, "Failed to get good count increment from line: " + response.substring(index2, index2 + 30) + " | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }

                String increment   = response.substring(index2 + 48, index3);
                goodCountIncrement = Double.parseDouble(increment);

                if (goodCountIncrement > 1)
                {
                    goodCountIncrement = 1;
                }

                goodEnabled = MainDatabase.ENABLED;

                i = index3 + 1;

                break;
            }
            else
            if (response.startsWith("<div class=\"product-unavailable-text\">", i))
            {
                ++divLevel;

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
                    AppLog.wtf(TAG, "Unexpected closure for tag div | URL: " + getCatalogUrl(shop, shopId, ffcId, parentCategoryId, firstPage));

                    return null;
                }
            }

            ++i;
        }



        return new Pair<>(i, new Pair<>(divLevel, new Pair<>(goodUnit, new Pair<>(goodUnitType, new Pair<>(goodCountIncrement, new Pair<>(goodCountType, goodEnabled))))));
    }
}
