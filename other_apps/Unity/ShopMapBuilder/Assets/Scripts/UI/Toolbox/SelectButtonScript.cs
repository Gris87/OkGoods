using UnityEngine;

using Utils;



namespace UI.Toolbox
{
    /// <summary>
    /// Select button script.
    /// </summary>
    public class SelectButtonScript : CustomToolButtonScript
    {
        /// <summary>
        /// Raises the button clicked event.
        /// </summary>
        public override void OnButtonClicked()
        {
            base.OnButtonClicked();

            DebugEx.Verbose("SelectButtonScript.OnButtonClicked()");
        }
    }
}
