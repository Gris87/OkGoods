using UnityEngine;

using Utils;



namespace UI.Toolbox
{
    /// <summary>
    /// Rack button script.
    /// </summary>
    public class RackButtonScript : CustomToolButtonScript
    {
        /// <summary>
        /// Raises the button clicked event.
        /// </summary>
        public override void OnButtonClicked()
        {
            base.OnButtonClicked();

            DebugEx.Verbose("RackButtonScript.OnButtonClicked()");
        }
    }
}
