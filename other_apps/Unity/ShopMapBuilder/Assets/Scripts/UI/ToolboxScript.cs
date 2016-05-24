using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;



public class ToolboxScript : UIBehaviour
{
    private GridLayoutGroup mGridLayoutGroup;
    private float           mPreviouseButtonWidth;



	// Use this for initialization
	void Start()
    {
        mGridLayoutGroup = GetComponent<GridLayoutGroup>();

        mPreviouseButtonWidth = 0;
	}
	
    /// <summary>
    /// Raises the rect transform dimensions change event.
    /// </summary>
    void Update()
    {
        Bounds toolBoxBounds = RectTransformUtility.CalculateRelativeRectTransformBounds(transform);
        float buttonWidth = (toolBoxBounds.max.x - toolBoxBounds.min.x - 8) / 2;

        Debug.LogError(buttonWidth);

        if (mPreviouseButtonWidth != buttonWidth)
        {
            mPreviouseButtonWidth = buttonWidth;

            mGridLayoutGroup.cellSize = new Vector2(buttonWidth, buttonWidth);
        }
	}
}
