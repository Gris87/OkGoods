package ru.okmarket.okgoods.util;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import ru.okmarket.okgoods.db.entities.GoodsCategoryEntity;

public final class Utils
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "Utils";
    // endregion
    // endregion



    private Utils()
    {
        // Nothing
    }

    public static Tree<GoodsCategoryEntity> buildCategoriesTreeFromList(ArrayList<GoodsCategoryEntity> categories, GoodsCategoryEntity rootCategory)
    {
        Tree<GoodsCategoryEntity> res = Tree.newInstance(rootCategory);

        Stack<Tree<GoodsCategoryEntity>> stack = new Stack<>();
        stack.push(res);

        do
        {
            Tree<GoodsCategoryEntity> item = stack.pop();

            for (int i = 0; i < categories.size(); ++i)
            {
                GoodsCategoryEntity category = categories.get(i);

                if (category.getParentId() == item.getData().getId())
                {
                    stack.push(item.addChild(category));
                }
            }
        } while (!stack.isEmpty());

        return res;
    }

    @Nullable
    public static JSONObject mergeJSONObjects(JSONObject first, JSONObject second)
    {
        if (first != null)
        {
            if (second != null)
            {
                Iterator<String> it = second.keys();

                while (it.hasNext())
                {
                    String key = it.next();

                    try
                    {
                        first.put(key, second.get(key));
                    }
                    catch (JSONException e)
                    {
                        AppLog.e(TAG, "Failed to merge JSON objects", e);
                    }
                }

                return first;
            }
            else
            {
                return first;
            }
        }
        else
        {
            if (second != null)
            {
                return second;
            }
            else
            {
                return null;
            }
        }
    }
}
