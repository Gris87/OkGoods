using UnityEngine;
using UnityEngine.UI;

using Utils;



namespace UI.Toolbox
{
    public class ToolboxScript : MonoBehaviour
    {
        private GridLayoutGroup mGridLayoutGroup;
        private Button          mButton;
        private ColorBlock      mNormalColorBlock;
        private ColorBlock      mSelectedColorBlock;
        private float           mPreviousButtonWidth;



        /// <summary>
        /// Script starting callback.
        /// </summary>
        void Start()
        {
            DebugEx.Verbose("ToolboxScript.Start()");

            mGridLayoutGroup = GetComponent<GridLayoutGroup>();

            mButton              = null;
            mNormalColorBlock    = new ColorBlock();
            mSelectedColorBlock  = new ColorBlock();
            mPreviousButtonWidth = 0;

            mNormalColorBlock.normalColor        = new Color(1f,   1f,   1f,   1f);
            mNormalColorBlock.highlightedColor   = new Color(0.9f, 0.9f, 0.9f, 1f);
            mNormalColorBlock.pressedColor       = new Color(0.7f, 0.7f, 0.7f, 1f);
            mNormalColorBlock.disabledColor      = new Color(0.7f, 0.7f, 0.7f, 0.5f);
            mNormalColorBlock.colorMultiplier    = 1f;
            mNormalColorBlock.fadeDuration       = 0.1f;

            mSelectedColorBlock.normalColor      = new Color(0.5f, 0.5f, 1f,   1f);
            mSelectedColorBlock.highlightedColor = new Color(0.4f, 0.4f, 0.9f, 1f);
            mSelectedColorBlock.pressedColor     = new Color(0.2f, 0.2f, 0.7f, 1f);
            mSelectedColorBlock.disabledColor    = new Color(0.2f, 0.2f, 0.7f, 0.5f);
            mSelectedColorBlock.colorMultiplier  = 1f;
            mSelectedColorBlock.fadeDuration     = 0.1f;



            for (int i = 0; i < transform.childCount; ++i)
            {
                Button button = transform.GetChild(i).GetComponent<Button>();

                if (button != null)
                {
                    if (mButton == null)
                    {
                        SelectButton(button);
                    }
                    else
                    {
                        button.colors = mNormalColorBlock;
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
            float buttonWidth = (toolBoxBounds.max.x - toolBoxBounds.min.x - 8 * (mGridLayoutGroup.constraintCount - 1)) / mGridLayoutGroup.constraintCount;

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
        public void SelectButton(Button button)
        {
            if (mButton != null)
            {
                mButton.colors = mNormalColorBlock;
            }

            mButton = button;

            if (mButton != null)
            {
                mButton.colors = mSelectedColorBlock;
            }
        }
    }
}
