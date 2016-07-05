package ru.okmarket.okgoods.util;

import java.util.ArrayList;
import java.util.Stack;

import ru.okmarket.okgoods.db.entities.GoodsCategoryEntity;

public class Utils
{
    @SuppressWarnings("unused")
    private static final String TAG = "Utils";



    public static Tree<GoodsCategoryEntity> buildCategoriesTreeFromList(ArrayList<GoodsCategoryEntity> categories, GoodsCategoryEntity rootCategory)
    {
        Tree<GoodsCategoryEntity> res = new Tree<>(rootCategory);

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
}
