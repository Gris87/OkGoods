package ru.okmarket.okgoods.net;

import java.net.URLEncoder;
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

    public static void getCatalogItemsFromResponse(String response, int parentCategoryId, ArrayList<GoodsCategoryEntity> categories, ArrayList<GoodEntity> goods)
    {
        try
        {
            int index = -1;

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
                            AppLog.wtf(TAG, "Failed to get category image name from line: " + response.substring(i, i + 30));

                            return;
                        }

                        imageName = response.substring(i + 10, index2);

                        if (imageName.regionMatches(true, 0, "/wcsstore/OKMarketCAS/", 0, 22))
                        {
                            imageName = URLEncoder.encode(imageName.substring(22), "UTF-8").replaceAll("%2F", "/").replaceAll("\\+", "%20");
                        }
                        else
                        {
                            AppLog.wtf(TAG, "Invalid image url: " + imageName);

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
                            AppLog.wtf(TAG, "Unexpected closure for tag div");

                            return;
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
                            AppLog.wtf(TAG, "Failed to get category link from line: " + response.substring(i, i + 30));

                            return;
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
                                    AppLog.wtf(TAG, "Failed to get category id from category link: " + categoryLink);

                                    return;
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
                            AppLog.wtf(TAG, "Unexpected closure for tag div");

                            return;
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
                            AppLog.wtf(TAG, "Failed to get category name from line: " + response.substring(i, i + 30));

                            return;
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
                            AppLog.wtf(TAG, "Unexpected closure for tag div");

                            return;
                        }
                    }

                    ++i;
                }

                index = i;

                if (categoryId != MainDatabase.SPECIAL_ID_NONE)
                {
                    boolean good = true;

                    for (int j = 0; j < categories.size(); ++j)
                    {
                        if (categories.get(j).getId() == categoryId)
                        {
                            good = false;
                            break;
                        }
                    }

                    if (good)
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
            AppLog.e(TAG, "Failed to parse categories", e);
        }
    }
}
