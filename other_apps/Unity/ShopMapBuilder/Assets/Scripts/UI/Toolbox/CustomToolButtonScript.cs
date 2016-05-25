using UnityEngine;
using UnityEngine.UI;

using Utils;



namespace UI.Toolbox
{
    public class CustomToolButtonScript : MonoBehaviour
    {
        private ToolboxScript mToolboxScript;
        private Button        mButton;



        /// <summary>
        /// Script starting callback.
        /// </summary>
        public virtual void Start()
        {
            DebugEx.VeryVerbose("CustomToolButtonScript.Start()");

            mToolboxScript = transform.parent.GetComponent<ToolboxScript>();
            mButton        = GetComponent<Button>();
        }

        /// <summary>
        /// Raises the button clicked event.
        /// </summary>
        public virtual void OnButtonClicked()
        {
            DebugEx.VeryVerbose("CustomToolButtonScript.OnButtonClicked()");

            mToolboxScript.SelectButton(mButton);
        }
    }
}
