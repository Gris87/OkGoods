using UnityEngine;
using UnityEngine.UI;

using Utils;



namespace UI.Toolbox
{
    public class ToolboxScript : MonoBehaviour
    {
        private GridLayoutGroup mGridLayoutGroup;
        private float           mPreviouseButtonWidth;



        // Use this for initialization
        void Start()
        {
            DebugEx.Verbose("ToolboxScript.Start()");

            mGridLayoutGroup = GetComponent<GridLayoutGroup>();

            mPreviouseButtonWidth = 0;
    	}
    	
        /// <summary>
        /// Raises the rect transform dimensions change event.
        /// </summary>
        void Update()
        {
            DebugEx.VeryVeryVerbose("ToolboxScript.Update()");
            
            Bounds toolBoxBounds = RectTransformUtility.CalculateRelativeRectTransformBounds(transform);
            float buttonWidth = (toolBoxBounds.max.x - toolBoxBounds.min.x - 8 * (mGridLayoutGroup.constraintCount - 1)) / mGridLayoutGroup.constraintCount;

            if (mPreviouseButtonWidth != buttonWidth)
            {
                mPreviouseButtonWidth = buttonWidth;

                mGridLayoutGroup.cellSize = new Vector2(buttonWidth, buttonWidth);
            }
    	}
    }
}
