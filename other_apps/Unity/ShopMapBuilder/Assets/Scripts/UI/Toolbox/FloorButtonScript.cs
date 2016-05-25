using UnityEngine;

using Utils;



namespace UI.Toolbox
{
    /// <summary>
    /// Floor button script.
    /// </summary>
    public class FloorButtonScript : CustomToolButtonScript
    {
        /// <summary>
        /// Raises the button clicked event.
        /// </summary>
        public override void OnButtonClicked()
        {
            base.OnButtonClicked();

            DebugEx.Verbose("FloorButtonScript.OnButtonClicked()");
        }
    }
}
