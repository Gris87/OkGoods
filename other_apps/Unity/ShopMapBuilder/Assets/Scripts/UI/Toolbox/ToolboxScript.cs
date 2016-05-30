using UnityEngine;
using UnityEngine.UI;

using Utils;



namespace UI.Toolbox
{
    public class ToolboxScript : MonoBehaviour
    {
        private GridLayoutGroup        mGridLayoutGroup;
        private CustomToolButtonScript mButton;
        private float                  mPreviousButtonWidth;



        /// <summary>
        /// Script starting callback.
        /// </summary>
        void Start()
        {
            DebugEx.Verbose("ToolboxScript.Start()");

            mGridLayoutGroup = GetComponent<GridLayoutGroup>();

            mButton              = null;
            mPreviousButtonWidth = 0;



            for (int i = 0; i < transform.childCount; ++i)
            {
                CustomToolButtonScript button = transform.GetChild(i).GetComponent<CustomToolButtonScript>();

                if (button != null)
                {
                    if (mButton == null)
                    {
                        SelectButton(button);
                    }
                    else
                    {
                        button.OnDeacivate();
                    }
                }
            }
    	}
    	
        /// <summary>
        /// Update is called once per frame.
        /// </summary>
        void Update()
        {
            DebugEx.VeryVeryVerbose("ToolboxScript.Update()");
            
            Bounds toolBoxBounds = RectTransformUtility.CalculateRelativeRectTransformBounds(transform);
            float  buttonWidth   = (toolBoxBounds.max.x - toolBoxBounds.min.x - 8 * (mGridLayoutGroup.constraintCount - 1)) / mGridLayoutGroup.constraintCount;

            if (mPreviousButtonWidth != buttonWidth)
            {
                mPreviousButtonWidth = buttonWidth;

                mGridLayoutGroup.cellSize = new Vector2(buttonWidth, buttonWidth);
            }
    	}

        /// <summary>
        /// Selects the button.
        /// </summary>
        /// <param name="button">Button.</param>
        public void SelectButton(CustomToolButtonScript button)
        {
            if (mButton != button)
            {
                if (mButton != null)
                {
                    mButton.OnDeacivate();
                }

                mButton = button;

                if (mButton != null)
                {
                    mButton.OnAcivate();
                }
            }
        }
    }
}
